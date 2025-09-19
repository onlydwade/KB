import * as dd from 'dingtalk-jsapi'; // 此方式为整体加载，也可按需进行加载
import { getENV } from 'dingtalk-jsapi/lib/env';

export function requestAuthCode(corpId) {
  return new Promise((resolve, reject) => {
    dd.ready(function () {
      dd.runtime.permission.requestAuthCode({
        corpId,
        onSuccess: (result) => resolve(result),
        onFail: (err) => reject(err)
      })
    })
  })
}

//#region checkPlatform
export function getPlatform() {
  const { platform } = getENV();

  return platform;
}

export function isDingTalkEnv() {
  try {
    if (getPlatform() === 'notInDingTalk') {
      return false;
    } else {
      return true;
    }
  } catch (err) {
    return false;
  }
}