function changeImg(el) {
    var img = prompt("Введите URL картинки: ");
    if(img != null) {
        var imgEl = document.getElementById("userimg");
        imgEl.setAttribute("src", img);
    }
}