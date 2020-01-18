package by.gstu.models.entities;

import org.json.JSONObject;

/**
 * Entity class. TODO: Add description
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
public class ReturnRequest extends Entity {

    private int orderId;
    private String description;
    private boolean returnMark;
    private double repairCost;

    public ReturnRequest(int id, int orderId, String description, boolean returnMark, double repairCost) {
        super(id);
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

    @Override
    public JSONObject toJSON() {
        return null;
    }
}
