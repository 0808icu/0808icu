package com.ethan.project.common;

import com.ethan.project.constant.CommonConstant;
import lombok.Data;

/**
 * @Description:用户分页
 * @Auther: http://www.0808.icu
 */
@Data
public class PageRequest {
    /**
     * 当前页号
     */
    private long current=1;
    /**
     * 每页大小
     */
    private long pageSize =10;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     *排序顺序（默认升序）
     */
    private String sortOrder= CommonConstant.SORT_ORDER_ASC;
}
