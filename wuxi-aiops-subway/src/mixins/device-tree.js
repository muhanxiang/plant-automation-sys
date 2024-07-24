import { mapGetters } from 'vuex';
import { StationMode } from '@/enums';

export default {
  computed: {
    ...mapGetters(['mode', 'stationId']),
  },
  data() {
    return {
      keyword: '',
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
      defaultCheckedKeys: [],
    };
  },
  created() {
    this.initTreeData();
  },
  methods: {
    onNodeCheck(data, res) {
      console.log('nodecheck: ', data, res);
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
            const data = await this.$api.getBaseApi('bay', { area: area.guid });
            if (data?.count > 0) {
              const bayList = data.results.map((bay) => ({
                ...bay,
                level: 2,
                isLeaf: true,
                nodeType: 'bay',
                icon: 'bay',
              }));
              treeArea1.push({
                ...area,
                level: 1,
                isLeaf: false,
                children: bayList,
                disabled: data?.count === 0,
                nodeType: 'area',
                icon: 'area',
              });
            } else {
              treeArea2.push({
                ...area,
                level: 1,
                isLeaf: false,
                children: [],
                disabled: data?.count === 0,
                nodeType: 'area',
                icon: 'area',
              });
            }
          }
          this.treeData = [...treeArea1, ...treeArea2];
          this.treeLoad = false;
        });
    },
    loadTreeData(node, resolve) {
      if (this.mode === StationMode.STATION) {
        this.loadSingleTreeData(node, resolve);
      } else {
        this.loadMultiTreeData(node, resolve);
      }
    },
    loadMultiTreeData(node, resolve) {
      const { data, level } = node;
      switch (level) {
        case 0:
          this.treeNode = node;
          this.treeResolve = resolve;
          this.$api
            .getBaseApi('unit')
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
              }));
              node.expand();
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 1:
          this.$api
            .getBaseApi('team', { unit: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 2:
          this.$api
            .getBaseApi('station', { team: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
                nodeType: 'station',
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 3:
          this.$api
            .getBaseApi('area', { station: this.stationId })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
                nodeType: 'area',
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 4:
          this.$api
            .getBaseApi('bay', { area: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: true,
                nodeType: 'bay',
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        default:
          return resolve([]);
      }
    },
    loadSingleTreeData(node, resolve) {
      const { data, level } = node;
      switch (level) {
        case 0:
          this.treeNode = node;
          this.treeResolve = resolve;
          this.$api
            .getBaseApi('area', { station: this.stationId })
            .then((res) => {
              if (!res?.results) return resolve([]);
              const children = res.results.map((item) => ({
                ...item,
                level: 1,
                isLeaf: false,
                nodeType: 'area',
                icon: 'area',
              }));
              node.expand();
              resolve(children);
            })
            .catch(() => resolve([]));
          break;
        case 1:
          this.$api
            .getBaseApi('bay', { area: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: 2,
                isLeaf: true,
                nodeType: 'bay',
                icon: 'bay',
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        // case 2:
        //   this.$api
        //     .getBaseApi('device', { bay: data.guid })
        //     .then((res) => {
        //       if (!res?.results) return resolve([]);
        //       data.children = res.results.map((item) => ({
        //         ...item,
        //         level: 3,
        //         isLeaf: true,
        //         nodeType: 'bay',
        //         icon: 'device',
        //       }));
        //       resolve(data.children);
        //     })
        //     .catch(() => resolve([]));
        //   break;
        default:
          return resolve([]);
      }
    },
    filterTreeData(val) {
      this.$refs.tree.filter(val);
    },
    filterTreeNode(val, data) {
      if (!val) return true;
      return data.name.indexOf(val) > -1;
    },
  },
};
