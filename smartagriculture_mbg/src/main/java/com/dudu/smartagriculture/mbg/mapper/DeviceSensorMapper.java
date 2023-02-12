package com.dudu.smartagriculture.mbg.mapper;

import com.dudu.smartagriculture.mbg.model.DeviceSensor;
import com.dudu.smartagriculture.mbg.model.DeviceSensorExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DeviceSensorMapper {
    long countByExample(DeviceSensorExample example);

    int deleteByExample(DeviceSensorExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DeviceSensor row);

    int insertSelective(DeviceSensor row);

    List<DeviceSensor> selectByExample(DeviceSensorExample example);

    DeviceSensor selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") DeviceSensor row, @Param("example") DeviceSensorExample example);

    int updateByExample(@Param("row") DeviceSensor row, @Param("example") DeviceSensorExample example);

    int updateByPrimaryKeySelective(DeviceSensor row);

    int updateByPrimaryKey(DeviceSensor row);
}