package com.meteorice.devilfish.service;

import com.meteorice.devilfish.dao.UserMapper;
import com.meteorice.devilfish.pojo.CommResult;
import com.meteorice.devilfish.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService  implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByName(String username) {
        return userMapper.findUserByName(username);
    }

    public Integer sizeOfUser(String username) {
        return userMapper.sizeOfUser(username);
    }

    public CommResult addUser(User user) {
        int num = sizeOfUser(user.getUsername());
        if (num > 0) {
            return CommResult.ERROR("用户名重复");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.addUser(user);
        return CommResult.SUCCESS("用户增加成功");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("用户登录: {}", username);
        return userMapper.findUserByName(username);
    }
}
