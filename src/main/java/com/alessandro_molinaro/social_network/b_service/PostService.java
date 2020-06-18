package com.alessandro_molinaro.social_network.b_service;

import com.alessandro_molinaro.social_network.c_repository.CommentoRepository;
import com.alessandro_molinaro.social_network.c_repository.LikeRepository;
import com.alessandro_molinaro.social_network.c_repository.PostRepository;
import com.alessandro_molinaro.social_network.c_repository.UtenteRepository;
import com.alessandro_molinaro.social_network.d_entity.Commento;
import com.alessandro_molinaro.social_network.d_entity.Like;
import com.alessandro_molinaro.social_network.d_entity.Post;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import com.alessandro_molinaro.social_network.support.exception.LikePresenteException;
import com.alessandro_molinaro.social_network.support.exception.NonAmiciException;
import com.alessandro_molinaro.social_network.support.exception.PostNonEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.UtenteNonEsistenteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
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
    public void removePost(long postId, Long userId)throws UtenteNonEsistenteException, PostNonEsistenteException {
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
//        List<Post> posts= utente.getPosts();
//        if(!posts.remove(post))
//            throw new PostNonEsistenteException();
        postRepository.delete(post);
    }

    @Transactional
    public Like aggiungiLike(Long userId, Long postId)
            throws UtenteNonEsistenteException, PostNonEsistenteException, LikePresenteException {
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

        if(likeRepository.existsByUtenteAndPost(utente, post))
            throw new LikePresenteException();

        Like l = new Like();
        l.setPost(post);
        l.setUtente(utente);
        likeRepository.save(l);
        post.getLikes().add(l);
        utente.getLikes().add(l);
        return l;
    }

    @Transactional
    public Commento aggiungiCommento(Long userId, Long postId, Commento c)
            throws UtenteNonEsistenteException, PostNonEsistenteException, NonAmiciException {
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
        if(!utente.getAmici().contains(post.getUtente()))
            throw new NonAmiciException();
        Commento nuovoCommento = new Commento();
        nuovoCommento.setTesto(c.getTesto());
        nuovoCommento.setPost(post);
        nuovoCommento.setUtente(utente);
        commentoRepository.save(nuovoCommento);
        post.getCommenti().add(nuovoCommento);
        return nuovoCommento;
    }

    @Transactional(readOnly = true)
    public List<Post> getPostPage(Long userId, int numPage, int numInPage) throws UtenteNonEsistenteException {
        Optional<Utente> o = utenteRepository.findById(userId);
        Utente u;
        if(o.isPresent()) {
            u = o.get();
            return postRepository.findAllByUtente(u, PageRequest.of(numPage, numInPage, Sort.by("dataCreazione").descending()));
        } else
            throw new UtenteNonEsistenteException();
    }

    @Transactional
    public List<Post> getPostDegliAmici(Long userId, int numPage, int numInPage) throws UtenteNonEsistenteException {
        Optional<Utente> o = utenteRepository.findById(userId);
        Utente u;
        if(o.isPresent()) {
            u = o.get();
            return postRepository.selAllPostDiAmici(userId, PageRequest.of(numPage, numInPage, Sort.by("data_creazione").descending()));
        } else
            throw new UtenteNonEsistenteException();
    }
}
