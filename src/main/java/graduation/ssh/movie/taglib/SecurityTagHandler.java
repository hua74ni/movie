package graduation.ssh.movie.taglib;

import graduation.ssh.movie.entity.User;
import graduation.ssh.movie.utils.SessionUtils;
import lombok.Setter;
import graduation.ssh.movie.utils.ContextUtils;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * 作为辅助JSP控制输出的Tag.
 * <p/>
 * 根据 LoginRequired 和 是否为本人和管理员 来进行限制
 * <p/>
 * Created by huangdonghua on 2017/12/28.
 */

public class SecurityTagHandler extends SimpleTagSupport {


    @Setter
    private String userId;

    @Override
    public void doTag() throws JspException, IOException {
        SessionUtils sessionUtils = getSessionUtils();
        if (null == sessionUtils) {
            return;
        }
        User currentUser = sessionUtils.getUser();
        if (null == currentUser) {
            return;
        }
        if (currentUser.isAdmin() || currentUser.getId().equals(userId)) {
            getJspBody().invoke(null);
        }
        return;
    }


    /**
     * 获取当前的SessionUtils
     *
     * @return
     */
    private SessionUtils getSessionUtils() {
        JspContext jc = getJspContext();
        Object sessionUtils = jc.getAttribute(ContextUtils.SESSIONUTILS, PageContext.SESSION_SCOPE);
        if (sessionUtils != null && sessionUtils instanceof SessionUtils) {
            return (SessionUtils) sessionUtils;
        }
        return null;
    }
}
