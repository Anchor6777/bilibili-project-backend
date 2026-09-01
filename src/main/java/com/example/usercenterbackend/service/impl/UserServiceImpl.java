package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.constant.Constant;
import com.example.usercenterbackend.model.body.UserLoginResponse;
import com.example.usercenterbackend.model.domain.Tag;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.service.UserService;
import com.example.usercenterbackend.mapper.UserMapper;
import com.example.usercenterbackend.websocket.ChatHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.example.usercenterbackend.commons.JwtUtils.generateToken;
import static com.example.usercenterbackend.commons.JwtUtils.verifyToken;
import static com.example.usercenterbackend.constant.Constant.USER_LOGIN_STATUS;
import static com.example.usercenterbackend.constant.Constant.SALT;

/**
 * @author 15927
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-08-28 14:28:08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    public static final Set<User> onlineUserSet = ConcurrentHashMap.newKeySet();

    @Resource
    private ChatHandler handler;

    @Override
    public BaseResponse<Long> userRegister(String username, String password, String checkPassword) {
        if (StringUtils.isAnyBlank(username, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数存在空值");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数长度不合法");
        }
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        // todo 校验是否包含特殊字符

        // 校验是否存在相同用户
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已存在");
        }

        // 加密用户密码
        String salt = "anchor";
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());

        // 保存用户信息
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        boolean res = this.save(user);
        if (!res) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "用户保存失败");
        }

        return BaseResponse.success(user.getId());
    }

    @Override
    public BaseResponse<UserLoginResponse> userLogin(String username, String password, HttpServletRequest request) throws Exception {
        // 1.校验账号密码
        if (StringUtils.isAnyBlank(username, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数存在空值");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数长度不合法");
        }

        // todo 校验是否包含特殊字符

        // 2.校验用户是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未注册");
        }

        // 3.校验密码是否正确
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        // 4.用户信息脱敏
        User safeUser = getSafeUser(user);

        // 5.保存用户登录态
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("username", user.getUsername());
        
        Map<String, Object> payload2 = new HashMap<>();
        payload2.put("id", user.getId());

        String token = generateToken(payload, new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        String rToken = generateToken(payload2, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7));

        // 6.通知chatHandler，更新聊天室的在线列表
        handler.notifyOnlineInfoChange();

        UserLoginResponse res = new UserLoginResponse();
        res.setUserInfo(safeUser);
        res.setToken(token);
        res.setRefreshToken(rToken);
        return BaseResponse.success(res);
    }

    @Override
    public BaseResponse<UserLoginResponse> refresh(String refreshToken) {
        Map<String, Object> data;
        try {
            data = verifyToken(refreshToken);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "refreshToken过期，刷新失败");
        }
        System.out.println("==== "+data);
        User user = this.getOne(new QueryWrapper<User>().eq("id", data.get("id")));
        User safeUser = this.getSafeUser(user);
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", user.getId());
        payload.put("username", user.getUsername());

        // 生成新token，有效期1h
        String token = generateToken(payload, new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        // 生成新refreshToken，有效期7d
        String rToken = generateToken(data, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7));

        UserLoginResponse res = new UserLoginResponse();
        res.setUserInfo(safeUser);
        res.setToken(token);
        res.setRefreshToken(rToken);
        return BaseResponse.success(res);

    }

    @Override
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) throws Exception {
        onlineUserSet.remove((User) request.getSession().getAttribute(USER_LOGIN_STATUS));
        request.getSession().removeAttribute(Constant.USER_LOGIN_STATUS);

        // 通知chatHandler，更新聊天室的在线列表
        handler.notifyOnlineInfoChange();

        return BaseResponse.success(true);
    }

    /**
     * 用户脱敏
     *
     * @param originUser 原始用户
     * @return 脱敏后用户
     */
    @Override
    public User getSafeUser(User originUser) {
        // 加密用户密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + originUser.getPassword()).getBytes());

        // 脱敏
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setAccount(originUser.getAccount());
        safeUser.setPassword(encryptPassword);
        safeUser.setUsername(originUser.getUsername());
        safeUser.setGender(originUser.getGender());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setAvatar(originUser.getAvatar());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserRole(originUser.getUserRole());
        return safeUser;
    }

    @Override
    public BaseResponse<List<User>> searchUsersByTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数存在空值");
        }
        // sql查询
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        for (String tag : tags) {
//            wrapper = wrapper.like("tags",tag);
//        }
//        List<User> users = this.list(wrapper);

        // 内存查询
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        List<User> userList = this.list(wrapper);
        Gson gson = new Gson();

        List<User> users = userList.stream().filter(user -> {
            String tagsStr = user.getTags();
            if (StringUtils.isBlank(tagsStr)) {
                return false;
            }
            Set<String> tagNameSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
            for (String tagName : tags) {
                if (!tagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafeUser).toList();
        return BaseResponse.success(users);
    }

    @Override
    public BaseResponse<List<User>> searchUsersByAnyTag(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数存在空值");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        for (String tag : tags) {
            wrapper = wrapper.or(wp -> wp.like("tags", tag));
        }
        List<User> users = this.list(wrapper);
        users.forEach(this::getSafeUser);
        return BaseResponse.success(users);
    }

    @Override
    public BaseResponse<List<User>> getOnlineInfo() {
        return BaseResponse.success(new ArrayList<>(onlineUserSet));
    }

    public static void main(String[] args) {
    }
}




