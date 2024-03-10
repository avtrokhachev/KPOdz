package cs.hse.DzKPO.features.orders.db.entities

import jakarta.persistence.*

@Entity
@Table(name = "specific_dishes")
data class SpecificDish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var status: SpecificDishStatus,
    var orderId: Long,
    var dishId: Long
) {
    enum class SpecificDishStatus {
        WAITING,
        COOKING,
        DONE,
        CANCELED
    }
}



