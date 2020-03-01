package by.gstu.models.entities.car;

import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@javax.persistence.Entity
@Table(name = "fuel_types")
public class FuelType extends CarEntity {

    @OneToMany(mappedBy = "fuelType")
    private List<Car> cars;

    public FuelType(int id, String name) {
        super(id, name);
    }

    public FuelType(String name) {
        super(name);
    }

    public FuelType() {
    }

    public List<Car> getCars() {
        return cars;
    }
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}