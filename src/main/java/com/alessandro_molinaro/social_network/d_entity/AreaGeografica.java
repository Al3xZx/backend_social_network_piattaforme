package com.alessandro_molinaro.social_network.d_entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "area_geografica")
public class AreaGeografica implements Serializable {

    private static final long serialVersionUID = -4977957439994438992L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nazione;

    @Column(nullable = false, length = 80)
    private String regione;

    @Column(nullable = false, length = 80)
    private String citta;

    @OneToMany(mappedBy = "areaGeografica")
    private List<Utente> residenti = new LinkedList<>();

    public AreaGeografica() {
    }

    public AreaGeografica(String nazione, String regione, String citta) {
        this.nazione = nazione;
        this.regione = regione;
        this.citta = citta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public List<Utente> getResidenti() {
        return residenti;
    }

    public void setResidenti(List<Utente> residenti) {
        this.residenti = residenti;
    }
}
