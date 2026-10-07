document.addEventListener("change", function (event) {
    if (event.target.matches("[data-image-select]")) {
        const preview = document.querySelector("[data-image-preview]");
        if (preview && event.target.value) {
            preview.src = event.target.value;
            preview.hidden = false;
        }
    }
});
