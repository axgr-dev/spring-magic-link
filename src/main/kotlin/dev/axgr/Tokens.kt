package dev.axgr

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Entity
@Table(name = "tokens")
class MagicToken {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_generator")
  @SequenceGenerator(name = "token_generator", sequenceName = "tokens_id_seq", allocationSize = 10)
  val id: Long? = null

  @Column(name = "created_at")
  lateinit var created: Instant
  lateinit var username: String
  lateinit var token: String

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as MagicToken

    return id == other.id
  }

  override fun hashCode(): Int = id.hashCode()

}

@Repository
interface TokenRepository : CrudRepository<MagicToken, Long> {

  fun findByToken(token: String): MagicToken?

  @Modifying
  @Transactional
  @Query("DELETE FROM MagicToken t WHERE t.created < :threshold")
  fun deleteExpired(threshold: Instant)

}
