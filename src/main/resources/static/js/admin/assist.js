var app = new Vue({
    el: '#article',
    data() {
        return {
            listimages: [],
            mainimg: ""
        }
    },
    mounted: function () {
        //将vue中的函数设置成全局的
        window.showlistimages = this.showlistimages;
        window.setmainimgurl = this.setmainimgurl;
        window.setvideourl = this.setvideourl;

        window.getlistimages = this.getlistimages;
        window.getmainimgurl = this.getmainimgurl;
        window.getvideourl = this.getvideourl;
    },
    methods: {
        showlistimages: function (imgurl) {
            var that = this;
            if (that.listimages.length !== 3) {
                var object = new Object();
                object["imgsrc"] = imgurl;
                that.listimages.push(object);//向vue数组中添加图片
            }
        },getlistimages: function () {
            var that = this;
            return that.listimages;
        },setmainimgurl: function (imgurl) {
            var that = this;
            that.mainimg = imgurl;
        },getmainimgurl: function () {
            var that = this;
            return that.mainimg;
        },setvideourl: function (videosrc) {
            var that = this;
            that.videourl = videosrc;
        },getvideourl: function () {
            var that = this;
            return that.videourl;
        },delimage: function (ids) {
            var that = this;
            that.listimages.splice(ids, 1);//从vue数组中删除此图
        },mouseOver: function (id) {
            $("#del" + id).show();
        }, mouseLeave: function (id) {
            $("#del" + id).hide();
        },openimg: function (imgsrc) {
            var img = '<img src="' + imgsrc + '" style="width:100%;height:100%">';
            layer.open({
                type: 1,
                title: false,
                shade: 0.6,
                closeBtn: 0, //不显示关闭按钮
                anim: 2,
                shadeClose: true, //开启遮罩关闭
                content: img
            });
        }
    }
});

layui.use(['form', 'upload', 'element', 'layedit'], function () {
    let upload = layui.upload;
    let form = layui.form, layedit = layui.layedit;
    //创建一个题目编辑器
    let editIndex = layedit.build('newscontents', {
        tool: [
            'strong'
            , 'italic'
            , 'underline'
            , 'del'
            , '|'
            , 'link'
            , 'face'
        ]
    });
    upload.render({
        elem: '#test3'
        , url: basePath + '/relgoods/images'
        , accept: 'images' //图片
        , size: 1024 * 20
        , exts: 'png|jpg'
        , multiple: true
        , number: 3
        , choose: function (obj) {
            //上传前判断已经上传了多少张图片
            var imgs = document.getElementById("otherimages").getElementsByTagName("img");
            if (imgs.length === 6) {
                layer.msg('最多上传三张图片');
                layer.close(obj);//报错让其停止上传
            }
        }
        , progress: function (n) {
            var percent = n + '%';
            layer.msg(percent, {
                icon: 16
                , shade: 0.01
            });
        }
        , done: function (res) {
            //如果上传失败
            if (res.code > 0) {
                return layer.msg('上传失败');
            } else {
                layer.closeAll('loading');
                layer.msg('上传成功', {
                    time: 1000,
                    icon: 1,
                    offset: '150px'
                });
                showlistimages(res.data.src[0]);
                $("#otherimages").show();
            }
        }
        , error: function () {
            layer.closeAll('loading');
            layer.msg('上传失败', {
                time: 1000,
                icon: 2,
                offset: '150px'
            });
        }
    });
    form.on('submit(submit)', function (data) {
        if (data.field.topic.length > 200) {
            layer.msg('公告标题过长', {
                time: 1000,
                icon: 2,
                offset: '150px'
            });
            return false;
        }
        var object = new Object();
        object["topic"] = data.field.topic;
        object["articleContent"] = layedit.getContent(editIndex);
        object["articleImage"] = vuemainimg;
        object["articleDesc"] = data.field.articleDesc;
        object["category"] = data.field.category;
        object["articleType"] = data.field.articleType;
        var jsonData = JSON.stringify(object);
        $.ajax({
            url: basePath + "/article/create",
            data: jsonData,
            contentType: "application/json;charset=UTF-8",
            type: "post",
            dataType: "json",
            beforeSend: function () {
                layer.load(1, {
                    content: '发布中...',
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
                layer.msg("发布成功", {
                    time: 1000,
                    icon: 1,
                    offset: '100px'
                }, function () {
                    window.location.reload();
                });
            }, error: function () {
                layer.msg('发布失败', {
                    time: 1000,
                    icon: 2,
                    offset: '150px'
                });
            }
        });
        return false;
    });
});