package ru.cards.SpringCard.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cards.SpringCard.dto.CardStatusAndHistoryDTO;
import ru.cards.SpringCard.model.*;
import ru.cards.SpringCard.repository.*;
import ru.cards.SpringCard.service.BankService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class WebController {

    private final RestTemplate restTemplate;
    private final BankService bankService;
    private final UserRepository userRepository;
    private BankBranchRepository bankBranchRepository;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Principal principal, Model model) {
        if (user == null) {
            System.out.println("User is null");
        } else {
            System.out.println("Authenticated user: " + user.getUsername());
        }
        System.out.println("Principal: " + (principal != null ? principal.getName() : "null"));
        model.addAttribute("user", user);
        return "index";
    }





    @GetMapping("/register_owner_form")
    public String showRegisterOwnerPage(@AuthenticationPrincipal User user,Model model, HttpSession session) {
        if (user == null) {
            // Если пользователь не аутентифицирован
            throw new RuntimeException("User not authenticated");
        }
        else if (user.getOwner() != null){
            session.setAttribute("ownerId", user.getOwner().getId());
            return "redirect:/select_product";
        }
        model.addAttribute("owner", new Owner());
        return "register_owner_form";
    }

    @PostMapping("/submit_owner")
    public String registerOwner(@AuthenticationPrincipal User user, @ModelAttribute Owner owner, HttpSession session, Model model) {
        if (user == null) {
            // Если пользователь не аутентифицирован
            throw new RuntimeException("User not authenticated");
        }
//        else if (user.getOwner() != null){
//            session.setAttribute("ownerId", user.getOwner().getId());
//            return "redirect:/select_product";
//        }

        String apiUrl = "http://localhost:8080/api/bank/register_owner";

        System.out.println("Authenticated user: " + user.getUsername());
        Owner savedOwner = restTemplate.postForObject(apiUrl, owner, Owner.class);
        user.setOwner(savedOwner);
        userRepository.save(user);

        session.setAttribute("ownerId", savedOwner.getId());
        return "redirect:/select_product";
    }


    @GetMapping("/select_product")
    public String showSelectProductPage(@AuthenticationPrincipal User user, Model model) {
        List<BankBranch> allBranches = bankBranchRepository.findAll();
        List<BankBranch> mainBranches = allBranches.stream()
                .filter(BankBranch::isMainBranch)
                .collect(Collectors.toList());
        List<BankBranch> nonMainBranches = allBranches.stream()
                .filter(branch -> !branch.isMainBranch())
                .collect(Collectors.toList());

        model.addAttribute("mainBranches", mainBranches);
        model.addAttribute("nonMainBranches", nonMainBranches);
        return "select_product";
    }


    @PostMapping("/create_card")
    public String createCard(@RequestParam("productType") Card.ProductType productType,
                             @RequestParam("paymentSystem") Card.PaymentSystem paymentSystem,
                             @RequestParam("bankBranch") Long bankBranchId,
                             @RequestParam("endPoint") Long endPointId,
                             HttpSession session) {
        Long ownerId = (Long) session.getAttribute("ownerId");
        Card card = new Card();
        card.setProductType(productType);
        card.setPaymentSystem(paymentSystem);

        try{
            String apiUrl = "http://localhost:8080/api/bank/cards/create?ownerId=" + ownerId + "&bankBranchId=" + bankBranchId + "&endPointId=" + endPointId;
            Card createdCard = restTemplate.postForObject(apiUrl, card, Card.class);
            session.setAttribute("createdCard", createdCard);
            return "redirect:/create_card_message?status=success";
        }
        catch (Exception exception){
            return "redirect:/create_card_message?status=error";
        }
    }

    @GetMapping("/create_card_message")
    public String showCreateCardMessage(@RequestParam("status") String status, Model model) {
        model.addAttribute("status", status);
        return "create_card_message";
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

    @GetMapping("/myCards")
    public String getCards(@AuthenticationPrincipal User user, Model model) {
        List<Card> cards = bankService.getCardsByUser(user);
        model.addAttribute("cards", cards);
        List<BankBranch> allBranches = bankBranchRepository.findAll();
        model.addAttribute("allBranches", allBranches);
        return "myCards";
    }

    @PutMapping("/receive_card_from_myCards")
    public String receiveCardFromMyCards(@RequestParam Long cardId) {
        String apiUrl = "http://localhost:8080/api/bank/cards/receive?cardId=" + cardId;
        restTemplate.exchange(apiUrl, HttpMethod.PUT, null, Card.class);
        return "redirect:/myCards";
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
