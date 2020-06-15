package com.huapei.smbms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.huapei.smbms.pojo.Role;
import com.huapei.smbms.pojo.User;
import com.huapei.smbms.service.role.RoleService;
import com.huapei.smbms.service.user.UserService;
import com.huapei.smbms.utils.Constants;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @RequestMapping("/query")
    public ModelAndView QueryUserList(ModelAndView modelAndView ,
        @RequestParam(value = "queryname" ,required = false) String userName,
        @RequestParam(value = "queryUserRole" ,required = false) Integer userRole,
        @RequestParam(value = "pageIndex" ,defaultValue = "1") Integer cuurrentPageNo,
        @RequestParam(value = "pageSize" ,defaultValue = "5") Integer pageSize
    ) throws Exception {
        PageInfo pageInfo = this.userService.getUserList(userName, userRole, cuurrentPageNo, pageSize);
        List<Role> roleList = this.roleService.getRoleList();
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("roleList",roleList);
        modelAndView.setViewName("user/userlist");
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping(value = "/query/{uid}")
    public Object  QueryUserByid(@PathVariable Integer uid) throws Exception {
        User user = this.userService.queryUserByID(uid);

        return user;
    }
    @RequestMapping("goView/{uid}")
    public String goView(@PathVariable Integer uid , Model model){
        model.addAttribute("uid",uid);
        return "user/userview";
    }
    @RequestMapping("/deluser/{uid}")
    @ResponseBody
    public String delUserById( @PathVariable("uid")Integer uid) throws Exception {
        Integer integer = this.userService.delUserById(uid);
        Map<String, String> map = new HashMap<>();
        if (integer>0){
            map.put("delResult","true");
        }else if (this.userService.queryUserByID(uid) == null){
            map.put("delResult","notexist");
        }
        else {
            map.put("delResult","false");
        }

        return JSON.toJSONString(map);
    }
    @RequestMapping("/ucexist")
    @ResponseBody
    public String ucexist(String userCode , HttpServletResponse response) throws IOException {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        } else {
            User user  =  this.userService.queryExist(userCode);
            if (null != user) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "notexist");
            }
        }
        return JSON.toJSONString(resultMap);
    }
    @RequestMapping("/add")
    public String InsertUser(User user ,
                             HttpSession session,
                             HttpServletRequest request ,
                             @RequestParam(name = "a_idPicPath", required = false)
                                         MultipartFile file) throws Exception {
        System.out.println(user);
        User attribute = (User) session.getAttribute(Constants.USER_SESSION);
        user.setCreatedBy(attribute.getId());
        user.setCreationDate(new Date());

        //保存照片
        if (!file.isEmpty()){
//            生成照片路径
            String path   = session.getServletContext().getRealPath("/Upimg");
            File filepath = new File(path);
            //判断路径是否存在
            if (!filepath.exists()){
                //不存在即创建目录
                filepath.mkdirs();
            }
            //        1.创建文件名
            String oldName = file.getOriginalFilename();
            String suffix = oldName.substring(oldName.lastIndexOf('.'), oldName.length());
            String newName = UUID.randomUUID().toString() + suffix;
            System.out.println("====================="+newName);
            file.transferTo(new File(path+File.separator+newName));
//        2.给user的idpath赋值.
            String protocal = request.getScheme();
            String serverName = request.getServerName();
            int port = request.getServerPort();
            String appName = request.getContextPath();
            user.setIdPicPath(protocal+"://"+serverName+":"+port+File.separator+appName+File.separator+"Upimg"+File.separator+newName);
        }
        this.userService.InsertUser(user);
        return "redirect:/user/query" ;
    }
    //只需要加上下面这段即可，注意不能忘记注解
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //转换日期 注意这里的转化要和传进来的字符串的格式一直 如2015-9-9 就应该为yyyy-MM-dd
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
    }

    @RequestMapping("modifyexe")
    public String modifyexe(User user , HttpSession session , HttpServletRequest request) throws Exception {
        String uid = request.getParameter("uid");
        int id = Integer.parseInt(uid);
        User attribute = (User) session.getAttribute(Constants.USER_SESSION);
        user.setModifyBy(attribute.getId());
        user.setId(id);
        user.setModifyDate(new Date());
        this.userService.modifyexe(user);
        return "redirect:/user/query";
    }
    @RequestMapping("getrolelist")
    public void getrolelist(HttpServletResponse response) throws Exception {
        List<Role> roleList = this.roleService.getRoleList();
        if (roleList==null || CollectionUtils.isEmpty(roleList)){
            ResponseEntity.notFound().build();
        }
        HashMap<String, List<Role>> resultMap = new HashMap<String,  List<Role>>();
        resultMap.put("roleList", roleList);
        //把resultMap转为json字符串以json的形式输出
        //配置上下文的输出类型
        response.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = response.getWriter();
        //把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();//刷新
        outPrintWriter.close();//关闭流
    }
}
