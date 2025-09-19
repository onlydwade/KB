import {defineConfig,loadEnv}   from 'vite'
import AutoImport               from 'unplugin-auto-import/vite'
import Components               from 'unplugin-vue-components/vite';
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers';
import vue                      from '@vitejs/plugin-vue'
import viteCompression          from 'vite-plugin-compression';
import legacyPlugin             from '@vitejs/plugin-legacy'
export default defineConfig(({ mode }) =>{
    process.env = {...process.env, ...loadEnv(mode, process.cwd())};
    return {
        resolve : {
            alias: {
                '@': '/src'
            },
        },
        define:{
            GLOBAL_PATH : JSON.stringify({
                api : process.env.VITE_BASE_API,
                oss : process.env.VITE_OSS_URL,
                
                auth      : `https://gis.gem-flower.com/bsh-unify/#/?clientId=noderingadkrstfyulfgetpccc&redirectUri=${process.env.VITE_BASE_API}/auth/login/oauth2/code`,
                ssoUrl    : `https://gis.gem-flower.com`,
                tokenAuth : `${process.env.VITE_BASE_API}/auth/login/oauth2/token?redirectUrl=${encodeURIComponent((process.env.VITE_BASE_URL || process.env.VITE_BASE_API)+'/#/login')}&token=`,
            }),
        },
        css: {
            //css预处理
            preprocessorOptions: {
                less: {
                    modifyVars : {
                        'primary-color'           : '#f99c34', // 全局主色
                        'link-color'              : '#f99c34', // 链接色
                        'success-color'           : '#52c41a', // 成功色
                        'warning-color'           : '#faad14', // 警告色
                        'error-color'             : '#f5222d', // 错误色
                        'font-size-base'          : '14px', // 主字号
                        'heading-color'           : 'rgba(0, 0, 0, 0.8)', // 标题色
                        'text-color'              : 'rgba(0, 0, 0, 0.7)', // 主文本色
                        'text-color-secondary'    : 'rgba(0, 0, 0, 0.45)', // 次文本色
                        'disabled-color'          : 'rgba(0, 0, 0, 0.25)', // 失效色
                        'border-radius-base'      : '4px', // 组件/浮层圆角
                        'border-color-base'       : '#ddd', // 边框色
                        'box-shadow-base'         : '0 2px 8px rgba(0, 0, 0, 0.15)', // 浮层阴影
                        'base-size'               : '16px',
                        'form-item-margin-bottom' : '32px',
                    },
                    javascriptEnabled : true,
                }
            },
        },
        plugins: [
            vue(),
            AutoImport({
                imports   : ['vue','vue-router'],
                resolvers : [AntDesignVueResolver({
                    importStyle  : 'less',
                    resolveIcons : true,
                })],
            }),
            Components({
                resolvers : [AntDesignVueResolver({
                    importStyle  : 'less',
                    resolveIcons : true,
                })],
            }),
            viteCompression({
                deleteOriginFile : false,
                algorithm        : "gzip",
            }),
            legacyPlugin({
                targets:['chrome 52'],  // 需要兼容的目标列表，可以设置多个
                additionalLegacyPolyfills:['regenerator-runtime/runtime'] // 面向IE11时需要此插件
            })
        ],
        server:{
            // host: '0.0.0.0',
            port : 5174,
            // proxy: {
            //     '/bsh-unify-api': {
            //         target       : 'https://gis.gem-flower.com/bsh-unify-api/bsh-unify-api',
            //         changeOrigin : true,
            //         rewrite      : path => path.replace(/^\/bsh-unify-api/, '')
            //     },
            // },
        },
        base  : '/',
        // optimizeDeps:{           //实验功能  升级vite > 4.0 版本后看情况使用
        //     disabled:false
        // },
        build : {
            target        : 'modules',
            outDir        : 'dist', //指定输出路径
            assetsDir     : 'assets', // 指定生成静态资源的存放路径
            rollupOptions : {
                minify : 'terser',// 混淆器，terser构建后文件体积更小
                output : {
                    manualChunks(id) {
                        if (id.includes('node_modules')) {
                            return id.toString().split('node_modules/')[1].split('/')[0].toString();
                        }
                    }
                },
            },
        },
    }
})