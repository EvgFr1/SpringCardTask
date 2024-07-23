package ru.cards.SpringCard.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.cards.SpringCard.dto.CardStatusAndHistoryDTO;
import ru.cards.SpringCard.model.BankBranch;
import ru.cards.SpringCard.model.Card;
import ru.cards.SpringCard.model.CardMovement;
import ru.cards.SpringCard.model.Owner;
import ru.cards.SpringCard.service.BankService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/bank")
public class BankController {

    private final BankService bankService;

    @PostMapping("/register_owner")
    public Owner registerOwner(@RequestBody Owner owner){
        return bankService.registerOwner(owner);
    }

    @PostMapping("/create_branch")
    public BankBranch createBranch(@RequestBody BankBranch bankBranch){
        return bankService.createBranch(bankBranch);
    }

    @PostMapping("/cards/create")
    public Card createCard(@RequestBody Card card, @RequestParam Long ownerId, @RequestParam Long bankBranchId){
        return bankService.createCard(card, ownerId, bankBranchId);
    }

    @PutMapping("/cards/move")
    public BankBranch moveCard(@RequestParam Long cardId, @RequestParam Long toBranchId){
        return bankService.moveCard(cardId,toBranchId);
    }

    @PutMapping("/cards/receive")
    public Card receiveCard(@RequestParam Long cardId){
        return bankService.receiveCard(cardId);

    }

    @GetMapping("/cards/{panNumber}/history")
    public List<CardMovement> getCardHistory(@PathVariable String panNumber) {
        return bankService.getCardHistory(panNumber);
    }
    @GetMapping("/cards/{panNumber}/status")
    public CardStatusAndHistoryDTO getCardStatus(@PathVariable String panNumber){
        return bankService.getCardStatusAndHistory(panNumber);
    }
}
