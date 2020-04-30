function sendFriendship() {
    let form = $("form#friendship");

    $.ajax({
        type: "POST",
        url: "/friendship?" + $("#csrf").attr("name") + "=" + $("#csrf").val(),
        data: form.serialize(),
        processData: false
    })
        .done(function () {
            let status = $("input[name='status']");
            switch (status.val()) {
                case "CANCEL":
                    status.attr("value", "ADD");
                    $("form#friendship button i").toggleClass("fa-user-times").toggleClass("fa-user-plus");
                    break;
                case "ACCEPT":
                    status.attr("value", "DELETE");
                    $("form#friendship button i").toggleClass("fa-user-check").toggleClass("fa-user-slash");
                    break;
                case "DELETE":
                    status.attr("value", "ADD");
                    $("form#friendship button i").toggleClass("fa-user-slash").toggleClass("fa-user-plus");
                    break;
                case "ADD":
                    status.attr("value", "CANCEL");
                    $("form#friendship button i").toggleClass("fa-user-plus").toggleClass("fa-user-times");
                    break;
            }
        })
        .fail(function () {
            alert("Ошибка соединения с сервером.");
        });
    return false;
}