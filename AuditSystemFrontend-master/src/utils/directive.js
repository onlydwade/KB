export const permission = {
    // mounted是指令的一个生命周期
    mounted(el, binding) {
        const isAdmin = sessionStorage.getItem('isAdmin') || 0;
        if(isAdmin==0){
            const { value }   = binding
            const permissions = JSON.parse(sessionStorage.getItem('permissions') || '[]');
            if (value && value.length > 0) {
                const hasPermission  = permissions.some(role => {
                    return value.includes(role)
                })
                if (!hasPermission) {
                    // el.style.display = 'none';
                    el.parentNode.removeChild(el);
                }
            }
        }
        
    }
}
export const permissionInvestment = {
    // mounted是指令的一个生命周期
    mounted(el, binding) {
        const isAdmin = sessionStorage.getItem('isAdmin') || 0;
        if(isAdmin==0){
            const { value }   = binding
            // let permissions = JSON.parse(sessionStorage.getItem('permissions') || '[]');
            let businessPer = JSON.parse(sessionStorage.getItem('businessPermissions_investment') || '[]');
            if (value && value.length > 0) {
                const hasPermission  = businessPer.some(role => {
                    return value.includes(role)
                })
                if (!hasPermission) {
                    // el.style.display = 'none';
                    el.parentNode.removeChild(el);
                }
            }
        }
        
    }
}