package com.zhaolong.mapper;

import com.zhaolong.po.ItripUser;

public interface ItripUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ItripUser record);

    int insertSelective(ItripUser record);

    ItripUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ItripUser record);

    int updateByPrimaryKey(ItripUser record);

    public ItripUser check(ItripUser user);
    //查询该用户是否存在
    public ItripUser findByUserCode(ItripUser user);
    //修改用户状态
    public int updateActivated(ItripUser user);
    //登录验证
    public ItripUser dologin(ItripUser user);
}