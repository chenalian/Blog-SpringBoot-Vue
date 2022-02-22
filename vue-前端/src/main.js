import Vue from 'vue'
import App from './App'
import router from './service/router'

Vue.config.productionTip = false;

// 引入JQuery、SemanticUI以及其他CSS文件
import $ from 'jquery'
import semantic from 'semantic-ui-css'
import '../node_modules/semantic-ui-css/semantic.min.css'
import "../static/css/me.css"
import "../static/css/animate.min.css"

// 在全局属性上挂载自定义服务
import Message from "./service/message"
import Request from "./service/request"
import Common from "./service/common"
Vue.prototype.$message = Message;
Vue.prototype.$http = Request;
Vue.prototype.$common = Common;

// 定义全局过滤器
import * as filters from './service/filters'
Object.keys(filters).forEach(key => {
    Vue.filter(key, filters[key])
});

// Markdown
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
Vue.use(mavonEditor);

// 博客详情页样式配置
import "../static/css/blog.css"
// 导入代码高亮，官网 https://highlightjs.org/
import hljs from 'highlight.js'
import 'highlight.js/styles/dracula.css'
Vue.directive('highlight', function (el) {
    let blocks = el.querySelectorAll('pre code');
    setTimeout(() =>{
        blocks.forEach((block)=>{
            hljs.highlightBlock(block)
        })
    }, 200)
})

new Vue({
    el: '#app',
    router,
    components: { App },
    data() {
        return {}
    },
    template: '<App/>'
});
