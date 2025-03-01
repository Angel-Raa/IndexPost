package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Tag;
import io.github.angel.raa.persistence.specification.TagSpecification;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TagRepository extends BaseJpaRepository<Tag, UUID>, PagingAndSortingRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {
    default Page<Tag> findByFilters(String name, String slug, boolean enabled, Pageable pageable){
        TagSpecification specification = new TagSpecification(name, slug, enabled);
        return findAll(specification, pageable);

    }
    Page<Tag> findByEnabledTrue(Pageable pageable);

    /**
     * Deshabilitar etiquetas (en vez de eliminar)
     * @param tagId UUID
     * @return long
     */
    @Modifying
    @Query(value = "UPDATE Tag t SET t.enabled = false WHERE t.tagId = :tagId")
    long disableTag(@Param("tagId") UUID tagId);
    @Modifying
    @Query(value = "UPDATE Tag t SET t.enabled = true WHERE t.tagId = :tagId")
    long enableTag(@Param("tagId") UUID tagId);
    boolean existsBySlug(final String slug);
    /**
     * Asignar múltiples etiquetas a un post
     * @param postId UUID post id
     * @param tagId UUID tag id
     */
    @Modifying
    @Query(value = "INSERT INTO PostTag (post_id, tag_id) VALUES (:postId, :tagId)", nativeQuery = true)
    void addTagsToPost(@Param("postId") UUID postId, @Param("tagId") UUID tagId);

    /**
     * Eliminar múltiples etiquetas de un post
     * @param postId UUID post id
     * @param tagId UUID tag id
     */
    @Modifying
    @Query(value = "DELETE FROM PostTag pt WHERE pt.post_id = :postId AND pt.tag_id = :tagId", nativeQuery = true)
    void removeTagsFromPost(@Param("postId") UUID postId, @Param("tagId") UUID tagId);
    /**
     * Obtener todas las etiquetas ordenadas por fecha de creación descendente
     * @param pageable Pageable
     * @return Page<Tag>
     */
    Page<Tag> findAllByOrderByCreatedAtDesc(Pageable pageable);




}
