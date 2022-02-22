<template>
    <div class="m-container-small m-padded-tb-massive" style="max-width: 28em !important;">
        <br /> <br /> <br />
        <div class="ur container">
            <div class="ui middle aligned center aligned grid">
                <div class="column">
                    <h2 class="ui teal image header">
                        <div class="content">管理后台登录</div>
                    </h2>
                    <div class="ui large form">
                        <div class="ui segment">
                            <div class="field">
                                <div class="ui left icon input">
                                    <i class="user icon"></i>
                                    <input type="text" placeholder="用户名" v-model="data.account">
                                </div>
                            </div>
                            <div class="field">
                                <div class="ui left icon input">
                                    <i class="lock icon"></i>
                                    <input type="password" placeholder="密码" v-model="data.password" @keydown.enter="login()">
                                </div>
                            </div>
                            <button class="ui fluid large teal submit button" @click="login()">登 录</button>
                        </div>
                        <div class="ui error mini message"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "login",
        data() {
            return {
                data: {
                    account: "",
                    password: ""
                }
            }
        },
        methods: {
            login() {
                if(this.checkForm()){
                    this.$http.post("/login", this.data).then(res=>{
                        this.$message.toast("登录成功");
                        this.$router.push("/admin/blogs.html");
                    })
                }
            },
            checkForm() {
                if(this.$common.strIsNull(this.data.account)) {
                    this.$message.toast("账号不能为空");
                    return false;
                } else if(this.$common.strIsNull(this.data.password)) {
                    this.$message.toast("密码不能为空");
                    return false;
                } else return true;
            }
        }
    }
</script>

<style scoped>
</style>
