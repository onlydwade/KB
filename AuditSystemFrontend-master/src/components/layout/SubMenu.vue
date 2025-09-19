
<template>
    <a-sub-menu :key="data.key">
        <template #icon v-if="data.icon">
            <i :class="'iconfont icon-' + data.icon"></i>
        </template>
        <template #title>{{ data.title }}</template>
        <template v-for="(item, index) in data.children">
            <SubMenu v-if="item.children && item.children.length > 0" :data="item" :key="item.key" />
            <a-menu-item v-else :key="item.key">
                {{ item.title }}
            </a-menu-item>
        </template>
    </a-sub-menu>
</template>
<script setup>
const props = defineProps({
    data: {
        type: Object,
        default: {},
    },
})
</script>
<style scoped lang="less"></style>
