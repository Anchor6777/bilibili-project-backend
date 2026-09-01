package com.example.usercenterbackend.mapper;

import com.example.usercenterbackend.model.domain.NewMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.usercenterbackend.model.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 15927
* @description 针对表【new_msg(b站消息页——聊天信息表)】的数据库操作Mapper
* @createDate 2026-06-29 19:16:12
* @Entity com.example.usercenterbackend.model.domain.NewMsg
*/
public interface NewMsgMapper extends BaseMapper<NewMsg> {

    @Select("SELECT u.* " +
            "FROM user u " +
            "INNER JOIN ( " +
            "    SELECT sender_id AS other_user_id " +
            "    FROM new_msg " +
            "    WHERE receiver_id = #{userId}" +
            "    UNION " +
            "    SELECT receiver_id " +
            "    FROM new_msg " +
            "    WHERE sender_id = #{userId}" +
            ") AS contacts ON u.id = contacts.other_user_id")
    List<User> selectRecentChatUsers(@Param("userId") Long userId);
}




