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
    let content = $("#editor").html();
    let heading = $("#heading").val();
    if(content.trim() === "" && heading.trim() === "") {
        $("#send_error").hide();
        $("#page_empty").show();
    } else {
        $("#page_empty").hide();

        $.ajax({
            url: window.location.href + "?heading=" + heading + "&" + $("#csrf").attr("name") + "=" + $("#csrf").val(),
            type: "POST",
            data: content,
            contentType: "text/html"
        })
            .done(function () {
                window.location.replace("/user");
            })
            .fail(function () {
                $("#send_error").show();
            });
    }
}