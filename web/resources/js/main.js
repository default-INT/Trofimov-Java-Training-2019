"use strict";

const loginRegEx = /^[a-zA-z]{1}[a-zA-Z1-9_]{3,20}$/i;
const passRegEx = /^[a-zA-z]{1}[a-zA-Z0-9_/!/@/#/$/%/&]{3,20}$/i;
const emailRegEx = /^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$/;

let msgColor = document.getElementsByClassName("msg")[0].style.color;
let submenus = Array.from(document.getElementsByClassName("menu"));

let userStatus = "guest";
let userData = {
    id: null,
    login: null,
    email: null,
    status: null
};

let modals;

window.onpopstate = () => {
    ContentManager.loadPageToUrl(new URL(document.URL).pathname);
};

/**
 * Initialize variable and run start method.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
init = function init() {
    modals = Array.from(document.getElementsByClassName("modal"));

    setValidation();
    loadPageOnStart();

    submenus.forEach(function(submenu, index, submenus) {
        submenu.addEventListener("click", openMenu)
    });

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        modals.forEach(function(modal, index, modals) {
            if (event.target === modal) {
                modal.style.display = "none";
                clearForms();
            }
        });
    };
    Account.authorizationAJAX()
        .then(user => setUserMenu(user))
        .catch(reject => console.log(reject));
    setInterval(function () {
        document.getElementById('time').innerHTML = getDateTime();
    }, 1000);
};

/**
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
class ContentManager {
    /**
     * Loaded page in main content on context path.
     *
     * @param path - page url
     */
    static loadPageToUrl(path) {
        if (path === "") return;
        httpGetHtml("/route" + path)
            .then(content => {
                document.getElementById("wrapper").innerHTML = content;
                if (new URL(document.URL).pathname !== path) {
                    history.pushState(null, null, path);
                }
                ContentManager._definePage(path);
            }).catch(() => ContentManager.loadPageToUrl("/not-found"));
    }

    static _definePage(path) {
        if (path.includes("/cars")) {
            if (/\/cars\/\d+/.test(path)) {
                let id = parseInt(path.split("/").pop());
                loadCar(id);
            } else {
                try {
                    getCarsCatalog();
                } catch (e) {
                    include("resources/js/filter-catalog.js");
                }
            }

        } else if (path.includes("/profile")) {
            try {
                loadProfile();
            } catch (e) {
                include("resources/js/profile.js");
            }
        }
    }
}

init();

function openedForm() {
    if (document.getElementById("login-form").style.display !== "none" &&
        document.getElementById("login-form").style.display !== "") {
        return document.getElementById("login-form");
    } else if (document.getElementById("registration-form").style.display !== "none" &&
        document.getElementById("registration-form").style.display !== "") {
        return document.getElementById("registration-form");
    } else return null;
}

/**
 * Authorization in system
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
function logInUser() {
    let loginForm = document.getElementById("login-form");
    let msgLabel = loginForm.querySelector(".msg");

    let loginEntry = loginForm.querySelector("input[name='login']");
    let passEntry = loginForm.querySelector("input[name='password']");

    if (isLoginValidation(loginEntry.value) && isPassValidation(passEntry.value)) {
        msgLabel.style.color = msgColor;
        msgLabel.innerHTML = "Идет проверка данных...";
        //ajax
        let authFormData = new FormData(document.getElementById("auth-form"));
        Account.logInAJAX(authFormData.get("login"), authFormData.get("password"))
            .then(user => setUserMenu(user))
            .catch(reject => console.log(reject));

    } else {
        msgLabel.style.color = "red";
        msgLabel.innerHTML = "Данные некорректного формата";
    }
}

function setUserMenu(authUser) {
    let form = openedForm();
    if (!authUser) {
        if (form == null) return;
        let msgLabel = form.querySelector(".msg");
        msgLabel.style.color = "red";
        msgLabel.innerHTML = "Неверный логин, либо пароль";
    } else {
        let userElement = document.getElementById("user");
        userElement.remove();
        let h1 = document.querySelector("header > h1");
        h1.appendChild(authUser.getMenu());
        if (!!form) form.style.display = "none";
        clearForms();
        loadPageOnStart();
    }
}

/**
 * Registration in system
 *
 * TODO: Need refactoring and change logic!
 * @author Evgeniy Trofimov
 * @version 1.0
 */
function signUpUser() {
    let regForm = document.getElementById("registration-form");
    let msgLabel = regForm.querySelector(".msg");

    let loginEntry = regForm.querySelector("input[name='login']");
    let passEntry = regForm.querySelector("input[name='password']");
    let retryPassEntry = regForm.querySelector("input[name='retryPassword']");
    let emailEntry = regForm.querySelector("input[name='email']");

    if (isLoginValidation(loginEntry.value) && isPassValidation(passEntry.value)
        && isEmailValidation(emailEntry.value) && isPassValidation(retryPassEntry.value)) {
        if (passEntry.value !== retryPassEntry.value) {

            passEntry.style.color = "red";
            retryPassEntry.style.color = "red";

            msgLabel.style.color = "red";
            msgLabel.innerHTML = "Пароли не совпадают";

            return;
        }
        msgLabel.style.color = msgColor;
        msgLabel.innerHTML = "Идет проверка данных...";
        //ajax
        let formData = new FormData(document.getElementById("reg-form"));

        let json = JSON.stringify({
            login: formData.get("login"),
            password: formData.get("password"),
            email: formData.get("email"),
            fullName: formData.get("fullName"),
            birthdayYear: formData.get("birthdayYear")
        });

        Account.signUpAJAX(json)
            .then(user => {
                setUserMenu(user);
            }).catch(reject => console.log(reject));
        //end ajax
    } else {
        msgLabel.style.color = "red";
        msgLabel.innerHTML = "Данные некорректного формата";
    }
}

/**
 * Open inner menu. Class - .submenu
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
function openMenu() {
    let submenu = this.getElementsByClassName("submenu")[0];
    if (submenu.style.transform === "scaleY(1)") {
        submenu.style.transform = "scaleY(0)";
    } else {
        submenu.style.transform = "scaleY(1)";
    }
}

/**
 * Defines loaded page on url and get content with support Ajax request.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
function loadPageOnStart() {
    let url = document.location.href;
    let originUrl = new URL(document.URL).origin;
    ContentManager.loadPageToUrl(url.replace(originUrl, ""));
}

/**
 * Adding zeros in start to numbers
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
function setZeroFirstFormat(value)
{
    if (value < 10)
    {
        value='0' + value;
    }
    return value;
}

/**
 * Function return now date and time in string format.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 * @returns {string} date in format: dd.mm.yyyy hh.mm.ss
 */
function getDateTime() {
    let currentDateTime = new Date();
    let day = setZeroFirstFormat(currentDateTime.getDate());
    let month = setZeroFirstFormat(currentDateTime.getMonth()+1);
    let year = currentDateTime.getFullYear();
    let hours = setZeroFirstFormat(currentDateTime.getHours());
    let minutes = setZeroFirstFormat(currentDateTime.getMinutes());
    let seconds = setZeroFirstFormat(currentDateTime.getSeconds());

    return day + "." + month + "." + year + " " + hours + ":" + minutes + ":" + seconds;
}

/**
 * Function convert date to string.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 * @return {string}
 */
function dateFormat(currentDateTime) {
    let day = setZeroFirstFormat(currentDateTime.getDate());
    let month = setZeroFirstFormat(currentDateTime.getMonth()+1);
    let year = currentDateTime.getFullYear();
    let hours = setZeroFirstFormat(currentDateTime.getHours());
    let minutes = setZeroFirstFormat(currentDateTime.getMinutes());

    return day + "." + month + "." + year + " " + hours + ":" + minutes;
}

function clearForms() {
    let forms = Array.from(document.querySelectorAll('input'));
    let msgs = Array.from(document.querySelectorAll('.msg'));

    forms.forEach((form, index, forms) => form.value = "");

    msgs.forEach((msg, index, msgs) => msg.innerHTML = "");

}

function isLoginValidation(login) {
    return loginRegEx.test(login);
}

function isPassValidation(password) {
    return passRegEx.test(password);
}

function isEmailValidation(email) {
    return emailRegEx.test(email);
}

function setValidation() {
    let loginForms = Array.from(document.getElementsByName("login"));
    let passForms = Array.from(document.querySelectorAll("input[type='password']"));
    let emailForms = Array.from(document.getElementsByName("email"));
    //TODO: update logic! Concat inputs and check names.
    emailForms.forEach((form, index, emailForms) => {
        form.addEventListener("change", function() {
            if (!emailRegEx.test(this.value)) {
                this.style.color = "red";
            } else {
                this.style.color = "black";
            }
        });
    });

    passForms.forEach((form, index, passForms) => {
        form.addEventListener("change", function() {
            if (!passRegEx.test(this.value)) {
                this.style.color = "red";
            } else {
                this.style.color = "black";
            }
        });
    });

    loginForms.forEach((form, index, loginForms) => {
        form.addEventListener("change", function() {
            if (!loginRegEx.test(this.value)) {
                this.style.color = "red";
            } else {
                this.style.color = "black";
            }
        });
    });    
}