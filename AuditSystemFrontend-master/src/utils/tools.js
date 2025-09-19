//函数节流
export const throttle = (fn, delay) => {
  let enterTime = 0;
  let gapTime = delay || 200;
  let timer = null;
  return function () {
    let context = this;
    let backTime = new Date().getTime();
    clearTimeout(timer);
    if (backTime - enterTime > gapTime) {
      fn.apply(context, arguments);
      enterTime = backTime;
    } else {
      timer = setTimeout(() => {
        fn.apply(context, arguments);
      }, gapTime);
    }
  };
};

import moment from "moment";
export const dateFormat = (date, fmt) => {
  let fmtStr = fmt || "YYYY-MM-DD HH:mm:ss";
  if (date) {
    return moment(date).format(fmtStr);
  } else {
    return "-";
  }
};

//深拷贝
export const deepClone = (obj) => {
  if (obj === null || typeof obj !== "object") {
    return obj;
  }
  let copy;
  if (Array.isArray(obj)) {
    copy = [];
    obj.forEach((item, index) => {
      copy[index] = deepClone(item);
    });
  } else {
    copy = {};
    for (const key in obj) {
      if (obj.hasOwnProperty(key)) {
        copy[key] = deepClone(obj[key]);
      }
    }
  }
  return copy;
};

export const createRouter = (router, menu) => {
  localStorage.setItem("menu", JSON.stringify(menu));
  const modules = import.meta.glob("../views/**/**.vue");
  function doArr(arr, parent) {
    arr.forEach((item, i) => {
      let obj = {
        path: "/" + item.path,
        name: parent + item.name,
        meta: {
          title: item.meta.title,
        },
      };
      if (item.children && item.children.length > 0) {
        let first = {};
        for (var i = 0; i < item.children.length; i++) {
          if (item.children[i].path) {
            first = item.children[i];
            break;
          }
        }
        if (first.path) {
          obj.redirect = "/" + first.path;
          router.addRoute(parent, obj);
          doArr(item.children, obj.name);
        } else {
          if (item.component) {
            obj.component =
              modules[/*@vite-ignore*/ "../views/" + item.component + ".vue"];
            router.addRoute(parent, obj);
          }
        }
      } else {
        if (item.component) {
          obj.component =
            modules[/*@vite-ignore*/ "../views/" + item.component + ".vue"];
          router.addRoute(parent, obj);
        }
      }
    });
  }
  doArr(menu, "home");
};
export const removeRouter = function (router) {
  function doArr(arr, parent) {
    arr.forEach((item, i) => {
      let rName = parent + item.name;
      if (item.children && item.children.length > 0) {
        doArr(item.children, rName);
        if (router.hasRoute(rName)) {
          router.removeRoute(rName);
        }
      } else {
        if (router.hasRoute(rName)) {
          router.removeRoute(rName);
        }
      }
    });
  }
  let menu = JSON.parse(localStorage.getItem("menu") || "[]");
  if (menu.length > 0) {
    doArr(menu, "home");
    localStorage.removeItem("menu");
  }
};

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children, ifHidden) {
  data.forEach((item, i) => {
    item.key = item[id];
  });

  let config = {
    id: id || "id",
    parentId: parentId || "parentId",
    childrenList: children || "children",
  };
  var childrenListMap = {};
  var nodeIds = {};
  var tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    if (ifHidden != "hidden" || d.visible == 0) {
      childrenListMap[parentId].push(d);
    }
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      if (ifHidden != "hidden" || d.visible == 0) {
        tree.push(d);
      }
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }
  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

export function getNodeById(nodes, deptId) {
  for (const node of nodes) {
    if (node.deptId == deptId) {
      return node.deptName;
    }
    if (node.children) {
      const foundNode = getNodeById(node.children, deptId);
      if (foundNode) {
        return foundNode;
      }
    }
  }
  return null;
}
export function getDictionariesVal(arr, val, key) {
  const matchedItem = arr.find((item) => item[key] === val);
  return matchedItem ? matchedItem.label : null;
}
//文件下载
export function dataToFile(data, name) {
  console.log(name);
  var blob = new Blob([data], { type: "application/vnd.ms-excel" });
  var downloadElement = document.createElement("a");
  var href = window.URL.createObjectURL(blob); //创建下载的链接
  downloadElement.style.display = "none";
  downloadElement.href = href;
  downloadElement.download = name || "宝石花"; //下载后文
  document.body.appendChild(downloadElement);
  downloadElement.click(); //点击下载
  document.body.removeChild(downloadElement); //下载完成移除元素
  window.URL.revokeObjectURL(href); //释放掉blob对象
}

export function formDataFill(formData, attrs, data) {
  attrs.forEach((attr, i) => {
    formData[attr] = data[attr];
  });
}
//基础数据map化
export function baseDataMap(list, value, label) {
  let map = {};
  let config = {
    value: value || "value",
    label: label || "label",
  };
  list.forEach((item, i) => {
    map[item[config.value]] = item[config.label];
  });
  return map;
}

export function hasPermission(value, business) {
  const isAdmin = sessionStorage.getItem("isAdmin") || 0;
  if (isAdmin == 1) {
    return true;
  }
  let permissions = JSON.parse(sessionStorage.getItem("permissions") || "[]");
  if (business) {
    let businessPer = JSON.parse(
      sessionStorage.getItem("businessPermissions_" + business) || "[]"
    );
    permissions = permissions.concat(businessPer);
  }
  if (value && value.length > 0) {
    const hasPermission = permissions.some((role) => {
      return value.includes(role);
    });
    return hasPermission;
  } else {
    return true;
  }
}

export function numFixed(val, num) {
  if (typeof val == "number") {
    let len = !num && num != 0 ? 2 : num;
    var fixedVal = val.toFixed(len);
    if (fixedVal.indexOf(".") > 0) {
      fixedVal = fixedVal.replace(/0+?$/g, ""); //去掉多余的0
      fixedVal = fixedVal.replace(/[.]$/g, ""); //如最后一位是.则去掉
    }
    return Number(fixedVal);
  } else {
    return val;
  }
}
export function amountUnit(amount) {
  if (amount && amount >= 10000) {
    let value = amount;
    let unit = "";
    let k = 10000;
    let sizes = ["", "万", "亿", "万亿", "万兆"];
    let i;
    let f = 2;
    if (value < k) {
      value = value;
    } else {
      i = Math.floor(Math.log(value) / Math.log(k));
      value = (value / Math.pow(k, i)).toFixed(4);
      unit = sizes[i];
      f = 4;
    }
    return numFixed(Number(value), f) + unit;
  } else {
    return "";
  }
}
export function amountTenthousand(amount) {
  let value = amount;
  value = value / 10000;
  return numFixed(Number(value), 4);
}
export function amountFormat(amount) {
  if (amount && amount != 0) {
    return `${amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  } else {
    return "-";
  }
}
export function amountFormatTow(amount) {
  if (amount || amount == 0) {
    return `${amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  } else {
    return "-";
  }
}

export function SHIFOUFormat(String) {
  if (!String) {
    return "-";
  } else if (String == "SHI") {
    return "是";
  } else if (String == "FOU") {
    return "否";
  }
}

export function isMobile() {
  const userAgentInfo = navigator.userAgent.toLowerCase();
  const agents = ["android", "iphone", "ipod", "ipad", "windows phone"];
  for (let i = 0; i < agents.length; i++) {
    if (userAgentInfo.indexOf(agents[i]) >= 0) {
      return true;
    }
  }
  return false;
}
//格式化金额
export function parseFormatNum(number, n) {
  if (!number && number != 0) {
    return "-";
  }
  if (number == 0) {
    return "0.00";
  }
  if (n != 0) {
    n = n > 0 && n <= 20 ? n : 2;
  }
  number = parseFloat((number + "").replace(/[^\d.-]/g, "")).toFixed(n) + "";
  var sub_val = number.split(".")[0].split("").reverse();
  var sub_xs = number.split(".")[1];

  var show_html = "";
  let i;
  for (i = 0; i < sub_val.length; i++) {
    show_html +=
      sub_val[i] + ((i + 1) % 3 == 0 && i + 1 != sub_val.length ? "," : "");
  }

  if (n == 0) {
    return show_html.split("").reverse().join("");
  } else {
    return show_html.split("").reverse().join("") + "." + sub_xs;
  }
}
export function hasFile(list) {
  for (const item of list) {
    if (item.projectCompanyDocumentList.length > 0) {
      return true;
    }
  }
  return false;
}
//校验数据
export function checkArrKeys(arr, keys) {
  for (let i = 0; i < arr.length; i++) {
    for (let j = 0; j < keys.length; j++) {
      if (!arr[i][keys[j]]) {
        return true;
      }
    }
  }
  return false;
}
export function getPercentage(curr, total) {
  if (curr === 0 || total === 0) {
    return 0;
  }
  return Math.round((curr / total) * 10000) / 100;
}
// CON  =>  水印文字内容
// H    =>  水印行高
// W    =>  水印宽度
// R    =>  旋转度数（可为负值）
// C    =>  水印字体颜色
// S    =>  水印字体的大小
// O    =>  水印透明度（0~1之间取值）
export function TpWatermark(CON, H, W, R, C, S, O) {
  // 判断水印是否存在，如果存在，那么不执行
  if (document.getElementById("tp-watermark") != null) {
    return;
  }
  var TpLine = parseInt(Math.max(document.body.clientWidth, 1920) / W) * 2; // 一行显示几列
  var StrLine = "";
  for (var i = 0; i < TpLine; i++) {
    StrLine +=
      '<span style="display: inline-block; line-height:' +
      H +
      "px; width:" +
      W +
      "px; text-align: center; transform:rotate(" +
      R +
      "deg); color:" +
      C +
      "; font-size:" +
      S +
      "px; opacity:" +
      O +
      ';">' +
      CON +
      "</span>";
  }
  var DivLine = document.createElement("div");
  DivLine.innerHTML = StrLine;

  var TpColumn = parseInt(document.body.clientHeight / H) * 2; // 一列显示几行
  var StrColumn = "";
  for (var i = 0; i < TpColumn; i++) {
    StrColumn +=
      '<div style="white-space: nowrap;">' + DivLine.innerHTML + "</div>";
  }
  var DivLayer = document.createElement("div");
  DivLayer.innerHTML = StrColumn;
  DivLayer.id = "tp-watermark"; // 给水印盒子添加类名
  DivLayer.style.position = "fixed";
  DivLayer.style.top = "0px"; // 整体水印距离顶部距离
  DivLayer.style.left = "-100px"; // 改变整体水印的left值
  DivLayer.style.zIndex = "9999999999"; // 水印页面层级
  DivLayer.style.pointerEvents = "none";
  document.body.appendChild(DivLayer); // 到页面中
}

// 移除水印方法
export function RemoveTpWatermark() {
  // 判断水印是否存在，如果存在，那么执行
  if (document.getElementById("tp-watermark") == null) {
    return;
  }
  document.body.removeChild(document.getElementById("tp-watermark"));
}
