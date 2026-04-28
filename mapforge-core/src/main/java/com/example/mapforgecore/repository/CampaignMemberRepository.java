package com.example.mapforgecore.repository;

import com.example.mapforgecore.model.entity.CampaignMember;
import com.example.mapforgecore.model.entity.CampaignMemberPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CampaignMemberRepository extends JpaRepository<CampaignMember, CampaignMemberPK> {
    @Query("""
         SELECT COUNT(cm) > 0
         FROM CampaignMember cm
         WHERE cm.owner.id = :ownerId
         AND cm.campaign.id = :campaignId
    """)
    boolean existsByIdOwnerIdAndIdCampaignId(Integer ownerId, UUID campaignId);
    Optional<CampaignMember> findByIdOwnerIdAndIdCampaignId(Integer ownerId, UUID campaignId);
}
