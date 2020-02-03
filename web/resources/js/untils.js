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
    script.type='text/javascript';
    script.src = url;
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

let init;