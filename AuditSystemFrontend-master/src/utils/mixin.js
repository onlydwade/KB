import moment from 'moment';
const baseMixin = {
    data() {
        return {
        }
    },
    methods: {
        timeOverRule(rule,value,callback,startTime){
            if(value){
                if(value<startTime){
                    return new Promise((resolve, reject) => {
                        reject('结束时间不能早于开始时间')
                    });
                }
            }
            return Promise.resolve();
        },
        dateFormat(date,fmt){
            let fmtStr = fmt || 'YYYY-MM-DD HH:mm:ss';
            if(date){
                return moment(date).format(fmtStr);
            }else{
                return '-'
            }
        },
    }
}
export default baseMixin;