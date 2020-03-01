package by.gstu.models.entities.car;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @version 2.0
 * @author Evgeniy Trofimov
 */
@Entity
@Table(name = "transmissions")
public class Transmission extends CarEntity {

    @OneToMany(mappedBy = "transmission")
    private List<Car> cars;

    public Transmission(int id, String name) {
        super(id, name);
    }

    public Transmission(String name) {
        super(name);
    }

    public Transmission() {
    }

    public List<Car> getCars() {
        return cars;
    }
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
