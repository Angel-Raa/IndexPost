package io.github.angel.raa.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "likes_table",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"fk_user_id", "fk_post_id"})
        })
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "like_id")
    private UUID likeId;
    @Column(name = "fk_user_id", insertable = true, updatable = true)
    private UUID userId;
    @Column(name = "fk_post_id", insertable = true, updatable = true)
    private UUID postId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "fk_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_post_id", referencedColumnName = "post_id", insertable = false, updatable = false)
    private Post post;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Like() {
    }

    public Like(UUID likeId, UUID userId, UUID postId, User user, Post post, LocalDateTime createdAt) {
        this.likeId = likeId;
        this.userId = userId;
        this.postId = postId;
        this.user = user;
        this.post = post;
        this.createdAt = createdAt;
    }

    public UUID getLikeId() {
        return likeId;
    }

    public void setLikeId(UUID likeId) {
        this.likeId = likeId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
