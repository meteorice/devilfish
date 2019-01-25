package com.meteorice.devilfish.web.contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {


    /**
     * 根目錄控制
     * @return
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    /**
     * 登陸界面
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 403錯誤界面
     * @return
     */
    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
}
