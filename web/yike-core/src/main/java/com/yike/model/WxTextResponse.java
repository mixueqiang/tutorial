package com.yike.model;

/**
 * @author ilakeyc
 * @since 2017/3/17
 */
public class WxTextResponse extends BaseModel {
    private static final long serialVersionUID = -2836276345625881475L;

    private String result;
    private String target;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
