package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Like;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends BaseJpaRepository<Like, UUID>, PagingAndSortingRepository<Like, UUID> {
    boolean existsByUserIdAndPostId(UUID userId, UUID postId);

    /**
     * Mostrar lista de usuarios que dieron "me gusta" a un post
     * @param postId UUID
     * @param pageable Pageable
     * @return Page<Like>
     */
    @Query(value = "SELECT l.user FROM Like l WHERE l.postId =:postId")
    Page<Like> findUsersWhoLikedPost(@Param("postId") UUID postId, Pageable pageable);
    /**
     *  Dar "me gusta" a un post (USER)
     *  @param userId UUID
     *  @param postId UUID
     *  @return Like
     */
    Optional<Like> findByUserIdAndPostId(final UUID userId,final UUID postId);
    /**
     *  Eliminar "me gusta" a un post (USER)
     *  @param userId UUID
     *  @param postId UUID
     */
    void deleteByUserIdAndPostId(final UUID userId, final UUID postId);

    /**
     * Contar "me gusta" de un post
     *
     */
    @Query(value = "SELECT COUNT(l) FROM Like l WHERE l.postId =:postId")
    long countLikesByPostId(@Param("postId") final UUID postId);
    /**
     * Evitar múltiples "me gusta" del mismo usuario en un post
     */
    default boolean hasUserAlreadyLiked(UUID userId, UUID postId){
        return findByUserIdAndPostId(userId, postId).isPresent();
    }

    /**
     * Historial de "me gusta" de un usuario
     */
    @Query(value = "SELECT l.post FROM Like l WHERE l.userId =:userId")
    Page<Like> findLikedPostsByUserId(@Param("userId") UUID userId, Pageable pageable);

    /**
     *  Métricas de engagement, como la relación entre "me gusta" y comentarios en un post.
     *  Este método devuelve el número de "me gusta" y comentarios para un post específico.
     *  @return Object[]
     *
     */
    @Query(value = """
            SELECT p.post_id, COUNT(l.like_id) AS likes, COUNT(c.comment_id) AS comments
            FROM posts_table p
            LEFT JOIN likes_table l ON p.post_id = l.fk_post_id
            LEFT JOIN comments_table c ON p.post_id = c.fk_post_id
            WHERE p.post_id = :postId
            GROUP BY p.post_id
            """, nativeQuery = true)
    Object[] findEngagementMetricsForPost(@Param("postId") UUID postId);


}
