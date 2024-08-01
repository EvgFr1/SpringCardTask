package ru.cards.SpringCard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "Cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public enum ProductType {
        PREMIUM, REGULAR, SALARY, CHILD
    }

    public enum PaymentSystem {
        MASTERCARD, VISA, MIR
    }

    public enum Status{
        CREATED, IN_DELIVERY, RECEIVED
    }

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    private PaymentSystem paymentSystem;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    private String panNumber;
    @Transient
    //@JsonProperty("maskPanNumber")
    private String maskPanNumber;

    @ManyToOne
    private BankBranch currentLocation;
    @ManyToOne
    private BankBranch endPoint;
    @ManyToOne
    private Owner owner;

    public String getMaskPanNumber(){
        if (panNumber == null)
            return "****************";
       return panNumber.substring(0,6) + "******" + panNumber.substring(panNumber.length()-4);
   }

}
