package com.huapei.smbms.service.role.impl;

import com.huapei.smbms.dao.role.RoleDao;
import com.huapei.smbms.pojo.Role;
import com.huapei.smbms.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> getRoleList() throws Exception {
        return this.roleDao.getRoleList();
    }
}
