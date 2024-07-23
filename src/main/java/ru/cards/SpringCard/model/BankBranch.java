package ru.cards.SpringCard.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "BankBranchList")
public class BankBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private boolean mainBranch;
    private String name;
    private String bankAddress;



}
