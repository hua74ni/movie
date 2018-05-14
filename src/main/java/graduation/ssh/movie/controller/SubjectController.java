package graduation.ssh.movie.controller;

import graduation.ssh.movie.dao.SubjectDao;
import graduation.ssh.movie.dao.CommentDao;
import graduation.ssh.movie.entity.Comment;
import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.rest.PageInfo;
import graduation.ssh.movie.security.AdminRequired;
import graduation.ssh.movie.service.DoubanService;
import graduation.ssh.movie.service.SubjectService;
import graduation.ssh.movie.solrJ.SolrJTest;
import graduation.ssh.movie.utils.ImageUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Controller
@RequestMapping("/subject")
public class SubjectController extends BaseController {

    @Autowired
    private DoubanService doubanService;

    @Autowired
    private SubjectService subjectService;

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
    public String getDelete(@PathVariable(value = "id") String id) throws IOException, SolrServerException {
        Subject subject = doubanService.find(id);
        if (null == subject) {
            return "/misc/404";
        } else {
            //清楚solr里的缓冲 并 删除数据的里的数据
            //选择具体的某一个solr core
            HttpSolrClient server = new HttpSolrClient(SubjectDao.SOLR_URL);
            //删除文档
            server.deleteById(id);
            //删除所有的索引
            //solr.deleteByQuery("*:*");
            //提交修改
            server.commit();
            server.close();
            subjectDao.delete(subject);
            ImageUtils.deleteImage(id);
        }
        return "redirect:/";
    }

    @RequestMapping("/adminDelete")
    @AdminRequired
    public Map<String,Object> adminDelete(String id) throws IOException, SolrServerException {
        Map<String,Object> map = new HashMap<>();
        Subject subject = doubanService.find(id);
        if (null == subject) {
            map.put("code",0);
        } else {
            //清楚solr里的缓冲 并 删除数据的里的数据
            //选择具体的某一个solr core
            HttpSolrClient server = new HttpSolrClient(SubjectDao.SOLR_URL);
            //删除文档
            server.deleteById(id);
            //删除所有的索引
            //solr.deleteByQuery("*:*");
            //提交修改
            server.commit();
            server.close();
            subjectDao.delete(subject);
            ImageUtils.deleteImage(id);
            map.put("code",1);
        }
        return map;
    }

    @RequestMapping("/adminList")
    @ResponseBody
    public Map<String, Object> adminList(int length, int start,
                                         @RequestParam(value = "sort",defaultValue = "不限") String sort,
                                         @RequestParam(value = "type",defaultValue = "不限") String type,
                                         @RequestParam(required=false,name="search[value]") String key){

        Map<String, Object> map = null;

        try{
            map = subjectService.getSubjectList(start,length,sort,type,key);
        }catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }


}
