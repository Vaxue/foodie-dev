package com.imooc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登录",tags = "用户注册登录的接口")
@RestController
@RequestMapping("/passport")
public class PassportController {
    @Autowired
    private UserService userService;

    private final static Logger logger = LoggerFactory.getLogger(PassportController.class);

    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){
        // 1 判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        // 2 查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }

        // 3 请求成功用户名没有重复
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        // 0  判断用户名或密码是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        // 1  实现登录
        Users result = null;
        try {
            result = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
            result = setNullProperty(result);
            ObjectMapper mapper = new ObjectMapper();
            CookieUtils.setCookie(request,response,"user",mapper.writeValueAsString(result),true);
        } catch (Exception e) {
            return IMOOCJSONResult.errorMsg("密码加密出错");
        }
        if (result == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }
        return IMOOCJSONResult.ok(result);
    }

    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,HttpServletRequest request,HttpServletResponse response){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        // 0  判断用户名或密码是否为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)
        ) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        // 1 查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        // 2 密码长度不能少于6位
        if (password.length() < 6) {
            return IMOOCJSONResult.errorMsg("密码长度最少6位");
        }
        // 3  判断两次密码是否一致
        if (!password.equals(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("两次密码输入的不一致");
        }
        // 4  实现注册
        try {
            Users user = userService.createUser(userBO);
            ObjectMapper mapper = new ObjectMapper();
            CookieUtils.setCookie(request,response,"user",mapper.writeValueAsString(user),true);
        } catch (Exception e) {
            return IMOOCJSONResult.errorMsg("注册失败，密码加密失败");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户退出登录",notes = "用户退出登录",httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,HttpServletRequest request,HttpServletResponse response){
        //清除用户的相关信息cookie
        CookieUtils.deleteCookie(request,response,"user");
        //TODO 用户退出登录需要清空购物车
        //TODO 在分布式会话中需要清除数据
        return IMOOCJSONResult.ok();
    }

    private Users setNullProperty(Users result) {
        result.setPassword(null);
        result.setMobile(null);
        result.setRealname(null);
        result.setEmail(null);
        result.setCreatedTime(null);
        result.setUpdatedTime(null);
        result.setBirthday(null);
        return result;
    }
}
