package by.gstu.controllers.services;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.dao.ReturnRequestDAO;
import by.gstu.models.entities.ReturnRequest;
import by.gstu.models.utils.ParserJSON;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class ReturnRequestService {

    private static final Logger logger = Logger.getLogger(ReturnRequestService.class);

    private static ReturnRequestService instance;
    private ReturnRequestDAO returnRequestDAO;

    private ReturnRequestService() {
        returnRequestDAO = DAOFactory.getDAOFactory().getReturnRequestDAO();
    }

    public static ReturnRequestService getInstance() {
        if (instance == null) {
            instance = new ReturnRequestService();
        }
        return instance;
    }

    public JSONArray getAllReturnRequests(int clientId) {
        return ParserJSON.toJSONArray(new ArrayList<>(returnRequestDAO.readAllForClient(clientId)));
    }

    public JSONArray getAllReturnRequests() {
        return ParserJSON.toJSONArray(new ArrayList<>(returnRequestDAO.readAllAvailable()));
    }

    public JSONObject acceptRequest(int requestId) {
        JSONObject msg = new JSONObject();
        if (returnRequestDAO.closeReturnRequest(requestId)) {
            msg.put("result", true);
        } else {
            msg.put("result", false);
        }
        return msg;
    }

    public JSONObject cancelRequest(ReturnRequest returnRequest) {
        JSONObject msg = new JSONObject();
        if (returnRequestDAO.cancelReturnRequest(returnRequest)) {
            msg.put("result", true);
        } else {
            msg.put("result", false);
        }
        return msg;
    }

    public static ReturnRequest parseRequestDescription(HttpServletRequest request) {
        StringBuilder buffer = new StringBuilder();
        String line;
        BufferedReader reader;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject jsonObject =  new JSONObject(buffer.toString());
            return new ReturnRequest(jsonObject.getInt("id"), jsonObject.getString("description"),
                    jsonObject.getDouble("repairCost"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }
}
