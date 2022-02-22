<template>
    <div class="m-container-small m-padded-tb-big">
        <div class="ui container">
            <!-- 头部 搜索结果标题 -->
            <div class="ui top attached segment">
                <div class="ui middle aligned two column grid">
                    <div class="column" style="width: 80%">
                        <h3 class="ui teal header">“{{searchInput}}” 的搜索结果为：</h3>
                    </div>
                    <div class="right aligned column" style="width: 20%">
                        共 <h2 class="ui orange header m-inline-block m-text-thin">{{info.size}}</h2> 个
                    </div>
                </div>
            </div>
            <!-- 博客列表 -->
            <div class="ui top attached teal segment">
                <blog-list :list="info.list" desc="没有找到符合条件的博客"></blog-list>
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
</template>

<script>
    import BlogList from "./include/blog-list"
    import {NOT_PRE_PAGE_MESSAGE, NOT_NEXT_PAGE_MESSAGE, PAGE_NUM_FAIL_MESSAGE} from "../service/constant"

    export default {
        name: "search",
        components: { "blog-list": BlogList },
        data() {
            return {
                searchInput: "",
                pageSizeInput: null,
                showContent: false,
                pageSize: 1,
                pageNum: 1,
                info: {}
            }
        },
        created() {
            this.initPage();
        },
        methods: {
            initPage() {
                this.searchInput = this.$route.params.searchInput;
                this.pageSize = this.$common.getField("pageSize");
                let data = {pageNum: this.pageNum, pageSize: this.pageSize, "searchInput": this.searchInput};
                this.$http.get("/search", data).then(res=>{
                    this.info = res.data.info;
                })
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
        }
    }
</script>

<style scoped>

</style>
