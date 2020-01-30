"use strict";

init();

/**
 * Initialize variable and run start method.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
function init() {
    if (document.getElementById("filter-title") !== undefined) {
        document.getElementById("filter-title").addEventListener("click", () => {
            let form = document.getElementById("form-filter");
            if (form.style.display != "none") {
                form.style.display = "none";
            } else {
                form.style.display = "block";
            }
        });
    }

    getCarsAjax();
}

/**
 * Sends AJAX request on ServiceServlet by url "/service/cars" and get all cars from database.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
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
                catalogAuto.appendChild(new Car(car).getCarItemNode());
            });
        }
    };
}



