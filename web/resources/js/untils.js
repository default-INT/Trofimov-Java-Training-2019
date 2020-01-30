let div = () => document.createElement("div");
let divClass = (className) => {
    let el = document.createElement("div");
    el.classList.add(className);
    return el;
};
let element = (tagName) => document.createElement(tagName);
let elementClass = (tagName, className) => {
    let el = document.createElement(tagName);
    el.classList.add(className);

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
    document.body.appendChild(script);
    script.src = url;
    //setTimeout('modules.sound.start();',5000);
}