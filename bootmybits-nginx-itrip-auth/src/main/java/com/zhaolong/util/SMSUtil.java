package com.zhaolong.util;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.zhaolong.po.ItripUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class SMSUtil {
    public static String testcheck(String usercode){
        //初始化SDK
        CCPRestSmsSDK ccPRestSmsSDK = new CCPRestSmsSDK();
        //初始化服务器端口和地址
        ccPRestSmsSDK.init("app.cloopen.com","8883");
        //设置账号和auth码
        ccPRestSmsSDK.setAccount("8aaf07086e0115bb016e90a82a855526","44b0abcd5e5249f3af71bce71b247fad");
        //设置appid
        ccPRestSmsSDK.setAppId("8aaf07086e0115bb016e90a82ae4552d");
        //**************************************举例说明***********************************************************************
        //*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
        //*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
        //*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
        //*********************************************************************************************************************

        //获取当前时间毫秒的后四位作为验证码
        Date date=new Date();
        long datetime=date.getTime();
        //保留后四位数字为验证码
        String val=""+datetime;
        String smsck=val.substring(val.length()-4,val.length());
        System.out.println("验证码-->"+smsck);
        HashMap<String,Object> result = ccPRestSmsSDK.sendTemplateSMS("13012246899","1" ,new String[]{smsck,"2"});
        //判断短信是否发送成功
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            System.out.println("短信发送成功");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
            ItripUser users=new ItripUser();
           return smsck;
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }
        return null;
    }

}
