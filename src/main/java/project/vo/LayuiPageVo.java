package project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "LayuiPageVo",description ="分页数据格式返回类" )
public class LayuiPageVo<T> {


    @ApiModelProperty(value = "",name = "msg",notes ="描述返回状态" )
    private String msg;
    @ApiModelProperty(value = "",name = "code",notes ="状态码" )
    private Integer code;
    @ApiModelProperty(value = "",name = "count",notes ="数量" )
    private Integer count;
    @ApiModelProperty(value = "",name = "data",notes ="返回数据" )
    private T data;

    public LayuiPageVo(String msg, Integer code, Integer count, T data) {
        this.msg = msg;
        this.code = code;
        this.count = count;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
