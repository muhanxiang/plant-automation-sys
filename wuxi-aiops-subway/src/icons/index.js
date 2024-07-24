import Vue from 'vue';
import SvgIcon from '@/components/Icon';

Vue.component('sy-icon', SvgIcon);

const svgIcons = require.context('./svg', false, /\.svg$/);
const requireAll = (ctx) => ctx.keys().map(ctx);
requireAll(svgIcons)
