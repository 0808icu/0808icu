package com.ethan.project.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:权限验证
 * @Auther: http://www.0808.icu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    /**
     * 有任何一个权限
     * @return
     */
    String[] anyRole() default "";

    /**
     * 必须是管理员权限
     * @return
     */
    String mustRole() default "";
}
