package io.github.angel.raa.persistence.specification;

import io.github.angel.raa.persistence.entity.Like;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class LikeSpecification implements Specification<Like> {
    @Override
    public Predicate toPredicate(Root<Like> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
