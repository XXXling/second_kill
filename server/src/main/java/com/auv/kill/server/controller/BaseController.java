package com.auv.kill.server.controller;

import com.auv.kill.api.enums.StatusCode;
import com.auv.kill.api.response.BaseResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/11 11:36
 */
@Controller
@RequestMapping("/base")
public class BaseController {

    @GetMapping("/welcome")
    public String wecomle(String name, ModelMap modelMap){
        if (StringUtils.isBlank(name)){
            name = "defult";
        }
        modelMap.put("name",name);
        return "welcome";
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    @ResponseBody
    public String data(String name){
        if (StringUtils.isBlank(name)){
            name = "标准数据";
        }
        return name;
    }

    @RequestMapping(value = "/response", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse response(String name){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        if (StringUtils.isBlank(name)){
            name = "标准请求";
        }
        response.setData(name);
        return response;
    }

    @RequestMapping(value = "error", method = RequestMethod.GET)
    public String error(){
        return "error";
    }
}
