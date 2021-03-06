package graduation.ssh.movie.rest;

import lombok.Data;

/**
 * Created by huangdonghua on 2017/12/28.
 * Define a standard JSON return format for client
 */
@Data
public class RestData {

    //response status
    private int success = 0;  // 默认为0,失败
    //return code
    private String code;
    //if some error occurs, it will set some reasons to comment
    private String comment;
    //data field
    private Object data;
}
