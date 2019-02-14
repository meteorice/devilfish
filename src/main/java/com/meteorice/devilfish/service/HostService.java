package com.meteorice.devilfish.service;

import com.meteorice.devilfish.dao.HostMapper;
import com.meteorice.devilfish.pojo.Host;
import com.meteorice.devilfish.pojo.HostConfig;
import com.meteorice.devilfish.util.datetime.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *
     * @param ip
     * @return
     */
    public List<HostConfig> getHostRule(String ip) {
        return hostMapper.getHostConfig(ip);
    }


    /**
     * 获取权限
     *
     * @param ip
     * @return
     */
    public HostConfig getAuth(String ip) {
        return hostMapper.getAuth(ip);
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

    public void sshLog(String username, String hostinfo) {
        Map map = new HashMap();
        map.put("sshtime", DateTimeUtil.datatimeToTimestamp(LocalDateTime.now()));
        map.put("fieldyear", DateTimeUtil.getYear());
        map.put("fieldmonth", DateTimeUtil.getMonth());
        map.put("ddate", DateTimeUtil.getDate());
        map.put("dtime", DateTimeUtil.getTime());
        map.put("username", username);
        map.put("hostinfo", hostinfo);
        hostMapper.sshLog(map);
    }

    public List getSshLog(String fieldyear) {
        return hostMapper.getSshLog(fieldyear);
    }

    public List getSshLogDetail(String date) {
        return hostMapper.getSshLogDetail(date);
    }

    /**
     * 获得满24小时的数据
     *
     * @param date
     * @return
     */
    public List getFullHoursSshLogDetail(String date) {
        List<Map> list = getSshLogDetail(date);
        List<Integer> timelist = DateTimeUtil.getFullHoursList();
        List<Integer> res = new ArrayList<>();
        int p = -1, z = 0;
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            p = Integer.valueOf((String) map.get("time"));
            for (int j = z; j < p; j++) {
                res.add(0);
            }
            z = ++p;
            res.add(((Long) map.get("times")).intValue());
        }
        for (; p < timelist.size(); p++) {
            res.add(0);
        }
        return res;
    }

    public int getHostCount(){
        return hostMapper.getHostCount();
    }

}
