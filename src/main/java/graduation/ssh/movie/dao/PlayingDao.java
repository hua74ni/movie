package graduation.ssh.movie.dao;

import graduation.ssh.movie.entity.Playing;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Repository
public class PlayingDao extends BaseDao<Playing> {

    public PlayingDao() {
        super(Playing.class);
    }

    //更新local热映列表
    public void renew(List<Playing> playingList) {
        deleteAll();
        for (Playing playing : playingList) {
            create(playing);
        }
    }

}
