<template>
    <div>
        <!-- 顶部选项卡：博客列表管理 -->
        <div class="ui attached pointing menu">
            <div class="ui container">
                <div class="right menu">
                    <a class="item" @click="switchTab()">发布博客</a>
                    <a class="teal active item">管理列表</a>
                </div>
            </div>
        </div>
        <!-- 主体 -->
        <div class="m-container-small3 m-padded-tb-big">
            <div class="ui container">
                <div class="ui secondary segment form">
                    <input type="hidden" name="page">
                    <div class="inline fields" style="margin-bottom: 0">
                        <div class="field">
                            <input type="text" name="title" placeholder="标题" v-model="tempParam.title">
                        </div>
                        <div class="field">
                            <select class="ui dropdown" :style="{'padding': '0 10px', 'color': color}" v-model="tempParam.typeId">
                                <option value="0" style="display: none">分类</option>
                                <option v-for="(item, index) in types" :key="index" :value="item.id">{{item.name}}</option>
                            </select>
                        </div>
                        <div class="field">
                            <div class="ui checkbox">
                                <input type="checkbox" id="recommend" name="recommend" v-model="tempParam.commend">
                                <label for="recommend">推荐</label>
                            </div>
                        </div>
                        <div class="field">
                            <button type="button" id="search-btn" class="ui mini teal basic button" @click="search()">
                                <i class="search icon"></i> 搜索
                            </button>
                        </div>
                        <div class="field" style="padding-left: 10px">
                            <button type="button" class="ui mini orange basic button" @click="reset()">
                                <i class="sync alternate icon"></i> 重置
                            </button>
                        </div>
                    </div>
                </div>
                <div id="table-container">
                    <table class="ui compact teal table">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>标题</th>
                            <th>分类</th>
                            <th>发布时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in info.list" :key="index">
                                <td>{{index+1}}</td>
                                <td>{{item.title}}</td>
                                <td>{{item.typeName}}</td>
                                <td>{{item.createTime | dateFormat}}</td>
                                <td>
                                    <!-- 编辑按钮 -->
                                    <router-link tag="a" :to="'/admin/editor/' + item.id + '.html'" class="ui mini teal basic button">编辑</router-link>
                                    <!-- 推荐按钮 -->
                                    <a class="ui mini green basic button" v-if="item.commend==0" @click="updateState('commend', item.id, item.commend)">推荐</a>
                                    <a class="ui mini orange basic button" v-else @click="updateState('commend', item.id, item.commend)">撤销</a>
                                    <!-- 发布按钮 -->
                                    <a class="ui mini teal basic button" v-if="item.release == 0" @click="updateState('release', item.id, item.release)">发布</a>
                                    <a class="ui mini orange basic button" v-else @click="updateState('release', item.id, item.release)">下架</a>
                                    <!-- 删除按钮 -->
                                    <a class="ui mini red basic button" @click="deleteBlog(item)">删除</a>
                                </td>
                            </tr>
                        </tbody>
                        <tfoot>
                        <tr>
                            <th colspan="7" style="text-align: center;">
                                <div class="ui mini pagination menu">
                                    <a :class="{'item': true, 'disabled': !info.hasPreviousPage}" @click="prePage()">上一页</a>
                                    <a :class="{'item': true, 'active': item===pageNum}" v-for="(item, index) in info.navigatepageNums" :key="index" v-text="item" @click="usePage(item)" />
                                    <a :class="{'item': true, 'disabled': !info.hasNextPage}" @click="nextPage()">下一页</a>
                                </div>
                            </th>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {NOT_PRE_PAGE_MESSAGE, NOT_NEXT_PAGE_MESSAGE, PAGE_NUM_FAIL_MESSAGE} from "../../service/constant"

    export default {
        name: "blogs",
        data(){
            return {
                pageNum: 1,
                pageSize: 5,
                tempParam: {
                    title: "",
                    typeId: 0,
                    commend: false
                },
                queryParam: {
                    title: "",
                    typeId: 0,
                    commend: false
                },
                info: {list: []},
                types: [],
                color: "#b6b6b6"
            }
        },
        created() {
            this.initPage();
        },
        methods: {
            initPage() {
                this.pageSize = this.$common.getField("pageSize");
                let data = {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    title: this.queryParam.title,
                    typeId: this.queryParam.typeId,
                    commend: this.queryParam.commend ? 1 : 0,
                }
                this.$http.get("/admin/blog", data).then(res=>{
                    this.info = res.data.info;
                    this.types = res.data.types;
                });
            },
            // 切换顶部Tab
            switchTab() {
                this.$router.push("/admin/release.html");
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
            // 条件查询
            search() {
                this.queryParam = this.tempParam;
                this.pageNum = 1;
                this.initPage();
            },
            // 重置搜索条件状态
            reset() {
                this.queryParam = { title: "", typeId: 0, commend: false };
                this.tempParam = { title: "", typeId: 0, commend: false };
                this.pageNum = 1;
                this.initPage();
            },
            // 删除博客
            deleteBlog(blog) {
                this.$message.confirm(`你真的要删除【${blog.title}】吗？`, ()=>{
                    this.$http.delete("/admin/blog", {id: blog.id}).then(res=>{
                        this.$message.toast(res.msg);
                        this.initPage();
                    });
                })
            },
            // 更新博客状态
            updateState(name, id, state) {
                this.$http.put(`/admin/blog/${id}/${name}/${state == 0 ? 1 : 0}`).then(res=>{
                    this.$message.toast(res.msg);
                    this.initPage();
                });
            }
        },
        watch: {
            "tempParam.typeId": function (before) {
                console.log(before);
                this.color = before == 0 ? "#b6b6b6" : "black"
            }
        }
    }
</script>

<style scoped>
</style>
