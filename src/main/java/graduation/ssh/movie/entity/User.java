package graduation.ssh.movie.entity;

import lombok.Data;
import graduation.ssh.movie.validator.constraints.UserName;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by huangdonghua on 2017/12/28.
 */

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 32)
    private String id;

    @Column(length = 20)
    @UserName
    private String userName;

    @Column(length = 32)
    private String password;

    private Date createTime;

    //是否为管理员
    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean admin;


}
