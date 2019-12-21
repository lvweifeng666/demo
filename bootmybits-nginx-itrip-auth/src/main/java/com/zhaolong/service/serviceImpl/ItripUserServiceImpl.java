package com.zhaolong.service.serviceImpl;

import com.zhaolong.mapper.ItripUserMapper;
import com.zhaolong.po.ItripUser;
import com.zhaolong.service.ItripUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItripUserServiceImpl implements ItripUserService {
    @Autowired
    private ItripUserMapper itripUserMapper;

    public ItripUserMapper getItripUserMapper() {
        return itripUserMapper;
    }

    public void setItripUserMapper(ItripUserMapper itripUserMapper) {
        this.itripUserMapper = itripUserMapper;
    }

    @Override
    public ItripUser findByUserCode(ItripUser user) {
        return itripUserMapper.findByUserCode(user);
    }

    @Override
    public int codeUserSave(ItripUser user) {
        return itripUserMapper.insert(user);
    }

    @Override
    public int updateUserActivated(ItripUser user) {
        return itripUserMapper.updateActivated(user);
    }

    @Override
    public ItripUser dologin(ItripUser user) {
        return itripUserMapper.dologin(user);
    }
}
