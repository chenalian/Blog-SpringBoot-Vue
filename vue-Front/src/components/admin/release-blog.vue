<template>
    <div>
        <!-- 顶部选项卡：发布博客 -->
        <div class="ui attached pointing menu">
            <div class="ui container">
                <div class="right menu">
                    <a class="teal active item">发布博客</a>
                    <a class="item" @click="switchTab()">管理列表</a>
                </div>
            </div>
        </div>
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
                <div class="field">
                        <mavon-editor ref="md" v-model="queryParam.content" :toolbars="mdSettings" @imgAdd="mdUpload" />
                    </div>
                     <br/>

                     <div class="inline fields">
                        <div class="field" style="float:left;">
                            <div class="ui checkbox">
                                <input type="checkbox" id="original" class="hidden" v-model="queryParam.origin">
                                <label for="original">原创</label>
                            </div>
                        </div>
                        <div class="field" style="float:left;margin-left:20px;">
                            <div class="ui checkbox">
                                <input type="checkbox" id="recommend" class="hidden" v-model="queryParam.commend">
                                <label for="recommend">推荐</label>
                            </div>
                        </div>
                        <div class="field" style="float:left;margin-left:20px;">
                            <div class="ui checkbox" >
                                <input type="checkbox" id="appreciation" class="hidden" v-model="queryParam.reward">
                                <label for="appreciation">赞赏</label>
                            </div>
                        </div>
                        <div class="field" style="float:left;margin-left:20px;">
                            <div class="ui checkbox">
                                <input type="checkbox" id="release" class="hidden" v-model="queryParam.release">
                                <label for="release">发布</label>
                            </div>
                        </div>
                    </div>
                    <!-- <div class="ui error message"></div> -->
                    <div class="ui right aligned container">
                        <button type="button" class="ui button" @click="switchTab()">返回列表</button>
                        <!-- 保存时提示 草稿仅保存在本地，不会进行网络同步 -->
                        <button type="button" id="save-btn" class="ui teal button" @click="readLocal()">读取草稿</button>
                        <button type="button" id="save-btn" class="ui secondary button" @click="saveLocal()">保存草稿</button>
                        <button type="button" class="ui button green" @click="release()">点击发布</button>
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
                queryParam: { title: "", typeId: 0, imgUrl: "", content: "", origin: true, commend: false, reward: true, release: true },
                color: "#b6b6b6",
            }
        },
        created() {
            this.$http.get("/type").then(res=>{
                this.types = res.data.types;
            })
        },
        methods: {
            // 离开页面时的保存提示
            switchTab() {
                this.$message.confirm("确定要离开页面吗？离开后数据不会保存", ()=>{
                    this.$router.push("/admin/blogs.html");
                })
            },
            // 保存到本地
            saveLocal() {
                let json = localStorage.getItem("blog");
                if( !this.$common.strIsNull(json) ) {
                    this.$message.confirm("新草稿会覆盖旧草稿，确定继续吗？", ()=>{
                        setTimeout(this.save, 300)
                    })
                } else this.save();
            },
            save() {
                let json = JSON.stringify(this.queryParam);
                localStorage.setItem("blog", json);
                this.$message.toast("草稿保存成功 ( 仅在本地有效 )");
            },
            // 读取保存的内容
            readLocal() {
                let json = localStorage.getItem("blog");
                if( !this.$common.strIsNull(json) ) {
                    // 防止分类被删除不存在
                    let obj = JSON.parse(json);
                    let type = this.types.find(item=>item.id==obj.typeId);
                    obj.typeId = type == null ? 0 : type.id;
                    // 稍稍加那么一点延时看起来更有感觉
                    setTimeout(()=>this.queryParam = obj, 300)
                } else this.$message.toast("您还没有保存过草稿")
            },
            // 发布博客
            release() {
                if( this.checkFrom() ) {
                    let blog = this.blogConvert(this.queryParam);
                    this.$http.post("/admin/blog", blog).then(res=>{
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
                blog.commend = this.boolToNumber(param.commend);
                blog.origin = this.boolToNumber(param.origin);
                blog.release = this.boolToNumber(param.release);
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

</style>
