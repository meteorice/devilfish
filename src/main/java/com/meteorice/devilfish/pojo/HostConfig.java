package com.meteorice.devilfish.pojo;

import org.apache.ibatis.type.Alias;

@Alias("hostConfig")
public class HostConfig {
    Integer ruleid;
    Integer timeout;
    String matchstr;
    String loginname;
    String loginpwd;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRuleid() {
        return ruleid;
    }

    public void setRuleid(Integer ruleid) {
        this.ruleid = ruleid;
    }

    public String getMatchstr() {
        return matchstr;
    }

    public void setMatchstr(String matchstr) {
        this.matchstr = matchstr;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getLoginpwd() {
        return loginpwd;
    }

    public void setLoginpwd(String loginpwd) {
        this.loginpwd = loginpwd;
    }
}
