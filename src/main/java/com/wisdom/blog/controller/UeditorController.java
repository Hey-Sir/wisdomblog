package com.wisdom.blog.controller;

import com.alibaba.fastjson.JSON;
import com.wisdom.blog.domain.User;
import com.wisdom.blog.util.PublicMsg;
import com.wisdom.blog.vo.Ueditor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
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
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            User principal = null;
            String username = null;
            if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                    && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
                principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                username = principal.getUsername();
            }else
                return null;

            inputStream = upfile.getInputStream();
            String fileName = upfile.getOriginalFilename();
            String imgFormat = fileName.substring(fileName.indexOf(".") + 1);
            fileName= username + "_blogs_" + System.currentTimeMillis() + "." + imgFormat;
            File upLoadDir = new File("blogimage");
            if(!upLoadDir.exists()){
                upLoadDir.mkdir();
            }
            File file = new File(upLoadDir.getName() + File.separator + fileName );
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int count;
            while ((count = inputStream.read(b)) != -1){
                fos.write(b,0,count);
            }
            ueditor.setState("SUCCESS");
            ueditor.setTitle(fileName);
            ueditor.setUrl(fileName);
            ueditor.setOriginal(fileName);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.toJSONString(ueditor);
    }

}
