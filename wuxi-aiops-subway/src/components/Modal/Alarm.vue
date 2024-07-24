<template>
  <el-dialog custom-class="sy-dialog sy-dialog--medium" :visible.sync="isVisible" :title="title" center>
    <div></div>
    <template #footer>
      <el-button class="sy-btn-regular" type="warning" @click="onDialogAction('cancel')">取 消</el-button>
      <el-button class="sy-btn-regular" type="primary" @click="onDialogAction('submit')">提 交</el-button>
    </template>
  </el-dialog>
</template>

<script>
// 特殊审核，需要修改字段
export default {
  name: 'AlarmModal',
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
      default: '告警审核',
    },
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
  methods: {
    onDialogAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          this.$emit('submit');
          this.isVisible = false;
          break;
        default:
          break;
      }
    },
  },
};
</script>
