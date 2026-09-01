package com.example.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.constant.Constant;
import com.example.usercenterbackend.model.body.UserFocusRequest;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.model.domain.UserRelation;
import com.example.usercenterbackend.service.UserRelationService;
import com.example.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relation")
public class UserRelationController {

    @Resource
    private UserRelationService relationService;

    // 关注
    @PostMapping("/focus")
    public BaseResponse<Boolean> focus(@RequestBody UserFocusRequest focusRequest, HttpServletRequest request) {
        if (!UserService.isLogin(request)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN,"用户未登录");
        }
        return relationService.focus(focusRequest.id,focusRequest.toId);
    }

    // 取关
    @PostMapping("/unfocus")
    public BaseResponse<Boolean> unfocus(@RequestBody UserFocusRequest focusRequest, HttpServletRequest request) {
//        if (request.getSession().getAttribute(Constant.USER_LOGIN_STATUS) == null) {
//            throw new BusinessException(ErrorCode.NOT_LOGIN,"用户未登录");
//        }
        return relationService.unfocus(focusRequest.id,focusRequest.toId);
    }

    // 粉丝数
    @GetMapping("/fans_count")
    public BaseResponse<Long> fansCount(@RequestParam Long id, HttpServletRequest request) {
        return relationService.fansCount(id);
    }

    // 关注数
    @GetMapping("/follows_count")
    public BaseResponse<Long> followsCount(@RequestParam Long id, HttpServletRequest request) {
        return relationService.followsCount(id);
    }

    // 用户(id)是否关注另一个用户(toId)
    @GetMapping("/get_focus_info")
    public BaseResponse<Boolean> getFocusInfo(@RequestParam Long id,@RequestParam Long toId) {
        QueryWrapper<UserRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("follower_id", id);
        wrapper.eq("followee_id", toId);
        UserRelation relation = relationService.getOne(wrapper);
        if (relation != null && relation.getStatus() == 0) {
            return BaseResponse.success(true);
        }else {
            return BaseResponse.success(false);
        }
    }
}
