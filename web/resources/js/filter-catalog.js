"use strict";

if (document.getElementById("filter-title") !== undefined) {
    document.getElementById("filter-title").addEventListener("click", () => {
        let form = document.getElementById("form-filter");
        if (form.style.display !== "none") {
            form.style.display = "none";
        } else {
            form.style.display = "block";
        }
    });    
}


let car = {
    id: 0,
    model: "Ford Mustang",
    number: "2456 AE-3",
    transmission: "механика",
    fuelType: "дизель",
    priceHour: 5,
    yearOfIssue: 2009,
    mileage: 243543
};

function getCarItem(car) {
    let carItem = divClass("car-item");

    let span = element("span");
    span.id = car.id;

    let title = divClass("title");
    title.innerHTML = car.model;

    let icon = divClass("icon");

    let img = element("img");
    img.src = "resources/img/ford-mustang.png";
    icon.appendChild(img);

    let itemContent = divClass("item-content");
    itemContent.appendChild(column(
        "КП: " + car.transmission,
        "Двигатель: " + car.fuelType,
        "Цена в ч.: " + car.priceHour
    ));
    itemContent.appendChild(column(
        "Номер: " + car.number,
        "Год выпуска: " + car.yearOfIssue,
        "Пробег: " + car.mileage
    ));

    carItem.appendChild(span);
    carItem.appendChild(title);
    carItem.appendChild(icon);
    carItem.appendChild(itemContent);

    return carItem;
}

//let catalogAuto = document.getElementById("catalog-auto");
//catalogAuto.innerHTML = "";
//catalogAuto.appendChild(getCarItem(car));

getCarsAjax();

function getCarsAjax() {
    let httpRequest = new XMLHttpRequest();

    httpRequest.open("GET", "/service/cars");
    httpRequest.responseType = "json";
    httpRequest.send();

    httpRequest.onload = () => {
        if (httpRequest.status === 200) {
            let cars = httpRequest.response;
            if (cars === undefined || cars == null) {
                return;
            }

            let catalogAuto = document.getElementById("catalog-auto");
            catalogAuto.innerHTML = "";

            cars.forEach((car, index, cars) => {
                catalogAuto.appendChild(getCarItem(car));
            });
        }
    };
}



