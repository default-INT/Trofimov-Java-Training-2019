"use strict";

/**
 * Initialize variable and run start method.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
init = function init() {
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
    getCarsCatalog();
};

init();


/**
 * Get cars from server and generate item in catalog.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
function getCarsCatalog() {
    Car.getAllCarsAJAX()
        .then(cars => {
            if (!cars) {
                return;
            }
            let catalogAuto = document.getElementById("catalog-auto");
            catalogAuto.innerHTML = "";
            cars.forEach((car) => {
                catalogAuto.appendChild(new Car(car).getCarItemNode());
            });
    }).catch(reject => console.log(reject));
}
