import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

import index from "@/components/index"
import type from "@/components/type"
import message from "@/components/message"
import about from "@/components/about"
import blog from "@/components/blog"
import search from "@/components/search"
import login from "@/components/login"
import blogsAdmin from "@/components/admin/blogs"
import releaseBlog from "@/components/admin/release-blog"
import editorBlog from "@/components/admin/editor-blog"
import typeAdmin from "@/components/admin/type"
import messageAdmin from "@/components/admin/message"
import settingsAdmin from "@/components/admin/settings"
import PageNotfound from "@/components/include/404"

const router = new Router({
    routes: [
        { path: "/", redirect: "/index.html" },
        { path: "/index.html", component: index },
        { path: "/type.html", component: type },
        { path: "/message.html", component: message },
        { path: "/about.html", component: about },
        { path: "/blog/:id.html", component: blog },
        { path: "/search/:searchInput.html", component: search },
        { path: "/login.html", component: login },
        { path: "/admin/blogs.html", component: blogsAdmin },
        { path: "/admin/release.html", component: releaseBlog },
        { path: "/admin/editor/:id.html", component: editorBlog },
        { path: "/admin/type.html", component: typeAdmin },
        { path: "/admin/message.html", component: messageAdmin },
        { path: "/admin/settings.html", component: settingsAdmin },
        { path: "/404.html", component: PageNotfound },
    ],
    // mode: 'history'
    mode: 'hash'
});

router.beforeEach((to, from, next) => {
    // to.fullPath.startsWith("/admin");
    if (to.matched.length === 0) {
        next('/404.html');
    } else next();  // 如果匹配到正确跳转
});

export default router;
