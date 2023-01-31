package com.jerry.request_shiro.request

import android.util.Log
import com.jerry.request_base.annotations.Bean
import com.jerry.request_base.annotations.Configuration
import com.jerry.request_shiro.shiro.config.ShiroConfig
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.model.AuthToken
import com.jerry.request_shiro.shiro.model.AuthenticationInfo
import com.jerry.request_shiro.shiro.model.AuthorizationInfo
import java.util.UUID

@Configuration
class Config {
//    @Bean
//    fun shiroConfig() = ShiroConfig("AA",900)

    @Bean
    fun ishro() = object :IShiroAuth{
        override fun onAuthentication(authToken: AuthToken): AuthenticationInfo {
            Log.e("ADSAD","onAuthentication:${authToken}")
            return AuthenticationInfo(authToken,authToken.getPassword(),UUID.randomUUID().toString())
        }

        override fun onAuthorization(authorization: AuthenticationInfo): AuthorizationInfo {
            val au = AuthorizationInfo()
            Log.e("ADSAD","onAuthorization")
            au.setRole("user")
            au.setPermission("add")
            au.setPermission("delete")
            return au
        }
    }
}