package com.ethan.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除用户请求体
 * @author 0808.icu
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}