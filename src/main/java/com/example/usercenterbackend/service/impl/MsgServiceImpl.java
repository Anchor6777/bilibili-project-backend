package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.model.domain.Msg;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.service.MsgService;
import com.example.usercenterbackend.service.UserService;
import com.example.usercenterbackend.mapper.MsgMapper;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 15927
* @description 针对表【msg(聊天信息表)】的数据库操作Service实现
* @createDate 2026-04-05 19:50:45
*/
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg>
    implements MsgService {

    @Override
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    public BaseResponse<Boolean> saveNewMsg(Msg msg) {
        this.save(msg);
        return BaseResponse.success(true);
    }

    @Override
    public BaseResponse<List<Msg>> getRecentMsg(int count) {
        QueryWrapper<Msg>  wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("createTime").last("limit 0," + count);
        List<Msg> msgList = this.list(wrapper);
        return BaseResponse.success(msgList);
    }
}




