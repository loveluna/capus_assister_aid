var app = new Vue({
    el: '#app',
    data: {
        article: {},
        userInfo: {},
        newComment: ''
    },
    mounted: function () {
        this.initialData();
    },
    methods: {
        initialData: function () {
            const articleId = CoreUtil.getUrlParameter('articleId')
            const url = ctx + "article/collaboration/" + articleId;
            CoreUtil.sendGet(url, null, res => {
                this.article = res.data.article
                this.userInfo = res.data.userInfo
            });
        }, addComment() {
            const content = this.newComment.trim()
            if (content !== '') {
                const commentParm = {
                    commid: this.article.articleId,
                    content: content
                }
                CoreUtil.sendPost(ctx + "article/add", commentParm, res => {
                    layer.msg(res.msg)
                    location.reload()
                });
            }

        }, reportArticle1() {
            const userId = this.article.userId;
            const userName = this.article.userName;
            const articleId = this.article.articleId;
            reportArticle(userId, userName, articleId);
        }
    },
    filters: {
        changeTime: function (value) {
            var time = new Date(value); //先将接收到的json格式的日期数据转换成可用的js对象日期
            return new timeago().format(time, 'zh_CN'); //转换成类似于几天前的格式
        }
    }
});

// 提交举报请求
function reportArticleSubmit() {
    var uid = $('#userId').val();
    var articleId = $('#articleId').val();
    var reason = $('#myModal5 textarea').val();
    if (reason.length == 0) {
        layer.tips("请输入举报内容", '#myModal5 textarea', {
            tips: [1, "#0FA6D8"],
            tipsMore: !1,
            time: 1300
        });
        $("#myModal5 textarea").focus();
        return;
    }

    var object = new Object();
    object["reporterId"] = uid;
    object["reportedId"] = articleId;
    object["reason"] = reason;
    // TODO: 将举报内容提交到服务器进行处理
    CoreUtil.sendPost("/article/report", object, res => {
        $('#myModal5').modal('hide');
        layer.msg(res.msg)
    });
}
function reportArticle(userId, userName, articleId) {
    $('#userName').val(userName);
    $('#userId').val(userId);
    $('#articleId').val(articleId);
    $('#myModal5 textarea').val('');
}