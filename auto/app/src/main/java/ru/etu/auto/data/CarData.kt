package ru.etu.auto.data

data class CarModel(
    val brand: String,
    val model: String,
    val oilChangeIntervalKm: Int = 10000,        // Default oil change interval (km)
    val fullInspectionIntervalKm: Int = 30000,   // Default full inspection interval (km)
    val tireRotationIntervalKm: Int = 10000,     // Default tire rotation interval (km)
    val brakeCheckIntervalKm: Int = 20000        // Default brake check interval (km)
) {
    // Helper property for full display name
    val fullName: String
        get() = "$brand $model"
}

object CarData {
    private val carModels = listOf(
        // German Manufacturers
        CarModel("BMW", "3 Series", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),
        CarModel("BMW", "5 Series", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),
        CarModel("BMW", "X3", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),
        CarModel("BMW", "X5", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),
        CarModel("BMW", "7 Series", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),

        CarModel("Mercedes-Benz", "C-Class"),
        CarModel("Mercedes-Benz", "E-Class"),
        CarModel("Mercedes-Benz", "S-Class"),
        CarModel("Mercedes-Benz", "GLC"),
        CarModel("Mercedes-Benz", "GLE"),

        CarModel("Audi", "A3", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),
        CarModel("Audi", "A4", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),
        CarModel("Audi", "Q5", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),
        CarModel("Audi", "A6", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),
        CarModel("Audi", "Q7", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 30000, tireRotationIntervalKm = 8000),

        // Russian Manufacturer
        CarModel("Lada", "Vesta", oilChangeIntervalKm = 15000, fullInspectionIntervalKm = 30000),
        CarModel("Lada", "Granta", oilChangeIntervalKm = 15000, fullInspectionIntervalKm = 30000),
        CarModel("Lada", "Niva", oilChangeIntervalKm = 10000, fullInspectionIntervalKm = 20000, tireRotationIntervalKm = 8000),
        CarModel("Lada", "Kalina", oilChangeIntervalKm = 15000, fullInspectionIntervalKm = 30000),
        CarModel("Lada", "Priora", oilChangeIntervalKm = 15000, fullInspectionIntervalKm = 30000),

        // Japanese Manufacturer
        CarModel("Toyota", "Corolla", fullInspectionIntervalKm = 40000),
        CarModel("Toyota", "Camry", fullInspectionIntervalKm = 40000),
        CarModel("Toyota", "RAV4", fullInspectionIntervalKm = 40000),
        CarModel("Toyota", "Prius", fullInspectionIntervalKm = 40000),
        CarModel("Toyota", "Land Cruiser", fullInspectionIntervalKm = 40000)
    )

    // Default fallback model
    private val defaultModel = CarModel(brand = "Unknown", model = "Custom")

    // Grouped access to brands and models
    private val modelsByBrand = carModels.groupBy { it.brand }

    fun getBrands(): List<String> = modelsByBrand.keys.sorted()

    fun getModelsForBrand(brand: String): List<String> =
        modelsByBrand[brand]?.map { it.model }?.sorted() ?: emptyList()

    fun getCarModel(brand: String, model: String): CarModel =
        carModels.find { it.brand == brand && it.model == model } ?: defaultModel

    // New utility function to check if a brand exists
    fun hasBrand(brand: String): Boolean = modelsByBrand.containsKey(brand)

    // New utility function to get all models
    fun getAllModels(): List<CarModel> = carModels.toList()
}