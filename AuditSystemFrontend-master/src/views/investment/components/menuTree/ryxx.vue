
<template>
    <div class="menu_inner">
        <Title title="外派人员"></Title>
        <Executives :companyId="companyId" :menuId="menuInfo.id" :tempId="1043"/>

        <Title title="项目组人员" style="margin-top:16px"></Title>
        <span>内部人员:</span>
        <Team :projectId="projectId" type="TOU"/>
        <br>
        <span>外部人员:</span>
        <TeamTwo :projectId="projectId" type="TOU"/>

        <Title title="企业联系人" style="margin-top:16px"></Title>
        <Linkman :companyId="companyId"/>
    </div>
</template>
<script setup>
import Executives from '../correlation/Executives.vue';
import Team       from '../correlation/Team.vue';
import Linkman    from '../correlation/Linkman.vue';
import TeamTwo    from '../correlation/TeamTow.vue';
const props = defineProps({
    companyId: {
        type    : Number,
        default : 0,
    },
    menuInfo : {
        type    : Object,
        default : {},
    }
})

const projectId  = inject('getAutoParams')('projectId');
</script>
<style scoped lang="less">

</style>
