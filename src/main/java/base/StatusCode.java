package base;

import com.alibaba.fastjson.JSONObject;

public enum StatusCode {
    SUCCESS(0, "success"), FAIL(1, "fail");
    private int msg;
    private String status;

    StatusCode() {
    }

    StatusCode(int msg, String status) {
        this.msg = msg;
        this.status = status;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        JSONObject signcontainer = new JSONObject();
        signcontainer.put("msg", msg);
        signcontainer.put("status", status);
        return signcontainer.toString();
    }
}
