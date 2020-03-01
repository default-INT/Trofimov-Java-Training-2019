package by.gstu.models.utils;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @version 1.1
 * @author Evgeniy Trofimov
 */
public class HibernateUtil {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            logger.error(ex.getMessage());
            logger.trace(ex.getStackTrace(), ex);
            throw new ExceptionInInitializerError();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
