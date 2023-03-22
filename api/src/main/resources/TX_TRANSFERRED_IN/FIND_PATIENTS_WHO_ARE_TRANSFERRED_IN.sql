select all_transferidos_de.patient_id 
from 
(
			select patient_id, max(data_transferencia)   data_transferencia                                                       
			from
			(                                           
					select distinct max_estado.patient_id, max_estado.data_transferencia                                             
					from 
					(                                                                                            
							select pg.patient_id, max(ps.start_date) data_transferencia                                                      
							from patient p                                                             
									inner join patient_program pg on p.patient_id = pg.patient_id                                     
									inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                              
							where pg.voided=0 and ps.voided=0 and p.voided=0  and pg.program_id = 2                                               
									and ps.start_date>=:startDate and  ps.start_date<=:endDate and pg.location_id =:location 
								group by pg.patient_id                           
					) max_estado                                                                                                                                  
							inner join patient_program pp on pp.patient_id = max_estado.patient_id                                      
							inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_transferencia                 
					where pp.program_id = 2 and ps.state = 29 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location                    
			
					union                                                                                               
					
					select transferido_de.patient_id, transferido_de.data_transferencia                                            
					from  
					(                                                                         
							select p.patient_id, max(obsOpenDate.value_datetime) data_transferencia 
							from patient p                               
									inner join encounter e on p.patient_id=e.patient_id                                           
									inner join obs obsTransIn on e.encounter_id= obsTransIn.encounter_id                                    
									inner join obs obsOpenDate on e.encounter_id= obsOpenDate.encounter_id                                  
									inner join obs obsInTarv on e.encounter_id= obsInTarv.encounter_id                                    
							where e.voided=0 and obsTransIn.voided=0 and p.voided=0  and obsOpenDate.voided =0 and obsInTarv.voided =0                 
									and e.encounter_type=53 and obsTransIn.concept_id=1369 and obsTransIn.value_coded=1065                          
									and obsOpenDate.concept_id=23891 and obsOpenDate.value_datetime is not null                               
									and obsInTarv.concept_id=6300 and obsInTarv.value_coded=6276                                        
									and obsOpenDate.value_datetime >=:startDate and obsOpenDate.value_datetime <=:endDate and e.location_id=:location 
								group by p.patient_id  
					) transferido_de                                                                    
						inner join encounter e on e.patient_id = transferido_de.patient_id                                        
					where e.voided is false and e.encounter_type in (6, 9, 18) and e.encounter_datetime between :startDate and :endDate and e.location_id =:location  
			)                                                                           
			transferidos_de group by patient_id 

			union
			
			select saida.patient_id, saida.data_estado
			from
			( 
					select allSaida.patient_id, allSaida.data_estado                                                                                              
					from 
					( 
							select patient_id,max(data_estado) data_estado                                                                                              
							from                                                                                                                                        
							(  
									select distinct max_estado.patient_id, max_estado.data_estado 
									from 
									(                                                                
											select  pg.patient_id, max(ps.start_date) data_estado                                                                                          
											from  patient p                                                                                                               
													inner join person pe on pe.person_id = p.patient_id                                                                         
													inner join patient_program pg on p.patient_id = pg.patient_id                                                               
													inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
											where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                        
													and ps.start_date<=  date_add(:startDate, interval -1 day) and pg.location_id =:location 
												group by pg.patient_id                                           
									) max_estado                                                                                                                        
											inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
											inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
									where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location                 
									
									union                                                                                                                               
             
									select  p.patient_id, max(o.obs_datetime) data_estado                                                                                             
									from patient p                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                         
											 inner join encounter e on p.patient_id=e.patient_id                                                                         
											 inner join obs  o on e.encounter_id=o.encounter_id                                                                          
									 where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
											and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706                         
											and o.obs_datetime<= date_add(:startDate, interval -1 day) and e.location_id=:location                                                                        
										group by p.patient_id                                                                                                               
									 
									union                                                                                                                               
            
									 select ultimaBusca.patient_id, ultimaBusca.data_estado                                                                              
									 from 
									 (                                                                                                                              
											select p.patient_id,max(e.encounter_datetime) data_estado                                                                   
											from patient p                                                                                                              
													inner join person pe on pe.person_id = p.patient_id                                                                     
													inner join encounter e on p.patient_id=e.patient_id                                                                     
													inner join obs o on o.encounter_id=e.encounter_id                                                                       
											 where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<=  date_add(:startDate, interval -1 day)                                       
													and e.encounter_type = 21 and  e.location_id= :location                                                                 
												group by p.patient_id                                                                                                   
										 ) ultimaBusca                                                                                                                   
												inner join encounter e on e.patient_id = ultimaBusca.patient_id                                                             
												inner join obs o on o.encounter_id = e.encounter_id                                                                         
										where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimaBusca.data_estado = e.encounter_datetime and e.location_id = :location 
							) allSaida                                                                                                                                      
								group by patient_id 
					) allSaida
					left join
					(
						select patient_id,max(max_date) max_date      
						from
						(
								Select p.patient_id,max(encounter_datetime) max_date                                                                                                
								from    patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
								where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
										and e.location_id=:location  and e.encounter_datetime<= date_add(:startDate, interval -1 day)                                                                                  
									group by p.patient_id 

								union

								Select p.patient_id,max(encounter_datetime) max_date                                                                                    
								from  patient p                                                                                                                                   
										inner join person pe on pe.person_id = p.patient_id                                                                                         
										inner join encounter e on e.patient_id=p.patient_id                                                                                         
								where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)                                                                
										and e.location_id=:location  and e.encounter_datetime<= date_add(:startDate, interval -1 day)                                                                                  
									group by p.patient_id 
               
						) last_encounter group  by patient_id
              
					) last_encounter on allSaida.patient_id  = last_encounter.patient_id 
					where allSaida.data_estado >= last_encounter.max_date
			) saida
			left join
			(   
				select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
				from 
				(
						 select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
                        from
                        (
                                select p.patient_id, max(encounter_datetime) data_fila                                                                                               
                                from    patient p                                                                                                                                   
                                    inner join person pe on pe.person_id = p.patient_id                                                                                         
                                    inner join encounter e on e.patient_id=p.patient_id                                                                                         
                                where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
                                    and e.location_id=:location and e.encounter_datetime <= date_add(:startDate, interval -1 day)                                                                                    
                                    group by p.patient_id 
                        ) ultimo_fila  
                        left join                                                                                                                                          
                            obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id                                                                                      
                            and obs_fila.voided=0                                                                                                                             
                            and obs_fila.obs_datetime=ultimo_fila.data_fila                                                                                                
                            and obs_fila.concept_id=5096                                                                                                                       
                            and obs_fila.location_id=:location  
				
					   union
							
						Select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
						from patient p                                                                                                                                   
								inner join person pe on pe.person_id = p.patient_id                                                                                         
								inner join encounter e on p.patient_id=e.patient_id                                                                                         
								inner join obs o on e.encounter_id=o.encounter_id                                                                                           
						where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
								and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= date_add(:startDate, interval -1 day)                                                                                      
							group by p.patient_id
				   ) ultimo_levantamento group by patient_id
			) ultimo_levantamento on saida.patient_id  = ultimo_levantamento.patient_id 
			where ultimo_levantamento.data_ultimo_levantamento is null or  ultimo_levantamento.data_ultimo_levantamento <=  date_add(:startDate, interval -1 day) 
)all_transferidos_de
	inner join
	(
			select e.patient_id 
			from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
			where p.voided=0 and e.encounter_type in (6,9,18) and e.voided=0 
				and e.encounter_datetime>=:startDate and e.encounter_datetime<=:endDate and e.location_id=:location 
            
			union 
    
			select p.patient_id 
			from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and o.concept_id=23866 
					and o.value_datetime is not null and o.value_datetime>=:startDate and o.value_datetime<=:endDate and e.location_id=:location 
  ) consulta_levantamento on consulta_levantamento.patient_id = all_transferidos_de.patient_id
		group by all_transferidos_de.patient_id