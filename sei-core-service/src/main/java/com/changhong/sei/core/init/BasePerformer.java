package com.changhong.sei.core.init;

import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.service.bo.OperateResult;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 实现功能: 任务执行器基类
 *
 * @author 王锦光 wangjg
 * @version 2022-02-25 10:40
 */
public abstract class BasePerformer<T extends BaseEntity> implements TaskPerformer {
    /**
     * 设置初始化业务实体名称
     *
     * @return 业务实体名称
     */
    protected abstract String getEntityName();

    /**
     * 在子类中设置初始换业务实体清单（存在当前租户，可以构造租户数据）
     * initEntities = new LinkedList<>();
     * initEntities.add(...);
     * return initEntities;
     */
    protected abstract List<T> constructInitEntities();

    /**
     * 检查初始化业务实体已经存在
     *
     * @param entity 初始化业务实体
     * @return 已经存在
     */
    protected abstract boolean alreadyExists(T entity);

    /**
     * 设置关联属性
     *
     * @param entity 初始化业务实体
     */
    protected void setRelationalField(T entity) {
    }

    /**
     * 创建一个初始化业务实体
     *
     * @param entity 初始化业务实体
     */
    protected abstract void save(T entity);

    /**
     * 执行任务
     *
     * @return 执行结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperateResult performTask() {
        // 设置初始化业务实体
        List<T> initEntities = constructInitEntities();
        for (T entity : initEntities) {
            // 设置关联属性
            setRelationalField(entity);
            // 判断是否已经存在
            if (alreadyExists(entity)) {
                // 如果存在则跳过
                continue;
            }
            // 不存在则保存
            save(entity);
        }
        // {0}初始化完毕！
        return OperateResult.operationSuccess("core_service_00041", getEntityName());
    }
}
