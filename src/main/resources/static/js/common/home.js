window.onload = function() {
    let indexList = new Vue({
        el: '#indexssss',
        data() {
            return {
                mainSubjectTotals:'',
                questionsViewCount: '',
                questionsNumberCount: '',
                technicalSharingViewCount: '',
                technicalSharingNumberCount: '',
                eventsViewCount: '',
                eventsNumberCount: '',
                indexData: [],
                otherTotals:'',
                // 定义一个图标样式数组
                iconClasses: ["fa-clock-o", "fa-bomb", "fa-bookmark", "fa-ambulance"]
            }
        },
        mounted: function() {
            this.randomFa();
            this.initialData();
            this.initialOtherData();
        },
        methods: {
            randomFa:function () {
                // 遍历所有的图标元素，随机设置图标样式
                this.$nextTick(() => {
                    this.$el.querySelectorAll('#container .forum-icon .fa').forEach((icon) => {
                        const randomIndex = Math.floor(Math.random() * this.iconClasses.length);
                        icon.classList.add(this.iconClasses[randomIndex]);
                    });
                });
            },initialData:function (){
                CoreUtil.sendGet(ctx + "sys/home/", null, function (res) {
                    indexList.mainSubjectTotals = res.data.mainSubjectTotals || 0;
                    indexList.questionsViewCount = res.data.questionsViewCount || 0;
                    indexList.questionsNumberCount = res.data.questionsNumberCount || 0;
                    indexList.technicalSharingViewCount = res.data.technicalSharingViewCount || 0;
                    indexList.technicalSharingNumberCount = res.data.technicalSharingNumberCount || 0;
                    indexList.eventsViewCount = res.data.eventsViewCount || 0;
                    indexList.eventsNumberCount = res.data.eventsNumberCount || 0;
                });
            },initialOtherData:function () {
                CoreUtil.sendGet(ctx + "sys/home/other", null, function (res) {
                    indexList.indexData = res.data;
                    if (res.data == null || res.data == undefined) {
                        indexList.otherTotals = 0;
                    } else {
                        indexList.otherTotals = res.data.length;
                    }

                });
            }
        }
    })
}