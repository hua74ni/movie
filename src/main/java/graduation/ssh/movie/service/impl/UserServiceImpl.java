package graduation.ssh.movie.service.impl;

import graduation.ssh.movie.entity.User;
import graduation.ssh.movie.service.UserService;
import graduation.ssh.movie.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by huangdonghua on 2017/12/28.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void create(User user) {
        userDao.create(user);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
