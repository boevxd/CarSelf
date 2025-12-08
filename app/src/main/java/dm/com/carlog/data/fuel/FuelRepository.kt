package dm.com.carlog.data.fuel

import dm.com.carlog.data.vehicle.VehicleDao
import javax.inject.Inject

class FuelRepository @Inject constructor(
    private val vehicleDao: VehicleDao,
    private val fuelDao: FuelDao
) {
    suspend fun insert(fuel: Fuel): Long {
        return fuelDao.insert(calculateDynamicFields(fuel))
    }

    suspend fun delete(fuel: Fuel) {
        fuelDao.delete(fuel)
    }

    suspend fun update(fuel: Fuel) {
        fuelDao.update(calculateDynamicFields(fuel))
    }

    fun getAll() = fuelDao.getAll()

    suspend fun getById(id: String) = fuelDao.getById(id)

    fun getByVehicleId(vehicleId: String) = fuelDao.getByVehicleId(vehicleId)

    private suspend fun calculateDynamicFields(fuel: Fuel): Fuel {
        val vehicle = vehicleDao.getById(fuel.vehicleId)
            ?: throw IllegalArgumentException("Vehicle with id ${fuel.vehicleId} not found")

        val totalCost = fuel.fuelAdded * fuel.pricePerGallon
        val fuelEconomy = if (fuel.trip > 0) fuel.trip / fuel.fuelAdded else 0.0
        val costPerMile = if (fuel.trip > 0) totalCost / fuel.trip else 0.0

        return fuel.copy(
            totalCost = totalCost,
            fuelEconomy = fuelEconomy,
            costPerMile = costPerMile,
        )
    }
}