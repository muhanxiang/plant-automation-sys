<template>
  <div class="index">
    <header class="index-header home-title">
      <span>台账</span>
      <span class="index-header__title">运行指标</span>
      <span>部件</span>
    </header>
    <main class="index-main">
      <ul class="index-ledger">
        <li class="index-ledger__item">
          <span>设备名称：</span>
          <span>{{ ledgerInfo.name }}</span>
        </li>
        <li class="index-ledger__item">
          <span>设备型号：</span>
          <span>{{ ledgerInfo.model }}</span>
        </li>
        <li class="index-ledger__item">
          <span>生产厂家：</span>
          <span>{{ ledgerInfo.factory }}</span>
        </li>
        <li class="index-ledger__item">
          <span>生产日期：</span>
          <span>{{ ledgerInfo.date }}</span>
        </li>
        <li class="index-ledger__item">
          <span>额定容量：</span>
          <span>{{ ledgerInfo.capacity }}</span>
        </li>
        <li class="index-ledger__item">
          <span>电压等级：</span>
          <span>{{ ledgerInfo.voltage }}</span>
        </li>
      </ul>
      <el-divider direction="vertical"></el-divider>
      <sy-chart class="index-component" type="bar" :option="componentOption" />
    </main>
  </div>
</template>

<script>
import { randomInteger } from '@/utils';

export default {
  name: 'RunningIndex',
  data() {
    return {
      ledgerInfo: {
        name: '升压站一号主变',
        model: 'TF009UL',
        factory: 'TF009UL',
        date: '2021年11月6日',
        capacity: '10000kW',
        voltage: '500kV',
      },
      componentOption: {
        grid: {
          top: '3%',
          left: '3%',
          right: '5%',
          bottom: '5%',
          containLabel: true,
        },
        xAxis: {
          type: 'value',
          position: 'top',
          inverse: true,
          axisLine: { show: false },
          axisTick: {
            show: true,
            lineStyle: { color: '#03CEFB' },
          },
          axisLabel: {
            show: true,
            margin: 10,
            color: '#004F95',
            fontSize: 16,
            fontStyle: 'italic',
            fontFamily: 'DS-DIGIB',
          },
          splitLine: { show: false },
        },
        yAxis: {
          type: 'category',
          position: 'right',
          inverse: true,
          axisLine: { show: true },
          axisTick: { show: false },
          axisLabel: { interval: 0 },
          splitLine: { show: false },
        },
        legend: { show: false },
        dataset: {
          source: ['本体', '套管', '非电量', '分接开关', '冷却系统'].map((item) => {
            return { name: item, value: randomInteger(10) };
          }),
        },
        series: [
          {
            type: 'bar',
            name: '部件指标统计',
            barMaxWidth: 20,
            datasetIndex: 0,
            showBackground: true,
            backgroundStyle: {
              color: '#04182B',
              opacity: 1,
            },
            itemStyle: {
              color: {
                type: 'linear',
                x: 1,
                y: 0,
                x2: 0,
                y2: 0,
                colorStops: [
                  { offset: 0, color: '#00152F' },
                  { offset: 1, color: '#035FA9' },
                ],
              },
            },
          },
        ],
        encode: { x: 'name', y: 'value' },
      },
    };
  },
};
</script>

<style lang="scss" scoped>
.index {
  width: 100%;
  height: 30vh;
  padding: 20px 40px;
  background-color: #021b31;

  &-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    // margin-bottom: 1vh;

    &__title {
      width: 200px;
      height: 50px;
      line-height: 50px;
      font-size: 24px;
      text-align: center;
      background: url('~@/assets/img/bg_index.png') no-repeat center/100% 100%;
    }
  }

  &-main {
    display: flex;
    align-items: center;
  }
  &-ledger {
    width: 30vw;
    color: $text-regular;
    font-size: 16px;

    &__item {
      display: flex;
      align-items: center;
      margin-bottom: 1vh;

      > span:first-child {
        width: 100px;
      }
      > span:last-child {
        font-size: 18px;
      }
    }
  }
  &-component {
    width: 30vw;
    height: 20vh;
  }

  .el-divider {
    height: 20vh;
    background-color: #035fa9;
  }
}
</style>
