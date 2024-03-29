package com.alessandro_molinaro.social_network.service;

import com.alessandro_molinaro.social_network.entity.Post;
import com.alessandro_molinaro.social_network.entity.Utente;
import com.alessandro_molinaro.social_network.repository.AreaGeograficaRepository;
import com.alessandro_molinaro.social_network.repository.UtenteRepository;
import com.alessandro_molinaro.social_network.support.exception.AmiciziaEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.RichiestaAmiciziaEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.RichiestaAmiciziaNonEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.UtenteNonEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UtenteService {

  @Autowired UtenteRepository utenteRepository;

  @Autowired AreaGeograficaRepository areaGeograficaRepository;

  /*-------------------------ricerca utenti------------------------------*/

  @Transactional(readOnly = true) // propagation di default è:  Propagation.REQUIRED.
  public Utente getUtente(Long userId) throws UtenteNonEsistenteException {
    Optional<Utente> o = utenteRepository.findById(userId);
    Utente u;
    if (o.isPresent()) {
      u = o.get();
      return u;
    } else throw new UtenteNonEsistenteException();
  }

  @Transactional(readOnly = true)
  public Utente getUtente(String email) throws UtenteNonEsistenteException {
    Utente utente = utenteRepository.findByEmail(email);
    if (utente == null) throw new UtenteNonEsistenteException();
    return utente;
  }

  @Transactional(readOnly = true)
  public List<Utente> getUtentiNomeOrCognomeContain(String s, int numPag, int numInPage) {
    // return utenteRepository.findAllByNomeContainingOrCognomeContaining(s);
    return utenteRepository.selAllByNomeContainingOrCognomeContaining(
        s, PageRequest.of(numPag, numInPage));
  }

  @Transactional(readOnly = true)
  public List<Utente> getUtentiNomeAndCognomeLike(
      String nome, String cognome, int numPag, int numInPage) {
    // return utenteRepository.findAllByNomeContainingOrCognomeContaining(s);
    return utenteRepository.selAllByNomeAndCognome(
        nome, cognome, PageRequest.of(numPag, numInPage));
  }

  //    @Transactional(readOnly = true)
  //    public List<Utente> getUtentiByNome(String nome){
  //        return utenteRepository.findAllByNome(nome);
  //    }
  //
  //    @Transactional(readOnly = true)
  //    public List<Utente> getUtentiByCognome(String cognome){
  //        return utenteRepository.findAllByCognome(cognome);
  //    }
  //
  //    @Transactional(readOnly = true)
  //    public List<Utente> getUtentiByNomeAndCognome(String nome, String cognome){
  //        return utenteRepository.findAllByNomeAndCognome(nome, cognome);
  //    }

  /*-------------------------gestione richiesta amicizia------------------------------*/

  @Transactional
  public void inviaRichiestaAmicizia(Long userIdRichiedente, Long userIdRicevente)
      throws UtenteNonEsistenteException, RichiestaAmiciziaEsistenteException,
          AmiciziaEsistenteException {
    Optional<Utente> oRichiedente = utenteRepository.findById(userIdRichiedente);
    Optional<Utente> oRicevente = utenteRepository.findById(userIdRicevente);
    Utente richiedente;
    Utente ricevente;
    if (oRichiedente.isPresent() && oRicevente.isPresent()) {
      richiedente = oRichiedente.get();
      ricevente = oRicevente.get();
    } else throw new UtenteNonEsistenteException();

    if (utenteRepository.richiestaAmicizia(userIdRichiedente, userIdRicevente) != null)
      throw new RichiestaAmiciziaEsistenteException();

    if (richiedente.getAmici().contains(ricevente)) throw new AmiciziaEsistenteException();

    ricevente.getRichiesteAmicizie().add(richiedente);
  }

  @Transactional
  public void accettaRichiesta(Long userIdRichiedente, Long userIdRicevente)
      throws UtenteNonEsistenteException, RichiestaAmiciziaNonEsistenteException {
    Optional<Utente> oRichiedente = utenteRepository.findById(userIdRichiedente);
    Optional<Utente> oRicevente = utenteRepository.findById(userIdRicevente);
    Utente richiedente;
    Utente ricevente;
    if (oRichiedente.isPresent() && oRicevente.isPresent()) {
      richiedente = oRichiedente.get();
      ricevente = oRicevente.get();
    } else throw new UtenteNonEsistenteException();

    if (utenteRepository.richiestaAmicizia(userIdRichiedente, userIdRicevente) == null)
      throw new RichiestaAmiciziaNonEsistenteException();
    ricevente.getRichiesteAmicizie().remove(richiedente);
    ricevente.getAmici().add(richiedente);
    richiedente.getAmici().add(ricevente);
  }

  @Transactional
  public void rifiutaRichiesta(Long userIdRichiedente, Long userIdRicevente)
      throws UtenteNonEsistenteException, RichiestaAmiciziaNonEsistenteException {
    Optional<Utente> oRichiedente = utenteRepository.findById(userIdRichiedente);
    Optional<Utente> oRicevente = utenteRepository.findById(userIdRicevente);
    Utente richiedente;
    Utente ricevente;
    if (oRichiedente.isPresent() && oRicevente.isPresent()) {
      richiedente = oRichiedente.get();
      ricevente = oRicevente.get();
    } else throw new UtenteNonEsistenteException();
    if (utenteRepository.richiestaAmicizia(userIdRichiedente, userIdRicevente) == null)
      throw new RichiestaAmiciziaNonEsistenteException();
    ricevente.getRichiesteAmicizie().remove(richiedente);
  }

  @Transactional(readOnly = true)
  public List<Utente> richiesteAmicizia(Long userId) throws UtenteNonEsistenteException {
    Optional<Utente> o = utenteRepository.findById(userId);
    Utente u;
    if (o.isPresent()) {
      u = o.get();
      return u.getRichiesteAmicizie();
    } else throw new UtenteNonEsistenteException();
  }

  /*-------------------------info amici e post------------------------------*/

  @Transactional(readOnly = true)
  public Set<Utente> getAllAmici(Long userId) throws UtenteNonEsistenteException {
    Optional<Utente> o = utenteRepository.findById(userId);
    Utente u;
    if (o.isPresent()) {
      u = o.get();
      return u.getAmici();
    } else throw new UtenteNonEsistenteException();
  }

  @Transactional(readOnly = true)
  public List<Post> getAllPost(Long userId) throws UtenteNonEsistenteException {
    Optional<Utente> o = utenteRepository.findById(userId);
    Utente u;
    if (o.isPresent()) {
      u = o.get();
      return u.getPosts();
    } else throw new UtenteNonEsistenteException();
  }

  @Transactional(readOnly = true)
  public Boolean sonoAmici(Long userIdRichiestaVerifica, Long userIdDaVerificare)
      throws UtenteNonEsistenteException {
    Optional<Utente> oRichiedente = utenteRepository.findById(userIdRichiestaVerifica);
    Optional<Utente> oRicevente = utenteRepository.findById(userIdDaVerificare);
    Utente richiedente;
    Utente daVerificare;
    if (oRichiedente.isPresent() && oRicevente.isPresent()) {
      richiedente = oRichiedente.get();
      daVerificare = oRicevente.get();
    } else throw new UtenteNonEsistenteException();
    Set<Utente> amiciRichiedente = richiedente.getAmici();
    for (Utente amico : amiciRichiedente) if (amico.getId().equals(userIdDaVerificare)) return true;
    return false;
  }
}
