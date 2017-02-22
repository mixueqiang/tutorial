package com.yike.service;

import com.yike.model.User;
import com.yike.model.WxUser;

import java.util.List;
import java.util.Map;

/**
 * @author ilakeyc
 * @since 2017/2/20
 */
public interface WxYiKeUserService {

    List<String> getApplicationCourseNames(WxUser wxUser);

    boolean hasApplication(WxUser wxUser);

    User getBindingUser(WxUser wxUser);

    boolean updateByOpenId(String opeId, String columnName, Object columnValue);

    boolean updateByOpenId(String openId, Map<String, Object> updateValues);

    WxUser findByUserId(long mainUserId);

    WxUser findByOpenId(String openId);

    WxUser getUser(String openId);

    WxUser sync(String openId);
}
