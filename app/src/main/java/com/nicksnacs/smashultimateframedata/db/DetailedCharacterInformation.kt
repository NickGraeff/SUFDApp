package com.nicksnacs.smashultimateframedata.db

import androidx.room.*

class DetailedCharacterInformation(
    val characterName: String,
    val groundAttacks: List<GroundAttack>,
    val aerialAttacks: List<AerialAttack>,
    val specialAttacks: List<SpecialAttack>,
    val grabThrows: List<GrabThrow>,
    val dodgeRolls: List<DodgeRoll>,
    val misc: List<Misc>
) {
    val size: Int
        get() {
            return groundAttacks.size +
                    aerialAttacks.size +
                    specialAttacks.size +
                    grabThrows.size +
                    dodgeRolls.size +
                    1
        }
}

@Dao
abstract class DetailedCharacterInformationGetter {
    @Transaction
    open fun getDetailedCharacterInformation(characterName: String): DetailedCharacterInformation {
        return DetailedCharacterInformation(
            characterName,
            getGroundAttacks(characterName).apply { if (isEmpty()) throw IllegalStateException("No data for this character") },
            getAerialAttacks(characterName).apply { if (isEmpty()) throw IllegalStateException("No data for this character") },
            getSpecialAttacks(characterName).apply { if (isEmpty()) throw IllegalStateException("No data for this character") },
            getGrabThrows(characterName).apply { if (isEmpty()) throw IllegalStateException("No data for this character") },
            getDodgeRolls(characterName).apply { if (isEmpty()) throw IllegalStateException("No data for this character") },
            getMisc(characterName).apply { if (isEmpty()) throw IllegalStateException("No data for this character") }
        )
    }

    @Transaction
    open fun insertDetailedCharacterInformation(detailedCharacterInformation: DetailedCharacterInformation) {
        insertGroundAttacks(detailedCharacterInformation.groundAttacks)
        insertAerialAttacks(detailedCharacterInformation.aerialAttacks)
        insertSpecialAttacks(detailedCharacterInformation.specialAttacks)
        insertGrabThrows(detailedCharacterInformation.grabThrows)
        insertDodgeRolls(detailedCharacterInformation.dodgeRolls)
        insertMisc(detailedCharacterInformation.misc)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertGroundAttacks(groundAttacks: List<GroundAttack>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertAerialAttacks(aerialAttacks: List<AerialAttack>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertSpecialAttacks(specialAttacks: List<SpecialAttack>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertGrabThrows(grabThrows: List<GrabThrow>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertDodgeRolls(dodgeRolls: List<DodgeRoll>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertMisc(misc: List<Misc>)

    @Transaction
    @Query("SELECT * FROM GroundAttack WHERE characterName = :characterName")
    protected abstract fun getGroundAttacks(characterName: String): List<GroundAttack>

    @Transaction
    @Query("SELECT * FROM AerialAttack WHERE characterName = :characterName")
    protected abstract fun getAerialAttacks(characterName: String): List<AerialAttack>

    @Transaction
    @Query("SELECT * FROM SpecialAttack WHERE characterName = :characterName")
    protected abstract fun getSpecialAttacks(characterName: String): List<SpecialAttack>

    @Transaction
    @Query("SELECT * FROM GrabThrow WHERE characterName = :characterName")
    protected abstract fun getGrabThrows(characterName: String): List<GrabThrow>

    @Transaction
    @Query("SELECT * FROM DodgeRoll WHERE characterName = :characterName")
    protected abstract fun getDodgeRolls(characterName: String): List<DodgeRoll>

    @Transaction
    @Query("SELECT * FROM Misc WHERE characterName = :characterName")
    protected abstract fun getMisc(characterName: String): List<Misc>
}

@Entity(primaryKeys = ["characterName", "moveName"])
abstract class Attack(val characterName: String,
                      val moveName: String,
                      val hitboxImageUrl: String?,
                      val startup: String,
                      val totalFrames: String,
                      val landingLag: String,
                      val notes: String,
                      val baseDamage: String,
                      val shieldLag: String,
                      val shieldStun: String,
                      val whichHitbox: String,
                      val advantage: String,
                      val activeFrames: String)

@Entity
class GroundAttack(characterName: String,
                   moveName: String,
                   hitboxImageUrl: String?,
                   startup: String,
                   totalFrames: String,
                   landingLag: String,
                   notes: String,
                   baseDamage: String,
                   shieldLag: String,
                   shieldStun: String,
                   whichHitbox: String,
                   advantage: String,
                   activeFrames: String) :
    Attack(characterName,
        moveName,
        hitboxImageUrl,
        startup,
        totalFrames,
        landingLag,
        notes,
        baseDamage,
        shieldLag,
        shieldStun,
        whichHitbox,
        advantage,
        activeFrames)

@Entity
class AerialAttack(characterName: String,
                   moveName: String,
                   hitboxImageUrl: String?,
                   startup: String,
                   totalFrames: String,
                   landingLag: String,
                   notes: String,
                   baseDamage: String,
                   shieldLag: String,
                   shieldStun: String,
                   whichHitbox: String,
                   advantage: String,
                   activeFrames: String) :
    Attack(characterName,
        moveName,
        hitboxImageUrl,
        startup,
        totalFrames,
        landingLag,
        notes,
        baseDamage,
        shieldLag,
        shieldStun,
        whichHitbox,
        advantage,
        activeFrames)

@Entity
class SpecialAttack(characterName: String,
                    moveName: String,
                    hitboxImageUrl: String?,
                    startup: String,
                    totalFrames: String,
                    landingLag: String,
                    notes: String,
                    baseDamage: String,
                    shieldLag: String,
                    shieldStun: String,
                    whichHitbox: String,
                    advantage: String,
                    activeFrames: String) :
    Attack(characterName,
        moveName,
        hitboxImageUrl,
        startup,
        totalFrames,
        landingLag,
        notes,
        baseDamage,
        shieldLag,
        shieldStun,
        whichHitbox,
        advantage,
        activeFrames)

@Entity(primaryKeys = ["characterName", "moveName"])
data class GrabThrow(val characterName: String,
                     val moveName: String,
                     val hitboxImageUrl: String?,
                     val startup: String,
                     val totalFrames: String,
                     val landingLag: String,
                     val notes: String,
                     val baseDamage: String)

@Entity(primaryKeys = ["characterName", "moveName"])
data class DodgeRoll(val characterName: String,
                     val moveName: String,
                     val hitboxImageUrl: String?,
                     val totalFrames: String,
                     val landingLag: String,
                     val notes: String)

@Entity(primaryKeys = ["characterName", "moveName"])
data class Misc(val characterName: String,
                val misc: String,
                val moveName: String = "misc")