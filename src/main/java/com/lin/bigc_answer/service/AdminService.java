package com.lin.bigc_answer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.bigc_answer.entity.user.Admin;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lin
 * @since 2022-07-05
 */
public interface AdminService extends IService<Admin> {
    Admin queryByUserName(String username);

    /**
     * 修改管理员密码
     * @param username 管理员用户名
     * @param newPassword 新密码
     * @return 修改成功返回真, 失败返回假, 管理员不存在返回null
     */
    Boolean changePassword(String username, String newPassword);
}
