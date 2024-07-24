import dayjs from 'dayjs';

export default {
  computed: {
    pickerOptions() {
      // 禁用日期选项
      const disableOption = {
        disabledDate: (date) => {
          return this.$dayjs(date).isAfter(this.$dayjs(), 'day');
        },
      };
      return this.isShortcuts
        ? {
            ...disableOption,
            shortcuts: [
              {
                text: '今天',
                onClick(picker) {
                  const start = dayjs().startOf('day').format('YYYY-MM-DD HH:mm:ss');
                  const end = dayjs().endOf('day').format('YYYY-MM-DD HH:mm:ss');
                  picker.$emit('pick', [start, end]);
                },
              },
              {
                text: '本周',
                onClick(picker) {
                  const start = dayjs().startOf('week').format('YYYY-MM-DD HH:mm:ss');
                  const end = dayjs().endOf('week').format('YYYY-MM-DD HH:mm:ss');
                  picker.$emit('pick', [start, end]);
                },
              },
              {
                text: '本月',
                onClick(picker) {
                  const start = dayjs().startOf('month').format('YYYY-MM-DD HH:mm:ss');
                  const end = dayjs().endOf('month').format('YYYY-MM-DD HH:mm:ss');
                  picker.$emit('pick', [start, end]);
                },
              },
            ],
          }
        : disableOption;
    },
  },
  data() {
    return {
      isShortcuts: false, // 是否有快捷键
    };
  },
};
