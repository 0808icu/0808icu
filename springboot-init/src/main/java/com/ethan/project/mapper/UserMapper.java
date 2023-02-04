package com.ethan.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ethan.project.model.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
* @author 0808.icu
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2023-02-02 18:22:44
* @Entity entity.model.com.ethan.project.User
*/
public interface UserMapper extends BaseMapper<User> {

}




