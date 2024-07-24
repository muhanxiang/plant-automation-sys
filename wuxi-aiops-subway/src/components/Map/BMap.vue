<template>
  <div class="sy-bmap">
    <div id="bmap" ref="bmap" class="sy-bmap-container"></div>
  </div>
</template>

<script>
export default {
  name: 'BMap',
  props: {
    zoom: {
      type: Number,
      default: 13,
    },
    center: {
      type: Object,
      default: function () {
        return { lng: 120.312833, lat: 31.593039 };
      },
    },
  },
  data() {
    return {
      bmap: null,
    };
  },
  mounted() {
    this.initBMap();
  },
  methods: {
    initBMap() {
      if (!this.center) return;
      this.bmap = new BMap.Map('bmap', { minZoom: 10, maxZoom: 15 });
      // const tileLayer = new BMap.TileLayer();
      // tileLayer.getTilesUrl = function (tileCoord, zoom) {
      //   var x = tileCoord.x;
      //   var y = tileCoord.y;
      //   var url = `tiles/${zoom}/${x}/${y}/.jpg`;
      //   return url;
      // };
      // this.bmap.addTileLayer(tileLayer);
      const { lng, lat } = this.center;
      this.bmap.centerAndZoom(new BMap.Point(lng, lat), this.zoom);
      this.bmap.enableScrollWheelZoom(true);
      // this.addMarker({ id: 'SSJD', name: '白银变电站', longitude: lng, latitude: lat });
      this.getStationData();
    },
    addMarker(station) {
      const { id, name, longitude, latitude } = station;
      const point = new BMap.Point(longitude, latitude);
      // const icon = new BMap.Icon(require('@/assets/images/icon_station.png'), new BMap.Size(20, 20), {
      //   imageSize: new BMap.Size(20, 20),
      // });
      const label = new BMap.Label(name, { offset: new BMap.Size(10, 20) });
      label.setStyle({
        border: 'none',
        fontWeight: 'bold',
        backgroundColor: 'tranparent',
        transform: 'translateX(-50%)',
      });
      const marker = new BMap.Marker(point /** { icon: icon } */);
      const clickHandler = () => {
        Promise.all([
          this.$api.getBaseApi('detector', { substation: id, dec_type__in: '1,4,5' }),
          this.$api.postHistoryApi('historytask', '_search', {
            size: 0,
            query: { match: { substation_id: id } },
          }),
        ])
          .then(([res1, res2]) => {
            const content = `<ul class="bmap-bubble">
              <li>站所名称：${name}</li>
              <li>机器人数量：${res1.count}</li>
              <li>巡视任务数量：${res2.hits.total.value}</li>
            </ul>`;
            const infoWindow = new BMap.InfoWindow(content, { width: 0, height: 0 });
            marker.openInfoWindow(infoWindow);
          })
          .catch(() => {
            this.$message.warn('暂无该变电站信息！');
          });
      };
      // label.addEventListener('click', clickHandler);
      marker.addEventListener('click', clickHandler);
      marker.setLabel(label);
      this.bmap.addOverlay(marker);
    },
    createInfoWindow(id) {
      return new Promise.all([
        this.$api.getBaseApi('detector_count', { substation: id, dec_type__in: '1,4,5' }),
        this.$api.postHistoryApi('historytask', '_search', {
          size: 0,
          query: { match: { substation_id: id } },
        }),
      ])
        .then(([robot, task]) => {
          const content = `<div style="margin-top: 5px">机器人数量：${robot.count}</div><div style="margin-top: 5px">巡视任务数量：${task.hits.total.value}</div>`;
          const infoWindow = new BMap.InfoWindow(content, { width: 0, height: 0 });
          return Promise.resolve(infoWindow);
        })
        .catch((err) => Promise.reject(err));
    },
    getStationData() {
      this.$api.getBaseApi('station').then((res) => {
        if (!res?.results) return;
        res.results.forEach((item) => {
          if (item.longitude && item.latitude) {
            this.addMarker(item);
          }
        });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-bmap {
  position: relative;
  width: 100%;
  height: 100%;

  &-container {
    width: 100%;
    height: 100%;
  }
}
</style>
