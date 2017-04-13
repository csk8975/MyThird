package com.detection.model.owner;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OwnerUnitRepository extends JpaRepository<CrOwnerUnit, String> {
    public List<CrOwnerUnit> findByOwnerName(String ownerName);

    public List<CrOwnerUnit> findByEmail(String email);
    
    public List<CrOwnerUnit> findByToken(String token);
    
    public List<CrOwnerUnit> findByDutyPerson(String dutyPerson);
    
    @Query(value="SELECT * FROM cr_owner_unit t WHERE t.duty_tel = ?1 and t.owner_name like %?2%", nativeQuery = true)
    public CrOwnerUnit findByDutyTelAndOwnerNameLike(String dutyTel,String ownerName);
    
}
