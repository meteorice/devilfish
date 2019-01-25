package com.meteorice.devilfish.web.contorller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/host")
public class HostController {

    private Logger logger = LoggerFactory.getLogger(HostController.class);

    @RequestMapping(value = "/getTreeNode", method = GET)
    public List getTreeNode(@RequestParam(name = "nodeId") String nodeId) {
        logger.info("传递参数：{}", nodeId);
        List list = new ArrayList();
        if ("0".equals(nodeId)) {
            list.add(new HashMap() {
                {
                    put("title", "127.0.0.1");
                    put("key", "11");
                    put("value", "127.0.0.1");
                }
            });
            list.add(new HashMap() {
                {
                    put("title", "127.0.0.2");
                    put("key", "12");
                    put("value", "127.0.0.2");
                }
            });
        }
        return list;
    }

}
