package graduation.ssh.movie.security;

/**
 * Created by huangdonghua on 2017/12/28.
 */

import java.lang.annotation.*;

/**
 * Need login to view/trigger action or resources no matter what privilege
 *
 * @author veryyoung
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface LoginRequired {
}
