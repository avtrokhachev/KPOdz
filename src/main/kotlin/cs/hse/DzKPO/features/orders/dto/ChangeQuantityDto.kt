package cs.hse.DzKPO.features.orders.dto

data class ChangeQuantityDto(
    val id: Long,
    val quantityDelta: Long
)