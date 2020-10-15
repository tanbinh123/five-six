package com.fivesix.fivesixserver.controller;

import com.fivesix.fivesixserver.entity.User;
import com.fivesix.fivesixserver.result.Result;
import com.fivesix.fivesixserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {
        String requestUserName = HtmlUtils.htmlEscape(requestUser.getUsername());
        if (!userService.exist(requestUserName) || !userService.getByName(requestUserName).getPassword().equals(requestUser.getPassword())) {
            return new Result(400,"账户或密码错误");
        }else {
            return new Result(200,"登录成功");
        }
    }
}