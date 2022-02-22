<!-- 公共导航栏 -->
<template>
    <nav class="ui inverted attached segment m-padded-tb-mini m-shadow-small">
        <div class="ui container">
            <div class="ui inverted secondary stackable menu">
                <!-- 标题 -->
                <router-link :to="isAdmin ? '/admin/blogs.html' : '/index.html'" tag="h2" class="ui teal header item" title="回到首页" style="cursor: pointer">
                    {{title}}
                </router-link>
                <!-- 特殊的返回按钮 { "icon": "reply", "name": "返回", "url": "/" }, -->
                <a class="item m-mobile-hidden m-nav" @click="toBack()" v-if="isAdmin">
                    <i class="icon reply"/> 返回
                </a>
                <!-- 循环输出导航栏 -->
                <router-link v-for="(item, i) in navList" :to="item.url" @click.native="useTab(i)" :class="{'item m-mobile-hidden m-nav': true, 'active': $route.path===item.url}" :key="i">
                    <i :class="'icon ' + item.icon"/> {{item.name}}
                </router-link>
                <!-- 用户页面：搜索框 -->
                <div v-if="!isAdmin" class="right item">
                    <div class="ui icon inverted input">
                        <input type="text" id="title" placeholder="在这里搜索" v-model="searchInput" @keydown.enter="search()">
                        <i class="search link icon" @click="search()"/>
                    </div>
                </div>
                <!-- 管理页面：登录头像、昵称、注销按钮 -->
                <div v-else class="right m-item m-mobile-hide menu" @click="isShowLogout = isShowLogout==='none' ? 'block' : 'none'">
                    <div class="ui dropdown item">
                        <div class="text">
                            <img class="ui avatar image" :src="adminImg"/>
                            <span>{{username}}</span>
                        </div>
                        <i class="dropdown icon"/>
                        <div class="menu" :style="{'display': isShowLogout}" @click="logout()">
                            <a class="item">注销</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 移动端的折叠按钮 -->
        <a class="ui black icon button m-right-top m-mobile-show" @click="isMobile()">
            <i class="sidebar icon"/>
        </a>
    </nav>
</template>

<script>
    export default {
        name: "top",
        data(){
            return {
                nav: {   // Nav导航列表数组
                    user: null,
                    admin: null
                },
                title: "",      // 标题
                navList: [],    // 真正使用的导航列表
                index: 0,       // 控制导航高亮
                isAdmin: false,       // 当前是否为管理管理页面
                isShowNav: false,     // 是否折叠导航(适配移动端)
                isShowLogout: "none", // 注销按钮显示状态
                searchInput: ""
            }
        },
        created(){
            // 读取JSON文件中的导航栏信息存放到nav对象中
            let { userNav, adminNav } = require("../../../static/json/nav.json");
            this.nav.user = userNav;
            this.nav.admin = adminNav;
            // 根据当前地址解析URL
            this.updateNavParam(this.$route.path);
        },
        methods: {
            // 切换标签页
            useTab( index ){
                this.index = index;
                $(".m-nav").addClass("m-mobile-hidden");
                this.isShowNav = false;
            },
            // 控制移动端的菜单隐藏显示
            isMobile(){
                if ( this.isShowNav ){
                    $(".m-nav").addClass("m-mobile-hidden");
                } else $(".m-nav").removeClass("m-mobile-hidden");
                this.isShowNav = !this.isShowNav;
            },
            // 动态更新param的方法
            updateNavParam( path ){
                // 如果地址是Admin开头代表当前是管理页面，否则是用户页面
                if (path.length >= 6 && path.substring(0, 6) === "/admin"){
                    this.title = "管理后台";
                    this.isAdmin = true;
                    this.navList = this.nav.admin;
                } else {
                    // user赋值
                    let title = this.$common.getField("title");
                    this.title = (title==null || title==="") ? "个人博客" : title;
                    this.isAdmin = false;
                    this.navList = this.nav.user;
                }
            },
            // 返回到博客首页
            toBack() {
                this.$message.confirm("确定要退出管理页面？", ()=>this.$router.push('/'));
            },
            // 跳转到搜索页面
            search() {
                if (this.$common.strIsNull(this.searchInput)) {
                    this.$message.toast("请输入搜索内容");
                } else if (this.searchInput.length > 20) {
                    this.$message.toast("你不觉得你的话有点多么");
                } else {
                    let arr = location.href.split("/");
                    let url = `${arr[0]}//${arr[2]}/#/search/${this.searchInput}.html`;
                    window.open(url);
                    // TODO 这里测试反复使用同一个路由会报错，暂时搁置
                    // this.$router.push(`/search/${this.searchInput}.html`)
                }
            },
            logout() {
                sessionStorage.removeItem("token");
                this.$router.push("/index.html");
                this.$message.toast("已成功退出登录");
            }
        },
        computed: {
            adminImg() {
                return this.$common.getField("adminImg");
            },
            username() {
                return this.$common.getField("username");
            }
        },
        watch: {
            // 当URL地址发生改变时检测页面状态
            "$route.path": function (current) {
                this.updateNavParam(current);
            }
        }
    }
</script>

<style scoped>

</style>
