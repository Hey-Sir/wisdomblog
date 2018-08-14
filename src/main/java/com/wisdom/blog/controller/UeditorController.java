package com.wisdom.blog.controller;

import com.alibaba.fastjson.JSON;
import com.wisdom.blog.util.PublicMsg;
import com.wisdom.blog.vo.Ueditor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/ueditor")
public class UeditorController {

    private static Logger logger = LoggerFactory.getLogger(UeditorController.class);

    @GetMapping
    public String ueditor(HttpServletRequest request){
        return PublicMsg.UEDITOR_CONFIG;
    }

    @PostMapping("/imgupload")
    public String imgUpload(MultipartFile upfile){
        logger.info("上传博客图片");
        Ueditor ueditor = new Ueditor();
        try {
            InputStream inputStream = upfile.getInputStream();
            String fileName = upfile.getOriginalFilename();
            String imgFormat = fileName.substring(fileName.indexOf(".") + 1);
            fileName= "blogs_" + System.currentTimeMillis() + "." + imgFormat;
            File upLoadDir = new File("blogimage");
            if(!upLoadDir.exists()){
                upLoadDir.mkdir();
            }
            File file = new File(upLoadDir.getName() + File.separator + fileName );
            FileOutputStream fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int count;
            while ((count = inputStream.read(b)) != -1){
                fos.write(b,0,count);
            }
            fos.close();
            inputStream.close();
            ueditor.setState("SUCCESS");
            ueditor.setTitle(fileName);
            ueditor.setUrl(fileName);
            ueditor.setOriginal(fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return JSON.toJSONString(ueditor);
    }

}
