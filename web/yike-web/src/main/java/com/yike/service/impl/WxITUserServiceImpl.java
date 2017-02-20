package com.yike.service.impl;

import com.yike.Constants;
import com.yike.dao.mapper.WxUserRowMapper;
import com.yike.model.Entity;
import com.yike.model.WxUser;
import com.yike.service.WxITService;
import com.yike.service.WxITUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/2/16
 */
@Service
public class WxITUserServiceImpl extends BaseService implements WxITUserService {
  private static final Log LOG = LogFactory.getLog(WxITUserServiceImpl.class);

  public boolean makeStudent(String openId) {
    return updateByOpenId(openId, "isStudent", 1);
  }

  public boolean saveInvitation(String scannedOpenId, String invterOpenId) {
    return updateByOpenId(scannedOpenId, "inviterId", invterOpenId);
  }

  public boolean saveTicket(String inviterOpenId, String ticket) {
    return updateByOpenId(inviterOpenId, "qrTicket", ticket);
  }

  public boolean hasTicket(String ticket) {
    return entityDao.exists("wx_user", "qrTicket", ticket);
  }

  public int countInvitation(String inviterOpenId) {
    return entityDao.count("wx_user", "inviterId", inviterOpenId);
  }


  public WxUser findByTicket(String ticket) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("qrTicket", ticket);
    return entityDao.findOne("wx_user", wxUserFindCondition, WxUserRowMapper.getInstance());
  }

  public WxUser findByOpenId(String openId) {
    Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
    wxUserFindCondition.put("openId", openId);
    return entityDao.findOne("wx_user", wxUserFindCondition, WxUserRowMapper.getInstance());
  }

  public WxUser getUser(String openId) {
    WxUser user = findByOpenId(openId);
    if (user == null) {
      user = sync(openId);
    }
    return user;
  }

  private boolean userExist(String openId) {
    return entityDao.exists("wx_user", "openId", openId);
  }

  private boolean updateByOpenId(String opeId, String columnName, Object columnValue) {
    Map<String, Object> updateValues = new HashMap<String, Object>();
    updateValues.put(columnName, columnValue);
    return updateByOpenId(opeId, updateValues);
  }

  private boolean updateByOpenId(String openId, Map<String, Object> updateValues) {
    Map<String, Object> updateCondition = new HashMap<String, Object>();
    updateCondition.put("openid", openId);
    try {
      entityDao.update("wx_user", updateCondition, updateValues);
      return true;
    } catch (Throwable t) {
      LOG.error("update WxUser failure", t);
    }
    return false;
  }

  public WxUser sync(String openId) {
    WxUser user = WxITService.apiUtils.requestWxUser(openId);
    if (userExist(openId)) {
      long id = update(user, openId);
      user.setId(id);
    } else {
      long id = save(user, openId);
      user.setId(id);
    }
    return user;
  }


  private long update(WxUser user, String openId) {
    Map<String, Object> updateCondition = new HashMap<String, Object>();
    updateCondition.put("openid", openId);
    Map<String, Object> updateValues = new HashMap<String, Object>();
    long updateTime = System.currentTimeMillis();
    updateValues.put("updateTime", updateTime);
    if (1 == user.getSubscribe()) {
      updateValues.put("nickName", user.getNickname());
      updateValues.put("sex", user.getSex());
      updateValues.put("language", user.getLanguage());
      updateValues.put("city", user.getCity());
      updateValues.put("province", user.getProvince());
      updateValues.put("country", user.getCountry());
      updateValues.put("headImgUrl", user.getHeadimgurl());
      updateValues.put("subscribeTime", user.getSubscribe_time() * 1000);
      updateValues.put("unionid", user.getUnionid());
      updateValues.put("remark", user.getRemark());
      updateValues.put("groupid", user.getGroupid());
      updateValues.put("status", Constants.STATUS_OK);
    } else {
      updateValues.put("status", Constants.STATUS_NO);
    }
    updateValues.put("subscribe", user.getSubscribe());
    try {
      entityDao.update("wx_user", updateCondition, updateValues);
      long id = entityDao.findOne("wx_user", updateCondition).getId();
      user.setId(id);
    } catch (Throwable t) {
      LOG.error("update WxUser failure", t);
    }
    return user.getId();
  }

  private long save(WxUser user, String openId) {
    long createTime = System.currentTimeMillis();
    Entity entity = new Entity("wx_user");
    entity.set("createTime", createTime);
    entity.set("subscribe", user.getSubscribe());
    entity.set("openid", openId);
    entity.set("isStudent", 0);
    entity.set("nickName", user.getNickname());
    entity.set("sex", user.getSex());
    entity.set("language", user.getLanguage());
    entity.set("city", user.getCity());
    entity.set("province", user.getProvince());
    entity.set("country", user.getCountry());
    entity.set("headImgUrl", user.getHeadimgurl());
    entity.set("subscribeTime", user.getSubscribe_time() * 1000);
    entity.set("unionid", user.getUnionid());
    entity.set("remark", user.getRemark());
    entity.set("groupid", user.getGroupid());
    entity.set("status", Constants.STATUS_OK);
    try {
      entity = entityDao.saveAndReturn(entity);
      return entity.getId();
    } catch (Throwable t) {
      LOG.error("save WxUser failure", t);
      return 0;
    }
  }

@Override
public boolean isStudent(String openId) {
	WxUser user = getUser(openId);
	if (user != null) {
		return user.getIsStudent() == 1;
	}
	return false;
}
}
