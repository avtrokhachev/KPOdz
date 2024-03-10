package cs.hse.DzKPO.features.users.services

import cs.hse.DzKPO.features.users.db.entities.Role
import cs.hse.DzKPO.features.users.db.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {
    fun createRole(role: Role): Role {
        if (roleRepository.existsByRoleName(role.roleName)) {
            return roleRepository.findByRoleName(role.roleName)!!
        }
        return roleRepository.save(role)
    }

    fun findRoleByName(roleName: Role.RoleName): Role? {
        return roleRepository.findByRoleName(roleName)
    }
}