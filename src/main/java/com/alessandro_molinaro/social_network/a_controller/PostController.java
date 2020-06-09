package com.alessandro_molinaro.social_network.a_controller;

import com.alessandro_molinaro.social_network.b_service.PostService;
import com.alessandro_molinaro.social_network.d_entity.Commento;
import com.alessandro_molinaro.social_network.d_entity.Like;
import com.alessandro_molinaro.social_network.d_entity.Post;
import com.alessandro_molinaro.social_network.support.exception.LikePresenteException;
import com.alessandro_molinaro.social_network.support.exception.NonAmiciException;
import com.alessandro_molinaro.social_network.support.exception.PostNonEsistenteException;
import com.alessandro_molinaro.social_network.support.exception.UtenteNonEsistenteException;
import com.alessandro_molinaro.social_network.support.messageForClient.Messaggio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping(value = "/add/{userId}")
    public ResponseEntity<Post> addPost(@RequestBody Post post, @PathVariable Long userId){
        try {
            Post p = postService.aggiungiPost(userId, post);
            return new ResponseEntity(p, HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{userId}/{postId}")
    public ResponseEntity delPost(@PathVariable Long postId, @PathVariable Long userId){
        try {
            postService.removePost(postId, userId);
            return new ResponseEntity(new Messaggio("post eliminato correttamente"), HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("utente non esistente"), HttpStatus.BAD_REQUEST);
        } catch (PostNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("post non esistente"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "{postId}/add_like_from/{userId}")
    public ResponseEntity addLike(@PathVariable Long postId, @PathVariable Long userId){
        try {
            Like l = postService.aggiungiLike(userId,postId);
            return new ResponseEntity(l, HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("utente non esistente"), HttpStatus.BAD_REQUEST);
        } catch (PostNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("post non esistente"), HttpStatus.BAD_REQUEST);
        } catch (LikePresenteException e) {
            return new ResponseEntity(new Messaggio("like già registrato"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "{postId}/add_comment_from/{userId}")
    public ResponseEntity addCommento(@PathVariable Long postId, @PathVariable Long userId, @RequestBody Commento commento){
        try {
            postService.aggiungiCommento(userId, postId, commento);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("utente non esistente"), HttpStatus.BAD_REQUEST);
        } catch (PostNonEsistenteException e) {
            return new ResponseEntity(new Messaggio("post non esistente"), HttpStatus.BAD_REQUEST);
        } catch (NonAmiciException e) {
            return new ResponseEntity(new Messaggio("non è possibile aggiungere un commento"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/friends/{userId}/{numPage}/{numOfPage}")
    public ResponseEntity<List<Post>> postDiAmici(@PathVariable Long userId, @PathVariable int numPage, @PathVariable int numOfPage){
        try {
            //return postService.getPostDegliAmici(userId, numPage, numOfPage);
            return new ResponseEntity(postService.getPostDegliAmici(userId, numPage-1, numOfPage), HttpStatus.OK);
        } catch (UtenteNonEsistenteException e) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }
}
