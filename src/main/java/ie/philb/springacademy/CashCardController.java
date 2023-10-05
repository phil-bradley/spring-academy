/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ie.philb.springacademy;

import java.net.URI;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Phil
 */
@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CardCardRepository cardCardRepository;

    public CashCardController(CardCardRepository cardCardRepository) {
        this.cardCardRepository = cardCardRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashCard> findById(@PathVariable Long id) {

        Optional<CashCard> result = cardCardRepository.findById(id);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result.get());
    }

    @PostMapping
    public ResponseEntity<Void> createCashCard(@RequestBody CashCard newCard, UriComponentsBuilder ucb) {
        CashCard saved = cardCardRepository.save(newCard);
        URI locationOfNewCard = ucb.path("cashcards/{id}").buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(locationOfNewCard).build();
    }
}