package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.commons.BusinessException;
import com.example.usercenterbackend.commons.ErrorCode;
import com.example.usercenterbackend.model.domain.UserRelation;
import com.example.usercenterbackend.service.UserRelationService;
import com.example.usercenterbackend.mapper.UserRelationMapper;
import org.springframework.stereotype.Service;

/**
 * @author 15927
 * @description 针对表【user_relation(用户关注关系表)】的数据库操作Service实现
 * @createDate 2026-07-05 17:09:31
 */
@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation>
        implements UserRelationService {

    @Override
    public BaseResponse<Boolean> focus(Long id, Long toID) {
        QueryWrapper<UserRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower_id", id);
        queryWrapper.eq("followee_id", toID);
        UserRelation relation = this.getOne(queryWrapper);
        // 关系条目不存在，插入新条目
        if (relation == null) {
            UserRelation userRelation = new UserRelation();
            userRelation.setFollower_id(id);
            userRelation.setFollowee_id(toID);
            return BaseResponse.success(this.save(userRelation));
        } else if (relation.getStatus() == 0) {
            // 存在条目，且已关注
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "已关注，请勿重复关注");
        }else {
            // 存在条目，未关注。更新代表关系的status字段
            UpdateWrapper<UserRelation> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("follower_id", id)
                    .eq("followee_id", toID)
                    .set("status", 0);
            return BaseResponse.success(this.update(updateWrapper));
        }
    }

    @Override
    public BaseResponse<Boolean> unfocus(Long id, Long toID) {
        QueryWrapper<UserRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("follower_id", id)
                .eq("followee_id", toID);
        UserRelation relation = this.getOne(queryWrapper);
        if (relation == null || relation.getStatus() != 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未关注，无法取关");
        }

        // 更新代表关系的status字段
        UpdateWrapper<UserRelation> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("follower_id", id)
                .eq("followee_id", toID)
                .set("status", 1);
        return BaseResponse.success(this.update(updateWrapper));
    }

    @Override
    public BaseResponse<Long> fansCount(Long id) {
        QueryWrapper<UserRelation> wrapper = new QueryWrapper<>();
        wrapper
                .eq("followee_id", id)
                .eq("status",0);
        Long count = this.baseMapper.selectCount(wrapper);
        return BaseResponse.success(count);
    }

    @Override
    public BaseResponse<Long> followsCount(Long id) {
        QueryWrapper<UserRelation> wrapper = new QueryWrapper<>();
        wrapper
                .eq("follower_id", id)
                .eq("status",0);
        Long count = this.baseMapper.selectCount(wrapper);
        return BaseResponse.success(count);
    }
}




