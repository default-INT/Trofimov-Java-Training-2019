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
 * @return Promise
 */
function httpRequest(params) {
    return new Promise(function (resolve, reject) {
        let xhr = new XMLHttpRequest();
        if (!!params.method) {
            xhr.open(params.method, params.url,
                !!params.type ? params.type : true);
        } else {
            xhr.open('GET', params.url,
                !!params.type ? params.type : true);
        }
        if (!!params.responseType) {
            xhr.responseType = params.responseType;
        }
        if (!!params.data) {
            xhr.send(params.data);
        } else {
            xhr.send();
        }
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
    })
}

/**
 * Create and return HTMLElement with object params {tag, id, classes, content (HTML)}.
 *
 * @param params {Object}
 * @return HTMLElement
 *
 * @version 1.6
 */
function node(params) {
    let element;
    element = !!params.tag ? document.createElement(params.tag)
        : document.createElement("div");
    if (!!params.id) element.id = params.id;
    if (!!params.classList) {
        if (params.classList instanceof Array)
            params.classList.forEach(c => element.classList.add(c));
        else element.classList.add(params.classList);
    }
    if (!!params.content) element.innerHTML = params.content;
    if (!!params.contentText) element.innerText = params.content;
    if (!!params.childNodes) {
        if (params.childNodes instanceof Array)
            params.childNodes.forEach(node => element.appendChild(node));
        else element.appendChild(params.childNodes);
    }
    if (!!params.src) element.src = params.src;
    if (!!params.src) element.src = params.src;
    if (!!params.name) element.name = params.name;
    if (!!params.cols) element.cols = params.cols;
    if (!!params.rows) element.rows = params.rows;
    if (!!params.number) element.number = params.number;
    if (!!params.background) element.style.background = params.background;
    if (!!params.placeholder) element.placeholder = params.placeholder;
    if (!!params.onclick) element.addEventListener("click", params.onclick);
    if (!!params.onchange) element.addEventListener("change", params.onclick);
    return element;
}

let init;