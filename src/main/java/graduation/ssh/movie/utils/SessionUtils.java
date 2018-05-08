package graduation.ssh.movie.utils;

import lombok.Data;
import graduation.ssh.movie.entity.User;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Data
public class SessionUtils {

    /**
     * The current login user.
     */
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
