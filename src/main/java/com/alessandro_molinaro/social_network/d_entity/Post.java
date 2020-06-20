package com.alessandro_molinaro.social_network.d_entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "post")
public class Post implements Serializable {

    private static final long serialVersionUID = -3637580651751852114L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "dd/MM/yyyy")
//    @Basic
//    @Temporal(TemporalType.TIMESTAMP)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy@HH:mm:ss")
    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    @Column(name = "contenuto_testuale", nullable = false)
    @Lob //per indicare longtext
    private String testo;

    @ManyToOne
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    //@JsonIgnore
    private List<Commento> commenti = new LinkedList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    //@JsonIgnore
    private Set<Like> likes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public List<Commento> getCommenti() {
        return commenti;
    }

    public void setCommenti(List<Commento> commenti) {
        this.commenti = commenti;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }


}
