/**
 * 工具类服务：抽取重复使用的方法
 */
export default {
    // 存储所有字段
    setFields(list) {
        let json = JSON.stringify(list);
        sessionStorage.setItem("fields", json)
    },
    // 获取字段列表
    getFields() {
        let json = sessionStorage.getItem("fields");
        return JSON.parse(json);
    },
    // 获取某个字段的值
    getField(name) {
        return this.getFields().find(item=>item.field===name).value
    },
    // 存储友链
    setLinks(list) {
        let json = JSON.stringify(list);
        sessionStorage.setItem("links", json)
    },
    // 获取友链
    getLinks() {
        let json = sessionStorage.getItem("links");
        return JSON.parse(json);
    },
    // 验证字符串是否为空，返回true为空
    strIsNull(str) {
        return str == null || str.length < 1
    }
}
