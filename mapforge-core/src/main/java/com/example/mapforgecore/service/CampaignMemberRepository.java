package com.example.mapforgecore.service;

import com.example.mapforgecore.model.entity.CampaignMember;
import com.example.mapforgecore.model.entity.CampaignMemberPK;
import org.springframework.data.repository.Repository;

interface CampaignMemberRepository extends Repository<CampaignMember, CampaignMemberPK> {
}
