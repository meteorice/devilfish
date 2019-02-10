package com.meteorice.devilfish.pojo;


/**
 * 返回给前端的对象
 */
public class CommResult {

    /**
     * 返回消息
     */
    public Object message = "";
    /**
     * 是否正常
     */
    public Boolean flag;


    /**
     * 成功调用
     * @param message
     * @return
     */
    public static CommResult SUCCESS(Object message) {
        return new CommResult(true,message);
    }

    /**
     * 失败调用
     * @param message
     * @return
     */
    public static CommResult ERROR(Object message) {
        return new CommResult(false,message);
    }

    private CommResult(Boolean flag, Object message) {
        this.flag = flag;
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
