package com.example.usercenterbackend.service;

import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.JwtUtils;
import com.example.usercenterbackend.model.body.UserLoginResponse;
import com.example.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户服务
 *
 * @author 15927
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2025-08-28 14:28:08
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param username      用户名
     * @param password      密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    BaseResponse<Long> userRegister(String username, String password, String checkPassword);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param request 请求相关参数
     * @return 用户信息
     */
    BaseResponse<UserLoginResponse> userLogin(String username, String password, HttpServletRequest request) throws Exception;

    BaseResponse<UserLoginResponse> refresh(String refreshToken);

    BaseResponse<Boolean> userLogout(HttpServletRequest request) throws Exception;

    User getSafeUser(User originUser);

    BaseResponse<List<User>> searchUsersByTags(List<String> tags);

    BaseResponse<List<User>> searchUsersByAnyTag(List<String> tags);

    BaseResponse<List<User>> getOnlineInfo();

    static boolean isLogin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null) {
            return false;
        }
        try{
            JwtUtils.verifyToken(token.split(" ")[1]);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
