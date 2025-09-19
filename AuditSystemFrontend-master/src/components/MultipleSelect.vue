<template>
  <a-cascader
    :getPopupContainer="
      (trigger) =>
        trigger.parentNode.parentNode.parentNode.parentNode.parentNode
    "
    v-model:value="yetaiIds"
    :options="selectOptions"
    :show-search="{ yetaiFilter }"
    class="w_full"
    :fieldNames="{
      label: 'label',
      value: 'value',
      children: 'children'
    }"
    placeholder="请选择"
    :multiple="multiple"
    allowClear
  />
</template>
<script setup>
import api from '@/api/index'
const props = defineProps({
  modelValue: {
    type: String,
    default: null
  },
  multiple: {
    type: Boolean,
    default: null
  }
})

const selectOptions = ref([])
const getDic = () => {
  api.common.getYeTaiDic().then((res) => {
    // console.log("res", res.data);
    selectOptions.value = res.data
  })
}
const list = ref([])
const multiple = ref(props.multiple)
const emit = defineEmits(['update:modelValue'])
const yetaiIds = computed({
  get: () => {
    let arr = []
    // 编辑回显
    // console.log("modelValue", props.modelValue);
    if (props.modelValue !== null && props.multiple == false) {
      let arr = getTreeIds(selectOptions.value, props.modelValue)
      // console.log("arr", arr);
      return arr
    }
    if (props.modelValue == null) {
      let arr = []
      return null
    }
    if (props.modelValue !== null && props.multiple == true) {
      props.modelValue.forEach((res) => {
        let index = selectOptions.value.find((item) => {
          let strList = {}
          item.children.forEach((items) => {
            if (items.value == res) {
              strList = items.value
              return
            }
          })
          if (strList) {
            return strList
          }
        })
        // const index = selectOptions.value.filter((item) => {
        //   console.log(item)
        // })
        //当找到这个值之后
        // if (index) {
        //   list.value = index.children;
        //   console.log(index.children)

        //   list.value.forEach((item) => {
        //     arr.push(item.value);
        //   });
        //   console.log("66600", arr);
        //   return arr;
        // }
      })
      // console.log("arr", arr);
      // return arr;
    }
    return
  },
  set: (val) => {
    console.log('77788', val)
    let arr = []
    let p = []
    // 循环查找数据
    val.forEach((res) => {
        selectOptions.value.forEach((item) => {
          if (item.value == res[0]) {
            if (res.length > 1) {
            res.forEach((items) => {
              console.log(items)
              let index = item.children.find((cl) => {
                // 循环的值和要找的值相等的时候返回
                return cl.value == items
              })
              if(index!=undefined){ arr.push(index.value)}
             
            })
          }
          else{
            item.children.forEach((items) => {
              arr.push(items.value)
            });
          }
          }
        }) 
    })
    console.log(arr)
    emit('update:modelValue', arr)
  }
})
onMounted(() => {
  getDic()
})
// 过滤--直接搜索选项并选择
const yetaiFilter = (inputValue, path) => {
  return path.some(
    (option) =>
      option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1
  )
}
// 根据子级value查找其父级value
const getTreeIds = (tree, nodeId, config) => {
  let { children = 'children', value = 'value', label = 'label' } = config || {}
  const toFlatArray = (tree, parentId) => {
    return tree.reduce((t, _) => {
      const child = _[children]
      return [
        ...t,
        parentId ? { ..._, parentId } : _,
        ...(child && child.length ? toFlatArray(child, _[value]) : [])
      ]
    }, [])
  }
  const getIds = (flatArray) => {
    let ids = [nodeId]
    let child = flatArray.find((_) => _[value] === nodeId)
    while (child && child.parentId) {
      ids = [child.parentId, ...ids]
      child = flatArray.find((_) => _[value] === child.parentId)
    }
    return ids
  }
  return getIds(toFlatArray(tree))
}
</script>
