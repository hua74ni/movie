package graduation.ssh.movie.controller;

import graduation.ssh.movie.dao.CommentDao;
import graduation.ssh.movie.entity.User;
import graduation.ssh.movie.rest.PageInfo;
import graduation.ssh.movie.security.AdminRequired;
import graduation.ssh.movie.dao.UserDao;
import graduation.ssh.movie.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping("/{id}")
    public ModelAndView getAccount(@PathVariable(value = "id") String id, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        ModelAndView modelAndView = new ModelAndView("/user");
        User user = userDao.find(id);
        PageInfo<Comment> pageInfo = new PageInfo<Comment>(pageNo, 5);
        modelAndView.addObject("user", user);
        int commentCount = commentDao.countByUserId(id);
        modelAndView.addObject("commentCount", commentCount);
        if (commentCount > 0) {
            pageInfo.setResultList(commentDao.listByUserId(id, pageInfo.getStartRow(), 5));
            pageInfo.setTotalRows(commentDao.countByUserId(id));
        }
        modelAndView.addObject("pageInfo", pageInfo);

        return modelAndView;
    }

    @RequestMapping("/{id}/setAdmin")
    @AdminRequired
    public String setAdmin(@PathVariable(value = "id") String id) {
        User user = userDao.find(id);
        if (null == user) {
            return "/misc/404";
        }
        if (user.isAdmin()) {
            user.setAdmin(false);
        } else {
            user.setAdmin(true);
        }
        userDao.update(user);
        return "redirect:/user/" + id;
    }

}
