select patient_id 
from(
			select  p.patient_id,o.value_datetime data_inicio                                                           
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id                                                           
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
				  e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id= :location 
				 and  o.value_datetime  between :startDate and :endDate
			
			union
			
			select  p.patient_id,e.encounter_datetime data_inicio                                                           
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id                                                           
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
				  e.encounter_type in (6,9) and o.concept_id=6257 
				  and o.value_coded in(1065,1066)  and e.location_id= :location 
				  and  e.encounter_datetime  between :startDate and :endDate
			
			union
			
			select  p.patient_id,e.encounter_datetime data_inicio                                                           
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id                                                           
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
				  e.encounter_type in (6,9) and o.concept_id=6277 
				  and o.value_coded = 703  and e.location_id= :location 
				  and  e.encounter_datetime  between :startDate and :endDate
			union
			
			select  p.patient_id,e.encounter_datetime data_inicio                                                           
			from patient p                                                                                                   
				inner join encounter e on p.patient_id=e.patient_id                                                         
				inner join obs obs6277 on obs6277.encounter_id= e.encounter_id 
				inner join obs obs6257 on obs6257.encounter_id= e.encounter_id                                                           
			where p.voided = 0 and e.voided = 0 and obs6277.voided = 0 and obs6257.voided = 0                                                                  
				and e.encounter_type in (6,9) and obs6277.concept_id=6277 and obs6277.value_coded = 664 and e.location_id= :location and obs6257.concept_id= 6257 and obs6257.value_coded in(1065,1066)
				and e.encounter_datetime  between :startDate and :endDate
	
			union

			select  p.patient_id,o.obs_datetime data_inicio                                                           
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id                                                           
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
				  e.encounter_type=53 and o.concept_id=1406 
				  and o.value_coded=42  and e.location_id= :location 
				  and  o.obs_datetime  between :startDate and :endDate
			union
			
			select  pg.patient_id,date_enrolled data_inicio                                                             
			from    patient p 
					inner join patient_program pg on p.patient_id=pg.patient_id                                       
			where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location  
					and date_enrolled  between :startDate and :endDate
			union

			select  p.patient_id,e.encounter_datetime data_inicio                                                           
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id                                                           
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
				  e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location  
				  and e.encounter_datetime between :startDate and :endDate
			union
			
			select  p.patient_id,e.encounter_datetime data_inicio                                                           
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id                                                           
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
				  e.encounter_type=6 and o.concept_id=23758 and o.value_coded in(1065,1066)  and e.location_id= :location  
				  and e.encounter_datetime between :startDate and :endDate
				  
			union
			
			select  p.patient_id,e.encounter_datetime data_inicio                                                           
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id                                                           
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
				  e.encounter_type=6 and o.concept_id=23761 and o.value_coded=1065  and e.location_id= :location  
				  and e.encounter_datetime between :startDate and :endDate
				  
			union
			
			select  p.patient_id,e.encounter_datetime data_inicio                                                           
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
				  e.encounter_type=6 and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,23760,161)   
				  and e.location_id= :location  and e.encounter_datetime  between :startDate and :endDate
				  
			union
			select  p.patient_id,e.encounter_datetime data_inicio                                                      
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
				  e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   
				  and e.location_id= :location and e.encounter_datetime between :startDate and :endDate
		
		union

			select  p.patient_id,e.encounter_datetime data_inicio                                                    
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
				  e.encounter_type=6 and o.concept_id in(23723, 23774, 23951, 307, 12) and o.value_coded is not null
				  and e.location_id= :location and e.encounter_datetime between :startDate and :endDate
			union

			select  p.patient_id,e.encounter_datetime data_inicio                                                    
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
				  e.encounter_type=13 and o.concept_id in(307, 23723, 23774, 23951, 165588) and o.value_coded is not null
				  and e.location_id= :location and e.encounter_datetime between :startDate and :endDate

			union

			select  p.patient_id,e.encounter_datetime data_inicio                                                    
			from    patient p                                                                                                   
				  inner join encounter e on p.patient_id=e.patient_id                                                         
				  inner join obs o on o.encounter_id=e.encounter_id
			where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
				  e.encounter_type=51 and o.concept_id in(23951) and o.value_coded is not null
				  and e.location_id= :location and e.encounter_datetime between :startDate and :endDate
)ScreenedForTB
