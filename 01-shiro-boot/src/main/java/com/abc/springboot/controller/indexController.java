package com.abc.springboot.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
                e.printStackTrace();
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

    /**
     *@RequiresRoles 这个注解是shiro提供的，用于标签类或当前在访问是必须需要什么样的角色
     * 属性
     *  value取值为String 数组类型 用于指定访问时所需要的一个或多个角色名
     *  logical 取值为logical,AND或Logical.OR,当指定多个角色时可以使用AND或OR一表示并且和或的意思默认值为AND
     *          表示当前用户必须同时拥有多个角色才可以访问这个方法
     * @return
     */
    @RequiresRoles(value = {"admin"})
    @RequestMapping("/admin/test")
    public @ResponseBody String adminTest(){
        return "AdminTest请求";
    }

    @RequiresPermissions(value = "{admin:add}")
    @RequiresRoles(value = "{admin}")
    @RequestMapping("/admin/add")
    public @ResponseBody String adminadd(){
        return "Admin/add请求";
    }

    @RequiresRoles(value = "user")
    @RequestMapping("/user/test")
    public @ResponseBody String userTest(){
        return "userTest请求";
    }

    /**
     * 配置自定义异常，需要拦截AuthorizationException异常或者ShiroException异常
     * 注意：当前shiro出现权限验证失败以后会抛出异常，因此必须要写一个自定义的异常拦截
     * @return
     */
    @ExceptionHandler(value = {AuthorizationException.class})
    public String permissionError(Throwable throwable){
        System.out.println("throwable"  + throwable);
        return "noPermission";
    }
}
