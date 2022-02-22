<template>
    <div id="app">
        <!-- 登录后台时的登录页面是个特殊的页面，要求页面上除开登录框之外不允许拥有其他组件 -->
        <top v-if="!isLoginPage && showContent" />
        <router-view v-if="showContent" style="min-height: 500px;" />
        <bottom v-if="!isLoginPage && showContent" />

        <!-- 行走miao -->
         <div id="marco">
			<div id="gato"></div>
	    </div>
    </div>
</template>

<script>
    import top from "./components/include/top"
    import bottom from "./components/include/bottom"
    // import L2Dwidget from "../static/live2dw/lib/L2Dwidget.min"
    export default {
        name: "App",
        components: { top, bottom },
        data() {
            return {
                showContent: false
            }
        },
        async created() {
            if(this.$common.getFields() == null || this.$common.getLinks() == null) {
                let res1 = await this.$http.get("/field");
                this.$common.setFields(res1.data.fields);
                let res2 = await this.$http.get("/link");
                let links = res2.data.list;
                this.$common.setLinks(links);
            }
            this.showContent = true;
            
            // console.log(window.L2Dwidget)
    
        },
        methods: { },
        computed: {
            // 判断当前页面是不是登录页
            isLoginPage(){
                return this.$route.path.startsWith("/login.html");
            },
        },
        mounted(){
           
                }
    }
</script>

<style scoped>
    /* 行走喵 */
    #marco{
        width: 1325px;
        height: 500px;
        margin: 0 auto;
        overflow: hidden;
        border-radius: 16px;
        margin-top: 2em;
        position:fixed; 
        bottom:0; right:0;
    }
    #gato{
        background: url('../static/img/cat.svg');
        height: 297px; 
        width: 507.5px;
        margin-top: 14em;
        margin-left: 60em;
        z-index: 10;
        -webkit-animation: sprite-animation 1.2s steps(16,end) infinite;
        -moz-animation: sprite-animation 1.2s steps(16,end) infinite; 
        -ms-animation: sprite-animation 1.2s steps(16,end) infinite; 
        animation: sprite-animation 1.2s steps(16,end) infinite;
    }
    @-webkit-keyframes sprite-animation { 
        from { background-position: 0 0; }
        to { background-position: -8120px 0; } 
    }
    @-ms-keyframes sprite-animation { 
            from { background-position: 0 0; }
            to { background-position: -8120px 0; }
        }

    @-moz-keyframes sprite-animation { 
            from { background-position: 0 0; }
            to { background-position: -8120px 0; }
        }

    @keyframes sprite-animation { 
            from { background-position: 0 0; }
            to { background-position: -8120px 0; }
        }
</style>
