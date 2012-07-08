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
    $("#alert-area").append($("<div class='alert-message alert alert-" + type + " fade in' data-alert><strong> " + message + " </strong></div>"));
    $(".alert-message").delay(5000).fadeTo(500, 0).slideUp(500, function () {
        $(this).remove();
    });

}