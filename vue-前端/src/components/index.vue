<!-- 首页 -->
<template>
    <main class="m-padded-tb-large m-min-height">
        <div class="ui container" style="min-height: 800px">
            <div class="ui grid stackable animated fadeIn" v-if="showContent">
                <!--左侧博客预览-->
                <div class="eleven wide column">
                    <!-- 博客Title -->
                    <div class="ui top attached segment">
                        <div class="ui middle aligned two column grid">
                            <div class="column">
                                <h3 class="ui teal header">博客总览</h3>
                            </div>
                            <div class="right aligned column">
                                共 <h3 class="ui orange header m-inline-block" v-text="info.total"></h3> 篇
                            </div>
                        </div>
                    </div>
                    <!-- 博客列表 -->
                    <div class="ui attached segment m-container">
                        <!--每条博客的内容-->
                        <blog-list :list="info.list"></blog-list>
                    </div>
                    <!-- 分页 item active -->
                    <div class="ui bottom attached segment" style="text-align: center">
                        <div class="ui mini pagination menu">
                            <a :class="{'item': true, 'disabled': !info.hasPreviousPage}" @click="prePage()">上一页</a>
                            <a :class="{'item': true, 'active': item===pageNum}" v-for="(item, index) in info.navigatepageNums" :key="index" v-text="item" @click="usePage(item)" />
                            <a :class="{'item': true, 'disabled': !info.hasNextPage}" @click="nextPage()">下一页</a>
                        </div>
                    </div>
                    <!-- 页码提示、跳转 -->
                    <div class="ui segment m-text middle two">
                        <div class="ui middle aligned two column grid">
                            <div class="left aligned column m-column m-opacity">
                                当前第 {{info.pageNum}} 页，
                                共 {{info.pages}} 页，
                                每页显示 {{info.pageSize}} 条
                            </div>
                            <div class="right aligned column m-column m-opacity">
                                我要去
                                <div class="ui input" style="max-width: 70px">
                                    <input id="pageNum" type="number" placeholder="页码" title="输入页码然后回车" v-model="pageSizeInput" @keydown.enter="toPageNum()">
                                </div>
                                页
                            </div>
                        </div>
                    </div>
                </div>
                <div class="five wide column">
                    <!-- 公告 -->
                    <div class="ui segments" v-if="notice!=null && notice.length > 0">
                        <div class="ui secondary segment">
                            <div class="ui two column grid">
                                <div class="column">
                                    <i class="idea icon"></i> 公告
                                </div>
                            </div>
                        </div>
                        <!-- 新域名 http://blog.hanzhe.site 已经启动！ -->
                        <div class="ui blue segment" v-html="notice"></div>
                    </div>
                    <!-- 分类 -->
                    <div class="ui segments" v-if="types.length > 0">
                        <div class="ui secondary segment">
                            <div class="ui two column grid">
                                <div class="column">
                                    <i class="tags icon"></i> 分类浏览
                                </div>
                            </div>
                        </div>
                        <div class="ui teal segment">
                            <router-link tag="a" target="_blank" v-for="(item, index) in types" :key="index"
                                        :to="'/type.html?id=' + item.id" class="ui teal basic left pointing label m-margin-lt-small">
                                <span>{{item.name}}</span>
                                <span>{{item.count}}</span>
                            </router-link>
                        </div>
                    </div>
                    <!-- 推荐 -->
                    <div class="ui segments m-margin-top-large" v-if="commend.length > 0">
                        <div class="ui secondary segment">
                            <i class="bookmark icon"></i> 推荐博客
                        </div>
                        <router-link class="ui segment" v-for="(item, index) in commend" :key="index" style="display: block"
                                tag="a" :to="'/blog/' + item.id + '.html'" target="_blank">
                            <span>{{item.title}}</span>
                        </router-link>
                    </div>
                    <!-- 二维码 -->
                    <h4 class="ui horizontal divider header m-margin-top-large">扫一扫添加微信</h4>
                    <div class="ui card" style="width: 11em; margin: 0px auto;" title="微信扫一扫加我好友">
                        <img :src="wechatQr" class="ui rounded image">
                    </div>
                </div>
            </div>
        </div>
    </main>
</template>

<script>
    import BlogList from "./include/blog-list"
    import {NOT_PRE_PAGE_MESSAGE, NOT_NEXT_PAGE_MESSAGE, PAGE_NUM_FAIL_MESSAGE} from "../service/constant"

    export default {
        name: "main.html",
        components: {"blog-list": BlogList},
        data() {
            return {
                pageNum: 1,
                pageSize: 5,
                info: {},
                commend: [],
                types: [],
                showContent: false,
                pageSizeInput: null,
            }
        },
        created() {
            this.initPage();
            setTimeout(() => {
                window.L2Dwidget.init({
                pluginRootPath: 'static/live2dw/',
                pluginJsPath: 'lib/',
                pluginModelPath: 'live2d-widget-model-hijiki/assets/',
                tagMode: false,
                debug: false,
                model: { jsonPath: '/static/live2dw/live2d-widget-model-hijiki/assets/hijiki.model.json' },
                display: { position: 'left', width: 150, height: 300 },
                mobile: { show: true },
                log: false
                })
            }, 1000)
        },
        methods: {
            // 初始化-重新加载
            async initPage() {
                this.pageSize = this.$common.getField("pageSize");
                let data = {pageNum: this.pageNum, pageSize: this.pageSize};
                let res = await this.$http.get("/index", data);
                this.info = res.data.info;
                this.commend = res.data.commend;
                this.types = res.data.types;
                this.showContent = true;
            },
            // 上一页
            prePage() {
                if (this.info.hasPreviousPage) {
                    this.pageNum--;
                    this.initPage();
                    window.scrollTo(x, y);
                } else this.$message.toast(NOT_PRE_PAGE_MESSAGE);
            },
            // 下一页
            nextPage() {
                if (this.info.hasNextPage) {
                    this.pageNum++;
                    this.initPage();
                    window.scrollTo(x, y);
                } else this.$message.toast(NOT_NEXT_PAGE_MESSAGE);
            },
            // 页码条跳转
            usePage(pageNum) {
                if (pageNum > 0 && pageNum <= this.info.pages){
                    this.pageNum = pageNum;
                    this.initPage();
                    window.scrollTo(x, y);
                } else this.$message.toast(PAGE_NUM_FAIL_MESSAGE);
            },
            // 底部文本框跳转
            toPageNum() {
                this.usePage(this.pageSizeInput);
                this.pageSizeInput = null;
            }
        },
        computed: {
            wechatQr() {
                return this.$common.getField("wechatQr");
            },
            notice() {
                return this.$common.getField("notice");
            }
        }
    }
</script>

<style scoped></style>
