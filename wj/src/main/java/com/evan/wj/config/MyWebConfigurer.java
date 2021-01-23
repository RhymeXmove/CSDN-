package com.evan.wj.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {

//    getMenusByRoleId


//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
////        对所有路径应用拦截器
//        registry.addInterceptor(getLoginInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/index.html")
//                .excludePathPatterns("/api/register")
//                .excludePathPatterns("/api/login")
//                .excludePathPatterns("/api/logout")
//                .excludePathPatterns("/api/books");
//    }


    //    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/api/file/**").addResourceLocations("file:" + "D:\\workspace\\WhiteJotter\\img\\");
//    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
//                .maxAge(3600)
        ;
    }



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/file/**").addResourceLocations("file:" + "D:\\workspace\\WhiteJotter\\img\\");
    }

}
