package com.example.usercenterbackend.mapper;

import com.example.usercenterbackend.model.domain.VideoComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 15927
* @description 针对表【video_comment(视频评论表)】的数据库操作Mapper
* @createDate 2026-07-07 10:44:25
* @Entity com.example.usercenterbackend.model.domain.VideoComment
*/
public interface VideoCommentMapper extends BaseMapper<VideoComment> {
    @Select("SELECT c.*, u.username, u.avatar " +
            "FROM video_comment c " +
            "LEFT JOIN user u ON c.uid = u.id " +
            "WHERE c.video_id = #{vid} AND c.parent_id = 0 " +
            "ORDER BY c.create_time DESC")
    List<VideoComment> selectCommentsByVideoId(@Param("vid") Long videoId);

    @Select("SELECT c.*, u.username, u.avatar " +
            "FROM video_comment c " +
            "LEFT JOIN user u ON c.uid = u.id " +
            "WHERE c.parent_id = #{pid} " +
            "ORDER BY c.create_time DESC")
    List<VideoComment> selectChildComments(@Param("pid") Long parentId);
}




