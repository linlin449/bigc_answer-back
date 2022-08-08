package com.lin.bigc_answer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.bigc_answer.entity.user.Admin;
import com.lin.bigc_answer.mapper.AdminMapper;
import com.lin.bigc_answer.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public Admin queryByUserName(String username) {
        if (username == null || username.equals("")) return null;
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, username);
        return adminMapper.selectOne(wrapper);
    }

    @Override
    public Boolean changePassword(String username, String newPassword) {
        if (newPassword == null || newPassword.equals("")) return false;
        Admin admin = queryByUserName(username);
        if (admin == null) return null;
        admin.setPassword(newPassword);
        return adminMapper.updateById(admin) == 1;
    }
}
