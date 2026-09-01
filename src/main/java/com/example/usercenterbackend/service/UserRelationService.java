package com.example.usercenterbackend.service;

import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.model.domain.UserRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 15927
* @description 针对表【user_relation(用户关注关系表)】的数据库操作Service
* @createDate 2026-07-05 17:09:31
*/
public interface UserRelationService extends IService<UserRelation> {

    /**
     * 关注
     * @param id 用户ID
     * @param toID 被关注者ID
     * @return 是否关注成功
     */
    BaseResponse<Boolean> focus(Long id,Long toID);

    /**
     * 取关
     * @param id 用户ID
     * @param toID 被取关者ID
     * @return 是否取关成功
     */
    BaseResponse<Boolean> unfocus(Long id,Long toID);

    /**
     * 用户粉丝数
     * @param id 用户ID
     * @return 粉丝数
     */
    BaseResponse<Long> fansCount(Long id);

    /**
     * 用户关注数
     * @param id 用户ID
     * @return 关注数
     */
    BaseResponse<Long> followsCount(Long id);
}
