"use strict";

/**
 * Entity class form car. Include methods for generate NodeElements
 * and parse to JSON,
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
class Car {
    id;
    model;
    number;
    yearOfIssue;
    mileage;
    transmission;
    fuelType;
    priceHour;
    imgUrl;

    constructor(car) {
        this.id = car.id;
        this.model = car.model;
        this.number = car.number;
        this.yearOfIssue = car.yearOfIssue;
        this.mileage = car.mileage;
        this.transmission = car.transmission;
        this.fuelType = car.fuelType;
        this.priceHour = car.priceHour;
        this.imgUrl = "resources/img/ford-mustang.png";
    }

    getCarItemNode() {
        let carItem = divClass("car-item");

        let span = element("span");
        span.id = this.id;

        let title = divClass("title");
        title.innerHTML = this.model;

        let icon = divClass("icon");

        let img = element("img");
        img.src = this.imgUrl;
        icon.appendChild(img);

        let itemContent = divClass("item-content");
        itemContent.appendChild(column(
            "КП: " + this.transmission,
            "Двигатель: " + this.fuelType,
            "Цена в ч.: " + this.priceHour + " $"
        ));
        itemContent.appendChild(column(
            "Номер: " + this.number,
            "Год выпуска: " + this.yearOfIssue,
            "Пробег: " + this.mileage + " км."
        ));

        carItem.appendChild(span);
        carItem.appendChild(title);
        carItem.appendChild(icon);
        carItem.appendChild(itemContent);

        return carItem;
    }

    toJSON() {
        return JSON.stringify({
            id: this.id,
            model: this.model,
            number: this.number,
            yearOfIssue: this.yearOfIssue,
            mileage: this.mileage,
            transmission: this.transmission,
            fuelType: this.fuelType,
            priceHour: this.priceHour
        });
    }

    static toJSON(car) {
        return car.toJSON();
    }

    static getCarItemNode(car) {
        return new Car(car).getCarItemNode();
    }
}