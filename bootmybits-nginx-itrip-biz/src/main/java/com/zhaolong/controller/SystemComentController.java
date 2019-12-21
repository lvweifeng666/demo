package com.zhaolong.controller;

import com.zhaolong.po.Dto;
import com.zhaolong.service.ITripComentService;
import com.zhaolong.service.ITripImageService;
import com.zhaolong.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/api/comment")
public class SystemComentController {

    @Autowired
    private ITripComentService iTripComentService;

    @Autowired
    private ITripImageService iTripImageService;

    public ITripComentService getiTripComentService() {
        return iTripComentService;
    }

    public void setiTripComentService(ITripComentService iTripComentService) {
        this.iTripComentService = iTripComentService;
    }



    public ITripImageService getiTripImageService() {
        return iTripImageService;
    }

    public void setiTripImageService(ITripImageService iTripImageService) {
        this.iTripImageService = iTripImageService;
    }

    @RequestMapping(value = "/gethotelscore/{hotelId}")
    public Dto<Object> getHotelScore(@PathVariable String hotelId) {
        System.out.println("根据酒店id查询酒店平均分。。。");

        Dto<Object> dtoo = new Dto<Object>();
        if (null != hotelId && !"".equals(hotelId)) {
            ItripScoreCommentVO itripScoreCommentVO = new ItripScoreCommentVO();
            try {
                itripScoreCommentVO = iTripComentService.getCommentAvgScore(Long.valueOf(hotelId));
                dtoo = DtoUtil.returnSuccess("获取评分成功", itripScoreCommentVO);
            } catch (Exception e) {
                e.printStackTrace();
                dtoo = DtoUtil.returnFail("获取评分失败", "100001");
            }
        } else {
            dtoo = DtoUtil.returnFail("hotelId不能为空", "100002");
        }
        return dtoo;
    }

//根据酒店ID查询评论数

    @RequestMapping(value = "/getcount/{hotelId}")
    public Dto<Object> getCommentCountByType(@PathVariable String hotelId){
        System.out.println("根据酒店id查询各类评论数。。。");
        Dto<Object> dto = new Dto<Object>();
        System.out.println("11111111111111");
        Integer count = 0;
        Map<String,Integer> countMap = new HashMap<String,Integer>();
        Map<String,Object> param = new HashMap<String,Object>();
        if(null != hotelId && !"".equals(hotelId)){
            System.out.println("222222222");
            param.put("hotelId",hotelId);
            count = getItripCommentCountByMap(param);
            if(count != -1){
                countMap.put("allcomment",count);
            }else{
                return DtoUtil.returnFail("获取酒店总评论数失败","100014");
            }

            System.out.println("3333333333333");
            param.put("isOk",1);//0：有待改善 1：值得推荐
            count = getItripCommentCountByMap(param);
            if(count != -1){
                countMap.put("isok",count);
            }else{
                return DtoUtil.returnFail("获取酒店值得推荐评论数失败","100017");
            }

            System.out.println("444444444");
            param.put("isOk",0);//0：有待改善 1：值得推荐
            count = getItripCommentCountByMap(param);
            if(count != -1){
                countMap.put("improve",count);
            }else{
                return DtoUtil.returnFail("获取酒店有待改善评论数失败","100016");
            }

            param.put("isHavingImg",1);//0:无图片 1:有图片
            param.put("isOk",null);
            count = getItripCommentCountByMap(param);
            if(count != -1){
                countMap.put("havingimg",count);
            }else{
                return DtoUtil.returnFail("获取酒店有图片评论数失败","100015");
            }

        }else{
            return DtoUtil.returnFail("参数hotelId为空","100018");
        }
        dto = DtoUtil.returnSuccess("获取酒店各类评论数成功",countMap);
        return dto;
    }

    public Integer getItripCommentCountByMap(Map<String,Object> param){
        Integer count  = -1;
        try {
            count =  iTripComentService.getItripCommentCountByMap(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


//根据评论类型查询评论列表，并分页显示
    /*@ApiOperation(value = "根据评论类型查询评论列表，并分页显示", httpMethod = "POST",
                protocols = "HTTP",produces = "application/json",
                response = Dto.class,notes = "根据评论类型查询评论列表，并分页显示"+
                "<p>参数数据e.g：</p>" +
                "<p>全部评论：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":-1,\"pageSize\":5,\"pageNo\":1}</p>" +
                "<p>有图片：{\"hotelId\":10,\"isHavingImg\":1,\"isOk\":-1,\"pageSize\":5,\"pageNo\":1}</p>" +
                "<p>值得推荐：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":1,\"pageSize\":5,\"pageNo\":1}</p>" +
                "<p>有待改善：{\"hotelId\":10,\"isHavingImg\":-1,\"isOk\":0,\"pageSize\":5,\"pageNo\":1}</p>" +
                "<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
                "<p>错误码：</p>"+
                "<p>100020 : 获取评论列表错误 </p>")*/
    @RequestMapping(value = "/getcommentlist")
    public Dto<Object> getCommentList(@RequestBody ItripSearchCommentVO itripSearchCommentVO){
        System.out.println("根据酒店id查询评论分页列表");
        Dto<Object> dto = new Dto<Object>();
        Map<String,Object> param=new HashMap<>();
        if(itripSearchCommentVO.getIsOk()== -1){
            itripSearchCommentVO.setIsOk(null);

        }
        if(itripSearchCommentVO.getIsHavingImg() == -1){
            itripSearchCommentVO.setIsHavingImg(null);

        }
        param.put("hotelId",itripSearchCommentVO.getHotelId());
        param.put("isHavingImg",itripSearchCommentVO.getIsHavingImg());
        param.put("isOk",itripSearchCommentVO.getIsOk());
        try{
            Page page = iTripComentService.queryItripCommentPageByMap(param,
                    itripSearchCommentVO.getPageNo(),
                    itripSearchCommentVO.getPageSize());
            dto = DtoUtil.returnDataSuccess(page);
        }catch (Exception e){
            e.printStackTrace();
            dto = DtoUtil.returnFail("获取评论列表错误","100020");
        }

        return dto;
    }



//根据targetId查询评论照片

    /*@ApiOperation(value = "根据targetId查询评论照片(type=2)", httpMethod = "GET",
			protocols = "HTTP",produces = "application/json",
			response = Dto.class,notes = "总体评分、位置评分、设施评分、服务评分、卫生评分"+
			"<p>成功：success = ‘true’ | 失败：success = ‘false’ 并返回错误码，如下：</p>" +
			"<p>错误码：</p>"+
			"<p>100012 : 获取评论图片失败 </p>"+
			"<p>100013 : 评论id不能为空</p>")*/
    @RequestMapping(value = "/getimg/{targetId}")

    public Dto<Object> getImgByTargetId(@PathVariable String targetId){
        System.out.println("根据targetId查询评论照片");
        Dto<Object> dto = new Dto<Object>();

        if(null != targetId && !"".equals(targetId)){
            List<ItripImageVO> itripImageVOList = null;
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("type","2");
            param.put("targetId",targetId);
            try {
                itripImageVOList = iTripImageService.getItripImageListByMap(param);
                dto = DtoUtil.returnSuccess("获取评论图片成功",itripImageVOList);
            } catch (Exception e) {
                e.printStackTrace();
                dto = DtoUtil.returnFail("获取评论图片失败","100012");
            }

        }else{
            dto = DtoUtil.returnFail("评论id不能为空","100013");
        }
        return dto;
    }
}