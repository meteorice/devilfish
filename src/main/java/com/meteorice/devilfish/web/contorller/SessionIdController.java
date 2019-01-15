package com.meteorice.devilfish.web.contorller;

import com.meteorice.devilfish.util.ssh.SshManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ctrl/sessionId")
public class SessionIdController {

    private Logger log = LoggerFactory.getLogger(SessionIdController.class);

    @RequestMapping("/get")
    @ResponseBody
    public String get() {
        return SshManager.getToken();
    }
}
