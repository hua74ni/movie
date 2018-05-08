package graduation.ssh.movie.service;

import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.rest.PageInfo;

import java.util.List;

/**
 * Created by huangdonghua on 2017/12/28.
 */
public interface SubjectService {

    public PageInfo<Subject> listBySearch(int pageNo, int pageSize, String year, String place, String type, String sort, String key);

    public List<Subject> getLeaderboard();
}
