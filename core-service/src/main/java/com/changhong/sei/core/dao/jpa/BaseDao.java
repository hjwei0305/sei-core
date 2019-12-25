package com.changhong.sei.core.dao.jpa;

import com.changhong.sei.core.entity.search.PageResult;
import com.changhong.sei.core.entity.search.Search;
import com.changhong.sei.core.entity.search.SearchFilter;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.QueryHint;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>
 * JPA基类
 * <i>通过解析方法名创建查询</i>
 * <br>
 * 框架在进行方法名解析时，会先把方法名多余的前缀截取掉，
 * 比如 find、findBy、read、readBy、get、getBy，然后对剩下部分进行解析。并且如果方法的最后一个参数是 Sort 或者 Pageable 类型，也会提取相关的信息，以便按规则进行排序或者分页查询。
 * 在创建查询时，我们通过在方法名中使用属性名称来表达，比如 findByUserAddressZip ()。
 * 框架在解析该方法时，首先剔除 findBy，然后对剩下的属性进行解析，详细规则如下（此处假设该方法针对的域对象为 AccountInfo 类型）：
 * <br>
 * 先判断 userAddressZip （根据 POJO 规范，首字母变为小写，下同）是否为 AccountInfo 的一个属性，如果是，则表示根据该属性进行查询；如果没有该属性，继续第二步；
 * 从右往左截取第一个大写字母开头的字符串（此处为 Zip），然后检查剩下的字符串是否为 AccountInfo 的一个属性。
 * 如果是，则表示根据该属性进行查询；如果没有该属性，则重复第二步，继续从右往左截取；最后假设 user 为 AccountInfo 的一个属性；
 * 接着处理剩下部分（ AddressZip ），先判断 user 所对应的类型是否有 addressZip 属性。
 * 如果有，则表示该方法最终是根据 "AccountInfo.user.addressZip" 的取值进行查询；
 * 否则继续按照步骤 2 的规则从右往左截取，最终表示根据 "AccountInfo.user.address.zip" 的值进行查询。
 * <br>
 * 在查询时，通常需要同时根据多个属性进行查询，且查询的条件也格式各样（大于某个值、在某个范围等等），Spring Data JPA 为此提供了一些表达条件查询的关键字，大致如下：
 * <br>
 * <i>And</i> --- 等价于 SQL 中的 and 关键字，比如 findByUsernameAndPassword(String user, Striang pwd)；<br>
 * <i>Or</i> --- 等价于 SQL 中的 or 关键字，比如 findByUsernameOrAddress(String user, String addr)；<br>
 * <i>Between</i> --- 等价于 SQL 中的 between 关键字，比如 findBySalaryBetween(int max, int min)；<br>
 * <i>LessThan</i> --- 等价于 SQL 中的 "<"，比如 findBySalaryLessThan(int max)；<br>
 * <i>GreaterThan</i> --- 等价于 SQL 中的">"，比如 findBySalaryGreaterThan(int min)；<br>
 * <i>IsNull</i> --- 等价于 SQL 中的 "is null"，比如 findByUsernameIsNull()；<br>
 * <i>IsNotNull</i> --- 等价于 SQL 中的 "is not null"，比如 findByUsernameIsNotNull()；<br>
 * <i>NotNull</i> --- 与 IsNotNull 等价；<br>
 * <i>Like</i> --- 等价于 SQL 中的 "like"，比如 findByUsernameLike(String user)；<br>
 * <i>NotLike</i> --- 等价于 SQL 中的 "not like"，比如 findByUsernameNotLike(String user)；<br>
 * <i>OrderBy</i> --- 等价于 SQL 中的 "order by"，比如 findByUsernameOrderBySalaryAsc(String user)；<br>
 * <i>Not</i> --- 等价于 SQL 中的 "！ ="，比如 findByUsernameNot(String user)；<br>
 * <i>In</i> --- 等价于 SQL 中的 "in"，比如 findByUsernameIn(Collection<String> userList) ，方法的参数可以是 Collection 类型，也可以是数组或者不定长参数；<br>
 * <i>NotIn</i> --- 等价于 SQL 中的 "not in"，比如 findByUsernameNotIn(Collection<String> userList) ，方法的参数可以是 Collection 类型，也可以是数组或者不定长参数；
 * <br>其他判断词还有：findByStartDateAfter，findByEndDateBefore，findByNameStartingWith，findByAgeOrderByName，findByActiveTrue
 * </p>
 *
 * @param <T>  Persistable的子类
 * @param <ID> Serializable的子类
 * @author <a href="mailto:chao2.ma@changhong.com">马超(Vision.Mac)</a>
 * @version 1.0.1 2017/3/12 13:08
 * @see JpaRepository
 * @see JpaSpecificationExecutor
 */
@NoRepositoryBean
public interface BaseDao<T extends Persistable & Serializable, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 清除当前实体二级缓存
     */
    void evict();

    /**
     * 清除实体指定Id的二级缓存
     *
     * @param id id
     */
    void evict(ID id);

    /**
     * 清除所有实体二级缓存
     */
    void evictAll();

    //////////////////////自定义方法/////////////////////////
    void save(Collection<T> entities);

    T findOne(ID id);

    /**
     * 获取业务实体类型
     *
     * @return 业务实体类型
     */
    Class<T> getEntityClass();

    /**
     * 通过Id删除业务实体
     * @param id 业务实体id
     */
    void delete(ID id);

    /**
     * 通过Id清单删除业务实体
     * @param ids 业务实体id清单
     */
    void delete(Collection<ID> ids);

    /**
     * 获取所有业务实体清单(可以使用二级缓存)
     *
     * @return 返回所有业务实体集合
     */
    @Override
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<T> findAll();

    /**
     * 获取所有包含标记删除的业务实体清单(可以使用二级缓存)
     *
     * @return 返回所有包含标记删除的业务实体集合
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<T> findAllWithDelete();

    /**
     * 获取所有未冻结的(可以使用二级缓存)
     *
     * @return 返回未冻结的数据集合
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<T> findAllUnfrozen();

    /**
     * 根据泛型对象属性和值查询集合对象(可以使用二级缓存)
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 返回符合条件的业务实体集合
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<T> findListByProperty(String property, Object value);

    /**
     * 根据泛型对象属性和值查询唯一对象(可以使用二级缓存)
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回null，如果查询到多条数据则抛出异常
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    T findByProperty(String property, Object value);

    /**
     * 根据泛型对象属性和值查询唯一对象(可以使用二级缓存)
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回null，如果查询到多条数据则返回第一条
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    T findFirstByProperty(String property, Object value);

    /**
     * 根据业务对象属性的值判断业务实体是否存在
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回false，如果查询到一条或多条数据则返回true
     */
    boolean isExistsByProperty(String property, Object value);

    /**
     * 单一条件对象查询数据集合
     *
     * @param searchFilter 查询过滤器
     * @return 返回符合条件的对象集合
     * @see SearchFilter
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<T> findByFilter(SearchFilter searchFilter);

    /**
     * 基于查询条件count记录数据
     *
     * @param searchConfig 查询过滤器
     * @return 返回符合条件的总数
     * @see Search
     */
    long count(Search searchConfig);

    /**
     * 基于动态组合条件对象查询数据
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    T findOneByFilters(Search searchConfig);

    /**
     * 基于动态组合条件对象和排序定义查询数据集合
     *
     * @param searchConfig 查询过滤器
     * @return 返回符合条件的对象集合
     * @see Search
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<T> findByFilters(Search searchConfig);

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     *
     * @param searchConfig 查询过滤器
     * @return 返回符合条件的分页查询结果
     * @see Search
     * @see PageResult
     */
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    PageResult<T> findByPage(Search searchConfig);

    /**
     * 检查代码是否已经存在
     *
     * @param code 代码
     * @param id   Id标识
     * @return 是否已经存在
     */
    boolean isCodeExists(String code, String id);

    /**
     * 检查租户中代码是否已经存在
     * @param tenantCode 租户代码
     * @param code 代码
     * @param id   Id标识
     * @return 是否已经存在
     */
    boolean isCodeExists(String tenantCode, String code, String id);
}
