package com.example.usercenterbackend.service;

import com.example.usercenterbackend.commons.BaseResponse;
import com.example.usercenterbackend.model.domain.Msg;
import com.example.usercenterbackend.model.domain.User;

import java.util.List;

public interface MsgService {
    BaseResponse<Boolean> saveNewMsg(Msg msg);
    BaseResponse<List<Msg>> getRecentMsg(int count);
}
