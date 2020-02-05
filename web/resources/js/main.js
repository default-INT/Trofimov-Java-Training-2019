"use strict";

//let links = Array.from(document.getElementsByClassName("link"));

let links = ["/main", "/cars", "/free-car", "/about", "/car-list"];

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

// Get the modal
let modals;

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
    Account.authorizationAJAX();
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
        let httpRequest = new XMLHttpRequest();

        httpRequest.open("GET", "/route" + path);
        httpRequest.responseType = "text/html";
        httpRequest.send();

        httpRequest.onload = function () {
            if (httpRequest.status === 200) {
                let content = httpRequest.response;
                document.getElementById("wrapper").innerHTML = content;
                history.pushState(null, null, path);
                ContentManager._definePage(path);
            }
        };
    }

    static _definePage(path) {
        if (path.includes("/main")) {
            ContentManager._printWelcome();
        } else if (path.includes("/cars")) {
            if (/\/cars\/\d+/.test(path)) {
                let id = parseInt(path.split("/").pop());
                Car.getCarAJAX(id);
            } else {
                try {
                    getCarsAjax();
                } catch (e) {
                    include("resources/js/filter-catalog.js");
                }
            }

        }
    }

    static _printWelcome() {
        let welcome = document.getElementById("welcome-user");
        welcome.innerHTML = "Авторизуйтесь в системе.";
    }
}

init();

function openedForm() {
    if (document.getElementById("login-form").style.display !== "none") {
        return document.getElementById("login-form");
    } else {
        return document.getElementById("registration-form");
    }
}

/**
 * Authorization in system
 *
 * TODO: Need refactoring and change logic!
 * @author Evgeniy Trofimov
 * @version 1.0
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
        Account.logInAJAX(authFormData.get("login"), authFormData.get("password"));

    } else {
        msgLabel.style.color = "red";
        msgLabel.innerHTML = "Данные некорректного формата";
    }
}

function setUserMenu(authUser) {
    let form = openedForm();
    if (!authUser) {
        let msgLabel = form.querySelector(".msg");
        msgLabel.style.color = "red";
        msgLabel.innerHTML = "Неверный логин, либо пароль";
    } else {
        let userElement = document.getElementById("user");
        userElement.remove();
        let h1 = document.querySelector("header > h1");
        h1.appendChild(authUser.getMenu());
        form.style.display = "none";
        clearForms();
        loadPageOnStart();
    }
}

/**
 * Function will check been session on server. If user logIn early, then function set in
 * userData information about user. If session empty function will setting null in userData.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
function getSessionAccount() {
    let httpRequest = new XMLHttpRequest();
    let urlPath = "/accounts/auth";
    httpRequest.open("GET", urlPath);
    httpRequest.response = "json";
    httpRequest.send();

    httpRequest.onload(function () {
        if (httpRequest.status === 200) {
            userData = httpRequest.response;
            if (!userData) {
                let user = document.getElementById("user");
                user.login.innerHTML = "Гость";
            }
        } else {
            console.log("Error send AJAX request to '" + urlPath + "'. " +
                "Response status: " + httpRequest.status);
        }
    })
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

    links.forEach(function(link, index, links) {
        if (url.includes(link)) {
            ContentManager.loadPageToUrl(link);
        }
    });
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
 * Function for logOut user.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
function exit() {
    user = undefined;
    document.getElementById("login").innerHTML = "Гость";
    let userSidebar = document.getElementById("user-sidebar");

    userSidebar.innerHTML = "";

    let li = document.createElement("li");
    li.addEventListener('click', () => {
       document.getElementById('login-form').style.display='block';
    });

    let aLog = document.createElement("a");
    aLog.class = "link";
    aLog.innerHTML = "Войти";
    li.appendChild(aLog);

    userSidebar.appendChild(li);

    li = document.createElement("li");
    li.addEventListener('click', () => {
       document.getElementById('registration-form').style.display='block';
    });
    let aReg = document.createElement("a");
    aReg.class = "link";
    aReg.innerHTML = "Зарегистрироваться";
    li.appendChild(aReg);

    userSidebar.appendChild(li);
    loadPageOnStart();
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