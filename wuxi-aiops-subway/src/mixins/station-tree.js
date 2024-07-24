export default {
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
                checkable: false,
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
                checkable: false,
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
    filterTreeData(val) {
      this.$refs.tree.filter(val);
    },
    filterTreeNode(val, data) {
      if (!val) return true;
      return data.name.indexOf(val) > -1;
    },
  },
};
