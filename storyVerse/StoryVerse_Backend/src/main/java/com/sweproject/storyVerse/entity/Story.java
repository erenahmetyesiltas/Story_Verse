package com.sweproject.storyVerse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "story")
@Getter
@Setter
public class Story extends BaseEntity {
    private String title;
    private int totalContributorsNumber;
    private String description;
    private boolean isDraft;

    @Lob
    @Column(columnDefinition = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId")
    @OnDelete(action = OnDeleteAction.CASCADE) // When the author is deleted, then the story is deleted with this annotation
    @JsonIgnore
    private Author author;

    @ManyToMany
    @JoinTable(
            name = "story_genre",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "story_contributor",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "contributor_id")
    )
    private List<Contributor> contributors;

    private double rate;

    private Long likeCounter = 0L;

    private Long dislikeCounter = 0L;

    // Like Dislike
}
