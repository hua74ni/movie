package graduation.ssh.movie.controller;

import graduation.ssh.movie.dao.SubjectDao;
import graduation.ssh.movie.dao.CommentDao;
import graduation.ssh.movie.entity.Comment;
import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.rest.PageInfo;
import graduation.ssh.movie.security.AdminRequired;
import graduation.ssh.movie.service.DoubanService;
import graduation.ssh.movie.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Controller
@RequestMapping("/subject")
public class SubjectController extends BaseController {

    @Autowired
    private DoubanService doubanService;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private SubjectDao subjectDao;


    @RequestMapping("/{id}")
    public ModelAndView getSubject(@PathVariable(value = "id") String id, RedirectAttributes attributes) throws UnsupportedEncodingException {
        ModelAndView modelAndView = new ModelAndView("/subject/details");
        Subject subject = doubanService.find(id);
        if (null == subject) {
            return new ModelAndView("/misc/404");
        }
        modelAndView.addObject("subject", subject);
        if (subject.getCommentCount() > 0) {
            modelAndView.addObject("comments", commentDao.listBySubjectId(id, 0, 5));
        }
//        if (StringUtils.isNotEmpty(error)) {
//            modelAndView.addObject("error", error);
//        }
        return modelAndView;
    }

    @RequestMapping("/{id}/comments")
    public ModelAndView getComments(@PathVariable(value = "id") String id, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {
        ModelAndView modelAndView = new ModelAndView("/subject/comments");
        Subject subject = doubanService.find(id);
        modelAndView.addObject("subject", subject);
        if (null == subject) {
            return new ModelAndView("/misc/404");
        }
        PageInfo<Comment> pageInfo = new PageInfo<Comment>(pageNo, 5);
        pageInfo.setTotalRows(subject.getCommentCount());
        if (subject.getCommentCount() > 0) {
            pageInfo.setResultList(commentDao.listBySubjectId(id, pageInfo.getStartRow(), 5));
        }
        modelAndView.addObject("pageInfo", pageInfo);
        return modelAndView;
    }


    @RequestMapping("/{id}/edit")
    @AdminRequired
    public ModelAndView getEdit(@PathVariable(value = "id") String id) {
        ModelAndView modelAndView = new ModelAndView("/subject/edit");
        Subject subject = doubanService.find(id);
        modelAndView.addObject("subject", subject);
        if (null == subject) {
            return new ModelAndView("/misc/404");
        }
        return modelAndView;
    }

    @RequestMapping("/{id}/update")
    @AdminRequired
    public String getUpdate(@PathVariable(value = "id") String id, Subject subject) {
        Subject storedSubject = doubanService.find(id);
        if (null == storedSubject) {
            return "/misc/404";
        }
        storedSubject.setDirectors(subject.getDirectors());
        storedSubject.setCasts(subject.getCasts());
        storedSubject.setWriters(subject.getWriters());
        storedSubject.setGenres(subject.getGenres());
        storedSubject.setCountries(subject.getCountries());
        storedSubject.setLanguages(subject.getLanguages());
        storedSubject.setDurations(subject.getDurations());
        storedSubject.setOriginalTitle(subject.getOriginalTitle());
        storedSubject.setSummary(subject.getSummary());
        subjectDao.update(storedSubject);
        return "redirect:/subject/" + id;
    }

    @RequestMapping("/{id}/delete")
    @AdminRequired
    public String getDelete(@PathVariable(value = "id") String id) {
        Subject subject = doubanService.find(id);
        if (null == subject) {
            return "/misc/404";
        } else {
            subjectDao.delete(subject);
            ImageUtils.deleteImage(id);
        }
        return "redirect:/";
    }


}
