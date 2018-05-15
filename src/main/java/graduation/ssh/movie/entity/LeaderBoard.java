package graduation.ssh.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Data
@Entity
@Table(name = "leaderboard")
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoard {
    //对应豆瓣电影的id
    @Id
    @Column(length = 8)
    private String id;
}