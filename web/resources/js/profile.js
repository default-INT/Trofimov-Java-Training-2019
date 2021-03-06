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
            //loadReturnRequest();
        } else if (response.status === "admin") {
            user = new Administrator(response);
            let itemList = document.querySelector("#wrapper .item-list");
            itemList.innerHTML = "";
            loadReturnRequest();
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
                    if (account.status === "admin") {
                        returnRequests.forEach(returnRequest => {
                            itemList.appendChild(new ReturnRequest(returnRequest)
                                .getItemNode(account.status));
                        });
                    } else if (account.status === "client") {
                        returnRequests.forEach(returnRequest => {
                            itemList.appendChild(new ReturnRequest(returnRequest)
                                .getItemNode(account.status));
                        });
                    }

                });

        });
}

function payFine(requestId) {
    ReturnRequest.closeReturnRequest(requestId)
        .then(result => {
            if (result) {
                let element = document.querySelector(".item-list #request" + requestId);
                element.parentNode.removeChild(element);
            } else console.log("Failed to delete item with id = " + requestId);
        }).catch(reject => console.log(reject));;
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

function cancelRequest(requestId) {
    let element = document.querySelector(".item-list #request" + requestId);
    ReturnRequest.cancelRequestAJAX({
        id: requestId,
        description: element.querySelector('.description textarea[name="description"]').value,
        repairCost: element.querySelector('.description input[name="repairCost"]').value
    }).then(result => {
        if (result) {
            element.parentNode.removeChild(element);
        } else console.log("Failed to delete item with id = " + requestId);
    });
}

function acceptRequest(requestId) {
    ReturnRequest.closeReturnRequest(requestId)
        .then(result => {
            if (result) {
                let element = document.querySelector(".item-list #request" + requestId);
                element.parentNode.removeChild(element);
            } else console.log("Failed to delete item with id = " + requestId);
        })
}

function choiceSection(section) {
    if (section === "myOrders") {
        loadOrders();
    } else if (section === "returnRequest") {
        loadReturnRequest();
    }
}