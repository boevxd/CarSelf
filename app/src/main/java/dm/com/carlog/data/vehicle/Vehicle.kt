package dm.com.carlog.data.vehicle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vehicles",
    indices = [Index(value = ["created_at"])]
)
data class Vehicle(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,

    @ColumnInfo(name = "model")
    val model: String,

    @ColumnInfo(name = "year")
    val year: Int,

    @ColumnInfo(name = "license_plate")
    val licensePlate: String,

    @ColumnInfo(name = "vin")
    val vin: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()

)