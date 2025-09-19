<template>
    <div class="page-index content-inner">
        <div class="index_header">
            <div class="left">
                <a-avatar class="avatar" :size="72" src="/images/logo.png"/>
                <div>
                    <h1>
                        Hello， {{store.userInfo.realname || store.userInfo.nickName}}
                    </h1>
                    <p>
                        欢迎来到宝石花投拓管理平台！
                    </p>
                </div>
            </div>
        </div>
        <div class="index_content">
            <Commission/>
            <div class="right_box">
                <Project/>
            </div>
        </div>
    </div>
</template>
<script setup>
import api           from '@/api/index';
import Commission    from './components/Commission.vue';
import Project       from './components/Project.vue';
import { mainStore } from '@/store';
const store  = mainStore();
const router = useRouter();
onMounted(() => {
})

</script>
<style scoped lang="less">
.page-index{
    display        : flex;
    flex-direction : column;
    .content-box{
        display        : flex;
        flex-direction : column;
    }
}

.index_header{
    background-color : #fff;
    border-radius    : 4px;
    margin-bottom    : 16px;
    padding          : 16px;
    display          : flex;
    align-items      : center;
    
    .left{
        flex : 1;
        display: flex;
        align-items: center;
        h1{
            font-size   : 20px;
            display     : flex;
            flex-wrap   : wrap;
            align-items : center;
        }
        .avatar{
            margin-right: 16px;
        }
        p{
            font-size  : 14px;
            color      : @text-color-secondary;
        }
    }
    .summary_item{
        border-left  : 1px solid #f0f2f5;
        margin-left  : 20px;
        padding-left : 20px;
        .title{
            font-size       : 14px;
            display         : flex;
            justify-content : space-between;
            min-width       : 150px;
            .today{
                font-size: 16px;
            }
        }
        .num_box{
            font-size   : 18px;
            line-height : 50px;
            text-align  : right;
            color       : @text-color-secondary;
            .today{
                font-size : 34px;
            }
        }
    }
}

.index_content{
    flex          : 1;
    height        : 0;
    display       : flex;
    margin-bottom : 16px;
    .right_box{
        width          : 360px;
        margin-left    : 16px;
        display        : flex;
        flex-direction : column;
    }
    &:last-child{
        margin-bottom: 0;
    }
}
</style>