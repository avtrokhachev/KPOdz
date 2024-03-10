package cs.hse.DzKPO.features.orders.db.entities

import jakarta.persistence.*
import lombok.NoArgsConstructor

@Entity
@Table(name = "dish")
@NoArgsConstructor
data class Dish(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var count: Long,
    val name: String,
    var cost: Long,

    @Column(name = "wait_time")
    var waitTime: Long
)
