package com.detection.model.owner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerUnitRepository extends JpaRepository<CrOwnerUnit, String> {
    public List<CrOwnerUnit> findByOwnerName(String ownerName);

    public List<CrOwnerUnit> findByEmail(String email);
    
    public List<CrOwnerUnit> findByToken(String token);
    
    public List<CrOwnerUnit> findByDutyPerson(String dutyPerson);
    
}
