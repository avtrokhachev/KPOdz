package cs.hse.DzKPO.features.orders.dto

data class DishDto(
    val name: String,
    val count: Long,
    val cost: Long,
    val waitTime: Long,
)