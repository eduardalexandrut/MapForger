package com.example.mapforgecore.model.entity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class TurnPK implements Serializable {
    private static final long serialVersionUID = -8205427271119416028L;

    @Column(name = "index")
    private int index;

    @Column(name = "campaignId")
    private UUID campaignId;
}