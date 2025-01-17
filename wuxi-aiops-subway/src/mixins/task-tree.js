import { mapGetters } from 'vuex';
import { StationMode } from '@/enums';

export default {
  computed: {
    ...mapGetters(['mode', 'stationId']),
  },
  data() {
    return {
      keyword: '',
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
                checkable: true,
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
                checkable: true,
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
                checkable: true,
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
                checkable: true,
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
                isLeaf: false,
                checkable: true,
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 5:
          this.$api
            .getBaseApi('device', { bay: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
                checkable: true,
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 6:
          this.$api
            .getBaseApi('component', { device: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
                checkable: true,
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 7:
          this.$api
            .getBaseApi('patrolpoint', { component: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: true,
                checkable: true,
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
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
                checkable: true,
              }));
              node.expand();
              resolve(data.children);
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
                level: level + 1,
                isLeaf: false,
                checkable: true,
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 2:
          this.$api
            .getBaseApi('device', { bay: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
                checkable: true,
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 3:
          this.$api
            .getBaseApi('component', { device: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                level: level + 1,
                isLeaf: false,
                checkable: true,
              }));
              resolve(data.children);
            })
            .catch(() => resolve([]));
          break;
        case 4:
          this.$api
            .getBaseApi('patrolpoint', { component: data.guid })
            .then((res) => {
              if (!res?.results) return resolve([]);
              data.children = res.results.map((item) => ({
                ...item,
                isLeaf: true,
                checkable: true,
                level: level + 1,
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
      if (!val) return true;
      return data.name.indexOf(val) > -1;
    },
  },
};
