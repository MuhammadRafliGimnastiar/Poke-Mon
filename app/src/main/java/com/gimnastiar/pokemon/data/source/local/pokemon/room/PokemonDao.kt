package com.gimnastiar.pokemon.data.source.local.pokemon.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gimnastiar.pokemon.data.source.local.pokemon.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Transaction
    suspend fun insertWithLimit(pokemon: PokemonEntity) {
        val currentCount = getCount()
        if (currentCount >= 10) {
            deleteOldest()
        }
        insertPokemon(pokemon)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun getCount(): Int

    @Query("DELETE FROM pokemon WHERE id = (SELECT id FROM pokemon ORDER BY id ASC LIMIT 1)")
    suspend fun deleteOldest()

    @Query("SELECT * FROM pokemon ORDER BY id DESC")
    fun getAllPokemonFlow(): Flow<List<PokemonEntity>>

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()
}