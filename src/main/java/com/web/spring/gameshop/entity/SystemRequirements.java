package com.web.spring.gameshop.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "sys_requirements")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemRequirements {

    @Id
    @Column(name = "game_id")
    private int gameID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "game_id")
    private Game game;

    private String processor;

    private String videocard;

    @Column(name = "disk_size")
    private int diskSize;

    @Column(name = "ram_size")
    private int ramSize;
}
