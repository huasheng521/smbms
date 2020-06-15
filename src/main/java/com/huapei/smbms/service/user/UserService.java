package com.huapei.smbms.service.user;


import com.github.pagehelper.PageInfo;
import com.huapei.smbms.pojo.User;

import java.util.List;

public interface UserService {

    public User login(String userCode, String userPassword) throws Exception;

    public PageInfo getUserList(String userName, Integer userRole, Integer currentPageNo, Integer pageSize) throws Exception;

    User queryExist(String userCode);

    void InsertUser(User user) throws Exception;

    void modifyexe(User user) throws Exception;

    User queryUserByID(Integer uid) throws Exception;

    Integer delUserById(Integer uid) throws Exception;
}
