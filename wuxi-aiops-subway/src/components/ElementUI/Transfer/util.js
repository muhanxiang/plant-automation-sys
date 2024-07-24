function _toConsumableArray(arr) {
  return (
    _arrayWithoutHoles(arr) ||
    _iterableToArray(arr) ||
    _unsupportedIterableToArray(arr) ||
    _nonIterableSpread()
  );
}

function _arrayLikeToArray(arr, len) {
  if (len == null || len > arr.length) len = arr.length;
  for (var i = 0, arr2 = new Array(len); i < len; i++) {
    arr2[i] = arr[i];
  }
  return arr2;
}

function _arrayWithoutHoles(arr) {
  if (Array.isArray(arr)) return _arrayLikeToArray(arr);
}

function _iterableToArray(iter) {
  if (typeof Symbol !== 'undefined' && Symbol.iterator in Object(iter)) return Array.from(iter);
}

function _nonIterableSpread() {
  throw new TypeError(
    'Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.'
  );
}

function _unsupportedIterableToArray(o, minLen) {
  if (!o) return;
  if (typeof o === 'string') return _arrayLikeToArray(o, minLen);
  var n = Object.prototype.toString.call(o).slice(8, -1);
  if (n === 'Object' && o.constructor) n = o.constructor.name;
  if (n === 'Map' || n === 'Set') return Array.from(n);
  if (n === 'Arguments' || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n))
    return _arrayLikeToArray(o, minLen);
}
/**
 * 将数组转化成树结构 array to tree
 * @param {*} array 数据源
 * @param {*} options 字段名配置项
 */
function arrayToTree() {
  let array = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];
  let options =
    arguments.length > 1 && arguments[1] !== undefined
      ? arguments[1]
      : {
          id: 'id',
          pid: 'pid',
          children: 'children',
        };
  let array_ = []; // 创建储存剔除叶子节点后的骨架节点数组

  let unique = {}; // 创建盒子辅助本轮children合并去重

  let root_pid = [
    0,
    '0',
    undefined,
    'undefined',
    null,
    'null',
    '00000000-0000-0000-0000-000000000000',
    '',
  ]; // 可能存在的根节点pid形式

  array.forEach(function (item) {
    // 筛选可以插入当前节点的所有子节点
    var children_array = array.filter(function (it) {
      return it[options.pid] === item[options.id];
    });

    if (
      item[options.children] &&
      item[options.children] instanceof Array &&
      item[options.children].length > 0
    ) {
      var _item$options$childre;

      // 去重合并数组
      item[options.children].map(function (i) {
        return (unique[i[options.id]] = 1);
      });

      (_item$options$childre = item[options.children]).push.apply(
        _item$options$childre,
        _toConsumableArray(
          children_array.filter(function (i) {
            return unique[i[options.id]] !== 1;
          })
        )
      );
    } else {
      item[options.children] = children_array;
    } // 当children_array有数据时插入下一轮array_，当无数据时将最后留下来的根节点树形插入数组

    var has_children = children_array.length > 0;

    if (has_children || (!has_children && root_pid.includes(item[options.pid]))) {
      array_.push(item);
    }
  }); // 当数组内仅有根节点时退出，否组继续处理 最终递归深度次

  if (
    !array_.every(function (item) {
      return root_pid.includes(item[options.pid]);
    })
  ) {
    return arrayToTree(array_, options);
  } else {
    return array_;
  }
}
/**
 * @name 从一维数组中找到节点的父祖节点
 * @param {Object} item 当前节点
 * @param {Array} arr 全部节点数组
 * @param {Object} options 配置项
 */
const findParents = (item, arr, options = { id: 'id', parentId: 'parentId', root: 0 }) => {
  let _parents = [];
  return (function findParent(item) {
    if (item[options.parentId] === options.root) return _parents;
    const parent = arr.find((i) => i[options.id] === item[options.parentId]);
    if (parent) {
      _parents.push(parent);
      return findParent(parent);
    } else {
      return _parents;
    }
  })(item);
};

/**
 * 将树形数据向下递归为一维数组
 * @param {*} arr 数据源
 * @param {*} childs  子集key
 */
function flattenDeep(arr, child = 'children') {
  // let arr = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];
  // let childs = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'Children';
  return arr.reduce(function (flat, item) {
    return flat.concat(item, item[child] ? flattenDeep(item[child], child) : []);
  }, []);
}

export { arrayToTree, findParents, flattenDeep };
