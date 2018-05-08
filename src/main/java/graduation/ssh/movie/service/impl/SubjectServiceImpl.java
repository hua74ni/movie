package graduation.ssh.movie.service.impl;

import graduation.ssh.movie.dao.SubjectDao;
import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.rest.PageInfo;
import graduation.ssh.movie.service.BaseService;
import graduation.ssh.movie.service.SubjectService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Service
public class SubjectServiceImpl extends BaseService implements SubjectService {

    @Autowired
    private SubjectDao subjectDao;

    @Override
    public PageInfo<Subject> listBySearch(int pageNo, int pageSize, String year, String place, String type, String sort, String key) {
        PageInfo<Subject> pageInfo = new PageInfo<>(pageNo, pageSize);
//        pageInfo.setResultList(subjectDao.listBySearch(pageInfo.getStartRow(), pageSize, year, place, type, sort,key));
//        pageInfo.setTotalRows(subjectDao.countBySearch(year, place, type, sort,key));
        try {
            subjectDao.queryPageInfoBySolrJ(pageInfo, pageSize, year, place, type, sort,key);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return pageInfo;
    }

    @Override
    public List<Subject> getLeaderboard() {
        return subjectDao.getLeaderboard();
    }

}
