package cs.hse.DzKPO.features.users.db.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val login: String,

    @JsonIgnore
    val passWord: String,

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    val roles: List<Role> = emptyList()
) : UserDetails {
    @JsonIgnore
    override fun getUsername() = this.login

    @JsonIgnore
    override fun getPassword() = this.passWord

    @JsonIgnore
    override fun getAuthorities() = this.roles

    @JsonIgnore
    override fun isAccountNonExpired() = true

    @JsonIgnore
    override fun isAccountNonLocked() = true

    @JsonIgnore
    override fun isCredentialsNonExpired() = true

    @JsonIgnore
    override fun isEnabled() = true
}