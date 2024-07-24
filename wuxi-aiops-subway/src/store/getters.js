const getters = {
  mode: (state) => state.app.mode,
  logo: (state) => state.app.logo,
  setup: (state) => state.app.setup,
  title: (state) => state.app.title,
  footer: (state) => state.app.footer,
  system: (state) => state.app.system,
  company: (state) => state.app.company,
  stationId: (state) => state.app.station,

  stomp: (state) => state.app.stomp,
  theme: (state) => state.app.theme,
  navMenu: (state) => state.app.navMenu,
  activeMenu: (state) => state.app.activeMenu,

  token: (state) => state.user.token,
  uname: (state) => state.user.uname,
  avatar: (state) => state.user.avatar,
  userId: (state) => state.user.userId,
  userAuth: (state) => state.user.group,
  username: (state) => state.user.username,
  password: (state) => state.user.password,
};
export default getters;
