package by.gstu.models.dao.hibernate;

import by.gstu.models.dao.ClientDAO;
import by.gstu.models.entities.Client;
import by.gstu.models.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.Collection;

/**
 * @version 1.1
 * @author Evgeniy Trofimov
 */
public class HibernateClientDAO implements ClientDAO {

    private static final Logger logger = Logger.getLogger(HibernateClientDAO.class);

    @Override
    public boolean create(Client client) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(client);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public Collection<Client> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Collection<Client>) session.createQuery("FROM Client ").list();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Client client) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }
}
