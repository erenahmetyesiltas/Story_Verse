package com.sweproject.storyVerse.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
@Getter
@Setter
public class Review extends BaseEntity {

    private String title;

    @Lob
    @Column(columnDefinition = "text")
    private String text;
    private byte point;


    //@OneToOne
    //private Contributor contributor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storyId")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Story story;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branchId")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;
}
