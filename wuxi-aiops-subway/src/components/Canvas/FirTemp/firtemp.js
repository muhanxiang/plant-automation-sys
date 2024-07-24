import axios from 'axios';
import Config from '../config';

// class AsyncConstructor {
//   constructor(asyncConstructor) {
//     const init = (async () => {
//       await asyncConstructor();
//       delete this.then;
//       return this;
//     })();
//     this.then = init.then.bind(init);
//   }
// }
export default class Firtemp {
  constructor(url, showTemp = false) {
    this.url = url;
    this.image = null;
    this.firData = [];
    this.firInfo = null;
    this.firTemp = null;
    this.showTemp = showTemp;

    const init = (async () => {
      await this.initFirTemp(url);
      delete this.then;
      return this;
    })();
    this.then = init.then.bind(init);
  }
  /**
   * 获取FIR文件
   * @param {*} url 文件地址
   * @param {*} callback 事件回调
   * @returns
   */
  initFirTemp(url) {
    if (!url) return console.error('未找到FIR文件地址！');

    return new Promise((resolve, reject) => {
      axios
        .get(url, { responseType: 'arraybuffer' })
        .then(async (res) => {
          if (res.status === 200) {
            const buffer = res.data;
            this.getFirInfo(buffer);
            this.image = await this.getFirImage(true);
            // console.log('this.image: ', this.image);
            resolve();
          } else {
            console.error(res.statusText);
            reject(res.statusText);
          }
        })
        .catch((err) => {
          reject(err);
        });
    });
  }
  /**
   * 获取FIR文件信息
   * @param {*} buffer 文件数据
   * @returns
   */
  getFirInfo(buffer) {
    if (!buffer || !buffer.byteLength) {
      return console.error('暂无FIR文件数据！');
    }

    const view = new DataView(buffer);
    const sign = view.getUint32(0, true);
    if (sign !== 5392710) {
      return console.error('FIR文件格式错误！');
    }

    const transmittance = view.getFloat32(4, true);
    const emissivity = view.getFloat32(8, true);
    const distance = view.getFloat32(12, true);
    const temperature = view.getFloat32(16, true);
    const humidity = view.getFloat32(20, true);
    const height = view.getInt16(24, true);
    const width = view.getInt16(26, true);
    this.firInfo = { sign, transmittance, emissivity, distance, temperature, humidity, width, height };

    let data = new Array(height * width),
      max = -Math.pow(2, 16),
      min = Math.pow(2, 16),
      maxX = 0,
      maxY = 0,
      minX = 0,
      minY = 0;
    for (let j = 0; j < height; j++) {
      for (let i = 0; i < width; i++) {
        const dot = view.getInt16(64 + (i + j * width) * 2, true);
        if (dot > max) {
          max = dot;
          maxX = i;
          maxY = j;
        }
        if (dot < min) {
          min = dot;
          minX = i;
          minY = j;
        }
        data[i + j * width] = dot;
      }
    }
    this.firData = data;

    const maxTemp = max,
      minTemp = min,
      diff = maxTemp - minTemp < 1 ? 1 : maxTemp - minTemp;
    this.firTemp = { maxTemp, maxX, maxY, minTemp, minX, minY, diff };
    // 绘制生成图片地址
    // this.getFirImage();
  }
  /**
   * 获取FIR温度
   * @param {*} param 测量区域
   * @returns
   */
  getFirTemp({ x = 0, y = 0, w = 10, h = 10 }) {
    if (!this.firInfo) return;

    const width = this.firInfo.width || 0;
    // if (w === 0 && h === 0) {
    //   return {
    //     type: 'point',
    //     temp: this.firData[width * y + x],
    //     coord: { x, y },
    //   };
    // }
    let maxTemp = -Math.pow(2, 16),
      minTemp = Math.pow(2, 16),
      maxCoord,
      minCoord;
    for (let j = y; j <= y + h; j++) {
      for (let i = x; i <= x + w; i++) {
        const temp = this.firData[width * j + i];
        if (temp > maxTemp) {
          maxTemp = temp;
          maxCoord = { x: i, y: j };
        }
        if (temp < minTemp) {
          minTemp = temp;
          minCoord = { x: i, y: j };
        }
      }
    }
    // console.log('FirTemp: ', maxTemp, minTemp);
    return {
      type: 'area',
      maxTemp,
      maxCoord,
      minTemp,
      minCoord,
    };
  }
  /**
   * 获取FIR图片
   * @param {*} blob 是否BLOB
   * @param {*} callback 事件回调
   * @returns
   */
  getFirImage(blob = false, type = 'image/jpg', quality = 0.8, fontStyle = 'bold 12px Arial') {
    if (!this.firInfo || !this.firTemp) return;

    const { width, height } = this.firInfo;
    const { maxTemp, maxX, maxY, minTemp, minX, minY, diff } = this.firTemp;

    const canvas = document.createElement('canvas');
    canvas.width = width;
    canvas.height = height;
    const ctx = canvas.getContext('2d');
    for (let j = 0; j < height; j++) {
      for (let i = 0; i < width; i++) {
        const c = Math.round((255 * (this.firData[j * width + i] - minTemp)) / diff);
        const r = this.red(c) & 0xff;
        const g = this.green(c) & 0xff;
        const b = this.blue(c) & 0xff;
        ctx.fillStyle = `rgba(${r},${g},${b})`;
        ctx.fillRect(i, j, 1, 1);
      }
    }
    if (this.showTemp) {
      // 限制最高/低温文字超出边界
      const textWidth = 15;
      const textHeight = 10;
      let textMaxX = maxX < textWidth ? textWidth : maxX > width - textWidth ? width - textWidth : maxX;
      let textMaxY = maxY < textHeight ? textHeight : maxY > height - textHeight ? height - textHeight : maxY;
      let textMinX = minX < textWidth ? textWidth : minX > height - textWidth ? width - textWidth : minX;
      let textMinY = minY < textHeight ? textHeight : minY > width - textHeight ? height - textHeight : minY;

      ctx.font = fontStyle;
      ctx.fillStyle = Config.TempFillColor;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';

      ctx.fillText(`${maxTemp}℃`, textMaxX, textMaxY, textWidth * 2);
      ctx.fillText(`${minTemp}℃`, textMinX, textMinY, textWidth * 2);
    }

    return new Promise((resolve, reject) => {
      try {
        if (blob) {
          canvas.toBlob((blob) => resolve(URL.createObjectURL(blob)), type, quality);
        } else {
          resolve(canvas.toDataURL(type, quality));
        }
      } catch (error) {
        reject(error);
      }
    });
  }

  red(gray) {
    if (gray < 127) {
      return gray * 2;
    } else {
      return 255;
    }
  }
  blue(gray) {
    if (gray < 50) {
      return (gray * (160 - 20)) / 50 + 20;
    } else if (gray < 165) {
      return 160 - ((gray - 50) * 160) / (165 - 50);
    } else {
      return ((gray - 165) * 255) / (255 - 165);
    }
  }
  green(gray) {
    if (gray < 90) {
      return 0;
    } else {
      return ((gray - 90) * 255) / (255 - 90);
    }
  }
}
