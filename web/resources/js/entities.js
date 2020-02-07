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
        this.available = car.available;
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

        carItem.addEventListener("click", function () {
            ContentManager.loadPageToUrl("/cars/" + this.querySelector("span").id);
        });

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
        //reject(new Error("Response to '" + url + "' undefined."));
        return httpGetJson("/service/cars/" + id)
            .then(response => {
                if (!response) throw new Error("Response to '/service/cars/" + id + "' undefined.");
                return new Car(response)
            });
    }

    /**
     * Sends AJAX request on ServiceServlet by url "/service/cars" and get all cars from database.
     *
     * @return {Promise<Array<Object>>}
     */
    static getAllCarsAJAX() {
        return httpGetJson("/service/cars/")
            .then(response => {
                if (!response) throw new Error("Response to '/service/cars/' undefined.");
                return response;
            });
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
        if (!!order.id) this.id = order.id;
        if (!!order.orderDate) this.orderDate = order.orderDate;
        if (!!order.rentalPeriod) this.rentalPeriod = order.rentalPeriod;
        if (!!order.returnDate) this.returnDate = order.returnDate;
        if (!!order.carId) this.carId = order.carId;
        if (!!order.clientId) this.clientId = order.clientId;
        if (!!order.passportData) this.passportData = order.passportData;
        if (!!order.price) this.price = order.price;
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
     * @return Promise<Object>
     */
    static createOrderAJAX(order) {
        return httpPostJson("/service/orders/", order.toJSON());
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
        if (!!account.id) this._id = account.id;
        if (!!account.login) this._login = account.login;
        if (!!account.email) this._email = account.email;
        //if (!!account.status) this._status = account.status;
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

    get status() {
        return this._status;
    }

    set status(value) {
        this._status = value;
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
        userMenu.addEventListener("click", openMenu);

        return userMenu;
    }

    /**
     * Function will check been session on server. If user logIn early, then function set in
     * userData information about user. If session empty function will setting null in userData.
     *
     * @version 2.2
     * @return Promise<Account>
     */
    static authorizationAJAX() {
        //let httpRequest = new XMLHttpRequest();
        let url = "/account/auth";

        return httpGetJson(url)
            .then(account => {
                let user;
                if (!account) {
                    console.log("HttpResponse define null or undefined.");
                    return ;
                } else if (!account.status) {
                    throw new Error("Status define null or undefined.");
                } else if (account.status === "client") {
                    user = new Client(account);
                } else if (account.status === "admin") {
                    user = new Administrator(account);
                }
                //setUserMenu(user);
                userStatus = user.status;
                return user;
            })
            .catch(reject => {
                console.log(reject);
                return null;
            });
    }

    /**
     * Authorization in system. Return logIn account (Client, Administrator) or guest.
     *
     * @param login
     * @param password
     * @return logIn user
     */
    static logInAJAX(login, password) {
        let documentURI = new URL(document.documentURI);
        let url = new URL("/account/auth", documentURI.origin);

        url.searchParams.set("login", login);
        url.searchParams.set("password", password);

        return httpGetJson(url)
            .then(account => {
                let user;
                if (!account) {
                    console.log("HttpResponse define null or undefined.");
                } else if (!account.status) {
                    throw new Error("Status define null or undefined.");
                } else if (account.status === "client") {
                    user = new Client(account);
                } else if (account.status === "admin") {
                    user =  new Administrator(account);
                }
                userStatus = user.status;
                return user;
            }).catch(reject => {
                console.log(reject);
                return null;
            });
    }

    /**
     *
     * @param {JSON} client
     * @return Promise<Account>
     */
    static signUpAJAX(client) {
        let documentURI = new URL(document.documentURI);
        let url = new URL("/account", documentURI.origin);

        return httpPostJson(url, client)
            .then(account => {
                let user;
                if (!account) {
                    console.log("HttpResponse define null or undefined.");
                } else if (!account.status) {
                    throw new Error("Status define null or undefined.");
                } else if (account.status === "client") {
                    user = new Client(account);
                } else if (account.status === "admin") {
                    user =  new Administrator(account);
                }
                userStatus = user.status;
                return user;
            }).catch(reject => {
                console.log(reject);
                return null;
            });
    }

    static getSubMenuElement(name, fx) {
        let btn = element("li");
        btn.addEventListener("click", fx);
        btn.innerHTML = name;

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
        userMenu.addEventListener("click", openMenu);

        return userMenu;
    }
}

function logOut() {
    let httpRequest = new XMLHttpRequest();
    let url = "/account/exit";

    httpRequest.open("PUT", url);
    httpRequest.responseType = "json";
    httpRequest.send();

    httpRequest.onload = function () {
        if (httpRequest.status === 200) {
            console.log("LogOut from account successful.")
        } else {
            console.log("Error send AJAX request for log out to '" + url +
                "'. Request status: " + httpRequest.status);
        }
    };

    userStatus = "guest";
    setUserMenu(new Account({
        login: "Гость",
        status: "guest"
    }));
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
        if (!!account.birthdayYear) this._birthdayYear = account.birthdayYear;
    }

    getMenu() {
        let userMenu = divClass("menu");
        userMenu.id = "user";
        let login = divIdContent("login", this.login);
        let userSidebar = elementClassId("ul", "user-sidebar",
            "submenu");

        let profileBtn = Account.getSubMenuElement("Мой профиль", undefined);
        let myOrdersBtn = Account.getSubMenuElement("Мои аренды", undefined);
        let exitBtn = Account.getSubMenuElement("Выход", logOut);

        userSidebar.appendChild(profileBtn);
        userSidebar.appendChild(myOrdersBtn);
        userSidebar.appendChild(exitBtn);

        userMenu.appendChild(login);
        userMenu.appendChild(userSidebar);
        userMenu.addEventListener("click", openMenu);

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
        let exitBtn = Account.getSubMenuElement("Выход", logOut);


        userSidebar.appendChild(profileBtn);
        userSidebar.appendChild(returnRequestBtn);
        userSidebar.appendChild(exitBtn);
        exitBtn.addEventListener("click", logOut);

        userMenu.appendChild(login);
        userMenu.appendChild(userSidebar);
        userMenu.addEventListener("click", openMenu);

        return userMenu;
    }
}