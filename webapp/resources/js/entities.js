"use strict";

/**
 * Entity class form car. Include methods for generate NodeElements
 * and parse to JSON,
 *
 * @author Evgeniy Trofimov
 * @version 1.2
 */
class Car {

    constructor(car) {
        if (!!car.id) this.id = car.id;
        if (!!car.model)this.model = car.model;
        if (!!car.number) this.number = car.number;
        if (!!car.yearOfIssue) this.yearOfIssue = car.yearOfIssue;
        if (!!car.mileage) this.mileage = car.mileage;
        if (!!car.transmission) this.transmission = car.transmission;
        if (!!car.fuelType) this.fuelType = car.fuelType;
        if (!!car.priceHour) this.priceHour = car.priceHour;
        this.imgUrl = "resources/img/ford-mustang.png";
        if (!!car.available) this.available = car.available;
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
            "Цена в ч.: " + this.priceHour + " $",
            "Статус: " + (this.available ? "Свободен" : "Занят")
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
        if (!!order.id) this._id = order.id;
        if (!!order.orderDate) this._orderDate = new Date(order.orderDate);
        if (!!order.rentalPeriod) this._rentalPeriod = order.rentalPeriod;
        if (!!order.returnDate) this._returnDate = new Date(order.returnDate);
        if (!!order.carId) this._carId = order.carId;
        if (!!order.car) this._car = new Car(order.car);
        if (!!order.clientId) this._clientId = order.clientId;
        if (!!order.client) this._client = new Client(order.client);
        if (!!order.passportData) this._passportData = order.passportData;
        if (!!order.price) this._price = order.price;
        if (!!order.carName) this._carName = order.carName;
        if (!!order.carImgUrl) this._carImgUrl = order.carImgUrl;
    }

    get id() {
        return this._id;
    }
    get orderDate() {
        return this._orderDate;
    }
    set orderDate(value) {
        this._orderDate = value;
    }
    get rentalPeriod() {
        return this._rentalPeriod;
    }
    set rentalPeriod(value) {
        this._rentalPeriod = value;
    }
    get returnDate() {
        return this._returnDate;
    }
    get carId() {
        return this._carId;
    }
    set carId(value) {
        this._carId = value;
    }
    get clientId() {
        return this._clientId;
    }
    set clientId(value) {
        this._clientId = value;
    }
    get passportData() {
        return this._passportData;
    }
    set passportData(value) {
        this._passportData = value;
    }
    get price() {
        return this._price;
    }
    get carName() {
        return this._carName;
    }
    get carImgUrl() {
        return this._carImgUrl;
    }
    get client() {
        return this._client;
    }
    get car() {
        return this._car;
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
     * @return {HTMLElement}
     */
    getItemNode() {
        let icon  = node({
            classList: "icon",
            childNodes: node({
                tag: "img",
                src: "/resources/img/ford-mustang.png", //car image
                alt: ""
            })
        });

        let title = node({
            classList: "title",
            content: this.carName
        });
        let status = this._getStatus();
        let description = this._getDescription();

        let contentItem = node({
            classList: "content-item",
            childNodes: [title, status, description]
        });

        return node({
            classList: "item",
            id: "order" + this.id,
            childNodes: [icon, contentItem]
        });
    }

    _getDescription() {
        let orderDate = node({
            tag: "label",
            content: "Дата аренды: ",
            childNodes: node({
                classList: "order-date",
                content: dateFormat(this.orderDate)
            })
        });
        let returnDate = node({
            tag: "label",
            content: "Дата возврата: ",
            childNodes: node({
                classList: "return-date",
                content: dateFormat(this.returnDate)
            })
        });
        let returnBtn = node({
            tag: "button",
            classList: "return-date",
            content: "Вернуть",
            onclick: () => {
                let date = new Date();
                date.setMilliseconds(0);
                date.setSeconds(0);
                closeOrder(this.id, date);
            }
        });
        return node({
            classList: "description",
            childNodes: [orderDate, returnDate, returnBtn]
        });
    }

    _getStatus() {
        let nowDate = new Date();
        nowDate.setMinutes(0);
        nowDate.setSeconds(0);
        nowDate.setMilliseconds(0);
        let hourLeft = Math.round(
            (this.returnDate.getTime() - nowDate.getTime()) / 3600 / 1000);
        if (hourLeft < 0) {
            return  node({
               classList: "status",
               content: "Вы просрочили аренду на " + (-hourLeft) + " ч."
            });
        }
        return node({
           classList: "status",
           content: "Осталось " + hourLeft + " ч. до конца аренды"
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
     * @return {HTMLElement}
     */
    static getItemNode(order) {
        return order.getItemNode();
    }

    /**
     *
     * @param order {Order}
     * @return Promise<Object>
     */
    static createOrderAJAX(order) {
        return httpPostJson("/service/orders/", order.toJSON());
    }

    /**
     *
     * @return {Promise<Array<Object>>}
     */
    static getProfileOrdersAJAX() {
        return httpRequest({
            url: "/service/orders",
            method: "GET",
            responseType: "json"
        }).then(response => {
            if (!response) return null;
            return response;
        });
    }

    /**
     *
     * @return {Promise<Boolean>}
     */
    static closeOrderAJAX(orderId, returnDate) {
        return httpRequest({
            url: "/service/orders",
            method: "PUT",
            responseType: "json",
            data: JSON.stringify({
                orderId: orderId,
                returnDate: returnDate
            })
        }).then(response => {
            if (!response) return false;
            return response.result;
        });
    }
}

/**
 * @version 1.0
 * @author Evgeniy Trofimov
 */
class ReturnRequest {
    /**
     *
     * @param returnRequest {Object}
     */
    constructor(returnRequest) {
        if (!!returnRequest.id) this._id = returnRequest.id;
        if (!!returnRequest.returnDate) this._returnDate = new Date(returnRequest.returnDate);
        if (!!returnRequest.orderId) this._orderId = returnRequest.orderId;
        if (!!returnRequest.order) this._order = new Order(returnRequest.order);
        if (!!returnRequest.description) this._description = returnRequest.description;
        if (!!returnRequest.returnMark) this._returnMark = returnRequest.returnMark;
        if (!!returnRequest.repairCost) this._repairCost = returnRequest.repairCost;
    }

    get id() {
        return this._id;
    }
    set id(value) {
        this._id = value;
    }
    get orderId() {
        return this._orderId;
    }
    set orderId(value) {
        this._orderId = value;
    }
    get order() {
        return this._order;
    }
    set order(value) {
        this._order = value;
    }
    get description() {
        return this._description;
    }
    set description(value) {
        this._description = value;
    }
    get returnMark() {
        return this._returnMark;
    }
    set returnMark(value) {
        this._returnMark = value;
    }
    get repairCost() {
        return this._repairCost;
    }
    set repairCost(value) {
        this._repairCost = value;
    }
    get returnDate() {
        return this._returnDate;
    }
    set returnDate(value) {
        this._returnDate = value;
    }

    /**
     *
     * @return {HTMLElement}
     */
    getItemNode(statusUser) {
        let icon  = node({
            classList: "icon",
            childNodes: node({
                tag: "img",
                src: "/resources/img/ford-mustang.png", //car image
                alt: ""
            })
        });

        let title = node({
            classList: "title",
            content: this.order.carName
        });
        let status = this._getStatus();
        let description = this._getDescription();
        let carInfo = this._getCarInfo();
        let returnForm = this._getReturnForm(statusUser);

        let contentItem = node({
            classList: "content-item",
            childNodes: [title, status, description]
        });

        return node({
            classList: ["item", "return-request"],
            childNodes: [icon, contentItem, carInfo, returnForm]
        });
    }

    _getDescription() {
        let orderDate = node({
            tag: "label",
            content: "Дата аренды: ",
            childNodes: node({
                classList: "order-date",
                content: dateFormat(this.order.orderDate)
            })
        });
        let returnDate = node({
            tag: "label",
            content: "Дата возврата: ",
            childNodes: node({
                classList: "return-date",
                content: dateFormat(this.returnDate)
            })
        });
        let client = node({
            tag: "label",
            content: "ФИО клиента: ",
            childNodes: node({
                classList: "return-date",
                content: this.order.client.fullName
            })
        });
        return node({
            classList: "description",
            childNodes: [orderDate, returnDate, client]
        });
    }

    _getStatus() {

        let hourLeft = Math.round(
            (this.order.returnDate.getTime() - this.returnDate.getTime()) / 3600 / 1000);
        if (hourLeft < 0) {
            return  node({
                classList: "status",
                content: "Клиент просрочили аренду на " + (-hourLeft) + " ч."
            });
        }
        return node({
            classList: "status",
            content: "Автомобиль возвращён раньше на " + hourLeft + " ч."
        });
    }

    _getCarInfo() {
        let title = node({
            classList: "title",
            content: "Информация об автомобиле"
        });
        let number = node({
           tag: "label",
           content: "Номер: " + this.order.car.number
        });
        let motor = node({
            tag: "label",
            content: "Двигатель: " + this.order.car.fuelType
        });
        let transmission = node({
            tag: "label",
            content: "КП: " + this.order.car.transmission
        });
        return node({
            classList: "car-info",
            childNodes: [title, number, motor, transmission]
        });
    }

    _getReturnForm(statusUser) {
        let title = node({
            classList: "title",
            content: "Оценка состояния"
        });
        //description
        let form;
        if (statusUser === "admin") {
            form = this._adminForm();
        } else if (statusUser === "client") {
            form = this._clientForm();
        }
        return (node({
            classList: "return-form",
            childNodes: [title, form]
        }));

    }

    /**
     * Return form for client with description request.
     *
     * @return {HTMLElement}
     * @private
     */
    _clientForm() {
        let label = node({
            tag: "label",
            content: "Описание неисправновстей:"
        });
        let br = node({tag: "br"});
        let desc = node({
            classList: "field",
            content: !!this.description ?
                this.description : "Описание отсутствует"
        });
        let repairCost = node({
            classList: "field",
            content: !!this.repairCost ?
                "Штраф: " + this.repairCost + "$" : "Заявка ещё не проверена администратором."
        });
        if (!!this.repairCost) {
            let payFineBtn = node({
                tag: "button",
                content: "Оплатить штраф",
                onclick: payFine
            });
            return node({
                classList: "description",
                childNodes: [label, br, desc, repairCost, payFineBtn]
            });
        }
        //end description
        return node({
            classList: "description",
            childNodes: [label, br, desc, repairCost]
        });
    }

    /**
     * Return form for administrator with entry field.
     *
     * @return {HTMLElement}
     * @private
     */
    _adminForm() {
        let label = node({
            tag: "label",
            content: "Описание неисправновстей:"
        });
        let br = node({tag: "br"});
        let desc = node({
            tag: "textarea",
            name: "description",
            cols: 40,
            rows: 4,
            placeholder: "Неисправности"
        });
        let repairCost = node({
            tag: "input",
            type: "number",
            name: "repairCost",
            placeholder: "Штраф в $"
        });
        let cancelBtn = node({
            tag: "button",
            content: "Отклонить заявку",
            background: "#ff4a27",
            onclick: cancelRequest
        });
        let acceptBtn = node({
            tag: "button",
            content: "Принять возврат",
            background: "darkgreen",
            onclick: acceptRequest
        });
        //end description
        return node({
            classList: "description",
            childNodes: [label, br, desc, repairCost, cancelBtn, acceptBtn]
        });
    }

    /**
     *
     * @return {Promise<Array<Object>>}
     */
    static getReturnRequestAJAX() {
        return httpRequest({
            url: "/service/returnRequests",
            method: "GET",
            responseType: "json"
        }).then(response => {
            if (!response) return null;
            return response;
        });
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

    constructor(account) {
        this._status = "guest";
        if (!!account.id) this._id = account.id;
        if (!!account.login) this._login = account.login;
        if (!!account.email) this._email = account.email;
        if (!!account.fullName) this._fullName = account.fullName;
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
    get statusRus() {
        if (this._status === "client") return "Клиент";
        else if (this._status === "admin") return "Администратор";
        else return "Гость";
    }
    get fullName() {
        return this._fullName;
    }
    set fullName(value) {
        this._fullName = value;
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

        let profileBtn = Account.getSubMenuElement("Мой профиль",
            () => ContentManager.loadPageToUrl("/profile"));
        let exitBtn = Account.getSubMenuElement("Выход", logOut);

        userSidebar.appendChild(profileBtn);
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

        let profileBtn = Account.getSubMenuElement("Мой профиль",
            () => ContentManager.loadPageToUrl("/profile"));
        let exitBtn = Account.getSubMenuElement("Выход", logOut);


        userSidebar.appendChild(profileBtn);
        userSidebar.appendChild(exitBtn);
        exitBtn.addEventListener("click", logOut);

        userMenu.appendChild(login);
        userMenu.appendChild(userSidebar);
        userMenu.addEventListener("click", openMenu);

        return userMenu;
    }
}