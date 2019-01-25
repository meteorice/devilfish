package com.meteorice.devilfish.web.contorller;

import com.meteorice.devilfish.pojo.CommResult;
import com.meteorice.devilfish.pojo.User;
import com.meteorice.devilfish.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/findUserByName", method = GET)
    public User findUserByName(@RequestParam(value = "name") String name) {
        return userService.findUserByName(name);
    }

    @PostMapping(value = "/addUser")
    public CommResult addUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return CommResult.ERROR(bindingResult.getFieldError().getDefaultMessage());
        }

        User insertuser = new User(user.getUsername(), user.getPassword());
        return userService.addUser(insertuser);
    }
}
