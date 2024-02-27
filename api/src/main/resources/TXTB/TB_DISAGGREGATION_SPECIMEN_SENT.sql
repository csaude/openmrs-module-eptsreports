select specimenSent.patient_id from
(
Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307)   
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
union

Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=6 and o.concept_id=23723 and o.value_coded in(703,664)
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
union

Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=6 and o.concept_id=23774 and o.value_coded in(703,664)
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
union

Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=6 and o.concept_id=23951 and o.value_coded in(703,664)
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
union

Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=6 and o.concept_id=307 and o.value_coded in(703,664)
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
 union
 Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=13 and o.concept_id=307 and o.value_coded in(703,165184)
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
      
 union
 
  Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=13 and o.concept_id in(23723,23774,23951) and o.value_coded in(703,664)
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
      
union

  Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=13 and o.concept_id in(165189) and o.value_coded in(1065,1066)
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
      
union

  Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=51 and o.concept_id in(23951) and o.value_coded in(703,664)
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
)specimenSent
  