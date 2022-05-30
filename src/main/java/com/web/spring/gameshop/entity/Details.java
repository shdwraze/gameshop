package com.web.spring.gameshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Details {

    @Id
    @Column(name = "user_login")
    private String login;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_login")
    @JsonBackReference
    private User user;

    private String name;

    private String surname;

    private int age;
}
