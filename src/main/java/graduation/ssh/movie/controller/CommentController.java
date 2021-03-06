package graduation.ssh.movie.controller;

import graduation.ssh.movie.dao.CommentDao;
import graduation.ssh.movie.dao.SubjectDao;
import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.entity.User;
import graduation.ssh.movie.rest.RestData;
import graduation.ssh.movie.utils.ContextUtils;
import graduation.ssh.movie.entity.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private SubjectDao subjectDao;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ModelAndView getComments(Comment comment, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/subject/" + comment.getSubjectId());
        if (StringUtils.isEmpty(ContextUtils.getUserId(request))) {
//            modelAndView = new ModelAndView("redirect:/subject/" + comment.getSubjectId()+"?error=登陆后才能发布评论");
//            modelAndView.addObject("error", "登陆后才能发布评论");
            attributes.addFlashAttribute("error", "登陆后才能发布评论");
            return modelAndView;
        }
        comment.setUserId(ContextUtils.getUserId(request));
        comment.setSubmitDate(new Date());
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        commentDao.create(comment);
        Subject subject = subjectDao.find(comment.getSubjectId());
        subject.setTotalRating(subject.getTotalRating() + Double.parseDouble(comment.getRating()));
        subject.setRatingCount(subject.getRatingCount() + 1);
        subject.setCommentCount(subject.getCommentCount() + 1);
        subjectDao.update(subject);
        return modelAndView;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RestData deleteComment(String id) {
        Comment comment = commentDao.find(id);
        RestData restData = new RestData();
        if (null == comment) {
            restData.setComment("该评论不存在哦！");
            return restData;
        }
        User currentUser = ContextUtils.getUser(request);
        if (null == currentUser) {
            restData.setComment("您还未登陆哦！");
            return restData;
        }
        if (currentUser.isAdmin() || currentUser.getId().equals(comment.getUserId())) {
            Subject subject = subjectDao.find(comment.getSubjectId());
            subject.setCommentCount(subject.getCommentCount() - 1);
            subject.setTotalRating(subject.getTotalRating() - Double.parseDouble(comment.getRating()));
            subject.setRatingCount(subject.getRatingCount() - 1);
            subjectDao.update(subject);
            commentDao.delete(comment);
            restData.setSuccess(1);
            return restData;

        } else {
            restData.setComment("你不是该评论的发表人或管理员，无权限删除该评论");
            return restData;
        }
    }
}
