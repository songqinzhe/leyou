package com.leyou.cart.intercerceptor;

import com.leyou.cart.config.JwtProperties;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtProperties jwtProperties;
    private static final ThreadLocal<UserInfo> THREAD_LOCAL= new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取cookie中的token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        //解析token，获取用户信息
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());

        if (userInfo == null){
            return false;
        }

        //把userInfo放入线程局部变量
        THREAD_LOCAL.set(userInfo);

        return true;
    }

    public static UserInfo getUserInfo() {
        return THREAD_LOCAL.get();
    }

    /**
     * 将当前线程局部变量的值删除，目的是为了减少内存的占用，该方法是JDK 1.5 新增的方法。需要指出的是，当线程结束后，
     * 对应该线程的局部变量将自动被垃圾回收，所以显式调用该方法清除线程的局部变量并不是必须的操作，但它可以加快内存回收的速度
     *
     * 我们这里的线程没有结束，我们使用的tomcat线程池，如果不释放掉，可能会拿到已经放好的线程信息
     */
    //释放线程，由于线程结束后会放到tomcat

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空线程的局部变量。因为使用的是tomcat的线程池，线程不会结束，也不会释放线程的局部变量
        THREAD_LOCAL.remove();
    }
}
