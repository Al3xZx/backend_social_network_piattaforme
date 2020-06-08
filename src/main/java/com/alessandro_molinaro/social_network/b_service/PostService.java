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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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
        Optional<Utente> o = utenteRepository.findById(userId);
        Utente u;
        if(o.isPresent()) {
            u = o.get();
            Post nuovoPost = new Post();
            nuovoPost.setTesto(p.getTesto());
            nuovoPost.setUtente(u);
            postRepository.save(nuovoPost);
            u.getPosts().add(nuovoPost);
            return nuovoPost;
        } else
            throw new UtenteNonEsistenteException();
    }

    @Transactional
    public Like aggiungiLike(Long userId, Long postId)throws UtenteNonEsistenteException, PostNonEsistenteException {
        Optional<Post> op = postRepository.findById(postId);
        Optional<Utente> ou = utenteRepository.findById(userId);
        Utente utente;
        Post post;
        if(ou.isPresent()) {
            utente = ou.get();
        } else
            throw new UtenteNonEsistenteException();
        if(op.isPresent()) {
            post = op.get();
        } else
            throw new PostNonEsistenteException();

        Like l = new Like();
        l.setPost(post);
        l.setUtente(utente);
        likeRepository.save(l);
        post.getLikes().add(l);
        utente.getLikes().add(l);
        return l;
    }

    @Transactional
    public Commento aggiungiCommento(Long userId, Long postId, Commento c)throws UtenteNonEsistenteException, PostNonEsistenteException{
        Optional<Post> op = postRepository.findById(postId);
        Optional<Utente> ou = utenteRepository.findById(userId);
        Utente utente;
        Post post;
        if(ou.isPresent()) {
            utente = ou.get();
        } else
            throw new UtenteNonEsistenteException();
        if(op.isPresent()) {
            post = op.get();
        } else
            throw new PostNonEsistenteException();

        Commento nuovoCommento = new Commento();
        nuovoCommento.setTesto(c.getTesto());
        nuovoCommento.setPost(post);
        nuovoCommento.setUtente(utente);
        commentoRepository.save(nuovoCommento);
        post.getCommenti().add(nuovoCommento);
        return nuovoCommento;
    }

    @Transactional
    public List<Post> getPostPage(Long userId, int numPage, int numOfPage) throws UtenteNonEsistenteException {
        Optional<Utente> o = utenteRepository.findById(userId);
        Utente u;
        if(o.isPresent()) {
            u = o.get();
            return postRepository.findAllByUtente(userId, PageRequest.of(numPage, numOfPage, Sort.by("dataCreazione").descending()));
        } else
            throw new UtenteNonEsistenteException();
    }

    @Transactional
    public List<Post> getPostDegliAmici(Long userId, int numPage, int numOfPage) throws UtenteNonEsistenteException {
        Optional<Utente> o = utenteRepository.findById(userId);
        Utente u;
        if(o.isPresent()) {
            u = o.get();
            return null;//TODO
        } else
            throw new UtenteNonEsistenteException();
    }
}
