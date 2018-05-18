package graduation.ssh.movie.dao;

import graduation.ssh.movie.entity.Playing;
import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.rest.PageInfo;
import graduation.ssh.movie.utils.OrderBySqlFormulaUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Repository
public class SubjectDao extends BaseDao<Subject> {

    public final static String SOLR_URL = "http://localhost:8080/solr/new_core";

    @Autowired
    private PlayingDao playingDao;

    public SubjectDao() {
        super(Subject.class);
    }

    public List<Subject> listBySearch(int start, int end, String year, String place, String type, String sort, String key) {
        Criteria criteria = getCurrentSession().createCriteria(Subject.class);
        if (!year.contains("不限")) {
            criteria.add(Restrictions.eq("year", new Short(year)));
        }
        if (!place.contains("不限")) {
            criteria.add(Restrictions.like("countries", "%".concat(place).concat("%")));
        }
        if (!type.contains("不限")) {
            criteria.add(Restrictions.like("genres", "%".concat(type).concat("%")));
        }
        if (sort.equals("rating")) {
            criteria.addOrder(OrderBySqlFormulaUtils.sqlFormula("rating desc"));
        } else if (sort.equals("date")) {
            criteria.addOrder(Order.desc("pubDate"));
        } else if (sort.equals("hot")) {
            criteria.addOrder(Order.desc("commentCount"));
        }
        if (StringUtils.isNotEmpty(key)) {
            criteria.add(Restrictions.disjunction()
                            .add(Restrictions.like("title", "%".concat(key).concat("%")))
                            .add(Restrictions.like("originalTitle", "%".concat(key).concat("%")))
                            .add(Restrictions.like("casts", "%".concat(key).concat("%")))
                            .add(Restrictions.like("directors", "%".concat(key).concat("%")))
                            .add(Restrictions.like("writers", "%".concat(key).concat("%")))

            );
        }
        criteria.setFirstResult(start);
        criteria.setMaxResults(end);
        return criteria.list();
    }

    public int countBySearch(String year, String place, String type, String sort, String key) {
        Criteria criteria = getCurrentSession().createCriteria(Subject.class).setProjection(Projections.rowCount());
        if (!year.contains("不限")) {
            criteria.add(Restrictions.eq("year", new Short(year)));
        }
        if (!place.contains("不限")) {
            criteria.add(Restrictions.like("countries", "%".concat(place).concat("%")));
        }
        if (!type.contains("不限")) {
            criteria.add(Restrictions.like("genres", "%".concat(type).concat("%")));
        }
        if (sort.equals("rating")) {
            criteria.addOrder(OrderBySqlFormulaUtils.sqlFormula("rating desc"));
        } else if (sort.equals("date")) {
            criteria.addOrder(Order.desc("pubDate"));
        }
        if (StringUtils.isNotEmpty(key)) {
            criteria.add(Restrictions.disjunction()
                            .add(Restrictions.like("title", "%".concat(key).concat("%")))
                            .add(Restrictions.like("originalTitle", "%".concat(key).concat("%")))
                            .add(Restrictions.like("casts", "%".concat(key).concat("%")))
                            .add(Restrictions.like("directors", "%".concat(key).concat("%")))
                            .add(Restrictions.like("writers", "%".concat(key).concat("%")))
            );
        }
        Long count = (Long) (criteria.uniqueResult());
        return count.intValue();
    }

    public List<Subject> getPlaying() {
        List<Playing> playingList = playingDao.findAll();
        if (playingList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Subject> playingSubjects = new ArrayList<Subject>(playingList.size());
        Subject subject;
        for (Playing playing : playingList) {
            subject = find(playing.getId());
            playingSubjects.add(subject);
        }
        return playingSubjects;
    }


    //通过 solr查询
    public PageInfo<Subject> queryPageInfoBySolrJ(PageInfo<Subject> pageInfo, int end, String year, String place, String type, String sort, String key) throws IOException, SolrServerException {
        HttpSolrClient solrServer = new HttpSolrClient(SOLR_URL);
        SolrQuery query = new SolrQuery();


        //参数fq, 给query增加过滤查询条件
        if (!year.contains("不限")) {
            // 将年份作为查询过滤条件
            query.addFilterQuery("year:"+year);
        }
        if (!place.contains("不限")) {
            // 将国家作为查询过滤条件；需要在solr上实现模糊查询
            query.addFilterQuery("countries:"+place);
        }
        if (!type.contains("不限")) {
            // 将电影类型作为查询过滤条件，需要在solr上实现模糊查询
            query.addFilterQuery("genres:"+type);
        }
//        casts:张国荣 title:张国荣
        //在solr查询上添加，参数sort,作为返回后的结果的指定排序规则
        if (sort.equals("rating")) {
            // 以电影评分作为排序字段
            query.setSort("rating",SolrQuery.ORDER.desc);
        } else if (sort.equals("date")) {
            // 以电影上映时间作为排序字段
            query.setSort("pubDate",SolrQuery.ORDER.desc);
        } else if (sort.equals("hot")) {
            // 以电影在本网站上评论次数作为排序字段
            query.setSort("commentCount",SolrQuery.ORDER.desc);
        }
        //下面设置solr查询参数
        if (StringUtils.isNotEmpty(key)) {
            // 中文分词查询，比如某条数据某个字段含有中国分词四个字
            // 拆分成 中国 分词 2个词组 将这2词组进行查询出来 ，这个作用适用于联想查询
            query.set("q","keywords:"+key);
        }else{
            query.set("q", "*:*");// 参数q  查询所有
        }

        //参数df,给query设置默认搜索域
//        query.set("df", "name");

        //设置分页参数
        query.setStart(pageInfo.getStartRow());
        query.setRows(end);//每一页多少值

        //参数hl,设置高亮
        query.setHighlight(true);
        //设置高亮的字段
        query.addHighlightField("title");
        //设置高亮的样式
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");

        //获取查询结果
        QueryResponse response = solrServer.query(query);
        //两种结果获取：得到文档集合或者实体对象

        //查询得到文档的集合
        SolrDocumentList solrDocumentList = response.getResults();

        //得到实体对象
        List<Subject> subjects = (List<Subject>) toBeanList(solrDocumentList,Subject.class);
        pageInfo.setResultList(subjects);
        pageInfo.setTotalRows((int)solrDocumentList.getNumFound());

        return pageInfo;
    }


    /**
     * 将SolrDocument转换成Bean
     * @param record
     * @param clazz
     * @return
     */
    public static Object toBean(SolrDocument record, Class clazz){
        Object obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field:fields){
                Object value = record.get(field.getName());
                if(value != null){
                    BeanUtils.setProperty(obj, field.getName(), value);
                }
            }
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * 将SolrDocumentList转换成BeanList
     * @param records
     * @param clazz
     * @return
     */
    public static Object toBeanList(SolrDocumentList records, Class clazz){
        List list = new ArrayList();
        for(SolrDocument record : records){
            list.add(toBean(record,clazz));
        }
        return list;
    }

    public List<Subject> getLeaderboard() {
        Criteria criteria = getCurrentSession().createCriteria(Subject.class);
        criteria.addOrder(OrderBySqlFormulaUtils.sqlFormula("rating desc"));
        criteria.setFirstResult(0);
        criteria.setMaxResults(10);
        return criteria.list();
    }
}
