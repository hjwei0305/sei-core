package com.changhong.sei.core.dto.serach;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <strong>实现功能:</strong>
 * <p>表格分页返回数据</p>
 *
 * @param <T> 泛型类
 * @author 陈飞(fly)
 * @version 2017/4/5 15:11
 */
public class PageResult<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private int page = 1;
    /**
     * 总条数
     */
    private long records;
    /**
     * 总页数
     */
    private int total;
    /**
     * 总金额
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;
    /**
     * 当前页数据
     */
    private ArrayList<T> rows;

    public PageResult() {
    }

    /**
     * 通过分页结果构造
     * @param pageResult 源分页结果
     */
    public PageResult(PageResult pageResult) {
        if (Objects.nonNull(pageResult)) {
            this.page = pageResult.getPage();
            this.records = pageResult.getRecords();
            this.total = pageResult.getTotal();
            this.totalAmount = pageResult.getTotalAmount();
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<T> getRows() {
        return rows;
    }

    /**
     * 这里new的原因是Page.getContent()返回的是只读的List集合,
     * 而平台采用restful api client方式需要序列化和反序列化，返回的集合必须是可写的
     *
     * @see org.springframework.data.domain #getContent()
     */
    public void setRows(List<T> list) {
        if (Objects.isNull(rows)) {
            rows = new ArrayList<T>();
        }
        rows.clear();
        if (CollectionUtils.isNotEmpty(list)) {
            rows.addAll(list);
        }
    }
}
