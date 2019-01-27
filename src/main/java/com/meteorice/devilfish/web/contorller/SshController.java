package com.meteorice.devilfish.web.contorller;

import com.meteorice.devilfish.pojo.CommResult;
import com.meteorice.devilfish.util.ssh.SshManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/ssh")
public class SshController {

    private static Logger logger = LoggerFactory.getLogger(SshController.class);

    @RequestMapping(value = "/getToken", method = GET)
    public CommResult getToken() {
        try {
            return CommResult.SUCCESS(SshManager.getToken());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return CommResult.ERROR(e.getMessage());
        }
    }
}
