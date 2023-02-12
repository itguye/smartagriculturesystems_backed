package com.dudu.smartagriculture.controller;


import com.dudu.smartagriculture.common.api.CommonResult;
import com.dudu.smartagriculture.mqtt.MqttPushClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:Dong
 * @Date：2020/8/26 16:12
 */
@Controller
@RequestMapping("/mqtt")
@Api(tags = "MqttController", description = "Mqtt管理")
public class MqttController {
    @Autowired
    private MqttPushClient mqttPushClient;

    @ApiOperation(value = "向Mqtt服务器发送消息")
    @RequestMapping(value = "/sendMsg",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult sendMsg(@RequestParam(value ="msg") String msg) {
        try{
            mqttPushClient.publish("smartagriculture/sub/","{"+msg+"}");
        }catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
            return CommonResult.success("success");
    }

    @ApiOperation(value = "向Mqtt服务器发送消息测试")
    @RequestMapping("testPublishMessage2")
    public void testPublishMessage2(String message) {
        mqttPushClient.publish("test_queue", message);
    }


}
