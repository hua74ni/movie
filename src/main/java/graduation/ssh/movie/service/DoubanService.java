package graduation.ssh.movie.service;

import graduation.ssh.movie.entity.Subject;

import java.util.List;

/**
 * Created by huangdonghua on 2017/12/28.
 */
public interface DoubanService {

    //存在直接返回，不存在从豆瓣抓取后返回
    public Subject find(String id);

    public List<Subject> findPlaying();

    public void saveBySearch(String q);

}
