package com.ethan.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ethan.project.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author 0808.icu
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-02-02 18:22:44
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册、
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return 新用户id
     */

    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前登陆用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     */
    User getSafetyUser(User originUser);
}
