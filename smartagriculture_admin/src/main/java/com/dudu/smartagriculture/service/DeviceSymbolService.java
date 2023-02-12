package com.dudu.smartagriculture.service;

import com.dudu.smartagriculture.mbg.model.DeviceSymbol;

public interface DeviceSymbolService {
  int  updateDeviceSymbol(DeviceSymbol deviceSymbol);
  int  addDevice(DeviceSymbol deviceSymbol);
  int deleteDeviceSymbolByRuleName(String name);
}
