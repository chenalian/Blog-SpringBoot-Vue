import CryptoJS from 'crypto-js/crypto-js'
import {AES_KEY, AES_IV} from './constant'

/**
 * AES加密解密服务：
 *    Key：秘钥，要求必要长度必须是16位
 *    iv：偏移量，同样要求必要长度必须是16位
 */
export default {
    // 秘钥，偏移量
    key: CryptoJS.enc.Utf8.parse(AES_KEY),
    iv: CryptoJS.enc.Utf8.parse(AES_IV),
    // 数据加密
    encrypt: function (param) {
        // NULL值判断
        let data = null;
        if (param == null){
            return null;
        } else data = JSON.stringify(param);
        // 加密过程
        let encrypted = CryptoJS.AES.encrypt(data, this.key, {
            iv: this.iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.ZeroPadding
        });
        return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
    },
    // 数据解密
    decrypt: function (param) {
        let decrypt = CryptoJS.AES.decrypt(param, this.key, {
            iv: this.iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.ZeroPadding
        });
        let decryptedStr = decrypt.toString(CryptoJS.enc.Utf8);
        return JSON.parse(decryptedStr.toString());
    }

}
