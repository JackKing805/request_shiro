package com.jerry.request_shiro.shiro.impl

import com.jerry.request_shiro.shiro.interfaces.IShiroCache
import com.jerry.request_shiro.shiro.interfaces.IShiroCacheManager

class ShiroCacheManager : IShiroCacheManager {
    private val lock = Any()
    private val cacheList = mutableSetOf<IShiroCache>()

    override fun addCache(cache: IShiroCache) {
        synchronized(lock) {
            cacheList.add(cache)
        }
    }

    override fun removeCache(cache: IShiroCache) {
        synchronized(lock) {
            cacheList.remove(cache)
        }
    }

    override fun getCache(id: String): IShiroCache? {
        val c = synchronized(lock) {
            cacheList.find { it.getID() == id }
        }

        if (c != null && c.isValid()) {
            return c
        }

        return null
    }

    override fun listOf(): List<IShiroCache> {
        return cacheList.toList()
    }

    override fun clear() {
        synchronized(cacheList) {
            cacheList.clear()
        }
    }
}