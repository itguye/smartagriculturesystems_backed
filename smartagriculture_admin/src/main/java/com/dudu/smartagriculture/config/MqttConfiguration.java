package com.dudu.smartagriculture.config;

import com.dudu.smartagriculture.mqtt.MqttPushClient;
import com.dudu.smartagriculture.mqtt.MqttSubClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * mqtt配置类，获取mqtt连接
 */
@Component
@Configuration
@ConfigurationProperties(MqttConfiguration.PREFIX)
@PropertySource(value = "./config/application-local.properties")
public class MqttConfiguration {

    @Resource
    private MqttPushClient mqttPushClient;
    //指定配置文件application-local.properties中的属性名前缀
    public static final String PREFIX = "ximo.mqtt";
    private String host;
    private String clientId;
    private String userName;
    private String password;
    private String topic;
    private int timeout;
    private int keepAlive;

    public void setMqttPushClient(MqttPushClient mqttPushClient) {
        this.mqttPushClient = mqttPushClient;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    /**
     * 连接至mqtt服务器，获取mqtt连接
     * @return
     */
    @Bean
    public MqttPushClient getMqttPushClient() {
        //连接至mqtt服务器，获取mqtt连接
        mqttPushClient.connect(host, clientId, userName, password, timeout, keepAlive);
        //一连接mqtt,就订阅默认需要订阅的主题（如test_queue）
        new MqttSubClient(mqttPushClient);
        return mqttPushClient;
    }
}