package com.ethan.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ethan.project.common.ErrorCode;
import com.ethan.project.exception.BusinessExcption;
import com.ethan.project.mapper.UserMapper;
import com.ethan.project.model.entity.User;
import com.ethan.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.ethan.project.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 0808.icu
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-02-02 18:22:44
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
     * 盐值
     */
    private static final String SALT="eh";
    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验
        if (StringUtils.isAllBlank(userAccount,userPassword,checkPassword)){
            System.out.println("参数为空");
            return -1;
            //参数为空
        }
        if(userAccount.length()<4){
            System.out.println("账号过短");
            return -1;
            //账号过短
        }
        if(userPassword.length()<6||checkPassword.length()<6){
            System.out.println("密码过短");
            return -1;
            //密码过短
        }
        // 密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            System.out.println("两次密码不一致");
            return -1;
            //两次密码不一致
        }
        synchronized(userAccount.intern()){
            //账号不能重复
            QueryWrapper queryWrapper =new QueryWrapper();
            queryWrapper.eq("userAccount",userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if(count>0){
                System.out.println("账号重复");
                return -1;
            }
            //2.加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + checkPassword).getBytes());
            User user =new User();
            user.setUseraccount(userAccount);
            user.setUserpassword(encryptPassword);
            boolean saveResult = this.save(user);
            if(!saveResult){
                System.out.println("创建失败，数据库错误");
                return -1;
            }
            return user.getId();
        }
    }

    /**
     *  用户登陆
     * @param userAccount
     * @param userPassword
     * @param request
     * @return 用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验
        if (StringUtils.isAllBlank(userAccount,userPassword)){
            System.out.println("参数为空");
            //参数为空
        }
        if(userAccount.length()<4){
            System.out.println("账号过短");
            //账号过短
        }
        if(userPassword.length()<6){
            System.out.println("密码过短");
            //密码过短
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user=new User();
        user.setUseraccount(userAccount);
        user.setUserpassword(encryptPassword);
        // 密码和校验密码相同
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",userPassword);
        user = userMapper.selectOne(queryWrapper);
        if(!userPassword.equals(user.getUserpassword())){
            System.out.println("两次密码不一致");
            //两次密码不一致
        }

        if(user==null){
            //用户不存在
            log.info("user login failed, userAccount cannot match userPassword");
            System.out.println("用户不存在");
        }
        //记录用户登录状态,返回前端
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return user;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE)==null){
            System.out.println("用户注销参数为空");
        }
        // 移除登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,"");
        return true;
    }

    /**
     * 获取当前登陆用户信息
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        //判断用户是否登陆
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser=(User) userObj;
        if (currentUser==null||currentUser.getId()==null){
            System.out.println("参数为空：无法获取登陆信息");
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId=currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser==null){
            System.out.println("没有该用户");
        }
        return currentUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return User
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser==null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUseraccount(originUser.getUseraccount());
        safetyUser.setUseravatar(originUser.getUseravatar());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setUserrole(originUser.getUserrole());
        safetyUser.setCreatetime(originUser.getCreatetime());
        return safetyUser;
    }
}




