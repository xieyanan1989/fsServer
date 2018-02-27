package com.xunsi.fs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class UTIL {
	public static String user_dir = "userimg/";
	 /**
	  * 测试
   * 图片临时存放路径
   */
	public static String temporary = "E:\\temp\\";
	public static String user_img = "E:\\userimg\\";
//	public static String tempurl = "HTTP://127.0.0.1:80/ImagesUploaded/";
	
	/**
	 * 正式
	 */
//	public static String temporary = "//var//www//html//img_tempdir//"; 
	
	public static int fileChannelCopy(String olddir, String newdir) {
		int result = 0 ;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			File s = new File(temporary+olddir);
			File t = new File(user_img+newdir);
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();//得到对应的文件通道
			out = fo.getChannel();//得到对应的文件通道
			in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
			result = 1 ;
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				result = 1 ;
			}
		}
		return result;
	}
}
