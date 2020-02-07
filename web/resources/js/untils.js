let div = () => document.createElement("div");
let divClass = (className) => {
    let el = div();
    el.classList.add(className);
    return el;
};
let divId = (id) => {
    let el = div();
    el.id = id;
    return el;
};
let divIdContent = (id, content) => {
    let el = divId(id);
    el.innerHTML = content;
    return el;
};
let element = (tagName) => document.createElement(tagName);
let elementClass = (tagName, className) => {
    let el = element(tagName);
    el.classList.add(className);
    return el;
};
let elementId = (tagName, id) => {
    let el = element(tagName);
    el.id = id;
    return el;
};
let elementClassId = (tagName, id, className) => {
    let el = elementClass(tagName, className);
    el.id = id;
    return el;
};
let column = function () {
    let col = divClass("column");

    for (let i = 0; i < arguments.length; i++) {
        let label = divClass("label");
        label.innerHTML = arguments[i];
        col.appendChild(label);
    }
    return col;
};

function include(url) {
    let script = document.createElement('script');
    let pageUrl = new URL(document.URL).origin;
    script.type='text/javascript';
    script.src = pageUrl + "/" + url;
    document.body.appendChild(script);

    script.onload = function () {
        if (!!this.init) {
            this.init();
        }
    };
    script.onerror = function() {
        alert("Error loading " + this.src);
    };
    //setTimeout('modules.sound.start();',5000);
}

function getInnerElement(node, query) {
    return node.querySelector(query);
}

/**
 * Send AJAX 'GET' request with help XMLHttpRequest and return Promise where including JSON object.
 *
 * @param url
 * @return {Promise<Object>}
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
function httpGetJson(url) {
    return new Promise(function(resolve, reject) {

        let xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.responseType = "json";

        xhr.onload = function() {
            if (this.status === 200) {
                resolve(this.response);
            } else {
                let error = new Error(this.statusText);
                error.code = this.status;
                reject(error);
            }
        };
        xhr.onerror = function() {
            reject(new Error("Network Error"));
        };
        xhr.send();
    });
}

/**
 * Send AJAX 'GET' request with help XMLHttpRequest and return Promise where including JSON object.
 *
 * @param url
 * @param json
 * @return {Promise<Object>}
 */
function httpPostJson(url, json) {
    return new Promise(function(resolve, reject) {
        let xhr = new XMLHttpRequest();
        xhr.open('POST', url);
        xhr.responseType = "json";

        xhr.onload = function() {
            if (this.status === 200) {
                resolve(this.response);
            } else {
                let error = new Error(this.statusText);
                error.code = this.status;
                reject(error);
            }
        };
        xhr.onerror = function() {
            reject(new Error("Network Error"));
        };
        xhr.send(json);
    });
}

/**
 *
 * @param url
 * @return {Promise<String>}
 */
function httpGetHtml(url) {
    return new Promise(function(resolve, reject) {
        let xhr = new XMLHttpRequest();
        xhr.open('GET', url);
        xhr.responseType = "text/html";

        xhr.onload = function() {
            if (this.status === 200) {
                resolve(this.response);
            } else {
                let error = new Error(this.statusText);
                error.code = this.status;
                reject(error);
            }
        };
        xhr.onerror = function() {
            reject(new Error("Network Error"));
        };
        xhr.send();
    });
}

/**
 *
 * @param params {Object}
 */
function httpRequest(params) {

}

let init;