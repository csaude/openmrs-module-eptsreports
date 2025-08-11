select transferido_para.patient_id
	from (
    	 select transferido_para.patient_id, data_transferencia, ultimo_fila.max_date  
    	 from ( 
    			select patient_id,max(data_estado) data_transferencia 
    			from ( 
    					select max_estado.patient_id, max_estado.data_estado 
						from(                                                                
							
							select max_estado.*
							from(
								
								select pp.patient_id, ps.patient_state_id, ps.state, max_estado.data_estado
								from(
									
									select pg.patient_id, max(ps.start_date) data_estado                                                                                          
									from patient p                                                                                                               
											inner join patient_program pg on p.patient_id = pg.patient_id                                                               
									  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                 
									where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
										and ps.start_date <= CURDATE()  and pg.location_id = :location group by pg.patient_id 
								)max_estado
									inner join patient_program pp on pp.patient_id = max_estado.patient_id
								 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado  
						  		where pp.program_id = 2 and pp.voided = 0 and ps.voided = 0 and pp.location_id =  :location 
						    			group by pp.patient_id, ps.patient_state_id  order by pp.patient_id, ps.patient_state_id desc         
						  	)max_estado group by max_estado.patient_id                                        
						) max_estado                                                                                                                        
							inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id  
							inner join patient_program pp on pp.patient_program_id = ps.patient_program_id         
						where ps.state = 7

    					union
    					 
						select  p.patient_id,max(o.obs_datetime) data_estado                                                                                             
		         		from patient p                                                                                                                   
		                 	inner join encounter e on p.patient_id=e.patient_id                                                                         
		                 	inner join obs o on e.encounter_id=o.encounter_id                                                                          
		         		where e.voided=0 and o.voided=0 and p.voided=0                                                                
		               	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706                          
		                 	and o.obs_datetime <= CURDATE()  and e.location_id=:location                                                                        
		         			group by p.patient_id 

		         	  union                                                                                                                               
	              
	              	select ultimaBusca.patient_id, ultimaBusca.data_estado                                                                              
	              	from (                                                                                                                              
	                      select p.patient_id,max(e.encounter_datetime) data_estado                                                                   
	                      from patient p                                                                                                              
	                          inner join encounter e on p.patient_id=e.patient_id                                                                     
	                          inner join obs o on o.encounter_id=e.encounter_id                                                                       
	                      where e.voided=0 and p.voided=0 and e.encounter_datetime <= CURDATE()                                    
	                          and e.encounter_type = 21 and  e.location_id= :location                                                                 
	                          group by p.patient_id                                                                                                   
	                  ) ultimaBusca                                                                                                                   
	                      inner join encounter e on e.patient_id = ultimaBusca.patient_id                                                             
	                      inner join obs o on o.encounter_id = e.encounter_id                                                                         
	                 where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimaBusca.data_estado = e.encounter_datetime and e.location_id = :location 	    	
    		) 
    	   transferido group by patient_id 
      ) 
     transferido_para 
     left join
	(
		select ultimo_fila.patient_id, ultimo_fila.max_date
		from(
			select p.patient_id,max(encounter_datetime) max_date                                                                                                
			from patient p                                                                                                                                   
				inner join person pe on pe.person_id = p.patient_id                                                                                         
				inner join encounter e on e.patient_id=p.patient_id                                                                                         
			where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                   
				and e.location_id=:location  and e.encounter_datetime <=CURDATE()                                                                             
				group by p.patient_id 
		) ultimo_fila
		inner join(
		select patient_id , data_ultimo_levantamento    
		from(  	
       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
               from(
         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
					from patient p                                                                                                                                   
						inner join encounter e on e.patient_id= p.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id                                                                                        
					where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                            
						and e.location_id= :location and e.encounter_datetime <= CURDATE()                                                                               
						group by p.patient_id 
         
         				union
         
              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
              			from patient p                                                                                                                                   
               			inner join person pe on pe.person_id = p.patient_id                                                                                         
                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id= :location and o.value_datetime <= CURDATE()                                                                                       
              				group by p.patient_id
               ) ultimo_levantamento group by patient_id
      	) ultimo_levantamento
     		where ultimo_levantamento.data_ultimo_levantamento <= CURDATE() 
     	)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
	
	) ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
			where transferido_para.data_transferencia >= ultimo_fila.max_date or ultimo_fila.max_date is null
)
transferido_para
left join (
		select patient_id , data_ultimo_levantamento    
		from(  	
       		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
               from(
         				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
					from patient p                                                                                                                                   
						inner join encounter e on e.patient_id= p.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id                                                                                        
					where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                           
						and e.location_id= :location and e.encounter_datetime <= CURDATE()                                                                               
						group by p.patient_id 
         
         				union
         
              			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
              			from patient p                                                                                                                                   
               			inner join person pe on pe.person_id = p.patient_id                                                                                         
                    		inner join encounter e on p.patient_id=e.patient_id                                                                                         
                    		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
              			where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
                    		and o.concept_id=23866 and o.value_datetime is not null and e.location_id= :location and o.value_datetime <= CURDATE()                                                                                       
              				group by p.patient_id
               ) ultimo_levantamento group by patient_id
      	) ultimo_levantamento
     		where ultimo_levantamento.data_ultimo_levantamento > CURDATE() 
     		) maxFilaMaiorCurDate on maxFilaMaiorCurDate.patient_id = transferido_para.patient_id
     		where maxFilaMaiorCurDate.patient_id is null
