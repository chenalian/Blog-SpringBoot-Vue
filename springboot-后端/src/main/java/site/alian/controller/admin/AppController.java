package site.alian.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.alian.oss.base.BaseOSS;
import site.alian.utils.BlogUtil;
import site.alian.utils.R;

import java.io.InputStream;

/**
 * 管理员公共接口
 */
@RestController("AdminAppController")
@RequestMapping("/api/admin")
public class AppController {

    @Autowired
    private BaseOSS oss;

    // 后期文件改进
    // 直接将图片存入服务器，然后在返回服务器获取静态图片的的链接就可以拿到图片了
    // 图片名字的话，就根据时间戳来生成吧
    @PostMapping("/upload")
    public R upload(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        String fileName = file.getOriginalFilename();
        // 获取图片的类型
        String type=fileName.substring(fileName.indexOf(".")+1,fileName.length());
        String path=BlogUtil.saveImage(is,type);
        return R.success("图片上传成功！").setAttribute("path", path);
    }
}