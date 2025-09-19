<template>
    <div>
        <ExclamationCircleFilled style="color:#ffb95e" />
        标书审核事项：
        <span v-for="(option, index) in dict.options('BIAO_SHU_SHEN_HE_SHI_XIANG')" :key="option.value">
        {{ index+1 }}、{{ option.label }}；
        </span>
    </div>
</template>

<script setup>
    import { useDictStore } from '@/store/dict'; 
    const dict = useDictStore();

    onMounted(() => {
    // console.log(dict.options('BIAO_SHU_SHEN_HE_SHI_XIANG'));
})
</script>

<style lang="scss" scoped>

</style>