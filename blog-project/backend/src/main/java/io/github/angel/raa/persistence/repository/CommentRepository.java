package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Comment;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends BaseJpaRepository<Comment, UUID>, PagingAndSortingRepository<Comment, UUID> {
    boolean existsByCommentId(final UUID commentId);

    long countByPostId(final UUID postId);

    /**
     * Listar comentarios por publicación, con paginación y ordenación por fecha.
     *
     * @param postId   UUID
     * @param pageable Pageable
     * @return Page<Comment>
     */
    @Query(value = "SELECT c FROM Comment c WHERE c.postId = :postId AND c.enabled = true ORDER BY c.createdAt DESC")
    Page<Comment> findByPostIdOrderByCreatedAtDesc(@Param("postId") UUID postId, Pageable pageable);

    /**
     * Deshabilitar un comentario
     *
     * @param commentId UUID
     */
    @Modifying
    @Query(value = "UPDATE Comment c SET c.enabled = false WHERE c.commentId =:commentId")
    void disableComment(@Param("commentId") UUID commentId);

    /**
     * Actualizar el contenido de un comentario
     *
     * @param commentId UUID
     * @param content   String
     */
    @Modifying
    @Query(value = "UPDATE Comment c SET c.content = :content WHERE c.commentId = :commentId")
    void updateComment(@Param("commentId") UUID commentId, @Param("content") String content);

    /**
     *  Ordenar posts por cantidad de "me gusta"
     * @param pageable Pageable
     * @return Page<Object[]>
     */
    @Query("SELECT l.post, COUNT(l) AS likeCount FROM Like l GROUP BY l.post ORDER BY likeCount DESC")
    Page<Object[]> findPostsSortedByLikes(Pageable pageable);
}
