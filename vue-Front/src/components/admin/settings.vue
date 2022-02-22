<template>
    <div>
        <!-- 顶部选项卡 -->
        <div class="ui attached pointing menu">
            <div class="ui container">
                <div class="right menu">
                    <a :class="{'item': true, 'teal active': tab==1}" @click="$message.alert('演示站暂不开放登录设置')">登录设置</a>
                    <a :class="{'item': true, 'teal active': tab==2}" @click="tab = 2">友链管理</a>
                    <a :class="{'item': true, 'teal active': tab==3}" @click="tab = 3">字段设置</a>
                </div>
            </div>
        </div>
        <div class="m-container-small m-padded-tb-big">
            <!-- 登录设置 -->
            <user-settings v-if="tab==1"></user-settings>
            <!-- 友链管理 -->
            <link-settings v-else-if="tab==2"></link-settings>
            <!-- 字段设置 -->
            <field-settings v-if="tab==3"></field-settings>
        </div>
    </div>
</template>

<script>
    import fieldSettings from "./settings/field"
    import linkSettings from "./settings/link"
    import userSettings from "./settings/user"

    export default {
        name: "settings.vue",
        components: {
            "field-settings": fieldSettings,
            "link-settings": linkSettings,
            "user-settings": userSettings
        },
        data() {
            return {
                tab: 3,
            }
        },
        created() {
        },
        methods: {
        }
    }
</script>

<style scoped></style>
