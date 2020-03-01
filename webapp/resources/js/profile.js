"use strict";

loadProfile();

function loadProfile() {
    httpRequest({
        url: "/account/auth",
        method: "GET",
        responseType: "json"
    }).then(response => {
        let user;
        if (!response) {
            ContentManager.loadPageToUrl("/main");
            return;
        }
        if (response.status === "client") {
            user = new Client(response);
            loadOrders();
        } else if (response.status === "admin") {
            user = new Administrator(response);
            let itemList = document.querySelector("#wrapper .item-list");
            itemList.innerHTML = "";
        } else {
            ContentManager.loadPageToUrl("/main");
            return;
        }
        document.getElementById("title").innerText = user.login;
        document.getElementById("fullName").innerText = user.fullName;
        document.getElementById("status").innerHTML = user.statusRus;
        document.getElementById("email").innerText = user.email;
        if (user instanceof Client) {
            document.getElementById("other-info").appendChild(node({
                tag: "label",
                content: "Год рождения: " + user.birthdayYear
            }));
        }
    }).catch(() => ContentManager.loadPageToUrl("/not-found"));

}

function loadOrders() {
    Order.getProfileOrdersAJAX()
        .then(orders => {
            let itemList = document.querySelector("#wrapper .item-list");
            itemList.innerHTML = "";
            if (orders == null) return;
            orders.forEach(order => {
                itemList.appendChild(new Order(order).getItemNode());
            });
        });
}

function loadReturnRequest() {
    ReturnRequest.getReturnRequestAJAX()
        .then(returnRequests => {
            let itemList = document.querySelector("#wrapper .item-list");
            itemList.innerHTML = "";
            if (returnRequests == null) return;
            // Get authAccount and take his status. Append element from this account.
            Account.authorizationAJAX()
                .then(account => {
                    let status = account.status;
                    returnRequests.forEach(returnRequest => {
                        itemList.appendChild(new ReturnRequest(returnRequest)
                                .getItemNode(status));
                    });
                });

        });
}

function payFine() {

}

function closeOrder(orderId, date) {
    Order.closeOrderAJAX(orderId, date)
        .then(result => {
            if (result) {
                let element = document.querySelector(".item-list #order" + orderId);
                element.parentNode.removeChild(element);
            } else console.log("Failed to delete item with id = " + orderId);
        }).catch(reject => console.log(reject));
}

function cancelRequest() {

}

function acceptRequest() {

}

function choiceSection(section) {
    if (section === "myOrders") {
        loadOrders();
    } else if (section === "returnRequest") {
        loadReturnRequest();
    }
}