package by.gstu.models.dao;

import by.gstu.models.entities.ReturnRequest;

import java.util.Collection;

public interface ReturnRequestDAO {
    boolean create(ReturnRequest returnRequest);
    Collection<ReturnRequest> readAll();
    ReturnRequest read(int id);
    boolean update(ReturnRequest returnRequest);
    boolean delete(int id);

    Collection<ReturnRequest> readAllAvailable();
    Collection<ReturnRequest> readAllForClient(int clientId);
    boolean closeReturnRequest(int returnRequestId);
    boolean cancelReturnRequest(ReturnRequest returnRequest);
}
