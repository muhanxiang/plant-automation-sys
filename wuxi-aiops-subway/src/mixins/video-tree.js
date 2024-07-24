import { mapGetters } from 'vuex';

export default {
  conputed: {
    ...mapGetters(['stationId']),
  },
  data() {
    return {
      keyword: '',
      videoList: [],
      videoIndex: 0,
      treeLoad: false,
      treeNode: null,
      treeResolve: null,
      treeRadio: 'device',
      treeData: [],
      treeProps: {
        label: 'name',
        children: 'children',
        isLeaf: (data) => data.isLeaf,
      },
    };
  },
  created() {
    this.initTreeData();
  },
  methods: {
    onNodeCheck(data, { checkedKeys, checkedNodes }) {
      console.log('nodecheck: ', data, checkedKeys, checkedNodes);
    },
    onNodeClick(data, node) {
      console.log('nodeclick: ', data, node);
    },
    onNodeCheckChange(data, checked) {
      console.log('nodecheckchange: ', data, checked);
    },
    onNodeCurrentChange(data, node) {
      console.log('nodecurrentchange: ', data, node);
    },
    initTreeData() {
      this.treeLoad = true;

      this.$api
        .getBaseApi('area', {
          station: this.stationId,
        })
        .then(async (res) => {
          if (!res?.results) return;

          let treeArea1 = [],
            treeArea2 = [];
          for (let index = 0; index < res.count; index++) {
            const area = res.results[index];
            const data = await this.$api.getBaseApi('camera', { area: area.guid });
            if (data?.count > 0) {
              const cameraList = data.results.map((camera) => ({
                ...camera,
                checkable: true,
                disabled: !camera.enabled,
                isLeaf: true,
                icon: 'camera',
              }));
              treeArea1.push({
                ...area,
                checkable: false,
                children: cameraList,
                disabled: data?.count === 0,
                isLeaf: data?.count === 0,
              });
            } else {
              treeArea2.push({
                ...area,
                checkable: false,
                children: [],
                disabled: data?.count === 0,
                isLeaf: data?.count === 0,
              });
            }
          }
          this.treeData = [...treeArea1, ...treeArea2];
          this.treeLoad = false;
        });
    },
    loadTreeData(node, resolve) {
      const { data, level } = node;
      switch (level) {
        case 0:
          this.treeLoad = true;
          Promise.all([
            this.$api.getBaseApi('area', { station: this.stationId }),
            this.$api.getBaseApi('camera', {
              station: this.stationId,
              area__isnull: true,
            }),
          ])
            .then(([res1, res2]) => {
              if (!res1 && !res2) return resolve([]);
              const arr1 = res1.results.map((item) => ({
                ...item,
                isLeaf: false,
                checkable: false,
              }));
              const arr2 = res2.results.map((item) => ({
                ...item,
                isLeaf: true,
                checkable: true,
                disabled: !item.enabled,
                icon: 'camera',
              }));
              data.children = [...arr1, ...arr2];
              resolve(data.children);
            })
            .catch(() => resolve([]))
            .finally(() => {
              this.treeLoad = false;
            });
          break;
        case 1:
          this.$api
            .getBaseApi('camera', { area: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                isLeaf: true,
                checkable: true,
                disabled: !item.enabled,
                icon: 'camera',
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        default:
          return resolve([]);
      }
    },
    filterTreeData(val) {
      this.$refs.tree.filter(val);
    },
    filterTreeNode(val, data) {
      // console.log('filterTreeNode: ', typeOf(val));
      if (this.keyword && this.cameraType && this.cameraType.length > 0) {
        return data.name.includes(this.keyword) && this.cameraType.includes(data.camera_type);
      } else if (this.keyword) {
        return data.name.includes(this.keyword);
      } else if (this.cameraType && this.cameraType.length > 0) {
        return this.cameraType.includes(data.camera_type);
      } else {
        return true;
      }
    },
  },
};
