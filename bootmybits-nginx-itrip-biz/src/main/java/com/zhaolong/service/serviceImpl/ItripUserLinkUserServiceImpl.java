package com.zhaolong.service.serviceImpl;

import com.zhaolong.mapper.ItripUserLinkUserMapper;
import com.zhaolong.po.ItripUserLinkUser;
import com.zhaolong.service.ItripUserLinkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
@Transactional
public class ItripUserLinkUserServiceImpl implements ItripUserLinkUserService {
    @Autowired
    private ItripUserLinkUserMapper itripUserLinkUserMapper;

    public ItripUserLinkUserMapper getItripUserLinkUserMapper() {
        return itripUserLinkUserMapper;
    }

    public void setItripUserLinkUserMapper(ItripUserLinkUserMapper itripUserLinkUserMapper) {
        this.itripUserLinkUserMapper = itripUserLinkUserMapper;
    }

    @Override
    public int addUsersLinkUser(ItripUserLinkUser linkUser) throws Exception {
        return itripUserLinkUserMapper.insertItripUserLinkUser(linkUser);
    }

    @Override
    public List<ItripUserLinkUser> findAllUserLinkUserByMap(Map<String, Object> param) throws Exception {
        return itripUserLinkUserMapper.getItripUserLinkUserListByMap(param);
    }

    @Override
    public int deleteUserLinkUserByIds(Long[] ids) throws Exception {
        return itripUserLinkUserMapper.deleteItripUserLinkUserByIds(ids);
    }

    @Override
    public int updateUsersLinkUser(ItripUserLinkUser linkUser) throws Exception {
        return itripUserLinkUserMapper.updateItripUserLinkUser(linkUser);
    }
}
