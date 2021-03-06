package com.boan.apps.cabinet.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Table(name = "Comments")
@Entity
@Getter
@Setter
@Indexed
@NoArgsConstructor
public class Comment implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", nullable = false)
    String id;

    @Column(name = "extra", nullable = false)
    String extra ="";

    @Column(name = "text", nullable = false)
    public String text = "";

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,

            mappedBy= "comments"
    )
    Set<Card> cards = new HashSet<Card>();
}
