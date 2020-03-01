package by.gstu.models.dao.hibernate;

import by.gstu.models.dao.ReturnRequestDAO;
import by.gstu.models.entities.ReturnRequest;
import by.gstu.models.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.Collection;

/**
 * @version 1.1
 * @author Evgeniy Trofimov
 */
public class HibernateReturnRequestDAO implements ReturnRequestDAO {

    private static final Logger logger = Logger.getLogger(HibernateReturnRequestDAO.class);

    @Override
    public boolean create(ReturnRequest returnRequest) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(returnRequest);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public Collection<ReturnRequest> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Collection<ReturnRequest>) session.createQuery("FROM ReturnRequest ").list();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public ReturnRequest read(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ReturnRequest.class, id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(ReturnRequest returnRequest) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(returnRequest);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(read(id));
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }
}
