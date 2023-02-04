package com.ethan.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.ethan.project.common.BaseResponse;
import com.ethan.project.common.DeleteRequest;
import com.ethan.project.common.ErrorCode;
import com.ethan.project.common.ResultUtils;
import com.ethan.project.exception.BusinessExcption;
import com.ethan.project.model.dto.user.*;
import com.ethan.project.model.entity.User;
import com.ethan.project.model.vo.UserVO;
import com.ethan.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.ethan.project.constant.UserConstant.ADMIN_ROLE;
import static com.ethan.project.constant.UserConstant.USER_LOGIN_STATE;

/**
 *  用户接口
 * @author 0808.icu
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return 新用户id
     */
    @RequestMapping("/register")
    private BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest==null){
            System.out.println("参数为空");
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        Long result = userService.userRegister(userAccount,userPassword,checkPassword);

        return ResultUtils.success(result);
    }

    /**
     *  用户登陆
     * @param userLoginRequest
     * @param request
     * @return 用户信息
     */
    @RequestMapping("/login")
    private BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest==null){
            System.out.println("登陆参数为空");
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        User user= userService.userLogin(userAccount,userPassword,request);
        return ResultUtils.success(user);
    }

    @RequestMapping("/logout")
    private BaseResponse<Boolean> userLogout(HttpServletRequest request){
        if (request==null){
            System.out.println("用户注销参数为空");
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        boolean result= userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 用户校验、信息脱敏
     * @param request
     * @return
     */
    @RequestMapping("/current")
    private BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser=(User) userObj;
        if (currentUser==null){
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        // 校验用户是否合法
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        //用户脱敏
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }
    @RequestMapping("/search")
    private BaseResponse<List<User>> searchUser(String username,HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("userName", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 获取当前登陆用户信息
     * @param request
     * @return
     */
    @RequestMapping("/get/login")
    private BaseResponse<UserVO> getLoginUser(HttpServletRequest request){
        User user= userService.getLoginUser(request);
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 添加用户
     * @param userAddRequest
     * @param request
     * @return
     */
    @RequestMapping("/add")
    private BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request){
        if (userAddRequest==null){
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        User user =new User();
        BeanUtils.copyProperties(userAddRequest,user);
        boolean result = userService.save(user);
        if (!result){
            throw new BusinessExcption(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据用户ID删除用户
     * @param deleteRequest
     * @param request
     * @return
     */
    @RequestMapping("/delete")
    private BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request){
        if (deleteRequest==null||deleteRequest.getId()<0){
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新用户信息
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @RequestMapping("/update")
    private BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request){
        if (userUpdateRequest==null){
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        User user=new User();
        BeanUtils.copyProperties(userUpdateRequest,user);
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    /**
     *
     * 根据用户ID获取用户
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/get")
    private BaseResponse<UserVO>  getUserById(int id, HttpServletRequest request){
        if (id<=0){
            System.out.println("用户id参数错误");
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        User user =userService.getById(id);
        UserVO userVO=new UserVO();
        if (user==null){
            System.out.println("查询的用户不存在");
            throw new BusinessExcption(ErrorCode.PARAMS_ERROR);
        }
        BeanUtils.copyProperties(user,userVO);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取用户列表
     * @param userQueryRequest
     * @param request
     * @return
     */
    @RequestMapping("/list")
    private BaseResponse<List<UserVO>>  listUser(UserQueryRequest userQueryRequest, HttpServletRequest request){
        if (userQueryRequest==null){
            System.out.println("获取用户列表请求参数为空");
        }
        User userQuery=new User();
        BeanUtils.copyProperties(userQueryRequest,userQuery);
        QueryWrapper<User> queryWrapper=new QueryWrapper<>(userQuery);
        List<User> userList = userService.list(queryWrapper);
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(userVOList);
    }

    /**
     * 获取用户列表分页
     * @param userQueryRequest
     * @param request
     * @return
     */
    @RequestMapping("/list/page")
    private BaseResponse<Page<UserVO>>  listUserByPage(UserQueryRequest userQueryRequest,HttpServletRequest request){
        long current=1;
        long pageSize=10;
        User userQuery=new User();
        if (userQueryRequest!=null){
            BeanUtils.copyProperties(userQueryRequest,userQuery);
            current=userQueryRequest.getCurrent();
            pageSize=userQueryRequest.getPageSize();
        }

        QueryWrapper<User> queryWrapper=new QueryWrapper<>(userQuery);
        Page<User> userPage = userService.page(new Page<>(current, pageSize), queryWrapper);
        Page<UserVO> userVOPage=new PageDTO<>(userPage.getCurrent(),userPage.getSize(),userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 判断是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user=(User) userObj;
        return user!=null && user.getUserrole()==ADMIN_ROLE;
    }
}
