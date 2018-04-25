package com.xunsi.fs.controller;

import com.xunsi.fs.service.AllService;
import com.xunsi.fs.util.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanan xie on 2018/2/27.
 */
@RestController
public class FsController {

//    private Logger log=Logger.getLogger(getClass());
    private static final Logger log = LoggerFactory.getLogger(FsController.class);
    @Autowired
    AllService allService;

    /**
     * 用户登录
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/user/login")
    public String login(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        log.info("sig:"+sig);
        log.info("json:"+json);
        log.info("Hello World");
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            String userName=jsonObject.getString("username");
            String passWord= jsonObject.getString("password");
            AllService service = new AllService();
            map = service.loginService(userName, passWord);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
//				HttpSession session = request.getSession();
//				String sessionId = session.getId();
//				Map mapv = (Map) session.getAttribute("userId");
//				if( (mapv == null) || (mapv.get("userName") == null)){
//					log.debug("bind session:"+(String) map.get("userId"));
//					session.setAttribute((String) map.get("userId"), sessionId);
//				}
//				if(sessionId == null || sessionId == ""){
//					log.debug("sessionId:"+sessionId);
//					System.out.println("login sessionId:"+sessionId);
//					log.debug("login sessionId:"+sessionId+",userId:"+(String) map.get("userId"));
//					session.setAttribute((String) map.get("userId"), sessionId);
//					Constants.judjeSession().put((String) map.get("userId"), sessionId);
//				}
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 忘记密码,重新设置密码
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/user/reset")
    public String forgetPassword(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            String username=jsonObject.getString("username");
            String passWord= jsonObject.getString("pwd");
            AllService service = new AllService();
            map = service.resetPassService(username, passWord);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 获取商品类别
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/pro/cates")
    public String getCates(@RequestParam String sig,@RequestParam String json){
        log.debug("sig="+sig+",json="+json);
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            //类别父ID
            String parentid=jsonObject.getString("parentid");
            AllService service = new AllService();
            map = service.getCates(parentid);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 获取城市类别
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/pro/cities")
    public String getCities(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            //城市父ID
            String parentid=jsonObject.getString("parentid");
            AllService service = new AllService();
            map = service.getCities(parentid);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 客户用户达成契约接口
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/pro/contract")
    public String madeContract(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            //用户ID
            String username=jsonObject.getString("username");
            //产品ID
            String proid= jsonObject.getString("proid");
            //销售数量
            String salecount= jsonObject.getString("salecount");
            //计量单位
            String salemea= jsonObject.getString("salemea");
            AllService service = new AllService();
            map = service.proContract(username,proid,salecount,salemea);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 产品创建接口
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/pro/create")
    public String madePro(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            String username=jsonObject.getString("username");
            String cateid= jsonObject.getString("cateid");
            int saletype= jsonObject.getInt("saletype");
            String saledetail= jsonObject.getString("saledetail");
            String saletitle= jsonObject.getString("saletitle");
            String imgurl= jsonObject.getString("imgurl");
            int salecount= jsonObject.getInt("salecount");
            String salemea= jsonObject.getString("salemea");
            int salesingle= jsonObject.getInt("salesingle");
            int send_ornot= jsonObject.getInt("send_ornot");
            int catetype= jsonObject.getInt("catetype");
            String book_time= jsonObject.getString("book_time");
            String pro_price= jsonObject.getString("pro_price");
            AllService service = new AllService();
            map = service.addProInfo(username,cateid,saletype,saledetail
                    ,saletitle,imgurl,salecount,salemea,salesingle,send_ornot,book_time,pro_price,catetype);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 产品展示接口
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/pro/info")
    public String proShow(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            String viewtype= jsonObject.getString("viewtype");
            String cateid= jsonObject.getString("cateid");
            String provinceId= jsonObject.getString("provinceId");
            String cityId= jsonObject.getString("cityId");
            String districtId= jsonObject.getString("districtId");
            String townId= jsonObject.getString("townId");
            String countryId= jsonObject.getString("countryId");
            String saletype= jsonObject.getString("saletype");
            String catetype= jsonObject.getString("catetype");
            AllService service = new AllService();
            map = service.getProducts(viewtype,cateid,provinceId,cityId,districtId,townId,countryId,saletype,catetype);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 附近的产品
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/pro/nearby")
    public String proNearby(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map = new HashMap() ;
        try {
            jsonObject = new JSONObject(json);
            String geoHash= jsonObject.getString("geoHash");
            AllService service = new AllService();
            List<Map> list = service.getProNearby(geoHash);
            map.put("list",list);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 实名认证
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/user/realname")
    public String realName(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            String username=jsonObject.getString("username");
            String nickname= jsonObject.getString("nickname");
            String realname= jsonObject.getString("realname");
            String identity= jsonObject.getString("identity");
            String qq= jsonObject.getString("qq");
            String wechat= jsonObject.getString("wechat");
            String paywd= jsonObject.getString("paywd");
            AllService service = new AllService();
            map = service.addUserInfo(username,nickname,realname,identity,qq,wechat,paywd);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 注册
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/user/regist")
    public String register(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            String userName=jsonObject.getString("username");
            String passWord= jsonObject.getString("pwd");
            String provinceId= jsonObject.getString("provinceId");
            String cityId= jsonObject.getString("cityId");
            String districtId= jsonObject.getString("districtId");
            String townId= jsonObject.getString("townId");
            String countryId= jsonObject.getString("countryId");
            String address= jsonObject.getString("address");
            String userimg= jsonObject.getString("userimg");
            Double blog= jsonObject.getDouble("blog");
            Double blat= jsonObject.getDouble("blat");
            AllService service = new AllService();
            map = service.registService(userName,passWord,provinceId,cityId,districtId,townId,countryId,address,userimg,blog,blat);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 修改秘密
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/user/chpwd")
    public String changePwd(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            String uername=jsonObject.getString("uername");
            String pwd= jsonObject.getString("pwd");
            String newpwd= jsonObject.getString("newpwd");
            String type= jsonObject.getString("type");//0-登录，1-支付
            AllService service = new AllService();
            map = service.updatePassword(uername,type,pwd,newpwd);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 图片上传
     * @return
     */
    @RequestMapping(value = "/pro/upload")
    public String upload(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = null;
        Map map ;
        try {
//            map = UploadUtil.upload(request, response, UTIL.temporary);
            map = UploadSpring.uploadImg(request, response, UTIL.temporary);
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 我的购买
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/user/buy")
    public String userBuyed(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            //用户名
            String username=jsonObject.getString("username");
            String saletype=jsonObject.getString("saletype");
            int pagenum=jsonObject.getInt("pagenum");
            int pagesize=jsonObject.getInt("pagesize");
            AllService service = new AllService();
            map = service.getPersonProduct(username,saletype,pagenum,pagesize);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 用户是否存在
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "/user/exist")
    public String userExist(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            String userName=jsonObject.getString("username");
            AllService service = new AllService();
            map = service.userExistService(userName);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }

    /**
     * 我的供应
     * @param sig
     * @param json
     * @return
     */
    @RequestMapping(value = "user/supply")
    public String userSupply(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
        if(valid == false){
            return "{\"msg\":"+ Constants.RESPONSE_SIGNERROR+"}";
        }
        JSONObject jsonObject = null;
        Map map ;
        try {
            jsonObject = new JSONObject(json);
            //用户名
            String username=jsonObject.getString("username");
            String saletype=jsonObject.getString("saletype");
            int pagenum=jsonObject.getInt("pagenum");
            int pagesize=jsonObject.getInt("pagesize");
            AllService service = new AllService();
            map = service.getUsersProduct(username,saletype,pagenum,pagesize);
            JsonUtils.AjaxJson(map);
            if(map == null){
                return "{\"msg\":"+Constants.RESPONSE_USERERROR+"}";
            }else{
                map.put("msg", 0);
            }
        }catch (Exception e) {
            e.printStackTrace();
            log.info("e:",e);
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }
}
