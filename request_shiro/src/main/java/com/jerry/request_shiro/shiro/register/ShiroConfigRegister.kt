package com.jerry.request_shiro.shiro.register

import android.content.Context
import com.jerry.request_base.annotations.Bean
import com.jerry.request_base.annotations.ConfigRegister
import com.jerry.request_base.annotations.Configuration
import com.jerry.request_base.bean.IConfigControllerMapper
import com.jerry.request_base.interfaces.IConfig
import com.jerry.request_core.Core
import com.jerry.request_core.utils.reflect.ReflectUtils
import com.jerry.request_shiro.shiro.ShiroUtils
import com.jerry.request_shiro.shiro.anno.ShiroPermission
import com.jerry.request_shiro.shiro.anno.ShiroRole
import com.jerry.request_shiro.shiro.config.ShiroConfig
import com.jerry.request_shiro.shiro.core.ShiroSessionManager
import com.jerry.request_shiro.shiro.exception.ShiroAuthException
import com.jerry.request_shiro.shiro.exception.ShiroPermissionException
import com.jerry.request_shiro.shiro.exception.ShiroRoleException
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.model.ShiroInfo
import com.jerry.request_shiro.shiro.utils.InnerShiroUtils
import com.jerry.rt.bean.RtSessionConfig
import com.jerry.rt.core.http.pojo.Request
import com.jerry.rt.core.http.pojo.Response

@ConfigRegister(registerClass = IShiroAuth::class)
class ShiroConfigRegister : IConfig() {

    @Bean
    fun registerSession() = RtSessionConfig(
        sessionKey = "SHIRO_SESSION_ID",
        sessionClazz = ShiroSessionManager::class.java
    )

    override fun init(annotation: Configuration, clazz: Any) {
        ShiroUtils.iShiroAuth = clazz as IShiroAuth
    }

    override fun onCreate() {
        val bean = Core.getBean(IShiroAuth::class.java)
        if (bean!=null){
            ShiroUtils.iShiroAuth = bean as IShiroAuth
        }

        Core.getBean(ShiroConfig::class.java)?.let {
            ShiroUtils.shiroConfig = it as ShiroConfig
        }
    }

    override fun onRequestEnd(context: Context, request: Request, response: Response): Boolean {
        return true
    }

    override fun onRequestPre(
        context: Context,
        request: Request,
        response: Response,
        IConfigControllerMapper: IConfigControllerMapper?
    ): Boolean {
        if (IConfigControllerMapper==null){
            return true
        }

        val clazzRoleAnno = ReflectUtils.getAnnotation(IConfigControllerMapper.instance.javaClass, ShiroRole::class.java)
        val clazzPermissionAnno = ReflectUtils.getAnnotation(IConfigControllerMapper.instance.javaClass,ShiroPermission::class.java)

        val methodRoleAnno = ReflectUtils.getAnnotation(IConfigControllerMapper.method,ShiroRole::class.java)
        val methodPermissionAnno = ReflectUtils.getAnnotation(IConfigControllerMapper.method,ShiroPermission::class.java)

        if (clazzRoleAnno==null && clazzPermissionAnno == null && methodRoleAnno == null && methodPermissionAnno==null){
            return true
        }

        val shiroInfo = request.getPackage().getSession().getAttribute(ShiroUtils.shiroConfig.tokenName) as? ShiroInfo ?: throw ShiroAuthException("no valid token")

        val token = ShiroUtils.iShiroAuth.getAccessToken(request,ShiroUtils.shiroConfig.tokenName)

        if (shiroInfo.authenticationInfo.token!=token){
            throw ShiroAuthException("invalid user")
        }
        val roles = shiroInfo.authorizationInfo.getRoles()
        val permissions = shiroInfo.authorizationInfo.getPermissions()

        if (clazzRoleAnno!=null){
            if (!InnerShiroUtils.isChildList(roles,clazzRoleAnno.role.toList())){
                throw ShiroRoleException(*clazzRoleAnno.role.subtract(roles.toSet()).toTypedArray())
            }
        }

        if (clazzPermissionAnno!=null){
            if (!InnerShiroUtils.isChildList(permissions,clazzPermissionAnno.permission.toList())){
                throw ShiroPermissionException(*clazzPermissionAnno.permission.subtract(permissions.toSet()).toTypedArray())
            }
        }

        if (methodRoleAnno!=null){
            if (!InnerShiroUtils.isChildList(roles,methodRoleAnno.role.toList())){
                throw ShiroRoleException(*methodRoleAnno.role.subtract(roles.toSet()).toTypedArray())
            }
        }

        if (methodPermissionAnno!=null){
            if (!InnerShiroUtils.isChildList(permissions,methodPermissionAnno.permission.toList())){
                throw ShiroPermissionException(*methodPermissionAnno.permission.subtract(permissions.toSet()).toTypedArray())
            }
        }

        return true
    }



}