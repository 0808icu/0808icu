package com.ethan.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ethan.project.mapper.PostMapper;
import com.ethan.project.model.entity.Post;
import com.ethan.project.service.PostService;
import org.springframework.stereotype.Service;

/**
* @author 0808.icu
* @description 针对表【post(帖子)】的数据库操作Service实现
* @createDate 2023-02-02 18:22:44
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService {

}




