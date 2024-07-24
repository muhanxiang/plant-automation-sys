// 系统控制指令
export const ControlCMD = {
  TASK_CONTROL: 41, // 任务控制指令
  REMOTE_CONTROL: 51, // 远程控制指令
  MODEL_SYNC: 61, // 模型同步指令
  CONFIG_ISSUE: 81, // 配置下发指令
  TASK_ISSUE: 101, // 任务下发指令
  TDINFO_SYNC: 122, // 三维信息同步指令
};
// 视频模式
export const DisplayMode = {
  SINGLE: 1,
  FOUR: 4,
  NINE: 9,
  // SIXTEEN: 16,
};
// 文件类型
export const FileType = {
  IMAGE: 0, // 可见光照片
  FIRTEMP: 1, // 红外图谱
  IDENTIFY: 2, // 识别图片
  AUDIO: 3, // 音频
  VIDEO: 4, // 视频
};
// 联动类型
export const LinkageType = {
  PATROLPOINT: 0, // 巡视点位
  PATROLTASK: 1, // 巡视任务
  YK_SIGNAL: 2, // 遥控信号
  CAMERA: 3, // 摄像机
  PRESET: 4, // 预置位
};
// 联动信号
export const LinkageSignal = {
  YX_SIGNAL: 0, // 遥信信号
  YC_SIGNAL: 1, // 遥测信号
  YK_SIGNAL: 3, // 遥控信号
  SC_SIGNAL: 2, // 一键顺控
  ZF_SIGNAL: 4, // 主辅联动
  PATROLPOINT: 5, // 巡视点位
};
// 云台控制指令
export const PtzCMD = {
  IRIS_CLOSE_STOP: 0x0101, // 光圈关闭停
  IRIS_CLOSE_START: 0x0102, // 光圈关闭
  IRIS_OPEN_STOP: 0x0103, // 光圈打开
  IRIS_OPEN_START: 0x0104, // 光圈打开停
  FOCUS_NEAR_STOP: 0x0201, // 近聚焦停止
  FOCUS_NEAR_START: 0x0202, // 近聚焦开始
  FOCUS_FAR_STOP: 0x0203, // 远聚焦停止
  FOCUS_FAR_START: 0x0204, // 远聚焦开始
  ZOOM_IN_STOP: 0x0301, //  放大停止
  ZOOM_IN_START: 0x0302, //  放大开始
  ZOOM_OUT_STOP: 0x0303, //  缩小停止
  ZOOM_OUT_START: 0x0304, //  缩小开始
  PAN_UP_STOP: 0x0401, // 向上停止
  PAN_UP_START: 0x0402, // 向上开始
  PAN_DOWN_STOP: 0x0403, // 向下停止
  PAN_DOWN_START: 0x0404, // 向下开始
  PAN_RIGHT_STOP: 0x0501, // 右转停止
  PAN_RIGHT_START: 0x0502, // 右转开始
  PAN_LEFT_STOP: 0x0503, // 左转停止
  PAN_LEFT_START: 0x0504, // 左转开始
  PAN_UPLEFT_STOP: 0x0701, // 左上方向运动停止
  PAN_UPLEFT_START: 0x0702, // 左上方向运动开始
  PAN_UPRIGHT_STOP: 0x0801, // 右上方向运动停止
  PAN_UPRIGHT_START: 0x0802, // 右上方向运动开始
  PAN_DOWNLEFT_STOP: 0x0703, // 左下方向运动停止
  PAN_DOWNLEFT_START: 0x0704, // 左下方向运动开始
  PAN_DOWNRIGHT_STOP: 0x0803, // 右下方向运动停止
  PAN_DOWNRIGHT_START: 0x0804, // 右下方向运动开始
  PRESET_CALL: 0x0602, // 预置位调用
  BRUSH_OPEN: 0x0604, // 雨刷开
  BRUSH_CLOSE: 0x0605, // 雨刷关
  LIGHT_OPEN: 0x0606, // 灯光开
  LIGHT_CLOSE: 0x0607, // 灯光关
  CONTROL_3D: 0x0901, // 3D控制
  AUTOSCAN_STOP: 0x0e01, // 自动扫描停止
  AUTOSCAN_START: 0x0e02, // 自动扫描开始
};
// 巡视任务
export const PatrolTask = {
  OVERALL: 0, // 全面巡视
  ROUTINE: 1, // 例行巡视
  SPECIAL: 2, // 特殊巡视
  PRIVATE: 3, // 专项巡视
  CUSTOM: 4, // 自定义巡视
  SILENT: 5, // 静默巡视
  ACLINKAGE: 100, // 联动巡视
  SEQCONTROL: 101, // 一键顺控
  MULTIPOINT: 102, // 多点识别
  SINGLEPOINT: 103, // 单点识别
};
// 巡视类型
export const PatrolType = {
  OVERALL: 0, // 全面巡视
  ROUTINE: 1, // 例行巡视
  SPECIAL: 2, // 特殊巡视
  PRIVATE: 3, // 专项巡视
  CUSTOM: 4, // 自定义巡视
  SILENT: 5, // 静默巡视
};
// 巡视设备
export const PatrolSource = {
  CAMERA: 1, // 摄像机
  ROBOT: 2, // 机器人
  HOST: 3, // 巡视主机
};
// 巡视状态
export const PatrolStatus = {
  SUCCESS: 0, // 成功
  DEFECTS: 1, // 缺陷
  WARNING: 2, // 告警
  FAILURE: 3, // 失败
};
// 预置位类型
export const PresetType = {
  PTZ_PARAM: 0,
  PRESET_NO: 1,
  FIXED: 2,
};
// 角色类型
export const RoleType = {
  ADMIN: 1,
  AUDITOR: 2,
  OPERATOR: 3,
  CUSTOMER: 4,
};
// 识别类型
export const RecognitionType = {
  METER: 1, // 表计读取
  STATUS: 2, // 位置状态
  APPEARANCE: 3, // 设备外观
  FIRTEMP: 4, // 红外测温
  SOUND: 5, // 声音检测
  BLINK: 6, // 闪烁检测
};
// 传感器类型
export const SensorType = {
  UNDEFINED: 0, //未定义
  TEMPERATURE: 1, //温度
  HUMIDITY: 2, //湿度
  HUMITURE: 3, //温湿度
  WEATHER: 4, //微气象
  SF6: 5, //SF6气体
  OXYGEN: 6, //氧气浓度
  WATER: 7, //水浸传感器
  FIRE: 8, //火灾探测器
  OPTICAL: 9, //感温光纤
  DIELECTRIC: 10, //套管介损
  DISCHARGE: 11, //局部放电
  GROUNDING: 12, //铁芯接地
  MECHANICAL: 13, //机械特性
};
// 系统模式
export const StationMode = {
  CENTRAL: 'central', // 集控
  MASTER: 'master', // 主站
  STATION: 'station', // 子站
};
// 执行类型
export const TaskType = {
  IMMEDIATE: 0, // 即时执行
  REGULAR: 1, // 定期执行
  CYCLE: 2, // 周期执行
  INTERVAL: 3, // 间隔执行
};
// 任务审核
export const TaskAudit = {
  UNAUDIT: 0,
  AUDITED: 1,
  NOTPASS: 2,
};
// 任务周期
export const TaskCycle = {
  MONTH: 1,
  WEEK: 2,
};
// 任务来源
export const TaskSource = {
  DEFAULT: 0, // 默认值
  CAMERA: 1, // 摄像机
  ROBOT: 2, // 机器人
  UNION: 3, // 摄像机+机器人
};
// 任务状态
export const TaskStatus = {
  DONE: 1, // 已执行
  DOING: 2, // 执行中
  PAUSE: 3, // 暂停
  TERMINATE: 4, // 终止
  UNDO: 5, // 未执行
  OVERDUE: 6, // 超期
  WAITING: 7, // 等待
};
// 任务间隔
export const TaskInterval = {
  HOUR: 1,
  DAY: 2,
};
// 视频分类
export const VideoMode = {
  VIDEO: 0, // 视频监控
  ROUND: 1, // 视频轮巡
  REPLAY: 2, // 视频回放
};

// 消息订阅地址
export const MessageDest = {
  PATROL_ALL: '/exchange/patrol/patrol.#', // 任务巡视推送
  PATROL_ALARM: '/exchange/patrol/patrol.alarm', // 任务巡视告警
  PATROL_TASK_ALL: '/exchange/patrol/patrol.task.*', // 任务巡视信息
  PATROL_TASK_ITEM: '/exchange/patrol/patrol.task.item', // 任务巡视结果
  PATROL_TASK_INDEX: '/exchange/patrol/patrol.task.index', // 任务巡视状态
  PATROL_SINGLE_RESULT: '/exchange/patrol/patrol.single.result', // 单点识别结果

  STATUS_ALL: '/exchange/status/status.platform.*', // 平台状态推送
  STATUS_HOST: '/exchange/status/status.platform.host', // 主机状态推送
  STATUS_MEDIA: '/exchange/status/status.platform.media', // 流媒体状态推送
  STATUS_ROBOT: '/exchange/status/status.platform.robot', // 机器人状态推送
  STATUS_CHANNEL: '/exchange/status/status.platform.channel', // 通道状态推送
  STATUS_STATION: '/exchange/status/status.platform.station', // 站所状态推送
  STATUS_COMMPOINT: '/exchange/status/status.platform.commpoint', // 实时测量推送
};
// 消息推送类型
export const MessageAction = {
  AC: {
    YCVALUE: 'status.platform.ycvalue', // 遥测信号值
    YXVALUE: 'status.platform.yxvalue', // 遥信信号值
    COMMPOINT: 'status.platform.commpoint', // 实时信号值
  },
  HOST: {
    STATUS: 'status.platform.hoststatus', // 巡视主机状态
  },
  MEDIA: {
    NVRINFO: 'status.platform.nvrinfo', // 录像机信息
    NVRSTATUS: 'status.platform.nvrstatus', // 录像机状态
    CAMERASTATUS: 'status.platform.camerastatus', // 摄像机状态
  },
  ROBOT: {
    SPEED: 'status.platform.robotspeed', // 机器人行驶速度
    STATUS: 'status.platform.robotstatus', // 机器人状态
    BATTERY: 'status.platform.robotbattery', // 机器人电池状态
    MILEAGE: 'status.platform.robotmileage', // 机器人行驶里程
    HUMIDITY: 'status.platform.robothumidity', // 机器人湿度上报
    WINDSPEED: 'status.platform.robotwindspeed', // 机器人风速上报
    COORDINATE: 'status.platform.robotcoordinate', // 机器人坐标上报
    FAULTALARM: 'status.platform.robotfaultalarm', // 机器人故障报警
    LOWBATTERY: 'status.platform.robotlowbattery', // 机器人电池电量低
    COORDINATES: 'status.platform.robotcoordinates', // 机器人巡视路线
    CONTROLMODE: 'status.platform.robotcontrolmode', // 机器人控制模式
    TEMPERATURE: 'status.platform.robottemperature', // 机器人温度上报
    COMMABNORMAL: 'status.platform.robotcommabnormal', // 机器人通信状态
    RUNNINGSTATE: 'status.platform.robotrunningstate', // 机器人运行状态
    CONTROLSTATUS: 'status.platform.robotcontrolstatus', // 机器人控制状态
    DRIVEEXCEPTION: 'status.platform.robotdriveexception', // 机器人驱动异常
    TRANSPORTSTATUS: 'status.platform.robottransportstatus', // 机器人轮转状态
    ULTRASONICSTOPPING: 'status.platform.robotultrasonicstopping', // 机器人超声停障
  },
  PATROL: {
    ALARM: 'patrol.alarm', // 任务巡视告警
    TASK_ITEM: 'patrol.task.item', // 任务巡视结果
    TASK_INDEX: 'patrol.task.index', // 任务巡视状态
    SINGLE_RESULT: 'patrol.single.result', // 单点识别结果
  },
  METRICS: {
    SERVER: 'metrics.metrics.server', // 服务器指标
    CONTAINER: 'metrics.metrics.container', // 容器指标
  },
  STATION: {
    HUMIDITY: 'status.platform.stationhumidity', // 变电站湿度
    PRESSURE: 'status.platform.stationpressure', // 变电站气压
    RAINFALL: 'status.platform.stationrainfall', // 变电站降雨量
    WINDSPEED: 'status.platform.stationwindspeed', // 变电站风速
    TEMPERATURE: 'status.platform.stationtemperature', // 变电站温度
    WINDDIRECTION: 'status.platform.stationwinddirection', // 变电站风向
  },
};

// 已用巡视类型
export const ActivePatrolTypes = [PatrolTask.OVERALL, PatrolTask.PRIVATE, PatrolTask.CUSTOM, PatrolTask.SILENT];
// 联动巡视类型
export const LinkagePatrolTypes = [PatrolTask.ACLINKAGE, PatrolTask.SEQCONTROL];
// 非任务巡视类型
export const NonTaskPatrolTypes = [PatrolTask.SINGLEPOINT, PatrolTask.MULTIPOINT];
// 自定义巡视类型
export const CustomPatrolTypes = [
  PatrolTask.ACLINKAGE,
  PatrolTask.SEQCONTROL,
  PatrolTask.SINGLEPOINT,
  PatrolTask.MULTIPOINT,
];
