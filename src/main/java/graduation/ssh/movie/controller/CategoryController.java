package graduation.ssh.movie.controller;

import graduation.ssh.movie.rest.PageInfo;
import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.service.DoubanService;
import graduation.ssh.movie.service.SubjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private DoubanService doubanService;

    @RequestMapping({"/index", ""})
    public ModelAndView index(String key) {
        ModelAndView modelAndView = new ModelAndView("/category/list");

        if (StringUtils.isNotEmpty(key)) {
            key = key.trim();
            modelAndView.addObject("key", key);
//            stop add movie by search movie name
//            doubanService.saveBySearch(key);
        }
        return modelAndView;
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageInfo<Subject> list(int pageNo, String year, String place, String type, String sort, String key) {
        return subjectService.listBySearch(pageNo, 6, year, place, type, sort, key);
    }

    @RequestMapping("/leaderboard")
    @ResponseBody
    public List<Subject> leaderboard(){

        List<Subject> subjectList = subjectService.getLeaderboard();

        return subjectList;
    }

}
