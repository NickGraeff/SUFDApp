package com.nicksnacs.smashultimateframedata.db

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class BasicCharacterInformation(@ColumnInfo(name = "name") val name: String,
                                     @ColumnInfo(name = "character_data_url") val characterDataUrl: String,
                                     @ColumnInfo(name = "character_image_url") val imageUrl: String)
