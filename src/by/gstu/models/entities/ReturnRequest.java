package by.gstu.models.entities;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.untils.ParserJSON;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Entity class. TODO: Add description
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
public class ReturnRequest extends Entity {

    private Calendar returnDate;
    private int orderId;
    private String description;
    private boolean returnMark;
    private double repairCost;

    private Order order;

    public ReturnRequest(int id, Calendar returnDate, int orderId, String description, boolean returnMark,
                         double repairCost) {
        super(id);
        this.returnDate = returnDate;
        this.orderId = orderId;
        this.description = description;
        this.returnMark = returnMark;
        this.repairCost = repairCost;
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isReturnMark() {
        return returnMark;
    }
    public void setReturnMark(boolean returnMark) {
        this.returnMark = returnMark;
    }
    public double getRepairCost() {
        return repairCost;
    }
    public void setRepairCost(double repairCost) {
        this.repairCost = repairCost;
    }
    public Calendar getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(Calendar returnDate) {
        this.returnDate = returnDate;
    }
    public Order getOrder() {
        if (order == null) {
            DAOFactory dao = DAOFactory.getDAOFactory();
            order = dao.getOrderDAO().read(orderId);
        }
        return order;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject returnReqJson = super.toJSON();

        returnReqJson.put("returnDate", ParserJSON.parseCalendar(returnDate));
        returnReqJson.put("orderId", orderId);
        returnReqJson.put("order", getOrder().toJSON());
        returnReqJson.put("description", description);
        returnReqJson.put("returnMark", returnMark);
        returnReqJson.put("repairCost", repairCost);

        return returnReqJson;
    }
}
