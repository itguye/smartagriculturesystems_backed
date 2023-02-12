package com.dudu.smartagriculture.mqtt;

import com.dudu.smartagriculture.config.MqttConfiguration;
import com.dudu.smartagriculture.dto.SumDataDto;
import com.dudu.smartagriculture.mbg.mapper.DeviceSymbolMapper;
import com.dudu.smartagriculture.mbg.model.DeviceController;
import com.dudu.smartagriculture.mbg.model.DeviceSensor;
import com.dudu.smartagriculture.mbg.model.DeviceSymbol;
import com.dudu.smartagriculture.mbg.model.DeviceSymbolExample;
import com.dudu.smartagriculture.mbg.utils.BooleanTypeAdapter;
import com.dudu.smartagriculture.service.DeviceDataService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Slf4j
@Component
public class PushCallback implements MqttCallback {
    @Resource
    private MqttConfiguration mqttConfiguration;
    @Resource
    private DeviceDataService deviceDataService;
    @Resource
    private DeviceSymbolMapper symbolMapper;



    @Override
    public void connectionLost(Throwable cause) {        // 连接丢失后，一般在这里面进行重连
        log.info("连接断开，正在重连");
       MqttPushClient mqttPushClient = mqttConfiguration.getMqttPushClient();
        if (null != mqttPushClient) {
            mqttPushClient.connect(mqttConfiguration.getHost(), mqttConfiguration.getClientId(), mqttConfiguration.getUserName(),
                    mqttConfiguration.getPassword(), mqttConfiguration.getTimeout(), mqttConfiguration.getKeepAlive());
            log.info("已重连");
        }

    }

    /**
     * 发送消息，消息到达后处理方法
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    /**
     * 订阅主题接收到消息处理方法
     * @param topic
     * @param message
     */
    @Override
    public void messageArrived(String topic, MqttMessage message){
        // subscribe后得到的消息会执行到这里面,这里在控制台有输出
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + message.getQos());
        log.info("接收消息内容 : " + new String(message.getPayload()));
        // 处理JSON数据
        try{
            String   mesg=new String(message.getPayload(),"GB2312");
            JsonParser jp = new JsonParser();
            //将json字符串转化成json对象
            JsonObject jo = jp.parse(mesg).getAsJsonObject();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                    .create();
            // 总数据
            SumDataDto sumData = gson.fromJson(jo.getAsJsonObject(), SumDataDto.class);

            // 传感器设备
            List<DeviceSensor> sensorData = sumData.getSensorData();// 传感器
            // 控制器设备
            List<DeviceController> controllerData = sumData.getControllerData();// 控制器


            // 传感器设备(保存)
            List<DeviceSensor> saveSensorData = new ArrayList<>();
            // 控制器设备(保存)
            List<DeviceController> saveControllerData = new ArrayList<>();

            Set<Integer> SensorSet = new HashSet<>();// 传感器计数
            Set<Integer> ControllerSet = new HashSet<>();// 控制器计数

            // 匹配符号
            for (DeviceSymbol symbol :  symbolMapper.selectByExample(new DeviceSymbolExample())) {
                 // 传感器
                if(symbol.getType().trim().equals("0")){
                    int current = 0;
                    // 表示传感器
                    for (int i = 0; i < sensorData.size() ; i++) {
                        if(SensorSet.contains(i)) continue;
                        DeviceSensor s = sensorData.get(i);//
                        if (symbol.getRuleName().equals(s.getRuleName())) {// 表示对应上来了
                            SensorSet.add(i);// 记录当前次数
                            s.setTime(new Date());
                            s.setName(symbol.getName());// 设置名称
                            s.setSymbol(symbol.getSymbol());// 设置符号
                            current = 1;
                            saveSensorData.add(s);
                            break;
                        }
                    }

                    if (current == 0) {
                        // 表示数据中没有
                        DeviceSensor deviceSensorTemp = new DeviceSensor();
                        deviceSensorTemp.setName(symbol.getName());
                        deviceSensorTemp.setRuleName(symbol.getRuleName());
                        deviceSensorTemp.setSymbol(symbol.getSymbol());
                        deviceSensorTemp.setTime(new Date());
                        saveSensorData.add(deviceSensorTemp);
                    }

                }

                // 控制器
                if (symbol.getType().trim().equals("1")){
                    int current = 0;
                    // 控制器
                    for (int i = 0; i < controllerData.size(); i++){
                        if(ControllerSet.contains(i)) continue;
                        DeviceController s = controllerData.get(i);
                        if (symbol.getRuleName().equals(s.getRuleName())) {// 表示对应上来了
                            ControllerSet.add(i);// 记录
                            s.setName(symbol.getName());
                            s.setTime(new Date());
                            // 给data赋值
                            s.setData(s.getState() == true ? "on" : "off");
                            current = 1;
                            saveControllerData.add(s);
                            break;
                        }

                    }

                    if(current == 0){
                        DeviceController deviceControllerTemp = new DeviceController();
                        deviceControllerTemp.setRuleName(symbol.getRuleName());
                        deviceControllerTemp.setName(symbol.getName());
                        deviceControllerTemp.setTime(new Date());
                        saveControllerData.add(deviceControllerTemp);
                    }
                }
            }


            // 数据存储
            log.info("保存传感器数据:"+saveSensorData.toString());
            log.info("保存控制器数据:"+saveControllerData.toString());
            // 将数据保存到缓存中
            deviceDataService.saveSensorsRedis(saveSensorData);
            deviceDataService.saveControllerRedis(saveControllerData);
            // 将数据保存到数据库中
            deviceDataService.saveSensors(saveSensorData);
            deviceDataService.saveController(saveControllerData);

//            // 表示硬件传递的数据没有此数据
//            for (int i = 0; i < sensorData.size(); i++) {
//                DeviceSensor s = sensorData.get(i);
//                s.setTime(new Date());// 添加时间
//                // 匹配符号
//                for (DeviceSymbol symbol : rulesSymbols) {
//                    if (symbol.getRuleName().equals(s.getRuleName())) {
//                        s.setName(symbol.getName());// 设置名称
//                        s.setSymbol(symbol.getSymbol());// 设置符号
//                        break;
//                    }else{
//                        DeviceSensor deviceSensorTemp = new DeviceSensor();
//                        deviceSensorTemp.setName(symbol.getName());
//                        deviceSensorTemp.setRuleName(symbol.getRuleName());
//                        deviceSensorTemp.setTime(new Date());
//                        sensorData.add(deviceSensorTemp);
//                    }
//                }
//            }





        }catch (Exception e){
            System.out.println("出现异常:"+e.toString());
        }


    }

}