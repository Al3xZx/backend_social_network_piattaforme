package com.alessandro_molinaro.social_network.a_controller;

import com.alessandro_molinaro.social_network.b_service.UtenteService;
import com.alessandro_molinaro.social_network.d_entity.AreaGeografica;
import com.alessandro_molinaro.social_network.d_entity.Post;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import com.alessandro_molinaro.social_network.support.exception.*;
import com.alessandro_molinaro.social_network.support.messageForClient.Messaggio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping(value = "/user")
public class UtenteController {

    @Autowired
    UtenteService utenteService;

    @PostMapping(value = "/add_user")
    public ResponseEntity aggiungi(@Valid @RequestBody Utente utente, Errors errors){
        if (errors.hasErrors()) {
            return new ResponseEntity(new Messaggio("errore di validazione dei campi"), HttpStatus.BAD_REQUEST);
        }
        try {
            Utente u = utenteService.registraUtente(utente);
            return new ResponseEntity(u, HttpStatus.OK);
        } catch (EmailEsistenteException e) {
            return new ResponseEntity(new Messaggio("email già associata ad un utente registrato"),HttpStatus.BAD_REQUEST);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("utente non ancora registrato per poter aggiungere un indirizzo"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{userId}/set_geographic_area")
    public ResponseEntity setAreaGeografica(@PathVariable Long userId, @RequestBody AreaGeografica a){
        try{
            AreaGeografica area = utenteService.aggiungiModificaAreaGeografica(userId, a);
            return new ResponseEntity(area,HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}/info")
    public ResponseEntity getInfoUtente(@PathVariable Long userId){
        try{
            Utente u = utenteService.getUtente(userId);
            return new ResponseEntity(u,HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/{userIdRichiedente}/send_friend_request/{userIdRicevente}")
    public ResponseEntity richiestaAmicizia(@PathVariable Long userIdRichiedente, @PathVariable Long userIdRicevente){
        try{
            utenteService.inviaRichiestaAmicizia(userIdRichiedente,userIdRicevente);
            return new ResponseEntity(new Messaggio("richiesta registrata correttamente"),HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
        } catch (RichiestaAmiciziaEsistenteException e) {
            return new ResponseEntity(new Messaggio("la richiesta è stata già eseguita"),HttpStatus.BAD_REQUEST);
        } catch (AmiciziaEsistenteException e) {
            return new ResponseEntity(new Messaggio("si è già amico con questo utente"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{userIdRicevente}/accept_friend_request/{userIdRichiedente}")
    public ResponseEntity accettaRichiestaAmicizia(@PathVariable Long userIdRichiedente, @PathVariable Long userIdRicevente){
        try{
            utenteService.accettaRichiesta(userIdRichiedente, userIdRicevente);
            return new ResponseEntity(new Messaggio("richiesta accettata correttamente"),HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
        } catch (RichiestaAmiciziaNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("la richiesta da parte dell'utente selezionato non esiste"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/{userIdRicevente}/reject_friend_request/{userIdRichiedente}")
    public ResponseEntity rifiutaRichiestaAmicizia(@PathVariable Long userIdRichiedente, @PathVariable Long userIdRicevente){
        try{
            utenteService.rifiutaRichiesta(userIdRichiedente, userIdRicevente);
            return new ResponseEntity(new Messaggio("richiesta rifiutata correttamente"), HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("l'utente non esiste"), HttpStatus.BAD_REQUEST);
        } catch (RichiestaAmiciziaNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("la richiesta da parte dell'utente selezionato non esiste"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}/friends_request")
    public ResponseEntity richiesteAmicizia(@PathVariable Long userId){
        try{
            List<Utente> richieste = utenteService.richiesteAmicizia(userId);
            return new ResponseEntity(richieste,HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}/friends_list")
    public ResponseEntity amici(@PathVariable Long userId){
        try{
            Set<Utente> amici = utenteService.getAllAmici(userId);
            return new ResponseEntity(amici,HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}/post")
    public ResponseEntity post(@PathVariable Long userId){
        try{
            List<Post> post = utenteService.getAllPost(userId);
            return new ResponseEntity(post,HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/search/{stringa}/{numPag}/{numOfPag}")
    public ResponseEntity richiesteAmicizia(@PathVariable String stringa, @PathVariable int numPag, @PathVariable int numOfPag){
        List<Utente> utenti = utenteService.getUtentiNomeOrCognomeContain(stringa, numPag-1, numOfPag);
        //List<Utente> utenti = utenteService.getUtentiNomeOrCognomeContain(stringa);
        return new ResponseEntity(utenti,HttpStatus.OK);
    }
}
