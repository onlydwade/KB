import { defineStore } from 'pinia'
import apiCommon       from '@/api/common';
export const useCityStore = defineStore('city', {
    state: () => {
        return {
            areaTree : [],      //省市区
            cityTree : [],     //省市
        }
    },
    getters: {
        getCity:(state)=>(type)=>{
            if(type=='province'){
                return state.cityTree.map(item=>{
                    item.subAreaList = null;
                    return item;
                });
            }else{
                return state[type+'Tree'];
            }
        },
    },  
    actions: {
        getSetAreaTree(){
            if(this.areaTree.length==0){
                apiCommon.listProvinceCityArea().then(res=>{
                    if(res.code==200){
                        this.areaTree = res.data;
                    }
                })
            }
        },
        geTreeData(type){
            if(type=='city'&&this.cityTree.length==0){
                apiCommon.listProvinceCity().then(res=>{
                    if(res.code==200){
                        this.cityTree = res.data;
                    }
                })
            }
            if(type=='area'&&this.areaTree.length==0){
                apiCommon.listProvinceCityArea().then(res=>{
                    if(res.code==200){
                        this.areaTree = res.data;
                    }
                })
            }
        },
    }
})