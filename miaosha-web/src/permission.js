import router from './router'
import {Message} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {getToken} from "./utils/auth";
import Axios from 'axios'
import VueAxios from "vue-axios"; // get token from cookie

NProgress.configure({showSpinner: false})

const whiteList = ['/login', '/register'] // all user can do in any situation
const userList = ['/home','/','/market'] // only user can visit
const adminList = ['/admin'] // only admin can visit
router.beforeEach(async (to, from, next) => {
  NProgress.start()
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
