<template>
  <el-row class="sy-row" :gutter="mode !== stationMode.STATION ? 10 : 0">
    <el-col class="sy-col" :span="5" v-if="mode !== stationMode.STATION">
      <div class="view-aside sy-view">
        <div class="view-aside-search">
          <el-input
            class="search-input"
            v-model.trim="keyword"
            suffix-icon="el-icon-search"
            placeholder="请输入关键词搜索"
            :maxlength="20"
            clearable
            @change="filterTreeData"
          >
          </el-input>
          <el-button class="sy-btn-regular" type="primary" size="medium" @click="handleResetTree">重置</el-button>
        </div>
        <sy-tree
          class="view-aside-tree"
          ref="tree"
          node-key="guid"
          :props="treeProps"
          :load="loadTreeData"
          :filter-node-method="filterTreeNode"
          lazy
          show-checkbox
          check-strictly
          highlight-current
          check-on-click-node
          @check-change="onNodeCheckChange"
        >
          <template v-slot="{ data, node }">
            <div class="el-tree-node__custom">
              <sy-icon v-if="data.icon" :name="data.icon" />
              <span class="el-tree-node__label" :title="node.label">{{ node.label }}</span>
              <i v-if="data.focus" class="el-tree-node__icon el-icon-star-off" />
            </div>
          </template>
        </sy-tree>
      </div>
    </el-col>
    <el-col class="sy-col" :span="mode === stationMode.STATION ? 24 : 19">
      <div :class="`view-main sy-view ${mode === stationMode.STATION ? 'noborder' : ''}`">
        <slot></slot>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { mapGetters } from 'vuex';
import { StationMode } from '@/enums';
import StationTree from '@/mixins/station-tree';

export default {
  name: 'TreeView',
  mixins: [StationTree],
  computed: {
    ...mapGetters(['mode', 'stationId']),
    stationMode() {
      return StationMode;
    },
  },
  data() {
    return {};
  },
  methods: {
    // 重置树选择
    handleResetTree() {
      this.keyword = '';
      this.$refs.tree.filter('');
      this.$refs.tree.setCheckedKeys([]);
    },
    onNodeCheckChange(data, checked) {
      // console.log(data, checked);
      if (checked) {
        this.$refs.tree.setCheckedKeys([data.guid]);
        this.$emit('selected', data.guid);
      } else {
        // this.$emit('selected', this.stationId);
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.sy-row {
  .view-aside {
    width: 100%;
    height: 100%;
    padding: 10px;
    border: $border-default;

    &-search {
      margin-bottom: 20px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .search-input {
        flex: auto;
        margin-right: 10px;
      }
    }
  }
  .view-main {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    padding: 0 10px;
    border: $border-default;
  }
}
</style>
