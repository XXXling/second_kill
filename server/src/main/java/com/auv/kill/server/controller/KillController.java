package com.auv.kill.server.controller;

import com.auv.kill.api.enums.StatusCode;
import com.auv.kill.api.response.BaseResponse;
import com.auv.kill.model.dto.KillSuccessUserInfo;
import com.auv.kill.model.mapper.ItemKillSuccessDao;
import com.auv.kill.server.dto.KillDto;
import com.auv.kill.server.service.KillService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/12 17:24
 */
@Controller
@RequestMapping("/kill")
public class KillController {
    private static final Logger log= LoggerFactory.getLogger(KillController.class);

    @Autowired
    private KillService killService;

    @Autowired
    private ItemKillSuccessDao itemKillSuccessDao;

    /***
     * 商品秒杀核心业务逻辑
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = "/execute", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BaseResponse execute(@RequestBody @Validated KillDto dto, BindingResult result, HttpSession session){
        if (result.hasErrors() || dto.getKillId() <= 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        Object uId=session.getAttribute("uid");
        if (uId==null){
            return new BaseResponse(StatusCode.UserNotLogin);
        }
        //Integer userId=dto.getUserId();
        Integer userId= (Integer)uId ;

        try{
            // Boolean rst = killService.killItem(dto.getKillId(), dto.getUserId());
            Boolean rst = killService.killItemV4(dto.getKillId(), userId);
            if (!rst){
                return new BaseResponse(StatusCode.Fail.getCode(),"商品已抢购完毕或者不在活动时间内");
            }
        }catch (Exception e) {
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return new BaseResponse(StatusCode.Success);
    }

    //抢购成功跳转页面
    @RequestMapping(value = "/execute/success",method = RequestMethod.GET)
    public String executeSuccess(){
        return "executeSuccess";
    }

    //抢购失败跳转页面
    @RequestMapping(value = "/execute/fail",method = RequestMethod.GET)
    public String executeFail(){
        return "executeFail";
    }

    /**
     *  秒杀成功订单详情页面
     * TODO
     * @param orderNo:
     * @param modelMap:
     * @return: java.lang.String
     */
    @RequestMapping(value = "/record/detail/{orderNo}", method = RequestMethod.GET)
    public String killRecordDetail(@PathVariable String orderNo, ModelMap modelMap){
        if (StringUtils.isBlank(orderNo)){
            return "error";
        }
        KillSuccessUserInfo info = itemKillSuccessDao.findByCode(orderNo);
        if (info == null) {
            return "error";
        }
        modelMap.put("info",info);
        return "killRecord";
    }




    /***
     * 商品秒杀核心业务逻辑-用于压力测试
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = "/execute/lock",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BaseResponse executeLock(@RequestBody @Validated KillDto dto, BindingResult result){
        if (result.hasErrors() || dto.getKillId() <= 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }

        try{
            // 不加分布式锁
           /* Boolean rst = killService.killItem(dto.getKillId(), dto.getUserId());
            if (!rst){
                return new BaseResponse(StatusCode.Fail.getCode(),"不加分布式锁，商品已抢购完毕或者不在活动时间内");
            }*/
           // 基于redis分布式锁的前提
            /*Boolean rst = killService.killItemV3(dto.getKillId(), dto.getUserId());
            if (!rst){
                return new BaseResponse(StatusCode.Fail.getCode(),"不加分布式锁，商品已抢购完毕或者不在活动时间内");
            }*/
            // 基于redisson分布式锁的前提
            /*Boolean rst = killService.killItemV4(dto.getKillId(), dto.getUserId());
            if (!rst){
                return new BaseResponse(StatusCode.Fail.getCode(),"不加分布式锁，商品已抢购完毕或者不在活动时间内");
            }*/
            // 基于ZooKeeper分布式锁的前提
            Boolean rst = killService.killItemV5(dto.getKillId(), dto.getUserId());
            if (!rst){
                return new BaseResponse(StatusCode.Fail.getCode(),"不加分布式锁，商品已抢购完毕或者不在活动时间内");
            }
        }catch (Exception e) {
            log.error("商品抢购异常，case:{}，errorMessage:{}",e.fillInStackTrace(),e.getMessage());
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return new BaseResponse(StatusCode.Success);
    }
}
