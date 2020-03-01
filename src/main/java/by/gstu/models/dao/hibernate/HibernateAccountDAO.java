package by.gstu.models.dao.hibernate;

import by.gstu.models.dao.AccountDAO;
import by.gstu.models.entities.Account;
import by.gstu.models.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Collection;

/**
 * @author Evgeni Trofimov
 * @version 1.1
 */
class HibernateAccountDAO implements AccountDAO {

    private static final Logger logger = Logger.getLogger(HibernateAccountDAO.class);

    @Override
    public boolean create(Account account) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(account);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public Collection<Account> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Collection<Account>) session.createQuery("FROM Account ").list();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Account read(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Account.class, id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Account account) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(account);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Account account) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(account);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        return delete(read(id));
    }

    @Override
    public Account logIn(String login, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Query query = session.getNamedQuery("logIn")
                    .setParameter("login", login)
                    .setParameter("password", password);
            return (Account) query.uniqueResult();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean availableLogin(String login) {
        return false;
    }
}
