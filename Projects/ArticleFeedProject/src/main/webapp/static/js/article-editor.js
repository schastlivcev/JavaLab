function link() {
    let url = prompt("Введите URL: ");
    if(url !== null) {
        document.execCommand("createLink", false, url);
    }
}

function image() {
    let url = prompt("Введите URL картинки: ");
    if(url !== null) {
        document.execCommand("insertImage", false, url);
    }
}

function send() {
    var sendingError = document.getElementById("send_error");
    var contentEmpty = document.getElementById("page_empty");
    var content = document.getElementById("editor").innerHTML;
    var heading = document.getElementById("heading").value;
    if(content === "" && heading === "") {
        sendingError.style.display = "none";
        contentEmpty.style.display = "block";
    } else {
        contentEmpty.style.display = "none";
        var xhr = new XMLHttpRequest();
        xhr.open("POST", window.location.href + "?heading=" + heading, true);
        xhr.send(content);
        xhr.onreadystatechange = function () {
            var status = xhr.status;
            if(status === 505) {
                sendingError.style.display = "block";
            } else {
                window.location.replace("/user");
            }
        };
    }
}