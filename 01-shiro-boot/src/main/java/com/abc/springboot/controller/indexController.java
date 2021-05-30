package com.abc.springboot.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class indexController {

    @RequestMapping("/")
    public String index(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model){
        //创建一个shiro的Subject对象，利用这个对象来完成用户的登录认证
        Subject subject = SecurityUtils.getSubject();

        //退出解决缓存,进入这个请求用户一定是要完成用户登录功能,否则shiro会有缓存，否则shiro不会重新登录
        subject.logout();

        //判断当前用户是否已经认证过，如果已经认证过着不需要认证如果没有认证过则进入if完成认证
        if (!subject.isAuthenticated()){
            //创建一个用户账号和密码的Token对象，并设置用户输入的账号和面
            //这个对象将在Shiro中被获取
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);

            try {
                //例如账号不存在或密码错误等等，我们需要根据不同的异常类型来判断用户的登录状态并给与友好的信息提示
                //调用login后Shiro就会自动执行我们自定义的Realm中的认证方法
                subject.login(usernamePasswordToken);
            }catch (UnknownAccountException e){
                //e.printStackTrace();
                model.addAttribute("errorMessage","帐号错误!");
                return "login";
            }catch (LockedAccountException e){
                //e.printStackTrace();
                model.addAttribute("errorMessage","帐号被锁定!");
                return "login";
            }catch (IncorrectCredentialsException e){
                //e.printStackTrace();
                model.addAttribute("errorMessage","密码错误!");
                return "login";
            }catch (AuthenticationException e){
                e.printStackTrace();
                model.addAttribute("errorMessage","认证失败!");
                return "login";
            }


        }
        return "redirect:success";
    }

    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        //退出当前帐号，清空shiro当前用户的缓存，否则无法重新登录
        subject.logout();
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
