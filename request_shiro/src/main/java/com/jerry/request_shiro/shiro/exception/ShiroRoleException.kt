package com.jerry.request_shiro.shiro.exception

class ShiroRoleException(vararg roles:String):Exception("don't have ${roles.toList()}")