package com.example.usercenterbackend.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.commons.JwtUtils;
import com.example.usercenterbackend.constant.Constant;
import com.example.usercenterbackend.model.body.UserLoginResponse;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.model.body.UserDeleteRequest;
import com.example.usercenterbackend.model.body.UserLoginRequest;
import com.example.usercenterbackend.model.body.UserRegisterRequest;
import com.example.usercenterbackend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.usercenterbackend.commons.JwtUtils.generateToken;
import static com.example.usercenterbackend.commons.JwtUtils.verifyToken;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关操作，登录、注册、查询、删除等")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求体为空");
        }
        String username = userRegisterRequest.getUsername();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数存在空值");
        }
        return userService.userRegister(username, password, checkPassword);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) throws Exception {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求体为空");
        }
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数存在空值");
        }
        return userService.userLogin(username, password, request);
    }

    /**
     * 更新双token
     */
    @GetMapping("/refresh")
    public BaseResponse<UserLoginResponse> refresh(@RequestParam String refreshToken) {
        return userService.refresh(refreshToken);
    }

    /**
     * 验证token是否有效
     */
    @GetMapping("/verify")
    public BaseResponse<Boolean> verifyToken(@RequestParam String token) {
        try{
            JwtUtils.verifyToken(token);
            return BaseResponse.success(true);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.NOT_LOGIN,e.getMessage());
        }
    }

    /**
     * 注销
     * todo
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) throws Exception {
        return userService.userLogout(request);
    }

    /**
     * 根据id获取用户信息
     */
    @GetMapping("/one")
    public BaseResponse<User> getUserById(@RequestParam Long id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        return BaseResponse.success(user);
    }

    @GetMapping("/online_info")
    public BaseResponse<List<User>> getOnlineInfo() {
        return userService.getOnlineInfo();
    }

//    @GetMapping("/getCurrentUser")
//    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
//        User user = (User) request.getSession().getAttribute(Constant.USER_LOGIN_STATUS);
//        return BaseResponse.success(user);
//    }

    @GetMapping("/search_users")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无管理员权限");
        }

        // 2.查询
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username", username);
        }
        List<User> users = userService.list(wrapper);

        // 3.脱敏
        List<User> safeUsers = users.stream().map(u -> userService.getSafeUser(u)).toList();
        return BaseResponse.success(safeUsers);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody UserDeleteRequest userDeleteRequest, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无管理员权限");
        }
        if (userDeleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不合法");
        }
        System.out.println("test===" + userDeleteRequest.getId());
        userService.removeById(userDeleteRequest.getId());
        return BaseResponse.success(true);
    }

    /**
     * 鉴权，检查是否是管理员
     */
    public boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constant.USER_LOGIN_STATUS);
        return user != null && user.getUserRole() == 1;
    }
}
