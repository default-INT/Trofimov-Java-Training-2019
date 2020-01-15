"use strict"

let links = Array.from(document.getElementsByClassName("link"));

for (let i = 0; i < links.length; i++) {
    links[i].addEventListener("click", clickRootLink);
}

loadPageOnStart();

function clickRootLink() {
    loadPageToUrl(this.name)
}

function loadPageOnStart() {
    let url = document.location.href;
    let httpRequest = new XMLHttpRequest();

    //Временное решение, ибо не все ссылки сайта могут быть на странице
    links.forEach(function(link, index, links) {
        if (url.includes(link.name)) {
            loadPageToUrl(link.name);
        }
    });
}

function loadPageToUrl(path) {
    let httpRequest = new XMLHttpRequest();

    httpRequest.open("GET", "/route" + path);
    httpRequest.responseType = 'text/html';
    httpRequest.send();

    httpRequest.onload = function () {
        if (httpRequest.status == 200) {

            let content = httpRequest.response;
            //let content = wrap.document.getElementById("wrapper");
            document.getElementById("wrapper").innerHTML = content;
            //content.f
            history.pushState(null, null, path);

        } else {
            console.log('Something went wrong..!!');
        }
    };
}

/*
 * Adding zeros in start to numbers
 */
function setZeroFirstFormat(value)
{
    if (value < 10)
    {
        value='0' + value;
    }
    return value;
}

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

setInterval(function () {
    document.getElementById('time').innerHTML = getDateTime();
}, 1000);