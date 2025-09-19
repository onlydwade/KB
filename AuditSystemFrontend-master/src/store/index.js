import { defineStore } from 'pinia'

export const mainStore = defineStore('main', {
    state: () => {
        return {
            token       : localStorage.getItem('token') || '',
            ssoToken    : localStorage.getItem('ssoToken') || '',
            userInfo    : {},
            deptPost    : {},
            deptPosts   : [],
            permissions : [],
            businessPermissions:{},

            menuRouters : JSON.parse(localStorage.getItem('menu') || '[]'),

            collapse : false,
            spin     : 0,

            routerCreated : false,

            deptTree : [],
            deptTreeList : [],
            postList : [],

            keepStatus : 0,
            keepAlive  : [],

            scale : 1,
        }
    },
    getters: {
        getDeptLeafSelect:(state)=>{
            const addKey = (treeData)=>{
                return treeData.map(item => ({
                    ...item,
                    disabled : item.children?.length>0 ? true : false,
                    children   : item.children?addKey(item.children) : []
                }))
            }
            return addKey(state.deptTree);
        },
        getDeptLeafSelectList:(state)=>{
            const addKey = (treeData)=>{
                return treeData.map(item => ({
                    ...item,
                    disabled : item.children?.length>0 ? true : false,
                    children   : item.children?addKey(item.children) : []
                }))
            }
            return addKey(state.deptTreeList);
        },
        getDeptWithOutRoot:(state)=>{
            let arr = [];
            function doArr(treeData,parentChild){
                if (treeData){
                    treeData.forEach((item, i) => {
                        let obj = {
                            ...item,
                            children : []
                        }

                        if(item.deptType === 'CENG_JI'  || (item.children&&item.children.length>0)){
                            doArr(item.children,obj.children);
                            parentChild.push(obj)
                        }
                        // if(item.level<3 || (item.children&&item.children.length>0)){
                        //     if(item.children&&item.children.length>0){
                        //         doArr(item.children,obj.children);
                        //     }
                        //     parentChild.push(obj)
                        // }
                    });
                }
            }
            doArr(state.deptTree,arr);
            return arr;
        },
        getDeptWithOutRootList:(state)=>{
            let arr = [];
            function doArr(treeData,parentChild){
                if (treeData){
                    treeData.forEach((item, i) => {
                        let obj = {
                            ...item,
                            children : []
                        }

                        if(item.deptType === 'CENG_JI' || (item.deptType === 'BU_MEN' && item.parentId ==100 )  || (item.children&&item.children.length>0)){
                            doArr(item.children,obj.children);
                            if(item.deptType === 'BU_MEN' && item.parentId !=100){
                                
                            }else{
                                parentChild.push(obj)
                            }
                        }
                        // if(item.level<3 || (item.children&&item.children.length>0)){
                        //     if(item.children&&item.children.length>0){
                        //         doArr(item.children,obj.children);
                        //     }
                        //     parentChild.push(obj)
                        // }
                    });
                }
            }
            doArr(state.deptTree,arr);
            return arr;
        },
        getMenu:(state)=>{
            let json = [{title : '首页',key:'index',icon:'index'}];
            function doArr(arr,target,parentPath){
                arr.forEach((item, i) => {
                    if(!item.hidden&&item.path){
                        let obj = {
                            title : item.meta.title,
                            icon  : (item.meta.icon&&item.meta.icon!='#')?item.meta.icon:null,
                            key   : item.path.replace(parentPath+'/',''),
                        }
                        if(item.children&&item.children.length>0){
                            obj.children = [];
                            doArr(item.children,obj.children,item.path);
                            target.push(obj);
                        }else{
                            target.push(obj);
                        }
                    }
                });
            }
            doArr(state.menuRouters,json,'');
            return json;
        }
    },
    actions: {
        setToken(val,ssoVal){
            this.token = val;
            localStorage.setItem('token',val);
            if(ssoVal){
                this.ssoToken = ssoVal;
                localStorage.setItem('ssoToken',ssoVal);
            }
        },
        clearToken(val){
            this.token = '';
            localStorage.removeItem('token');
            this.ssoToken = '';
            localStorage.removeItem('ssoToken');
        },
        setUserInfo(info){
            this.userInfo    = info.user || {};
            this.deptPosts   = info.deptPosts || [];
            this.permissions = info.permissions || [];
            sessionStorage.setItem('permissions',JSON.stringify(info.permissions));
            sessionStorage.setItem('isAdmin',info.user.admin?1:0);
            this.changeRole(info.loginDeptPost || {})
        },
        setBusinessPermissions(permissions,business){
            this.businessPermissions[business] = permissions || [];
            sessionStorage.setItem('businessPermissions_'+business,JSON.stringify(permissions || []));
        },

        setMenuRouters(menu){
            this.menuRouters = menu;
        },
        changeRole(deptPost){
            this.deptPost = deptPost || {};
        },
        handleCollapse() {
            this.collapse = !this.collapse;
        },
        spinChange(num){
            if(num==0){
                this.spin = 0;
            }else{
                this.spin += num;
            }
        },

        setDeptTree(data){
            this.deptTree = data || [];
        },
        setPostList(data){
            this.postList = data || [];
        }
    }
})
