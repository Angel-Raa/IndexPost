package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Category;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends BaseJpaRepository<Category, UUID>, PagingAndSortingRepository<Category, UUID> {
    boolean existsBySlug(String slug);

    Optional<Category> findBySlug(String slug);

    Page<Category> findByNameContaining(String name, Pageable pageable);

    Optional<Category> findByName(String name);

    @Modifying
    @Query(value = "UPDATE Post p SET p.categoryId =:categoryId WHERE p.postId = :postId")
    void assignCategoryToPost(@Param("postId") UUID postId, @Param("categoryId") UUID categoryId);

    @Query("SELECT COUNT(p) FROM Post WHERE p.categoryId = :categoryId")
    long countPostByCategoryId(@Param("categoryId") UUID categoryId);
}
