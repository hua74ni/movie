package graduation.ssh.movie.controller;

import graduation.ssh.movie.entity.User;
import graduation.ssh.movie.rest.RestData;
import graduation.ssh.movie.security.LoginRequired;
import graduation.ssh.movie.service.DoubanService;
import graduation.ssh.movie.service.UserService;
import graduation.ssh.movie.utils.ContextUtils;
import graduation.ssh.movie.utils.PropertiesUtils;
import graduation.ssh.movie.validator.InvalidException;
import graduation.ssh.movie.dao.UserDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

/**
 * Created by huangdonghua on 2017/12/28.
 */
@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

    private static final String IMAGE_NAME = "movie_";

    private static final String IMAGESUFFIX = ".jpg";

    @Autowired
    private UserService userService;

    @Autowired
    private DoubanService doubanService;

    @Autowired
    private UserDao userDao;


    @RequestMapping({"/index", "/"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index");
        modelAndView.addObject("subjects", doubanService.findPlaying());
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegister() {
        return "/register";
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(User user, String captcha) {
        ModelAndView modelAndView = new ModelAndView("/register");
        String strCode = (String) request.getSession().getAttribute("strCode");
        if (!strCode.equals(captcha)) {
            modelAndView.addObject("error", "验证码错误");
            return modelAndView;
        }

        logger.debug("user:{}", user);
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        user.setCreateTime(new Date());
        try {
            getValidatorWrapper().tryValidate(user);
            userService.create(user);
        } catch (InvalidException ex) {
            logger.error("Invalid User Object: {}", user.toString(), ex);
            modelAndView.addObject("error", ex.getMessage());
            return modelAndView;
        }

        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/checkUserName", method = RequestMethod.GET)
    @ResponseBody
    public RestData checkUserName(String userName) {
        RestData restData = new RestData();
        if (userDao.checkUserName(userName)) {
            restData.setSuccess(1);
        } else {
            restData.setComment("该用户名已存在");
        }
        return restData;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLogin(String redirect) {
        ModelAndView modelAndView = new ModelAndView("/login");
        if (StringUtils.isNotEmpty(redirect)) {
            modelAndView.addObject("error", "您需要先登录!");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(String userName, String password, String captcha) {
        ModelAndView modelAndView = new ModelAndView("/login");
        String strCode = (String) request.getSession().getAttribute("strCode");
        if (!strCode.equals(captcha)) {
            modelAndView.addObject("error", "验证码错误");
            return modelAndView;
        }

        User user = userDao.findByUserName(userName);

        if (null != user && user.getPassword().equals(DigestUtils.md5Hex(password))) {
            ContextUtils.getSessionUtils(request).setUser(user);
            return new ModelAndView("redirect:/");
        } else {
            modelAndView.addObject("error", "用户名或密码错误");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String getPassword() {
        return "/password";
    }

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @LoginRequired
    public ModelAndView postPassword(String oldPassword, String newPassword, String captcha) {
        ModelAndView modelAndView = new ModelAndView("/password");
        String strCode = (String) request.getSession().getAttribute("strCode");
        if (!strCode.equals(captcha)) {
            modelAndView.addObject("error", "验证码错误");
            return modelAndView;
        }

        User user = ContextUtils.getUser(request);
        if (user.getPassword().equals(DigestUtils.md5Hex(oldPassword))) {
            user.setPassword(DigestUtils.md5Hex(newPassword));
            userDao.update(user);
            return new ModelAndView("redirect:/");
        } else {
            modelAndView.addObject("error", "原密码输入错误");
        }
        return modelAndView;

    }

    @RequestMapping("logout")
    public String logout() {
        request.getSession().invalidate();
        return "redirect:/";
    }

    @RequestMapping("403")
    public String get403() {
        return "/misc/403";
    }


    @RequestMapping("/loadImage/{id}")
    public void loadImage(@PathVariable(value = "id") String id, HttpServletResponse response){

        String shopImagePath = PropertiesUtils.getString("movie.image.path");

        // 文件路径/movie_123123.jpg
        File file = new File(shopImagePath+"/"+IMAGE_NAME+id+IMAGESUFFIX);
        if(!file.isFile() || !file.exists()){
            logger.error("不是文件或者文件不存在");
        }

        OutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            FileUtils.copyFile(file,outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 前端验证码
     *
     * @param request
     * @param response
     * @param session	存放验证
     * @throws IOException
     */
    @RequestMapping({ "authCode" })
    public void getAuthCode(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int width = 63;
        int height = 37;
        Random random = new Random();
        // 设置response头信息
        // 禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);
        // 产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();
        // Graphics类的样式
        g.setColor(this.getRandColor(200, 250));
        g.setFont(new Font("Times New Roman", 0, 28));
        g.fillRect(0, 0, width, height);
        // 绘制干扰线
        for (int i = 0; i < 40; i++) {
            g.setColor(this.getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }

        // 绘制字符
        String strCode = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            strCode = strCode + rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 28);
        }
        // 将字符保存到session中用于前端的验证
        session.setAttribute("strCode", strCode);
        g.dispose();

        ImageIO.write(image, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();

    }

    //创建颜色
    public Color getRandColor(int fc,int bc){
        Random random = new Random();
        if(fc>255)
            fc = 255;
        if(bc>255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r,g,b);
    }

}
