package com.alessandro_molinaro.social_network.controller;

import com.alessandro_molinaro.social_network.entity.Commento;
import com.alessandro_molinaro.social_network.entity.Like;
import com.alessandro_molinaro.social_network.entity.Post;
import com.alessandro_molinaro.social_network.service.PostService;
import com.alessandro_molinaro.social_network.support.exception.*;
import com.alessandro_molinaro.social_network.support.messageForClient.Messaggio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/post")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

  @Autowired PostService postService;

  @PostMapping(value = "/add/{userId}")
  public ResponseEntity<Post> addPost(@RequestBody Post post, @PathVariable Long userId) {
    try {
      Post p = postService.aggiungiPost(userId, post);
      return new ResponseEntity(p, HttpStatus.CREATED);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    }
  }

  @DeleteMapping(value = "/delete/{userId}/{postId}") // userid deve essere il proprietario del post
  public ResponseEntity delPost(@PathVariable Long postId, @PathVariable Long userId) {
    try {
      postService.removePost(postId, userId);
      return new ResponseEntity(new Messaggio("post eliminato correttamente"), HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("utente non esistente"), HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    } catch (PostNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("post non esistente"), HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "il post non esiste", e);
    }
  }

  @PostMapping(value = "{postId}/add_like_from/{userId}")
  public ResponseEntity addLike(@PathVariable Long postId, @PathVariable Long userId) {
    try {
      Like l = postService.aggiungiLike(userId, postId);
      return new ResponseEntity(l, HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("utente non esistente"), HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    } catch (PostNonEsistenteException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "il post non esiste", e);
      // return new ResponseEntity(new Messaggio("post non esistente"), HttpStatus.BAD_REQUEST);
    } catch (LikePresenteException e) {
      // return new ResponseEntity(new Messaggio("like già registrato"), HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "like già registrato", e);
    }
  }

  @PostMapping(value = "{postId}/add_comment_from/{userId}")
  public ResponseEntity addCommento(
      @PathVariable Long postId, @PathVariable Long userId, @RequestBody Commento commento) {
    try {
      Commento c = postService.aggiungiCommento(userId, postId, commento);
      return new ResponseEntity(c, HttpStatus.CREATED);
    } catch (UtenteNonEsistenteException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
      // return new ResponseEntity(new Messaggio("utente non esistente"), HttpStatus.BAD_REQUEST);
    } catch (PostNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("post non esistente"), HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "il post non esiste", e);
    } catch (NonAmiciException e) {
      // return new ResponseEntity(new Messaggio("non è possibile aggiungere un commento"),
      // HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "NON SIETE AMICI, non è possibile aggiungere un commento", e);
    }
  }

  @DeleteMapping(
      value =
          "/commento/delete/{userId}/{commentoId}") // userid deve essere il proprietario del
                                                    // commento
  public ResponseEntity delCommento(@PathVariable Long commentoId, @PathVariable Long userId) {
    try {
      postService.removeCommento(commentoId, userId);
      return new ResponseEntity(new Messaggio("commento eliminato correttamente"), HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("utente non esistente"), HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    } catch (CommentoNonEsistenteException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "il commento non esiste", e);
    }
  }

  @GetMapping(value = "/friends/{userId}/{numPage}/{numInPage}")
  public ResponseEntity<List<Post>> postDiAmici(
      @PathVariable Long userId, @PathVariable int numPage, @PathVariable int numInPage) {
    try {
      // return postService.getPostDegliAmici(userId, numPage, numOfPage);
      return new ResponseEntity(
          postService.getPostDegliAmici(userId, numPage - 1, numInPage), HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
      // return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/{userId}/page_post_list/{numPag}/{numInPage}")
  public ResponseEntity postListUtente(
      @PathVariable Long userId, @PathVariable int numPag, @PathVariable int numInPage) {
    try {
      List<Post> post = postService.getPostPage(userId, numPag - 1, numInPage);
      return new ResponseEntity(post, HttpStatus.OK);
    } catch (UtenteNonEsistenteException e) {
      // return new ResponseEntity(new Messaggio("l'utente non esiste"),HttpStatus.BAD_REQUEST);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "l'utente non esiste", e);
    }
  }
}
