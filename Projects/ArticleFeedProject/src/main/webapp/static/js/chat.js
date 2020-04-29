params = new URLSearchParams(window.location.search);
window.onload = function () {
    scrollToBottom();
    receiveMessage();
};

$(document).on("keydown", "form", function (event) {
    return event.key !== "Enter";
});

function scrollToBottom() {
    $("#chat_window").scrollTop($("#chat_window").prop("scrollHeight") + 100);

}

function sendMessage() {
    if($("#message_content").val().trim() === "") {
        $("#message_content").val("");
        return false;
    }

    let body = {
        content: $("#message_content").val(),
        page_id: $("#page_id").val()
    };

    $.ajax({
        url: "/messages?ch=" + params.get("ch"),
        method: "POST",
        data: JSON.stringify(body),
        contentType: "application/json"
    })
        .done(function(response) {
            $("#message_content").val("");
            // alert("Сообщение получено");
        })
        .fail(function() {
            alert("Ошибка отправления.");
        });

    return false;
}

// LONG POLLING
function receiveMessage() {
    $.ajax({
        url: "/messages?ch=" + params.get("ch") + "&page_id=" + $("#page_id").val(),
        method: "GET",
        success: function (response) {
            if($("#chat_empty") != null) {
                $("#chat_empty").remove();
            }
            $("#chat_window").append($.parseHTML(response));
            scrollToBottom();
            receiveMessage();
        }
    })
}