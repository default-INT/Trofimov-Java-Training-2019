"use strict";

const passportRegEx = /[A-Z]{2}[0-9]{7}/;

function passportDataChange(entry) {
    if (!passportRegEx.test(entry.value)) {
        entry.style.color = "red";
    } else {
        entry.style.color = "black";
    }
}

/**
 *
 * @param id
 * @version 2.0
 */
function loadCar(id) {
    Car.getCarAJAX(id)
        .then(car => {
            let carId = document.getElementById("carId");
            let carTitle = document.getElementById("carTitle");
            //img-block
            let img = document.querySelector("#imgBlock > img");
            let desc = document.getElementById("description");

            carId.innerText = car.id;
            carTitle.innerText = car.model;

            getInnerElement(desc, "#number").innerText = car.number;
            getInnerElement(desc, "#transmission").innerText = car.transmission;
            getInnerElement(desc, "#fuelType").innerText = car.fuelType;
            getInnerElement(desc, "#yearOfIssue").innerText = car.yearOfIssue;
            getInnerElement(desc, "#mileage").innerText = car.mileage;
            getInnerElement(desc, "#priceHour").innerText = car.priceHour;
            let status = getInnerElement(desc, "#status");
            if (car.available) {
                status.style.color = "green";
                status.innerHTML = "Автомобиль доступен для аренды";
            } else {
                status.style.color = "red";
                status.innerHTML = "Автомобиль занят";
            }
            //desc.status.innerText = car.status;

            if (userStatus === "guest" || userStatus === "admin") {
                document.getElementById("formTitle").innerText
                    = "Данный пользователь не может брать автомобиль в аренду";
                document.getElementById("orderForm").style.display = "none";
            } else if (!car.available) {
                document.getElementById("formTitle").innerText
                    = "Данный автомобиль занят";
                document.getElementById("orderForm").style.display = "none";
            }
        }).catch(reject => console.log(reject));
}

/**
 * TODO: Description
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
function createOrder() {
    let formData = new FormData(document.getElementById("orderForm"));
    let msg = document.getElementById("msg");
    let returnDate = new Date(formData.get("dateReturn") + "T"
        + formData.get("timeReturn"));
    let orderDate = new Date();
    orderDate.setMilliseconds(0);
    orderDate.setSeconds(0);

    if (userData.status === "admin") {
        msg.style.color = "red";
        msg.innerHTML = "Администраторам нельзя брать автомобили в аренду!";
        return;
    }

    let rentalPeriod = Math.round((returnDate.getTime() - orderDate.getTime())/ 3600 / 1000);
    if (isEmptyOrderForm()) {
        msg.style.color = "red";
        msg.innerHTML = "Не все данные введены!";
        return;
    }
    if (rentalPeriod < 0) {
        msg.style.color = "red";
        msg.innerHTML = "Дата возврата неккоректна. Возврат раньше заказа!";
        return;
    }
    let passportData = formData.get("passportData");
    if (!passportRegEx.test(passportData)) {
        msg.style.color = "red";
        msg.innerHTML = "Паспортные данные введены неккоректно!";
        return;
    }
    msg.style.color = "black";

    let order = new Order({
        orderDate: orderDate,
        rentalPeriod: rentalPeriod,
        returnDate: null,
        carId: parseInt(document.getElementById("carId").innerText),
        clientId: userData.id,
        passportData: formData.get("passportData"),
        price: null
    });

    Order.createOrderAJAX(order)
        .then(resp => {
            if (resp.result) {
                let formTitle = document.getElementById("formTitle");
                document.getElementById("orderForm").innerHTML = "";
                formTitle.style.color = "Green";
                formTitle.innerHTML = "Заказ успешно совершён!";
                let status = document.getElementById("status");
                status.style.color = "red";
                status.innerHTML = "Автомобиль занят";
            } else {
                msg.innerHTML = "Не удалось совершить заказ.";
            }
        });

}

function isEmptyOrderForm() {
    let formData = new FormData(document.getElementById("orderForm"));
    return formData.get("dateReturn") === ""
        && formData.get("timeReturn") === ""
        && formData.get("passportData");
}

function calculatePrice() {
    let formData = new FormData(document.getElementById("orderForm"));
    if (!!formData) {
        let msg = document.getElementById("msg");
        let returnDate = new Date(formData.get("dateReturn") + "T"
            + formData.get("timeReturn"));
        if (formData.get("timeReturn") === "" || formData.get("dateReturn") === "") {
            return;
        }
        let orderDate = new Date();
        orderDate.setMilliseconds(0);
        orderDate.setSeconds(0);

        if (userData.status === "admin") {
            msg.style.color = "red";
            msg.innerHTML = "Администраторам нельзя брать автомобили в аренду!";
            return;
        }

        let rentalPeriod = Math.round((returnDate.getTime() - orderDate.getTime())/ 3600 / 1000);
        let priceHour = parseInt(document.getElementById("priceHour").innerText);

        let price = priceHour * rentalPeriod;
        if (!isNaN(price) && price > 0) {
            let rentalPrice = document.getElementById("rentalPrice");
            rentalPrice.innerText = (priceHour * rentalPeriod).toString();
        }
    }
}