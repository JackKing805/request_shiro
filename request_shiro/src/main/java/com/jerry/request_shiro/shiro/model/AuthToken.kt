package com.jerry.request_shiro.shiro.model

import java.io.Serializable

class AuthToken(private val username:String,private val password:Any) :Serializable{
     fun getPassword() = password
     fun getUserName() = username
}