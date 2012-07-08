if (typeof jQuery !== 'undefined') {
    (function ($) {
        $('#spinner').ajaxStart(function () {
            $(this).fadeIn();
        }).ajaxStop(function () {
                $(this).fadeOut();
            });
    })(jQuery);
}

function newAlert(type, message) {
    $("#alert-area").append($("<div class='alert-message alert alert-" + type + " fade in' data-alert> " + message + " </div>"));
    $(".alert-message").delay(3000).fadeOut("slow", function () {
        $(this).remove();
    });
}