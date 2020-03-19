package com.changhong.sei.core.service.flow;

import com.changhong.sei.core.api.FlowCommonConditionApi;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dao.jpa.BaseDao;
import com.changhong.sei.core.dto.flow.FlowStatus;
import com.changhong.sei.core.dto.flow.IBusinessFlowEntity;
import com.changhong.sei.core.dto.flow.IConditionPojo;
import com.changhong.sei.core.dto.flow.annotaion.BusinessEntityAnnotaion;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.utils.ExpressionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：通用客户端条件表达式服务
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/07/20 13:22      谭军(tanjun)                    新建
 * <p/>
 * *************************************************************************************************
 */
public class CommonConditionService implements FlowCommonConditionApi {


    public CommonConditionService() {
    }

    private Map<String, String> getPropertiesForConditionPojo(String conditonPojoClassName,Boolean all) throws ClassNotFoundException {
        return ExpressionUtil.getProperties(conditonPojoClassName,all);
    }


    private Map<String, Object> getPropertiesAndValues(String conditonPojoClassName,Boolean all) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        return ExpressionUtil.getPropertiesAndValues(conditonPojoClassName,all);
    }


    private Map<String, Object> getConditonPojoMap(String conditonPojoClassName, String daoBeanName, String id,Boolean all) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        Class conditonPojoClass = Class.forName(conditonPojoClassName);
        BaseDao appModuleDao = ContextUtil.getBean(daoBeanName);
        IConditionPojo conditionPojo = (IConditionPojo) conditonPojoClass.newInstance();
        BaseEntity content = (BaseEntity) appModuleDao.findOne(id);
        BeanUtils.copyProperties(conditionPojo, content);
        if (conditionPojo != null) {
            return new ExpressionUtil<>().getPropertiesAndValues(conditionPojo,all);
        } else {
            return null;
        }
    }


    @Override
    public Map<String, String> properties(String businessModelCode,Boolean all) throws ClassNotFoundException {
        String conditonPojoClassName = getConditionBeanName(businessModelCode);
        return this.getPropertiesForConditionPojo(conditonPojoClassName,all);
    }
    public Map<String, String> propertiesAll(String businessModelCode) throws ClassNotFoundException {
        String conditonPojoClassName;
        conditonPojoClassName = getConditionBeanName(businessModelCode);
        return this.getPropertiesForConditionPojo(conditonPojoClassName,true);
    }

    @Override
    public Map<String, Object> initPropertiesAndValues(String businessModelCode) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        String conditonPojoClassName;
        conditonPojoClassName = getConditionBeanName(businessModelCode);
        return this.getPropertiesAndValues(conditonPojoClassName,true);
    }

    @Override
    public Map<String, Object> propertiesAndValues(String businessModelCode, String id,Boolean all) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        String conditonPojoClassName;
        String daoBeanName;
        conditonPojoClassName = getConditionBeanName(businessModelCode);
        daoBeanName = getDaoBeanName(businessModelCode);
        return this.getConditonPojoMap(conditonPojoClassName, daoBeanName, id,all);
    }

    /**
     * 重置单据状态
     *
     * @param businessModelCode 业务实体代码
     * @param id                单据id
     * @param status            状态
     * @return 返回结果
     * @throws ClassNotFoundException    类找不到异常
     * @throws InvocationTargetException 目标类解析异常
     * @throws InstantiationException    实例异常
     * @throws IllegalAccessException    访问异常
     * @throws NoSuchMethodException     没有方法异常
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean resetState(String businessModelCode, String id, FlowStatus status) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        String daoBeanName;
        daoBeanName = getDaoBeanName(businessModelCode);
        BaseDao appModuleDao = ContextUtil.getBean(daoBeanName);
        IBusinessFlowEntity content = (IBusinessFlowEntity) appModuleDao.findOne(id);
        if(status==FlowStatus.INIT){//针对流程强制终止时，表单已经被删除的情况
            if(content!=null){
                content.setFlowStatus(status);
                appModuleDao.save(content);
            }
        }else{
          if(content==null){
             throw new RuntimeException("business.id do not exist, can not start or complete the process!");
           }
            content.setFlowStatus(status);
            appModuleDao.save(content);
        }
        return true;
    }

    @Override
    public Map<String,Object> businessPropertiesAndValues(String businessModelCode,String id) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,NoSuchMethodException{
        String daoBeanName;
        daoBeanName = getDaoBeanName(businessModelCode);
        BaseDao appModuleDao = ContextUtil.getBean(daoBeanName);
        IBusinessFlowEntity content = (IBusinessFlowEntity) appModuleDao.findOne(id);
        return   BusinessUtil.getPropertiesAndValues(content,null);
    }
    private String getDaoBeanName(String className)throws ClassNotFoundException {
        BusinessEntityAnnotaion businessEntityAnnotaion = this.getBusinessEntityAnnotaion(className);
         return   businessEntityAnnotaion.daoBean();
    }
    private String getConditionBeanName(String className)throws ClassNotFoundException {
        BusinessEntityAnnotaion businessEntityAnnotaion = this.getBusinessEntityAnnotaion(className);
        return   businessEntityAnnotaion.conditionBean();
    }
    private BusinessEntityAnnotaion getBusinessEntityAnnotaion(String className)throws ClassNotFoundException {
        if (StringUtils.isNotEmpty(className)) {
            Class sourceClass = Class.forName(className);
            BusinessEntityAnnotaion businessEntityAnnotaion = (BusinessEntityAnnotaion) sourceClass.getAnnotation(BusinessEntityAnnotaion.class);
            return  businessEntityAnnotaion;
        }else {
            throw new RuntimeException("className is null!");
        }
    }
}
