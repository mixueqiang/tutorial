package com.yike.service.impl;

import com.yike.Constants;
import com.yike.dao.mapper.WxUserRowMapper;
import com.yike.model.Entity;
import com.yike.model.WxUser;
import com.yike.service.WxYiKeService;
import com.yike.service.WxYiKeUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/2/20
 */
@Service
public class WxYiKeUserServiceImpl extends BaseService implements WxYiKeUserService {
    private static final Log LOG = LogFactory.getLog(WxYiKeUserServiceImpl.class);

    public WxUser findByUserId(long mainUserId) {
        return null;
    }

    public WxUser findByOpenId(String openId) {
        Map<String, Object> wxUserFindCondition = new HashMap<String, Object>();
        wxUserFindCondition.put("openId", openId);
        WxUser user = null;
        try {
            user = entityDao.findOne("wx_yike_user", wxUserFindCondition, WxUserRowMapper.getInstance());
        } catch (Throwable t) {
            LOG.error("find WxUser by openId failure", t);

        }
        return user;
    }

    public WxUser getUser(String openId) {
        WxUser user = findByOpenId(openId);
        if (user == null) {
            user = sync(openId);
        }
        return user;
    }

    private boolean userExist(String openId) {
        return entityDao.exists("wx_yike_user", "openId", openId);
    }

    public boolean updateByOpenId(String opeId, String columnName, Object columnValue) {
        Map<String, Object> updateValues = new HashMap<String, Object>();
        updateValues.put(columnName, columnValue);
        return updateByOpenId(opeId, updateValues);
    }

    public boolean updateByOpenId(String openId, Map<String, Object> updateValues) {
        Map<String, Object> updateCondition = new HashMap<String, Object>();
        updateCondition.put("openid", openId);
        try {
            entityDao.update("wx_yike_user", updateCondition, updateValues);
            return true;
        } catch (Throwable t) {
            LOG.error("update WxUser failure", t);
        }
        return false;
    }

    public WxUser sync(String openId) {
        WxUser user = WxYiKeService.apiUtils.requestWxUser(openId);
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
            entityDao.update("wx_yike_user", updateCondition, updateValues);
            long id = entityDao.findOne("wx_yike_user", updateCondition).getId();
            user.setId(id);
        } catch (Throwable t) {
            LOG.error("update WxUser failure", t);
        }
        return user.getId();
    }

    private long save(WxUser user, String openId) {
        long createTime = System.currentTimeMillis();
        Entity entity = new Entity("wx_yike_user");
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

}
