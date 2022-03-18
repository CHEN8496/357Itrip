package com.bdqn.controller;

import cn.itrip.dao.itripHotel.ItripHotelMapper;
import cn.itrip.dao.itripUser.ItripUserMapper;
import cn.itrip.pojo.ItripUser;
import cn.itrip.pojo.ItripUserVO;
import com.alibaba.fastjson.JSONArray;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import itrip.common.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class itripController {

    @Resource
    ItripHotelMapper dao;

    @Resource
    TokenBiz biz;

    @Resource
    RedisUtil RedisUtil;

    @RequestMapping(value="api/ckusr",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    public Dto yxyz() throws Exception {
        return DtoUtil.returnSuccess();
    }

    @RequestMapping(value="api/doregister",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto Registermail(@RequestBody ItripUserVO vo) throws Exception {
        //把前台的信息插入到数据库中
        ItripUser user=new ItripUser();
        user.setUserCode(vo.getUserCode());
        user.setUserPassword(vo.getUserPassword());
        user.setUserName(vo.getUserName());
        dao4.insertItripUser(user);
        //发送短信验证码
        Random random=new Random(4);
        int sj = random.nextInt(9999);
        //App.createMimeMessage(user.getUserCode(),""+sj);
        App.SentSmail(user.getUserCode(),""+sj);
        //存入到redis中，一会验证去这里匹配
        RedisUtil.setRedis(vo.getUserCode(),""+sj);

        return DtoUtil.returnSuccess();
    }

    @Resource
    ItripUserMapper dao4;

    @RequestMapping(value="api/activate",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto rupdatee(String user, String code) throws Exception {
        //需要去redis中判断，验证码是否正确
        String coder=RedisUtil.getstr(user);
        if (coder!=null&coder.equals(code)){
            //如果正确,我们可以去数据库激活账号
            dao4.updateItripUser(user);
            return DtoUtil.returnSuccess("激活成功，请登录");
        }else{
            return DtoUtil.returnFail("验证码错误","10000");
        }
    }
    @RequestMapping(value="api/registerbyphone",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto Register(@RequestBody ItripUserVO vo) throws Exception {
        //把前台的信息插入到数据库中
        ItripUser user=new ItripUser();
        user.setUserCode(vo.getUserCode());
        user.setUserPassword(vo.getUserPassword());
        user.setUserName(vo.getUserName());
        dao4.insertItripUser(user);
        //发送短信验证码
        Random random=new Random(4);
        int sj = random.nextInt(9999);
        sentSms(vo.getUserCode(),""+sj);
        //存入到redis中，一会验证去这里匹配
        RedisUtil.setRedis(vo.getUserCode(),""+sj);

        return DtoUtil.returnSuccess();
    }




    @RequestMapping(value="/api/validatephone",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto rupdate(String user, String code) throws Exception {
        //需要去redis中判断，验证码是否正确
        String coder=RedisUtil.getstr(user);
        if (coder!=null&coder.equals(code)){
            //如果正确,我们可以去数据库激活账号
            dao4.updateItripUser(user);
            return DtoUtil.returnSuccess("激活成功，请登录");
        }else{
            return DtoUtil.returnFail("验证码错误","10000");
        }
    }


    @Resource
    ItripUserMapper dao1;

    @RequestMapping(value="api/dologin",produces="application/json;charset=utf-8")
    //ResponseBody 在当前控制器直接返回对象或者字符串
    //不再走逻辑视图
    @ResponseBody
    //Sting 不是逻辑视图名，也不是重定向，是直接返回数据
    public Dto login(String name, String password, HttpServletRequest request) throws Exception {
        Map b =new HashMap<>();
        b.put("a",name);
        b.put("b",password);
        ItripUser user=dao1.getItripUserListByMap(b);

        if(user!=null){
            //模拟session的票据-----------
            //把user存入redis中生成一个token
            String token=biz.generateToken(request.getHeader("User-Agent"),user);
            //把这个token存储到redis中
            //fastjson 把当前用户转成字符串。
            RedisUtil.setRedis(token,JSONArray.toJSONString(user));

            ItripTokenVO obj=new ItripTokenVO(token, Calendar.getInstance().getTimeInMillis()*3600*2,
                    Calendar.getInstance().getTimeInMillis());

            return DtoUtil.returnDataSuccess(obj);
            //return DtoUtil.returnSuccess("用户登录失败","1000");
        }
        return DtoUtil.returnFail("登录失败","1000");

        //通过fastjson把当前对象转换成字符串
        //return JSONArray.toJSONString(user);
    }

    public static void sentSms(String Phone, String message){
        //生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8aaf07087f639e2b017f6c33958601e8";
        String accountToken = "34a1a44fb6a24c5ba0d8255e6f18f262";
        //请使用管理控制台中已创建应用的APPID
        String appId = "8aaf07087f639e2b017f6c33969501ee";
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_XML);
        String to = "15028065770";
        String templateId= "1";
        String[] datas = {message};
        //  String subAppend="1234";  //可选	扩展码，四位数字 0~9999
        //  String reqId="***";  //可选 第三方自定义消息id，最大支持32位英文数字，同账号下同一自然天内不允许重复
        HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas);
        //  HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId,datas,subAppend,reqId);
        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else {
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }
    }

    @RequestMapping("/clist")
    public String clist(){

        return "clist";
    }

}
