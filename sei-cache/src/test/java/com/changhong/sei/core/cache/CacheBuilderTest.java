package com.changhong.sei.core.cache;

import com.changhong.sei.util.IdGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-01 11:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheBuilderTest {

    @Autowired
    private CacheBuilder cacheBuilder;

    @Test
    public void testCacheVerson() throws Exception {

        String version = cacheBuilder.getCacheVersion();
        System.out.println(String.format("当前缓存版本：%s", version));

        String cacheKey = cacheBuilder.generateVerKey("test123456");

        CacheVo goodsVO = new CacheVo();
        goodsVO.setId(IdGenerator.uuid2());
        goodsVO.setCode("123456789");
        goodsVO.setName("我的测试商品");
        goodsVO.setDate(new Date());

        cacheBuilder.set(cacheKey, goodsVO);

        CacheVo goodsVO1 = cacheBuilder.get(cacheKey);

        Assert.assertNotNull(goodsVO1);

        version = cacheBuilder.resetCacheVersion();
        System.out.println(String.format("重置后的缓存版本：%s", version));

        cacheKey = cacheBuilder.generateVerKey("goods112233");


        cacheBuilder.set(cacheKey, goodsVO);

        CacheVo goodsVO2 = cacheBuilder.get(cacheKey);

        Assert.assertNotNull(goodsVO2);

        Assert.assertTrue("两个缓存对象的主键相同", goodsVO1.getId().equals(goodsVO2.getId()));
    }
}