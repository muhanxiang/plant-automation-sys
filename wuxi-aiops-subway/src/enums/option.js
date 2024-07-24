// 基础配置项
export default {
  // 告警类型
  AlarmTypes: [
    { key: 1, value: '超温报警' },
    { key: 2, value: '温升报警' },
    { key: 3, value: '三相温差报警' },
    { key: 4, value: '三相对比报警' },
    { key: 5, value: '声音异常' },
    { key: 6, value: '外观异常' },
    { key: 7, value: '仪表越限报警' },
    { key: 8, value: '仪表超量程报警' },
    { key: 9, value: '仪表三相对比' },
    { key: 10, value: '变位报警' },
    { key: 101, value: '信号告警' },
    { key: 102, value: '设备告警' },
    { key: 103, value: '系统告警' },
  ],
  // 告警等级
  AlarmLevels: [
    { key: 1, value: '预警' },
    { key: 2, value: '一般' },
    { key: 3, value: '严重' },
    { key: 4, value: '危急' },
  ],
  // 外观类型
  AppearanceTypes: [
    { key: 1, value: '电子围栏' },
    { key: 2, value: '红外对射' },
    { key: 3, value: '泡沫喷淋' },
    { key: 4, value: '消防水泵' },
    { key: 5, value: '消防栓' },
    { key: 6, value: '消防室' },
    { key: 7, value: '设备室' },
    { key: 8, value: '照明灯' },
    { key: 9, value: '摄像头' },
    { key: 10, value: '水位线' },
    { key: 11, value: '排水泵' },
    { key: 12, value: '沉降监测点' },
  ],
  // 摄像机类型
  CameraTypes: [
    { key: 0, value: '球机' },
    { key: 1, value: '枪机' },
    { key: 2, value: '云台' },
  ],
  // 设备类型
  DeviceTypes: [
    { key: 1, value: '油浸式变压器(电抗器)' },
    { key: 2, value: '断路器' },
    { key: 3, value: '组合电器' },
    { key: 4, value: '隔离开关' },
    { key: 5, value: '开关柜' },
    { key: 6, value: '电流互感器' },
    { key: 7, value: '电压互感器' },
    { key: 8, value: '避雷器' },
    { key: 9, value: '并联电容器组' },
    { key: 10, value: '干式电抗器' },
    { key: 11, value: '串联补偿装置' },
    { key: 12, value: '母线及绝缘子' },
    { key: 13, value: '穿墙套管' },
    { key: 14, value: '消弧线圈' },
    { key: 15, value: '高频阻波器' },
    { key: 16, value: '耦合电容器' },
    { key: 17, value: '高压熔断器' },
    { key: 18, value: '中性点隔直(限直)装置' },
    { key: 19, value: '接地装置' },
    { key: 20, value: '端子箱及检修电源箱' },
    { key: 21, value: '站用变压器' },
    { key: 22, value: '站用交流电源系统' },
    { key: 23, value: '站用直流电源系统' },
    { key: 24, value: '设备构架' },
    { key: 25, value: '辅助设施' },
    { key: 26, value: '土建设施' },
    { key: 27, value: '独立避雷针' },
    { key: 28, value: '避雷器动作次数表' },
  ],
  // 设备层级
  DeviceLevels: [
    { key: 0, value: '区域' },
    { key: 1, value: '间隔' },
    { key: 2, value: '设备' },
    { key: 3, value: '巡视点位' },
    { key: 4, value: '设备部件' },
  ],
  // 缺陷类型
  DefectTypes: [
    { key: 0, value: '表盘模糊' },
    { key: 1, value: '表盘破损' },
    { key: 2, value: '外壳破损' },
    { key: 3, value: '绝缘子破损' },
    { key: 4, value: '地面油污' },
    { key: 5, value: '呼吸器破损' },
    { key: 6, value: '箱门闭合异常' },
    { key: 7, value: '挂空悬浮物' },
    { key: 8, value: '鸟巢' },
    { key: 9, value: '盖板破损或缺失' },
    { key: 10, value: '绝缘子裂纹' },
    { key: 11, value: '部件表面油污' },
    { key: 12, value: '金属锈蚀' },
    { key: 13, value: '门窗墙地面损坏' },
    { key: 14, value: '构架爬梯未上锁' },
    { key: 15, value: '表面污秽' },
    { key: 16, value: '越线闯入' },
    { key: 17, value: '未戴安全帽' },
    { key: 18, value: '未穿工装' },
    { key: 19, value: '吸烟' },
    { key: 20, value: '烟火识别' },
    { key: 21, value: '小动物识别' },
    { key: 22, value: '积水' },
    { key: 23, value: '渗水' },
  ],
  // 缺陷等级
  DefectLevels: [
    { key: 0, value: '一般' },
    { key: 1, value: '严重' },
    { key: 2, value: '危急' },
  ],
  // 缺陷状态
  DefectStates: [
    { key: 0, value: '未消缺' },
    { key: 1, value: '消缺中' },
    { key: 2, value: '已消缺' },
  ],
  // 缺陷来源
  DefectSources: [
    { key: 0, value: '巡视结果' },
    { key: 1, value: '智辅遥测' },
    { key: 2, value: '在线监测' },
  ],
  // 执行类型
  ExecuteTypes: [
    { key: 0, value: '由视频执行巡视' },
    { key: 1, value: '由机器人执行巡视' },
    { key: 2, value: '由视频与机器人执行巡视' },
    { key: 3, value: '由巡视主机执行巡视' },
  ],
  // 表达式类型
  ExpressionTypes: [
    { key: 0, value: '结果表达式' },
    { key: 1, value: '告警表达式' },
  ],
  // 文件类型
  FileTypes: [
    { key: 1, value: '红外图谱' },
    { key: 2, value: '可见光照片' },
    { key: 3, value: '音频' },
    { key: 4, value: '视频' },
    { key: 5, value: '识别图片' },
  ],
  // 致热类型
  HeatingTypes: [
    { key: 1, value: '电流致热型' },
    { key: 2, value: '电压致热型' },
    { key: 3, value: '综合致热型' },
  ],
  // 接入类型
  InputTypes: [
    { key: 0, value: '有线' },
    { key: 1, value: '无线' },
  ],
  // 联动类型
  LinkageTypes: [
    { key: 100, value: '任务联动' },
    { key: 101, value: '点位联动' },
  ],
  // 联动信号
  LinkageSignals: [
    { key: 0, value: '遥信信号' },
    { key: 1, value: '遥测信号' },
    { key: 2, value: '主辅联动信号' },
  ],
  // 联动符号
  LinkageSymbols: [
    { key: 0, value: '=' },
    { key: 1, value: '>' },
    { key: 2, value: '≧' },
    { key: 3, value: '<' },
    { key: 4, value: '≦' },
  ],
  // 仪表类型
  MeterTypes: [
    { key: 1, value: '油位表' },
    { key: 2, value: '避雷器动作次数表' },
    { key: 3, value: '泄漏电流表' },
    { key: 4, value: 'SF6压力表' },
    { key: 5, value: '液压表' },
    { key: 6, value: '开关动作次数表' },
    { key: 7, value: '油温表' },
    { key: 8, value: '档位表' },
    { key: 9, value: '气压表' },
  ],
  // 录像机类型
  NvrTypes: [
    { key: 0, value: '海康' },
    { key: 1, value: '大华' },
    { key: 2, value: '华为' },
    { key: 3, value: '其它' },
  ],
  // 相位类型
  PhaseTypes: [
    { key: 1, value: 'A相' },
    { key: 2, value: 'B相' },
    { key: 3, value: 'C相' },
    { key: 4, value: 'O相' },
    { key: 5, value: '三相' },
  ],
  // 巡视任务
  PatrolTasks: [
    { key: 0, value: '全面巡视' },
    // { key: 1, value: '例行巡视' },
    // { key: 2, value: '特殊巡视' },
    { key: 3, value: '专项巡视' },
    { key: 4, value: '自定义巡视' },
    { key: 5, value: '静默巡视' },
    { key: 100, value: '联动巡视' },
    { key: 101, value: '一键顺控' },
    { key: 102, value: '多点识别' },
    { key: 103, value: '单点识别' },
  ],
  // 巡视类型
  PatrolTypes: [
    { key: 0, value: '全面巡视' },
    // { key: 1, value: '例行巡视' },
    // { key: 2, value: '特殊巡视' },
    { key: 3, value: '专项巡视' },
    { key: 4, value: '自定义巡视' },
    { key: 5, value: '静默巡视' },
  ],
  // 巡视状态
  PatrolStates: [
    { key: 0, value: '成功' },
    // { key: 1, value: '缺陷' },
    { key: 2, value: '告警' },
    { key: 3, value: '失败' },
  ],
  // 预置位类型
  PresetTypes: [
    { key: 0, value: 'PTZ参数' },
    { key: 1, value: '预置位号' },
    { key: 2, value: '固定位置' },
  ],
  // 角色类型
  RoleTypes: [
    { key: 1, value: '管理员' },
    { key: 2, value: '操作员' },
    { key: 3, value: '审计员' },
  ],
  // 控制模式
  RobotModes: [
    { key: 1, value: '任务模式' },
    { key: 2, value: '紧急定位模式' },
    { key: 3, value: '后台遥控模式' },
    { key: 4, value: '手持遥控模式' },
  ],
  // 关联类型
  RelateTypes: [
    { key: 1, value: 'station' },
    { key: 2, value: 'area' },
    { key: 3, value: 'bay' },
    { key: 4, value: 'device' },
    { key: 5, value: 'component' },
    { key: 6, value: 'patrolpoint' },
  ],
  // 识别类型
  RecognitionTypes: [
    { key: 1, value: '表计读取' },
    { key: 2, value: '位置状态' },
    { key: 3, value: '设备外观' },
    { key: 4, value: '红外测温' },
    { key: 5, value: '声音检测' },
    { key: 6, value: '闪烁检测' },
  ],
  // 录像类型
  StreamTypes: [
    { key: 0, value: '视频丢失告警录像' },
    { key: 1, value: '移动侦测告警录像' },
    { key: 2, value: '视频遮挡告警录像' },
    { key: 8, value: '设备高温录像' },
    { key: 9, value: '设备低温录像' },
    { key: 10, value: '风扇故障告警' },
    { key: 11, value: '磁盘故障告警' },
    { key: 16, value: '状态事件告警' },
    { key: 20, value: '设备定时录像' },
    { key: 21, value: '用户请求录像' },
  ],
  // 传感器类型
  SensorTypes: [
    { key: 0, value: '未定义', icon: 'empty' },
    { key: 1, value: '温度', icon: 'temperature' },
    { key: 2, value: '湿度', icon: 'humidity' },
    { key: 3, value: '温湿度', icon: 'humiture' },
    { key: 4, value: '微气象', icon: 'weather' },
    { key: 5, value: 'SF6气体', icon: 'sf6' },
    { key: 6, value: '氧气浓度', icon: 'oxygen' },
    { key: 7, value: '水浸传感器', icon: 'water-sensor' },
    { key: 8, value: '火灾探测器', icon: 'fire-detector' },
    { key: 9, value: '感温光纤', icon: 'optical-fiber' },
    { key: 10, value: '套管介损', icon: 'drivepipe' },
    { key: 11, value: '局部放电', icon: 'discharge' },
    { key: 12, value: '铁芯接地电流', icon: 'grounding' },
    { key: 13, value: '机械特性', icon: 'setting' },
  ],
  // 服务类型
  ServiceTypes: [
    { key: 0, value: '平台' },
    { key: 1, value: '历史' },
    { key: 2, value: '流媒体' },
    { key: 3, value: '识别' },
    { key: 4, value: '任务' },
  ],
  // 站所类型
  StationTypes: [
    { key: 0, value: '变电站/主所' },
    { key: 1, value: '配电所' },
    { key: 2, value: '牵引所' },
    { key: 3, value: '牵混所' },
    { key: 4, value: '跟随所' },
  ],
  // 辅助开关动作
  SwitchActions: [
    { key: 0, value: '合闸' },
    { key: 1, value: '开闸' },
  ],
  // 任务-月
  TaskMonths: [
    { key: 1, value: '1日' },
    { key: 2, value: '2日' },
    { key: 3, value: '3日' },
    { key: 4, value: '4日' },
    { key: 5, value: '5日' },
    { key: 6, value: '6日' },
    { key: 7, value: '7日' },
    { key: 8, value: '8日' },
    { key: 9, value: '9日' },
    { key: 10, value: '10日' },
    { key: 11, value: '11日' },
    { key: 12, value: '12日' },
    { key: 13, value: '13日' },
    { key: 14, value: '14日' },
    { key: 15, value: '15日' },
    { key: 16, value: '16日' },
    { key: 17, value: '17日' },
    { key: 18, value: '18日' },
    { key: 19, value: '19日' },
    { key: 20, value: '20日' },
    { key: 21, value: '21日' },
    { key: 22, value: '22日' },
    { key: 23, value: '23日' },
    { key: 24, value: '24日' },
    { key: 25, value: '25日' },
    { key: 26, value: '26日' },
    { key: 27, value: '27日' },
    { key: 28, value: '28日' },
    { key: 29, value: '29日' },
    { key: 30, value: '30日' },
    { key: 31, value: '31日' },
  ],
  // 任务-周
  TaskWeeks: [
    { key: 0, value: '周日' },
    { key: 1, value: '周一' },
    { key: 2, value: '周二' },
    { key: 3, value: '周三' },
    { key: 4, value: '周四' },
    { key: 5, value: '周五' },
    { key: 6, value: '周六' },
  ],
  // 执行类型
  TaskTypes: [
    { key: 0, value: '即时执行' },
    { key: 1, value: '定期执行' },
    { key: 2, value: '周期执行' },
    { key: 3, value: '间隔执行' },
  ],
  // 任务审核
  TaskAudits: [
    { key: 0, value: '待审核' },
    { key: 1, value: '已审核' },
    { key: 2, value: '未通过' },
  ],
  // 任务周期
  TaskCycles: [
    { key: 1, value: '月' },
    { key: 2, value: '周' },
  ],
  // 任务状态
  TaskStates: [
    { key: 1, value: '已执行' },
    { key: 2, value: '执行中' },
    { key: 3, value: '暂停' },
    { key: 4, value: '终止' },
    { key: 5, value: '未执行' },
    { key: 6, value: '超期' },
    { key: 7, value: '等待' },
  ],
  // 任务来源
  TaskSources: [
    { key: 1, value: '摄像机' },
    { key: 2, value: '机器人' },
    { key: 3, value: '摄像机+机器人' },
  ],
  // 任务间隔
  TaskIntervals: [
    { key: 1, value: '小时' },
    { key: 2, value: '天' },
  ],
  // 任务优先级
  TaskPriorities: [
    { key: 0, value: '低' },
    // { key: 1, value: '较低' },
    { key: 2, value: '中' },
    // { key: 3, value: '较高' },
    { key: 4, value: '高' },
  ],
  // 视频类型
  VideoTypes: [
    { key: 0, value: '可见光相机' },
    { key: 1, value: '红外热成像仪' },
    { key: 2, value: '双光谱相机' },
  ],
  // 电压等级
  VoltageLevels: [
    { key: 0, value: '1000kV' },
    { key: 1, value: '800kV' },
    { key: 2, value: '750kV' },
    { key: 3, value: '500kV' },
    { key: 4, value: '330kV' },
    { key: 5, value: '220kV' },
    { key: 6, value: '110kV' },
    { key: 7, value: '35kV' },
    { key: 8, value: '27.5kV' },
    { key: 9, value: '10kV' },
    { key: 10, value: '1500V' },
  ],
};
