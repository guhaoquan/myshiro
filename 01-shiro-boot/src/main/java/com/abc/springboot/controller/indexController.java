package com.abc.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class indexController {

    @RequestMapping("/")
    public String index(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(){
        return "redirect:success";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:/";
    }

    @RequestMapping("/success")
    public String loginSuccess(){
        return "success";
    }

    @RequestMapping("/noPermission")
    public String indexNoPermission(){
        return "noPermission";
    }

    @RequestMapping("/admin/test")
    public @ResponseBody String adminTest(){
        return "AdminTest请求";
    }

    @RequestMapping("/user/test")
    public @ResponseBody String userTest(){
        return "userTest请求";
    }
}
