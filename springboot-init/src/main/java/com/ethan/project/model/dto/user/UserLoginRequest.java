package com.ethan.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:用户登陆请求体
 * @Auther: http://www.0808.icu
 */
@Data
public class UserLoginRequest implements Serializable {
    private String userAccount;
    private String userPassword;
}
