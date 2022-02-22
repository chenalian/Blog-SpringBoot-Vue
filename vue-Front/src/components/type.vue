<!-- 分类博客详情 -->
<template>
    <div class="m-container-small m-padded-tb-big">
        <!-- 顶部分类 -->
        <div class="ui container" style="min-height: 800px">
            <!-- 标题 -->
            <div class="ui top attached segment">
                <div class="ui middle aligned two column grid">
                    <div class="column">
                        <h3 class="ui teal header">分类博客详情</h3>
                    </div>
                    <div class="right aligned column">
                        共 <h2 class="ui orange header m-inline-block m-text-thin">{{types.length}}</h2> 个分类
                    </div>
                </div>
            </div>
            <!-- 内容 -->
            <div class="ui attached segment m-padded-tb-large" style="padding-top: 15px !important; padding-bottom: 15px !important;">
                <div v-if="types.length < 1" style="text-align: center; padding: 10px 0">当前没有分类信息</div>
                <!-- 循环输出分类标签 -->
                <div class="ui labeled button m-margin-tb-tiny" style="margin-right: 10px" v-for="(item, index) in types" :key="index" @click="useType(item.id)">
                    <a :class="{'ui basic button': true, 'teal': typeId===item.id}">{{item.name}}</a>
                    <div :class="{'ui basic left pointing label': true, 'teal': typeId===item.id}">{{item.count}}</div>
                </div>
            </div>

            <div v-if="showContent && types.length > 0" class="animated bounceInUp" style="margin-top: 1rem">
                <!-- 博客列表 -->
                <div class="ui top attached teal segment">
                    <blog-list :list="info.list" desc="该分类下没有博客"></blog-list>
                </div>
                <!-- 分页 -->
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
        </div>
    </div>
</template>

<script>
    import BlogList from "./include/blog-list"
    import {NOT_PRE_PAGE_MESSAGE, NOT_NEXT_PAGE_MESSAGE, PAGE_NUM_FAIL_MESSAGE} from "../service/constant"

    export default {
        name: "type",
        components: { "blog-list": BlogList },
        data() {
            return {
                pageNum: 1,
                pageSize: 5,
                types: [],
                info: {},
                typeId: null,
                pageSizeInput: null,
                // 更好的视觉效果：闲的没事儿加的变量，可以取消
                showContent: false
            }
        },
        async created() {
            await this.initPage();
            this.getBlogList();
        },
        methods: {
            // 初始化-重新加载
            async initPage() {
                let id = this.$route.query.id;
                this.pageSize = this.$common.getField("pageSize");
                let res = await this.$http.get("/type");
                this.types = res.data.types;
                if (this.types.length < 1) {
                    return;
                }
                this.typeId = id == null ? this.types[0].id : id;
            },
            // 获取博客列表
            getBlogList() {
                this.showContent = false;
                let data = {pageNum: this.pageNum, pageSize: this.pageSize};
                this.$http.get("/type/" + this.typeId, data).then(res=>{
                    this.info = res.data.info;
                    this.showContent = true;
                })
            },
            // 切换当前选择分类
            useType(typeId) {
                this.typeId = typeId;
                this.pageNum = 1;
                this.getBlogList();
            },
            // 上一页
            prePage() {
                if (this.info.hasPreviousPage) {
                    this.pageNum--;
                    this.getBlogList();
                    window.scrollTo(x, y);
                } else this.$message.toast(NOT_PRE_PAGE_MESSAGE);
            },
            // 下一页
            nextPage() {
                if (this.info.hasNextPage) {
                    this.pageNum++;
                    this.getBlogList();
                    window.scrollTo(x, y);
                } else this.$message.toast(NOT_NEXT_PAGE_MESSAGE);
            },
            // 页码条跳转
            usePage(pageNum) {
                if (pageNum > 0 && pageNum <= this.info.pages){
                    this.pageNum = pageNum;
                    this.getBlogList();
                    window.scrollTo(x, y);
                } else this.$message.toast(PAGE_NUM_FAIL_MESSAGE);
            },
            // 底部文本框跳转
            toPageNum() {
                this.usePage(this.pageSizeInput);
                this.pageSizeInput = null;
            }
        }
    }
</script>

<style scoped>

</style>
