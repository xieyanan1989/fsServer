package com.xunsi.fs.service;

import com.xunsi.fs.dao.AllDao;
import com.xunsi.fs.util.GeoHash;
import com.xunsi.fs.util.UTIL;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AllService {


	private static final org.slf4j.Logger log = LoggerFactory.getLogger(AllService.class);

	private AllDao allDao = new AllDao();
	/**
	 * 登录
	 * @param userName 用户名
	 * @param password 密码
	 * @return
	 */
	public Map loginService(String userName,String password){
		return allDao.login(userName, password);
	}
	/**
	 * 注册
	 * @param userName 用户名
	 * @param password	密码
	 * @param blat 纬度
	 * @param blog 经度
	 * @param userimg 头像地址
	 * @param address 用户地址
	 * @param countryId 村ID
	 * @param townId 	镇ID
	 * @param districtId 县ID
	 * @param cityId 	市ID
	 * @param provinceId 省ID
	 * @return
	 */
	public Map registService(String userName,String password, String provinceId, String cityId, String districtId, String townId, String countryId, String address, String userimg, Double blog, Double blat){
		Map map = new HashMap<String, String>();
		GeoHash g = new GeoHash(blat, blog);
		int result = allDao.regist(userName,password,provinceId,cityId,districtId,townId,countryId,address,UTIL.user_dir+userName+"/"+userimg,blog,blat,g.getGeoHashBase32());
		if(result == 0){
			//注册成功，用户图片写入用户图片目录
			UTIL.fileChannelCopy(userimg,userName);
		}else{
			log.debug("registService 注册异常，数据库返回码："+map.get("msg"));
		}
		map.put("msg", result);
		return map;
	}
	/**
	 * 密码重置
	 * @param passWord
	 * @return
	 */
	public Map resetPassService(String userName, String passWord) {
		// TODO Auto-generated method stub
		return allDao.resetPass(userName, passWord);
	}
	/**
	 * 实名认证
	 * @param username 用户ID
	 * @param nickname	昵称
	 * @param realname	真实姓名
	 * @param identity	身份证号
	 * @param qq		QQ号
	 * @param wechat	微信号
	 * @param paywd		支付密码
	 * @return
	 */
	public Map addUserInfo(String username, String nickname, String realname, String identity, String qq, String wechat, String paywd){
		// TODO Auto-generated method stub
		return allDao.addUserInfo(username,nickname,realname,identity,qq,wechat,paywd);
	}
	/**
	 * 更新密码
	 * @param uername	用户ID
	 * @param type		类型
	 * @param pwd		密码
	 * @param newpwd	新密码
	 * @return
	 */
	public Map updatePassword(String uername, String type, String pwd, String newpwd) {
		// TODO Auto-generated method stub
		return allDao.updatePassword(uername,type,pwd,newpwd);
	}
	/**
	 * 创建商品
	 * @param username		用户ID
	 * @param cateid		类别ID
	 * @param saletype		销售类型0-售罄,1-预售,2-销售中
	 * @param saledetail	销售详情
	 * @param saletitle		销售标题
	 * @param imgurl		产品图片地址
	 * @param salecount		销售数量
	 * @param salemea		计量单位
	 * @param bookTime 
	 * @param sendOrnot 
	 * @param proPrice 
	 * @param catetype
	 * @return
	 */
	public Map addProInfo(String username, String cateid,int saletype, String saledetail,
			String saletitle, String imgurl, int salecount, String salemea, int salesingle, int sendOrnot, String bookTime, String proPrice, int catetype) {
		//图片保存，从临时目录到商品副目录 imgurl
		
		// 数据写入
		Map map = allDao.addProInfo(username,cateid,saletype,saledetail,saletitle,username+"/"+imgurl,salecount,salemea,salesingle,sendOrnot,bookTime,proPrice,catetype);
		int result = (int) map.get("msg");
		if(result ==0){
			//注册成功，用户图片写入用户图片目录
			String[] urls = imgurl.split("\\,");
			for(String url : urls){
				UTIL.fileChannelCopy(url,username);
			}
		}else{
			log.debug("addProInfo 创建商品异常，数据库返回码："+map.get("msg"));
		}
		return map;
	}
	/**
	 * 客户用户达成契约
	 * @param username		用户ID
	 * @param proid			商品ID
	 * @param salecount		销售数量
	 * @param salemea		计量单位
	 * @return
	 */
	public Map proContract(String username, String proid, String salecount,String salemea) {
		// TODO Auto-generated method stub
		return allDao.proContract(username,proid,salecount,salemea);
	}
	/**
	 * 获取类别表
	 * @param parentid 父ID
	 * @return
	 */
	public Map getCates(String parentid) {
		// TODO Auto-generated method stub
		return allDao.getCates(parentid);
	}
	/**
	 * 获取城市表
	 * @param parentid 父ID
	 * @return
	 */
	public Map getCities(String parentid) {
		// TODO Auto-generated method stub
		return allDao.getCities(parentid);
	}
	/**
	 * 获取所有产品
	 * @param viewtype		显示ID
	 * @param cateid		类别ID
	 * @param provinceId	省ID
	 * @param cityId		市ID
	 * @param districtId	县ID
	 * @param townId		镇ID
	 * @param countryId		村ID
	 * @param saletype		销售类型	0-售罄,1-预售,2-销售中
	 * @param catetype		产品类型	1-人工,2-野生
	 * @return
	 */
	public Map getProducts(String viewtype,String provinceId,String cityId,String districtId,String cateid, String townId, String countryId,
			String saletype, String catetype) {
		Map map = null;
		if(viewtype == null || viewtype.equals(2)){
			map = allDao.getProducts(cateid,townId,countryId,saletype,catetype);
		}else{
			map = allDao.getProGroups(cateid,provinceId,cityId,districtId,saletype,catetype);
		}
		return map;
	}
	/**
	 * 获取用户所有的产品
	 * @param username	用户名
	 * @param saletype	0-售罄,1-预售,2-销售中
	 * @param pagenum 	页数
	 * @return
	 */
	public Map getUsersProduct(String username, String saletype, int pagenum, int pagesize) {
		Map map = null;
		int begin = pagenum*pagesize;
		int end = (pagenum+1)*pagesize;
		map = allDao.getUsersProduct(username,saletype,begin,end,pagesize);
		map.put("currpage", ""+((pagenum+1)));
		return map;
	}
	/**
	 * 获取用户所有的产品
	 * @param username	用户名
	 * @param saletype	0-成功,1-用户失败,2-店家失败,3-转让,4-订制中
	 * @param pagenum 	页数
	 * @return
	 */
	public Map getPersonProduct(String username, String saletype, int pagenum,int pagesize) {
		// TODO Auto-generated method stub
		Map map = null;
		int begin = pagenum*pagesize;
		int end = (pagenum+1)*pagesize;
		map = allDao.getPersonProduct(username,saletype,begin,end,pagesize);
		map.put("currpage", ""+((pagenum+1)));
		return map;
	}
	/**
	 * 查询附近产品
	 * @param userName
	 * @return
	 */
	public Map userExistService(String userName) {
		return allDao.exist(userName);
	}

    public List<Map> getProNearby(String geoHash) {
		return allDao.getProNearby(geoHash);
	}
}
