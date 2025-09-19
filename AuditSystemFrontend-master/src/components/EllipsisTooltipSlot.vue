<template>
  <div class="ellipsis_box">
    <a-tooltip v-if="isShowTooltip" destroyTooltipOnHide placement="top" trigger="hover" overlayClassName="table-class"
      v-model:visible="isShow">
      <template #title>
        <span>
          <slot></slot>
        </span>
      </template>
      <div class="content" @mouseleave="mouseleave">
        <span ref="contentRef">
          <slot></slot>
        </span>
      </div>
    </a-tooltip>
    <div v-else class="content" @mouseenter="mouseenter">
      <span ref="contentRef">
        <slot></slot>
      </span>
    </div>
  </div>
</template>
<script setup>
const props = defineProps({
  content: {
    type: String,
    default: "",
  },
  mode: {
    type: Number,
    default: 0,
  },
})
const isShow = ref(true);
const contentRef = ref();
const isShowTooltip = ref(false);
const mouseleave = () => {
  // isShowTooltip.value = false
};
const mouseenter = () => {
  // 关键代码逻辑
  if (contentRef.value.parentNode.offsetWidth >= contentRef.value.offsetWidth) {
    isShowTooltip.value = false;
  } else {
    isShowTooltip.value = true;
  }
};
</script>

<style>
.ellipsis_box {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.content {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.status {
  color: white;
  border-radius: 40px;
  width: 100px;
  text-align: center;
  height: 26px;
  line-height: 26px;
}</style>