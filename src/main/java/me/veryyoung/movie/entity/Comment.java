package me.veryyoung.movie.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by veryyoung on 2015/5/11.
 */
@Data
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;

    private String subjectId;

    private String userId;

    private String rating;

    //简介
    @Column(length = 1000)
    private String content;

    private Date submitDate;

    public String getRating() {
        BigDecimal bigDecimal = new BigDecimal(rating);
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
    }

    public void setRating(double rating) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(rating));
        this.rating = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
    }

}
