<!-- 留言板 -->
<template>
    <div class="m-container-small m-padded-tb-big m-min-height">
        <div v-if="showContent" class="ui containe">
            <!--标题-留言板-->
            <div class="ui top attached segment">
                <div class="ui middle aligned two column grid">
                    <div class="column">
                        <h3 class="ui teal header">留言板</h3>
                    </div>
                    <div class="right aligned column">
                        共 <h3 class="ui orange header m-inline-block">12</h3> 条留言
                    </div>
                </div>
            </div>
            <!--发言表单-->
            <div class="ui attached segment">
                <div class="ui form">
                    <div>
                        <textarea :placeholder="placeholder" rows="5" v-model="content"/>
                    </div>
                    <div>
                        <div class="m-mobile-wide m-margin-bottom-small" style="text-align: right;">
                            <button class="ui teal button m-mobile-wide sub" @click="subMessage()">
                                <i class="edit icon"/> 回复留言
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <br/> <br/>
            <!--标题--留言展示-->
            <div class="ui attached top segment">
                <div class="ui middle aligned two column grid">
                    <div class="column">
                        <h3 class="ui teal header">留言展示</h3>
                    </div>
                </div>
            </div>
            <!-- 留言展示 -->
            <div class="ui attached segment">
                <!-- 循环遍历每个留言 -->
                <div class="ui teal segment" v-for="(item, index) in info.list" :key="index">
                    <div class="ui comments">
                        <div class="comment">
                            <a class="avatar">
                                <img :src="guestImg">
                            </a>
                            <div class="content">
                                <a class="author" @click="use(item)">{{item.name}}</a>
                                <div class="metadata">
                                    <span>{{item.createTime | dateFormat}}</span>
                                    <span class="delete" @click="deleteMessage(item)">删除</span>
                                </div>
                                <div class="text">
                                    <p>{{item.content}}</p>
                                </div>
                            </div>
                            <!-- 循环遍历每个留言的二级留言 -->
                            <div class="comments" v-for="(item2, index) in item.children" :key="index">
                                <div class="comment">
                                    <a class="avatar">
                                        <img :src="adminImg">
                                    </a>
                                    <div class="content">
                                        <a class="author">{{item2.name}}</a>
                                        <div class="metadata">
                                            <span>{{item2.createTime | dateFormat}}</span>
                                            <span class="delete" @click="deleteMessage(item2)">删除</span>
                                        </div>
                                        <div class="text">{{item2.content}}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--分页-->
            <div class="ui bottom attached segment">
                <div class="ui middle aligned two column grid">
                    <div class="column">
                        <a class="ui mini teal basic button" @click="prePage()">上一页</a>
                    </div>
                    <div class="right aligned column">
                        <a class="ui mini teal basic button" @click="nextPage()">下一页</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {NOT_PRE_PAGE_MESSAGE, NOT_NEXT_PAGE_MESSAGE} from "../../service/constant"

    export default {
        name: "message",
        data() {
            return {
                info: {},
                showContent: false,
                pageNum: 1,
                id: "0",
                name: "",
                placeholder: "点击用户昵称进行回复",
                content: ""
            }
        },
        created() {
            this.initPage();
        },
        methods: {
            initPage() {
                let data = {pageNum: this.pageNum, pageSize: 10};
                this.$http.get("/admin/message", data).then(res => {
                    this.info = res.data.info;
                    this.showContent = true;
                })
            },
            // 提交留言
            subMessage() {
                if (this.checkForm()) {
                    // 执行提交逻辑
                    let data = {id: this.id, content: this.content};
                    this.$http.post("/admin/message", data).then(res=>{
                        this.$message.toast(res.msg);
                        this.initPage();
                        this.resetForm();
                        setTimeout(()=>this.initPage(), 1000)
                    });
                }
            },
            // 表单校验
            checkForm() {
                if ( this.id == "0" ) {
                    this.$message.toast("请选择您要回复的人");
                    return false;
                } else if (this.$common.strIsNull(this.content)) {
                    this.$message.toast("回复内容不能为空");
                    return false;
                } else if (this.content.length > 300) {
                    this.$message.toast("回复内容应在300字以内");
                    return false;
                } else return true;
            },
            // 重置表单
            resetForm() {
                this.id = "0";
                this.name = "";
                this.placeholder = "点击用户昵称进行回复";
                this.content = "";
            },
            // 选择要回复的人
            use(item) {
                this.id = item.id;
                this.name = item.name;
                this.placeholder = "回复 " + item.name;
                window.scrollTo(x, y);
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
            deleteMessage(item) {
                let title = item.children.length == 0 ?
                        "你确定要删除这条留言吗" :
                        "删除该留言会连同下面的回复留言一同删除，确定要继续删除吗";
                // 如果有子留言就连同子留言一同删除，拼接ID字符串通过 "-" 进行拼接
                let ids = item.id;
                item.children.forEach(i=>ids+=("-" + i.id))
                this.$message.confirm(title, ()=>{
                    this.$http.delete("/admin/message", {ids}).then(res=>{
                        this.$message.toast(res.msg);
                        this.initPage();
                    })
                })
            }
        },
        computed: {
            adminImg() {
                return this.$common.getField("adminImg");
            },
            guestImg() {
                return this.$common.getField("guestImg");
            }
        }
    }
</script>

<style scoped>
    .delete:hover {
        cursor: pointer;
        color: rgb(221, 29, 29);
    }
</style>
