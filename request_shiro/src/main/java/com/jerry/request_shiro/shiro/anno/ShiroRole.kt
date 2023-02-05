package com.jerry.request_shiro.shiro.anno

import com.jerry.request_shiro.shiro.bean.ShiroLogic
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS,AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class ShiroRole(
    val roles:Array<String>,//用户角色
    val logic: ShiroLogic = ShiroLogic.AND
)

