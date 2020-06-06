package com.alessandro_molinaro.social_network.a_controller;

import com.alessandro_molinaro.social_network.b_service.UtenteService;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import com.alessandro_molinaro.social_network.support.exception.EmailEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.UtenteNonEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/user")
public class UtenteController {

    @Autowired
    UtenteService utenteService;

    @PostMapping(value = "add_user")
    public ResponseEntity aggiungi(@Valid @RequestBody Utente utente, Errors errors){
        if (errors.hasErrors()) {
            return new ResponseEntity("errore di validazione dei campi", HttpStatus.BAD_REQUEST);
        }
        try {
            Utente u = utenteService.registraUtente(utente);
            return new ResponseEntity(u, HttpStatus.OK);
        } catch (EmailEsistenteException e) {
            return new ResponseEntity("email già associata ad un utente registrato", HttpStatus.BAD_REQUEST);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity("utente non ancora registrato per poter aggiungere un indirizzo", HttpStatus.BAD_REQUEST);
        }
    }

}
