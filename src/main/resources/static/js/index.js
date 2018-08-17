$(function () {

    var _pageSize;

    // 根据用户名、页面索引、页面大小获取用户列表
    function getBlogByName(pageIndex, pageSize) {
        $.ajax({
            url:"/blogs",
            contentType:'application/json',
            data:{
                "async":true,
                "pageIndex":pageIndex,
                "pageSize":pageSize,
                "keyword":$("#indexkeyword").val()
            },
            success:function (data) {
                $("#mainContainer").html(data);

                var keyword = $("#indexkeyword").val();

                if(keyword.length > 0){
                    $(".nav-item .nav-link").removeClass("active");
                }

            },
            error :function () {
                toastr.error("error");
            }

        })
    }

    $.tbpage("#mainContainer",function (pageIndex, pageSize) {
        getBlogByName(pageIndex,pageSize);
        _pageSize = pageSize;
    })

    //关键字搜索
    $("#indexsearch").click(function () {
        alert("search");
        getBlogByName(0,_pageSize);
    });

    $(".nav-item .nav-link").click(function () {

        var url = $(this).attr("url");
        $(".nav-item .nav-link").removeClass("active");
        $(this).addClass("active");

        $.ajax({
            url : url + '&async=true',
            success:function (data) {
                $("#mainContainer").html(data);
            },
            error:function () {
                toastr.error("error");
            }
        })

        $("#indexkeyword").val('');

    });

})