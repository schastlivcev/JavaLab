function sendFriendship() {
    let form = $("form#friendship");

    $.ajax({
        type: "POST",
        url: "/friendship",
        data: form.serialize(),
        processData: false
    })
        .done(function () {
            let status = $("input[name='status']");
            $("form#friendship button").remove();
            switch (status.val()) {
                case "CANCEL":
                    status.attr("value", "ADD");
                    form.append($.parseHTML('<button type="button" class="w-100 p-2 btn btnlight" onclick="sendFriendship()"><b>Добавить в друзья</b></button>'));
                    break;
                case "ACCEPT":
                    status.attr("value", "DELETE");
                    form.append($.parseHTML('<button type="button" class="w-100 p-2 btn btnwhite" onclick="sendFriendship()"><b class="lightblue">Удалить из друзей</b></button>'));
                    break;
                case "DELETE":
                    status.attr("value", "ADD");
                    form.append($.parseHTML('<button type="button" class="w-100 p-2 btn btnlight" onclick="sendFriendship()"><b>Добавить в друзья</b></button>'));
                    break;
                case "ADD":
                    status.attr("value", "CANCEL");
                    form.append($.parseHTML('<button type="button" class="w-100 p-2 btn btnwhite" onclick="sendFriendship()"><b class="lightblue">Отменить заявку</b></button>'));
                    break;
            }
        })
        .fail(function () {
            alert("Ошибка соединения с сервером.");
        });
    return false;
}