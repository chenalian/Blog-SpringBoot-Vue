<template>
    <div class="ui container">
        <!-- 列表展示 -->
        <table class="ui compact teal table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>名称</th>
                    <th>URL地址</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(item, index) in list" :key="index">
                    <td>{{index+1}}</td>
                    <td>{{item.name}}</td>
                    <td>{{item.url}}</td>
                    <td>
                        <a class="ui mini yellow basic button" @click="reset(item)">初始化</a>
                        <a class="ui mini teal basic button" @click="useItem(item, index+1)">编辑</a>
                    </td>
                </tr>
            </tbody>
        </table>
        <!-- 编辑区域 -->
        <div v-if="index != null" style="margin: 50px 0">
            <div class="ui teal attached segment">
                <div class="ui middle aligned two column grid">
                    <div class="column">
                        <h3 class="ui teal header">友链编辑 &nbsp; # {{index}}</h3>
                    </div>
                </div>
            </div>
            <div class="ui buttom attached segment">
                <form class="ui form">
                    <div class="field">
                        <label>友链名称</label>
                        <input type="text" v-model="item.name">
                    </div>
                    <div class="field">
                        <label>URL地址</label>
                        <input type="text" v-model="item.url">
                    </div>
                </form>
                <div class="m-button">
                    <button class="small ui button" @click="cancel()">取消</button>
                    <button class="small ui button" @click="submit()">修改</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "link-settings",
    data() {
        return {
            list: [],
            index: null,
            item: null
        }
    },
    created() {
        this.initList();
    },
    methods: {
        initList() {
            this.$http.get("/admin/link").then(res=>this.list = res.data.list);
        },
        // 选择编辑
        useItem(item, index) {
            let obj = {};
            Object.assign(obj, item);
            this.item = obj;
            this.index = index;
        },
        // 放弃编辑
        cancel() {
            this.item = null;
            this.index = null;
        },
        // 提交修改
        submit() {
            if(this.chechForm()) {
                this.$http.put("/admin/link", this.item).then(res=>{
                    this.$message.toast(res.msg);
                    this.initList();
                    this.cancel();
                })
            }
        },
        // 表单校验
        chechForm() {
            if( this.$common.strIsNull(this.item.name) ) {
                this.$message.toast("友链名称不能为空");
                return false;
            } else if ( this.$common.strIsNull(this.item.url) ) {
                this.$message.toast("链接地址不能为空");
                return false;
            } else return true;
        },
        reset(item) {
            this.$message.confirm("你确定要初始化【" + item.name + "】吗", ()=>{
                this.item = {
                    id: item.id,
                    name: "联系站长申请友链",
                    url: "#"
                }
                this.submit();
            })
        }
    }
}
</script>

<style scoped>
    .m-button {
        margin: 20px 0 10px 0;
        text-align: right;
    }

    .m-button button {
        margin-left: 10px !important;
    }
</style>
