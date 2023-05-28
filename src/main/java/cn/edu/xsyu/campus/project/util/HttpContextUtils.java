package cn.edu.xsyu.campus.project.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * HttpContextUtils
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
public class HttpContextUtils {

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {

        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        // 如果是异步请求或是手机端，则直接返回信息
        return ((accept != null && accept.contains("application/json")
                || (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest"))
        ));
    }
}
