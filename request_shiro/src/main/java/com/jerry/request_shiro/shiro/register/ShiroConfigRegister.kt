package com.jerry.request_shiro.shiro.register

import android.content.Context
import com.jerry.request_base.annotations.ConfigRegister
import com.jerry.request_base.annotations.Configuration
import com.jerry.request_base.interfaces.IConfig
import com.jerry.request_core.Core
import com.jerry.request_core.utils.reflect.ReflectUtils
import com.jerry.request_shiro.shiro.ShiroUtils
import com.jerry.request_shiro.shiro.anno.ShiroPermission
import com.jerry.request_shiro.shiro.anno.ShiroRole
import com.jerry.request_shiro.shiro.config.ShiroConfig
import com.jerry.request_shiro.shiro.exception.ShiroException
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.model.ShiroInfo
import com.jerry.request_shiro.shiro.utils.InnerShiroUtils
import com.jerry.rt.core.http.pojo.Request
import com.jerry.rt.core.http.pojo.s.IResponse

@ConfigRegister(registerClass = Any::class)
class ShiroConfigRegister : IConfig() {
    private var isF = true

    override fun init(annotation: Configuration, clazz: Any) {
        if (isF){
            isF = false
            val bean = Core.getBean(IShiroAuth::class.java)
            if (bean!=null){
                ShiroUtils.iShiroAuth = bean as IShiroAuth
            }
            if (ReflectUtils.isSameClass(clazz::class.java,IShiroAuth::class.java)){
                ShiroUtils.iShiroAuth = clazz as IShiroAuth
            }

            Core.getBean(ShiroConfig::class.java)?.let {
                ShiroUtils.shiroConfig = it as ShiroConfig
            }
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

        val shiroInfo = request.getPackage().getSession().getAttribute(ShiroUtils.shiroConfig.tokenName) as? ShiroInfo ?: throw ShiroException("no valid token")

        val token = ShiroUtils.iShiroAuth.getAccessToken(request,ShiroUtils.shiroConfig.tokenName)

        if (shiroInfo.authenticationInfo.token!=token){
            throw ShiroException("invalid user")
        }
        val roles = shiroInfo.authorizationInfo.getRoles()
        val permissions = shiroInfo.authorizationInfo.getPermissions()

        if (clazzRoleAnno!=null){
            if (!InnerShiroUtils.isChildList(roles,clazzRoleAnno.role.toList())){
                throw ShiroException("invalid access role")
            }
        }

        if (clazzPermissionAnno!=null){
            if (!InnerShiroUtils.isChildList(permissions,clazzPermissionAnno.permission.toList())){
                throw ShiroException("invalid access permission")
            }
        }

        if (methodRoleAnno!=null){
            if (!InnerShiroUtils.isChildList(roles,methodRoleAnno.role.toList())){
                throw ShiroException("invalid access role")
            }
        }

        if (methodPermissionAnno!=null){
            if (!InnerShiroUtils.isChildList(permissions,methodPermissionAnno.permission.toList())){
                throw ShiroException("invalid access permission")
            }
        }

        return true
    }



}