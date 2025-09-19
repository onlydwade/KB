const innerPage = [
    //项目内页
    {
        path: "projectInfo",
        name: 'projectInfo',
        component: () => import("@/views/project/info.vue"),
        meta: {
            title: '查看项目',
        }
    },
    {
        path: "projectEdit",
        name: 'projectEdit',
        component: () => import("@/views/project/edit.vue"),
        meta: {
            title: '项目编辑',
        }
    },
    {
      path: "projectEnterEdit",
      name: 'projectEnterEdit',
      component: () => import("@/views/project/enterEdit.vue"),
      meta: {
          title: '项目补录编辑',
      }
    },
    {
        path: "renewalEdit",
        name: 'renewalEdit',
        component: () => import("@/views/project/renewalEdit.vue"),
        meta: {
            title: '项目续签/重新投标',
        }
    },

    //客户管理内页
    {
        path: "customerInfo",
        name: 'customerInfo',
        component: () => import("@/views/customer/info.vue"),
        meta: {
            title: '查看客户',
        }
    },
    {
        path: "customerEdit",
        name: 'customerEdit',
        component: () => import("@/views/customer/edit.vue"),
        meta: {
            title: '客户编辑',
        }
    },
    {
        path: "Cooperation",
        name: 'Cooperation',
        component: () => import("@/views/customer/components/CooperationEdit.vue"),
        meta: {
            title: '编辑客户战略合作',
        }
    },
    {
        path: "extensionInfo",
        name: 'extensionInfo',
        component: () => import("@/views/project/extensionInfo.vue"),
        meta: {
            title: '查看项目',
        }
    },
    {
        path: "operationInfo",
        name: 'operationInfo',
        component: () => import("@/views/project/operationInfo.vue"),
        meta: {
            title: '运营数据信息',
        }
    },

    //投后管理内页
    {
        path: "subsidiaryInfo",
        name: 'subsidiaryInfo',
        component: () => import("@/views/investment/subsidiaryInfo.vue"),
        meta: {
            title: '查看子公司',
        }
    },
    {
        path: "subsidiaryEdit",
        name: 'subsidiaryEdit',
        component: () => import("@/views/investment/subsidiaryEdit.vue"),
        meta: {
            title: '子公司编辑',
        }
    },
    {
        path: "workBriefEdit",
        name: 'workBriefEdit',
        component: () => import("@/views/workbrief/components/edit.vue"),
        meta: {
            title: '工作简报编辑',
        }
    },
    {
        path: "ruleDetail",
        name: 'ruleDetail',
        component: () => import("@/views/oaMessage/detail.vue"),
        meta: {
            title: 'oa待办详情',
        }
    },
    {
        path: "/opportunity/realTime/detail",
        name: 'realTimeDetail',
        component: () => import("@/views/opportunity/realTime/components/DetailForm.vue"),
        meta: {
            title: '实时商机库详情页面',
        }
    },
    {
        path: "/opportunity/stock/detail",
        name: 'stockDetail',
        component: () => import("@/views/opportunity/stock/components/EditForm.vue"),
        meta: {
            title: '存量商机库详情页面',
        }
    },
];
export default innerPage;
