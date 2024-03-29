package com.lin.bigc_answer.config.shiro;

import com.lin.bigc_answer.utils.JWTUtil;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("defaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);

        //设置跨域过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc-cors", new FormAuthenticationFilter() {
            @Override
            protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
                //验证Token
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                String header = httpServletRequest.getHeader("X-Token");
                if (JWTUtil.getUserName(header) == null) return false;
                //放行OPTIONS
                if (httpServletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
                    return true;
                }
                return super.isAccessAllowed(request, response, mappedValue);
            }
        });

        Map<String, String> map = new LinkedHashMap<>();
        bean.setFilters(filterMap);
        //无需授权
        map.put("/student/login", "anon");//登陆接口
        map.put("/teacher/login", "anon");//登陆接口
        map.put("/admin/login", "anon");//登陆接口
        map.put("/student/register", "anon");//注册接口
        map.put("/captcha/**", "anon");//验证码接口
        map.put("/error/**", "anon");//错误页面
        map.put("/**", "authc-cors");

        //配置自定义错误返回
        bean.setUnauthorizedUrl("/error/401");
        bean.setLoginUrl("/error/10000");

        bean.setFilterChainDefinitionMap(map);
        return bean;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //设置SessionManager,去除重定向时Url中的JSESSIONID
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
        defaultWebSecurityManager.setSessionManager(defaultWebSessionManager);

        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

}
