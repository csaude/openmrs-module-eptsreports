select coorte_final.patient_id
from (
		select coorte_final.*, datediff(data_restart,data_iit) iit_art_interval
		from (
				select inicio_fila_recepcao_prox.*,
					GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30), COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_iit,
					LEAST(COALESCE(data_fila_restart,data_recepcao_levantou_restart), COALESCE(data_recepcao_levantou_restart,data_fila_restart)) data_restart 
				from(
						select inicio_fila_recepcao.*,
						 	max(obs_fila.value_datetime) data_proximo_lev, 
						     date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30    
						from(
								select inicio.*,
									 saida.data_estado,
									 max_fila.data_fila,
									 max_recepcao.data_recepcao_levantou,
								     max_fila_restart.data_fila_restart,
								     max_recepcao_restart.data_recepcao_levantou_restart   
								from (
										select patient_id, art_start_date from 
										(
										      select patient_id, min(art_start_date) art_start_date 
										      from 
										            (
										                  select p.patient_id, min(e.encounter_datetime) art_start_date 
										                  from patient p 
										                        inner join encounter e on p.patient_id=e.patient_id 
										                        inner join obs o on o.encounter_id=e.encounter_id 
										                  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
										                        and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime< :startDate and e.location_id=:location 
										                        group by p.patient_id 
										                  union 
										                  
										                  select p.patient_id, min(value_datetime) art_start_date 
										                  from patient p 
										                        inner join encounter e on p.patient_id=e.patient_id 
										                        inner join obs o on e.encounter_id=o.encounter_id 
										                  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
										                        and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime< :startDate and e.location_id=:location 
										                        group by p.patient_id 
										                  
										                  union 
										       
										                  select pg.patient_id, min(date_enrolled) art_start_date 
										                  from patient p 
										                        inner join patient_program pg on p.patient_id=pg.patient_id 
										                  where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled< :startDate and location_id=:location 
										                        group by pg.patient_id 
										                  
										                  union 
										
										                  select e.patient_id, min(e.encounter_datetime) as art_start_date 
										                  from patient p 
										                        inner join encounter e on p.patient_id=e.patient_id 
										                  where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime< :startDate and e.location_id=:location 
										                        group by p.patient_id 
										                  
										                  union 
										       
										                  select p.patient_id, min(value_datetime) art_start_date 
										                  from patient p 
										                        inner join encounter e on p.patient_id=e.patient_id 
										                        inner join obs o on e.encounter_id=o.encounter_id 
										                  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
										                        and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime< :startDate and e.location_id=:location 
										                        group by p.patient_id
										            ) 
										      art_start group by patient_id 
										) tx_new where art_start_date <:startDate and art_start_date < '2023-12-21'
										union
										select tx_new.patient_id, tx_new.art_start_date 
										from 
										(
										      select tx_new.patient_id, tx_new.art_start_date 
										      from
										      ( 
										            select patient_id, art_start_date from 
										            (
										                  select patient_id, min(art_start_date) art_start_date 
										                  from 
										                        (
										                              select e.patient_id, min(e.encounter_datetime) as art_start_date 
										                              from patient p 
										                                    inner join encounter e on p.patient_id=e.patient_id 
										                              where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime< :startDate and e.location_id=:location 
										                                    group by p.patient_id 
										                              
										                              union 
										                   
										                              select p.patient_id, min(value_datetime) art_start_date 
										                              from patient p 
										                                    inner join encounter e on p.patient_id=e.patient_id 
										                                    inner join obs o on e.encounter_id=o.encounter_id 
										                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
										                                    and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime< :startDate and e.location_id=:location 
										                                    group by p.patient_id
										                        ) 
										                  art_start group by patient_id 
										            ) tx_new where art_start_date < :startDate  and art_start_date >= '2023-12-21'
										      ) tx_new
										      left join
										      (
										            select patient_id from 
										            (
										                  select patient_id, min(art_start_date) art_start_date 
										                  from 
										                        (
										                              select p.patient_id, min(e.encounter_datetime) art_start_date 
										                              from patient p 
										                                    inner join encounter e on p.patient_id=e.patient_id 
										                                    inner join obs o on o.encounter_id=e.encounter_id 
										                              where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
										                                    and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime< :startDate and e.location_id=:location 
										                                    group by p.patient_id 
										                              union 
										                              
										                              select p.patient_id, min(value_datetime) art_start_date 
										                              from patient p 
										                                    inner join encounter e on p.patient_id=e.patient_id 
										                                    inner join obs o on e.encounter_id=o.encounter_id 
										                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
										                                    and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime < :startDate and e.location_id=:location 
										                                    group by p.patient_id 
										                              
										                              union 
										                   
										                              select pg.patient_id, min(date_enrolled) art_start_date 
										                              from patient p 
										                                    inner join patient_program pg on p.patient_id=pg.patient_id 
										                              where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled< :startDate and location_id=:location 
										                                    group by pg.patient_id 
										                        ) 
										                  art_start group by patient_id 
										            ) tx_new where art_start_date < '2023-12-21'
										      ) tx_new_period_anterior on tx_new.patient_id = tx_new_period_anterior.patient_id
										       where tx_new_period_anterior.patient_id is null
									) tx_new where art_start_date < :startDate
								)inicio
								left join
								(
									select patient_id,max(data_estado) data_estado                                                                                              
		                     			from (                                                                                                                                       
					                             	select distinct max_estado.patient_id, max_estado.data_estado from (                                                                
													select  pg.patient_id,                                                                                                          
													      max(ps.start_date) data_estado                                                                                          
													from    patient p                                                                                                               
													  inner join person pe on pe.person_id = p.patient_id                                                                         
													  inner join patient_program pg on p.patient_id = pg.patient_id                                                               
													  inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
													where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                        
													  and ps.start_date< :startDate and pg.location_id = :location group by pg.patient_id                                           
													) max_estado                                                                                                                        
													inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
													inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
													where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location                 
													union                                                                                                                               
													select  p.patient_id,                                                                                                               
													  max(o.obs_datetime) data_estado                                                                                             
													from    patient p                                                                                                                   
													  inner join person pe on pe.person_id = p.patient_id                                                                         
													  inner join encounter e on p.patient_id=e.patient_id                                                                         
													  inner join obs  o on e.encounter_id=o.encounter_id                                                                          
													where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0 and                                                              
													  e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1709 and                        
													  o.obs_datetime< :startDate and e.location_id= :location                                                                        
													group by p.patient_id                                                                                                               
													union                                                                                                                               
													
													select ultimaBusca.patient_id, ultimaBusca.data_estado                                                                              
					                             	from (                                                                                                                              
					                                     select p.patient_id,max(e.encounter_datetime) data_estado                                                                   
					                                     from patient p                                                                                                              
					                                         inner join person pe on pe.person_id = p.patient_id                                                                     
					                                         inner join encounter e on p.patient_id=e.patient_id                                                                     
					                                         inner join obs o on o.encounter_id=e.encounter_id                                                                       
					                                     where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime< :startDate                                       
					                                         and e.encounter_type = 21 and  e.location_id= :location                                                                 
					                                         group by p.patient_id                                                                                                   
					                                 ) ultimaBusca                                                                                                                   
					                                     inner join encounter e on e.patient_id = ultimaBusca.patient_id                                                             
					                                     inner join obs o on o.encounter_id = e.encounter_id                                                                         
					                                where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimaBusca.data_estado = e.encounter_datetime and e.location_id = :location                       
					                                                                                                                                                                                           
					                         ) allSaida                                                                                                                                      
					                 				group by patient_id 
					                
		              				) saida on inicio.patient_id = saida.patient_id  
								left join                                                                                                                                           
								( 	
									select p.patient_id,max(encounter_datetime) data_fila                                                                                                
									from patient p                                                                                                                                   
								     	inner join person pe on pe.person_id = p.patient_id                                                                                         
								      	inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18 
										and e.location_id=:location and e.encounter_datetime< :startDate                                                                                 
										group by p.patient_id                                                                                                                               
								) max_fila on inicio.patient_id=max_fila.patient_id
								left join                                                                                                                                           
								( 	
									select p.patient_id,min(encounter_datetime) data_fila_restart                                                                                                
									from patient p                                                                                                                                   
								     	inner join person pe on pe.person_id = p.patient_id                                                                                         
								      	inner join encounter e on e.patient_id=p.patient_id                                                                                         
									where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18 
										and e.location_id=:location and e.encounter_datetime between :startDate and :endDate                                                                                 
										group by p.patient_id                                                                                                                               
								) max_fila_restart on inicio.patient_id=max_fila_restart.patient_id                                                                                                 
								left join                                                                                                                                          
								(                                                                                                                                                  
									select p.patient_id,max(value_datetime) data_recepcao_levantou                                                                                     
								     from patient p                                                                                                                                   
								     	inner join person pe on pe.person_id = p.patient_id                                                                                         
								          inner join encounter e on p.patient_id=e.patient_id                                                                                         
								          inner join obs o on e.encounter_id=o.encounter_id                                                                                           
								     where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
								     	and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime < :startDate and e.location_id=:location                                                                                      
								     	group by p.patient_id                                                                                                                               
								) max_recepcao on inicio.patient_id=max_recepcao.patient_id 
								left join                                                                                                                                          
								(                                                                                                                                                  
									select p.patient_id,min(value_datetime) data_recepcao_levantou_restart                                                                                     
								     from patient p                                                                                                                                   
								     	inner join person pe on pe.person_id = p.patient_id                                                                                         
								          inner join encounter e on p.patient_id=e.patient_id                                                                                         
								          inner join obs o on e.encounter_id=o.encounter_id                                                                                           
								     where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
								     	and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime between :startDate and :endDate and e.location_id=:location                                                                                      
								     	group by p.patient_id                                                                                                                               
								) max_recepcao_restart on inicio.patient_id=max_recepcao_restart.patient_id                                                                                            
									group by inicio.patient_id  
								                                                                                                                       
							) inicio_fila_recepcao                                                                                                          
						   	left join                                                                                                                                          
							encounter ultimo_fila_data_criacao on inicio_fila_recepcao.patient_id = ultimo_fila_data_criacao.patient_id                                                                                 
								and ultimo_fila_data_criacao.voided=0                                     
								and ultimo_fila_data_criacao.encounter_type = 18  
								and ultimo_fila_data_criacao.encounter_datetime = inicio_fila_recepcao.data_fila                                                                                            
								and ultimo_fila_data_criacao.location_id=:location                      
							left join                                                                                                                                          
							obs obs_fila on inicio_fila_recepcao.patient_id = obs_fila.person_id                                                                                     
								and obs_fila.voided=0                                                                                                                             
								and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id                                                                                                        
								and obs_fila.concept_id=5096                                                                                                                       
								and obs_fila.location_id=:location                                                                                                                 
						group by inicio_fila_recepcao.patient_id  
					) inicio_fila_recepcao_prox
				  		group by patient_id                                                                                                                                                                    
		  ) coorte_final 
		  where ((data_estado is null or (data_estado is not null and  data_fila > data_estado)) and date_add(data_iit, interval 28 day) < date_add(:startDate, interval -1 day) or (data_iit is null and data_fila is not null) ) and data_restart between :startDate and :endDate  
 ) coorte_final  