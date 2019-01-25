package com.meteorice.devilfish.dao;

import com.meteorice.devilfish.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /**
     * @param username
     * @return
     */
    User findUserByName(String username);


    /**
     * 用户的数量
     * @param username
     * @return
     */
    Integer sizeOfUser(String username);

    /**
     * @param user
     */
    void addUser(User user);
}
