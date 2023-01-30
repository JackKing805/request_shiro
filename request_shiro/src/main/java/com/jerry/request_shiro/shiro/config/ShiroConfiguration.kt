package com.jerry.request_shiro.shiro.config

import com.jerry.request_base.annotations.Bean
import com.jerry.request_base.annotations.Configuration
import com.jerry.request_shiro.shiro.core.ShiroSessionManager
import com.jerry.rt.bean.RtConfig
import com.jerry.rt.bean.RtSessionConfig
import java.time.Duration

@Configuration
class ShiroConfiguration {

    @Bean
    fun shiroConfig():RtConfig{
        return RtConfig(rtSessionConfig = RtSessionConfig(
            sessionClazz = ShiroSessionManager::class.java,
            sessionKey = "SHIRO_SESSION_ID",
            sessionValidTime = Duration.ofMinutes(15)
        ))
    }
}