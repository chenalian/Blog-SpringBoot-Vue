<template>
    <div>
        <!-- 页面主体 -->
        <div  class="m-container m-padded-tb-big">
            <div class="ui container">
                
                <!-- 整体表单 -->
                <div class="ui form">
                    <div class="two fields">
                        <!-- 标题 -->
                        <div class="field">
                            <div class="ui left labeled input">
                                <label class="ui compact teal basic label">标 题</label>
                                <input type="text" name="firstPicture" placeholder="博客标题" v-model="queryParam.title">
                            </div>
                        </div>
                        <!-- 分类 -->
                        <div class="field">
                            <div class="ui left labeled action input">
                                <label class="ui compact teal basic label">分 类</label>
                                <select class="ui dropdown" :style="{'padding': '0 10px', 'color': color}" v-model="queryParam.typeId">
                                    <option value="0" style="display: none">请选择分类</option>
                                    <option v-for="(item, index) in types" :key="index" :value="item.id">{{item.name}}</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="field">
                        <div class="ui left labeled input">
                            <label class="ui teal basic label">略 缩 图</label>
                            <input type="text" name="firstPicture" placeholder="略缩图引用地址" v-model="queryParam.imgUrl">
                            <button type="button" class="ui teal button" style="margin-left: 5px" @click="uploadBtn()">上传略缩图</button>
                        </div>
                    </div>
                   
                    
                    
                   
                   
                </div>
                <br/>
                 <!-- 此处为markdown编辑器 -->
                    <div class="field">
                        <mavon-editor ref="md" v-model="queryParam.content" :toolbars="mdSettings" @imgAdd="mdUpload"/>
                    </div>
                      <br/>
                     <div class="inline fields" >
                        <div class="field" style="float:left;margin-right:10px;">
                            <div class="ui checkbox">
                                <input type="checkbox" id="original" class="hidden" v-model="queryParam.origin">
                                <label for="original">原创</label>
                            </div>
                        </div>
                        <div class="field" style="float:left">
                            <div class="ui checkbox">
                                <input type="checkbox" id="appreciation" class="hidden" v-model="queryParam.reward">
                                <label for="appreciation">赞赏</label>
                            </div>
                        </div>
                    </div>
                    <!-- <br/> -->
                     <!-- <div class="ui error message"></div> -->
                    <div class="ui right aligned container">
                        <button type="button" class="ui button" @click="cancel()">放弃修改</button>
                        <button type="button" class="ui button green" @click="update()">提交修改</button>
                    </div>
            </div>
            <input id="fileInput" type="file" style="opacity: 0; pointer-events: none;" accept=".jpg,.png.jpeg" @change="upload($event)">
        </div>
         
    </div>

    
</template>

<script>
    import {MD_SETTING} from "./md-settings";

    export default {
        name: "release",
        data(){
            return {
                mdSettings: MD_SETTING,
                types: [],
                queryParam: { title: "", typeId: 0, imgUrl: "", content: "", origin: null, reward: null },
                color: "#b6b6b6",
            }
        },
        created() {
            let id = this.$route.params.id;
            this.$http.get("/admin/blog/" + id).then(res=>{
                this.types = res.data.types;
                this.queryParam = res.data.blog;
                // 这里动一下手脚，把数字改为布尔值
                this.queryParam.origin = this.queryParam.origin == 1;
                this.queryParam.reward = this.queryParam.reward == 1;
            })
        },
        methods: {
            // 放弃修改
            cancel() {
                this.$message.confirm("确定要放弃编辑吗？", ()=>{
                    this.$router.push("/admin/blogs.html")
                })
            },
            // 提交修改
            update() {
                if(this.checkFrom()) {
                    let blog = this.blogConvert(this.queryParam);
                    this.$http.put("/admin/blog", blog).then(res=>{
                        this.$router.push("/admin/blogs.html")
                        this.$message.toast(res.msg);
                    })
                }
            },
            // 点击上传按钮时获取到File的对象，触发File的文件选择器
            uploadBtn() {
                let input = document.getElementById("fileInput");
                input.click()
            },
            // 获取文件并上传
            upload(e) {
                let file = e.target.files[0];
                this.$http.upload("/admin/upload", file).then(res=>{
                    this.$message.toast(res.msg);
                    this.queryParam.imgUrl = res.data.path;
                })
            },
            // Markdown上传文件
            mdUpload(index, file) {
                this.$http.upload("/admin/upload", file).then(res=>{
                    this.$refs.md.$img2Url(index, res.data.path);
                })
            },
            // 表单校验
            checkFrom() {
                if (this.$common.strIsNull(this.queryParam.title)) {
                    this.$message.toast("您还未填写标题")
                    return false;
                } else if (this.queryParam.typeId == 0) {
                    this.$message.toast("您还未选择分类")
                    return false;
                } else if (this.$common.strIsNull(this.queryParam.imgUrl)) {
                    this.$message.toast("您还未设置略缩图")
                    return false;
                } else if (this.$common.strIsNull(this.queryParam.content)) {
                    this.$message.toast("博客内容不能为空")
                    return false;
                } return true;
            },
            // 参数转换
            blogConvert(param) {
                let blog = Object.assign({}, param);
                blog.origin = this.boolToNumber(param.origin);
                blog.reward = this.boolToNumber(param.reward);
                return blog;
            },
            // 布尔值转数字
            boolToNumber(bool) {
                return bool ? 1 : 0;
            }
        },
        watch: {
            "queryParam.typeId": function () {
                this.color = "black"
            }
        }
    }
</script>

<style scoped>
    textarea {
    height: 100%;
    min-height: 100%;
    max-height: 100%;
}
</style>
