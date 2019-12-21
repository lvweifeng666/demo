package com.zhaolong.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhaolong.po.Dto;
import com.zhaolong.po.ItripUser;
import com.zhaolong.po.ItripUserLinkUser;
import com.zhaolong.service.ItripUserLinkUserService;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value="/api/userinfo")
public class ItripUserLinkUserController {
    @Autowired
    private ItripUserLinkUserService itripUserLinkUserService;

    public ItripUserLinkUserService getItripUserLinkUserService() {
        return itripUserLinkUserService;
    }

    public void setItripUserLinkUserService(ItripUserLinkUserService itripUserLinkUserService) {
        this.itripUserLinkUserService = itripUserLinkUserService;
    }
    private Jedis jedis=new Jedis("127.0.0.1",6379);
    //修改
   /* @ApiOperation(value = "修改常用联系人接口", httpMethod = "POST",
            protocols = "HTTP",produces = "application/json",
            response = Dto.class,notes = "修改常用联系人信息"+
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>"+
            "<p>100421 : 修改常用联系人失败 </p>"+
            "<p>100422 : 不能提交空，请填写常用联系人信息</p>"+
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value="/modifyuserlinkuser")
    public Dto<Object> updateUserLinkUser(@RequestBody ItripModifyUserLinkUserVO itripModifyUserLinkUserVO, HttpServletRequest request, HttpServletResponse response){
        System.out.println("修改常用联系人");
        //获取redis中存储的key值---token
        String tokenstr=jedis.get("token");
        //将key值token（pc-XXX）的value转化为json字符串
        String jsontoken=jedis.get(tokenstr).toString();
        JSONObject jsonObject = JSONObject.parseObject(jedis.get(tokenstr).toString());
        //将json字符串转成用户对象
        ItripUser currentUser = (ItripUser) JSONObject.toJavaObject(jsonObject,ItripUser.class);
        System.out.println(currentUser.toString());
        if(null != currentUser && null != itripModifyUserLinkUserVO){
            ItripUserLinkUser itripUserLinkUser = new ItripUserLinkUser();
            itripUserLinkUser.setId(itripModifyUserLinkUserVO.getId());
            itripUserLinkUser.setLinkUserName(itripModifyUserLinkUserVO.getLinkUserName());
            itripUserLinkUser.setLinkIdCardType(itripModifyUserLinkUserVO.getLinkIdCardType());
            itripUserLinkUser.setLinkIdCard(itripModifyUserLinkUserVO.getLinkIdCard());
            itripUserLinkUser.setUserId(currentUser.getId());
            itripUserLinkUser.setLinkPhone(itripModifyUserLinkUserVO.getLinkPhone());
            itripUserLinkUser.setModifiedBy(currentUser.getId());
            try {
                System.out.println("修改对象："+itripUserLinkUser);
                itripUserLinkUserService.updateUsersLinkUser(itripUserLinkUser);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("修改常用联系人失败", "100421");
            }
            return DtoUtil.returnSuccess("修改常用联系人成功");
        }else if(null != currentUser && null == itripModifyUserLinkUserVO){
            return DtoUtil.returnFail("不能提交空，请填写常用联系人信息","100422");
        }else{
            return DtoUtil.returnFail("token失效，请重新登录", "100000");
        }
    }

    /*删除*/
   /* @ApiOperation(value = "删除常用联系人接口", httpMethod = "GET",
            protocols = "HTTP",produces = "application/json",
            response = Dto.class,notes = "删除常用联系人信息"+
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>"+
            "<p>100431 : 所选的常用联系人中有与某条待支付的订单关联的项，无法删除 </p>"+
            "<p>100432 : 删除常用联系人失败 </p>"+
            "<p>100433 : 请选择要删除的常用联系人</p>"+
            "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value="/deluserlinkuser")
    @ResponseBody
    public Dto<Object> delUserLinkUser(HttpServletRequest request, HttpServletResponse response,Long[] ids) {
        System.out.println("删除常用联系人");
        //获取redis中存储的key值---token
        String tokenstr=jedis.get("token");
        //将key值token（pc-XXX）的value转化为json字符串
        String jsontoken=jedis.get(tokenstr).toString();
        JSONObject jsonObject = JSONObject.parseObject(jedis.get(tokenstr).toString());
        //将json字符串转成用户对象
        ItripUser currentUser = (ItripUser) JSONObject.toJavaObject(jsonObject,ItripUser.class);
        System.out.println(currentUser.toString());
        List<Long> idsList = new ArrayList<Long>();
        if(null != currentUser && EmptyUtils.isNotEmpty(ids)){
            try {
                //订单部分暂时不处理
               /* List<Long> linkUserIds = itripOrderLinkUserService.getItripOrderLinkUserIdsByOrder();
                Collections.addAll(idsList, ids);
                idsList.retainAll(linkUserIds);
                if(idsList.size() > 0)
                {
                    return DtoUtil.returnFail("所选的常用联系人中有与某条待支付的订单关联的项，无法删除","100431");
                }else{*/

                System.out.println(ids[0]);
                itripUserLinkUserService.deleteUserLinkUserByIds(ids);
                System.out.println("删除成功");
                return DtoUtil.returnSuccess("删除常用联系人成功");
                /*   }*/
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("删除常用联系人失败","100432");
            }
            //return DtoUtil.returnSuccess("删除常用联系人成功");
        }else if(null != currentUser && EmptyUtils.isEmpty(ids)){
            System.out.println("选择删除联系人");
            return DtoUtil.returnFail("请选择要删除的常用联系人","100433");
        }else{
            return DtoUtil.returnFail("token失效，请重新登录","100000");
        }
    }
    /*查询常用联系人*/
    /**
     * 根据UserId,联系人姓名查询常用联系人-add by donghai
     * @return
     * @throws Exception
     */
   /* @ApiOperation(value = "查询常用联系人接口", httpMethod = "POST",
            protocols = "HTTP",produces = "application/json",
            response = Dto.class,notes = "查询常用联系人信息(可根据联系人姓名进行模糊查询)"+
            "<p>若不根据联系人姓名进行查询，不输入参数即可 | 若根据联系人姓名进行查询，须进行相应的入参，比如：{\"linkUserName\":\"张三\"}</p>" +
            "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
            "<p>错误码：</p>"+
            "<p>100401 : 获取常用联系人信息失败 </p>"+
            "<p>100000 : token失效，请重登录</p>")*/
    @RequestMapping(value = "/queryuserlinkuser")
    public  Dto<ItripUserLinkUser> queryUserLinkUser(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripSearchUserLinkUserVO itripSearchUserLinkUserVO){
        System.out.println("查询常用联系人");
        //获取redis中缓存的key值--token
        String strtoken=jedis.get("token");
        //通过key值（pc-XXX）找到json的value转为字符
        JSONObject jsonobject=JSONObject.parseObject(jedis.get(strtoken).toString());
        //将jsonobject字符串转化为user对象
        ItripUser currentUser=(ItripUser) JSONObject.toJavaObject(jsonobject,ItripUser.class);
        System.out.println(currentUser.toString());
        List<ItripUserLinkUser> userLinkUsers=new ArrayList<ItripUserLinkUser>();
        String linkUserName=(itripSearchUserLinkUserVO==null)?null:itripSearchUserLinkUserVO.getLinkUserName();
        if(currentUser!=null){
            Map param=new HashMap();
            param.put("userId",currentUser.getId());
            param.put("linkUserName",linkUserName);
            try {
                userLinkUsers=itripUserLinkUserService.findAllUserLinkUserByMap(param);
            for(int i=0;i<userLinkUsers.size();i++){
                System.out.println("查询返回结果："+userLinkUsers.get(i).toString());
            }
                return DtoUtil.returnSuccess("获取常用联系人信息成功",userLinkUsers);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("获取常用联系人信息失败","100401");
            }
        }else{
            return DtoUtil.returnFail("token失效，请重新登录","100000");
        }
        }


    /*  新增常用联系人接口, httpMethod = "POST",
             protocols = "HTTP",produces = "application/json",
            response = Dto.class,notes = "新增常用联系人信息"+
             "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
             "<p>错误码：</p>"+
             "<p>100411 : 新增常用联系人失败 </p>"+
             "<p>100412 : 不能提交空，请填写常用联系人信息</p>"+
             "<p>100000 : token失效，请重登录 </p>")*/
    @RequestMapping(value = "/adduserlinkuser")
    public Dto addUserLinkUser(HttpServletRequest request, HttpServletResponse response, @RequestBody ItripAddUserLinkUserVO itripAddUserLinkUserVO) {
        System.out.println("新增常用联系人");
        //获取redis中缓存的key值--token
        String strtoken=jedis.get("token");
        //通过key值（pc-XXX）找到json的value转为字符
        JSONObject jsonobject=JSONObject.parseObject(jedis.get(strtoken).toString());
        //将jsonobject字符串转化为user对象
        ItripUser currentUser=(ItripUser) JSONObject.toJavaObject(jsonobject,ItripUser.class);
        System.out.println(currentUser.toString());
        //用户对象不为空，注入数据
        if (currentUser!=null&&itripAddUserLinkUserVO!=null){
            System.out.println(itripAddUserLinkUserVO.toString());
            ItripUserLinkUser ItripUserLinkUser=new ItripUserLinkUser();
            ItripUserLinkUser.setLinkUserName(itripAddUserLinkUserVO.getLinkUserName());
            ItripUserLinkUser.setLinkIdCardType(itripAddUserLinkUserVO.getLinkIdCardType());
            ItripUserLinkUser.setLinkIdCard(itripAddUserLinkUserVO.getLinkIdCard());
            ItripUserLinkUser.setLinkPhone(itripAddUserLinkUserVO.getLinkPhone());
            ItripUserLinkUser.setUserId(currentUser.getId());
            ItripUserLinkUser.setCreatedBy(currentUser.getId());
            ItripUserLinkUser.setCreationDate(new Date(System.currentTimeMillis()));
            try {
                System.out.println("新增联系人返回数据："+ItripUserLinkUser.toString());
                itripUserLinkUserService.addUsersLinkUser(ItripUserLinkUser);
            } catch (Exception e) {
                e.printStackTrace();
                return DtoUtil.returnFail("新增常用联系人失败", "100411");
            }
            return DtoUtil.returnSuccess("新增常用联系人成功");
        } else if (null != currentUser && null == itripAddUserLinkUserVO) {
            return DtoUtil.returnFail("不能提交空，请填写常用联系人信息", "100412");
        } else {
            return DtoUtil.returnFail("token失效，请重新登录", "100000");
        }
    }
}
