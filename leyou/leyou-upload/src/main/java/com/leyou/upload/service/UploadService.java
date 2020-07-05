package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    //制作白名单，一个集合,所有枚举类型
    private static final List<String> content_type= Arrays.asList("image/gif","image/jpeg");

    //log日志，log对象
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);
    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadImage(MultipartFile file) {
        //获取文件类型
        String originalFilename = file.getOriginalFilename();
        //StringUtils.substringAfterLast()
        //校验文件类型
            //获取枚举类型
        String contentType = file.getContentType();
            //判断是否包含
        if (!content_type.contains(contentType)){
            //LOGGER.info("文件类型不合法："+originalFilename);
            LOGGER.info("文件类型不合法：{}",originalFilename);  //{}是占位符  值会填充到{}里面
            return null;
        }

        try {
            //检验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage==null ) {
                LOGGER.info("文件内容不合法：{}",originalFilename);  //{}是占位符  值会填充到{}里面
                return null;
            }
            //保存到文件的服务器
            //file.transferTo(new File("D:\\IdeaProjects\\image\\"+originalFilename));   //文件名生产一个不唯一的随机数
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            //返回url，进行回显
            //return "http://image.leyou.com/"+originalFilename;
            return "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器内部错误：{}",originalFilename);
            e.printStackTrace();
        }
        return null;

    }
}
