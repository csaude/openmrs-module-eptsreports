select diagnosticTest.patient_id from
(
Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=6 and o.concept_id in(23723,23774,23951) and o.value_coded=703
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
union
Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=13 and o.concept_id in(307,23723,23774,23951) and o.value_coded=703
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
union
Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=13 and o.concept_id=165189 and o.value_coded=1065
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
union
Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=51 and o.concept_id=23951 and o.value_coded=703
      and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate
)diagnosticTest
