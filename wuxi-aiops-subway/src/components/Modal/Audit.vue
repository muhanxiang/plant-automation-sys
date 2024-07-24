<template>
  <el-dialog
    width="500"
    custom-class="sy-dialog sy-dialog--mini"
    :visible.sync="isVisible"
    :title="title"
    center
    @open="onDialogOpen"
  >
    <el-form class="dialog-aside" ref="form" size="small" label-width="120px" :model="formModel" :rule="formRules">
      <div class="dialog-aside__body">
        <el-form-item label="是否通过:">
          <el-radio-group class="sy-radio-group" v-model="formModel.approved">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="0" style="margin-left: 20px">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="意见内容:">
          <el-input
            class="sy-input-textarea"
            v-model="formModel.approval_comments"
            type="textarea"
            placeholder="请输入审核意见内容"
            resize="none"
            :maxlength="50"
            :rows="10"
          ></el-input>
        </el-form-item>
      </div>
    </el-form>

    <template #footer>
      <el-button class="sy-btn-regular" type="warning" @click="onDialogAction('cancel')">取 消</el-button>
      <el-button class="sy-btn-regular" type="primary" @click="onDialogAction('submit')">提 交</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  name: 'AuditModal',
  computed: {
    ...mapGetters(['username']),
    isVisible: {
      get() {
        return this.visible;
      },
      set(bool) {
        this.$emit('update:visible', bool);
      },
    },
    formRules() {
      return {};
    },
  },
  props: {
    title: {
      type: String,
      default: '审核确认',
    },
    visible: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      formModel: {
        approval_comments: '',
        approved: 1,
        approver: '',
      },
    };
  },
  methods: {
    onDialogOpen() {
      this.formModel = {
        approval_comments: '',
        approved: 1,
        approver: this.username,
      };
    },
    onDialogAction(action) {
      switch (action) {
        case 'cancel':
          this.isVisible = false;
          break;
        case 'submit':
          this.$emit('submit', this.formModel);
          break;
        default:
          break;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.sy-input-textarea {
  width: 80%;
}
</style>
