import { createRouter, createWebHashHistory } from 'vue-router'
import innerRouter from './innerPage'
import { isDingTalkEnv } from '@/utils/dingtalk-jsapi'

const menuLocal = JSON.parse(localStorage.getItem('menu') || '[]')
function createMenu(arr) {
  let json = []
  const modules = import.meta.glob('../views/**/**.vue')
  function doArr(arr, parent, parentJson) {
    arr.forEach((item, i) => {
      let obj = {
        path: '/' + item.path,
        name: parent + item.name,
        meta: {
          title: item.meta.title
        }
      }
      if (item.children && item.children.length > 0) {
        let first = {}
        for (let j = 0; j < item.children.length; j++) {
          if (item.children[j].path) {
            first = item.children[j]
            break
          }
        }
        if (first.path) {
          obj.redirect = '/' + first.path
          // obj.component = {render: (e) => e("router-view")};
          obj.children = []
          parentJson.push(obj)
          doArr(item.children, obj.name, obj.children)
        } else {
          if (item.component) {
            obj.component = modules[/*@vite-ignore*/ '../views/' + item.component + '.vue']
            parentJson.push(obj)
          }
        }
      } else {
        if (item.component) {
          obj.component = modules[/*@vite-ignore*/ '../views/' + item.component + '.vue']
          parentJson.push(obj)
        }
      }
    })
  }
  doArr(arr, 'home', json)
  return json
}
let menuRouter = createMenu(menuLocal)
const routes = [
  {
    path: '/',
    redirect: '/index'
  },
  {
    path: '/home',
    component: () => import('@/views/home.vue'),
    name: 'home',
    redirect: '/index',
    children: [
      {
        path: '/index',
        name: 'index',
        component: () => import('@/views/index/index.vue'),
        meta: {
          title: '首页'
        }
      },
      ...menuRouter,
      {
        path: '/innerPage',
        name: 'innerPage',
        children: [...innerRouter]
      }
    ]
  },
  {
    path: '/projectOaInfo',
    name: 'projectOaInfo',
    meta: {
      title: 'OA审批详情',
      open: true
    },
    component: () => import('@/views/project/oaInfo.vue')
  },
  {
    path: '/mobile/workReport',
    name: 'workReport',
    meta: {
      title: '数据简报详情',
      open: true
    },
    component: () => import('@/views/project/components/oaMenuTree/workReport.vue')
  },
  {
    path: '/mobile/yjqrspOldVersion',
    name: 'yjqrspOldVersion',
    meta: {
      title: '上一个版本合同信息',
      open: true
    },
    component: () => import('@/views/project/components/oaMenuTree/yjqrspOldVersion.vue')
  },
  {
    path: '/ruleDetail',
    name: 'ruleDetail',
    component: () => import('@/views/oaMessage/detail.vue'),
    meta: {
      title: 'oa待办详情',
      open: true
    }
  },
  {
    path: '/workBriefOaEdit',
    name: 'workBriefOaEdit',
    component: () => import('@/views/workbrief/components/edit.vue'),
    meta: {
      title: '工作简报审批详情',
      open: true
    }
  },
  {
    path: '/extensionOaInfo',
    name: 'extensionOaInfo',
    meta: {
      title: 'OA审批详情',
      open: true
    },
    component: () => import('@/views/project/oaExtension.vue')
  },
  {
    path: '/companyOaInfo',
    name: 'companyOaInfo',
    meta: {
      title: 'OA审批详情',
      open: true
    },
    component: () => import('@/views/investment/oaInfo.vue')
  },
  {
    path: '/customerInfo',
    name: 'customerInfoDetail',
    meta: {
      title: '客户详情',
      open: true
    },
    component: () => import('@/views/customer/info.vue')
  },
  {
    path: '/projectInfo',
    name: 'projectInfoDetail',
    component: () => import('@/views/project/info.vue'),
    meta: {
      title: '项目详情',
      open: true
    }
  },
  {
    path: '/projectOaStop',
    name: 'projectOaStop',
    meta: {
      title: 'OA审批详情',
      open: true
    },
    component: () => import('@/views/project/oPStop.vue')
  },
  {
    path: '/msgInfo',
    name: 'msgInfo',
    meta: {
      title: '消息通知',
      open: true
    },
    component: () => import('@/views/index/msg.vue')
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      title: '登录',
      open: true
    },
    component: () => import('@/views/login.vue')
  },
  {
    path: '/flower',
    name: 'flower',
    meta: {
      title: '平台登录',
      open: true
    },
    //component: () => import ("@/views/flower.vue")
    component: () => import('@/views/login.vue')
  },
  {
    path: '/ddLogin',
    name: 'ddLogin',
    meta: {
      title: 'ddLogin',
      open: true
    },
    component: () => import('@/views/project/components/oaMenuTree/ddLogin.vue')
  },
  {
    path: '/mobile',
    children: [
      {
        path: 'dashboard',
        name: 'dashboard',
        meta: {
          title: 'dashboard',
          open: true
        },
        component: () => import('@/views/project/components/oaMenuTree/dashboard.vue')
      },
      {
        path: 'workReport',
        name: 'workReport',
        meta: {
          title: 'workReport',
          open: true
        },
        component: () => import('@/views/project/components/oaMenuTree/workReport.vue')
      },
      {
        path: 'backlogInfo',
        name: 'backlogInfo',
        meta: {
          title: 'backlogInfo',
          open: true
        },
        component: () => import('@/views/project/components/oaMenuTree/enterEditMobile.vue')
      }
    ]
  },
  {
    path: '/dataBoardReport',
    name: 'dataBoardReport',
    meta: {
      title: 'dataBoardReport',
      open: true
    },
    component: () => import('@/views/analysis/components/checkDeviceAndRedirect.vue')
  },
  {
    path: '/pcDashBoard',
    name: 'pcDashBoard',
    meta: {
      title: 'pcDashBoard',
      open: true
    },
    component: () => import('@/views/analysis/dashboard.vue')
  },
  {
    path: '/backlogOaView',
    name: 'backlogOaView',
    meta: {
      title: 'backlogOaView',
      open: true
    },
    component: () => import('@/views/project/components/oaMenuTree/enterEdit.vue')
  },
  {
    path: '/checkBacklog',
    name: 'checkBacklog',
    meta: {
      title: 'checkBacklog',
      open: true
    },
    component: () => import('@/views/analysis/components/checkBacklogDeviceAndRedirect.vue')
  },
  {
    path: '/404',
    name: '404',
    meta: {
      title: '404',
      open: true
    },
    component: () => import('@/views/404.vue')
  },
  {
    path: '/error',
    name: 'error',
    meta: {
      title: 'error',
      open: true
    },
    component: () => import('@/views/error.vue')
  },
  { path: '/:error*', redirect: '/index' }
]
menuRouter = null
const router = createRouter({
  history: createWebHashHistory(),
  routes
})

import { mainStore } from '@/store'

router.beforeEach((to, from, next) => {
  const store = mainStore()
  const token = localStorage.getItem('token')
  if (!(to.meta || {}).open && !token) {
    if (from.path.includes('/mobile') && isDingTalkEnv()) {
      next('/ddLogin')
    } else {
      sessionStorage.setItem(
        'backPath',
        window.location.href.replace(window.location.origin + '/#', '')
      )
      next('/flower')
    }
  } else {
    if (to.path.indexOf('/innerPage') > -1) {
      if (from.path.indexOf('/innerPage') == -1) {
        if (from.name) {
          store.keepAlive = [from.name]
        }
        store.keepStatus = 1
      }
    } else {
      if (store.keepStatus == 0) {
        store.keepAlive = []
      }
      store.keepStatus = 0
    }
    next()
  }
})
export default router
