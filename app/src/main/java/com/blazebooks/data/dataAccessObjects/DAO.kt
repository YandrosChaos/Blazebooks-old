package com.blazebooks.data.dataAccessObjects

interface DAO<T> {

    fun getAll(): List<T>

    fun get(id: String): T

    fun insert(todo: T)

    fun delete(id: String)

    fun update(todo: T)

}//interface