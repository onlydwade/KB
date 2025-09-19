
<template>
    <a-select v-model:value="userName" class="w_full" allowClear placeholder="请搜索选择" showSearch :options="options"
        :filter-option="false" :not-found-content="fetching ? undefined : '按姓名搜索后选择'" @search="fetchUser">
        <template #option="item">
            <div class="user_item">
                {{ item.label }}
                <span class="color-info" style="font-size:12px;font-weight:normal">
                    {{ item.deptName }}
                </span>
            </div>
        </template>
        <template #notFoundContent>
            <a-spin v-if="fetching" size="small" />
            <div class="empty" v-else>
                按姓名搜索后选择
            </div>
        </template>
    </a-select>
</template>
<script setup>
import api from '@/api/index';
const props = defineProps({
    modelValue: {
        type: String,
        default: null,
    },
})
const emit = defineEmits(['update:modelValue']);
const userName = computed({
    get: () => props.modelValue || null,
    set: (val) => {
        emit('update:modelValue', val)
    }
})
const options = ref([]);
const fetching = ref(false);
const timerFun = ref(null);
const fetchUser = (name) => {
    window.clearTimeout(timerFun.value);
    if (name) {
        timerFun.value = setTimeout(() => {
            searchUser(name);
        }, 500)
    }
}
const searchUser = (name) => {
    fetching.value = true;
    let postData = {
        pageNo: 1,
        pageSize: 100,
        content: name,
        contentColumn: 'realname',
        params: {}
    }
    api.sys.userPage(postData).then(res => {
        if (res.code == 200) {
            options.value = res.data.records.map(item => {
                return {
                    label: item.realname,
                    value: item.realname,
                    deptId: item.deptId,
                    deptName: item.deptName
                }
            })
            nextTick(() => {
                fetching.value = false;
            })
        }
    })
}
onMounted(() => {
});

</script>
<style scoped lang="less">
.user_box {
    display: inline-flex;
    align-items: center;

    .avatar+.avatar {
        margin-left: 4px;
    }
}

.empty {
    text-align: center;
}

.user_item {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
}
</style>
