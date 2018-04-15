package com.xunsi.fs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class UTIL {
	 /**
	  * 测试
   * 图片临时存放路径
   */
//	 public static String user_dir = "userImg/";
//	public static String line = "\\\\";
//	public static String temporary = "E:\\temp\\";
//	public static String user_img = "E:\\userImg\\";
//	 public static String pro_img = "E:\\proImg\\";
//	public static String tempurl = "HTTP://127.0.0.1:80/ImagesUploaded/";
	
	/**
	 * 正式
	 */
	public static String user_dir = "userImg/";
	public static String line = "//";
	public static String temporary = "//var//www//html//img_tempdir//";
	public static String user_img = "//var//www//html//img_user//";
	public static String pro_img = "//var//www//html//img_pro//";
	public static int fileChannelCopy(String imgUrl,String userName) {
		int result = 0 ;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			//判断用户文件夹是否存在，不存在则创建
			judgeDir(userName);
			File s = new File(temporary+imgUrl);
			File t = new File(user_img+userName+line+imgUrl);
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

	private static void judgeDir(String userName) {
		File file = new File(user_img+userName);
		if (file.exists()) {
			if (file.isDirectory()) {
				System.out.println("dir exists");
			} else {
				System.out.println("the same name file exists, can not create dir");
			}
		} else {
			System.out.println("dir not exists, create it ...");
			file.mkdir();
		}

	}
}
