package com.web.spring.gameshop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "publisher")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String country;

    @Column(name = "founding_year")
    private int foundingYear;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "publisher",
            fetch = FetchType.LAZY)
    private List<Game> games;
}
