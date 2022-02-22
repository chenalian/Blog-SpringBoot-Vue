<!-- 关于我页面 -->
<template>
    <div class="m-container m-padded-tb-big m-min-height flipInY animated">
        <div class="ui container">
            <div class="ui stackable grid m-padded-lr-small">
                <div class="eleven wide column">
                    <img src="" class="ui rounded image" style="width: 800px;">
                    <img :src="aboutImg" class="ui rounded image" style="width: 800px;">
                </div>
                <div class="five wide column">
                    <div class="ui top attached segment">
                        <div class="ui header">{{username}}</div>
                    </div>
                    <div class="ui attached segment">
                        <p class="m-text" v-html="aboutInfo"></p>
                    </div>
                    <div class="ui attached segment">
                        <!-- 循环遍历标签 -->
                        <div class="ui orange basic left pointing label m-margin-bottom" v-for="(item, index) in tags" :key="index">{{item}}</div>
                    </div>
                    <div class="ui bottom attached segment">
                        <a class="ui circular icon button" id="qq" :data-content="qqNum" data-position="bottom center">
                            <i class="qq icon" />
                        </a>
                        <a class="ui circular icon button" id="weChat" :data-content="wechatNum" data-position="bottom center">
                            <i class="weixin icon" />
                        </a>
                        <a class="ui circular icon button" id="email" :data-content="email" data-position="bottom center">
                            <i class="mail icon" />
                        </a>
                        <router-link to="/admin/blogs.html" id="admin" class="ui circular icon button" data-content="后台管理入口" data-position="bottom center">
                            <i class="user secret icon" />
                        </router-link>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "about.bue",
        data() {
            return {
                qqNum: null,
                wechatNum: null,
                email: null,
                username: null,
                aboutInfo: null,
                aboutTag: null
            }
        },
        created() {
            this.qqNum = this.$common.getField("qqNum");
            this.wechatNum = this.$common.getField("wechatNum");
            this.email = this.$common.getField("email");
            this.username = this.$common.getField("username");
            this.aboutInfo = this.$common.getField("aboutInfo");
            this.aboutTag = this.$common.getField("aboutTag");
            // 页面加载完成后执行
            this.$nextTick(()=>{
                $("#qq").popup();
                $("#weChat").popup();
                $("#admin").popup();
                $("#email").popup();
            });
        },
        computed: {
            tags() {
                return this.aboutTag.split("-");
            },
            aboutImg() {
                return this.$common.getField("aboutImg");
            }
        }
    }
</script>

<style scoped>

</style>
