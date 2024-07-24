import stomp from 'stompjs';
import sockjs from 'sockjs-client';
import { socketUrl } from '@/config';
import { Client } from '@stomp/stompjs';
export default class Stomp {
  constructor(options = {}) {
    this._options = {
      url: socketUrl,
      account: 'admin',
      password: 'qwer1234',
      onConnect: null,
      onDisconnect: null,
      onStompError: null,
      ...options,
    };
    this._client = null;
    this._isConnect = false;
    this._retryTimes = 5; // 重连次数
    this._retryInterval = 3000;

    this._initSockjs();
  }

  // 基于sockjs和@stomp/stompjs创建客户端
  _initSockjs() {
    const { url, account, password, onConnect, onDisconnect, onStompError } = this._options;

    const client = new Client({
      brokerURL: url,
      connectHeaders: {
        login: account,
        passcode: password,
      },
      debug: function () {
        // console.log('debug: ', msg);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 30000,
      heartbeatOutgoing: 30000,
    });
    if (typeof WebSocket !== 'function') {
      client.webSocketFactory = () => new sockjs(url);
    }
    // 客户端连接
    client.onConnect = () => {
      this._isConnect = true;
      onConnect && onConnect();
    };
    // 客户端断开
    client.onDisconnect = () => {
      this._isConnect = false;
      onDisconnect && onDisconnect();
    };
    // 客户端错误
    client.onStompError = (err) => {
      console.log('onStompError: ', err);
      this._isConnect = false;
      onStompError && onStompError();
    };
    // 启用客户端
    client.activate();

    this._client = client;
  }
  // 基于stompjs创建客户端
  _initStomp() {
    const { url } = this._options;

    this._client = stomp.client(url);
    this._client.heartbeat.outgoing = 10000;
    this._client.heartbeat.incoming = 0;
    this._client.debug = null;

    let retryCount = 0;
    const retryCallback = () => {
      if (retryCount < this._retryTimes) {
        setTimeout(() => {
          this._connect(retryCallback);
          retryCount++;
        }, this._retryInterval);
      }
    };
    this._connect(retryCallback);
  }

  _connect(/** errCallback */) {
    if (this._isConnect) return;
    const { account, password, callback } = this._options;
    this._client.connect(
      account,
      password,
      (res) => {
        console.log('Stomp 连接成功: ', res);
        this._isConnect = true;
        callback && callback();
      },
      (err) => {
        console.error('Stomp 连接失败: ', err);
        this._isConnect = false;
        setTimeout(() => {
          this._connect();
        }, this._retryInterval);
        // errCallback && errCallback();
      },
      '/'
    );
  }
  /**
   * 发送消息
   * @param {*} options 配置项
   * @param {*} destination 发送地址
   * @param {*} body 消息内容
   * @param {*} headers 消息头
   * @param {*} binaryBody 二进制内容
   * @param {*} skipContentLengthHeader 禁用内容长度
   */
  publish(options = { destination: '', body: '', headers: {} }) {
    if (!this._isConnect) return;
    this._client.publish(options);
  }
  /**
   * 订阅消息
   * @param {*} destination 订阅地址
   * @param {*} callback 订阅回调
   * @param {*} headers 订阅头
   * @returns 订阅实例
   */
  subscribe({ destination, callback, headers = {} }) {
    if (!this._isConnect) return;
    return this._client.subscribe(destination, callback, headers);
  }
  /**
   * 取消订阅
   * @param {*} subscription 订阅实例
   */
  unsubscribe(subscription) {
    subscription && subscription.unsubscribe();
  }
  /**
   * 注销客户端
   */
  destroy() {
    if (!this._client) return;
    this._client.deactivate && this._client.deactivate();
    this._client = null;
    this._isConnect = false;
  }
}
