package org.example.project.portofolio.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing the user's cash balance.
 * This class is used to define the structure of the user balance data that will be stored in the local database using Room.
 */
@Entity
data class UserBalanceEntity(
    @PrimaryKey val id: Int = 1,
    val cashBalance: Double,
)