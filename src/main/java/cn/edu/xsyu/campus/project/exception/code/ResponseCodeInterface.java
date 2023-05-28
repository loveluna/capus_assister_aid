package cn.edu.xsyu.campus.project.exception.code;

/**
 * ResponseCodeInterface
 *
 * @author 宋亚超
 * @version V1.0
 * @date 2023年4月18日
 */
public interface ResponseCodeInterface {
    /**
     * 获取code
     *
     * @return code
     */
    int getCode();

    /**
     * 获取信息
     *
     * @return msg
     */
    String getMsg();
}
