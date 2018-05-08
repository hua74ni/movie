package graduation.ssh.movie.entity;

import lombok.Data;
import graduation.ssh.movie.utils.ImageUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 电影条目
 * Created by huangdonghua on 2017/12/28.
 */

@Data
@Entity
@Table(name = "subject")
public class Subject {

    //对应豆瓣电影的id
    @Id
    @Column(length = 8)
    private String id;

    //标题
    private String title;

    //原名
    private String originalTitle;

    //评分人数
    private int ratingCount;

    //总评分
    private double totalRating;

    //平均分
    private double rating;

    //导演
    private String directors;

    //主演
    private String casts;

    //编剧
    private String writers;

    //上映日期
    private Date pubDate;

    //上映年份
    private short year;

    //语言
    private String languages;

    //片长
    private String durations;

    //影片类型
    private String genres;

    //制片国家/地区
    private String countries;

    //简介
    @Column(length = 1000)
    private String summary;

    //评论次数
    private int commentCount;

    public String getImage() {
        return ImageUtils.IMAGEURL + id;
    }

}
