package com.huapei.smbms.controller;

import com.huapei.smbms.pojo.User;
import com.huapei.smbms.service.user.UserService;
import com.huapei.smbms.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public ModelAndView goIndex(ModelAndView modelAndView){
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping("/login")
    public String login(@RequestParam("userCode") String userCode,
                        @RequestParam("userPassword") String password,
                        HttpSession session,
                        Model model) throws Exception {

        User user = userService.login(userCode, password);
        //判断用户是否登录
        if(user != null){
            session.setAttribute(Constants.USER_SESSION, user);
            //跳转到后台首页
            return "frame";
        }else{
            //跳转到登录页面，显示错误信息
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }

    }


    @RequestMapping("/loginOut")
    public String  LoginOut(HttpSession session){
        session.invalidate();
        return "login";
    }
}
