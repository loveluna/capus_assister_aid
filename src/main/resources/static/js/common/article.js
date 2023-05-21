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

        }
    },
    filters: {
        changeTime: function (value) {
            var time = new Date(value); //先将接收到的json格式的日期数据转换成可用的js对象日期
            return new timeago().format(time, 'zh_CN'); //转换成类似于几天前的格式
        }
    }
});