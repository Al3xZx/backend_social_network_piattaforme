package com.alessandro_molinaro.social_network.d_entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "utente")
public class Utente implements Serializable {

    private static final long serialVersionUID = 2578627111708620527L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    @NotNull
    private String nome;

    @Column(nullable = false, length = 50)
    @NotNull
    private String cognome;

    @Column(length = 50)
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
    @JsonIgnore
    private Set<Utente> amici = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "richieste_amicizia",
            joinColumns = @JoinColumn(name = "id_ricevente", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_richiedente", referencedColumnName = "id"))
    @JsonIgnore
    private List<Utente> richiesteAmicizie = new LinkedList<>();//richieste di amicizia inviate da altri utenti

    @OneToMany(mappedBy = "utente")
    @JsonIgnore
    private List<Commento> commenti = new LinkedList<>();//commenti effettuati da this

    @OneToMany(mappedBy = "utente") //se elimino un post lo elimino anche dalla tabella post
    @JsonIgnore
    private List<Post> posts = new LinkedList<>();//post effettuati da this

    @OneToMany(mappedBy = "utente")
    @JsonIgnore
    private Set<Like> likes = new HashSet<>();

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

    public Set<Utente> getAmici() {
        return amici;
    }

    public void setAmici(Set<Utente> amici) {
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

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utente utente = (Utente) o;

        if (id != null ? !id.equals(utente.id) : utente.id != null) return false;
        return email != null ? email.equals(utente.email) : utente.email == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
