if (typeof jQuery !== 'undefined') {
    (function ($) {
        $('#spinner').ajaxStart(function () {
            $(this).fadeIn();
        }).ajaxStop(function () {
                $(this).fadeOut();
            });
    })(jQuery);
}

function newAlert(type, message, targetDiv) {
    targetDiv = targetDiv || '#alert-area';
    $(targetDiv).html($("<div class='alert-message alert alert-" + type + " fade in' data-alert><strong> " + message + " </strong></div>"));
    $(".alert-message").delay(5000).fadeTo(500, 0).slideUp(500, function () {
        $(this).remove();
    });

}

function clearForm(ele) {
    $(ele).find(':input').each(function() {
        switch(this.type) {
            case 'password':
            case 'select-multiple':
            case 'select-one':
            case 'text':
            case 'textarea':
                $(this).val('');
                break;
            case 'checkbox':
            case 'radio':
                this.checked = false;
        }
    });

}

function disableShout() {
    $('#shout').val($('#shout_').val());
    $('#shout_').attr('disabled', 'disabled');
    $('#shoutBtn').attr('disabled', 'disabled');
}

function enableShout() {
    setTimeout(function() {$('#shout_').removeAttr('disabled'); $('#shoutBtn').removeAttr('disabled'); $('#shout_').focus();}, 5000);
}

function pollReservationStatus() {
    $.ajax({
        url: '/reservationStatus/ajaxFetchStatus',
        dataType: 'html',
        data: { timestamp: new Date().getTime() },
        success:function (data) {
            $('#reservationStatus').html(data);
            if (cntResPolls++ < 120) { // poll for 120 * 1 minute = 120 minutes
                setTimeout(function () {
                    pollReservationStatus();
                }, 60000);
            }
        }
    });
}

$(document).ready(function() {
    pollReservationStatus();
})