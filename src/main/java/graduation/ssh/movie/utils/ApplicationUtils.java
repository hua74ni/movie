package graduation.ssh.movie.utils;

import graduation.ssh.movie.dao.SubjectDao;
import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * for jstl
 * <p/>
 * Created by huangdonghua on 2017/12/28.
 */
@Service
public class ApplicationUtils {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SubjectDao subjectDao;

    public String findUserNameById(String id) {
        return userDao.find(id).getUserName();
    }

    public Subject findSubjectById(String id) {
        return subjectDao.find(id);
    }

}
