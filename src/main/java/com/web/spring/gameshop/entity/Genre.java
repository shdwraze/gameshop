package com.web.spring.gameshop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genre")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "game_genres",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id"))
    private List<Game> games;

}
