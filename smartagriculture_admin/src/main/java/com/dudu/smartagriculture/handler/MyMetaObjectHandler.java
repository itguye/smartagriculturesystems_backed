package com.dudu.smartagriculture.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("自动插入时间");
        this.setFieldValByName("time",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
