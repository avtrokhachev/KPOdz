package cs.hse.DzKPO.features.orders.db.repository

import cs.hse.DzKPO.features.orders.db.entities.Dish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DishRepository : JpaRepository<Dish, Long>


