package com.fivesix.fivesixserver.controller;

import com.fivesix.fivesixserver.entity.User;
import com.fivesix.fivesixserver.result.Result;
import com.fivesix.fivesixserver.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;


@Controller
public class LoginController {


    final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }
    @CrossOrigin
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {
        String requestUserName = HtmlUtils.htmlEscape(requestUser.getName());
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(requestUserName,requestUser.getPassword());
        try{
            subject.login(usernamePasswordToken);
            return new Result(200,"login successfully");

        } catch (AuthenticationException e) {
            return new Result(400,"账号或密码错误");
        }
    }

    @CrossOrigin
    @PostMapping("/api/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        String username = HtmlUtils.htmlEscape(user.getName());
        user.setName(username);
        //生成盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        user.setSalt(salt);
        //设置迭代次数
        int times = 2;
        //生成加密的密码
        String encodedPassword = new SimpleHash("md5",user.getPassword(),salt,times).toString();
        user.setPassword(encodedPassword);

        try {
            userService.register(user);
            return new Result(200,"register successfully.");
        }catch (Exception e) {
            return new Result(400,e.getMessage());
        }
    }
}
