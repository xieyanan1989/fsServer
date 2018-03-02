package com.xunsi.fs.util;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Map;

public class JsonUtils {
	protected static Logger log=Logger.getLogger(JsonUtils.class);
	
	public static String AjaxJson(Map<?, ?> map){
		JSONObject json = new JSONObject(map);
		return json.toString();
	}
	
}
