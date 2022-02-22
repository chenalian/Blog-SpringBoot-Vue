import axios from 'axios'
import AES from './aes'
import Message from './message'
// Axios请求常量
import {REQUEST_BASE_URL, REQUEST_TIMEOUT, RESULT_CODE} from './constant';
import router from './router';

// 请求的目标URL
axios.defaults.baseURL = REQUEST_BASE_URL;
// 设置请求超时时间
axios.defaults.timeout = REQUEST_TIMEOUT;
// 允许携带Cookie
axios.defaults.withCredentials = true;

axios.interceptors.request.use((config) => {
    let token = sessionStorage.getItem("token");
    if( token != null ) {
        config.headers.token = token;
    }
    return config;
}, (error) => { Promise.reject(error) });

export default {
    // GET请求
    get: function (url, param = null) {
        return new Promise((resolve, reject) => {
            axios.get(url, { params: { json: AES.encrypt(param) } }).then(({ data: res }) => {
                this.requestSuccess(res, resolve, reject)
            }).catch(err => this.requestFail(err));
        });
    },
    // POST请求
    post: function (url, param = null) {
        return new Promise((resolve, reject) => {
            axios.post(url, { json: AES.encrypt(param) }).then(({ data: res }) => {
                this.requestSuccess(res, resolve, reject)
            }).catch(err => this.requestFail(err));
        });
    },
    // DELETE请求
    delete: function (url, param = null) {
        return new Promise((resolve, reject) => {
            axios.delete(url, { params: { json: AES.encrypt(param) } }).then(({ data: res }) => {
                this.requestSuccess(res, resolve, reject)
            }).catch(err => this.requestFail(err));
        });
    },
    // PUT请求
    put: function (url, param = null) {
        return new Promise((resolve, reject) => {
            axios.put(url, { json: AES.encrypt(param) }).then(({ data: res }) => {
                this.requestSuccess(res, resolve, reject)
            }).catch(err => this.requestFail(err));
        });
    },
    // 上传文件
    upload: function (url, file) {
        let form = new FormData();
        form.append("file", file);
        return new Promise((resolve, reject) => {
            axios.post(url, form).then(({ data: res }) => {
                this.requestSuccess(res, resolve, reject)
            }).catch(err => this.requestFail(err));
        });
    },

    // 请求成功处理方法
    requestSuccess(res, resolve, reject) {
        switch (res.code) {
            // 请求处理成功
            case RESULT_CODE.SUCCESS: {
                // 解密
                if(res.data != null){
                    res.data = AES.decrypt(res.data["result"]);
                    // 返回值中包含Token就更新Token信息
                    if(res.data.token != null) {
                        sessionStorage.setItem("token", res.data.token)
                    }
                }
                // 走Promise
                resolve(res);
                break;
            }
            // 找不到博客
            case RESULT_CODE.BLOG_NOT_FOUND: {
                Message.toast(res.msg);
                router.push("/")
                break;
            }
            // Token异常
            case RESULT_CODE.TOKEN_ERROR: {
                sessionStorage.removeItem("token");
                this.toLogin(res.msg);
                break;
            }
            // 兜底方案
            default: {
                Message.toast(res.msg);
                reject(res);
            }
        }
    },
    // 请求失败通用模板
    requestFail(err) {
        console.log(err);
        Message.alert("服务器请求失败，请联系站长进行处理！");
    },
    // 跳转到登录页并显示消息
    toLogin(msg) {
        Message.toast(msg);
        router.push("/login.html")
    }
}
