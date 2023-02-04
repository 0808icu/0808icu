package com.ethan.project.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ethan.project.annotation.AuthCheck;
import com.ethan.project.model.entity.User;
import com.ethan.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:权限验证AOP
 * @Auther: http://www.0808.icu
 */
@Component
@Aspect
public class AuthInterceptor {
    @Resource
    private UserService userService;

    /**
     * 执行拦截
     * @param point
     * @param authCheck
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint point, AuthCheck authCheck) throws Throwable{
        List<String> anyRole = Arrays.stream(authCheck.anyRole()).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
        String mustRole= authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        //当前登陆对象
        User user = userService.getLoginUser(httpServletRequest);
        //拥有任何权限就通过
        if (CollectionUtils.isNotEmpty(anyRole)){
            String userRole=user.getUserrole();
            if (!anyRole.contains(userRole)){
                System.out.println("用户没有任何权限，请退出！");
                return -1;
            }
        }
        //必须拥有所有权限才通过
        if (StringUtils.isNoneBlank(mustRole)){
            String userRole=user.getUserrole();
            if (!userRole.equals(mustRole)){
                System.out.println("用户没有管理员权限，请退出！");
                return -1;
            }
        }
        //通过权限校验，放行
        return point.proceed();
    }
}
