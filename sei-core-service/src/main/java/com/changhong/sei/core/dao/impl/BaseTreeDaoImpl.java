package com.changhong.sei.core.dao.impl;

import com.changhong.sei.core.dao.BaseTreeDao;
import com.changhong.sei.core.dao.datachange.DataHistoryUtil;
import com.changhong.sei.core.dao.jpa.impl.BaseDaoImpl;
import com.changhong.sei.core.datachange.DataChangeProducer;
import com.changhong.sei.core.dto.datachange.DataHistoryRecord;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.IFrozen;
import com.changhong.sei.core.dto.TreeEntity;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.dto.serach.SearchFilter;
import com.changhong.sei.core.dto.serach.SearchOrder;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能:
 * 树形实体Dao基类
 *
 * @param <T> BaseEntity的子类
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/5/9 1:38
 */
public class BaseTreeDaoImpl<T extends BaseEntity & TreeEntity> extends BaseDaoImpl<T, String> implements BaseTreeDao<T> {
    public BaseTreeDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    /**
     * 获取所有树根节点
     *
     * @return 返回树根节点集合
     */
    @Override
    public List<T> getAllRootNode() {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.PARENT_ID, SearchFilter.EMPTY_VALUE, SearchFilter.Operator.BK));
        setDefaultSort(search);
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    @Override
    public List<T> getChildrenNodes(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = findOne(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartingWith(entity.getCodePath()));
        }
        return nodeList;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    @Override
    public List<T> getChildrenNodesNoneOwn(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = findOne(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartingWithAndIdNot(entity.getCodePath(), nodeId));
        }
        return nodeList;
    }

    /**
     * 获取指定节点名称的所有节点
     *
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    @Override
    public List<T> getChildrenNodesByName(String nodeName) {
        Assert.notNull(nodeName, "nodeName不能为空");

        List<T> nodeList = findByNamePathLike(nodeName);
        if (CollectionUtils.isEmpty(nodeList)) {
            nodeList = new ArrayList<T>();
        }
        return nodeList;
    }

    /**
     * 获取树
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点树形对象
     */
    @Override
    public T getTree(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        T tree = findOne(nodeId);
        if (Objects.nonNull(tree)) {
            List<T> childrenList = getChildrenNodes(nodeId);
            if (CollectionUtils.isNotEmpty(childrenList)) {
                recursiveBuild(tree, childrenList);
            }
        }
        return tree;
    }

    /**
     * 通过代码路径获取指定路径开头的集合
     *
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    @Override
    public List<T> findByCodePathStartingWith(String codePath) {
        List<T> list = new ArrayList<>();
        //查询自己
        T own = findByProperty(TreeEntity.CODE_PATH, codePath);
        if (own != null) {
            list.add(own);
        }

        //查询子项
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath + TreeEntity.CODE_DELIMITER, SearchFilter.Operator.LLK));
        setDefaultSort(search);
        List<T> childrens = findByFilters(search);
        if (childrens != null && childrens.size() > 0) {
            list.addAll(childrens);
        }
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param codePath
     * @param nodeId
     * @return
     */
    @Override
    public List<T> findByCodePathStartingWithAndIdNot(String codePath, String nodeId) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath + TreeEntity.CODE_DELIMITER, SearchFilter.Operator.LLK));
        search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
        setDefaultSort(search);
        List<T> list = findByFilters(search);
        return list;
    }

    @Override
    public List<T> findByNamePathStartingWith(String namePath) {
        //查询自己
        List<T> list = findListByProperty(TreeEntity.NAME_PATH, namePath);
        if (list == null) {
            list = new ArrayList<>();
        }

        //查询子项
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath + TreeEntity.NAME_DELIMITER, SearchFilter.Operator.LLK));
        setDefaultSort(search);
        List<T> childrens = findByFilters(search);
        if (childrens != null && childrens.size() > 0) {
            list.addAll(childrens);
        }
        return list;
    }

    /**
     * @param namePath
     * @param nodeId
     * @return
     */
    @Override
    public List<T> findByNamePathStartingWithAndIdNot(String namePath, String nodeId) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath + TreeEntity.NAME_DELIMITER, SearchFilter.Operator.LLK));
        search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
        setDefaultSort(search);
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 节点名称模糊获取节点
     *
     * @param nodeName 节点名称
     * @return 返回含有指定节点名称的集合
     */
    @Override
    public List<T> findByNamePathLike(String nodeName) {
        Search search = new Search();
        search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, nodeName, SearchFilter.Operator.LK));
        setDefaultSort(search);
        List<T> list = findByFilters(search);
        return list;
    }

    /**
     * 递归构造树
     */
    @Override
    public T recursiveBuild(T parentNode, List<T> nodes) {
        List<T> children = parentNode.getChildren();
        if (Objects.isNull(children)) {
            children = new ArrayList<T>();
        }

        for (T treeNode : nodes) {
            if (Objects.equals(parentNode.getId(), treeNode.getParentId())) {
                T treeEntity = recursiveBuild(treeNode, nodes);

                children.add(treeEntity);
            }
        }
        parentNode.setChildren(children);
        return parentNode;
    }

    /////////////////////////////以下为冻结特性的方法/////////////////////////

    /**
     * @param id
     * @return
     */
    @Override
    public T findOne4Unfrozen(String id) {
        Assert.notNull(id, "主键不能为空");
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Specification<T> spec = (root, query, builder) -> builder.and(builder.equal(root.get(IFrozen.FROZEN), Boolean.FALSE), builder.equal(root.get(BaseEntity.ID), id));
            return findOne(spec).orElse(null);
        } else {
            return findOne(id);
        }
    }

    /**
     * 获取所有未冻结的树根节点
     *
     * @return 返回树根节点集合
     */
    @Override
    public List<T> getAllRootNode4Unfrozen() {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.PARENT_ID, SearchFilter.EMPTY_VALUE, SearchFilter.Operator.BK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            list = findByFilters(search);
        } else {
            list = getAllRootNode();
        }
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    @Override
    public List<T> getChildrenNodes4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartWith4Unfrozen(entity.getCodePath()));
        }
        return nodeList;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    @Override
    public List<T> getChildrenNodesNoneOwn4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        List<T> nodeList = new ArrayList<T>();
        //获取当前节点
        T entity = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(entity)) {
            nodeList.addAll(findByCodePathStartWithAndIdNot4Unfrozen(entity.getCodePath(), nodeId));
        }
        return nodeList;
    }

    /**
     * 获取指定节点名称的所有节点
     *
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    @Override
    public List<T> getChildrenNodesByName4Unfrozen(String nodeName) {
        Assert.notNull(nodeName, "nodeName不能为空");

        List<T> nodeList = findByNamePathLike4Unfrozen(nodeName);
        if (CollectionUtils.isEmpty(nodeList)) {
            nodeList = new ArrayList<T>();
        }
        return nodeList;
    }

    /**
     * 获取树
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点树形对象
     */
    @Override
    public T getTree4Unfrozen(String nodeId) {
        Assert.notNull(nodeId, "nodeId不能为空");

        T tree = this.findOne4Unfrozen(nodeId);
        if (Objects.nonNull(tree)) {
            List<T> childrenList = getChildrenNodes4Unfrozen(nodeId);
            if (CollectionUtils.isNotEmpty(childrenList)) {
                recursiveBuild4Unfrozen(tree, childrenList);
            }
        }
        return tree;
    }

    /**
     * 通过代码路径获取指定路径开头的集合
     *
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    @Override
    public List<T> findByCodePathStartWith4Unfrozen(String codePath) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            list = new ArrayList<>();
            //查询自己
            T own = findByProperty(TreeEntity.CODE_PATH, codePath);
            if (own != null) {
                list.add(own);
            }

            //查询子项
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath + TreeEntity.CODE_DELIMITER, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            List<T> childrens = findByFilters(search);
            if (childrens != null && childrens.size() > 0) {
                list.addAll(childrens);
            }
        } else {
            list = findByCodePathStartingWith(codePath);
        }
        return list;
    }

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param codePath
     * @param nodeId
     * @return
     */
    @Override
    public List<T> findByCodePathStartWithAndIdNot4Unfrozen(String codePath, String nodeId) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.CODE_PATH, codePath + TreeEntity.CODE_DELIMITER, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            list = findByFilters(search);
        } else {
            list = findByCodePathStartingWithAndIdNot(codePath, nodeId);
        }
        return list;
    }

    @Override
    public List<T> findByNamePathStartWith4Unfrozen(String namePath) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            //查询自己
            list = findListByProperty(TreeEntity.NAME_PATH, namePath);
            if (list == null) {
                list = new ArrayList<>();
            }

            //查询子项
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath + TreeEntity.NAME_DELIMITER, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            List<T> childrens = findByFilters(search);
            if (childrens != null && childrens.size() > 0) {
                list.addAll(childrens);
            }
        } else {
            list = findByNamePathStartingWith(namePath);
        }
        return list;
    }

    /**
     * @param namePath
     * @param nodeId
     * @return
     */
    @Override
    public List<T> findByNamePathStartWithAndIdNot4Unfrozen(String namePath, String nodeId) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, namePath + TreeEntity.NAME_DELIMITER, SearchFilter.Operator.LLK));
            search.addFilter(new SearchFilter(BaseEntity.ID, nodeId, SearchFilter.Operator.NE));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            list = findByFilters(search);
        } else {
            list = findByNamePathStartingWithAndIdNot(namePath, nodeId);
        }
        return list;
    }

    /**
     * 节点名称模糊获取节点
     *
     * @param nodeName 节点名称
     * @return 返回含有指定节点名称的集合
     */
    @Override
    public List<T> findByNamePathLike4Unfrozen(String nodeName) {
        List<T> list;
        if (IFrozen.class.isAssignableFrom(domainClass)) {
            Search search = new Search();
            search.addFilter(new SearchFilter(TreeEntity.NAME_PATH, nodeName, SearchFilter.Operator.LK));
            search.addFilter(new SearchFilter(IFrozen.FROZEN, Boolean.FALSE, SearchFilter.Operator.EQ));
            setDefaultSort(search);
            list = findByFilters(search);
        } else {
            list = findByNamePathLike(nodeName);
        }
        return list;
    }

    /**
     * todo 递归构造树(排除冻结节点)
     */
    @Override
    public T recursiveBuild4Unfrozen(T parentNode, List<T> nodes) {
        List<T> children = parentNode.getChildren();
        if (Objects.isNull(children)) {
            children = new ArrayList<T>();
        }

        for (T treeNode : nodes) {
            if (Objects.equals(parentNode.getId(), treeNode.getParentId())) {
                if (treeNode instanceof IFrozen) {
                    IFrozen frozen = (IFrozen) treeNode;
                    //冻结
                    if (frozen.getFrozen()) {
                        continue;
                    }
                }

                T treeEntity = recursiveBuild4Unfrozen(treeNode, nodes);

                children.add(treeEntity);
            }
        }
        parentNode.setChildren(children);
        return parentNode;
    }

    /**
     * 默认排序
     * 优先使用RANK字段排序(顺序),再按CODE字段排序(顺序)
     */
    protected void setDefaultSort(Search search) {
        search.addSortOrder(SearchOrder.asc(TreeEntity.RANK));
        search.addSortOrder(SearchOrder.asc(TreeEntity.CODE));
        //若是BaseAuditableEntity子类，再按BaseAuditableEntity.CREATED_DATE字段排序(倒序)
//        try {
//            if (domainClass.newInstance() instanceof BaseAuditableEntity) {
//                search.addSortOrder(SearchOrder.desc(BaseAuditableEntity.CREATED_DATE));
//            }
//        } catch (Exception ignored) {
//        }
    }
}
