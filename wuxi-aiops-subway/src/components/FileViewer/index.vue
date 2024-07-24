<template>
  <div class="file-viewer">
    <div class="file-viewer__info">
      <slot name="info"></slot>
    </div>

    <template v-if="data.file_path">
      <Identify
        v-if="type === fileType.IDENTIFY"
        v-bind="$attrs"
        v-on="$listeners"
        :data="data"
        :image-size="imageSize"
      ></Identify>
      <FirTemp
        v-else-if="type === fileType.FIRTEMP"
        v-bind="$attrs"
        v-on="$listeners"
        :data="data"
        :image-size="imageSize"
      ></FirTemp>
      <sy-player v-else-if="type === fileType.VIDEO" v-on="$listeners" :video-url="videoUrl"></sy-player>
      <sy-image
        v-else-if="type === fileType.IMAGE"
        class="sy-firtemp-image"
        :src="imgUrl"
        :image-size="imageSize"
      ></sy-image>
      <sy-empty v-else :image-size="imageSize"></sy-empty>
    </template>

    <sy-empty v-else description="文件路径为空"></sy-empty>
  </div>
</template>

<script>
import { FileType } from '@/enums';
import { mediaUrl } from '@/config';
import { getFirFullPath, getImgFullPath } from '@/utils';

import FirTemp from '../Canvas/FirTemp';
import Identify from '../Canvas/Identify';

export default {
  name: 'FileViewer',
  components: {
    FirTemp,
    Identify,
  },
  computed: {
    fileType() {
      return FileType;
    },
    type({ data }) {
      if (/\.(png|jpe?g)$/.test(data.file_path)) {
        return FileType.IDENTIFY;
      } else if (/\.fir$/.test(data.file_path)) {
        return FileType.FIRTEMP;
      } else if (/\.mp3$/.test(data.file_path)) {
        return FileType.AUDIO;
      } else if (/\.mp4$/.test(data.file_path)) {
        return FileType.VIDEO;
      } else {
        return data.file_type;
      }
    },
    firUrl() {
      return this.data.file_path && getFirFullPath(this.data.file_path);
    },
    imgUrl() {
      return this.data.file_path && getImgFullPath(this.data.file_path);
    },
    videoUrl() {
      return `${mediaUrl}/${this.data.nvr_id}/${this.data.channel_no}.flv`;
    },
  },
  props: {
    data: {
      type: Object,
      default: () => ({}),
    },
    imageSize: {
      type: Number,
      defailt: 80,
    },
  },
  methods: {
    handleSelect(res) {
      this.$emit('select', res);
    },
  },
};
</script>

<style lang="scss" scoped>
.file-viewer {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  width: 100%;
  height: 100%;

  &__info {
    z-index: 100;
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    padding: 10px;
    font-size: 14px;
  }
}
</style>
