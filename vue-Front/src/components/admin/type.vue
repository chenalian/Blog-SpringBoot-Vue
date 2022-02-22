<template>
    <div class="m-container-small m-padded-tb-big">
        <div style="height: 20px;"></div>
        <div class="ui container">
            <table class="ui compact teal table">
                <thead>
                <tr>
                    <th></th>
                    <th>名称</th>
                    <th>创建时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(item, index) in info.list" :key="index">
                    <td>{{index+1}}</td>
                    <td>{{item.name}}</td>
                    <td>{{item.createTime | dateFormat}}</td>
                    <td>
                        <a class="ui mini teal basic button" @click="updateType(item)">编辑</a>
                        <a class="ui mini red basic button" @click="deleteType(item)">删除</a>
                    </td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <th colspan="6">
                        <div class="ui mini pagination menu">
                            <a :class="{'item': true, 'disabled': !info.hasPreviousPage}" @click="prePage()">上一页</a>
                            <a :class="{'item': true, 'disabled': !info.hasNextPage}" @click="nextPage()">下一页</a>
                        </div>
                        <a class="ui mini right floated teal basic button" @click="addType()">新增</a>
                    </th>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</template>

<script>
    import {NOT_PRE_PAGE_MESSAGE, NOT_NEXT_PAGE_MESSAGE} from "../../service/constant"

    export default {
        name: "admin-type",
        data() {
            return {
                pageNum: 1,
                pageSize: 5,
                info: {list: []}
            }
        },
        created() {
            this.pageSize = this.$common.getField("pageSize");
            this.initPage();
        },
        methods: {
            initPage() {
                let data = {pageNum: this.pageNum, pageSize: this.pageSize}
                this.$http.get("/admin/type", data).then(res=>{
                    this.info = res.data.info;
                })
            },
            // 上一页
            prePage() {
                if (this.info.hasPreviousPage) {
                    this.pageNum--;
                    this.initPage();
                } else this.$message.toast(NOT_PRE_PAGE_MESSAGE);
            },
            // 下一页
            nextPage() {
                if (this.info.hasNextPage) {
                    this.pageNum++;
                    this.initPage();
                } else this.$message.toast(NOT_NEXT_PAGE_MESSAGE);
            },
            // 添加分类
            addType() {
                this.$message.prompt("请输入新增分类名称", value=>{
                    this.$http.post("/admin/type", {name: value}).then(res=>{
                        setTimeout(()=>{
                            this.$message.toast(res.msg);
                            this.initPage();
                        }, 100);
                    })
                })
            },
            // 更新分类
            updateType(item) {
                this.$message.prompt("将【" + item.name + "】修改为", (value)=>{
                    let data = {id: item.id, name: value};
                    this.$http.put("/admin/type", data).then(res=>{
                        setTimeout(()=>{
                            this.$message.toast(res.msg);
                            this.initPage();
                        }, 100)
                    })
                })
            },
            // 删除分类
            deleteType(item) {
                this.$message.confirm("你确定要删除【" + item.name + "】分类吗", ()=>{
                    this.$http.delete("/admin/type/" + item.id).then(res=>{
                        setTimeout(()=>{
                            this.$message.toast(res.msg);
                            this.initPage();
                        }, 100)
                    })
                })
            }
        }
    }
</script>

<style scoped>

</style>
