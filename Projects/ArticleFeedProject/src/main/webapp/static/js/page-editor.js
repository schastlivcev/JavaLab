function changeImg() {
    let imageUrl = prompt("Введите URL изображения: ");
    if(imageUrl != null && imageUrl.trim() !== "") {
        let image = new Image();
        image.addEventListener("load", function () {
            $("#profile_image img").remove();
            $("#profile_image").append(image);
            $("#image_url").val(imageUrl);
        });
        image.src = imageUrl;
    }
}

function sendPageEdit() {
    let form = $("form#page_edit");

    $.ajax({
        url: "/edit",
        method: "POST",
        data: form.serialize(),
        processData: false
    })
        .done(function() {
            window.location.replace("/user");
        })
        .fail(function() {
            $("#send_error").show();
        });
}