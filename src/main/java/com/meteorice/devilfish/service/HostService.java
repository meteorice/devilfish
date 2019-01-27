package com.meteorice.devilfish.service;

import com.meteorice.devilfish.dao.HostMapper;
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
}
