package com.yike.service;

import com.yike.model.WxUser;

/**
 * @author ilakeyc
 * @since 2017/2/16
 */
public interface WxUserService {

  boolean makeStudent(String openId);

  boolean saveTicket(String inviterOpenId, String ticket);

  boolean saveInvitation(String scannedOpenId, String invterOpenId);

  boolean hasTicket(String ticket);

  int countInvitation(String inviterOpenId);

  WxUser findByTicket(String ticket);

  WxUser findByOpenId(String openId);

  WxUser sync(String openId);
}
