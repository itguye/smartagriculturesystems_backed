package com.dudu.smartagriculture.quartz;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.dudu.smartagriculture.easyrules.smartrule.EasyRuleTool;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定义任务
 */
@Component
public class DongAoJob extends QuartzJobBean {

    @Resource
    private EasyRuleTool easyRuleTool;


    private static final Log logger = LogFactory.getLog(DongAoJob.class);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            if (easyRuleTool != null) {
                easyRuleTool.executeRule();// 执行规则任务
            }else{
                logger.info("easyRuleTool出现null");
            }

        } catch (Exception e) {
            logger.error("出现异常:"+e);
        }
        logger.info("定时任务正常执行中");

    }
}
