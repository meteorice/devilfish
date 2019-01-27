package com.meteorice.devilfish.web.contorller;

import com.meteorice.devilfish.service.HostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/host")
public class HostController {

    private Logger logger = LoggerFactory.getLogger(HostController.class);

    @Autowired
    HostService hostService;

    @RequestMapping(value = "/getTreeNode", method = GET)
    public List getTreeNode(@RequestParam(name = "nodeId") int nodeId) {
        logger.info("传递参数：{}", nodeId);
        List list = hostService.getTreeNode(nodeId);
        return list;
    }

}
