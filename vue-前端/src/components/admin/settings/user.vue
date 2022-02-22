<template>
    <div class="user">
        <div class="ui labeled action input" style="margin-bottom: 15px">
            <div class="ui label">登录账号</div>
            <input type="text" placeholder="account" v-model="account">
            <div class="ui button primary" @click="updateAccount()">修改</div>
        </div>
        <br /> <br />
        <div class="ui labeled action input" style="margin-bottom: 15px">
            <div class="ui label">登录密码</div>
            <input type="password" placeholder="password" v-model="password">
            <div class="ui button primary" @click="updatePassword()">修改</div>
        </div>
    </div>
</template>

<script>
export default {
    name: "user-settings",
    data() {
        return {
            account: null,
            password: null
        }
    },
    created() {
        this.$http.get("/admin/field/account").then(res=>this.account=res.data.value)
    },
    methods: {
        updateAccount() {
            if(!this.$common.strIsNull(this.account)) {
                this.$http.put("/admin/account", {account: this.account}).then(res=>{
                    this.$message.toast(res.msg);
                })
            }
        },
        updatePassword() {
            if(!this.$common.strIsNull(this.password)) {
                this.$http.put("/admin/password", {password: this.password}).then(res=>{
                    this.$message.toast(res.msg);
                })
            }
        }
    }
}
</script>

<style scoped>
    .user {
        text-align: center;
    }
    .user input {
        width: 350px;
    }
</style>
