<!-- 列表中展示的每个博客都单独抽取为一个组件 -->
<template>
    <div>
        <div v-if="list.length < 1" style="padding: 10px 0; text-align: center">
            {{tips}}
        </div>
        <div class="ui padded vertical segment m-padded m-margin-bottom" v-for="(blog, index) in list" :key="index">
            <div class="ui middel aligned mobile reversed stackable grid">
                <div class="eleven wide column">
                    <h3 class="ui header">
                        <router-link style="color: black" :to="'/blog/' + blog.id + '.html'" target="_blank">{{blog.title}}</router-link>
                    </h3>
                    <p class="m-text">{{blog.desc}}</p>
                    <div class="ui grid">
                        <div class="nine wide column">
                            <div class="ui mini horizontal link list">
                                <div class="item">
                                    <img :src="adminImg" class="ui avatar image" width="100px">
                                    <div class="content">{{name}}</div>
                                </div>
                                <div class="item" title="发布日期">
                                    <i class="calendar icon"/>
                                    <span>{{blog.createTime | dateFormat}}</span>
                                </div>
                                <div class="item" title="浏览次数">
                                    <i class="eye icon"/>
                                    <span>{{blog.viewCount}}</span>
                                </div>
                            </div>
                        </div>
                        <div class="right aligned seven wide column">
                            <router-link tag="a" target="_blank" :to="'/type.html?id=' + blog.typeId" class="ui orange basic label m-padded-tiny">{{blog.typeName}}</router-link>
                            <router-link tag="a" target="_blank" :to="'/blog/' + blog.id + '.html'" class="ui teal basic label m-padded-tiny">查看全文</router-link>
                        </div>
                    </div>
                </div>
                <div class="five wide column">
                    <router-link tag="a" target="_blank" :to="'/blog/' + blog.id + '.html'">
                        <img :src="blog.imgUrl" class="ui rounded image" width="100%" height="120em">
                    </router-link>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "blog-list",
        // list：博客列表，desc：列表为0时的提示文字
        props: ["list", "desc"],
        computed: {
            name() {
                return this.$common.getField("username");
            },
            adminImg() {
                return this.$common.getField("adminImg");
            },
            tips() {
                return (this.desc == null || this.desc == "") ? "没有博客可以显示" : this.desc;
            }
        }
    }
</script>

<style scoped>

</style>
