package by.gstu.models.dao.hibernate;

import by.gstu.models.dao.OrderDAO;
import by.gstu.models.entities.Order;
import by.gstu.models.entities.ReturnRequest;
import by.gstu.models.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Calendar;
import java.util.Collection;

/**
 * @version 1.1
 * @author Evgeniy Trofimov
 */
public class HibernateOrderDAO implements OrderDAO {

    private static final Logger logger = Logger.getLogger(HibernateOrderDAO.class);

    @Override
    public boolean create(Order order) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(order);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public Collection<Order> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Collection<Order>) session.createQuery("FROM Order ").list();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Order read(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Order.class, id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Order order) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(order);
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

    @Override
    public Collection<Order> readAll(int clientId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createNamedQuery("getOrdersByClientId")
                    .setParameter("clientId", clientId);
            return (Collection<Order>) query.uniqueResult();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean closeOrder(int orderId, Calendar returnDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.createNamedQuery("closeOrder")
                    .setParameter("orderId", orderId)
                    .executeUpdate();
            session.save(new ReturnRequest(returnDate, orderId));
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }
}
