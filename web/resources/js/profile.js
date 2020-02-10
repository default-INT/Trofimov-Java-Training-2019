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
        } else if (response.status === "admin") {
            user = new Administrator(response);
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