import router from './router'
import {Message} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {getToken} from "./utils/auth"; // get token from cookie

NProgress.configure({showSpinner: false}) // NProgress Configuration

const whiteList = ['/login', '/register'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()

  // determine whether the user has logged in
  const hasToken = getToken()
  if (whiteList.includes(to.path)) {
    next()
  } else {
    if (!hasToken) {
      next('/login')
      Message.info('登陆信息已失效，请重新登陆')
      NProgress.done()
    }else{
      next()
    }
  }

})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
