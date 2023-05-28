layui.use(['form', 'element', 'util', 'carousel', 'laypage', 'layer', 'table'], function () {
    var element = layui.element;
    var util = layui.util;
    var form = layui.form;
    var laypage = layui.laypage
        , layer = layui.layer;
    form.on('select(types)', function (data) {
        var indexGID = data.elem.selectedIndex;
        lookallproduct(data.elem[indexGID].title);
    });
});
function lookallproduct(status) {
    layui.use(['form', 'element', 'util', 'carousel', 'laypage', 'layer','table'], function () {
        var table = layui.table;
        table.render({
            elem: '#reportList'
            , url: basePath+'/article/reports/'+status
            , page: {
                layout: ['limit', 'count', 'prev', 'page', 'next', 'skip']
                , groups: 3
                , limits: [20, 50, 100]
                , limit: 20
            }, cols: [[
                {field: 'qid', title: 'ID',width:80, align:'center'}
                , {field: 'topic', title: '名称', width: 300, align:'center'}
                , {field: 'articleType', title: '类别', width: 100, align:'center',templet: function(d){
                        return d.articleType === "0" ? '学习互助' : '生活互助';
                    }}
                , {field: 'userName', title: '被举报者', width: 700, align:'center'}
                , {field: 'reason', title: '举报内容', width: 700, align:'center',templet: function(d){
                        return d.report.reason;
                    }}
                , {field: 'reportTime', title: '举报时间', width: 160,sort: true, align:'center',templet: function(d){
                            return d.report.reportTime;
                        }}
                , {fixed: 'right', title: '操作', toolbar: '#barDemo', width:200, align:'center'}
            ]], done: function (res, curr, count) {
                var i=1;
                $("[data-field='qid']").children().each(function () {
                    if($(this).text() == 'ID') {
                        $(this).text("ID")
                    }else{
                        $(this).text(i++)
                    }
                });
            }
        });
        //监听行工具事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            if(obj.event === 'disable'){
                layer.confirm('确认文章违规吗？', {
                    btn: ['确定','算了'], //按钮
                    title:"违规文章",
                    offset:"50px"
                }, function(){
                    layer.closeAll();
                    $.ajax({
                        url: basePath+'/article/updateStatus/'+data.articleId+"/0",
                        data: "",
                        contentType: "application/json;charset=UTF-8", //发送数据的格式
                        type: "get",
                        dataType: "json", //回调
                        beforeSend: function () {
                            layer.load(1, { //icon支持传入0-2
                                content: '请稍等...',
                                success: function (layero) {
                                    layero.find('.layui-layer-content').css({
                                        'padding-top': '39px',
                                        'width': '60px'
                                    });
                                }
                            });
                        },
                        complete: function () {
                            layer.closeAll('loading');
                        },
                        success: function (data) {
                            console.log(data)
                            if(data.status===200){
                                layer.msg(data.message, {
                                    time: 1000,
                                    icon: 1,
                                    offset: '50px'
                                }, function () {
                                    location.reload();
                                });
                            }else {
                                layer.msg(data.message, {
                                    time: 1000,
                                    icon: 2,
                                    offset: '50px'
                                });
                            }
                        }
                    });
                }, function(){
                });
            }else if (obj.event === 'release') {
                layer.confirm('确认文章解封吗？', {
                    btn: ['确定','算了'], //按钮
                    title:"审核文章",
                    offset:"50px"
                }, function(){
                    layer.closeAll();
                    $.ajax({
                        url: basePath+'/article/updateStatus/'+data.articleId+"/1",
                        data: "",
                        contentType: "application/json;charset=UTF-8", //发送数据的格式
                        type: "get",
                        dataType: "json", //回调
                        beforeSend: function () {
                            layer.load(1, { //icon支持传入0-2
                                content: '请稍等...',
                                success: function (layero) {
                                    layero.find('.layui-layer-content').css({
                                        'padding-top': '39px',
                                        'width': '60px'
                                    });
                                }
                            });
                        },
                        complete: function () {
                            layer.closeAll('loading');
                        },
                        success: function (data) {
                            console.log(data)
                            if(data.status===200){
                                layer.msg(data.message, {
                                    time: 1000,
                                    icon: 1,
                                    offset: '50px'
                                }, function () {
                                    location.reload();
                                });
                            }else {
                                layer.msg(data.message, {
                                    time: 1000,
                                    icon: 2,
                                    offset: '50px'
                                });
                            }
                        }
                    });
                }, function(){
                });
            }
        });
    });
}
lookallproduct(100);