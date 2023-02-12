package com.dudu.smartagriculture.mbg.mapper;

import com.dudu.smartagriculture.mbg.model.DeviceSymbol;
import com.dudu.smartagriculture.mbg.model.DeviceSymbolExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DeviceSymbolMapper {
    long countByExample(DeviceSymbolExample example);

    int deleteByExample(DeviceSymbolExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DeviceSymbol row);

    int insertSelective(DeviceSymbol row);

    List<DeviceSymbol> selectByExample(DeviceSymbolExample example);

    DeviceSymbol selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") DeviceSymbol row, @Param("example") DeviceSymbolExample example);

    int updateByExample(@Param("row") DeviceSymbol row, @Param("example") DeviceSymbolExample example);

    int updateByPrimaryKeySelective(DeviceSymbol row);

    int updateByPrimaryKey(DeviceSymbol row);
}