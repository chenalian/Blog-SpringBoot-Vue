package site.alian.utils;

import java.util.HashMap;

/**
 * 自定义通用返回模板
 *   code：100为正常，非100为异常
 *   msg：文字描述信息
 *   data：携带的数据
 */
public class R extends HashMap<String, Object> {

    public R(int code) {
        this.put("code", code);
        this.put("time", System.currentTimeMillis());
    }

    public static R success() {
        return new R(100);
    }

    public static R success(String msg) {
        R r = success();
        r.put("msg", msg);
        return r;
    }

    public static R error() {
        return new R(200);
    }

    public static R error(String msg) {
        R r = error();
        r.put("msg", msg);
        return r;
    }

    public static R error(int code, String msg) {
        R r = new R(code);
        r.put("msg", msg);
        return r;
    }

    @SuppressWarnings("unchecked")
    public R setAttribute(String key, Object value) {
        String data = "data";
        if (!this.containsKey(data)) {
            this.put(data, new HashMap<String, Object>());
        }
        ((HashMap<String, Object>) this.get(data)).put(key, value);
        return this;
    }

}