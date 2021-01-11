import Vue from 'vue'

import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({

  state: {
    type:0
  },

  mutations: {


  },

  getters: {
    getType: state=>state.type
  },

  actions: {
    setType(state, type){
      state.type = type
    }
  },

  modules: {

  }

})
