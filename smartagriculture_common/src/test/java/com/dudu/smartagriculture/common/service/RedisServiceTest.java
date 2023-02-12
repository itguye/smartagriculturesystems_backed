package com.dudu.smartagriculture.common.service;

import com.dudu.smartagriculture.common.service.impl.RedisServiceImpl;
import org.junit.jupiter.api.Test;


class RedisServiceTest {
    private RedisService redisService;

    @Test
    public void getConnect() {
        redisService = new RedisServiceImpl();
        Object o = redisService.get("mall:ums:admin:admin");
        System.out.println(o);
    }

}