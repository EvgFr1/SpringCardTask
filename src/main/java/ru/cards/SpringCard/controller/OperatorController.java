//package ru.cards.SpringCard.controller;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import ru.cards.SpringCard.model.BankBranch;
//import ru.cards.SpringCard.model.Card;
//import ru.cards.SpringCard.model.User;
//import ru.cards.SpringCard.repository.BankBranchRepository;
//import ru.cards.SpringCard.repository.CardRepository;
//import ru.cards.SpringCard.repository.UserRepository;
//import ru.cards.SpringCard.service.BankService;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/operator")
//@Data
//@AllArgsConstructor
//public class OperatorController {
//
//    private final CardRepository cardRepository;
//    private BankService bankService; // Сервис для работы с картами
//
//    private UserRepository userRepository;
//
//    private BankBranchRepository bankBranchRepository;
//
//    @GetMapping("/dashboard")
//    public String getOperatorDashboard(@AuthenticationPrincipal User user, Model model) {
//        // Получить текущее отделение банка оператора
//        BankBranch currentBranch = user.getBankBranch();
//
//        // Получить карты, которые находятся в текущем отделении
//        List<Card> cards = cardRepository.findByBankBranchId(currentBranch.getId());
//        model.addAttribute("username", user.getUsername());
//        model.addAttribute("currentBranch", currentBranch);
//        model.addAttribute("cards", cards);
//
//        return "operator_dashboard";
//    }
//}
