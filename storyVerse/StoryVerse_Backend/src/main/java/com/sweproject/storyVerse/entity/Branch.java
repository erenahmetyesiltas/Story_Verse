package com.sweproject.storyVerse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "branch")
public class Branch extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storyId")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Story story;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contributorId")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Contributor contributor;

    @Lob
    @Column(columnDefinition = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentBranchId")
    @JsonBackReference
    private Branch parentBranch; // If it is null, then it is the root branch

    @OneToMany(mappedBy = "parentBranch", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Branch> childBranches = new ArrayList<>();

    private double rate;

    private String title;

    private Long likeCounter = 0L;

    private Long dislikeCounter = 0L;
    // List<Branch> subBranches;
    // Like // Dislike
}