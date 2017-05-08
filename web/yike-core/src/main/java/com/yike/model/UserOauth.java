package com.yike.model;

/**
 * @author ilakeyc
 * @since 2017/3/28
 */
public class UserOauth extends BaseModel {
    public static final int PROVIDER_WECHAT = 1;
    public static final int PROVIDER_QQ = 2;
    private static final long serialVersionUID = 8508055365470520670L;
    private long userId;
    private String openId;
    private String unionId;
    private String provider;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
