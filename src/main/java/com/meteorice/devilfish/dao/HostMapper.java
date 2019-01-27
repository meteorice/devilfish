package com.meteorice.devilfish.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HostMapper {

    /**
     * @param pid
     * @return
     */
    List getTreeNode(int pid);

}
