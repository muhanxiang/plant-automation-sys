export const BASE_URL = process.env.BASE_URL;
export const NODE_ENV = process.env.NODE_ENV;
export const isDev = NODE_ENV === 'development';

const isTLS = location.protocol === 'https:';

export const baseIP = '10.168.1.100';
export const baseWS = isTLS ? 'wss:' : 'ws:';
export const basePort = isTLS ? 8443 : 8000;
// 基础地址
export const baseUrl = isDev ? '/baseUrl' : location.origin;
// 资源地址
export const assetUrl = isDev ? '/baseUrl' : location.origin;
// 流媒体地址
export const mediaUrl = isDev ? `${baseWS}//${baseIP}:${basePort}/video` : `${baseWS}//${location.host}/video`;
// SOCKET地址
export const socketUrl = isDev ? `${baseWS}//${baseIP}:${basePort}/ws` : `${baseWS}//${location.host}/ws`;
