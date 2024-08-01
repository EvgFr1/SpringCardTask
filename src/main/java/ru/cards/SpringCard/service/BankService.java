package ru.cards.SpringCard.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.cards.SpringCard.dto.CardStatusAndHistoryDTO;
import ru.cards.SpringCard.model.*;
import ru.cards.SpringCard.repository.*;

import java.util.List;
import java.util.Random;


@Service
@AllArgsConstructor
public class BankService {

    private final UserRepository userRepository;
    private CardRepository cardRepository;
    private BankBranchRepository bankBranchRepository;
    private OwnerRepository ownerRepository;
    private CardMovementRepository cardMovementRepository;

    private String generateCardNumber(Card.PaymentSystem paymentSystem) {
        String bin = "";
        String customerIdentifier = "";
        Random random = new Random();

        switch (paymentSystem) {
            case MIR:
                bin = "220" + (random.nextInt(5)) + String.format("%02d", random.nextInt(100));
                break;
            case MASTERCARD:
                bin = "5" + (1 + random.nextInt(5)) + String.format("%04d", random.nextInt(10000));
                break;
            case VISA:
                bin = "4" + String.format("%05d", random.nextInt(100000));
                break;
            default:
                throw new IllegalArgumentException("Платежная система не найдена");
        }

        customerIdentifier = String.format("%010d", Math.abs(random.nextLong() % 10000000000L));
        String cardNumber = bin + customerIdentifier;
        return cardNumber;
    }



    public Owner registerOwner(Owner owner, User user) {
//        if (user != null){
//            user.setOwner(owner);
//            userRepository.save(user);
//        }
        return ownerRepository.save(owner);
    }

    public BankBranch createBranch(BankBranch bankBranch){
        return bankBranchRepository.save(bankBranch);
    }

    public Card createCard(Card card, Long ownerId, Long bankBranchId, Long endPointId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Клиент не найден"));
        BankBranch bankBranch = bankBranchRepository.findById(bankBranchId)
                .orElseThrow(() -> new IllegalArgumentException("Банковское отделение не найдено"));
        BankBranch endPoint = bankBranchRepository.findById(endPointId)
                .orElseThrow(() -> new IllegalArgumentException("Банковское отделение не найдено"));

        if (!bankBranch.isMainBranch()) {
            throw new IllegalArgumentException("Карта может быть создана только в главном отделении");
        }

        card.setOwner(owner);
        card.setCurrentLocation(bankBranch);
        card.setEndPoint(endPoint);
        String panNumber = generateCardNumber(card.getPaymentSystem());
        card.setPanNumber(panNumber);
        card.setMaskPanNumber(panNumber);
        card.setStatus(Card.Status.CREATED);
        return cardRepository.save(card);
    }


    public BankBranch moveCard(Long cardId, Long toBranchId){
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Карта не найдена"));
        BankBranch fromBranch = card.getCurrentLocation();
        BankBranch toBranch = bankBranchRepository.findById(toBranchId).orElseThrow(() -> new IllegalArgumentException("Банковское отделение не найдено"));

        if(fromBranch.equals(toBranch)){
            throw new IllegalArgumentException("Нельзя отправить карту в отделение банка в котором она уже находится");
        }

        card.setCurrentLocation(toBranch);
        card.setStatus(Card.Status.IN_DELIVERY);
        cardRepository.save(card);

        CardMovement cardMovement = new CardMovement();
        cardMovement.setCard(card);
        cardMovement.setFromLocation(fromBranch);
        cardMovement.setToLocation(toBranch);
        cardMovementRepository.save(cardMovement);
        return card.getCurrentLocation();
    }

    public Card receiveCard(Long cardId){

        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Карта не найдена"));
        BankBranch bankBranch = card.getCurrentLocation();

        if (bankBranch != null && bankBranch.isMainBranch()) {
            throw new IllegalArgumentException("Карта находится в главном отделении. Оформите доставку в дочернее отделение.");
        }

        card.setCurrentLocation(null);
        card.setStatus(Card.Status.RECEIVED);
        return cardRepository.save(card);
    }

    public List<CardMovement> getCardHistory(String panNumber) {
        List<CardMovement> movements = cardMovementRepository.findByCardPanNumber(panNumber);
        if (movements.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return movements;
    }

    public CardStatusAndHistoryDTO getCardStatusAndHistory(String panNumber) {
        Card card = cardRepository.findByPanNumber(panNumber);

        List<CardMovement> movements = cardMovementRepository.findByCardPanNumber(panNumber);

        return new CardStatusAndHistoryDTO(card.getStatus(), movements);
    }

    public List<Card> getCardsByUser(User user) {
        if (user != null && user.getOwner() != null) {
            Long ownerId = user.getOwner().getId();
            return cardRepository.findByOwnerId(ownerId);
        }
        return List.of();
    }

//    public List<CardMovement> getCardHistory(Long cardId) {
//        List<CardMovement> movements = cardMovementRepository.findByCardId(cardId);
//        if (movements.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//        }
//        return movements;
//    }
//
//    public CardStatusAndHistoryDTO getCardStatusAndHistory(Long cardId) {
//        Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//
//        List<CardMovement> movements = cardMovementRepository.findByCardId(cardId);
//
//        return new CardStatusAndHistoryDTO(card.getStatus(), movements);
//    }
}
