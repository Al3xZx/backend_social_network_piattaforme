package com.alessandro_molinaro.social_network.b_service;

import com.alessandro_molinaro.social_network.c_repository.CommentoRepository;
import com.alessandro_molinaro.social_network.c_repository.LikeRepository;
import com.alessandro_molinaro.social_network.c_repository.PostRepository;
import com.alessandro_molinaro.social_network.c_repository.UtenteRepository;
import com.alessandro_molinaro.social_network.d_entity.Commento;
import com.alessandro_molinaro.social_network.d_entity.Like;
import com.alessandro_molinaro.social_network.d_entity.Post;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import com.alessandro_molinaro.social_network.support.exception.PostNonEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.UtenteNonEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentoRepository commentoRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Transactional
    public Post aggiungiPost(Long userId, Post p)throws UtenteNonEsistenteException{
        Utente utente = utenteRepository.findById(userId).get();
        if(utente == null) throw new UtenteNonEsistenteException();
        Post nuovoPost = new Post();
        nuovoPost.setTesto(p.getTesto());
        nuovoPost.setUtente(utente);
        postRepository.save(nuovoPost);
        utente.getPosts().add(nuovoPost);
        return nuovoPost;
    }

    @Transactional
    public Like aggiungiLike(Long userId, Long postId)throws UtenteNonEsistenteException, PostNonEsistenteException {
        Utente utente = utenteRepository.findById(userId).get();
        if(utente == null) throw new UtenteNonEsistenteException();
        Post post = postRepository.findById(postId).get();
        if(post == null) throw new PostNonEsistenteException();
        Like l = new Like();
        l.setPost(post);
        l.setUtente(utente);
        likeRepository.save(l);
        post.getLikes().add(l);
        utente.getLikes().add(l);
        return l;
    }

    @Transactional
    public Commento aggoingiCommento(Long userId, Long postId, Commento c)throws UtenteNonEsistenteException, PostNonEsistenteException{
        Utente utente = utenteRepository.findById(userId).get();
        if(utente == null) throw new UtenteNonEsistenteException();
        Post post = postRepository.findById(postId).get();
        if(post == null) throw new PostNonEsistenteException();
        Commento nuovoCommento = new Commento();
        nuovoCommento.setTesto(c.getTesto());
        nuovoCommento.setPost(post);
        nuovoCommento.setUtente(utente);
        commentoRepository.save(nuovoCommento);
        post.getCommenti().add(nuovoCommento);
        return nuovoCommento;
    }

}
