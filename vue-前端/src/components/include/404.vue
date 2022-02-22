<template>
    <div class="m-container-small m-padded-tb-big">
        <div class="ui container m-box">
            <h1>[ 4 0 4 ]</h1>
            <div>
                页面不存在或已删除！点击
                <router-link to="/" tag="a">返回首页</router-link>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "pageNotfound"
    }
</script>

<style scoped>
    .m-box {
        background-color: rgba(27, 28, 29, 0.95);
        height: 330px;
        border-radius: 20px
    }
    .m-box h1{
        text-align: center;
        color: white;
        font-size: 100px;
        padding: 50px 0;
    }
    .m-box div {
        text-align: center;
        color: white;
    }
</style>
