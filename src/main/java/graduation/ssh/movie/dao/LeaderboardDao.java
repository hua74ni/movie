package graduation.ssh.movie.dao;

import graduation.ssh.movie.entity.LeaderBoard;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huangdonghua on 2018/5/14.
 */
@Repository
public class LeaderboardDao extends BaseDao<LeaderBoard>{


    public LeaderboardDao() {
        super(LeaderBoard.class);
    }

    //更新local热映列表
    public void updateLeaderloard(List<LeaderBoard> leaderboardList) {
        deleteAll();
        for (LeaderBoard leaderBoard : leaderboardList) {
            create(leaderBoard);
        }
    }

}
