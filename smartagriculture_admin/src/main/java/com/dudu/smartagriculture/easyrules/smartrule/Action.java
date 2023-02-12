package com.dudu.smartagriculture.easyrules.smartrule;

import com.dudu.smartagriculture.config.MqttConfiguration;
import com.dudu.smartagriculture.mbg.mapper.RulesActionsMapper;
import com.dudu.smartagriculture.mbg.model.RulesActions;
import com.dudu.smartagriculture.mbg.model.RulesActionsExample;
import com.dudu.smartagriculture.mqtt.MqttPushClient;
import java.util.List;

public class Action {
    // source:null,action_name:"",act:null
    private String source;
    private String action_name;
    private Boolean act;
    private String sendMsg;

    public Action() {
    }

    public Action(String source, String action_name, Boolean act) {
        this.source = source;
        this.action_name = action_name;
        this.act = act;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public Boolean getAct() {
        return act;
    }

    public void setAct(Boolean act) {
        this.act = act;
    }

    // 将执行的动作发送给下位机
    public void send(String msg){
        if (msg == null) {
            System.out.println("没有内容可以发");
            return;
        }
        // 连接Mqtt服务器
        MqttConfiguration configuration = new MqttConfiguration();
        // host, clientId, userName, password, timeout, keepAlive
        configuration.setHost("tcp://www.itguye.top:18084");
        configuration.setClientId("role_client");
        configuration.setUserName("admin");
        configuration.setPassword("public");
        configuration.setTimeout(1000);
        configuration.setKeepAlive(60);

        // 获取发布客户端
        MqttPushClient mqttPushClient = new MqttPushClient();
        // 进行连接
        mqttPushClient.connect(configuration.getHost(),configuration.getClientId(),configuration.getUserName(),configuration.getPassword(),configuration.getTimeout(),configuration.getKeepAlive());
        mqttPushClient.publish("smartagriculture/sub/",msg.toString());
        System.out.println("执行规则:"+msg);


    }

}
