package org.example.springassignment2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "User",
        uniqueConstraints = @UniqueConstraint(columnNames = "name") // UNIQUE INDEX
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id; // 작성자 ID

    @Column(length = 31, unique = true, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String email;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Post> posts; // 작성한 게시글

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments; // 작성한 댓글

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
