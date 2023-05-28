package cn.edu.xsyu.campus.project.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ResultVo",description = "普通返回的数据统一json格式")
public class ResultVo {

    @ApiModelProperty(value = "",name = "flag",notes ="描述是否成功" )
    private boolean flag;   //是否成功
    @ApiModelProperty(value = "",name = "status",notes ="描述返回状态码" )
    private Integer status; //状态码
    @ApiModelProperty(value = "",name = "message",notes ="描述返回信息" )
    private String message; //返回信息
    @ApiModelProperty(value = "",name = "data",notes ="返回数据" )
    private Object data;    //返回数据

    public ResultVo(){
        super();
    }

    public ResultVo(boolean flag, Integer status, String message, Object data) {
        this.flag = flag;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResultVo(boolean flag, Integer status, String message) {
        this.flag = flag;
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
