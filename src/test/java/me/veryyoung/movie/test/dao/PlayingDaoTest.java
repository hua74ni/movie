package me.veryyoung.movie.test.dao;

import me.veryyoung.movie.dao.PlayingDao;
import me.veryyoung.movie.entity.Playing;
import me.veryyoung.movie.test.AbstractSpringTest;
import me.veryyoung.movie.utils.PropertiesUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.URL;

/**
 * Created by veryyoung on 2015/5/7.
 */

public class PlayingDaoTest extends AbstractSpringTest {

    private final String IMAGESUFFIX = ".jpg";

    @Autowired
    private PlayingDao playingDao;

    @Test
    public void test() {
        String[] idList = {"111", "222", "333", "444"};
        for (String id : idList) {
            Playing playing = new Playing(id);
            playingDao.create(playing);
        }
    }

    @Test
    public void testCeil() {
        System.out.println(1 / 6);
        System.out.println(Math.ceil(((float)1 / 6)));
    }

    @Test
    public void testImage() {
        String imageURL = "http://img7.doubanio.com\\/view\\/photo\\/s_ratio_poster\\/public\\/p2507227732.jpg";
        imageURL = imageURL.replace("\\","");

        //获取电影图片保存路径
        String imagePath = PropertiesUtils.getString("movie.image.path");

        File folder = new File(imagePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        DataInputStream dataInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            dataInputStream = new DataInputStream((new URL(imageURL).openStream()));

            fileOutputStream = new FileOutputStream(new File(imagePath,"movie_26862829"+IMAGESUFFIX));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context=output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();

        } catch (IOException e) {
            logger.error("图片上传失败");
            e.printStackTrace();
        }finally {
            try {
                if (dataInputStream != null){
                    dataInputStream.close();
                }
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
