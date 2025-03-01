package io.github.angel.raa.persistence.specification;

import io.github.angel.raa.persistence.entity.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class is used to filter the categories by name and slug
 */
public class CategorySpecification implements Specification<Category> {
    private final String name;
    private final String slug;
    private final boolean enabled;

    public CategorySpecification(String name, String slug,boolean enabled) {
        this.name = name;
        this.slug = slug;
        this.enabled = enabled;
    }
    @Override
    public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();
        if (name != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (slug != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), "%" + slug.toLowerCase() + "%"));
        }
        if (enabled) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.isTrue(root.get("enabled")));
        }
        return predicate;
    }
}
