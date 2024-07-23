package ru.cards.SpringCard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
        CREATED, DELIVERED, RECEIVED
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
    private Owner owner;

    public String getMaskPanNumber(){
        if (maskPanNumber == null)
            return "****************";
       return maskPanNumber.substring(0,6) + "******" + maskPanNumber.substring(maskPanNumber.length()-4);
   }

}
