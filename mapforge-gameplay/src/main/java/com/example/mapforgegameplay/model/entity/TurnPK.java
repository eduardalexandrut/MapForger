package com.example.mapforgegameplay.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TurnPK implements Serializable {

    @Column(name = "index")
    private Integer index;

    @Column(name = "campaign_id")
    private UUID campaignId;
}