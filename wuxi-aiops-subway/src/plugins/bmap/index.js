import { assetUrl } from '@/config';

window.BMap_loadScriptTime = new Date().getTime();
window.BMapCFG = {
  home: '/bmap/', // 地图基础路径
  imgext: '.png', // 地图瓦片文件后缀
  customstyle: '', // 地图自定义样式文件路径 bmap/customstyle/
  tiles_dir: `${assetUrl}/data/bmap/tiles`, // 地图瓦片文件路径 bmap/tiles/
  tiles_v_dir: `${assetUrl}/data/bmap/tiles_v`, // 矢量瓦片图文件路径 bmap/tiles_v/
  tiles_road_dir: '', // 地图路网文件路径 bmap/tiles_road/
  tiles_satellite_dir: '', // 卫星地图文件路径 bmap/tiles_satellite/
};
(function () {
  // window.BMap = window.BMap || {};
  // window.BMap.apiLoad = function () {
  //   delete window.BMap.apiLoad;
  // };
  const script = document.createElement('script');
  script.type = 'text/javascript';
  script.src = '/bmap/bmap.min.js';
  document.head.appendChild(script);
  // document.write(`<script type="text/javascript" src='./bmap/bmap.min.js'></script>`);
})();
