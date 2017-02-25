/**
 * Created by eric on 17-2-25.
 */
(function($) {
    $.fn.extend({"verticalCenter": function () {
        var target = this;
        if(target && $(target).parent()) {
            $(target).css("position", "absolute");
            var targetHeight = $(target).height();
            var parentHeight = $(target).parent().height();
            $(target).css("top", (parentHeight - targetHeight) / 2);
        }
        return $(target);
    }});
    $.fn.extend({"horizontalCenter": function () {
        var target = this;
        if(target && $(target).parent()) {
            $(target).css("position", "absolute");
            var targetWidth = $(target).width();
            var parentWidth = $(target).parent().width();
            $(target).css("left", (parentWidth - targetWidth) / 2);
        }
        return $(target);
    }})
})(jQuery);