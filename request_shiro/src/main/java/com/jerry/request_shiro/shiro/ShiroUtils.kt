package com.jerry.request_shiro.shiro

import com.jerry.request_base.bean.IConfigControllerMapper
import com.jerry.request_core.utils.reflect.ReflectUtils
import com.jerry.request_shiro.shiro.anno.ShiroPermission
import com.jerry.request_shiro.shiro.anno.ShiroRole
import com.jerry.request_shiro.shiro.config.ShiroConfig
import com.jerry.request_shiro.shiro.exception.ShiroAuthException
import com.jerry.request_shiro.shiro.exception.ShiroPermissionException
import com.jerry.request_shiro.shiro.exception.ShiroRoleException
import com.jerry.request_shiro.shiro.exception.ShiroVerifyException
import com.jerry.request_shiro.shiro.impl.ShiroCache
import com.jerry.request_shiro.shiro.impl.ShiroCacheManager
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.interfaces.IShiroCache
import com.jerry.request_shiro.shiro.interfaces.IShiroCacheManager
import com.jerry.request_shiro.shiro.interfaces.UserLoginToken
import com.jerry.request_shiro.shiro.model.*
import com.jerry.request_shiro.shiro.utils.InnerShiroUtils
import com.jerry.rt.core.http.pojo.Cookie
import com.jerry.rt.core.http.pojo.Request
import com.jerry.rt.core.http.pojo.Response
import java.util.*

/**
 * 如果是以token形式来进行认证的话，目前用session保存是无效，客户端一旦断开，session立马就会失效
 * 重新写一个缓存接口，使其可以自定怎么增加缓存，删除缓存，并且可以设置缓存有效期，或者定时删除缓存
 *
 * //todo 增加手动验证role和permission的方法
 */
object ShiroUtils {
    internal var shiroConfig =  ShiroConfig("shiro_token",20)

    internal var iShiroAuth: IShiroAuth = object :IShiroAuth{
        override fun onAuthentication(authToken: AuthToken): AuthenticationInfo {
            return AuthenticationInfo(authToken,authToken.getPassword(),UUID.randomUUID().toString())
        }

        override fun onAuthorization(authorization: AuthenticationInfo): AuthorizationInfo {
            return AuthorizationInfo()
        }
    }

    internal var cacheManager:IShiroCacheManager = ShiroCacheManager()


    @Throws(exceptionClasses = [ShiroAuthException::class])
    fun login(userLoginToken: UserLoginToken):String{
        val shiroInfo = getShiroInfo(userLoginToken.getRequest())
        if (shiroInfo!=null){
            return shiroInfo.authenticationInfo.token
        }

        val onAuthentication = iShiroAuth.onAuthentication(AuthToken(userLoginToken.getUserName(),userLoginToken.getPassword())) ?: throw ShiroAuthException("auth error")

        val onAuthorization = iShiroAuth.onAuthorization(onAuthentication)

        val newCache = shiroConfig.cacheType.newInstance().apply {
            setId(onAuthentication.token)
            setMaxAge(shiroConfig.validTime*1000L)
            putValue(shiroConfig.tokenName,ShiroInfo(onAuthorization,onAuthentication))
        }

        cacheManager.addCache(newCache)
        userLoginToken.getResponse().addCookie(Cookie(shiroConfig.tokenName, value = onAuthentication.token, maxAge = shiroConfig.validTime, path = "/"))
        return onAuthentication.token
    }

    fun logout(request: Request,response: Response){
        val token = iShiroAuth.getAccessToken(request, shiroConfig.tokenName)
        if (token!=null){
            val cache = cacheManager.getCache(token)
            if (cache!=null){
                cacheManager.removeCache(cache)
            }
            response.addCookie(Cookie(shiroConfig.tokenName, value = "", maxAge = 0, path = "/"))
        }
    }

    fun getShiroInfo(request: Request) :ShiroInfo?{
        val token = iShiroAuth.getAccessToken(request, shiroConfig.tokenName)
        if (token!=null){
            val shiroInfo = cacheManager.getCache(token)?.getValue(shiroConfig.tokenName, null) as? ShiroInfo
            if (shiroInfo!=null){
                return shiroInfo
            }
        }
        return null
    }

    fun verifyRoles(request: Request,needRoles:List<String>){
        val token = iShiroAuth.getAccessToken(request,shiroConfig.tokenName) ?: throw ShiroVerifyException("token is invalid")
        val shiroInfo = cacheManager.getCache(token)?.getValue(shiroConfig.tokenName, null) as? ShiroInfo ?:throw ShiroVerifyException("no valid auth info")
        val roles = shiroInfo.authorizationInfo.getRoles()
        if (!InnerShiroUtils.isChildList(roles,needRoles)){
            throw ShiroRoleException(*needRoles.subtract(roles.toSet()).toTypedArray())
        }
    }

    fun verifyPermissions(request: Request,needPermissions:List<String>){
        val token = iShiroAuth.getAccessToken(request,shiroConfig.tokenName) ?: throw ShiroVerifyException("token is invalid")
        val shiroInfo = cacheManager.getCache(token)?.getValue(shiroConfig.tokenName, null) as? ShiroInfo ?:throw ShiroVerifyException("no valid auth info")
        val permissions = shiroInfo.authorizationInfo.getPermissions()
        if (!InnerShiroUtils.isChildList(permissions,needPermissions)){
            throw ShiroRoleException(*needPermissions.subtract(permissions.toSet()).toTypedArray())
        }
    }

    fun verify(request: Request,needRoles: List<String>, needPermissions: List<String>){
        verifyRoles(request,needRoles)
        verifyPermissions(request,needPermissions)
    }
}