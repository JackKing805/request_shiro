package com.jerry.request_shiro.shiro.anno

import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS,AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class ShiroPermission(
    val permission:Array<String>//权限
)

