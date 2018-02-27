package com.xunsi.fs.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tools {
	public static String md5JM(String plainText ) { 
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(plainText.getBytes("utf-8")); 
			byte b[] = md.digest(); 
			int i; 
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) 
				buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			} 
			return buf.toString();
		} catch (Exception e) { 
			e.printStackTrace(); 
			return null; 
		}
	} 

	public static boolean valid(String json,String sig){
		boolean val = true;
		try {
			System.out.println(sig);
			System.out.println(md5JM(json));
			if(sig.equals(md5JM(json))){
				val = true;
			}else{
				val = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			val = false;
		}
		return val;
	}
    public final static String md5Java(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String MD5(String message) {  
        String digest = null;  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            byte[] hash = md.digest(message.getBytes("UTF-8"));  
  
            //converting byte array to Hexadecimal String  
            StringBuilder sb = new StringBuilder(2 * hash.length);  
            for (byte b : hash) {  
                sb.append(String.format("%02x", b & 0xff));  
            }  
            digest = sb.toString();  
        } catch (UnsupportedEncodingException ex) {  
            //Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE, null, ex);  
        } catch (NoSuchAlgorithmException ex) {  
            //Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE, null, ex);  
        }  
        return digest;  
    }  
	public static void main(String[] args) throws UnsupportedEncodingException {
		String sig="b5cdc5f59a0b53c0d8d7f0a0f16749b9";
		String json="%7B%22nickName%22%3A%22weiwei%22%2C%22mobile%22%3A%2218210236982%22%2C%22userId%22%3A%22100000120%22%2C%22realName%22%3A%22%E7%8E%8B%E7%BB%B4%E8%8B%B1%22%2C%22idCade%22%3A%22130434198803150848%22%7D";
		MD5Tools.valid(json, sig);
	    
	}
}
