package com.meteorice.devilfish.pojo;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

/**
 *
 */
@Alias("user")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 4859987312853743081L;

    //用户id
    private Integer userid;
    //用户名
    @NotBlank(message = "用户名不能为空")
    private String username;
    //密码
    @Max(value = 999999, message = "超过最大数值")
    @Min(value = 000000, message = "密码设定不正确")
    private String password;
    //状态1:正常
    private int status;
    //最后登录时间
    private Timestamp lastlogintime;
    //建立时间
    private Timestamp createtime;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // 帐户是否过期
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    // 帐户是否被冻结
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 帐号是否可用
    @Override
    public boolean isEnabled() {
        return false;
    }

    // 封装了权限信息
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Timestamp lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }
}
