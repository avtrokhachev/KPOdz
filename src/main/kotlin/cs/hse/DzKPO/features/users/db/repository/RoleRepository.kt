package cs.hse.DzKPO.features.users.db.repository

import cs.hse.DzKPO.features.users.db.entities.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun existsByRoleName(name: Role.RoleName): Boolean

    fun findByRoleName(name: Role.RoleName): Role?
}