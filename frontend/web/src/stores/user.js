import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    userInfo: null,
  }),
  actions: {
    setUserInfo(userInfo) {
      this.userInfo = userInfo;
    },
    clearUserInfo() {
      this.userInfo = null;
    },
  },
  getters: {
    isLoggedIn: (state) => {
      return state.userInfo && state.userInfo.id;
    },
  },
});
