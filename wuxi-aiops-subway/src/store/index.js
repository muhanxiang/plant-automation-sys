import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'

Vue.use(Vuex)

const storeModules = require.context('./modules', false, /\.js$/)
const modules = storeModules.keys().reduce((moduleMap, modulePath) => {
  // set './app.js' => 'app'
  const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
  const moduleValue = storeModules(modulePath)
  moduleMap[moduleName] = moduleValue.default
  return moduleMap
}, {})

const store = new Vuex.Store({
  modules,
  getters
})

export default store
