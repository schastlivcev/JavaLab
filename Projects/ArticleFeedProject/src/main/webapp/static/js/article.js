function update(el, artId, type) {
    var xhr = new XMLHttpRequest();
    var a = window.location.href.split("/");
    var url = a[0] + "//" + a[2] + "/" + a[3] + "/update";
    if(el.classList.contains("far")) {
        xhr.open("GET", url + "?type=" + type + "&add=true&artId=" + artId, true);
        xhr.send();
        xhr.onreadystatechange = function () {
            var answer = xhr.getResponseHeader("REPLYSTAT");
            if(answer === "ADDED") {
                el.classList.remove("far");
                el.classList.add("fas");
            }
        };
    }
    else {
        xhr.open("GET", url + "?type=" + type + "&remove=true&artId=" + artId, true);
        xhr.send();
        xhr.onreadystatechange = function () {
            var answer = xhr.getResponseHeader("REPLYSTAT");
            if(answer === "REMOVED") {
                el.classList.remove("fas");
                el.classList.add("far");
            }
        };
    }
}

function remove(el, artId) {
    var xhr = new XMLHttpRequest();
    var a = window.location.href.split("/");
    var url = a[0] + "//" + a[2] + "/";
    xhr.open("DELETE", url + "article/" + artId, true);
    xhr.send();
    xhr.onreadystatechange = function () {
        var answer = xhr.status;
        if(answer === 200) {
            if(window.location.href.includes("article")) {
                window.location.replace(url);
            }
            else {
                var parent = el.closest(".card.m-1.p-2");
                parent.remove();
            }
        }
    };
}