package graduation.ssh.movie.scheduler;

import graduation.ssh.movie.dao.SubjectDao;
import graduation.ssh.movie.dao.PlayingDao;
import graduation.ssh.movie.entity.Playing;
import graduation.ssh.movie.service.DoubanService;
import graduation.ssh.movie.service.impl.DoubanServiceImpl;
import graduation.ssh.movie.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Component
public class DouBanSchedule {

    private static Logger logger = LoggerFactory.getLogger(DouBanSchedule.class);

    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private PlayingDao playingDao;

    @Autowired
    private DoubanService doubanService;

    @Scheduled(cron = "0 19 01 * * ? ")   //每天凌晨三点执行一次
    public void renewPlaying() {
        String id;
        List<Playing> playingList;
        Map<String,String> map = new HashMap<>();
        map.put("start","0");
        map.put("count","40");
        map.put("city","福州");
        map.put("apikey",DoubanServiceImpl.DOUBAN_APIKEY);
        String responseBody = HttpUtils.URLGet(DoubanServiceImpl.DOUBAN_PLAYING_URL,map,HttpUtils.URL_PARAM_DECODECHARSET_UTF8);
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray jsonArray = jsonObject.getJSONArray("subjects");
        int length = jsonArray.length();
        logger.debug("{} movies are playing", length);
        playingList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            id = jsonArray.getJSONObject(i).getString("id");
            doubanService.find(id);
            playingList.add(new Playing(id));
        }
        playingDao.renew(playingList);
    }
}
