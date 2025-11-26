package com.example.mapforgecore.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class CampaignMemberPK implements Serializable {
    private static final long serialVersionUID = -8205427271119416028L;
    
    @Column(name = "owner", nullable = false)
    private Integer ownerId;

    @Column(name = "campaign", nullable = false)
    private UUID campaignId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CampaignMemberPK entity = (CampaignMemberPK) o;
        return Objects.equals(this.ownerId, entity.ownerId) &&
                Objects.equals(this.campaignId, entity.campaignId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerId, campaignId);
    }

}