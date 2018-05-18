package graduation.ssh.movie.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

/**
 * Created by huangdonghua on 2017/12/28.
 */
public class ImageUtils {

    public static final String IMAGEURL = "/loadImage/";

    private static final String IMAGESUFFIX = ".jpg";

    private static final String IMAGE_NAME = "movie_";

    private static Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    public static void deleteImage(String subjectId){

        String shopImagePath = PropertiesUtils.getString("movie.image.path");
        String filePath = shopImagePath + "/" + IMAGE_NAME + subjectId +IMAGESUFFIX;
        File file = new File(filePath);

        try {
            FileUtils.forceDelete(file);
            logger.info("image :{}  删除状态 :{}",IMAGE_NAME+subjectId+IMAGESUFFIX,"成功");
        } catch (IOException e) {
            logger.error("image :{}  删除状态 :{}",IMAGE_NAME+subjectId+IMAGESUFFIX,"失败");
            e.printStackTrace();
        }

    }

    public static void uploadImage(String imageURL,String subjectId){
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
            //      movie_1764796.jpg
            fileOutputStream = new FileOutputStream(new File(imagePath,IMAGE_NAME + subjectId +IMAGESUFFIX));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
            logger.info("upload image :{} to :{},状态 :{}", imageURL, subjectId, "成功");
        } catch (IOException e) {
            logger.error("upload image :{} to :{},状态 :{}",imageURL, subjectId, "失败");
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
