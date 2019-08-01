package com.synowkrz.housemanager.babyTask.model

import androidx.room.Entity
import androidx.room.PrimaryKey


enum class DiperType {
    PEE, POO, MIX
}

@Entity
class Diaper(@PrimaryKey(autoGenerate = true)
             val id: Int,
             val profileName: String,
             val diperType: DiperType,
             val day: String,
             val time: String)