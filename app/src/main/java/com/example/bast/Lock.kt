package com.example.bast

import java.util.*

class Lock(
    var lockName: String                        // name of lock
) {
    val lockID                                  // id of lock
            : Int
    private var lockIDIterator = 0              // iterator used to id lock starting from 0
    var lockList: MutableMap<String, Int> =     // list of locks
        HashMap()

    fun lockIDGenerator(): Int {
        lockIDIterator++
        return lockIDIterator
    }

    fun renameLock(name: String) {
        lockName = name
    }

    init {
        lockID = lockIDGenerator()
        lockList[lockName] = lockID
    }
}