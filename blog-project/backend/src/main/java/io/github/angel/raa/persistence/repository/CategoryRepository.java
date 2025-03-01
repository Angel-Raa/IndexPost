package io.github.angel.raa.persistence.repository;

import io.github.angel.raa.persistence.entity.Category;
import io.github.angel.raa.persistence.specification.CategorySpecification;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends BaseJpaRepository<Category, UUID>, PagingAndSortingRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    boolean existsBySlug(String slug);
    Page<Category> findByEnabledTrue(Pageable pageable);
    Optional<Category> findBySlug(String slug);
    Page<Category> findByNameContaining(String name, Pageable pageable);
    Optional<Category> findByName(String name);
    @Modifying
    @Query(value = "UPDATE Post p SET p.categoryId =:categoryId WHERE p.postId = :postId")
    void assignCategoryToPost(@Param("postId") UUID postId, @Param("categoryId") UUID categoryId);
    default Page<Category> findByFilters(final String name, final String slug, boolean enabled, Pageable pageable) {
        CategorySpecification specification = new CategorySpecification(name, slug, enabled);
        return findAll(specification, pageable);
    }
    @Modifying
    @Query(value = "UPDATE Category c SET c.name = :name, c.slug = :slug WHERE c.categoryId = :categoryId")
    void updateCategory(@Param("categoryId") UUID categoryId, @Param("name") String name, @Param("slug") String slug);
    // Buscar categorías ordenadas por nombre, fecha de creación o cantidad de posts
    @Query(value = "SELECT c FROM Category c ORDER BY c.name ASC")
    Page<Category> findAllOrderedByName(Pageable pageable);
    @Query(value = "SELECT c FROM Category c ORDER BY c.createdAt ASC")
    Page<Category> findAllOrderedByCreatedAt(Pageable pageable);
    /**
     * Deshabilitar categorías (en vez de eliminar)
     * @param categoryId UUID
     * @return long
     */
    @Modifying
    @Query(value = "UPDATE Category c SET c.enabled = false WHERE c.categoryId = :categoryId")
    long disableCategory(@Param("categoryId") UUID categoryId);
    // Contar el número de posts asociados a una categoría
    @Query(value = "SELECT COUNT(p) FROM Post p WHERE p.category.categoryId = :categoryId")
    long countPostsByCategoryId(@Param("categoryId") UUID categoryId);

}
