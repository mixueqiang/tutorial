package com.yike.service;

import com.yike.model.WxUser;

/**
 * @author ilakeyc
 * @since 2017/2/20
 */
public interface WxYiKeUserService {

  WxUser findByUserId(long mainUserId);

  WxUser findByOpenId(String openId);

  WxUser getUser(String openId);

  WxUser sync(String openId);
}
