package com.dudu.smartagriculture.service.impl;

import com.dudu.smartagriculture.mbg.mapper.DeviceSymbolMapper;
import com.dudu.smartagriculture.mbg.model.DeviceSymbol;
import com.dudu.smartagriculture.mbg.model.DeviceSymbolExample;
import com.dudu.smartagriculture.service.DeviceSymbolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DeviceSymbolServiceImpl implements DeviceSymbolService {
    @Resource
    private DeviceSymbolMapper deviceSymbolMapper;

    /**
     * 修改设备符号表
     * @param deviceSymbol 设备符号对象
     * @return 受影响的行数:1:成功 0:失败
     */
    @Override
    public int updateDeviceSymbol(DeviceSymbol deviceSymbol) {
        DeviceSymbolExample deviceSymbolExample = new DeviceSymbolExample();
        deviceSymbolExample.createCriteria().andRuleNameEqualTo(deviceSymbol.getRuleName());
        return deviceSymbolMapper.updateByExampleSelective(deviceSymbol, deviceSymbolExample);
    }

    /**
     * 添加设备
     * @param deviceSymbol 设备符号对象
     * @return 受影响的行数:1:成功 0:失败
     */
    @Override
    public int addDevice(DeviceSymbol deviceSymbol) {
        int i = deviceSymbolMapper.insertSelective(deviceSymbol);
        return i;
    }

    /**
     * 删除成功
     * @param name 设备名
     * @return 受影响的行数:1:成功 0:失败
     */
    @Override
    public int deleteDeviceSymbolByRuleName(String name) {
        DeviceSymbolExample deviceSymbolExample = new DeviceSymbolExample();
        deviceSymbolExample.createCriteria().andRuleNameEqualTo(name);
        return deviceSymbolMapper.deleteByExample(deviceSymbolExample);
    }
}
