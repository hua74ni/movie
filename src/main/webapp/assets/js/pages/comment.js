/**
 * Created by huangdonghua on 2018/4/20.
 */
;
define(function (require, exports, module) {
    "require:nomunge,exports:nomunge,module:nomunge";

    /**
     * @class Comment
     * @constructor
     */
    function Comment() {
        this.init();
        console.log('Comment init calling');
    }


    var tool = require("ui-helper.js");

    /**
     * method to init page
     */
    Comment.prototype.init = function () {
        var self = this;
        self.startScore($("#scoremark1"));
        self.startScore($("#scoremark2"));
        self.startScore($("#scoremark3"));

        $("#comment-submit").bind("click", function (e) {
            var array = $(".scoremark .ystar");
            var total = 0;
            for(var i = 0;i < array.length;i++){
                total += parseFloat($(array[i]).attr("val"));
            }
            total = total / array.length;
            $("#rating").val(total);

            $("#form_comment").submit();

            // $.post("/comment/post", {subjectId: subjectId,subjectId: subjectId,rating: total}, function (data) {
            //     if (!data.success) {
            //         $(this).focus();
            //         tool.tooltip(jqCtrl, data.comment, null, true);
            //         return;
            //     } else {
            //         window.location.reload();
            //     }
            // }, "json");
        });
    };

    Comment.prototype.startScore = function (star) {
        star.find(".star ul li a").mouseenter(function(){
            var txt = $(this).attr("data-name");
            var x = $(this).parent("li").index();
            star.find('.ystar').css("width",(x+1)*2+"0%");
            star.find('.ystar').attr("val",(x+1)*2);
            // $(".score").html((x+1)*2+".0");
            star.find(".tips").html(txt).css("left",55+x*24).show();
        });
        star.find(".star ul li a").mouseleave(function(){
            star.find(".tips").html("").css("left",0).hide();
        });
    }




    module.exports = new Comment();
});