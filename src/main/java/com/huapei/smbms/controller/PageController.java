package com.huapei.smbms.controller;

import com.huapei.smbms.dao.role.RoleDao;
import com.huapei.smbms.pojo.Role;
import com.huapei.smbms.pojo.User;
import com.huapei.smbms.service.role.RoleService;
import com.huapei.smbms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PageController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @RequestMapping("addPage")
    public ModelAndView goAddUser(ModelAndView modelAndView) throws Exception {
        List<Role> roleList = this.roleService.getRoleList();
        modelAndView.addObject("roleList",roleList);
        modelAndView.setViewName("user/useradd");
        return modelAndView;
    }
    @RequestMapping("user/modify")
    public ModelAndView goModefiy(ModelAndView modelAndView, @RequestParam("uid") Integer uid) throws Exception {
        User user=  this.userService.queryUserByID(uid);
        modelAndView.addObject("user",user);
         modelAndView.setViewName("user/usermodify");
         return modelAndView;
    }
}
