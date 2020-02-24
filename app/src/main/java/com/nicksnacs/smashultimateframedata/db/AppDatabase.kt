package com.nicksnacs.smashultimateframedata.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GroundAttack::class, AerialAttack::class, SpecialAttack::class, GrabThrow::class, DodgeRoll::class, Misc::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun detailedCharacterInformationGetter(): DetailedCharacterInformationGetter
}