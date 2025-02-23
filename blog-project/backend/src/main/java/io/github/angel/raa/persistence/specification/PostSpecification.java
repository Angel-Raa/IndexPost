package io.github.angel.raa.persistence.specification;

import io.github.angel.raa.persistence.entity.Post;
import io.github.angel.raa.utils.Status;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class PostSpecification implements Specification<Post> {
    private final UUID categoryId;
    private final UUID tagId;
    private final UUID authorId;
    private final Status status;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public PostSpecification(UUID categoryId, UUID tagId, UUID authorId, Status status, LocalDateTime startDate, LocalDateTime endDate) {
        this.categoryId = categoryId;
        this.tagId = tagId;
        this.authorId = authorId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {


        Predicate predicate = criteriaBuilder.conjunction();
        if (categoryId != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("categoryId"), categoryId));
        }
        if (tagId != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.join("tags").get("tagId"), tagId));
        }
        if (status != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
        }

        if (authorId != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("userId"), authorId));
        }

        if (startDate != null && endDate != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("createdAt"), startDate, endDate));
        }
        return predicate;

    }
}
