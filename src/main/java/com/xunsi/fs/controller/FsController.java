package com.xunsi.fs.controller;

import com.xunsi.fs.service.AllService;
import com.xunsi.fs.util.Constants;
import com.xunsi.fs.util.JsonUtils;
import com.xunsi.fs.util.MD5Tools;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by yanan xie on 2018/2/27.
 */
@RestController
public class FsController {

    @Autowired
    AllService allService;

    @RequestMapping(value = "/user/login")
    public String login(@RequestParam String sig,@RequestParam String json){
        boolean valid = MD5Tools.valid(json, sig);
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
            return "{\"msg\":"+Constants.RESPONSE_FAIL+"}";
        }
        return JsonUtils.AjaxJson(map);
    }
}
