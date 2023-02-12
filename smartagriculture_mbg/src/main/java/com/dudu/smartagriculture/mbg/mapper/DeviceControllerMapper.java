package com.dudu.smartagriculture.mbg.mapper;

import com.dudu.smartagriculture.mbg.model.DeviceController;
import com.dudu.smartagriculture.mbg.model.DeviceControllerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DeviceControllerMapper {
    long countByExample(DeviceControllerExample example);

    int deleteByExample(DeviceControllerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DeviceController row);

    int insertSelective(DeviceController row);

    List<DeviceController> selectByExample(DeviceControllerExample example);

    DeviceController selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") DeviceController row, @Param("example") DeviceControllerExample example);

    int updateByExample(@Param("row") DeviceController row, @Param("example") DeviceControllerExample example);

    int updateByPrimaryKeySelective(DeviceController row);

    int updateByPrimaryKey(DeviceController row);
}