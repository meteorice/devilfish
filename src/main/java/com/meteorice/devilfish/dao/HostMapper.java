package com.meteorice.devilfish.dao;

import com.meteorice.devilfish.pojo.Host;
import com.meteorice.devilfish.pojo.HostConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface HostMapper {

    /**
     * @param pid
     * @return
     */
    List<Host> getTreeNode(int pid);


    Host getHost(String hostip);

    HostConfig getAuth(String ip);

    List getHostConfig(String ip);

    void sshLog(Map map);

    List getSshLog(String fieldyear);

    List getSshLogDetail(String date);

    int getHostCount();

}
