package com.imp.config.resources;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/WEB-INF/view/react/build/static/");

        registry.addResourceHandler("/**")
                .addResourceLocations("/WEB-INF/view/react/build/");

        registry.addResourceHandler("/")
                .addResourceLocations("/WEB-INF/view/react/build/index.html");
    }
}
