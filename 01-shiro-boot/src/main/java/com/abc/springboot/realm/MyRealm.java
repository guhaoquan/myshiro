package com.abc.springboot.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义的Realm用来实现用户的认证和授权
 * 父类AuthenticatingRealm只支持用户认证(登录)
 */
public class MyRealm extends AuthorizingRealm {

    /**
     * 用户认证的方法 这个方法不通手动调用shiro会自动调用
     * @param authenticationToken   用户身份 这里存放着用户的帐号和密码
     * @return  用户登录成功后的身份证明
     * @throws AuthenticationException  如果认证失败shiro会抛出各种异常
     * UnknownAccountException      帐号不存在
     *AuthenticationException       帐号异常
     * LockedAccountException       帐号锁定
     * IncorrectCredentialsException    认证失败
     * 注意
     *      如果这些异常不够用可以自定义异常类并继承shiro认证异常父类AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();//获取页面中用户的帐号
        String password =new String(token.getPassword());//获取页面中用户的密码
        System.out.println("当前登录的用户是:" + username + "密码为:" + password);
        /**
         * 认证帐号
         */
        if (!"admin".equals(username)&&!"lisi".equals(username)&&!"user".equals(username)){
            throw new UnknownAccountException();//抛出帐号错误的异常
        }
        /**
         * 认证帐号
         */
        if ("lisi".equals(username)){
            throw new LockedAccountException();//抛出帐号错误的异常

        }

        /**
         * 注意：
         *      建议浏览器传递数据时就是加密数据，数据库中存在的数据也是加密数据，我们必须保证前段传递的数据
         *      和数据主库中存放的数据加密次数以及盐一会规则都是完全相同的否则认证失败
         */
        //设置让当前登录用户中的密码数据进行加密
        //HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //credentialsMatcher.setHashAlgorithmName("MD5");
        //credentialsMatcher.setHashIterations(2);
        //this.setCredentialsMatcher(credentialsMatcher);

        //对数据库中的密码进行加密
        //Object obj = new SimpleHash("MD5","123456","",2);
        /**
         * 创建密码认证对象，由shiro自动认证
         * 参数1：数据库中的帐号
         * 参数2：为数据中读取数据来的密码
         * 参数3：为当前Realm的名字
         * 如果密码认证成功则返回一个用户身份对象，如果密码认证失败shiro会抛出异常
         */
        return new SimpleAuthenticationInfo(username,"123456",getName());
    }

    /**
     * 用户授权的方法,当用户认证通过以后自动执行该方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("------授权了----");
        Object obj=principalCollection.getPrimaryPrincipal();//获取用户的帐号，根据帐号来从数据库中获取
        Set<String> roles = new HashSet<String>();//定义用户角色的set集合这个集合应该来自数据库
        //设置角色
        if ("admin".equals(obj)){
            roles.add("admin");
            roles.add("user");
        }
        if ("user".equals(obj)){
            roles.add("user");
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);//设置角色信息
        return info;
    }
}
