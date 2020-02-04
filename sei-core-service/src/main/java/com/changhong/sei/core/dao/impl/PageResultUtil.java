package com.changhong.sei.core.dao.impl;

import com.changhong.sei.core.dto.serach.PageInfo;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.QueryParam;
import com.changhong.sei.core.dto.serach.SearchOrder;
import com.changhong.sei.core.entity.search.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <strong>实现功能:</strong>
 * <p>Query分页查询工具类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-11-25 10:40
 */
public class PageResultUtil {
    private static final String ORDER_BY = "order by";
    /**
     * 获取Query的分页查询结果
     * @param entityManager 业务实体数据环境
     * @param sql 查询语句
     * @param sqlParams 查询语句的参数
     * @param queryParam 查询参数
     * @param <T> 实体类型
     * @return 分页查询结果
     */
    public static <T extends Serializable> PageResult<T> getResult(EntityManager entityManager,
                                                                   QuerySql sql,
                                                                   Map<String, Object> sqlParams,
                                                                   QueryParam queryParam){
        String select = sql.getSelect();
        String countSelect = sql.getCountSelect();
        String fromAndwhere = sql.getFromAndwhere();
        String orderBy = sql.getOrderBy();
        // 生成查询
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(select).append(" ")
                .append(fromAndwhere);
        StringBuilder countSqlBuilder = new StringBuilder();
        if (StringUtils.isBlank(countSelect)){
            countSqlBuilder.append("select count(*) ").append(fromAndwhere);
        } else {
            countSqlBuilder.append(countSelect).append(" ").append(fromAndwhere);
        }
        // 处理排序(如果SQL中已经排序，则忽略查询参数中的排序)
        List<SearchOrder> searchOrders = queryParam.getSortOrders();
        if (StringUtils.isBlank(orderBy) && Objects.nonNull(searchOrders) && !searchOrders.isEmpty()){
            sqlBuilder.append(" ").append(ORDER_BY).append(" ");
            int sortLen = searchOrders.size();
            for (int i = 0; i < sortLen; i++) {
                SearchOrder searchOrder= searchOrders.get(i);
                if (i==0){
                    sqlBuilder.append(searchOrder.getProperty()).append(" ").append(searchOrder.getDirection());
                } else {
                    sqlBuilder.append(",").append(searchOrder.getProperty()).append(" ").append(searchOrder.getDirection());
                }
            }
        } else if (!StringUtils.isBlank(orderBy)){
            sqlBuilder.append(" ").append(orderBy);
        }
        Query query = entityManager.createQuery(sqlBuilder.toString());
        Query countQuery = entityManager.createQuery(countSqlBuilder.toString());
        // 处理查询参数
        if (Objects.nonNull(sqlParams) && !sqlParams.isEmpty()){
            sqlParams.forEach(query::setParameter);
            sqlParams.forEach(countQuery::setParameter);
        }
        // 处理分页查询
        PageInfo pageInfo = queryParam.getPageInfo();
        // 获取查询的COUNT
        long total = (long)countQuery.getSingleResult();
        // 设置起始查询位置
        int start = (pageInfo.getPage() - 1) * pageInfo.getRows();
        int pageSize = pageInfo.getRows();
        if (start < total && pageSize > 0){
            query.setFirstResult(start);
            query.setMaxResults(pageSize);
        }
        // 计算总页数
        int totalPage =  total % pageSize == 0 ? (int)(total / pageSize) : ((int)(total / pageSize)+1) ;
        List result = query.getResultList();
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(totalPage);
        pageResult.setRecords(total);
        pageResult.setPage(pageInfo.getPage());
        pageResult.setRows(result);
        return pageResult;
    }
}
