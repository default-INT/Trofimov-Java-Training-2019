package by.gstu.models.dao.hibernate;

import by.gstu.models.dao.CarDAO;
import by.gstu.models.entities.car.Car;
import by.gstu.models.entities.car.FuelType;
import by.gstu.models.entities.car.Transmission;
import by.gstu.models.utils.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.Collection;

/**
 * @version 1.1
 * @author Evgeniy Trofimov
 */
public class HibernateCarDAO implements CarDAO {

    private static final Logger logger = Logger.getLogger(HibernateCarDAO.class);

    @Override
    public boolean create(Car car) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(car);
            session.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public Collection<Car> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return (Collection<Car>) session.createQuery("FROM Car").list();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public Car read(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Car.class, id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Car car) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(car);
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

    public static class HibernateTransmissionDAO implements CarEntityDAO<Transmission> {

        @Override
        public Collection<Transmission> readAll() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return (Collection<Transmission>) session.createQuery("FROM Transmission ").list();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            return null;
        }

        @Override
        public Transmission read(int id) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.get(Transmission.class, id);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            return null;
        }
    }

    public static class HibernateFuelTypeDAO implements CarEntityDAO<FuelType> {

        @Override
        public Collection<FuelType> readAll() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return (Collection<FuelType>) session.createQuery("FROM FuelType ").list();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            return null;
        }

        @Override
        public FuelType read(int id) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                return session.get(FuelType.class, id);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            return null;
        }
    }
}
