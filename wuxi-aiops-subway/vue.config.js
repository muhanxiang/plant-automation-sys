const path = require('path');

const isProd = process.env.NODE_ENV === 'production';

const resolve = (dir) => path.join(__dirname, dir);

module.exports = {
  publicPath: process.env.BASE_URL, // 部署基础路径, History路由和多页面时必须使用绝对路径!
  outputDir: 'dist', // 构建文件目录, 添加--no-clean可避免构建时文件被清除!
  assetsDir: 'static', // 静态资源目录
  indexPath: 'index.html', // Html输出路径
  filenameHashing: true, // 是否开启文件名哈希
  lintOnSave: false, // 是否开启编译提示
  runtimeCompiler: false, // 是否使用运行时编译版本, 应用体积会增加10kb左右!
  transpileDependencies: ['swiper', 'vue-echarts', 'resize-detector'], // 添加Babel显式转义
  productionSourceMap: false, // 是否开启生产环境SourceMap
  // crossorigin: undefined,// 设置标签crossorigin属性
  integrity: false, // 是否启用标签Subresource Integrity
  configureWebpack: (config) => {
    config.externals = {
      BMap: 'BMap',
    };
  },
  chainWebpack: (config) => {
    // 修复HMR
    config.resolve.symlinks(true);
    // 设置别名
    config.resolve.alias
      .set('@', resolve('src'))
      .set('@a', resolve('src/assets'))
      .set('@c', resolve('src/components'))
      .set('@m', resolve('src/mixins'))
      .set('@s', resolve('src/styles'))
      .set('@v', resolve('src/views'));
    // 设置标题
    config.plugin('html').tap((args) => {
      args[0].title = '厂站自动化管控系统';
      return args;
    });
    // 配置SVG
    config.module.rule('svg').exclude.add(resolve('src/icons')).end();
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]',
      })
      .end();
    if (isProd) {
      // 图片压缩
      // config.module
      //   .rule('images')
      //   .test(/\.(jpe?g|png|gif)(\?.*)?$/)
      //   .use('image-webpack-loader')
      //   .loader('image-webpack-loader')
      //   .options({
      //     optipng: { enabled: false },
      //     gifsicle: { interlaced: false },
      //     mozjpeg: { progressive: true, quality: 75 },
      //     pngquant: { quality: [0.75, 0.9], speed: 4 },
      //   });
      // Webpack运行时代码块分割
      config.optimization.runtimeChunk('single');
      config.optimization.splitChunks({
        chunks: 'async', // all/async/initial
        minSize: 30000, // 代码块最小体积
        minChunks: 1, // 代码块最小引用次数
        maxAsyncRequests: 5, // 代码块最大异步请求数
        maxInitialRequests: 3, // 代码块最大初始请求数
        name: true, // 代码块名称
        cacheGroups: {
          element: {
            name: 'chunk-element',
            priority: 10,
            chunks: 'all',
            test: /[\\/]node_modules[\\/]element-ui[\\/]/,
          },
          vendors: {
            priority: -10,
            test: /[\\/]node_modules[\\/]/,
          },
          default: {
            minChunks: 2,
            priority: -20,
            reuseExistingChunk: true,
          },
        },
      });
      // 移除注释和打印
      config.optimization.minimizer('terser').tap((args) => {
        args[0].terserOptions.output = { comments: false };
        args[0].terserOptions.compress.drop_console = true;
        args[0].terserOptions.compress.drop_debugger = true;
        args[0].terserOptions.compress.pure_funcs = ['console.log'];
        return args;
      });
    }
  },
  css: {
    requireModuleExtension: true, // 是否开启CSS Module后缀
    // extract: true, // 是否提取CSS, 不兼容CSS热重载开发环境下关闭!
    sourceMap: false, // 是否开启CSS SourceMap, 开启后会影响构建性能!
    loaderOptions: {
      scss: {
        // 引入全局变量, 在sass-loader^7.0中该选项名是'data'
        prependData: '@import "~@/styles/mixin.scss";@import "~@/styles/variables.scss";',
      },
    },
  },
  devServer: {
    hot: true,
    https: true,
    open: true,
    port: 9000,
    proxy: {
      '/baseUrl': {

        //target: 'https://192.168.3.40:8443',
        target: 'https://192.168.3.40:8443',
        ws: false,
        secure: false,
        logLevel: 'info',
        changeOrigin: true,
        pathRewrite: {
          '^/baseUrl': '',
        },
        router: {},
      },
    },
  },
  parallel: require('os').cpus().length > 1, // 多核时自动启用
  pluginOptions: {}, // 第三方插件配置
  pwa: {}, // PWA插件配置
};
