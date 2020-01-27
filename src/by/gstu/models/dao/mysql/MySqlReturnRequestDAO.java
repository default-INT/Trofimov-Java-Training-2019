package by.gstu.models.dao.mysql;

import by.gstu.models.dao.ReturnRequestDAO;
import by.gstu.models.entities.ReturnRequest;

import java.util.Collection;

/**
 * Implements ReturnRequestDAO.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
public class MySqlReturnRequestDAO implements ReturnRequestDAO {
    @Override
    public boolean create(ReturnRequest returnRequest) {
        return false;
    }

    @Override
    public Collection<ReturnRequest> readAll() {
        return null;
    }

    @Override
    public ReturnRequest read(int id) {
        return null;
    }

    @Override
    public boolean update(ReturnRequest returnRequest) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
