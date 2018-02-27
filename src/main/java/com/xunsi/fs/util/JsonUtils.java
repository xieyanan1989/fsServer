package com.xunsi.fs.util;

import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class JsonUtils {
	protected static Logger log=Logger.getLogger(JsonUtils.class);
	
	public static String AjaxJson(Map<?, ?> map){
		JSONObject json = new JSONObject(map);
		return json.toString();
	}
	
}
