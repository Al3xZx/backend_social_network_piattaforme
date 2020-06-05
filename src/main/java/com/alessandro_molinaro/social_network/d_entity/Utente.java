package com.alessandro_molinaro.social_network.d_entity;

import com.sun.xml.bind.v2.TODO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "utente")
public class Utente implements Serializable {

    private static final long serialVersionUID = 2578627111708620527L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    private Boolean genere;//false maschio , true femmina

    @ManyToOne
    @JoinColumn(name = "id_area_geografica", nullable = true)
    private AreaGeografica areaGeografica;

    //verificare....//TODO
    @ManyToMany()
    @JoinTable(name = "amici",
            joinColumns = {@JoinColumn(name = "id_utente_A", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_utente_B", referencedColumnName = "id")})
    private List<Utente> amici = new LinkedList<>();

    @ManyToMany()
    @JoinTable(name = "richieste_amicizie",
            joinColumns = {@JoinColumn(name = "id_richiedente", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_ricevente", referencedColumnName = "id")})
    private List<Utente> richiesteAmicizie = new LinkedList<>();

    @OneToMany(mappedBy = "utente")
    private List<Commento> commenti = new LinkedList<>();

    @OneToMany(mappedBy = "utente")
    private List<Post> posts = new LinkedList<>();

    @OneToMany(mappedBy = "utente")
    private List<Like> likes = new LinkedList<>();

}
