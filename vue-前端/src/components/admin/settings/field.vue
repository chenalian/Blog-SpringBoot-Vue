<template>
    <div>
        <div class="settings">
            <div class="ui labeled action input" style="margin-bottom: 30px">
                <div class="ui label" style="width: 100px;">图片URL</div>
                <input type="text" style="width: 360px" placeholder="在这里上传图片获取URL" v-model="imgUrl">
                <div class="ui button primary" @click="uploadBtn()">上传</div>
            </div>
            <div :class="{'ui labeled action input ': true, 'm-hidden': item.id=='none'}" v-for="(item, index) in settingList" :key="index">
                <div class="ui label">{{item.name}}</div>
                <input type="text" :placeholder="item.field" v-model="item.value">
                <div class="ui button primary" @click="updateField(item)">修改</div>
            </div>
        </div>
        <input id="fileInput" type="file" style="opacity: 0; pointer-events: none;" accept=".jpg,.png.jpeg" @change="upload($event)">
    </div>
</template>

<script>
    export default {
        name: "field-settings",
        data() {
            return {
                imgUrl: "",
                settingList: [],
            }
        },
        created() {
            this.$http.get("/admin/field").then(res=>{
                this.settingList = res.data.fields
                // 为了页面样式好看，如果是单数的话就补一个
                if(this.settingList.length % 2 == 1) {
                    this.settingList.push({id: "none", field: "占位置", name: "样式美化", value: "CSS功底太差，投机取巧"})
                }
            })
        },
        methods: {
            // 更新字段方法
            updateField(item) {
                this.$http.put("/admin/field", item).then(res=>{
                    this.$message.toast(res.msg);
                })
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
                    this.imgUrl = res.data.path;
                })
            },
        }
    }
</script>

<style scoped>
    .settings {
        text-align: center;
    }
    .settings .ui {
        margin: 5px 20px;
    }
    .settings .ui .label {
        width: 110px;
        text-align: center;
    }
    .settings .ui input {
        width: 200px;
    }
    .m-hidden {
        pointer-events: none;
        opacity: 0;
    }
</style>
