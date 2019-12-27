package com.changhong.sei.core.dao;

import com.changhong.sei.core.dao.jpa.impl.DaoImplMapper;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

/**
 * 实现功能:
 * 业务实体数据访问实现工厂类
 *
 * @param <T> Persistable的子类
 * @param <R> JpaRepository的子类
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/5/8 15:48
 */
@SuppressWarnings("unchecked")
public class BaseDaoFactoryBean<R extends JpaRepository<T, Serializable>, T extends Persistable>
        extends JpaRepositoryFactoryBean<R, T, Serializable> {

    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public BaseDaoFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
        return new JpaRepositoryFactory(entityManager) {
            @Override
            protected SimpleJpaRepository<T, Serializable> getTargetRepository(
                    RepositoryInformation information, EntityManager entityManager) {
                Class<T> domainClass = (Class<T>) information.getDomainType();
                return new DaoImplMapper<T>().getTargetRepository(domainClass, entityManager);
            }

            @Override
            protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
                if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
                    return QuerydslJpaRepository.class;
                } else {
                    Class clazz = metadata.getDomainType();
                    return new DaoImplMapper<T>().getRepositoryBaseClass(clazz);
                }
            }

            /**
             * Returns whether the given repository interface requires a QueryDsl specific implementation to be chosen.
             *
             * @param repositoryInterface
             * @return
             */
            private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
                return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
            }
        };
    }
}
