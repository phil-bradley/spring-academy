/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ie.philb.springacademy;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<CashCard> findById(@PathVariable Long id, Principal principal) {

        CashCard cashCard = findCashCard(id, principal);

        if (cashCard == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cashCard);
    }

    @PostMapping
    public ResponseEntity<Void> createCashCard(@RequestBody CashCard newCard, UriComponentsBuilder ucb, Principal principal) {

        CashCard cardWithOwner = new CashCard(null, newCard.amount(), principal.getName());
        CashCard saved = cardCardRepository.save(cardWithOwner);
        URI locationOfNewCard = ucb.path("cashcards/{id}").buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(locationOfNewCard).build();
    }

    @GetMapping
    public ResponseEntity<List<CashCard>> findAll(Pageable pageable, Principal principal) {

        String owner = principal.getName();

        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
        );

        Page<CashCard> page = cardCardRepository.findByOwner(owner, pageRequest);
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCashCard(@PathVariable Long id, @RequestBody CashCard cardUpdate, Principal principal) {

        CashCard existing = findCashCard(id, principal);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        CashCard updated = new CashCard(existing.id(), cardUpdate.amount(), existing.owner());
        cardCardRepository.save(updated);

        return ResponseEntity.noContent().build();
    }

    private CashCard findCashCard(Long id, Principal principal) {
        return cardCardRepository.findByIdAndOwner(id, principal.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCashCard(@PathVariable Long id, Principal principal) {

        if (!cardCardRepository.existsByIdAndOwner(id, principal.getName())) {
            return ResponseEntity.notFound().build();
        }

        cardCardRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
