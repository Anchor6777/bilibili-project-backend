package com.example.usercenterbackend.commons;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页类型返回值，返回值样例BaseResponse<PageResponse<NamedVideo>>：
 * {
 *   "code": 200,
 *   "data": {
 *     "records": [ { "video": {...}, "username": "anchor" } ],
 *     "total": 137,
 *     "pageNum": 1,
 *     "pageSize": 15,
 *     "hasMore": true
 *   },
 *   message: "123",
 *   description: "456"
 * }
 * @param <T> : 参数泛型
 */
@Data
public class PageResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> records;   // 当前页数据
    private long total;        // 总条数
    private long pageNum;      // 页数
    private long pageSize;     // 当前页数量
    private boolean hasMore;   // 是否还有下一页，前端直接用它停

}
