package com.meteorice.devilfish.web.contorller;

import com.meteorice.devilfish.pojo.CommResult;
import com.meteorice.devilfish.pojo.HostConfig;
import com.meteorice.devilfish.service.HostService;
import com.meteorice.devilfish.util.datetime.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/getAuth", method = GET)
    public CommResult getHost(@RequestParam(name = "ip") String ip) {
        return CommResult.SUCCESS(hostService.getAuth(ip));
    }

    @PostMapping(value = "setAuth")
    public CommResult setAuth(HostConfig hostConfig) {

        return CommResult.SUCCESS("");
    }

    @GetMapping(value = "/getSshLog")
    public CommResult getSshLog() {
        return CommResult.SUCCESS(hostService.getSshLog(DateTimeUtil.getYear()));
    }

    @GetMapping(value = "/getSshLogDetail")
    public CommResult getSshLogDetail(@RequestParam(name = "date") String date) {
        return CommResult.SUCCESS(hostService.getFullHoursSshLogDetail(date));
    }

}
