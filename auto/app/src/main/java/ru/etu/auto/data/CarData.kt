package ru.etu.auto.data

data class CarModel(
    val brand: String,
    val model: String,
    val oilChangeIntervalKm: Int,        // Интервал замены масла (км)
    val fullInspectionIntervalKm: Int,   // Интервал полной проверки (км)
    val tireRotationIntervalKm: Int,     // Интервал ротации шин (км)
    val brakeCheckIntervalKm: Int        // Интервал проверки тормозов (км)
)

object CarData {
    val carModels = listOf(
        // BMW
        CarModel("BMW", "3 Series", 10000, 30000, 8000, 20000),
        CarModel("BMW", "5 Series", 10000, 30000, 8000, 20000),
        CarModel("BMW", "X3", 10000, 30000, 8000, 20000),
        CarModel("BMW", "X5", 10000, 30000, 8000, 20000),
        CarModel("BMW", "7 Series", 10000, 30000, 8000, 20000),

        // Mercedes-Benz
        CarModel("Mercedes-Benz", "C-Class", 10000, 30000, 10000, 20000),
        CarModel("Mercedes-Benz", "E-Class", 10000, 30000, 10000, 20000),
        CarModel("Mercedes-Benz", "S-Class", 10000, 30000, 10000, 20000),
        CarModel("Mercedes-Benz", "GLC", 10000, 30000, 10000, 20000),
        CarModel("Mercedes-Benz", "GLE", 10000, 30000, 10000, 20000),

        // Lada
        CarModel("Lada", "Vesta", 15000, 30000, 10000, 15000),
        CarModel("Lada", "Granta", 15000, 30000, 10000, 15000),
        CarModel("Lada", "Niva", 10000, 20000, 8000, 15000),
        CarModel("Lada", "Kalina", 15000, 30000, 10000, 15000),
        CarModel("Lada", "Priora", 15000, 30000, 10000, 15000),

        // Toyota
        CarModel("Toyota", "Corolla", 10000, 40000, 10000, 20000),
        CarModel("Toyota", "Camry", 10000, 40000, 10000, 20000),
        CarModel("Toyota", "RAV4", 10000, 40000, 10000, 20000),
        CarModel("Toyota", "Prius", 10000, 40000, 10000, 20000),
        CarModel("Toyota", "Land Cruiser", 10000, 40000, 10000, 20000),

        // Audi
        CarModel("Audi", "A3", 10000, 30000, 8000, 20000),
        CarModel("Audi", "A4", 10000, 30000, 8000, 20000),
        CarModel("Audi", "Q5", 10000, 30000, 8000, 20000),
        CarModel("Audi", "A6", 10000, 30000, 8000, 20000),
        CarModel("Audi", "Q7", 10000, 30000, 8000, 20000)
    )

    // Общие рекомендации для пользовательских марок/моделей
    private val defaultRecommendations = CarModel(
        brand = "Custom",
        model = "Custom",
        oilChangeIntervalKm = 10000,    // Среднее значение для большинства авто
        fullInspectionIntervalKm = 30000, // Типичный интервал полной проверки
        tireRotationIntervalKm = 10000,   // Стандартная рекомендация
        brakeCheckIntervalKm = 20000      // Средний интервал для тормозов
    )

    fun getBrands(): List<String> = carModels.map { it.brand }.distinct().sorted()

    fun getModelsForBrand(brand: String): List<String> =
        carModels.filter { it.brand == brand }.map { it.model }

    fun getRecommendations(brand: String, model: String): CarModel {
        return carModels.find { it.brand == brand && it.model == model } ?: defaultRecommendations
    }
}