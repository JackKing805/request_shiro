package com.jerry.request_shiro.main.request

import android.util.Log
import com.jerry.request_base.annotations.Controller
import com.jerry.request_shiro.shiro.ShiroUtils
import com.jerry.request_shiro.shiro.anno.ShiroPermission
import com.jerry.request_shiro.shiro.anno.ShiroRole
import com.jerry.request_shiro.shiro.bean.ShiroLogic
import com.jerry.request_shiro.shiro.impl.SimpleUserLogin
import com.jerry.rt.core.http.pojo.Request
import com.jerry.rt.core.http.pojo.Response

@Controller("/", isRest = true)
class TestController {
    @Controller("/")
    fun onRoot(request: Request,response: Response):String{
        Log.e("ADSAD","onRoot:${request.getPackage().getSession().getId()}")
        return ShiroUtils.login(SimpleUserLogin(request,response,"AA","BB"))
    }

    @Controller("/rp")
    fun onRolePermission(request: Request,response: Response):String{
        val authInfo = ShiroUtils.getAuthInfo(request)
        Log.e("ADSAD","onRoot:${request.getPackage().getSession().getId()}")
        ShiroUtils.verify(request, listOf("user","a","b","c"), listOf("add","delete"), roleLogic = ShiroLogic.OR, permissionLogic = ShiroLogic.OR)
        return authInfo.toString()
    }
}