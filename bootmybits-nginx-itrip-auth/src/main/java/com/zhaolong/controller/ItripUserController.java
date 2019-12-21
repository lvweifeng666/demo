package com.zhaolong.controller;

import com.zhaolong.po.Dto;
import com.zhaolong.po.ItripUser;
import com.zhaolong.service.ItripUserService;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value="/api")
public class ItripUserController {
    @Autowired
    private ItripUserService itripUserService;

    public ItripUserService getItripUserService() {
        return itripUserService;
    }

    public void setItripUserService(ItripUserService itripUserService) {
        this.itripUserService = itripUserService;
    }
    private Jedis jedis=new Jedis("127.0.0.1",6379);
    //手机注册
    @RequestMapping(value="/registerbyphone")
    public Dto codesave(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripUserVO userVO){
        System.out.println("手机注册。。。");
        try {
            System.out.println("获取到的数据："+userVO.toString());
            //将前台数据封装到po
            ItripUser user=new ItripUser();
            user.setUsercode(userVO.getUserCode());//手机号码
            user.setUserpassword(userVO.getUserPassword());
            user.setUsertype(0);//区分第三方登录
            user.setUsername(userVO.getUserName());
            user.setActivated(0);//是否激活
            //判断是否注册
            ItripUser olduser=itripUserService.findByUserCode(user);
            if(olduser==null){
                //没有注册过
                //处理密码使用md5加密
                user.setUserpassword(MD5Util.getMd5(user.getUserpassword(),32));
                System.out.println("封装后："+user.toString());
                //添加用户
                itripUserService.codeUserSave(user);
                //发送短信验证码
                String codecheck= SMSUtil.testcheck(user.getUsercode());
                //redis缓存短信验证码
                jedis.setex(user.getUsercode(),120,codecheck);
                return DtoUtil.returnSuccess();
            }else{
                //已经注册过(是否激活)
                if(olduser.getActivated()==0&&jedis.get(olduser.getUsercode())==null){
                    //用户注册过但没有激活重新发送验证
                    //发送短信验证码
                    String codecheck= SMSUtil.testcheck(user.getUsercode());
                    //redis缓存短信验证码
                    jedis.setex(user.getUsercode(),120,codecheck);
                    return DtoUtil.returnFail("用户已存在，但未激活，请重新激活",ErrorCode.AUTH_AUTHENTICATION_UPDATE);
                }
                System.out.println("已经存在用户");
                return  DtoUtil.returnFail("用户已经存在，注册失败", ErrorCode.AUTH_AUTHENTICATION_FAILED);
            }

        }catch (Exception e){
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }

    }
    /*手机验证
     * user -- 手机号
     * code -- 验证码
     *
     * */

    @RequestMapping(value="/validatephone")
    public Dto validatePhone(HttpServletRequest request, HttpServletResponse response,String user,String code){
        System.out.println("手机验证。。。");
        try {
            //获取redis中的该手机号的缓存数据
            System.out.println(jedis.get(user));
            if (jedis.get(user)!=null){
                System.out.println("3333333333333");
                //匹配
                if (jedis.get(user).equals(code)) {
                    ItripUser users = new ItripUser();
                    users.setUsercode(user);
                    //修改状态
                    System.out.println("1111111111111111");
                    itripUserService.updateUserActivated(users);
                    return DtoUtil.returnSuccess("激活成功");
                }
            }else{
                return DtoUtil.returnFail("验证失败",ErrorCode.AUTH_AUTHENTICATION_FAILED);
            }
        }catch (Exception e){
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }
        return null;
    }
    //用户名验证
    @RequestMapping(value="/ckusr")
    public @ResponseBody Dto checkUser(String name){
        System.out.println("用户名重复验证");
        try{
            ItripUser user=new ItripUser();
            user.setUsercode(name);
            if (itripUserService.findByUserCode(user)==null){
                return DtoUtil.returnSuccess("用户名可用");
            }else{
                return DtoUtil.returnFail("用户已存在，注册失败", ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_UNKNOWN);
        }
    }
    //邮箱注册
    @RequestMapping(value="/doregister")
    public Dto doRegister(@RequestBody ItripUserVO userVO) {
        System.out.println("邮箱注册。。。");
        try{
            //将获取到的数据封装到指定对象
            //将前台数据封装到PO
            ItripUser user=new ItripUser();
            user.setUsercode(userVO.getUserCode());//邮箱号码
            user.setUserpassword(userVO.getUserPassword());
            user.setUsertype(0);
            user.setUsername(userVO.getUserName());
            user.setActivated(0);//是否激活
            //判断是否注册
            ItripUser olduser=itripUserService.findByUserCode(user);
            if (olduser==null){
                //未注册过
                //使用MD5加密密码
                user.setUserpassword(MD5Util.getMd5(user.getUserpassword(),32));
                System.out.println("封装后"+user.toString());
                //添加用户
                itripUserService.codeUserSave(user);
                //发送邮箱验证码
                String emailcheck=EmailUtil.emailregister(user);
                //缓存验证码
                jedis.setex(user.getUsercode(),120,emailcheck);
                return DtoUtil.returnSuccess();
            }else{
                System.out.println("已经存在用户");
                return  DtoUtil.returnFail("用户已经存在，注册失败", ErrorCode.AUTH_AUTHENTICATION_FAILED);
            }
        }catch(Exception e){
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }

    }
    /*邮箱验证
     * user -- 手机号
     * code -- 验证码
     *
     * */

    /*邮箱验证*/
    @RequestMapping(value="/activate")
    public Dto activate(HttpServletRequest request, HttpServletResponse response,String user,String code) {
        System.out.println("邮箱验证。。。");
        try {
            //获取redis中的该手机号的缓存数据
            System.out.println(jedis.get(user));
            if (jedis.get(user) != null) {
                //匹配
                if (jedis.get(user).equals(code)) {
                    ItripUser users = new ItripUser();
                    users.setUsercode(user);
                    //修改状态
                    itripUserService.updateUserActivated(users);
                    return DtoUtil.returnSuccess("激活成功");
                }
            } else {
                ItripUser users = new ItripUser();
                users.setUsercode(user);
                //判断是否注册
                ItripUser olduser = itripUserService.findByUserCode(users);
                //已经注册过(是否激活)
                if (olduser.getActivated() == 0 && jedis.get(olduser.getUsercode()) == null) {
                    //用户注册过但没有激活重新发送验证
                    //发送邮箱验证码
                    String emailcheck = EmailUtil.emailregister(users);
                    //缓存验证码
                    jedis.setex(users.getUsercode(), 120, emailcheck);
                    return DtoUtil.returnFail("用户已存在，但未激活，请重新激活", ErrorCode.AUTH_AUTHENTICATION_UPDATE);
                }
                return DtoUtil.returnFail("验证失败", ErrorCode.AUTH_AUTHENTICATION_FAILED);
            }
        } catch (Exception e) {
            return DtoUtil.returnFail(e.getMessage(), ErrorCode.AUTH_UNKNOWN);
        }
        return null;
    }
}
