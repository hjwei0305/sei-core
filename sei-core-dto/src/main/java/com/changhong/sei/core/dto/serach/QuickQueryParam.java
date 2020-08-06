package com.changhong.sei.core.dto.serach;

/**
 * <strong>实现功能:</strong>
 * <p>Query快速查询参数</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-11-25 10:40
 */
public class QuickQueryParam extends QueryParam {

    private static final long serialVersionUID = 1L;
    /**
     * 快速搜索关键字
     */
    private String quickSearchValue;

    public String getQuickSearchValue() {
        return quickSearchValue;
    }

    public void setQuickSearchValue(String quickSearchValue) {
        this.quickSearchValue = quickSearchValue;
    }
}
