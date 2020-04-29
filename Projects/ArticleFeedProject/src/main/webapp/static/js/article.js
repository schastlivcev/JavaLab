function updateWall(el, wallId, type) {
    let singlePage = false;
    let bookmarksPage = false;
    let artId = $(el).closest("#article_card").find("#article_id").val();

    if(type === "DELETE_SINGLE") {
        singlePage = true;
        type = "DELETE";
    }

    if(type === "DELETE_BOOKMARK_SINGLE") {
        bookmarksPage = true;
        type = "DELETE_BOOKMARK";
    }

    $.ajax({
        url: "/article/" + wallId,
        method: "POST",
        data: "type=" + type
    })
        .done(function(response) {
            switch (response) {
                case "DELETED":
                    if(singlePage) {
                        window.location.href = "/user";
                    } else {
                        $(el).closest("#article_card").remove();
                    }
                    break;
                case "DELETE_ERROR":
                    alert("Ошибка при удалении записи.");
                    break;
                case "REPLY_DELETED":
                    $("div[id='article_card']").each(function(index, element) {
                        if($("#article_id", this).val() === artId) {
                            $("#reply_button i", this).toggleClass("fas").toggleClass("far");
                            $("#reply_button", this).attr("onclick", "updateWall(this, " + wallId + ", 'REPLY')");
                        }
                    });
                    break;
                case "REPLY_DELETE_ERROR":
                    alert("Ошибка при удалении репоста.");
                    break;
                case "BOOKMARK_DELETED":
                    if(bookmarksPage) {
                        $("div[id='article_card']").each(function(index, element) {
                            if($("#article_id", this).val() === artId) {
                                element.remove();
                            }
                        });
                        if($("div[id='article_card']").length == 0) {
                            $("#empty_bookmarks").show();
                        }
                    } else {
                        $("div[id='article_card']").each(function(index, element) {
                            if($("#article_id", this).val() === artId) {
                                $("#bookmark_button i", this).toggleClass("fas").toggleClass("far");
                                $("#bookmark_button", this).attr("onclick", "updateWall(this, " + wallId + ", 'BOOKMARK')");
                            }
                        });
                    }
                    break;
                case "BOOKMARK_DELETE_ERROR":
                    alert("Ошибка при удалении закладки.");
                    break;
                case "REPLIED":
                    $("div[id='article_card']").each(function(index, element) {
                        if($("#article_id", this).val() === artId) {
                            $("#reply_button i", this).toggleClass("fas").toggleClass("far");
                            $("#reply_button", this).attr("onclick", "updateWall(this, " + wallId + ", 'DELETE_REPLY')");
                        }
                    });
                    break;
                case "REPLY_ERROR":
                    alert("Ошибка при создании репоста");
                    break;
                case "BOOKMARKED":
                    $("div[id='article_card']").each(function(index, element) {
                        if($("#article_id", this).val() === artId) {
                            $("#bookmark_button i", this).toggleClass("fas").toggleClass("far");
                            $("#bookmark_button", this).attr("onclick", "updateWall(this, " + wallId + ", 'DELETE_BOOKMARK')");
                        }
                    });
                    break;
                case "BOOKMARK_ERROR":
                    alert("Ошибка при создании закладки");
                    break;
                default:
                    alert("Неизвестная ошибка");
                    break;
            }
        })
        .fail(function() {
            alert("Ошибка отправления запроса.");
        });
}