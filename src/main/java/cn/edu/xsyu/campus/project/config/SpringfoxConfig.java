package cn.edu.xsyu.campus.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author chuan
 * @Date 2020-12-27-11:59
 */
@Configuration
@EnableSwagger2
@ComponentScan("com.controller")
public class SpringfoxConfig {


    @Bean
    public Docket createRestApi() {
        return new Docket (DocumentationType.SWAGGER_2)
                .apiInfo (apiInfo ())
                .select ()
                .apis (RequestHandlerSelectors.basePackage ("com.controller"))
                .paths (PathSelectors.any ())
                .build ();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact ("chuan", "", "468336329a@gmail.com");
        return new ApiInfoBuilder ()
                .title ("xianyu api文档")
                .description ("")
                .contact (contact)
                .version ("1.0.0")
                .build ();
    }
}

