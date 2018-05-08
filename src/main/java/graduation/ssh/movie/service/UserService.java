package graduation.ssh.movie.service;

import graduation.ssh.movie.entity.User;

import java.util.List;

/**
 * Created by huangdonghua on 2017/12/28.
 */
public interface UserService {

    public void create(User user);

    public List<User> findAll();
}
