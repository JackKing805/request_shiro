package com.jerry.request_shiro.shiro.anno

import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS,AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class ShiroRole(
    val role:Array<String>//用户角色
)

