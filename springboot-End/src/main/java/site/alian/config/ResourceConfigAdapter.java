package site.alian.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
*
* 将上传的图片资源存储在项目本地
*
* */


@Configuration
public class ResourceConfigAdapter extends WebMvcConfigurerAdapter {
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加图片映射(可以配置系统目录映射到项目目录)
        // 根据不同的系统设置不同的图片存储路径
        //获取文件的真实路径
        String os = System.getProperty("os.name");
        String winPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\static\\blogImage\\";
        //在Linux环境下的图片存放资源路径
        String linuxPath = "/usr/local/alian/images/";
        if (os.toLowerCase().startsWith("win")) {  //windows系统
            // 设置项目真实路径
            registry.addResourceHandler("/static/blogImage/**").addResourceLocations("file:"+winPath);
        }
        else{// linux系统
            registry.addResourceHandler("/images/**").
                    addResourceLocations("file:"+linuxPath);
        }
        //获取文件的真实路径
    }
}
