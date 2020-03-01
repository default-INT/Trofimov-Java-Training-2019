package by.gstu.models.dao;

import by.gstu.models.entities.car.Car;
import by.gstu.models.entities.car.CarEntity;

import java.util.Collection;

/**
 * DAO interface for table cars with CRUD operation.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public interface CarDAO {
    boolean create(Car car);
    Collection<Car> readAll();
    Car read(int id);
    boolean update(Car car);
    boolean delete(int id);

    /**
     * DAO interface for table transmissions and fuel_types with reading operation.
     *
     * @author Evgeniy Trofimov
     * @version 1.0
     */
    interface CarEntityDAO<T extends CarEntity> {
        Collection<T> readAll();
        T read(int id);
    }
}
