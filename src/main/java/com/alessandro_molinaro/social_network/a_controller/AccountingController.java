package com.alessandro_molinaro.social_network.a_controller;

import com.alessandro_molinaro.social_network.b_service.AccountingService;
import com.alessandro_molinaro.social_network.d_entity.AccountUtente;
import com.alessandro_molinaro.social_network.d_entity.AreaGeografica;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import com.alessandro_molinaro.social_network.support.exception.*;
import com.alessandro_molinaro.social_network.support.messageForClient.Messaggio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/accounting")
@CrossOrigin(origins="http://localhost:4200")
public class AccountingController {

    @Autowired
    AccountingService accountingService;


    @PostMapping(value = "/registra_utente/{username}/{password}")
    public ResponseEntity registrazione(
            @Valid @RequestBody Utente utente, @PathVariable String username, @PathVariable String password){

        if (utente == null ||utente.getNome() == null || utente.getCognome() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nome e cognome obbligatori");
        }
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username obbligatorio");
        }
        if (password == null || password.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "errore di validazione password");
        }

        AccountUtente accountUtente = new AccountUtente(username, password);
        try {
            Utente u = accountingService.registraAccount(accountUtente, utente);
            return new ResponseEntity(u, HttpStatus.CREATED);
        } catch (EmailEsistenteException e) {
            //return new ResponseEntity(new Messaggio("email già associata ad un utente registrato"),HttpStatus.BAD_REQUEST);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email già associata ad un utente registrato", e);
        } catch (UtenteNonEsistenteException e) {
            //return new ResponseEntity(new Messaggio("utente non ancora registrato per poter aggiungere un indirizzo"),HttpStatus.BAD_REQUEST);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registrazione non andata a buon fine!!", e);
        } catch (UsernameEsistenteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username già associata ad un utente registrato", e);
        }
    }

    @PostMapping(value = "/{userId}/set_geographic_area")
    public ResponseEntity setAreaGeografica(@PathVariable Long userId, @RequestBody AreaGeografica a){
        try{
            AreaGeografica area = accountingService.aggiungiModificaAreaGeografica(userId, a);
            return new ResponseEntity(area,HttpStatus.CREATED);
        } catch (UtenteNonEsistenteException e) {
            //return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
        }
    }

    @PostMapping(value = "/verifica_accesso")
    public ResponseEntity verificaRegistrazione(@Valid @RequestBody AccountUtente accountUtente){
        try{
            Utente u = accountingService.verificaRegistrazione(accountUtente);
            return new ResponseEntity(u,HttpStatus.OK);
        } catch (PasswordErrataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password errata", e);
        } catch (UsernameErratoException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste oppure è errato", e);
        }
    }
}
