package dm.com.carlog.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dm.com.carlog.data.AppDatabase
import dm.com.carlog.data.fuel.FuelDao
import dm.com.carlog.data.fuel.FuelRepository
import dm.com.carlog.data.vehicle.VehicleDao
import dm.com.carlog.data.vehicle.VehicleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "car_log_db")
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()

    @Provides
    fun provideVehicleDao(db: AppDatabase): VehicleDao = db.vehicleDao()

    @Provides
    fun provideFuelDao(db: AppDatabase) = db.fuelDao()

    @Provides
    @Singleton
    fun provideVehicleRepository(dao: VehicleDao): VehicleRepository =
        VehicleRepository(dao)

    @Provides
    @Singleton
    fun provideFuelRepository(vehicleDao: VehicleDao, fuelDao: FuelDao): FuelRepository =
        FuelRepository(vehicleDao, fuelDao)

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("car_log_prefs", Context.MODE_PRIVATE)
    }
}