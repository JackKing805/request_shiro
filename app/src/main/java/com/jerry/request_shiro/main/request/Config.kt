package com.jerry.request_shiro.main.request

import android.util.Log
import com.jerry.request_base.annotations.Bean
import com.jerry.request_base.annotations.Configuration
import com.jerry.request_core.additation.DefaultRtConfigRegister
import com.jerry.request_shiro.shiro.config.ShiroConfig
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.model.AuthToken
import com.jerry.request_shiro.shiro.model.AuthenticationInfo
import com.jerry.request_shiro.shiro.model.AuthorizationInfo
import com.jerry.rt.core.http.Client
import com.jerry.rt.core.http.pojo.Request
import com.jerry.rt.core.http.pojo.Response
import java.util.UUID

@Configuration
class Config {
    @Bean
    fun config() = ShiroConfig(
        tokenName = "AA",
        validTime = 50000
    )
    @Bean
    fun ishro() = object :IShiroAuth{
        override fun onAuthentication(authToken: AuthToken): AuthenticationInfo {
            return AuthenticationInfo(authToken,authToken.getPassword(),UUID.randomUUID().toString())
        }

        override fun onAuthorization(authorization: AuthenticationInfo): AuthorizationInfo {
            val au = AuthorizationInfo()
            au.setRole("a")
            au.setPermission("delete")
            return au
        }
    }

    @Bean
    fun rtClient() = object :DefaultRtConfigRegister.RtClient{
        override fun handUrl(): String {
            return "/a/b"
        }

        override fun onRtIn(client: Client, request: Request, response: Response) {

        }

        override fun onRtMessage(request: Request, response: Response) {

        }

        override fun onRtOut(client: Client) {

        }
    }
}