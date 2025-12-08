package dm.com.carlog.data.vehicle

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VehicleRepository @Inject constructor(
    private val vehicleDao: VehicleDao
) {
    suspend fun insert(vehicle: Vehicle): Long {
        return vehicleDao.insert(vehicle)
    }

    suspend fun delete(vehicle: Vehicle) {
        vehicleDao.delete(vehicle)
    }

    suspend fun update(vehicle: Vehicle) {
        vehicleDao.update(vehicle)
    }

    fun getAll() = vehicleDao.getAll()

    suspend fun getById(id: String) = vehicleDao.getById(id)

    fun getAllWithStats(): Flow<List<VehicleWithStats>> = vehicleDao.getAllWithStats()

    suspend fun getByIdWithStats(id: String): VehicleWithStats? = vehicleDao.getByIdWithStats(id)
}