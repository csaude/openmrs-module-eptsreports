select inicio_tarv.patient_id from 
                	(  select patient_id,art_start_date from 
                      (
                      select patient_id, min(art_start_date) art_start_date 
                      from 
                            (
                                  select p.patient_id, min(e.encounter_datetime) art_start_date 
                                  from patient p 
                                        inner join encounter e on p.patient_id=e.patient_id 
                                        inner join obs o on o.encounter_id=e.encounter_id 
                                  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
                                        and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=(:endDate - INTERVAL 1 MONTH ) and e.location_id=:location 
                                        group by p.patient_id 
                                  union 
                                  
                                  select p.patient_id, min(value_datetime) art_start_date 
                                  from patient p 
                                        inner join encounter e on p.patient_id=e.patient_id 
                                        inner join obs o on e.encounter_id=o.encounter_id 
                                  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
                                        and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=(:endDate - INTERVAL 1 MONTH ) and e.location_id=:location 
                                        group by p.patient_id 
                                  
                                  union 
                       
                                  select pg.patient_id, min(date_enrolled) art_start_date 
                                  from patient p 
                                        inner join patient_program pg on p.patient_id=pg.patient_id 
                                  where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=(:endDate - INTERVAL 1 MONTH ) and location_id=:location 
                                        group by pg.patient_id 
                                  
                                  union 

                                  select e.patient_id, min(e.encounter_datetime) as art_start_date 
                                  from patient p 
                                        inner join encounter e on p.patient_id=e.patient_id 
                                  where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=(:endDate - INTERVAL 1 MONTH ) and e.location_id=:location 
                                        group by p.patient_id 
                                  
                                  union 
                       
                                  select p.patient_id, min(value_datetime) art_start_date 
                                  from patient p 
                                        inner join encounter e on p.patient_id=e.patient_id 
                                        inner join obs o on e.encounter_id=o.encounter_id 
                                  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
                                        and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=(:endDate - INTERVAL 1 MONTH ) and e.location_id=:location 
                                        group by p.patient_id
                            ) 
                      art_start group by patient_id 
                ) tx_new where art_start_date <= (:endDate - INTERVAL 1 MONTH ) and art_start_date < '2023-12-21'
                union
                select tx_new.patient_id,tx_new.art_start_date 
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
                                              where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=(:endDate - INTERVAL 1 MONTH ) and e.location_id=:location 
                                                    group by p.patient_id 
                                              
                                              union 
                                   
                                              select p.patient_id, min(value_datetime) art_start_date 
                                              from patient p 
                                                    inner join encounter e on p.patient_id=e.patient_id 
                                                    inner join obs o on e.encounter_id=o.encounter_id 
                                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
                                                    and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=(:endDate - INTERVAL 1 MONTH ) and e.location_id=:location 
                                                    group by p.patient_id
                                        ) 
                                  art_start group by patient_id 
                            ) tx_new where art_start_date <= (:endDate - INTERVAL 1 MONTH ) and art_start_date >= '2023-12-21'
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
                                                    and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=(:endDate - INTERVAL 1 MONTH ) and e.location_id=:location 
                                                    group by p.patient_id 
                                              union 
                                              
                                              select p.patient_id, min(value_datetime) art_start_date 
                                              from patient p 
                                                    inner join encounter e on p.patient_id=e.patient_id 
                                                    inner join obs o on e.encounter_id=o.encounter_id 
                                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
                                                    and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=(:endDate - INTERVAL 1 MONTH ) and e.location_id=:location 
                                                    group by p.patient_id 
                                              
                                              union 
                                   
                                              select pg.patient_id, min(date_enrolled) art_start_date 
                                              from patient p 
                                                    inner join patient_program pg on p.patient_id=pg.patient_id 
                                              where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=(:endDate - INTERVAL 1 MONTH ) and location_id=:location 
                                                    group by pg.patient_id 
                                        ) 
                                  art_start group by patient_id 
                            ) tx_new where art_start_date < '2023-12-21'
                      ) tx_new_period_anterior on tx_new.patient_id = tx_new_period_anterior.patient_id
                       where tx_new_period_anterior.patient_id is null
                       )tx_new
                	) inicio_tarv																				
                	inner join																					
                	(
                	select art_init_real.patient_id, art_init_real.art_enrolled_date from(                     
                		select patient_id,min(art_enrolled_date) art_enrolled_date from(                        
                	  		select p.patient_id, min(o.value_datetime) art_enrolled_date from patient p         
                	  			join encounter e on e.patient_id = p.patient_id            			            
                	  			join obs o on o.encounter_id = e.encounter_id								    
                	  		where p.voided =0 and e.voided = 0 and o.voided = 0 				                
                	  			and e.encounter_type = 53 and o.concept_id =23808 and o.value_datetime is not null 
                	  			and o.value_datetime <=(:endDate - INTERVAL 1 MONTH )                           
                	               and e.location_id =:location group by p.patient_id							
                	  		union                                                                               
                	  		select patient.patient_id, min(patient_program.date_enrolled) art_enrolled_date from patient 
                	  			join patient_program on patient_program.patient_id = patient.patient_id      	
                	  			join program on program.program_id = patient_program.program_id	             	
                	  		where patient.voided = 0 and patient_program.voided = 0 and program.retired =0   	
                	  			and program.program_id = 1 and patient_program.date_enrolled is not null     	
                	  			and patient_program.date_enrolled <=(:endDate - INTERVAL 1 MONTH )		     	
                	  			and patient_program.location_id =:location group by patient.patient_id          
                	  	) art_init group by patient_id                                                          
                	) art_init_real                                                                          	
                	where art_enrolled_date between (:endDate - INTERVAL 2 MONTH  + INTERVAL 1 DAY) and (:endDate - INTERVAL 1 MONTH )	
                	) inscricao_tarv on inicio_tarv.patient_id = inscricao_tarv.patient_id	
                	where inscricao_tarv.art_enrolled_date > inicio_tarv.art_start_date