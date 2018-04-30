package com.xunsi.fs.util;

import com.xunsi.fs.controller.FsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadSpring {
    private static final Logger log = LoggerFactory.getLogger(UploadSpring.class);
    public static Map<String,String> uploadImg( HttpServletRequest request, HttpServletResponse response, String path) {
        Map map = new HashMap();
        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
            for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
                MultipartFile mf = entry.getValue();
                String fileName = mf.getOriginalFilename();
                String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
                String serverPath = path + System.currentTimeMillis() + "." + fileExt;
                String endPath = System.currentTimeMillis() + "." + fileExt;
                File uoloadFile = new File(path + endPath);
                try {
                    mf.transferTo(uoloadFile);
                    map.put("msg", "0");
                    map.put("filename", endPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("e:", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("e:", e);
        }
        return map;
    }
}
