package com.jerry.request_shiro.shiro.exception

class ShiroPermissionException(vararg permissions:String):Exception("don't have ${permissions.toList()}")