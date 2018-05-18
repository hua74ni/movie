package graduation.ssh.movie.service.impl;

import graduation.ssh.movie.dao.PlayingDao;
import graduation.ssh.movie.dao.SubjectDao;
import graduation.ssh.movie.service.BaseService;
import graduation.ssh.movie.entity.Playing;
import graduation.ssh.movie.entity.Subject;
import graduation.ssh.movie.service.DoubanService;
import graduation.ssh.movie.utils.HttpUtils;
import graduation.ssh.movie.utils.ImageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Service
public class DoubanServiceImpl extends BaseService implements DoubanService {


    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private PlayingDao playingDao;

    private static final String DOUBAN_API_URL = "https://api.douban.com/v2/movie";

    public static final String DOUBAN_APIKEY = "0df993c66c0c636e29ecbb5344252a4a";

//    private static final String DOUBAN_SUBJECT_URL = DOUBAN_API_URL + "/subject/%s?" + DOUBAN_APIKEY;
    private static final String DOUBAN_SUBJECT_URL = DOUBAN_API_URL + "/subject/" ;

//    public static final String DOUBAN_PLAYING_URL = DOUBAN_API_URL + "/nowplaying?" + DOUBAN_APIKEY;
    //                  in_theaters/fuzhou/
    public static final String DOUBAN_PLAYING_URL = DOUBAN_API_URL + "/in_theaters" ;

    private static final String DOUBAN_SEARCH_URL = DOUBAN_API_URL + "/search";


    private JSONObject jsonObject;

    private JSONArray jsonArray;

    private int length;


    @Override
    public Subject find(String id) {
        Subject subject = subjectDao.find(id);

        if (null != subject) {
            return subject;
        }

//        String responseBody = HttpClientUtils.get(String.format(DOUBAN_SUBJECT_URL, id));
//        String responseBody2 = HttpUtils.URLGet("https://api.douban.com/v2/movie/subject/"+"26862829",null,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
        Map<String,String> map = new HashMap<>();

        try{
            map.put("apikey",DoubanServiceImpl.DOUBAN_APIKEY);
            String responseBody = HttpUtils.URLPost(DOUBAN_SUBJECT_URL+id,map,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);

            if (StringUtils.isEmpty(responseBody)) {
                return null;
            }
            jsonObject = new JSONObject(responseBody);
            subject = new Subject();
            subject.setId(jsonObject.getString("id"));
            subject.setTitle(jsonObject.getString("title"));
            subject.setOriginalTitle(jsonObject.getString("original_title"));

            subject.setRatingCount(jsonObject.getInt("ratings_count"));
            subject.setRating(jsonObject.getJSONObject("rating").getDouble("average"));
            subject.setTotalRating(subject.getRatingCount() * jsonObject.getJSONObject("rating").getDouble("average"));
            ImageUtils.uploadImage(jsonObject.getJSONObject("images").getString("large"),id);

            jsonArray = jsonObject.getJSONArray("directors");
            length = jsonArray.length();
            StringBuilder sb;
            if (length > 0) {
                sb = new StringBuilder(jsonArray.getJSONObject(0).getString("name"));
                for (int i = 1; i < length; i++) {
                    sb.append("/").append(jsonArray.getJSONObject(i).getString("name"));
                }
                subject.setDirectors(sb.toString());
            }

            jsonArray = jsonObject.getJSONArray("casts");
            length = jsonArray.length();
            if (length > 0) {
                sb = new StringBuilder(jsonArray.getJSONObject(0).getString("name"));
                for (int i = 1; i < length; i++) {
                    sb.append("/").append(jsonArray.getJSONObject(i).getString("name"));
                }
                subject.setCasts(sb.toString());
            }


            jsonArray = jsonObject.getJSONArray("writers");
            length = jsonArray.length();
            if (length > 0) {
                sb = new StringBuilder(jsonArray.getJSONObject(0).getString("name"));
                for (int i = 1; i < length; i++) {
                    sb.append("/").append(jsonArray.getJSONObject(i).getString("name"));
                }
                subject.setWriters(sb.toString());
            }

            String mainland_pubdate = jsonObject.getString("mainland_pubdate");
            if (StringUtils.isNotEmpty(mainland_pubdate)) {
                try {
                    subject.setPubDate(DateUtils.parseDate(mainland_pubdate, "yyyy-mm-dd"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (StringUtils.isNotEmpty(jsonObject.getString("year"))) {
                subject.setYear((short) jsonObject.getInt("year"));
            }

            jsonArray = jsonObject.getJSONArray("languages");
            length = jsonArray.length();
            if (length > 0) {
                sb = new StringBuilder(jsonArray.get(0).toString());
                for (int i = 1; i < length; i++) {
                    sb.append("/").append(jsonArray.get(i));
                }
                subject.setLanguages(sb.toString());
            }

            jsonArray = jsonObject.getJSONArray("durations");
            length = jsonArray.length();
            if (length > 0) {
                sb = new StringBuilder(jsonArray.get(0).toString());
                for (int i = 1; i < length; i++) {
                    sb.append("/").append(jsonArray.get(i));
                }
                subject.setDurations(sb.toString());
            }

            jsonArray = jsonObject.getJSONArray("genres");
            length = jsonArray.length();
            if (length > 0) {
                sb = new StringBuilder(jsonArray.get(0).toString());
                for (int i = 1; i < length; i++) {
                    sb.append("/").append(jsonArray.get(i));
                }
                subject.setGenres(sb.toString());
            }

            jsonArray = jsonObject.getJSONArray("countries");
            length = jsonArray.length();
            if (length > 0) {
                sb = new StringBuilder(jsonArray.get(0).toString());
                for (int i = 1; i < length; i++) {
                    sb.append("/").append(jsonArray.get(i));
                }
                subject.setCountries(sb.toString());
            }

            subject.setSummary(jsonObject.getString("summary"));

            subjectDao.create(subject);

            logger.debug("add subject [{}] from douban", subject);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return subject;
    }

    @Override
    public List<Subject> findPlaying() {
        List<Subject> result = subjectDao.getPlaying();
        if (result.isEmpty()) {
            Subject subject;
            result = new ArrayList<>();
            String id;
            List<Playing> playingList;
//            String responseBody = HttpClientUtils.get(DOUBAN_PLAYING_URL);
            Map<String,String> map = new HashMap<>();
            map.put("city","福州");
            map.put("apikey",DoubanServiceImpl.DOUBAN_APIKEY);
            String responseBody = HttpUtils.URLPost(DOUBAN_PLAYING_URL,map,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray jsonArray = jsonObject.getJSONArray("entries");
            int total = jsonArray.length();
            logger.debug("{} movies are playing", total);
            playingList = new ArrayList<>(total);
            for (int i = 0; i < total; i++) {
                id = jsonArray.getJSONObject(i).getString("id");
                subject = find(id);
                if(subject != null && subject.getTitle() != null && !subject.getTitle().equals("")){
                    result.add(subject);
                    playingList.add(new Playing(id));
                }
            }
            playingDao.renew(playingList);
        }
        return result;
    }

    @Override
    public void saveBySearch(String q) {
//        String responseBody = HttpClientUtils.get(String.format(DOUBAN_SEARCH_URL, q));
        Map<String,String> map = new HashMap<>();
        map.put("q",q);
        map.put("apikey",DoubanServiceImpl.DOUBAN_APIKEY);
        String responseBody = HttpUtils.URLPost(DOUBAN_SEARCH_URL,map,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray jsonArray = jsonObject.getJSONArray("subjects");
        int total = jsonArray.length();
        JSONObject jo;
        if (total > 0) {
            for (int i = 0; i < total; i++) {
                jo = jsonArray.optJSONObject(i);
                if (null != jo) {
                    find(jo.getString("id"));
                }
            }
        }
    }
}
