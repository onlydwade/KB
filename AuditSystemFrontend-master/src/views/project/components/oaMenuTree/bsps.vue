<template>
    <div class="menu_inner">
        <div class="card_box">
            <van-cell-group title="项目信息">
                <van-cell title="项目名称" :value="formData.projectName || '-'" />
                <van-cell title="所属大区" :value="formData.regionName || '-'" />
                <van-cell title="归属单位" :value="formData.companyName || '-'" />
                <van-cell title="拓展模式" :value="formData.expansionModeStr || '-'" />
                <van-cell title="业务" :value="formData.businessTypeStr || '-'" />
                <van-cell title="开标时间"  :value="dateFormat(formData.bidingOpentime, 'YYYY-MM-DD') || '-'" />
                
            </van-cell-group>
        </div>
        <div class="card_box">
            <van-cell-group title="选择相关标书形式">
                <van-cell title="投标类型" :value="formData.bidTypeStr || '-'" />
                <van-cell v-if="formData.bidType == 'ZHI_ZHI_BIAO'" title="申请类型"
                    :value="formData.applicationTypeStr || '-'" />
                <van-cell v-if="formData.bidType == 'ZHI_ZHI_BIAO'" title="关联用印类型" :value="formData.relatedSealTypeStr || '-'" />
                <van-cell v-if="formData.bidType != 'ZHI_ZHI_BIAO'" title="关联用印类型" :value="formData.relatedSealType == '1'?'CA证书盖章':'投标文件需借CA'" />
                <van-cell v-if="formData.bidType != 'ZHI_ZHI_BIAO'" title="是否涉及公章借用" :value="formData.isSealBorrowingStr || '-'" />
                <van-cell v-if="formData.bidType != 'ZHI_ZHI_BIAO' && formData.isSealBorrowing == 'SHI' " title="用印类型"  :value="formData.applicationTypeStr || '-'" />
                <van-cell v-if="formData.bidType == 'ZHI_ZHI_BIAO'" title="借用日期"  :value="dateFormat(formData.borrowingTime, 'YYYY-MM-DD') || '-'" />
                <van-cell v-if="formData.bidType == 'ZHI_ZHI_BIAO'" title="归还日期"  :value="dateFormat(formData.returnTime, 'YYYY-MM-DD') || '-'" />

            </van-cell-group>
        </div>
        <FileList :menuId="menuId" :projectId="projectId" />
    </div>
</template>
<script setup>
import { useMenuTree } from "./menu";
const props = defineProps({
    projectId: {
        type: Number,
        default: 0,
    },
    menuId: {
        type: Number,
        default: 0,
    },
    menuInfo: {
        type: Object,
        default: {},
    },
});

const {
    formData,
    getInfo,
} = useMenuTree(props.projectId, toRefs(props).menuInfo);

onMounted(() => {
    getInfo();
});
</script>
<style scoped lang="less">
.menu_inner {
    .card_box {
        margin-bottom: 20px;
        border-radius: 12px;
        background: #fff;
    }

    .van-cell {
        color: #969799;
    }

    :v-deep .van-cell__value {
        color: #000;
    }

    :v-deep .van-cell-group__title {
        color: #000;
        font-weight: bold;
    }
}
</style>
