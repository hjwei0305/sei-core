package com.changhong.sei.core.service;

import com.changhong.sei.core.api.BaseRelationService;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.RelationEntityDto;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.RelationEntity;

/**
 * <strong>实现功能:</strong>
 * <p>分配关系业务实体的服务API接口默认实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-03 17:15
 */
public interface DefaultRelationService<TT extends BaseEntity & RelationEntity<PT, CT>, PT extends AbstractEntity<String>, CT extends AbstractEntity<String>, TD extends BaseEntityDto & RelationEntityDto<PD, CD>, PD extends BaseEntityDto, CD extends BaseEntityDto>
        extends DefaultBaseService<TT, TD>, BaseRelationService<TD, PD, CD> {
}
