<template>
  <el-dialog
    custom-class="sy-dialog sy-dialog--medium"
    :title="title"
    :visible.sync="isVisible"
    :append-to-body="appendToBody"
    :destroy-on-close="destroyOnClose"
    center
    @open="onDialogOpen"
    @close="onDialogClose"
  >
    <el-row class="device sy-row">
      <el-col class="device-model sy-col" :span="16">
        <BabylonModel @pick-mesh="onComponentPicked"></BabylonModel>
      </el-col>
      <el-col class="device-info sy-col" :span="8">
        <div class="device-ledger">
          <div class="device-ledger__header sy-title">设备台账</div>
          <ul class="device-ledger__info">
            <li>
              设备型号：<span>{{ deviceData.model || '-' }}</span>
            </li>
            <li>
              生产厂家：<span>{{ deviceData.manufacturer || '-' }}</span>
            </li>
            <li>
              投运日期：<span>{{ deviceData.use_date || '-' }}</span>
            </li>
          </ul>
        </div>
        <div class="device-component">
          <div class="device-component__header">{{ componentName }}</div>
          <ul class="device-component__info">
            <li v-for="(item, key) in componentData" :key="key">
              <span>{{ item.name }}</span>
              <span>{{ item.value }}</span>
            </li>
          </ul>
        </div>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
import BabylonModel from '@/components/Model/Babylon';

export default {
  name: 'DeviceModal',
  components: {
    BabylonModel,
  },
  computed: {
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
  },
  props: {
    title: {
      type: String,
      default: '主变变压器',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    appendToBody: {
      type: Boolean,
      default: true,
    },
    destroyOnClose: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      deviceData: {
        model: 'TF009UL',
        use_date: '2021年12月6日',
        manufacturer: 'TOSHIBA',
      },
      componentName: '设备部件名称',
      componentData: {
        key1: { name: '本体温度', value: '67.3°C' },
        key2: { name: '油枕外观', value: '正常' },
        key3: { name: '散热器组温度', value: '55.5°C' },
      },
    };
  },
  methods: {
    onDialogOpen() {},
    onDialogClose() {},
    onComponentPicked(component) {
      this.componentName = component.name;
    },
  },
};
</script>

<style lang="scss" scoped>
.device {
  border: $border-default;

  &-info {
    border-left: $border-default;
  }

  &-ledger {
    &__header {
      height: 4vh;
      line-height: 4vh;
      font-size: 20px;
      color: $text-primary;
    }
    &__info {
      width: 100%;
      height: 12vh;
      padding: 15px;
      font-size: 16px;
      color: $text-regular;

      > li {
        display: flex;
        align-items: center;
        // justify-content: center;
        width: 100%;
        height: 3vh;

        > span {
          font-size: 18px;
          color: $text-secondary;
        }
      }
    }
  }

  &-component {
    &__header {
      width: 100%;
      height: 5vh;
      line-height: 5vh;
      color: $text-primary;
      font-size: 24px;
      text-align: center;
      background-color: #002e51;
    }
    &__info {
      width: 100%;
      height: 33vh;
      overflow-y: auto;

      > li {
        display: flex;
        align-items: center;
        // justify-content: center;
        width: 100%;

        > span {
          width: 50%;
          height: 4vh;
          line-height: 4vh;
          text-align: center;
          border: $border-default;

          &:first-child {
            font-size: 16px;
            color: $text-regular;
          }
          &:last-child {
            font-size: 18px;
            color: $text-secondary;
          }
        }
      }
    }
  }
}
</style>
