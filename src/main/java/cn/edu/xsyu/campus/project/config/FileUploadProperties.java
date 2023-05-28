package cn.edu.xsyu.campus.project.config;

import io.netty.util.internal.StringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 文件上传参数配置类
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
@Component
@ConfigurationProperties(prefix = "file")
public class FileUploadProperties {

    private String path;
    private String url;
    private String accessUrl;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;

        //set accessUrl
        if (StringUtils.isEmpty(url)) {
            this.accessUrl = null;
        }
        this.accessUrl = url.substring(url.lastIndexOf("/")) + "/**";
        System.out.println("accessUrl=" + accessUrl);
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public String convertURLPath(List<String> urlList){
        String path;
        Objects.requireNonNull(urlList, "urlList cannot be null");
        String[] paths = this.path.split("/static");
        if (paths != null && paths.length > 2) {
            path = paths[1];
            urlList = Optional.ofNullable(urlList).orElse(Collections.emptyList());
            return urlList.stream().map(url -> url.replaceAll(this.url, path)).collect(Collectors.joining("#"));
        }
        return StringUtil.EMPTY_STRING;
    }
}