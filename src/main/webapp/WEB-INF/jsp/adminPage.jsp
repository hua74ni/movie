<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>管理员页面</title>
    <script src="${pageContext.request.contextPath}/assets/js/jquery-2.1.1.min.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap-theme.min.css">

    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>

    <link
            href="${pageContext.request.contextPath}/assets/css/jquery-confirm.min.css"
            rel="stylesheet" type="text/css" />
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/assets/js/jquery-confirm.min.js"></script>

    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/dataTables.bootstrap.css" />
    <!-- 导出数据按钮样式 -->
    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/dataTables.tableTools.css" />

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/assets/js/jquery.dataTables.min.js"></script>

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/assets/js/dataTables.bootstrap.js"></script>
    <!-- 导出数据按钮引用的js-->
    <script type="text/javascript" charset="utf-8"
            src="${pageContext.request.contextPath}/assets/js/dataTables.tableTools.js"></script>

    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/laydate/need/laydate.css" />
    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/laydate/skins/molv/laydate.css" />

    <script type="text/javascript"
            src="${pageContext.request.contextPath}/assets/laydate/laydate.js"></script>

    <script>
        var contextPath = "${pageContext.request.contextPath}";
    </script>

    <script type="text/javascript">
        var table;
        $("button[name='searchbtn']").bind("click", function () { //按钮 触发table重新请求服务器
            table.ajax.reload();
        });
        $(function() {
            table = $("#example").DataTable({
                "aLengthMenu":[10,20,40],
//		"aLengthMenu":[10,20,40,60],
                "searching":true,//开启搜索
                "lengthChange":true,
                "paging": true,//开启表格分页
                "bProcessing" : true,
                "bServerSide" : true,
                "bAutoWidth" : false,
                "sort" : "position",
                "deferRender":true,//延迟渲染
                "bStateSave" : true, //在第三页刷新页面，不会自动到第一页
                "iDisplayLength" : 8,
                "iDisplayStart" : 0,
                "dom": '<l<\'#topPlugin\'>f>rt<ip><"clear">',
                "ordering": false,//全局禁用排序
                "ajax": {
                    "type": "POST",
                    "url":contextPath + "/subject/adminList",
                    "data":function(d){
                        /*	d.notice_title=$("#notice_title").val().trim();
                         d.start_time=$("#start_time").val();
                         d.end_time=$("#end_time").val();*/
                    }
                },
                "aoColumns" : [
                    {
                        "mData" : "id",
                        "orderable" : false,
                        "sDefaultContent" : "",
                        "sWidth" : "6%"
                    }, {
                        "mData" : "title",
                        "orderable" : false, // 禁用排序
                        "sDefaultContent" : "",
                        "sWidth" : "10%"
                    }, {
                        "mData" : "directors",
                        "orderable" : false,
                        "sDefaultContent" : "",
                        "sWidth" : "10%"
                    }, {
                        "mData" : "genres",
                        "orderable" : false,
                        "sDefaultContent" : "",
                        "sWidth" : "10%"
                    }, {
                        "mData" : "rating",
                        "orderable" : false,
                        "sDefaultContent" : "",
                        "sWidth" : "10%"
                    }, {
                        "mData" : "originalTitle",
                        "orderable" : false,
                        "sDefaultContent" : "",
                        "sWidth" : "10%"
                    }, {
                        "mData" : "id",
                        "orderable" : false, // 禁用排序
                        "sDefaultContent" : "",
                        "sWidth" : "12%",
                        "render" : function(data, type, full, meta){
                            data =
                                '<button id="modifySubject" onclick="modifySubject('+data+')" class="btn btn-success btn-sm" data-id = '+ data +'><span class="glyphicon glyphicon-edit"></span> &nbsp;查看</button> &nbsp;&nbsp;'+
                                '<button id="modifyBuyer" onclick="modifyBuyer('+data+')" class="btn btn-info btn-sm" data-id = '+ data +'><span class="glyphicon glyphicon-edit"></span> &nbsp;修改</button> &nbsp;&nbsp;'+
                                '<button id="deleteBuyer" class="btn btn-danger btn-sm" data-id = '+ data +'><span class="glyphicon glyphicon-trash"></span> &nbsp;删除</button>';
                            return data;
                        }
                    }]/*,
                 "columnDefs" :
                 [{
                 "orderable" : false, // 禁用排序
                 "targets" : [0], // 指定的列
                 "data" : "id",
                 "render" : function(data, type, full, meta) {
                 return '<input type="checkbox" value="'+ data + '" name="id"/>';
                 }
                 }]*/,
                "oLanguage" : { // 国际化配置
                    "sProcessing" : "正在获取数据，请稍后...",
                    "sLengthMenu" : "显示 _MENU_ 条",
                    "sZeroRecords" : "没有找到数据",
                    "sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
                    "sInfoEmpty" : "记录数为0",
                    "sInfoFiltered" : "(全部记录数 _MAX_ 条)",
                    "sInfoPostFix" : "",
                    "sSearch" : "搜索",
                    "sUrl" : "",
                    "oPaginate" : {
                        "sFirst" : "第一页",
                        "sPrevious" : "上一页",
                        "sNext" : "下一页",
                        "sLast" : "最后一页"
                    }
                }/*,
                 initComplete:initComplete,
                 drawCallback: function( settings ) {
                 $('input[name=allChecked]')[0].checked=false;//取消全选状态
                 //	        function initComplete(data){
                 //	        	//上方topPlugin DIV中追加HTML
                 //	        	var topPlugin='<button id="addButton" class="btn btn-success btn-sm" data-toggle="modal" data-target="#addUser" style="display:block;">' +
                 //	        				  '<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>批量删除</button>';
                 //
                 //	        	//删除用户按钮的HTMLDOM
                 //	            var deleteHtml='<button id="deleteButton" class="btn btn-danger btn-sm" style="flot:left;margin-right:10px;">批量删除用户</button>' +
                 //	    					       '<span name="deleteTips" style="flot:left;margin-right:10px;color:green;"></span><div style="clear:left;"></div>';
                 //
                 //	    		$("#topPlugin").append(topPlugin);//在表格上方topPlugin DIV中追加HTML
                 //	           // $("#deleteDep").append(deleteHtml);//表格下方的按钮操作区
                 //	           // $("#deleteButton").on("click", deleteUser);//给下方按钮绑定事件
                 //
                 //	        }
                 }*/

            });
        });
    </script>

    <style type="text/css">
        #example {
            text-align: center;
        }

        #example th {
            text-align: center;
            font-weight: 500px;
            font-family: Microsoft Yahei;
        }

    </style>
</head>
<body style="margin: 30px;">
    <table id="example" class="table table-bordered table-condensed">
        <thead>
        <tr>
            <th>影片编码</th>
            <th>影片名</th>
            <th>导演</th>
            <th>影片类型</th>
            <th>评分</th>
            <th>别名</th>
            <th>操作
                <%--&nbsp;&nbsp;:&nbsp;&nbsp;--%>
                <%--<button id="addBuyer" class="btn btn-primary btn-sm">--%>
                    <%--<span class="glyphicon glyphicon-plus"></span> &nbsp;新增--%>
                <%--</button>--%>
            </th>
        </tr>
        </thead>
    </table>
</body>

<script type="text/javascript" src="<%=path%>/assets/js/jquery.json-2.4.js"></script>
<script type="text/javascript">

    $(document).delegate('#deleteBuyer','click',function() {
        var id = $(this).data("id");

        $.confirm({
            theme: 'light',
            content: "确认删除？",
            title: "信息需要确认",
            animation: 'RotateX',
            closeAnimation: 'RotateXR',
            type: 'red',
            buttons :{
                confirm: {
                    text : "确认",
                    btnClass : "btn-blue",
                    action: function(){
                        delSubmit(id);
                        $.alert("删除成功");
                    }
                },
                cancel: {
                    text : "取消",
                    btnClass : "btn-dark",
                    action: function(){

                    }
                }
            }
        });

//		alert("删除成功");
    });
    /**
     * 点击确认删除按钮
     */
    function delSubmit(id){
        $.ajax({
            url:contextPath+"/subject/adminDelete?id="+id,
            async:true,
            type:"GET",
            dataType:"json",
            success: function(data){
                var obj = eval(data);
                if(obj.code == 1)
                {
                    table.ajax.reload();
                }
                else
                {
                    alert("删除失败");
                }
            },
            error:function(data){
                alert("请求异常");
            }
        });
    };

    $("#addBuyer").click(function(){
        var url = "<%=path%>/buyerAction!add";
        window.location.href = url;
    });

    function modifySubject(id){
        var url = "<%=path%>/subject/"+id+"/edit";
        window.location.href = url;
    }

    function detailSubject(id){
        var url = "<%=path%>/subject/"+id;
        window.location.href = url;
    }


</script>

</html>
