package com.xunsi.fs.dao;

import com.xunsi.fs.util.Constants;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AllDao {

	private Logger log=Logger.getLogger(getClass());
	/**
	 * 登陆
	 * @param userName 用户名
	 * @param password 密码
	 * @return
	 */
	public Map login(String userName,String password){
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = null;
		try {
			conn = DatabaseConnection.getInstance().getConnection();;
			String sql = "SELECT USER_NAME,REAL_NAME,CREATE_TIME,FREE_BALANCE  FROM BA_USER_INFO WHERE USER_NAME = ? AND LOGIN_PWD = CK_UTL.MD5(?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1,userName);
			ps.setString(2,password);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap();
				map.put("userName", rs.getString(1));
				map.put("realName", rs.getString(2));
				map.put("createTime", rs.getString(3));
				map.put("bonus", rs.getInt(4));
			}
		} catch (Exception e) {
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
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
	public int regist(String userName, String password, String provinceId, String cityId, String districtId, String townId, String countryId, String address, String userimg, Double blog, Double blat) {
		Connection conn = null;
		CallableStatement cs = null;
		int result = 0;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			cs = conn.prepareCall("{CALL BA_USER.REGIST(?,?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, userName);
			cs.setString(2, password);
			cs.setString(3, provinceId);
			cs.setString(4, cityId);
			cs.setString(5, districtId);
			cs.setString(6, townId);
			cs.setString(7, countryId);
			cs.setString(8, address);
			cs.setString(9, userimg);
			cs.setDouble(10, blog);
			cs.setDouble(11, blat);
			cs.registerOutParameter(12, Types.VARCHAR);
			if( !cs.execute() ) {
				result = cs.getInt(12);
			}
		} catch (Exception e) {
			log.debug("error", e);
			e.printStackTrace();
			return 1;
		}finally{
			if (cs != null) try { cs.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return result;
	}
	/**
	 * 重置密码
	 * @param userName 用户名
	 * @param passWord 密码
	 * @return
	 */
	public Map resetPass(String userName, String passWord) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = new HashMap();
		try {
			conn = DatabaseConnection.getInstance().getConnection();;
			String sql = "UPDATE  BA_USER_INFO SET LOGIN_PWD = CK_UTL.MD5(?) WHERE USER_NAME = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1,passWord);
			ps.setString(2,userName);
			ps.executeUpdate();
			map.put("msg", 0);
		} catch (Exception e) {
			map.put("msg", Constants.RESPONSE_FAIL);
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
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
		Connection conn = null;
		CallableStatement cs = null;
		Map map = new HashMap();
		int result = 0;
		try {
			conn = DatabaseConnection.getInstance().getConnection();;
			cs = conn.prepareCall("{CALL BA_USER.CERTIFICATION(?,?,?,?,?,?,?,?)}");
			cs.setString(1, username);
			cs.setString(2, nickname);
			cs.setString(3, realname);
			cs.setString(4, identity);
			cs.setString(5, qq);
			cs.setString(6, wechat);
			cs.setString(7, paywd);
			cs.registerOutParameter(8, Types.INTEGER);
			if( !cs.execute() ) {
				result = cs.getInt(8);
				map.put("msg", result);
			}
		} catch (Exception e) {
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (cs != null) try { cs.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
	/**
	 * 更新密码
	 * @param uername 用户ID
	 * @param pwd 旧密码
	 * @param newpwd 新密码
	 * @return
	 */
	public Map updatePassword(String uername, String type, String pwd, String newpwd) {
		// TODO Auto-generated method stub
		Connection conn = null;
		CallableStatement cs = null;
		Map map = new HashMap();
		int result = 0;
		try {
			conn = DatabaseConnection.getInstance().getConnection();;
			cs = conn.prepareCall("{CALL BA_USER.MODIFY_PASS(?,?,?,?,?)}");
			cs.setString(1, uername);
			cs.setString(2, type);
			cs.setString(3, pwd);
			cs.setString(4, newpwd);
			cs.registerOutParameter(5, Types.VARCHAR);
			if( !cs.execute() ) {
				result = cs.getInt(5);
				map.put("msg", result);
			}
		} catch (Exception e) {
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (cs != null) try { cs.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
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
	 * @param salesingle 	产品状态1-单卖，2-非单卖
	 * @param bookTime 
	 * @param sendOrnot 
	 * @param proPrice 
	 * @param catetype 
	 * @return
	 */
	public Map addProInfo(String username, String cateid, int saletype, String saledetail,
			String saletitle, String imgurl, int salecount, String salemea, int salesingle, int sendOrnot, String bookTime, String proPrice, int catetype) {
		Connection conn = null;
		CallableStatement cs = null;
		Map map = new HashMap();
		int result = 0;
		try {
			conn = DatabaseConnection.getInstance().getConnection();;
			cs = conn.prepareCall("{CALL BA_PRO.PRO_INSERT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, username);
			cs.setString(2, cateid);
			cs.setInt(3, saletype);
			cs.setString(4, saledetail);
			cs.setString(5, saletitle);
			cs.setString(6, imgurl);
			cs.setInt(7, salecount);
			cs.setString(8, salemea);
			cs.setInt(9, salesingle);
			cs.setInt(10, sendOrnot);
			cs.setString(11, bookTime);
			cs.setString(12, proPrice);
			cs.setInt(13, catetype);
			cs.registerOutParameter(14, Types.VARCHAR);
			cs.registerOutParameter(15, Types.VARCHAR);
			if( !cs.execute() ) {
				result = cs.getInt(14);
				map.put("msg", result);
				map.put("orderId", cs.getString(15));
			}
		} catch (Exception e) {
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (cs != null) try { cs.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
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
		Connection conn = null;
		CallableStatement cs = null;
		Map map = new HashMap();
		int result = 0;
		try {
			conn = DatabaseConnection.getInstance().getConnection();;
			cs = conn.prepareCall("{CALL BA_USER.PRO_CONTRACT(?,?,?,?,?)}");
			cs.setString(1, username);
			cs.setString(2, proid);
			cs.setString(3, salecount);
			cs.setString(4, salemea);
			cs.registerOutParameter(5, Types.VARCHAR);
			if( !cs.execute() ) {
				result = cs.getInt(5);
				map.put("msg", result);
			}
		} catch (Exception e) {
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (cs != null) try { cs.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
	
	/**
	 * 获取类别
	 * @param parentid
	 * @return
	 */
	public Map getCates(String parentid) {
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = new HashMap();
		List list = new ArrayList();
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			String sql = "SELECT CATE_ID,CATE_NAME,CATE_PARENT_ID,COMM,CATE_TYPE FROM BA_CATE_TYPE_SIMP WHERE CATE_PARENT_ID = ? ";
			ps = conn.prepareCall(sql);
			ps.setString(1, parentid);
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,String> maps = new HashMap<String, String>();
				maps.put("cateid", rs.getString(1));
				maps.put("catename", rs.getString(2));
				maps.put("parentid", rs.getString(3));
				maps.put("comm", rs.getString(4));
				maps.put("catetype", rs.getString(5));
				list.add(maps);
			}
			map.put("msg",0);
			map.put("list", list);
		} catch (Exception e) {
			map.put("msg", Constants.RESPONSE_FAIL);
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
	
	/**
	 * 获取城市表
	 * @param parentid 父ID
	 * @return
	 */
	public Map getCities(String parentid) {
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = new HashMap();
		List list = new ArrayList();
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			String sql = "SELECT CITY_ID,CITY_NAME,CITY_PARENT_ID,B_LONGITUDE,B_LATITUDE FROM BA_CITY_COOR WHERE CITY_PARENT_ID = ? ";
			ps = conn.prepareCall(sql);
			ps.setString(1, parentid);
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,String> maps = new HashMap<String, String>();
				maps.put("cityid", rs.getString(1));
				maps.put("cityname", rs.getString(2));
				maps.put("parentid", rs.getString(3));
				maps.put("citylog", rs.getString(4));
				maps.put("citylat", rs.getString(5));
				list.add(maps);
			}
			map.put("msg",0);
			map.put("list", list);
		} catch (Exception e) {
			map.put("msg", Constants.RESPONSE_FAIL);
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
	/**
	 * 获取所有产品
	 * @param cateid		类别ID
	 * @param townId		镇ID
	 * @param countryId		村ID
	 * @param saletype		销售类型	0-售罄,1-预售,2-销售中
	 * @param catetype		产品类型	1-人工,2-野生
	 * @return
	 */
	public Map getProducts(String cateid,String townId, String countryId,String saletype, String catetype) {
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = new HashMap();
		List list = new ArrayList();
		ResultSet rs = null;
		String sql = "";
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			sql = "SELECT P.USER_NAME,P.PRO_ID,P.CATE_ID,P.CATE_NAME,P.PROVINCE_ID,P.CITY_ID,P.DISTRICT_ID,P.TOWN_ID,P.COUNTRY_ID "
				+" P.SALE_TYPE,P.SALE_DETAIL,P.SALE_TITLE,P.IMG_URL,P.SALE_COUNT,P.SALE_MEA,U.B_LONGITUDE,U.B_LATITUDE,U.SALE_SINGLE "
				+" FROM BA_PRO_INFO P,BA_USER_INFO U WHERE P.USER_NAME = U.USER_NAME AND P.SALE_TYPE = ? AND " 
				+" P.CATE_ID = ? AND P.CATE_TYPE = ? ";
			if(townId != "" || !townId.equals("")){
				sql += " AND P.TOWN_ID = ? ";
			}
			if(countryId != "" || !countryId.equals("")){
				sql += " AND P.COUNTRY_ID = ? ";
			}
			ps = conn.prepareCall(sql);
			ps.setString(1, saletype);
			ps.setString(2, cateid);
			ps.setString(3, catetype);
			if(townId != "" || !townId.equals("")){
				ps.setString(4, townId);
			}
			if(countryId != "" || !countryId.equals("")){
				ps.setString(4, countryId);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,String> maps = new HashMap<String, String>();
				maps.put("username", rs.getString(1));
				maps.put("proid", rs.getString(2));
				maps.put("cateid", rs.getString(3));
				maps.put("catename", rs.getString(4));
				maps.put("provinceId", rs.getString(5));
				maps.put("cityId", rs.getString(6));
				maps.put("districtId", rs.getString(7));
				maps.put("townId", rs.getString(8));
				maps.put("countryId", rs.getString(9));
				maps.put("saletype", rs.getString(10));
				maps.put("saledetail", rs.getString(11));
				maps.put("saletitle", rs.getString(12));
				maps.put("imgurl", rs.getString(13));
				maps.put("salecount", rs.getString(14));
				maps.put("salemea", rs.getString(15));
				maps.put("blog", rs.getString(16));
				maps.put("blat", rs.getString(17));
				maps.put("salesingle", rs.getString(18));
				list.add(maps);
			}
			map.put("msg",0);
			map.put("list", list);
		} catch (Exception e) {
			map.put("msg", Constants.RESPONSE_FAIL);
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
	/**
	 * 获取产品集合
	 * @param cateid		类别ID
	 * @param provinceId	省ID
	 * @param cityId		市ID
	 * @param districtId	县ID
	 * @param saletype		销售类型	0-售罄,1-预售,2-销售中
	 * @param catetype		产品类型	1-人工,2-野生
	 * @return
	 */
	public Map getProGroups(String cateid, String provinceId, String cityId,
			String districtId, String saletype, String catetype) {
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = new HashMap();
		List list = new ArrayList();
		ResultSet rs = null;
		String sql = "";
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			sql = "SELECT P.SALE_COUNT,P.SALE_MEA,B.B_LONGITUDE,B.D_LATITUDE FROM " 
				+"(SELECT SALE_COUNT,SALE_MEA FROM BA_PRO_INFO WHERE SALE_TYPE = ? AND CATE_ID = ? AND CATE_TYPE = ? " 
				+"GROUP BY ";
			if(provinceId != "" || !provinceId.equals("")){
				sql += provinceId+" ) P, BA_CITY_COOR B WHERE B.CITY_PARENT_ID = P.PROVINCE_ID ";
			}
			if(cityId != "" || !cityId.equals("")){
				sql += cityId+" ) P, BA_CITY_COOR B WHERE B.CITY_PARENT_ID = P.CITY_ID ";
			}
			if(districtId != "" || !districtId.equals("")){
				sql += districtId+" ) P, BA_CITY_COOR B WHERE B.CITY_PARENT_ID = P.DISTRICT_ID ";
			}
			ps = conn.prepareCall(sql);
			ps.setString(1, saletype);
			ps.setString(2, cateid);
			ps.setString(3, catetype);
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String,String> maps = new HashMap<String, String>();
				maps.put("salecount", rs.getString(1));
				maps.put("salemea", rs.getString(2));
				maps.put("blog", rs.getString(3));
				maps.put("blat", rs.getString(4));
				list.add(maps);
			}
			map.put("msg",0);
			map.put("list", list);
		} catch (Exception e) {
			map.put("msg", Constants.RESPONSE_FAIL);
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
	/**
	 * 获取用户下所有的产品
	 * @param username 用户名
	 * @param saletype	产品类型0-售罄,1-预售,2-销售中
	 * @param end 开始伪类
	 * @param begin 结束伪类
	 * @param pagesize 
	 * @return
	 */
	public Map getUsersProduct(String username, String saletype, int begin, int end, int pagesize) {
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = new HashMap();
		List list = new ArrayList();
		ResultSet rs = null;
		CallableStatement cs = null;
		String condition =  " BA_PRO_INFO WHERE USER_NAME = '"+username+"' AND SALE_TYPE = "+saletype+" ORDER BY CREATE_TIME DESC";
		String sql = "SELECT PRO_ID,CATE_ID,CATE_NAME,SALE_TITLE,IMG_URL,SALE_COUNT,SALE_MEA,CATE_TYPE,PRO_CREDIT,PRO_PRICE FROM"+condition;
		try {
			cs = conn.prepareCall("{CALL BA_PRO.PAGE_CSR(?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, sql);
			cs.setString(2, condition);
			cs.setInt(3, begin);
			cs.setInt(4, end);
			cs.setInt(5, pagesize);
			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, OracleTypes.CURSOR);
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.execute();
            rs = (ResultSet)cs.getObject(8);
            while(rs!=null&& rs.next()){
            	Map<String,String> maps = new HashMap<String, String>();
				maps.put("proid", rs.getString(1));
				maps.put("cateid", rs.getString(2));
				maps.put("catename", rs.getString(3));
				maps.put("saletitle", rs.getString(4));
				maps.put("imgurl", rs.getString(5));
				maps.put("salecount", rs.getString(6));
				maps.put("salemea", rs.getString(7));
				maps.put("catetype", rs.getString(8));
				maps.put("procredit", rs.getString(9));
				maps.put("amount", rs.getString(10));
				list.add(maps);
            }
            map.put("totalpage", cs.getString(6));
            map.put("totalitem", cs.getString(7));
			map.put("msg",0);
			map.put("list", list);
		} catch (Exception e) {
			map.put("msg", Constants.RESPONSE_FAIL);
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
	/**
	 * 获取用户所有的产品
	 * @param username	用户名
	 * @param saletype	0-成功,1-用户失败,2-店家失败,3-转让,4-订制中
	 * @param begin 	开始
	 * @param end 	结束
	 * @param pagesize 每页条数
	 * @return
	 */
	public Map getPersonProduct(String username, String saletype, int begin,int end, int pagesize) {
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = new HashMap();
		List list = new ArrayList();
		ResultSet rs = null;
		CallableStatement cs = null;
		String condition =  " SELECT PRO_ID FROM BA_PRO_USER WHERE USER_NAME = '"+username+"' GROUP BY PRO_ID ";
		String sql = "SELECT PRO_ID,CATE_ID,CATE_NAME,SALE_TITLE,IMG_URL,SALE_COUNT,SALE_MEA,CATE_TYPE,PRO_CREDIT,PRO_PRICE FROM BA_PRO_INFO WHERE PRO_ID IN ("+condition+") ORDER BY CREATE_TIME DESC";
		try {
			cs = conn.prepareCall("{CALL BA_PRO.PAGE_CSR(?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, sql);
			cs.setString(2, condition);
			cs.setInt(3, begin);
			cs.setInt(4, end);
			cs.setInt(5, pagesize);
			cs.registerOutParameter(6, Types.INTEGER);
			cs.registerOutParameter(7, Types.INTEGER);
            cs.registerOutParameter(8, OracleTypes.CURSOR);
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.execute();
            rs = (ResultSet)cs.getObject(8);
            while(rs!=null&& rs.next()){
            	Map<String,String> maps = new HashMap<String, String>();
				maps.put("proid", rs.getString(1));
				maps.put("cateid", rs.getString(2));
				maps.put("catename", rs.getString(3));
				maps.put("saletitle", rs.getString(4));
				maps.put("imgurl", rs.getString(5));
				maps.put("salecount", rs.getString(6));
				maps.put("salemea", rs.getString(7));
				maps.put("catetype", rs.getString(8));
				maps.put("procredit", rs.getString(9));
				maps.put("amount", rs.getString(10));
				list.add(maps);
            }
            map.put("totalpage", cs.getString(6));
            map.put("totalitem", cs.getString(7));
			map.put("msg",0);
			map.put("list", list);
		} catch (Exception e) {
			map.put("msg", Constants.RESPONSE_FAIL);
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
	/**
	 * 查询用户是否存在
	 * @param userName
	 * @return
	 */
	public Map exist(String userName) {
		Connection conn = null;
		PreparedStatement ps = null;
		Map map = null;
		try {
			conn = DatabaseConnection.getInstance().getConnection();;
			String sql = "SELECT USER_NAME,REAL_NAME,CREATE_TIME,FREE_BALANCE  FROM BA_USER_INFO WHERE USER_NAME = ? ";
			ps = conn.prepareStatement(sql);
			ps.setString(1,userName);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				map = new HashMap();
				map.put("userName", rs.getString(1));
				map.put("realName", rs.getString(2));
				map.put("createTime", rs.getString(3));
				map.put("bonus", rs.getInt(4));
			}
		} catch (Exception e) {
			log.debug("error", e);
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception e) { } 
			if (conn != null) try { conn.close(); } catch (Exception e) { } 
		}
		return map;
	}
}
