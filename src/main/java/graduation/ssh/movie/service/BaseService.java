package graduation.ssh.movie.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by huangdonghua on 2017/12/28.
 */
public abstract class BaseService {

    protected Logger logger = null;

    public BaseService() {
        logger = LoggerFactory.getLogger(getClass().getName());
    }
}
