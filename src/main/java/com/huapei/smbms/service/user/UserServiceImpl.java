package com.huapei.smbms.service.user;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huapei.smbms.dao.role.RoleDao;
import com.huapei.smbms.dao.user.UserDao;
import com.huapei.smbms.pojo.Role;
import com.huapei.smbms.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Override
    public User login(String userCode, String userPassword ) throws Exception {
        User user = userDao.getLoginUser(userCode, userPassword);
        return user;
    }

    @Override
    public PageInfo getUserList(String userName, Integer userRole, Integer currentPageNo, Integer pageSize) throws Exception {
        PageHelper.startPage(currentPageNo,pageSize);
        List<User> userList = this.userDao.getUserList(userName,userRole);
        PageInfo<User> userPageInfo = new PageInfo<>(userList);
        return userPageInfo;
    }

    @Override
    public User queryExist(String userCode) {
        return this.userDao.queryExistByUserCode(userCode);
    }

    @Override
    @Transactional
    public void InsertUser(User user) throws Exception {
        this.userDao.add(user);
    }

    @Override
    @Transactional
    public void modifyexe(User user) throws Exception {
        this.userDao.modify(user);
    }

    @Override
    public User queryUserByID(Integer uid) throws Exception {
        User u = this.userDao.getUserById(uid);
       Role r = this.roleDao.QueryRoleByRID(u.getUserRole());

        u.setUserRoleName(r.getRoleName());
        return u;
    }

    @Override
    public Integer delUserById(Integer uid) throws Exception {
         return this.userDao.deleteUserById(uid);
    }
}
