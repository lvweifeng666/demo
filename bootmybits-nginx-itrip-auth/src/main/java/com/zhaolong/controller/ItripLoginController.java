package com.zhaolong.controller;

import com.alibaba.fastjson.JSON;
import com.zhaolong.po.Dto;
import com.zhaolong.po.ItripUser;
import com.zhaolong.service.ItripUserService;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

@RestController
@RequestMapping(value="/api")
public class ItripLoginController {
    @Autowired
    private ItripUserService itripUserService;

    public ItripUserService getItripUserService() {
        return itripUserService;
    }

    public void setItripUserService(ItripUserService itripUserService) {
        this.itripUserService = itripUserService;
    }
    private Jedis jedis=new Jedis("127.0.0.1",6379);
    //登录验证
    @RequestMapping(value = "/dologin")
    public Dto dologin(HttpServletRequest request, HttpServletResponse response,String name,String password){
        System.out.println("登录。。。。。。" );
        //判断账号密码不为空
        if (!EmptyUtils.isEmpty(name)&&!EmptyUtils.isEmpty(password)){
            System.out.println("账号密码不为空。。。。。。");
            ItripUser user=null;
            user=itripUserService.dologin(new ItripUser(name.trim(), MD5Util.getMd5(password.trim(),32)));
            if (user!=null){
                //登陆成功
                //生成token
                String  token= TokenUtil.getTokenGenerator(request.getHeader("user-agent"),user);
                System.out.println("浏览器类型"+request.getHeader("user-agent"));
                //缓存token
                if(token.startsWith("token:PC-")){
                    //将token作为key，将user对象作为value，存入redis(因为将来取值需要返回对象所以value转成json)
                    String StrJSON= JSON.toJSONString(user);
                    jedis.setex(token,7200,StrJSON);
                    //专门存token值
                    jedis.setex("token",7200,token);
                    System.out.println("登录token："+jedis.get("token"));
                }
                ItripTokenVO tokenVO=new ItripTokenVO(token,
                        Calendar.getInstance().getTimeInMillis()+60*1000,
                        Calendar.getInstance().getTimeInMillis());
                System.out.println("登录成功返回参数："+tokenVO.toString());
                return DtoUtil.returnDataSuccess(tokenVO);
            }else{
                System.out.println("账号密码错误登录失败");
                return DtoUtil.returnFail("用户名密码错误", ErrorCode.AUTH_AUTHENTICATION_FAILED);
            }
        }else{
            System.out.println("账号密码有空登录失败");
            return DtoUtil.returnFail("用户名密码错误", ErrorCode.AUTH_AUTHENTICATION_FAILED);
        }
    }
    //退出登录
    @RequestMapping(value="/logout")
    public  Dto logout(javax.servlet.http.HttpServletRequest request) {
        System.out.println("注销用户");
        String token=jedis.get("token");
        System.out.println("token:+++++++>"+token);
        if (!TokenUtil.validate(request.getHeader("user-agent"),token)){
            System.out.println("时间异常");
            return DtoUtil.returnFail("token无效",ErrorCode.AUTH_TOKEN_INVALID);

        }
        try {
            //删除TOKEN信息
            TokenUtil.delete(token);
            return DtoUtil.returnSuccess("注销成功");
        }catch (Exception e){
            e.printStackTrace();
            return  DtoUtil.returnFail("注销失败",ErrorCode.AUTH_UNKNOWN);
        }

    }

    //置换token
    @RequestMapping(value="/retoken")
    public  Dto  replace(HttpServletRequest request){
        System.out.println("置换tolen");
        String token=null;
        String agent=request.getHeader("user-agent");
        token=jedis.get("token");
        System.out.println(agent);
        System.out.println(token);
        if(token==null){
            return DtoUtil.returnFail("token已经过期，请重新登录",ErrorCode.AUTH_UNKNOWN);

        }
        String newToken= null;
        try {
            newToken = TokenUtil.replaceToken(agent,token);
            return  DtoUtil.returnSuccess(newToken);
        } catch (TokenValidationFailedException e) {
            e.printStackTrace();
            return DtoUtil.returnFail("token置换失败",ErrorCode.AUTH_UNKNOWN);
        }


    }

}
