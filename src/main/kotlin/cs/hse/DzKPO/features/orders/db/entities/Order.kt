package cs.hse.DzKPO.features.orders.db.entities

import jakarta.persistence.*

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var status: OrderStatus,
    var price: Long
) {
    enum class OrderStatus {
        CREATED,
        COOKING,
        PAYMENT_REQUIRED,
        PAID,
        CANCELED
    }
}



