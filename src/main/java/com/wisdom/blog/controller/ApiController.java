package com.wisdom.blog.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/asset/{dir}/{imageName}")
    public void showUeditPic(HttpServletResponse response, @PathVariable String dir, @PathVariable String imageName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + imageName);
        IOUtils.copy(new FileInputStream(dir + "/" + imageName),response.getOutputStream());
        response.flushBuffer();
    }

}
