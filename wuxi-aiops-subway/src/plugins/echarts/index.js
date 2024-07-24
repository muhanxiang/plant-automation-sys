import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { BarChart, PieChart, LineChart } from 'echarts/charts';
import { GridComponent, LegendComponent, TooltipComponent } from 'echarts/components';

use([
  CanvasRenderer,
  BarChart,
  PieChart,
  LineChart,
  GridComponent,
  LegendComponent,
  TooltipComponent,
]);

import * as echarts from 'echarts';
import theme from './theme.json';
echarts.registerTheme('echarts', theme);
