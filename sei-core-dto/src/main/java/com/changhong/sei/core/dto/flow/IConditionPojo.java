package com.changhong.sei.core.dto.flow;

import java.io.Serializable;

/**
 * 条件Pojo接口
 * 注：条件属性需要提供默认值，以供表达式验证(主要供平台6.0使用)
 *
 *
 */
public interface IConditionPojo extends Serializable{

    /**
     * 条件表达式初始化，提供给表达式做初始化验证，
     * 结合具体业务实现
     */
    public  void init();

    /**
     * 自定义逻辑方法，
     * 场景：应用于条件表达式POJO的额外定义属性值初始化
     */
    public void customLogic();

}
