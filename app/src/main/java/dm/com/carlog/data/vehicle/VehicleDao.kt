package dm.com.carlog.data.vehicle

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vehicle: Vehicle): Long

    @Delete
    suspend fun delete(vehicle: Vehicle)

    @Update
    suspend fun update(vehicle: Vehicle)

    @Query("SELECT * FROM vehicles ORDER BY created_at DESC")
    fun getAll(): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getById(id: String): Vehicle?

    
    @Transaction
    @Query(
        """
    WITH fuel_stats AS (
        SELECT 
            f.vehicle_id,
            MAX(f.odometer) AS latest_odometer,
            AVG(f.fuel_economy) AS average_fuel_economy,
            SUM(f.fuel_added) AS total_fuel_added,
            SUM(f.total_cost) AS total_spent,
            COUNT(*) AS refuel_count,
            AVG(f.fuel_added) AS avg_gallon_refueled,
            AVG(f.total_cost) AS avg_spent_per_refuel,
            MIN(f.date) AS first_refuel_date,
            MAX(f.date) AS last_refuel_date
        FROM fuels f
        GROUP BY f.vehicle_id
    )
    SELECT 
        v.*,
        COALESCE(fs.latest_odometer, 0.0) AS latest_odometer,
        COALESCE(fs.average_fuel_economy, 0.0) AS average_fuel_economy,
        COALESCE(fs.total_fuel_added, 0.0) AS total_fuel_added,
        COALESCE(fs.total_spent, 0.0) AS total_spent,
        COALESCE(fs.refuel_count, 0) AS refuel_count,
        CASE
            WHEN fs.refuel_count IS NULL OR fs.refuel_count = 0 THEN 0.0
            ELSE 
                CAST(fs.refuel_count AS REAL) /
                (
                    (CAST(strftime('%Y', datetime(fs.last_refuel_date / 1000, 'unixepoch')) AS INT) - 
                     CAST(strftime('%Y', datetime(fs.first_refuel_date / 1000, 'unixepoch')) AS INT)) * 12 +
                    (CAST(strftime('%m', datetime(fs.last_refuel_date / 1000, 'unixepoch')) AS INT) - 
                     CAST(strftime('%m', datetime(fs.first_refuel_date / 1000, 'unixepoch')) AS INT)) + 1
                )
        END AS refuel_per_month,
        
        COALESCE(fs.avg_gallon_refueled, 0.0) AS avg_gallon_refueled,
        COALESCE(fs.avg_spent_per_refuel, 0.0) AS avg_spent_per_refuel
        
    FROM vehicles v
    LEFT JOIN fuel_stats fs ON fs.vehicle_id = v.id
    ORDER BY v.created_at DESC
    """
    )
    fun getAllWithStats(): Flow<List<VehicleWithStats>>

    
    @Transaction
    @Query(
        """
    WITH fuel_stats AS (
        SELECT 
            f.vehicle_id,
            MAX(f.odometer) AS latest_odometer,
            AVG(f.fuel_economy) AS average_fuel_economy,
            SUM(f.fuel_added) AS total_fuel_added,
            SUM(f.total_cost) AS total_spent,
            COUNT(*) AS refuel_count,
            AVG(f.fuel_added) AS avg_gallon_refueled,
            AVG(f.total_cost) AS avg_spent_per_refuel,
            MIN(f.date) AS first_refuel_date,
            MAX(f.date) AS last_refuel_date
        FROM fuels f
        WHERE f.vehicle_id = :id
        GROUP BY f.vehicle_id
    )
    SELECT 
        v.*,
        COALESCE(fs.latest_odometer, 0) AS latest_odometer,
        COALESCE(fs.average_fuel_economy, 0.0) AS average_fuel_economy,
        COALESCE(fs.total_fuel_added, 0.0) AS total_fuel_added,
        COALESCE(fs.total_spent, 0.0) AS total_spent,
        COALESCE(fs.refuel_count, 0) AS refuel_count,
        
        CASE
            WHEN fs.refuel_count IS NULL OR fs.refuel_count = 0 THEN 0.0
            ELSE 
                CAST(fs.refuel_count AS REAL) /
                (
                    (CAST(strftime('%Y', datetime(fs.last_refuel_date / 1000, 'unixepoch')) AS INT) - 
                     CAST(strftime('%Y', datetime(fs.first_refuel_date / 1000, 'unixepoch')) AS INT)) * 12 +
                    (CAST(strftime('%m', datetime(fs.last_refuel_date / 1000, 'unixepoch')) AS INT) - 
                     CAST(strftime('%m', datetime(fs.first_refuel_date / 1000, 'unixepoch')) AS INT)) + 1
                )
        END AS refuel_per_month,
        
        COALESCE(fs.avg_gallon_refueled, 0.0) AS avg_gallon_refueled,
        COALESCE(fs.avg_spent_per_refuel, 0.0) AS avg_spent_per_refuel
        
    FROM vehicles v
    LEFT JOIN fuel_stats fs ON fs.vehicle_id = v.id
    WHERE v.id = :id
    """
    )
    suspend fun getByIdWithStats(id: String): VehicleWithStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vehicles: List<Vehicle>)

    @Query("DELETE FROM vehicles")
    suspend fun deleteAll()

    @Query("SELECT * FROM vehicles")
    suspend fun getAllSnapshot(): List<Vehicle>
}
