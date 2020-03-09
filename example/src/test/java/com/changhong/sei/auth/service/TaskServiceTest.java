package com.changhong.sei.auth.service;

import com.changhong.sei.core.test.BaseUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-10 01:05
 */
public class TaskServiceTest extends BaseUnitTest {
    @Autowired
    private TaskService taskService;

    @Test
    public void testTask() {
        try {
            taskService.doTaskOne();
            taskService.doTaskTwo();
            taskService.doTaskThree();

            Thread.sleep(100000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}