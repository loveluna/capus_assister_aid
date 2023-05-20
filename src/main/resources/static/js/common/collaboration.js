var app = new Vue({
    el: '#app',
    data: {
        articleRespVO: []
    },
    computed: {
        articleRespVOGrouped: function () {
            var groups = [[], [], []]; // 初始化三个组
            this.articleRespVO.forEach(function (item, index) {
                var groupIndex = index % 3; // 计算组的索引
                groups[groupIndex].push(item); // 将文章数据按照组的索引分组
            });
            return groups;
        }
    },
    created: function () {
        var self = this;
        var category = CoreUtil.getUrlParameter("category");
        CoreUtil.sendGet(ctx + "sys/forum_post/" + category, null, function (res) {
            self.articleRespVO = res.data;
        });
    },
    filters: {
        changeTime: function (value) {
            var time = new Date(value); //先将接收到的json格式的日期数据转换成可用的js对象日期
            return new timeago().format(time, 'zh_CN'); //转换成类似于几天前的格式
        }
    }
});