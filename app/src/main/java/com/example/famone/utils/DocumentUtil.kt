package com.example.famone.utils

object DocumentUtil {
    fun getCategories(): ArrayList<String>{
        val list = ArrayList<String>()
        list.add("Receipts")
        list.add("Household")
        list.add("Transport")
        list.add("Gadgets")
        list.add("Miscellaneous")
        return list
    }
}