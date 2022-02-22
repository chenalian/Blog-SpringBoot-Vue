<!-- 公共页脚 -->
<template>
    <footer class="ui inverted vertical segment m-padded-tb-huge">
        <div class="ui center aligned container">
            <div class="ui stackable inverted divided grid">
                <div class="three wide column">
                    <div class="ui inverted link list">
                        <div class="item">
                            <img :src="qqQr" title="添加QQ" class="ui rounded image" style="width: 110px;">
                        </div>
                        <span class="m-opacity">添加QQ</span>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui inverted link list">
                        <h3 class="ui teal header">友情链接</h3>
                        <a class="item" target="_blank" v-for="(item, index) in links" :key="index" v-text="item.name" :href="item.url"/>
                    </div>
                </div>
                <div class="three wide column">
                    <div class="ui inverted link list">
                        <h3 class="ui teal header">联系我</h3>
                        <a class="item">微信：{{wechatNum}}</a>
                        <a class="item">QQ：{{qqNum}}</a>
                        <a class="item">邮箱：{{email}}</a>
                    </div>
                </div>
                <div class="seven wide column">
                    <h3 class="ui teal header">关于我</h3>
                    <p class="m-opacity">{{indexInfo}}</p>
                </div>
            </div>
            <div class="ui inverted section divider"></div>
            <p><a href="http://zhang_hanzhe.gitee.io/markdown/" target="_blank" style="color: #8D8E8E">本站由Gitee提供图床支持</a></p>
        </div>
    </footer>
</template>

<script>

    export default {
        name: "bottom",
        data() {
            return {
                wechatNum: "",
                qqNum: "",
                email: "",
                indexInfo: "",
                qqQr: "",
                model:{
                    　jsonPath: "https://unpkg.com/live2d-widget-model-shizuku@1.0.5/assets/shizuku.model.json",//这是插件模型，可以任意选择想要添加的2d动画
            　　　　"scale": 1 
                }
            }
        },
        created() {
            let arr = ["wechatNum", "qqNum", "email", "indexInfo", "qqQr"];
            arr.forEach(item=>{
                this[item] = this.$common.getField(item);
            })

        },
        computed: {
            links() {
                return this.$common.getLinks();
            }
        },
    }
</script>

<style scoped>

</style>
