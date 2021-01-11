import router from './router'
import {Message} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {getToken} from "./utils/auth";
import Axios from 'axios'
import VueAxios from "vue-axios"; // get token from cookie

NProgress.configure({showSpinner: false}) // NProgress Configuration

const whiteList = ['/login', '/register'] // no redirect whitelist
const userList = ['/home','/','/market']
const adminList = ['/admin']
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
      Axios.get("http://localhost:8082/user/info",{
        headers:{
          'X-token':getToken()
        }
      }).then(response=>{
        const {data} = response
        const {code} = data
        console.log('user code '+code)
        if (code === 20000 && userList.includes(to.path)){
          next()
        }else if(code === 20001 && adminList.includes(to.path)){
          next()
        }else{

        }
      })

    }
  }

})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()

})
