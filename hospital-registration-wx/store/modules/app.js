const state = {
  systemInfo: null
}

const mutations = {
  SET_SYSTEM_INFO(state, info) {
    state.systemInfo = info
  }
}

const actions = {}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
