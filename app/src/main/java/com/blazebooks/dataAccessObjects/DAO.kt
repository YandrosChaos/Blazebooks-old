package com.blazebooks.dataAccessObjects

interface DAO<T> {

    fun getAll(): List<T>

    fun get(id: String): T

    fun insert(todo: T)

    fun delete(todo: T)

    fun update(todo: T)

}//interface