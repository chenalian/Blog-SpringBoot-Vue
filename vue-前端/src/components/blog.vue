<template>
    <div>
        <!--博客主体-->
        <div class="animated">
            <div class="ui m-container-small2 m-padded-tb-big animated bounceInLeft">
                <!--博客头信息-->
                <div class="ui top attached segment">
                    <div class="eleven wide column">
                        <div class="ui horizontal link list">
                            <div class="item">
                                <img :src="adminImg" class="ui avatar image">
                                <div class="content">{{username}}</div>
                            </div>
                            <div class="item" title="发布日期">
                                <i class="calendar icon" />
                                <span>{{blog.createTime | dateFormat}}</span>
                            </div>
                            <div class="item" title="浏览次数">
                                <i class="eye icon"/>
                                <span>{{blog.viewCount}}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <!--博客内容区域-->
                <div class="ui attached padded segment">
                    <!-- 原创、转载 -->
                    <div class="ui right aligned basic segment">
                        <div class="ui teal label" v-if="blog.origin===1">原创</div>
                        <div class="ui orange label" v-if="blog.origin===0">转载</div>
                    </div>
                    <!-- 博客标题 -->
                    <h1 class="ui center aligned header" style="margin: 24px 0">{{blog.title}}</h1>
                    <!-- 博客内容 typo typo-selection -->
                    <div id="content" class="m-padded-lr-responsive m-padded-tb-large" v-highlight v-html="blog.content"></div>
                    <!-- 博客分类 -->
                    <div class="m-padded-lr">
                        <div class="ui basic teal left pointing label">{{blog.typeName}}</div>
                    </div>
                    <!-- 赞赏 -->
                    <div class="ui center aligned basic segment">
                        <button class="get-btn ui orange basic circular button" v-show="blog.reward===1">赞赏</button>
                    </div>
                    <div class="get-img ui flowing popup transition hidden">
                        <div class="ui orange basic label">
                            <div class="ui images">
                                <div class="image">
                                    <img :src="wechatPay" class="ui rounded bordered image" style="width: 140px">
                                    <div style="font-size: 15px">微信</div>
                                </div>
                                <div class="image">
                                    <img :src="aliPay" class="ui rounded bordered image" style="width: 140px">
                                    <div style="font-size: 15px">支付宝</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--博客描述-->
                <div class="ui attache d positive message">
                    <div class="ui grid">
                        <div class="eleven wide column">
                            <ul class="list">
                                <li>作者：{{username}}</li>
                                <li>发表时间：<span>{{blog.createTime | dateFormat}}</span></li>
                                <li>最后一次修改时间：<span>{{blog.updateTime | dateFormat}}</span></li>
                                <li>博客内容由作者整理而来，如有违规或侵权等内容请联系我进行更正</li>
                            </ul>
                        </div>
                        <div class="five wide column" style="padding-top: 0; padding-bottom: 0">
                            <img :src="wechatQr" class="ui right floated rounded bordered image" style="width: 110px">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- TODO 这个位置的导航还没有弄 -->
        <!--浮动操作框-->
        <div class="m-fixed">
            <div class="ui vertical icon buttons ">
                <button class="show-mulu ui teal button">目录</button>
                <button id="to-top" class="ui icon button" @click="toTop()">
                    <i class="chevron up icon" />
                </button>
            </div>
        </div>
        <!-- 目录div -->
        <div class="ui toc-container mulu popup flowing transition hidden" style="width: 250px; overflow: hidden;"></div>
    </div>
</template>

<script>
    import tocbot from 'tocbot';

    export default {
        name: "blog",
        data() {
            return {
                id: null,
                blog: {}
            }
        },
        created() {
            this.id = this.$route.params.id;
            this.selectBlog();
            this.$nextTick(()=>{
                this.openQR();
                this.openTocbot();
                setTimeout(()=>{
                    tocbot.init({
                        //要把目录添加元素位置，支持选择器
                        tocSelector: ".toc-container",
                        //获取html的元素
                        contentSelector: "#content",
                        //要显示的id的目录
                        headingSelector: "h1, h2, h3",
                        hasInnerContainers: false
                    });
                }, 500)
            });
        },
        mounted() {

        },
        methods: {
            // 打开，关闭赞赏二维码
            openQR() {
                $(".get-btn").popup({
                    popup: $(".get-img.popup"),
                    on: "click",
                    position: "bottom center"
                });
            },
            // 打开，关闭目录
            openTocbot() {
                $(".show-mulu").popup({
                    popup: $(".toc-container.popup"),
                    on: "click",
                    position: "top center"
                });
            },
            // 查询博客详情
            selectBlog() {
                this.$http.get("/blog/" + this.id).then(res=>{
                    this.blog = res.data.blog
                })//.catch(err=>this.$router.push("/"))
            },
            toTop() {
                window.scrollTo({top: 0, behavior: "smooth"});
            }
        },
        computed: {
            wechatPay() {
                return this.$common.getField("wechatPay");
            },
            aliPay() {
                return this.$common.getField("aliPay");
            },
            username() {
                return this.$common.getField("username");
            },
            adminImg() {
                return this.$common.getField("adminImg");
            },
            wechatQr() {
                return this.$common.getField("wechatQr");
            }
        }
    }
</script>

<style scoped>
.m-fixed {
    position: fixed;
    bottom: 10px;
    right: 10px;
    z-index: 10;
}
</style>
