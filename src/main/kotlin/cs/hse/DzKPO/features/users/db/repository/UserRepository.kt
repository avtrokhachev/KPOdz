package cs.hse.DzKPO.features.users.db.repository


import cs.hse.DzKPO.features.users.db.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByLogin(login: String): Boolean

    fun findByLogin(login: String): User?
}