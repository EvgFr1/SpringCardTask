package ru.cards.SpringCard.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cards.SpringCard.dto.CardStatusAndHistoryDTO;
import ru.cards.SpringCard.model.BankBranch;
import ru.cards.SpringCard.model.Card;
import ru.cards.SpringCard.model.CardMovement;
import ru.cards.SpringCard.model.Owner;
import ru.cards.SpringCard.repository.BankBranchRepository;
import ru.cards.SpringCard.repository.CardMovementRepository;
import ru.cards.SpringCard.repository.CardRepository;
import ru.cards.SpringCard.repository.OwnerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class WebController {

    private final RestTemplate restTemplate;
    private BankBranchRepository bankBranchRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register_owner_form")
    public String showRegisterOwnerPage(Model model) {
        model.addAttribute("owner", new Owner());
        return "register_owner_form";
    }

    @PostMapping("/submit_owner")
    public String registerOwner(@ModelAttribute Owner owner, HttpSession session, Model model) {
        String apiUrl = "http://localhost:8080/api/bank/register_owner";

        Owner savedOwner = restTemplate.postForObject(apiUrl, owner, Owner.class);
        session.setAttribute("ownerId", savedOwner.getId());
        return "redirect:/select_product";
    }


    @GetMapping("/select_product")
    public String showSelectProductPage(Model model) {
        List<BankBranch> allBranches = bankBranchRepository.findAll();
        List<BankBranch> mainBranches = allBranches.stream()
                .filter(BankBranch::isMainBranch)
                .collect(Collectors.toList());
        model.addAttribute("mainBranches", mainBranches);
        return "select_product";
    }


    @PostMapping("/create_card")
    public String createCard(@RequestParam("productType") Card.ProductType productType,
                             @RequestParam("paymentSystem") Card.PaymentSystem paymentSystem,
                             @RequestParam("bankBranch") Long bankBranchId,
                             HttpSession session) {
        Long ownerId = (Long) session.getAttribute("ownerId");
        Card card = new Card();
        card.setProductType(productType);
        card.setPaymentSystem(paymentSystem);

        String apiUrl = "http://localhost:8080/api/bank/cards/create?ownerId=" + ownerId + "&bankBranchId=" + bankBranchId;
        Card createdCard = restTemplate.postForObject(apiUrl, card, Card.class);
        session.setAttribute("createdCard", createdCard);
        return "redirect:/move_card_form";

    }


    @GetMapping("/move_card_form")
    public String showMoveCardPage(HttpSession session, Model model) {
        Card createdCard = (Card) session.getAttribute("createdCard");
        if (createdCard == null) {
            return "redirect:/select_product";
        }
        List<BankBranch> allBranches = bankBranchRepository.findAll();
        model.addAttribute("allBranches", allBranches);
        model.addAttribute("createdCard", createdCard);
        return "move_card_form";
    }

    @PutMapping("/move_card")
    public String moveCard(@RequestParam Long cardId, @RequestParam Long toBranchId, RedirectAttributes redirectAttributes, HttpSession session) {
        String apiUrl = "http://localhost:8080/api/bank/cards/move?cardId=" + cardId + "&toBranchId=" + toBranchId;
        BankBranch newLocation = restTemplate.exchange(apiUrl, HttpMethod.PUT, null, BankBranch.class).getBody();

        redirectAttributes.addFlashAttribute("successMessage", "Карта успешно перемещена в " + newLocation.getName());
        redirectAttributes.addFlashAttribute("newLocation", newLocation.getName());

        Card createdCard = (Card) session.getAttribute("createdCard");
        createdCard.setCurrentLocation(newLocation);
        session.setAttribute("createdCard", createdCard);
        return "redirect:/move_card_form";
    }


    @PutMapping("/receive_card")
    public String receiveCard(@RequestParam Long cardId, RedirectAttributes redirectAttributes, HttpSession session) {
        String apiUrl = "http://localhost:8080/api/bank/cards/receive?cardId=" + cardId;
        Card updatedCard = restTemplate.exchange(apiUrl, HttpMethod.PUT, null, Card.class).getBody();

        redirectAttributes.addFlashAttribute("successMessage", "Карта успешно получена.");
        Card createdCard = (Card) session.getAttribute("createdCard");

        createdCard.setStatus(updatedCard.getStatus());
        createdCard.setCurrentLocation(updatedCard.getCurrentLocation());
        session.setAttribute("createdCard", createdCard);
        return "redirect:/move_card_form";
    }

    @GetMapping("/card_history_form")
    public String showCardHistoryPage(Model model){
        return  "card_history_form";
    }

    @GetMapping("/view_card_history")
    public String viewCardHistory(@RequestParam String cardNumber, Model model) {
        String apiUrl = "http://localhost:8080/api/bank/cards/" + cardNumber + "/history";
        List<CardMovement> movements = restTemplate.getForObject(apiUrl, List.class);
        model.addAttribute("cardMovements", movements);
        return "card_history_result";
    }

    @GetMapping("/view_card_status_and_history")
    public String viewCardStatusAndHistory(@RequestParam String cardNumber, Model model) {
        String apiUrl = "http://localhost:8080/api/bank/cards/" + cardNumber + "/status";
        CardStatusAndHistoryDTO response = restTemplate.getForObject(apiUrl, CardStatusAndHistoryDTO.class);
        model.addAttribute("cardStatusAndHistory", response);
        return "card_status_and_history";
    }

}
