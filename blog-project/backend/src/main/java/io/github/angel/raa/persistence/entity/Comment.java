package io.github.angel.raa.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments_table", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_user_id", "fk_post_id"}))
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID commentId;
    @Column(name = "fk_user_id", updatable = true, insertable = true)
    private UUID userId;
    @Column(name = "fk_post_id", updatable = true, insertable = true)
    private UUID postId;
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_post_id", referencedColumnName = "post_id", insertable = false, updatable = false)
    private Post post;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Comment() {
    }

    public Comment(UUID userId, UUID commentId, UUID postId, User user, String content, Post post, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.commentId = commentId;
        this.postId = postId;
        this.user = user;
        this.content = content;
        this.post = post;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public void setCommentId(UUID commentId) {
        this.commentId = commentId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
