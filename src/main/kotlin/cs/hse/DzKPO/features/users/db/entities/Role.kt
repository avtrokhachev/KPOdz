package cs.hse.DzKPO.features.users.db.entities

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    var roleName: RoleName
) : GrantedAuthority {
    enum class RoleName {
        USER,
        ADMIN
    }

    override fun getAuthority() = roleName.name
}