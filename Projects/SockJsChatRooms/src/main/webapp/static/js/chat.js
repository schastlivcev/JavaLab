var websocket;

window.onload = function () {
    scrollToBottom();
    connect();
};

$(document).on("keydown", "form", function (event) {
    return event.key !== "Enter";
});

function scrollToBottom() {
    $("#chat_window").scrollTop($("#chat_window").prop("scrollHeight") + 100);

}

function connect() {
    websocket = new SockJS("http://localhost:8080/messages");
    websocket.onmessage = function(response) {
        let message = response["data"];
        let json = JSON.parse(message);
        if($("#chat_empty") != null) {
            $("#chat_empty").remove();
        }
        $("#chat_window").append($.parseHTML(json["content"]));
        scrollToBottom();
    }
}

function sendMessage() {
    if(!($("#message_content").val().trim() === "")) {
        let body = {
            content: $("#message_content").val(),
            channel_id: $("#channel_id").val(),
            user_id: $("#user_id").val()
        };

        websocket.send(JSON.stringify(body));
        $("#message_content").val("");
    } else {
        $("#message_content").val("");
    }
}