<template>
  <div class="sy-tree">
    <el-input
      v-model.trim="keyword"
      class="sy-tree-search"
      placeholder="请输入关键字搜索"
      :maxlength="20"
      clearable
      @change="filterTreeData"
    />
    <el-tree
      ref="tree"
      node-key="id"
      :props="treeProps"
      :load="loadTreeNode"
      :filter-node-method="filterTreeNode"
      lazy
      accordion
      show-checkbox
      check-strictly
      highlight-current
      @check="onNodeCheck"
      @node-click="onNodeClick"
      @check-change="onNodeCheckChange"
      @current-change="onNodeCurrentChange"
    ></el-tree>
  </div>
</template>

<script>
import ElTree from './tree';

export default {
  name: 'SyTree',
  components: {
    ElTree,
  },
  data() {
    return {
      keyword: '',
      treeData: [],
      treeProps: {
        label: 'name',
        isLeaf: (data) => data.isLeaf,
      },
    };
  },
  methods: {
    onNodeClick(data, node) {
      console.log('nodeclick: ', data, node);
    },
    onNodeCheck(data, res) {
      console.log('nodecheck: ', data, res);
    },
    onNodeCheckChange(data, checked) {
      console.log('nodecheckchange: ', data, checked);
      if (checked) {
        this.$refs.tree.setCheckedKeys([data.id]);

        const { id, out_code, video_address1 } = data;
        this.checkedKeys = [id];
        if (out_code.includes('_')) {
          this.videoUrl = `wss://${location.hostname}:8443/${video_address1}`;
          const [nvrId, channelNo] = out_code.split('_');
          this.nvrId = nvrId;
          this.channelNo = channelNo;
          this.getVideoList();
        } else {
          this.videoUrl = video_address1;
        }
      }
    },
    onNodeCurrentChange(data, node) {
      console.log('nodecurrentchange: ', data, node);
    },
    loadTreeNode(node, resolve) {
      console.log(node);
      const { data, level } = node;
      switch (level) {
        case 0:
          this.$api.getBaseApi('maintenanceunit').then((res) => {
            if (!res?.results) return resolve([]);
            const children = res.results.map((item) => ({
              ...item,
              isLeaf: false,
              checkable: false,
            }));
            return resolve(children);
          });
          break;
        case 1:
          this.$api.getBaseApi('maintenanceteam', { maintenanceunit: data.id }).then((res) => {
            if (!res?.results) return resolve([]);
            const children = res.results.map((item) => ({
              ...item,
              isLeaf: false,
              checkable: false,
            }));
            return resolve(children);
          });

          break;
        case 2:
          this.$api.getBaseApi('substation', { maintenanceteam: data.id }).then((res) => {
            if (!res?.results) return resolve([]);
            const children = res.results.map((item) => ({
              ...item,
              isLeaf: false,
              checkable: false,
            }));
            return resolve(children);
          });

          break;
        case 3:
          this.$api.getBaseApi('detector', { substation: data.id }).then((res) => {
            if (!res?.results) return resolve([]);
            const children = res.results.map((item) => ({
              ...item,
              isLeaf: true,
              checkable: true,
              // disabled: !item.status,
            }));
            return resolve(children);
          });
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
</script>

<style lang="scss" scoped>
.sy-tree {
  width: 100%;
  height: 100%;
  overflow: auto;
}
</style>
