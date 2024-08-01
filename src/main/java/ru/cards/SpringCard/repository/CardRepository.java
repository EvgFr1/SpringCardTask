package ru.cards.SpringCard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cards.SpringCard.model.Card;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    //public Card findByCardId(Long cardId);
    public Card findByPanNumber(String panNumber);
    public List<Card> findByOwnerId(Long ownerId);
    //List<Card> findByBankBranchId(Long bankBranchId);
}
