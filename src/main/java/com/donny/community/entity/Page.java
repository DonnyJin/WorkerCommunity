package com.donny.community.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * 封装分页相关的信息
 */
@Data
public class Page {

    //当前的页码
    private Integer current = 1;
    // 页面大小, 默认10
    private Integer pageSize = 10;
    //数据总数
    private Integer rows;
    // 查询路径
    private String path;


    public void setCurrent(Integer current) {
        if(current >= 1) {
            this.current = current;
        }
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize >= 1 && pageSize <= 100) {
            this.pageSize = pageSize;
        }
    }

    public void setRows(Integer rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public Integer getOffset() {
        return (current - 1) * pageSize;
    }

    /**
     * 获取总页数
     * @return
     */
    public Integer getTotal() {
        return rows % pageSize == 0 ? rows / pageSize : rows / pageSize + 1;
    }

    /**
     * 获取起始页码
     * @return
     */
    public Integer getFrom() {
        return Math.max(current - 2, 1);
    }

    public Integer getTo() {
        return Math.min(getTotal(), current + 2);
    }


}
