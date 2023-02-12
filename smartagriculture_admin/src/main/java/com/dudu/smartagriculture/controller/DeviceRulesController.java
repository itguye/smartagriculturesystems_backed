package com.dudu.smartagriculture.controller;


import com.dudu.smartagriculture.common.api.CommonPage;
import com.dudu.smartagriculture.common.api.CommonResult;
import com.dudu.smartagriculture.dto.RulesFormsDataDto;
import com.dudu.smartagriculture.dto.RulesListDto;
import com.dudu.smartagriculture.service.DeviceRulesService;
import com.dudu.smartagriculture.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/devicepolicies")
@Api(tags = "RulesController", description = "规则管理")
public class DeviceRulesController {
    @Value("${easyrules.facts.trigger}")
    private String prefix;
    @Resource
    private DeviceRulesService deviceRulesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRulesController.class);

    public DeviceRulesController() {
    }


    @ApiOperation(value = "添加规则")
    @RequestMapping(value = "/addScene",method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('devicepolicies:devicepolicies:insert')")
    public CommonResult addScene(@RequestBody  RulesFormsDataDto form){

        // 保存对象
        try {
            deviceRulesService.addRules(form, prefix);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }

        return CommonResult.success("success");
    }

    @ApiOperation(value = "修改规则")
    @RequestMapping(value = "/updateScene",method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('devicepolicies:devicepolicies:update')")
    public CommonResult updateScene(@RequestBody  RulesFormsDataDto form){
        // 保存对象
        try {
            deviceRulesService.updateScene(form,prefix);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }

        return CommonResult.success("success");
    }


    @ApiOperation(value = "获取所有规则")
    @RequestMapping(value = "/getAllList",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<RulesListDto>> getAllList(@RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){

        try{
            List<RulesListDto> allList = deviceRulesService.getAllListByPagination(keyword, pageSize, pageNum);
            if (allList != null) {
                return CommonResult.success(CommonPage.restPage(allList));
            }

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.failed();
    }



    @ApiOperation(value = "通过id获取规则")
    @RequestMapping(value = "/getSceneById/{id}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<RulesFormsDataDto> getSceneById(@PathVariable("id")int id){

        try{
            return CommonResult.success(deviceRulesService.getSceneById(id));
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }





    @ApiOperation(value = "删除规则")
    @RequestMapping(value = "/delRules/{id}",method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('devicepolicies:devicepolicies:delete')")
    public CommonResult delRules(@PathVariable("id") int id){
        try {
            ArrayList<Integer> integers = new ArrayList<>();
            integers.add(id);
            deviceRulesService.delRules(integers);// 删除指定规则
            return CommonResult.success("success");
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "批量删除删除规则")
    @RequestMapping(value = "/delChecksRules",method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('devicepolicies:devicepolicies:delete')")
    public CommonResult delChecksRules(@RequestBody  List<Integer> ids){
        try {
            deviceRulesService.delRules(ids);// 删除指定规则
            return CommonResult.success("success");
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }

    @ApiOperation(value = "导出策略列表Excel")
    @RequestMapping(value = "/exportMemberList", method = RequestMethod.GET)
    public void exportMemberList(@ApiIgnore HttpServletResponse response) {

        try {
            // 设置下载弹窗的文件名和格式（文件名要包括名字和文件格式）
            List<RulesListDto> allList = deviceRulesService.getAllListByKeyword(null);// 获取数据
            //导出操作
            ExcelUtil.exportExcel(allList,"智慧农业监测系统策略记录表","策略管理", RulesListDto.class,"智慧农业监测系统策略记录表.xlsx",response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

    }




}
