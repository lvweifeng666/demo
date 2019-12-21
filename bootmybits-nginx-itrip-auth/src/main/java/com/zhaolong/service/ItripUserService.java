package com.zhaolong.service;

import com.zhaolong.po.ItripUser;

public interface ItripUserService {
    //判断用户是否存在
    public ItripUser findByUserCode(ItripUser user);
    //增加用户
    public int codeUserSave(ItripUser user);
    //修改用户激活状态
    public int updateUserActivated(ItripUser user);
    //登录验证
    public ItripUser dologin(ItripUser user);
}
