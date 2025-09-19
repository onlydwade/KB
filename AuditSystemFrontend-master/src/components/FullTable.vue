
<template>
    <div class="full_table" ref="el">
        <a-table :bordered="bordered" :columns="columns" :loading="loadding" :data-source="dataSource" :rowKey="rowKey"
            :defaultExpandedRowKeys="defaultExpandedRowKeys" :pagination="false" :scroll="{ x: '100%', y: tableHeight }">
            <template v-slot:[item]="scope" v-for="item in renderArr">
                <slot :name="item" :scope="scope" v-bind="scope || {}"></slot>
            </template>
        </a-table>
    </div>
</template>
<script setup>
import { useElementSize } from '@vueuse/core'
const el = ref(null)
const { height } = useElementSize(el)
const slots = useSlots()
const renderArr = Object.keys(slots)
const props = defineProps({
    headerH: {
        type: Number,
        default: 55,
    },
    loadding: {
        type: Boolean,
        default: false,
    },
    columns: {
        type: Array,
        default: [],
    },
    dataSource: {
        type: Array,
        default: [],
    },
    rowKey: {
        type: String,
        default: 'id',
    },
    defaultExpandedRowKeys: {
        type: Array,
        default: [],
    },
    bordered: {
        type: Boolean,
        default: false,
    }
})
const DEFAULT_HEIGHT = 200
const tableHeight = ref(DEFAULT_HEIGHT);
watch(() => height.value, () => {
    let h = height.value - props.headerH;
    tableHeight.value = h
}, { immediate: true })
</script>
<style scoped lang="less"></style>
