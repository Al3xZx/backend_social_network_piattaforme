package com.alessandro_molinaro.social_network.b_service;

import com.alessandro_molinaro.social_network.c_repository.AccountUtenteRepository;
import com.alessandro_molinaro.social_network.c_repository.AreaGeograficaRepository;
import com.alessandro_molinaro.social_network.c_repository.UtenteRepository;
import com.alessandro_molinaro.social_network.d_entity.AccountUtente;
import com.alessandro_molinaro.social_network.d_entity.AreaGeografica;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import com.alessandro_molinaro.social_network.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountingService {

    @Autowired
    AccountUtenteRepository accountUtenteRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    AreaGeograficaRepository areaGeograficaRepository;


    @Transactional(readOnly = true)
    public Utente verificaRegistrazione(AccountUtente a)
            throws PasswordErrataException, UsernameErratoException {
        Optional<AccountUtente> ol = accountUtenteRepository.findById(a.getUsername());
        if(ol.isPresent()) {
            AccountUtente l = ol.get();
            if (!l.getPassword().equals(a.getPassword()))
                throw new PasswordErrataException();
            return l.getDatiUtente();
        }else
            throw new UsernameErratoException();
    }

    @Transactional
    public Utente registraAccount(AccountUtente a, Utente utente)
            throws UsernameEsistenteException, EmailEsistenteException, UtenteNonEsistenteException {
        Optional<AccountUtente> ol = accountUtenteRepository.findById(a.getUsername());
        if(ol.isPresent()) throw new UsernameEsistenteException();
        //if(password.length() < 8) throw new PasswordNonSufficienteException();//impostare il @Valid
        AccountUtente nuovoAccount = new AccountUtente();
        nuovoAccount.setUsername(a.getUsername());
        nuovoAccount.setPassword(a.getPassword());
        nuovoAccount.setDatiUtente(registraUtente(utente));
        accountUtenteRepository.save(nuovoAccount);
        return nuovoAccount.getDatiUtente();
    }

    @Transactional
    public Utente registraUtente(Utente u) throws EmailEsistenteException, UtenteNonEsistenteException {
        if(utenteRepository.existsByEmail(u.getEmail()))
            throw new EmailEsistenteException();
        Utente nuovo = new Utente(u.getNome(), u.getCognome(), u.getEmail(), u.getGenere());
        utenteRepository.save(nuovo);
        if(u.getAreaGeografica() != null)
            aggiungiModificaAreaGeografica(nuovo.getId(), u.getAreaGeografica());
        return nuovo;
    }

    @Transactional
    public AreaGeografica aggiungiModificaAreaGeografica(Long userId, AreaGeografica ag)
            throws UtenteNonEsistenteException {
        AreaGeografica area = areaGeograficaRepository
                .findByNazioneAndRegioneAndCitta(ag.getNazione(), ag.getRegione(), ag.getCitta());
        Optional<Utente> ou = utenteRepository.findById(userId);
        Utente utente = ou.isPresent() ? ou.get() : null;
        if(utente == null)
            throw new UtenteNonEsistenteException();
        if(area == null){
            area = new AreaGeografica(ag.getNazione(), ag.getRegione(), ag.getCitta());
            areaGeograficaRepository.save(area);
        }
        if(utente.getAreaGeografica() != null){
            Optional<AreaGeografica> oa = areaGeograficaRepository.findById(utente.getAreaGeografica().getId());
            if(oa.isPresent()) {
                AreaGeografica a = oa.get();
                a.getResidenti().remove(utente);
            }
        }
        utente.setAreaGeografica(area);
        area.getResidenti().add(utente);
        return area;
    }
}
