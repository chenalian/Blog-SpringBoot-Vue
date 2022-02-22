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
                    <div class="field">
                        <textarea placeholder="Hi~ 来说点什么吧！" rows="5" v-model="param.content"/>
                    </div>
                    <div class="fields">
                        <div class="field m-mobile-wide m-margin-bottom-small">
                            <div class="ui left icon input">
                                <i class="user icon"/>
                                <input type="text" placeholder="昵称 1~10个字符" v-model="param.name">
                            </div>
                        </div>
                        <div class="field m-mobile-wide m-margin-bottom-small">
                            <div class="ui left icon input">
                                <i class="mail icon"/>
                                <input type="text" placeholder="邮箱，别乱写哦" v-model="param.email"/>
                            </div>
                        </div>
                        <div class="field m-mobile-wide m-margin-bottom-small">
                            <button class="ui teal button m-mobile-wide sub" @click="subMessage()">
                                <i class="edit icon"/> 点击留言
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
                <div v-if="info.list.length < 1" style="padding: 10px 0; text-align: center;">
                    还没有人来留言
                </div>
                <div class="ui teal segment animated fadeIn" v-for="(item, index) in info.list" :key="index">
                    <div class="ui comments">
                        <div class="comment">
                            <a class="avatar">
                                <img :src="guestImg">
                            </a>
                            <div class="content">
                                <a class="author">{{item.name}}</a>
                                <div class="metadata">
                                    <span class="date">{{item.createTime | dateFormat}}</span>
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
                                            <span class="date">{{item2.createTime | dateFormat}}</span>
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
    import {NOT_PRE_PAGE_MESSAGE, NOT_NEXT_PAGE_MESSAGE} from "../service/constant"

    export default {
        name: "message",
        data() {
            return {
                info: {},
                showContent: false,
                pageNum: 1,
                param: { content: "", name: "", email: "" }
            }
        },
        created() {
            this.initPage();
        },
        methods: {
            initPage() {
                let data = {pageNum: this.pageNum, pageSize: 10};
                this.$http.get("/message", data).then(res => {
                    this.info = res.data.info;
                    this.showContent = true;
                })
            },
            // 提交留言
            subMessage() {
                if (this.checkForm()) {
                    // 执行提交逻辑
                    this.$http.post("/message", this.param).then(res=>{
                        this.param.content = "";
                        this.$message.toast(res.msg);
                        // 设置延时，先看到提示然后在看到留言出来，效果比较好
                        setTimeout(()=>this.initPage(), 1000)
                    });
                }
            },
            // 校验表单内容
            checkForm() {
                let reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
                if (this.$common.strIsNull(this.param.content)) {
                    this.$message.toast("留言内容不能为空");
                    return false;
                } else if (this.param.content.length > 300) {
                    this.$message.toast("留言字数不能超过300");
                    return false;
                } else if (this.$common.strIsNull(this.param.name)) {
                    this.$message.toast("昵称不能为空");
                    return false;
                } else if (this.$common.strIsNull(this.param.email)) {
                    this.$message.toast("邮箱不能为空");
                    return false;
                } else if(!reg.test(this.param.email)) {
                    this.$message.toast("邮箱格式不符合规范");
                    return false
                } else return true;
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

</style>
