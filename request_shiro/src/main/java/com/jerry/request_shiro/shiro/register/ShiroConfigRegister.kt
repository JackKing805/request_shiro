package com.jerry.request_shiro.shiro.register

import android.content.Context
import com.jerry.request_base.annotations.ConfigRegister
import com.jerry.request_base.annotations.Configuration
import com.jerry.request_base.interfaces.IConfig
import com.jerry.request_core.Core
import com.jerry.request_core.utils.ResponseUtils
import com.jerry.request_core.utils.reflect.ReflectUtils
import com.jerry.request_shiro.shiro.ShiroUtils
import com.jerry.request_shiro.shiro.anno.ShiroPermission
import com.jerry.request_shiro.shiro.anno.ShiroRole
import com.jerry.request_shiro.shiro.exception.ShiroException
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.model.ShiroToken
import com.jerry.rt.core.http.pojo.Request
import com.jerry.rt.core.http.pojo.s.IResponse
import com.jerry.rt.core.http.protocol.RtContentType

@ConfigRegister(registerClass = Any::class)
class ShiroConfigRegister : IConfig() {
    override fun init(annotation: Configuration, clazz: Any) {
        val bean = Core.getBean(IShiroAuth::class.java)
        if (bean!=null){
            ShiroUtils.iShiroAuth = bean as IShiroAuth
            return
        }
        if (ReflectUtils.isSameClass(clazz::class.java,IShiroAuth::class.java)){
            ShiroUtils.iShiroAuth = clazz as IShiroAuth
        }
    }

    override fun onRequest(
        context: Context,
        request: Request,
        response: IResponse,
        controllerMapper: ControllerMapper?
    ): Boolean {
        if (controllerMapper==null){
            return true
        }

        val clazzRoleAnno = ReflectUtils.getAnnotation(controllerMapper.instance.javaClass, ShiroRole::class.java)
        val clazzPermissionAnno = ReflectUtils.getAnnotation(controllerMapper.instance.javaClass,ShiroPermission::class.java)

        val methodRoleAnno = ReflectUtils.getAnnotation(controllerMapper.method,ShiroRole::class.java)
        val methodPermissionAnno = ReflectUtils.getAnnotation(controllerMapper.method,ShiroPermission::class.java)

        if (clazzRoleAnno==null && clazzPermissionAnno == null && methodRoleAnno == null && methodPermissionAnno==null){
            return true
        }

        val shiroToken = request.getPackage().getSession().getAttribute("shiro_token") as? ShiroToken ?: throw ShiroException("no valid token")

        val token = request.getPackage().getHeader().getCookie(ShiroUtils.SHIRO_TOKEN)

        if (shiroToken.authenticationInfo.token!=token){
            throw ShiroException("invalid user")
        }
        val roles = shiroToken.authorizationInfo.getRoles()
        val permissions = shiroToken.authorizationInfo.getPermissions()

        if (clazzRoleAnno!=null){
            if (clazzRoleAnno.role.intersect(roles.toSet()).isEmpty()){
                throw ShiroException("invalid access role")
            }
        }

        if (clazzPermissionAnno!=null){
            if (clazzPermissionAnno.permission.intersect(permissions.toSet()).isEmpty()){
                throw ShiroException("invalid access permission")
            }
        }

        if (methodRoleAnno!=null){
            if (methodRoleAnno.role.intersect(roles.toSet()).isEmpty()){
                throw ShiroException("invalid access role")
            }
        }

        if (methodPermissionAnno!=null){
            if (methodPermissionAnno.permission.intersect(permissions.toSet()).isEmpty()){
                throw ShiroException("invalid access permission")
            }
        }

        return true
    }



}