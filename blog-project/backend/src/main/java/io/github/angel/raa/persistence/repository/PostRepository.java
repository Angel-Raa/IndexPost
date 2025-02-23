package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Post;
import io.github.angel.raa.persistence.specification.PostSpecification;
import io.github.angel.raa.utils.Status;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends BaseJpaRepository<Post, UUID>, JpaSpecificationExecutor<Post>, PagingAndSortingRepository<Post, UUID> {
    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Post> findByCategoryId(UUID categoryId, Pageable pageable);

    @Query(value = "SELECT p FROM Post p JOIN p.tags t WHERE t.tagId = :tagId",
            countQuery = "SELECT COUNT(p) FROM Post p JOIN p.tags t WHERE t.tagId = :tagId")
    Page<Post> findByTagId(@Param("tagId") UUID tagId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Post p SET p.thumbnail = :thumbnail WHERE p.postId = :postId")
    void updateThumbnail(@Param("postId") UUID postId, @Param("thumbnail") String thumbnail);

    @Modifying
    @Query(value = "UPDATE Post p SET p.status = :status WHERE p.postId = :postId")
    void updateStatus(@Param("postId") UUID postId, @Param("status") Status status);

    Page<Post> findByStatus(Status status, Pageable pageable);

    Page<Post> findByTitleContaining(String title, Pageable pageable);

    default Page<Post> findByFilters(UUID categoryId, UUID tagId, UUID authorId, Status status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        PostSpecification specification = new PostSpecification(categoryId, tagId, authorId, status, startDate, endDate);
        return findAll(specification, pageable);
    }

}
