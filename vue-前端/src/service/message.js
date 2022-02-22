import layer from "layui-layer";

export default {
    // 吐司：屏幕中央的黑色背景提示，会自动关闭
    toast: function (title) {
        layer.msg(title);
    },
    // 警告框：需要手动点击确认
    alert: function (title, callBack = null) {
        return new Promise((resolve, reject) => {
            layer.alert(title, null, index => {
                layer.close(index);
                resolve();
            });
        })
    },
    // 确认框：可选择继续取消
    confirm: function (title, yesCallback, noCallback = null) {
        layer.confirm(title, { btn: ['确定', '取消'] }, () => {
            yesCallback();
            // 这里需要手动关闭弹窗
            layer.close(layer.index);
        }, noCallback);
    },
    // 输入框：输入内容然后回传给callBack
    prompt(title, callBack) {
        layer.prompt({title}, (value, index)=>{
            callBack(value);
            layer.close(index);
        });
    }
};
