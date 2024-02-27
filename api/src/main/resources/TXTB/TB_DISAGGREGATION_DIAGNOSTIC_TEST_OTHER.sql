select diagnosticTest.patient_id from
(
Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23774,23951)
      and e.location_id= :location  and e.encounter_datetime   between  :startDate and :endDate
union
Select  p.patient_id,e.encounter_datetime data_inicio                                                      
from    patient p                                                                                                   
      inner join encounter e on p.patient_id=e.patient_id                                                         
      inner join obs o on o.encounter_id=e.encounter_id
where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
      e.encounter_type in(6,13) and o.concept_id in(23774,23951) and o.value_coded in(703,664)
      and e.location_id= :location  and e.encounter_datetime   between  :startDate and :endDate
)diagnosticTest
