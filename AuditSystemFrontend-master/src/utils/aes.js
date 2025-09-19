import CryptoJS from 'crypto-js/crypto-js'
// 默认的 KEY 与 iv 如果没有给
const key = CryptoJS.enc.Utf8.parse("dadich,roc2cloud");
const iv = CryptoJS.enc.Utf8.parse('dadich,roc2cloud');
/**
* AES加密 ：字符串 key iv  返回base64
*/
export function Encrypt(word) {
    let srcs = CryptoJS.enc.Utf8.parse(word);
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {
        iv      : iv,
        mode    : CryptoJS.mode.CFB,
        // padding : CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
}


export const encryption = (params) => {
    let { data, type, param, key } = params;
    const result = JSON.parse(JSON.stringify(data));
    if (type === "Base64") {
        param.forEach(ele => {
            result[ele] = btoa(result[ele]);
        });
    } else {
        param.forEach(ele => {
            var data = result[ele];
            key = CryptoJS.enc.Latin1.parse(key);
            var iv = key;
            //密加
            var encrypted = CryptoJS.AES.encrypt(data, key, {
                iv: iv,
                mode: CryptoJS.mode.CFB,
                padding: CryptoJS.pad.NoPadding
            });
            result[ele] = encrypted.toString();
        })
    }
    return result;
}