package com.lin.sqzmHtgl.config;


import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lin
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

//    @Autowired
//    private LoginInterceptor loginInterceptor;

    //跨域
    @Override
    public void addCorsMappings(@NotNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
/*               .allowedOrigins("http://localhost:8192","http://127.0.0.1:8192","http://192.168.43.5:8192")
                .allowedOriginPatterns("http://localhost:8192","http://127.0.0.1:8192","http://192.168.43.5:8192")*/

    /***
     * 自定义资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        //swagger进行配置
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");//ui地址
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");//增强版ui地址
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
