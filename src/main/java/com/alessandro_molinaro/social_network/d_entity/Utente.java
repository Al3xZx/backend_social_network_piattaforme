package com.alessandro_molinaro.social_network.d_entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 50)
    private String cognome;

    @Column(nullable = false, length = 50)
    @NotNull
    @Email
    private String email;

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
    @JoinTable(name = "richiesta_amicizia",
            joinColumns = @JoinColumn(name = "id_richiedente", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_ricevente", referencedColumnName = "id"))
    private List<Utente> richiesteAmicizie = new LinkedList<>();

    @OneToMany(mappedBy = "utente")
    private List<Commento> commenti = new LinkedList<>();

    @OneToMany(mappedBy = "utente")
    private List<Post> posts = new LinkedList<>();

    @OneToMany(mappedBy = "utente")
    private List<Like> likes = new LinkedList<>();

    public Utente() {
    }

    public Utente(String nome, String cognome, @Email String email, Boolean genere) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.genere = genere;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getGenere() {
        return genere;
    }

    public void setGenere(Boolean genere) {
        this.genere = genere;
    }

    public AreaGeografica getAreaGeografica() {
        return areaGeografica;
    }

    public void setAreaGeografica(AreaGeografica areaGeografica) {
        this.areaGeografica = areaGeografica;
    }

    public List<Utente> getAmici() {
        return amici;
    }

    public void setAmici(List<Utente> amici) {
        this.amici = amici;
    }

    public List<Utente> getRichiesteAmicizie() {
        return richiesteAmicizie;
    }

    public void setRichiesteAmicizie(List<Utente> richiesteAmicizie) {
        this.richiesteAmicizie = richiesteAmicizie;
    }

    public List<Commento> getCommenti() {
        return commenti;
    }

    public void setCommenti(List<Commento> commenti) {
        this.commenti = commenti;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
