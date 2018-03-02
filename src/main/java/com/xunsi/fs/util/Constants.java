package com.xunsi.fs.util;

public class Constants {

	/**
	 * 响应代码表0-成功，1001-失败，1002-用户名或密码不正确,1003-用户已存在
	 * 1004-签名验证失败，1005-用户未登陆,1006-未实名认证
	 * 1007-，1008-已实名认证,1009-余额不足
	 * 1010-允许上传的文件类型（"jpg", "jpeg", "gif", "png"），
	 * 1011-请选择上传文件,1012-文件尺寸超过规定大小
	 */
	public final static int RESPONSE_SUCCESS = 0;
	public final static int RESPONSE_FAIL = 1001;
	public final static int RESPONSE_USERERROR = 1002;
	public final static int RESPONSE_EXITERROR = 1003;
	public final static int RESPONSE_SIGNERROR = 1004;
	public final static int RESPONSE_OZZFIVE = 1005;
	public final static int RESPONSE_OZZSIX = 1006;
	public final static int RESPONSE_OZZSEVEN = 1007;
	public final static int RESPONSE_OZZEIGHT = 1008;
	public final static int RESPONSE_OZZNINE = 1009;
	public final static int RESPONSE_OZZTEN = 1010;
	public final static int RESPONSE_OZZELEVEN = 1011;
	public final static int RESPONSE_OZZTWELVE = 1012;
	public final static int RESPONSE_OZZTHIRTEEN = 1013;
	public final static int RESPONSE_OZZFOURTEEN = 1014;
	public final static int RESPONSE_OZZFIFTEEN = 1015;
	public final static int RESPONSE_OZZSIXTEEN = 1016;
	
	public final static int PAGE_NUM = 10;
	public final static int PAGE_HOME_NUM = 50;
}
