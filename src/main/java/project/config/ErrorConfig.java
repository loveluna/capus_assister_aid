package project.config;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/error/400");
        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");

        factory.addErrorPages(error400Page, error401Page, error404Page, error500Page);
    }
}
