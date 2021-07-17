package com.alessandro_molinaro.social_network.controller;

import com.alessandro_molinaro.social_network.entity.Post;
import com.alessandro_molinaro.social_network.entity.Utente;
import com.alessandro_molinaro.social_network.service.UtenteService;
import com.alessandro_molinaro.social_network.support.exception.AmiciziaEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.RichiestaAmiciziaEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.RichiestaAmiciziaNonEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.UtenteNonEsistenteException;
import com.alessandro_molinaro.social_network.support.messageForClient.Messaggio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UtenteController {

  @Autowired UtenteService utenteService;

  @GetMapping(value = "/{userId}/info")
  public ResponseEntity getInfoUtente(@PathVariable Long userId) {
    try {
      Utente u = utenteService.getUtente(userId);
      return new ResponseEntity(u, HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    }
  }

  @PostMapping(value = "/{userIdRichiedente}/send_friend_request/{userIdRicevente}")
  public ResponseEntity richiestaAmicizia(
      @PathVariable Long userIdRichiedente, @PathVariable Long userIdRicevente) {
    try {
      utenteService.inviaRichiestaAmicizia(userIdRichiedente, userIdRicevente);
      return new ResponseEntity(
          new Messaggio("richiesta registrata correttamente"), HttpStatus.CREATED);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    } catch (RichiestaAmiciziaEsistenteException e) {
      // return new ResponseEntity(new Messaggio("la richiesta è stata già
      // eseguita"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "la richiesta è stata già eseguita", e);
    } catch (AmiciziaEsistenteException e) {
      // return new ResponseEntity(new Messaggio("si è già amico con questo
      // utente"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "si è già amico con questo utente", e);
    }
  }

  @PostMapping(value = "/{userIdRicevente}/accept_friend_request/{userIdRichiedente}")
  public ResponseEntity accettaRichiestaAmicizia(
      @PathVariable Long userIdRichiedente, @PathVariable Long userIdRicevente) {
    try {
      utenteService.accettaRichiesta(userIdRichiedente, userIdRicevente);
      return new ResponseEntity(new Messaggio("richiesta accettata correttamente"), HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    } catch (RichiestaAmiciziaNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("la richiesta da parte dell'utente selezionato non
      // esiste"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "la richiesta da parte dell'utente selezionato non esiste", e);
    }
  }

  @PostMapping(value = "/{userIdRicevente}/reject_friend_request/{userIdRichiedente}")
  public ResponseEntity rifiutaRichiestaAmicizia(
      @PathVariable Long userIdRichiedente, @PathVariable Long userIdRicevente) {
    try {
      utenteService.rifiutaRichiesta(userIdRichiedente, userIdRicevente);
      return new ResponseEntity(new Messaggio("richiesta rifiutata correttamente"), HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("l'utente non esiste"), HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    } catch (RichiestaAmiciziaNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("la richiesta da parte dell'utente selezionato non
      // esiste"), HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "la richiesta da parte dell'utente selezionato non esiste", e);
    }
  }

  @GetMapping(value = "/{userIdRichiestaVerifica}/verifiva_amicizia/{userIdDaVerificare}")
  public ResponseEntity verificaAmicizia(
      @PathVariable Long userIdRichiestaVerifica, @PathVariable Long userIdDaVerificare) {
    try {
      return new ResponseEntity(
          utenteService.sonoAmici(userIdRichiestaVerifica, userIdDaVerificare), HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    }
  }

  @GetMapping(value = "/{userId}/friends_request")
  public ResponseEntity richiesteAmicizia(@PathVariable Long userId) {
    try {
      List<Utente> richieste = utenteService.richiesteAmicizia(userId);
      return new ResponseEntity(richieste, HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    }
  }

  @GetMapping(value = "/{userId}/friends_list")
  public ResponseEntity amici(@PathVariable Long userId) {
    try {
      Set<Utente> amici = utenteService.getAllAmici(userId);
      return new ResponseEntity(amici, HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    }
  }

  @GetMapping(value = "/{userId}/post_list")
  public ResponseEntity allPost(@PathVariable Long userId) {
    try {
      List<Post> post = utenteService.getAllPost(userId);
      return new ResponseEntity(post, HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    }
  }

  @GetMapping(value = "/search/{stringa}/{numPag}/{numInPage}")
  public ResponseEntity cercaUtenti(
      @PathVariable String stringa, @PathVariable int numPag, @PathVariable int numInPage) {
    List<Utente> utenti =
        utenteService.getUtentiNomeOrCognomeContain(stringa, numPag - 1, numInPage);
    // List<Utente> utenti = utenteService.getUtentiNomeOrCognomeContain(stringa);
    return new ResponseEntity(utenti, HttpStatus.OK);
  }

  @GetMapping(value = "/search/{nome}/{cognome}/{numPag}/{numInPage}")
  public ResponseEntity cercaUtenti(
      @PathVariable String nome,
      @PathVariable String cognome,
      @PathVariable int numPag,
      @PathVariable int numInPage) {
    List<Utente> utenti =
        utenteService.getUtentiNomeAndCognomeLike(nome, cognome, numPag - 1, numInPage);
    return new ResponseEntity(utenti, HttpStatus.OK);
  }
}
