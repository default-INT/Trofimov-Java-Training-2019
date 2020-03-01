package by.gstu.models.dao.hibernate;

import by.gstu.models.dao.AdministratorDAO;
import by.gstu.models.entities.Administrator;
import by.gstu.models.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * @version 1.0
 * @author Evgeniy Trofimov
 */
public class HibernateAdministratorDAO implements AdministratorDAO {

    private static final Logger logger = Logger.getLogger(HibernateAdministratorDAO.class);

    @Override
    public boolean create(Administrator administrator) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(administrator);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }
}
