package com.meteorice.devilfish.dao;

import com.meteorice.devilfish.pojo.Host;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HostMapper {

    /**
     * @param pid
     * @return
     */
    List<Host> getTreeNode(int pid);


    Host getHost(String hostip);


    List getHostConfig(String ip);

}
