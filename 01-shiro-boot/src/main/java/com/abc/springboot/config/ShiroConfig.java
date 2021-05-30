package com.abc.springboot.config;

import com.abc.springboot.realm.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

//标记当前类是一个Spring的配置类，用于模拟Spring的配置文件
@Configuration
public class ShiroConfig {

    /**
     * 配置安全管理器
     *
     */
    @Bean
    public SecurityManager securityManager(Realm myRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //设置一个Realm，这个Realm是最终用于完成我们的认证号和授权操作的具体对象
        defaultWebSecurityManager.setRealm(myRealm);
        return defaultWebSecurityManager;
    }

    /**
     * 配置一个自定义的Realm的bean，最终将使用这个bean返回的对象来完成我们的认证和授权
     *
     */
    @Bean
    public MyRealm myRealm(){
        MyRealm myRealm = new MyRealm();
        return myRealm;
    }

    /**
     * 配置一个shiro的过滤器bean,这个bean将配置shiro相关的一个拦截
     *
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        //创建过滤器bean
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/");//配置用户登录请求，如果shiro需要认证就会跳到这个请求页面
        shiroFilterFactoryBean.setSuccessUrl("/success");//配置登录成功后转向的页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noPermission");//配置没有权限时转向的地址
        /**
         * 配置拦截规则
         */
        Map<String,String> filerchaimMap = new LinkedHashMap<>();
        filerchaimMap.put("login","anon");//配置登录请求不需要认证，anon表示某个请求不需要认证
        filerchaimMap.put("logout","logout");//配置登录的请求，登出后会清空当前用户的内存

        filerchaimMap.put("/admin/**","authc");//配置一个admin开头的所有请求需要登录 authc表示需要登录
        filerchaimMap.put("/user/**","authc");//配置一个user开头的所有请求需要登录 authc表示需要登录

        //配置剩下的所有请求都需要登录(注意这个必须写在最后!!!)
        //filerchaimMap.put("/**","authc");

        //把拦截规则赋给过滤嘴
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filerchaimMap);

        return shiroFilterFactoryBean;
    }
}
