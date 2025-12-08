package dm.com.carlog.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dm.com.carlog.data.fuel.Fuel
import dm.com.carlog.data.fuel.FuelDao
import dm.com.carlog.data.vehicle.Vehicle
import dm.com.carlog.data.vehicle.VehicleDao

@Database(
    entities = [Vehicle::class, Fuel::class],
    version = 1, // NOTE: Create local migration when changing version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun fuelDao(): FuelDao
}