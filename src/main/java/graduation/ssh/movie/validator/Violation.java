package graduation.ssh.movie.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Data
@AllArgsConstructor
public class Violation {

    private String message;

    private Object invalidValue;

    private Object invalidObject;

}
