package com.changhong.sei.core.util;

import com.changhong.sei.core.dto.ResultData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-05-31 17:58
 */
class HttpUtilsTest {

    @Test
    void sendPost() {
    }

    @Test
    void testSendPost() {
    }

    @Test
    void testSendPost1() {
    }

    @Test
    void testSendPost2() {
    }

    @Test
    void testSendPost3() {
    }

    @Test
    void testSendPost4() {
    }

    @Test
    void testSendPost5() {
    }

    @Test
    void sendGet() {
    }

    @Test
    void testSendGet() {
    }

    @Test
    void testSendGet1() {
    }

    @Test
    void testSendGet2() {
    }

    @Test
    void post() {
    }

    @Test
    void postForm() {
    }

    @Test
    void get() throws Exception {
        String url = "http://sei.changhong.com/sei-manager/authWhitelist/get?envCode=Dev";
        String data = HttpUtils.sendGet(url);
        ResultData<List<AuthWhitelistDto>> resultData = JsonUtils.mapper().readValue(data, new TypeReference<ResultData<List<AuthWhitelistDto>>>() {});
        List<AuthWhitelistDto> whitelists = resultData.getData();
        whitelists.forEach(gi -> {
            System.out.println(gi.getUri());
        });
        System.out.println(resultData);
    }

    @Test
    void httpClientUpload() {
    }

    @Test
    void concatGetUrl() {
    }

    @Test
    void responseText() {
    }
}