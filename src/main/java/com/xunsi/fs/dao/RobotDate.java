package com.xunsi.fs.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

public class RobotDate
{
	protected static Logger log = Logger.getLogger(RobotDate.class);

	public static void insert(String json, Integer lotterId, Integer playType) { Integer success = null;
	Connection conn = null;
	CallableStatement cs = null;
	try {
		String[] jsonArry = json.split("\\|");
		conn = DatabaseConnection.getInstance().getConnection();
		cs = conn.prepareCall("{CALL LT_RACE.INSERT_MATCH_ODDS(?,?,?,?)}");
		long LONG = System.currentTimeMillis();
		String jobId = "123";
		for (int i = 0; i < jsonArry.length; i++) {
			//        System.out.println(jsonArry[i]);
			cs.setString(1, jobId);
			cs.setString(2, jobId + LONG);
			cs.setString(3, jsonArry[i]);
			cs.registerOutParameter(4, 12);
			if (!cs.execute()) {
				success = Integer.valueOf(Integer.parseInt(cs.getString(4)));
				//          System.out.println(success);
				if (success.intValue() != 0) {
					return;
				}
			}
			cs.clearParameters();
		}

	}catch (Exception e){
		e.printStackTrace();
		log.debug("error", e);

		if (cs != null) try { cs.close(); } catch (Exception localException3) {} 
		if (conn != null) try { conn.close();}catch (Exception localException4){}
	}finally{
		if (cs != null) try { cs.close(); } catch (Exception localException5) {} 
		if (conn != null) try { conn.close();} catch (Exception localException6){} 
	} 
	}

	public static void updateJackpotIssue(String json, int lotteryId) {
		Connection conn = null;

		PreparedStatement ps = null;
		try {
			String[] jsonArry = json.split("\\|");
			conn = DatabaseConnection.getInstance().getConnection();

			String sql = "UPDATE LT_ISSUE_INFO SET LUCKY_NO =? WHERE LOTTERY_ID = ? AND ISSUE_NO = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, jsonArry[0].split(";")[1]);
			ps.setInt(2, lotteryId);
			ps.setString(3, jsonArry[0].split(";")[0]);
			ps.executeUpdate();
			String sql1 = "UPDATE LT_ISSUE_INFO SET JACKPOT =? WHERE LOTTERY_ID = ? AND ISSUE_NO = ?";
			ps = conn.prepareStatement(sql1);
			ps.setString(1, jsonArry[1].split(";")[1]);
			ps.setInt(2, lotteryId);
			ps.setString(3, jsonArry[1].split(";")[0]);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("error", e);
			if (ps != null) try { ps.close(); } catch (Exception localException1) {} 
			if (conn != null) try { conn.close();}catch (Exception localException2){}
		}finally{
			if (ps != null) try { ps.close(); } catch (Exception localException3) {} 
			if (conn != null) try { conn.close();} catch (Exception localException4) {} 
		}
	}

	public static void updateIssue(String json, int lotteryId,int provinceId) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String[] str = json.split(";");
			String lucky_no = str[1];
			String issue_no = str[0];
			conn = DatabaseConnection.getInstance().getConnection();
			String sql = "UPDATE LT_ISSUE_INFO SET LUCKY_NO =? WHERE LOTTERY_ID = ? AND ISSUE_NO = ? AND PROVINCE_ID = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, lucky_no);
			ps.setInt(2, lotteryId);
			ps.setString(3, issue_no);
			ps.setInt(4, provinceId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("error", e);
			if (ps != null) try { ps.close(); } catch (Exception localException1) {} 
			if (conn != null) try { conn.close();}catch (Exception localException2){}
		}finally
		{
			if (ps != null) try { ps.close(); } catch (Exception localException3) {} 
			if (conn != null) try { conn.close();}catch (Exception localException4){}
		}
	}
}
