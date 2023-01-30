package com.jerry.request_shiro.shiro.model

class AuthToken(private val username:String,private val password:Any) {
     fun getPassword() = password
     fun getUserName() = username
}