package com.alessandro_molinaro.social_network.b_service;

import com.alessandro_molinaro.social_network.c_repository.AreaGeograficaRepository;
import com.alessandro_molinaro.social_network.c_repository.UtenteRepository;
import com.alessandro_molinaro.social_network.d_entity.AreaGeografica;
import com.alessandro_molinaro.social_network.d_entity.Post;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import com.alessandro_molinaro.social_network.support.exception.EmailEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.UtenteNonEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    AreaGeograficaRepository areaGeograficaRepository;

    @Transactional
    public Utente registraUtente(Utente u) throws EmailEsistenteException, UtenteNonEsistenteException {
        if(utenteRepository.existsByEmail(u.getEmail()))
            throw new EmailEsistenteException();
        Utente nuovo = new Utente(u.getNome(), u.getCognome(), u.getEmail(), u.getGenere());
        utenteRepository.save(nuovo);
        if(u.getAreaGeografica() != null)
            aggiungiAreaGeografica(nuovo.getId(), u.getAreaGeografica());
        return nuovo;
    }

    @Transactional
    public void aggiungiAreaGeografica(Long userId, AreaGeografica ag) throws UtenteNonEsistenteException {
        AreaGeografica area = areaGeograficaRepository
                .findByNazioneAndRegioneAndCitta(ag.getNazione(), ag.getRegione(), ag.getCitta());
        Utente utente = utenteRepository.findById(userId).get();
        if(utente == null) throw new UtenteNonEsistenteException();
        if(area == null){
            area = new AreaGeografica(ag.getNazione(), ag.getRegione(), ag.getCitta());
            areaGeograficaRepository.save(area);
        }
        utente.setAreaGeografica(area);
        area.getResidenti().add(utente);
    }

    /*-------------------------ricerca utenti------------------------------*/

    @Transactional(readOnly = true)
    public Utente getUtente(Long userId)throws UtenteNonEsistenteException{
        Utente utente = utenteRepository.findById(userId).get();
        if(utente == null) throw new UtenteNonEsistenteException();
        return utente;
    }

    @Transactional(readOnly = true)
    public Utente getUtente(String email)throws UtenteNonEsistenteException{
        Utente utente = utenteRepository.findByEmail(email);
        if(utente == null) throw new UtenteNonEsistenteException();
        return utente;
    }

    @Transactional(readOnly = true)
    public List<Utente> getUtentiByNome(String nome){
        return utenteRepository.findAllByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Utente> getUtentiByCognome(String cognome){
        return utenteRepository.findAllByCognome(cognome);
    }

    @Transactional(readOnly = true)
    public List<Utente> getUtentiByNomeAndCognome(String nome, String cognome){
        return utenteRepository.findAllByNomeAndCognome(nome, cognome);
    }

    //TODO ricerca utenti per città, regione o nazione

    /*-------------------------gestione richiesta amicizia------------------------------*/

    @Transactional
    public void inviaRichiestaAmicizia(Long userIdRichiedente, Long userIdRicevente) throws UtenteNonEsistenteException{
        Utente richiedente = utenteRepository.findById(userIdRichiedente).get();
        Utente ricevente = utenteRepository.findById(userIdRicevente).get();
        if(richiedente == null || ricevente == null) throw new UtenteNonEsistenteException();
        ricevente.getRichiesteAmicizie().add(richiedente);
    }

    @Transactional
    public void accettaRichiesta(Long userIdRichiedente, Long userIdRicevente)throws UtenteNonEsistenteException{
        Utente richiedente = utenteRepository.findById(userIdRichiedente).get();
        Utente ricevente = utenteRepository.findById(userIdRicevente).get();
        if(richiedente == null || ricevente == null) throw new UtenteNonEsistenteException();
        ricevente.getRichiesteAmicizie().remove(richiedente);
        ricevente.getAmici().add(richiedente);
        richiedente.getAmici().add(ricevente);
    }

    @Transactional
    public void rifiutaRichiesta(Long userIdRichiedente, Long userIdRicevente)throws UtenteNonEsistenteException{
        Utente richiedente = utenteRepository.findById(userIdRichiedente).get();
        Utente ricevente = utenteRepository.findById(userIdRicevente).get();
        if(richiedente == null || ricevente == null) throw new UtenteNonEsistenteException();
        ricevente.getRichiesteAmicizie().remove(richiedente);
    }

    @Transactional(readOnly = true)
    public List<Utente> richiesteAmicizia(Long userId)throws UtenteNonEsistenteException{
        Utente utente = utenteRepository.findById(userId).get();
        if(utente == null) throw new UtenteNonEsistenteException();
        return utente.getRichiesteAmicizie();
    }

    /*-------------------------info amici e post------------------------------*/

    public List<Utente> getAmici(Long userId)throws UtenteNonEsistenteException{
        Utente utente = utenteRepository.findById(userId).get();
        if(utente == null) throw new UtenteNonEsistenteException();
        return utente.getAmici();
    }

    public List<Post> getPost(Long userId)throws UtenteNonEsistenteException{
        Utente utente = utenteRepository.findById(userId).get();
        if(utente == null) throw new UtenteNonEsistenteException();
        return utente.getPosts();
    }
}
