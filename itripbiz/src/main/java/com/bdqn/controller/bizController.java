package com.bdqn.controller;

import cn.itrip.dao.itripAreaDic.ItripAreaDicMapper;
import cn.itrip.dao.itripLabelDic.ItripLabelDicMapper;
import cn.itrip.dao.itripUserLinkUser.ItripUserLinkUserMapper;
import cn.itrip.pojo.*;
import itrip.common.Dto;
import itrip.common.DtoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class bizController {
    @Resource
    ItripAreaDicMapper dao;

    @RequestMapping("/api/hotel/queryhotcity/{type}")
    @ResponseBody
    public Dto getCitty(@PathVariable("type")int t) throws Exception {

        //去后台Mybatis中读取数据
        System.out.println(t);
        Map map=new HashMap();
        map.put("aa",t);
        List <ItripAreaDic> list=dao.getItripAreaDicListByMap(map);

        return DtoUtil.returnDataSuccess(list);
    }

    @Resource
    ItripLabelDicMapper dao1;

    @RequestMapping("/api/hotel/queryhotelfeature")
    @ResponseBody
    public Dto getCittytese() throws Exception {
        //去后台Mybatis中读取数据
        List<ItripLabelDic> list=dao1.fisttop();

        return DtoUtil.returnDataSuccess(list);
    }

    @Resource
    ItripUserLinkUserMapper user;
    @RequestMapping("/api/userinfo/queryuserlinkuser")
    @ResponseBody
    public Dto User() throws Exception {
        //去后台Mybatis中读取数据
        List<ItripUserLinkUser> list=user.selectuser();

        return DtoUtil.returnDataSuccess(list);
    }

    @Resource
    ItripUserLinkUserMapper user1;
    @RequestMapping("/api/userinfo/adduserlinkuser")
    @ResponseBody
    public Dto addUser(@RequestBody ItripUserLinkUserVO vo) throws Exception {
        ItripUserLinkUser user =new ItripUserLinkUser();
        user.setLinkUserName(vo.getLinkUserName());
        user.setLinkPhone(vo.getLinkPhone());
        user.setLinkIdCard(vo.getLinkIdCard());
        user1.insertuser(user);
        return DtoUtil.returnDataSuccess(user);
    }

    @Resource
    ItripUserLinkUserMapper user2;
    @RequestMapping("/api/userinfo/deluserlinkuser")
    @ResponseBody
    public Dto delUser(Long ids) throws Exception {
        user2.deluser(ids);
        return DtoUtil.returnDataSuccess("删除成功");
    }

    @Resource
    ItripUserLinkUserMapper user3;
    @RequestMapping("/api/userinfo/modifyuserlinkuser")
    @ResponseBody
    public Dto upduser(@RequestBody ItripUserLinkUserVO vo) throws Exception {
        ItripUserLinkUser user= new ItripUserLinkUser();
        user.setLinkUserName(vo.getLinkUserName());
        user.setLinkPhone(vo.getLinkPhone());
        user.setLinkIdCard(vo.getLinkIdCard());
        user3.upduser(user);
        return DtoUtil.returnDataSuccess(user);
    }
}
