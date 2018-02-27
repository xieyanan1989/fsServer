package com.xunsi.fs.util;

import java.io.BufferedInputStream;  
import java.io.BufferedOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.PrintWriter;  
import java.net.URLEncoder;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import org.apache.commons.fileupload.FileItem;  
import org.apache.commons.fileupload.FileUploadException;  
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;  
import org.apache.commons.fileupload.disk.DiskFileItemFactory;  
import org.apache.commons.fileupload.servlet.ServletFileUpload;  
import org.apache.log4j.Logger;

public class UploadUtil {  
	protected static Logger log=Logger.getLogger(UploadUtil.class);
    /** 
     * 允许上传的文件大小 
     */  
    private static long MAX_SIZE = 1024*1024;  
      
    /** 
     * 允许上传的文件类型 
     */  
    private static String[] allowedExt = new String[] { "jpg", "jpeg", "gif", "png"};

      
    public UploadUtil() {  
    }  
      
    /** 
     * @param request 
     * @param response 
     * @param path 用户设置的保存路径 
     * 上传文件并获取表单域及文件域的详细信息 
     * @throws Exception 
     */  
    public static Map<String,String> upload(HttpServletRequest request, HttpServletResponse response, String path) throws Exception {
    	String restr = "";
    	Map<String,String> map = new HashMap<String, String>();
    	/** 
    	 * 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload 
    	 */  
    	DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();  
    	diskFileItemFactory.setSizeThreshold(4096);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘  
    	/** 
    	 * 采用系统临时文件目录作为上传的临时目录 
    	 */  
    	File tempfile = new File(System.getProperty("java.io.tmpdir"));    
    	diskFileItemFactory.setRepository(tempfile);  

    	/** 
    	 * 用以上工厂实例化上传组件 
    	 * 设置最大上传尺寸 
    	 */  
    	ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);  
    	fileUpload.setSizeMax(MAX_SIZE);  

    	/** 
    	 * 调用FileUpload.settingHeaderEncoding(”UTF-8″)，这项设置可以解决路径或者文件名为乱码的问题。 
    	 * 设置输出字符集 
    	 */  
    	fileUpload.setHeaderEncoding("UTF-8");  
    	response.setContentType("text/html;charset=utf-8");  

    	PrintWriter out = response.getWriter();  
    	/** 
    	 * 从request得到 所有 上传域的列表 
    	 */  
    	List fileList = null;  
    	try {  
    		fileList = fileUpload.parseRequest(request);  
    	} catch (FileUploadException e) {  
    		if (e instanceof SizeLimitExceededException) {  
    			/** 
    			 * 文件大小超出限制 
    			 */  
    			restr = "文件尺寸超过规定大小:" + MAX_SIZE + "字节";
    			map.put("result",restr);
    			map.put("msg","1012");
    			return map;  
    		}  
    		e.printStackTrace();  
    	}  
    	/** 
    	 * 没有上传文件 
    	 */  
    	if (fileList == null || fileList.size() == 0) {  
    		//                out.println("请选择上传文件<p />");  
    		//                out.println("<a href=\"upload.html\" target=\"_top\">返回</a>");  
    		restr = "请选择上传文件";
    		map.put("result",restr);
    		map.put("msg","1011");
    		return map;  
    	}  
    	/** 
    	 * 得到所有上传的文件 
    	 * 对文件域操作 
    	 * 并保存每个文件的详细信息 
    	 */  
    	Iterator fileItr = fileList.iterator();  
    	//            Map fileInfo = null;  
    	while (fileItr.hasNext()) {  
    		FileItem fileItem = null;  
    		long size = 0;  
    		String userPath = null;  
    		String serverPath = null; 
    		String endPath = null; 
    		String fileName = null;  
    		String fileExt = null;  
    		fileItem = (FileItem) fileItr.next();  
    		/** 
    		 * 忽略简单form字段而不是上传域的文件域(<input type="text" />等) 
    		 */ 
    		if (!fileItem.isFormField()) {  

    			/** 
    			 * 得到文件的详细信息 
    			 * 客户端完整路径：userPath 
    			 * 服务器端完整路径：serverPath 
    			 * 大小：size 
    			 * 文件名：fileName 
    			 * 扩展名：fileExt 
    			 *  
    			 */  
    			userPath = fileItem.getName();  
    			size = fileItem.getSize();  
    			if ("".equals(userPath) || size == 0) {  
    				//                        out.println("请选择上传文件<p />");  
    				//                        out.println("<a href=\"upload.html\" target=\"_top\">返回</a>");  
    				restr = "请选择上传文件";
    				map.put("result",restr);
    				map.put("msg","1011");
    				return map;   
    			}  
    			fileName = userPath.substring(userPath.lastIndexOf("\\") + 1);  
    			fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);  

    			/** 
    			 * 文件类型是否合法 
    			 */  
    			int allowFlag = 0;  
    			int allowedExtCount = allowedExt.length;  
    			for (; allowFlag < allowedExtCount; allowFlag++) {  
    				if (allowedExt[allowFlag].toLowerCase().equals(fileExt.toLowerCase()))  
    					break;  
    			}  
    			if (allowFlag == allowedExtCount) {  
    				//                    	out.println("请上传以下类型的文件<p />");  
    				restr 	= "请上传以下类型的文件  ";
    				for (allowFlag = 0; allowFlag < allowedExtCount; allowFlag++)  
    					restr 	+= "*." + allowedExt[allowFlag].toLowerCase();
    				//                    		out.println("*." + allowedExt[allowFlag].toLowerCase()+ "&nbsp;&nbsp;&nbsp;");  
    				//                    	out.println("<p /><a href=\"upload.html\" target=\"_top\">返回</a>");  
    				map.put("result",restr);
    				map.put("msg","1010");
    				return map;   
    			}  
    			/** 
    			 * 根据系统时间生成上传后保存的文件名 
    			 */  
    			serverPath = path + System.currentTimeMillis() + "." + fileExt;  
    			endPath = System.currentTimeMillis() + "." + fileExt; 
    			try {  
    				/** 
    				 * 保存文件 
    				 */  
    				File diskPath = new File(path);  
    				if(!diskPath.exists()) {  
    					diskPath.mkdirs();  
    				}  
    				File diskFile = new File(serverPath);  
    				if(!diskFile.exists()) {  
    					diskFile.createNewFile();  
    				}  
    				fileItem.write(diskFile);  
    				map.put("msg","0");
//    				map.put("result","成功");
    				map.put("size",String.valueOf(size));
    				map.put("filename",endPath);
//    				map.put("imgname",fileExt);
//    				map.put("serverpath",endPath);
    				//                        map.put("serverPath",serverPath);
    				//                        map.put("result",restr);
    			} catch (Exception e) {  
    				e.printStackTrace();  
    			}  

    			//                    fileInfo = new HashMap();  
    			//                    fileInfo.put("size", String.valueOf(size));  
    			//                    fileInfo.put("userpath", userPath);  
    			//                    fileInfo.put("name",fileName);  
    			//                    fileInfo.put("ext", fileExt);  
    			//                    fileInfo.put("serverpath", serverPath);  
    			//                    fileInfoList.add(map);  
    		} else {  
    			String fieldName = fileItem.getFieldName();  
    			/** 
    			 * 在取字段值的时候，用FileItem.getString(”UTF-8″)，这项设置可以解决获取的表单字段为乱码的问题。 
    			 */   
    			String value = fileItem.getString("UTF-8");  
    			//                    parameters.put(fieldName, value);  
    		}  
    	}
    	return map;  
    }  
      
      
    /** 
     * 该方法支持支持国际化 
     * 但是文件名不能超过17个汉字 
     * 而且在IE6下存在bug 
     */  
    public void downloadI18N(HttpServletRequest request, HttpServletResponse response) throws Exception {  
        response.setContentType("text/html;charset=utf-8");  
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
  
        String filePath = request.getParameter("filePath");  
        String fileName = request.getParameter("fileName");  
        System.out.println(fileName);  
        try {  
            long fileLength = new File(filePath).length();  
  
            fileName = URLEncoder.encode(fileName, "UTF-8");  
            response.setContentType("application/x-msdownload;");  
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);  
            response.setHeader("Content-Length", String.valueOf(fileLength));  
  
            bis = new BufferedInputStream(new FileInputStream(filePath));  
            bos = new BufferedOutputStream(response.getOutputStream());  
            byte[] buff = new byte[2048];  
            int bytesRead;  
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
                bos.write(buff, 0, bytesRead);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bis != null)  
                bis.close();  
            if (bos != null)  
                bos.close();  
        }  
    }  
  
    /** 
     * 支持中文,文件名长度无限制 
     * 不支持国际化 
     */  
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {  
        response.setContentType("text/html;charset=utf-8");  
        request.setCharacterEncoding("UTF-8");  
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
  
        String filePath = request.getParameter("filePath");  
        String fileName = request.getParameter("fileName");  
        System.out.println(fileName);  
        try {  
            long fileLength = new File(filePath).length();  
  
            response.setContentType("application/x-msdownload;");  
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"),"ISO8859-1"));  
            response.setHeader("Content-Length", String.valueOf(fileLength));  
  
            bis = new BufferedInputStream(new FileInputStream(filePath));  
            bos = new BufferedOutputStream(response.getOutputStream());  
            byte[] buff = new byte[2048];  
            int bytesRead;  
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
                bos.write(buff, 0, bytesRead);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bis != null)  
                bis.close();  
            if (bos != null)  
                bos.close();  
        }  
    }  
  
    public String[] getAllowedExt() {  
        return allowedExt;  
    }  
  
    public void setAllowedExt(String[] allowedExt) {  
        this.allowedExt = allowedExt;  
    }  
  
    public long getMAX_SIZE() {  
        return MAX_SIZE;  
    }  
  
    public void setMAX_SIZE(long max_size) {  
        MAX_SIZE = max_size;  
    }

	public static Map<String, String> delImg(String delurl) {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		try {
			boolean end = true;
			File file = new File(delurl);
			 // 判断目录或文件是否存在  
		    if (!file.exists()) {  // 不存在返回 false  
		    	end = false;
		    } else {  
		        // 判断是否为文件  
		        if (file.isFile()) {  // 为文件时调用删除文件方法  
		        	end = deleteFile(delurl);  
		        } else {  // 为目录时调用删除目录方法  
		        	end = deleteDirectory(delurl);  
		        }  
		    }  
		    if(end == true){
		    	map.put("result", "0");
		    }else{
		    	map.put("result", "1");
		    }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map; 
	}  
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public static boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    try {
	    	 File file = new File(sPath);  
	 	    // 路径为文件且不为空则进行删除  
	 	    if (file.isFile() && file.exists()) {  
	 	        file.delete();  
	 	        flag = true;  
	 	    }  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	    return flag;  
	}  
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public static boolean deleteDirectory(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
		boolean flag = false;  
		try {
			if (!sPath.endsWith(File.separator)) {  
		        sPath = sPath + File.separator;  
		    }  
		    File dirFile = new File(sPath);  
		    //如果dir对应的文件不存在，或者不是一个目录，则退出  
		    if (!dirFile.exists() || !dirFile.isDirectory()) {  
		        return false;  
		    }  
		    flag = true;  
		    //删除文件夹下的所有文件(包括子目录)  
		    File[] files = dirFile.listFiles();  
		    for (int i = 0; i < files.length; i++) {  
		        //删除子文件  
		        if (files[i].isFile()) {  
		            flag = deleteFile(files[i].getAbsolutePath());  
		            if (!flag) break;  
		        } //删除子目录  
		        else {  
		            flag = deleteDirectory(files[i].getAbsolutePath());  
		            if (!flag) break;  
		        }  
		    }  
		    if (!flag) return false;  
		    //删除当前目录  
		    if (dirFile.delete()) {  
		        return true;  
		    } else {  
		        return false;  
		    }  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return flag;
	}  
}
