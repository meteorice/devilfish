package com.meteorice.devilfish.service;

import com.meteorice.devilfish.dao.HostMapper;
import com.meteorice.devilfish.pojo.Host;
import com.meteorice.devilfish.pojo.HostConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class HostService {

    private Logger logger = LoggerFactory.getLogger(HostService.class);

    @Autowired
    private HostMapper hostMapper;

    public List getTreeNode(int pid) {
        return hostMapper.getTreeNode(pid);
    }

    public Host getHost(String ip) {
        return hostMapper.getHost(ip);
    }

    /**
     * 获取主机表达式
     * @param ip
     * @return
     */
    public List<HostConfig> getHostRule(String ip) {
        return hostMapper.getHostConfig(ip);
    }



    /**
     * 获取主机的配置密码,谁最接近就选谁,精确匹配的最,然后看谁的正则表达式最长就选谁
     *
     * @param ip
     * @return
     */
    public HostConfig getHostConfig(String ip) {
        List<HostConfig> list = getHostRule(ip);
        HostConfig nearlyHostConfig = null;
        for (HostConfig hostConfig : list) {
            if (StringUtils.equals(ip, hostConfig.getMatchstr())) {
                return hostConfig;
            } else if (nearlyHostConfig != null) {
                if (nearlyHostConfig.getMatchstr().length() < hostConfig.getMatchstr().length()) {
                    nearlyHostConfig = hostConfig;
                }
            } else {
                nearlyHostConfig = hostConfig;
            }
        }
        return nearlyHostConfig;
    }
}
