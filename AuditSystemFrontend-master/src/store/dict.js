import { defineStore } from "pinia";
export const useDictStore = defineStore("dict", {
  state: () => {
    return {
      dictList: {},
    };
  },
  getters: {
    optionsAll: (state) => (type) => {
      let arr = (state.dictList[type] || []).map((item) => {
        let d = JSON.parse(JSON.stringify(item));
        d.disabled = false;
        return d;
      });
      return [{ value: "-1", label: "全部" }].concat(arr);
    },
    options: (state) => (type, disabled) => {
      const arr = (state.dictList[type] || []).filter(
        (item) => !disabled || !item.disabled
      );
    //   console.log(arr)
      return arr;
    },
    map: (state) => (type) => {
      let map = {}(state.dictList[type] || []).forEach((item, i) => {
        map[item.value] = item.label;
      });
      return map;
    },
  },
  actions: {
    setDict(data) {
      let map = {};
      data.forEach((item, i) => {
        if (!map[item.dictType]) {
          map[item.dictType] = [];
        }
        let obj = {
          value: item.dictValue,
          label: item.dictLabel,
        };
        if (item.isDefault == "N") {
          obj.disabled = true;
        }
        map[item.dictType].push(obj);
      });
      this.dictList = map;
    },
  },
});
