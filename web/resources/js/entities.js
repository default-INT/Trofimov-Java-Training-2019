"use strict";

/**
 * Entity class form car. Include methods for generate NodeElements
 * and parse to JSON,
 *
 * @author Evgeniy Trofimov
 * @version 1.1
 */
class Car {

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

    static getCarAJAX(id) {
        let httpRequest = new XMLHttpRequest();
        let urlPath = "/service/cars/" + id;

        httpRequest.open("GET", urlPath);
        httpRequest.responseType = "json";
        httpRequest.send();

        httpRequest.onload(function () {
            if (httpRequest.status === 200) {
                let car = httpRequest.response;
                if (!car) {
                    console.log("Response from 'route/cars/" + id + "' is equal to " +
                        "null or undefined");
                    return null;
                }
                return new Car(car);
            } else {
                console.log("Error send AJAX request to '" + urlPath +
                    "'. Request status: " + httpRequest.status);
            }
        });
    }

    static getAllCarsAjax() {
        let httpRequest = new XMLHttpRequest();

        httpRequest.open("GET", "/service/cars/");
        httpRequest.responseType = "json";
        httpRequest.send();

        httpRequest.onload = () => {
            if (httpRequest.status === 200) {
                return httpRequest.response;
            } else {
                return null;
            }
        };
    }
}

/**
 * Class has describes 'Orders'. Contains a static method to get the
 * JSON object from the server and for generation HTML elements.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
class Order {

    /**
     *
     * @param order {Object}
     */
    constructor(order) {
        this.id = order.id;
        this.orderDate = order.orderDate;
        this.rentalPeriod = order.rentalPeriod;
        this.returnDate = order.returnDate;
        this.carId = order.carId;
        this.clientId = order.clientId;
        this.passportData = order.passportData;
        this.price = order.price;
    }

    /**
     * Convert order to JSON format.
     *
     * @return {string}
     */
    toJSON() {
        return JSON.stringify({
                id: this.id,
                orderDate: this.orderDate,
                rentalPeriod: this.rentalPeriod,
                carId: this.carId,
                clientId: this.clientId,
                passportData: this.passportData
        });
    }

    /**
     *
     * @param order {Object}
     * @return {string}
     */
    static toJSON(order) {
        return new Order(order).toJSON();
    }

    /**
     *
     * @param order {Order}
     */
    static createOrderAJAX(order) {
        let httpRequest = new XMLHttpRequest();
        let url = "/service/orders/";

        httpRequest.open("POST", url);
        httpRequest.responseType = "json";
        httpRequest.send(order.toJSON());

        httpRequest.onload =function () {
            if (httpRequest.status === 200) {
                let msg = httpRequest.response;
            } else {
                console.log("Error send AJAX request to '" + url +
                    "'. Request status: " + httpRequest.status);
            }
        };
    }
}

/**
 * Class has describes 'Accounts'. Contains a static method to get the
 * JSON object from the server and for generation HTML elements.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
class Account {

    _status = "guest";

    constructor(account) {
        this._id = account.id;
        this._login = account.login;
        this._email = account.email;
        this._status = account.status;
    }

    get id() {
        return this._id;
    }

    set id(value) {
        if (value < 0) throw new Error("Id cannot be negative.")
    }

    get login() {
        return this._login;
    }

    set login(value) {
        this._login = value;
    }

    get email() {
        return this._email;
    }

    set email(value) {
        this._email = value;
    }

    /**
     * Generate HTML elements and create submenu for logIn user or guest.
     *
     * @return {HTMLDivElement} User submenu.
     */
    getMenu() {
        let userMenu = divClass("menu");
        userMenu.id = "user";
        let login = divIdContent("login", this.login);
        let userSidebar = elementClassId("ul", "user-sidebar",
            "submenu");

        let sigInBtn = element("li");
        sigInBtn.addEventListener("click", () => {
            document.getElementById('login-form').style.display = "block";
        });
        sigInBtn.innerHTML = "Войти";

        let regBtn = element("li");
        regBtn.addEventListener("click", () => {
            document.getElementById('registration-form').style.display = 'block';
        });
        regBtn.innerHTML = "Зарегистрироваться";

        userSidebar.appendChild(sigInBtn);
        userSidebar.appendChild(regBtn);

        userMenu.appendChild(login);
        userMenu.appendChild(userSidebar);

        return userMenu;
    }

    /**
     * Function will check been session on server. If user logIn early, then function set in
     * userData information about user. If session empty function will setting null in userData.
     *
     * @version 2.0
     */
    static authorizationAJAX() {
        let httpRequest = new XMLHttpRequest();
        let url = "/account/auth";

        httpRequest.open("GET", url);
        httpRequest.responseType = "json";
        httpRequest.send();

        let guest = new Account({
            login: "Гость",
            status: "guest"
        });

        httpRequest.onload = function () {
            if (httpRequest.status === 200) {
                let account = httpRequest.response;
                if (!account) {
                    console.log("HttpResponse define null or undefined.");
                } else if (!account.status) {
                    console.log("Status define null or undefined.");
                } else if (account.status === "client") {
                    return new Client(account);
                } else if (account.status === "admin") {
                    return  new Administrator();
                }
                return guest;
            } else {
                console.log("Error send AJAX request to '" + url +
                    "'. Request status: " + httpRequest.status);
                return guest;
            }
        }
    }

    /**
     * Authorization in system. Return logIn account (Client, Administrator) or guest.
     *
     * @param login
     * @param password
     * @return logIn user
     */
    static logInAJAX(login, password) {
        let httpRequest = new XMLHttpRequest();
        let documentURI = new URL(document.documentURI);
        let url = new URL("/account/auth", documentURI.origin);

        url.searchParams.set("login", login);
        url.searchParams.set("password", password);

        httpRequest.open("GET", url);
        httpRequest.responseType = "json";
        httpRequest.send();

        let guest = new Account({
            login: "Гость",
            status: "guest"
        });

        httpRequest.onload = function () {
            if (httpRequest.status === 200) {
                let account = httpRequest.response;
                if (!account) {
                    console.log("HttpResponse define null or undefined.");
                } else if (!account.status) {
                    console.log("Status define null or undefined.");
                } else if (account.status === "client") {
                    return new Client(account);
                } else if (account.status === "admin") {
                    return  new Administrator();
                }
                return guest;
            } else {
                console.log("Error send AJAX request to '" + url +
                    "'. Request status: " + httpRequest.status);
                return guest;
            }
        };
    }

    static getSubMenuElement(name, fx) {
        let btn = element("li");
        btn.addEventListener("click", fx);
        btn.innerHTML = "Мой профиль";

        return btn;
    }

    static getGuestMenu() {
        let userMenu = divClass("menu");
        userMenu.id = "user";
        let login = divIdContent("login", "Гость");
        let userSidebar = elementClassId("ul", "user-sidebar",
            "submenu");

        let sigInBtn = element("li");
        sigInBtn.addEventListener("click", () => {
            document.getElementById('login-form').style.display = "block";
        });
        sigInBtn.innerHTML = "Войти";

        let regBtn = element("li");
        regBtn.addEventListener("click", () => {
            document.getElementById('registration-form').style.display = 'block';
        });
        regBtn.innerHTML = "Зарегистрироваться";

        userSidebar.appendChild(sigInBtn);
        userSidebar.appendChild(regBtn);

        userMenu.appendChild(login);
        userMenu.appendChild(userSidebar);

        return userMenu;
    }
}

/**
 * Class has describes 'Clients', extends 'Account'. Contains a static method to get the
 * JSON object from the server and for generation HTML elements.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
class Client extends Account {
    _birthdayYear;

    constructor(account) {
        super(account);
        this._status = "client";
        this._birthdayYear = account.birthdayYear;
    }

    getMenu() {
        let userMenu = divClass("menu");
        userMenu.id = "user";
        let login = divIdContent("login", this.login);
        let userSidebar = elementClassId("ul", "user-sidebar",
            "submenu");

        let profileBtn = Account.getSubMenuElement("Мой профиль", undefined);
        let myOrdersBtn = Account.getSubMenuElement("Мои аренды", undefined);
        let exitBtn = Account.getSubMenuElement("Выход", undefined);

        userSidebar.appendChild(profileBtn);
        userSidebar.appendChild(myOrdersBtn);
        userSidebar.appendChild(exitBtn);

        userMenu.appendChild(login);
        userMenu.appendChild(userSidebar);

        return userMenu;
    }

    get birthdayYear() {
        return this._birthdayYear;
    }

    set birthdayYear(value) {
        if (value < 18) throw new Error("User is under 18 years old.");
        this._birthdayYear = value;
    }
}

/**
 * Class has describes 'Administrators', extends 'Account'. Contains a static method to get the
 * JSON object from the server and for generation HTML elements.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
class Administrator extends Account {
    constructor(account) {
        super(account);
        this._status = "admin";
    }

    getMenu() {
        let userMenu = divClass("menu");
        userMenu.id = "user";
        let login = divIdContent("login", this.login);
        let userSidebar = elementClassId("ul", "user-sidebar",
            "submenu");

        let profileBtn = Account.getSubMenuElement("Мой профиль", undefined);
        let returnRequestBtn = Account.getSubMenuElement("Заявки на возврат", undefined);
        let exitBtn = Account.getSubMenuElement("Выход", undefined);

        userSidebar.appendChild(profileBtn);
        userSidebar.appendChild(returnRequestBtn);
        userSidebar.appendChild(exitBtn);

        userMenu.appendChild(login);
        userMenu.appendChild(userSidebar);

        return userMenu;
    }
}