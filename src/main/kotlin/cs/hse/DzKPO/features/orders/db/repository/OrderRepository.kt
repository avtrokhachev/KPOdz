package cs.hse.DzKPO.features.orders.db.repository

import cs.hse.DzKPO.features.orders.db.entities.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>