select transferido_para.patient_id
from(
	select transferido_para.*
	from(
		select patient_id,
	   		max(data_transferido_para) data_transferido_para
	   	from(
	      	select maxEstado.patient_id,
	      		maxEstado.data_transferido_para
	      	from(
	         		select pg.patient_id,
	         			max(ps.start_date) data_transferido_para
	         		from patient p
	         			inner join patient_program pg on p.patient_id = pg.patient_id
	         			inner join patient_state ps on pg.patient_program_id = ps.patient_program_id
	         		where pg.voided = 0 and ps.voided = 0 and p.voided = 0 and pg.program_id = 2
	         			and ps.start_date <= :endDate and pg.location_id = :location
	         			group by p.patient_id
	      	)
	      	maxEstado
	      		inner join patient_program pg2 on pg2.patient_id = maxEstado.patient_id
	      		inner join patient_state ps2 on pg2.patient_program_id = ps2.patient_program_id
	      	where pg2.voided = 0 and ps2.voided = 0 and pg2.program_id = 2 and ps2.start_date = maxEstado.data_transferido_para
	      		and pg2.location_id =:location and ps2.state = 7
	      	union
	      	
	      	select p.patient_id,
	      		max(e.encounter_datetime) data_transferido_para
	      	from patient p
	      		inner join encounter e on p.patient_id = e.patient_id
	      		inner join obs o on o.encounter_id = e.encounter_id
	      	where e.voided = 0 and p.voided = 0 and e.encounter_datetime <:startDate and e.encounter_type = 21
	      		and o.voided = 0 and o.concept_id = 2016 and o.value_coded in(1706,23863) and e.location_id =:location
	      		group by p.patient_id
	      	union
	      	
	      	select p.patient_id,
	      		max(o.obs_datetime) data_transferido_para
	      	from patient p
	     		 inner join encounter e on p.patient_id = e.patient_id
	      		inner join obs o on o.encounter_id = e.encounter_id
	      	where e.voided = 0 and p.voided = 0 and o.obs_datetime <:startDate and o.voided = 0 and o.concept_id = 6272 and o.value_coded = 1706
	      		and e.encounter_type = 53 and e.location_id =:location
	      		group by p.patient_id
	      	
	      	union
	      	
	      	select p.patient_id,
	      		max(e.encounter_datetime) data_transferido_para
	      	from patient p
	      		inner join encounter e on p.patient_id = e.patient_id
	      		inner join obs o on o.encounter_id = e.encounter_id
	      	where e.voided = 0 and p.voided = 0 and e.encounter_datetime <:startDate and o.voided = 0
	      		and o.concept_id = 6273 and o.value_coded = 1706 and e.encounter_type = 6 and e.location_id =:location
	      		group by p.patient_id
	  	)
	   	transferido
	   		group by patient_id
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
				and e.location_id=:location  and e.encounter_datetime <:startDate                                                                             
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
						and e.location_id= :location and e.encounter_datetime <= :endDate                                                                               
						group by p.patient_id 
		
					union
					
					select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
		       		from patient p                                                                                                                                   
						inner join person pe on pe.person_id = p.patient_id                                                                                         
			     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
			     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
		       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
		               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id= :location and o.value_datetime <= :endDate                                                                                        
						group by p.patient_id
		          ) ultimo_levantamento group by patient_id
		 	) ultimo_levantamento
			where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
		)ultimo_levantamento on ultimo_levantamento.patient_id = ultimo_fila.patient_id
	) 
	ultimo_fila on transferido_para.patient_id  = ultimo_fila.patient_id 
	where (transferido_para.data_transferido_para >= ultimo_fila.max_date or ultimo_fila.max_date is null)
		and transferido_para.data_transferido_para <= :endDate
)
transferido_para
inner join
(
	select patient_id , data_ultimo_levantamento    
	from(  	
  		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
          from(

			select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
			from patient p                                                                                                                                   
				inner join encounter e on e.patient_id= p.patient_id 
				inner join obs o on o.encounter_id = e.encounter_id                                                                                        
			where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
				and e.location_id= :location and e.encounter_datetime <= :endDate                                                                               
				group by p.patient_id 

			union
			
			select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
       		from patient p                                                                                                                                   
				inner join person pe on pe.person_id = p.patient_id                                                                                         
	     		inner join encounter e on p.patient_id=e.patient_id                                                                                         
	     		inner join obs o on e.encounter_id=o.encounter_id                                                                                           
       		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
               	and o.concept_id=23866 and o.value_datetime is not null and e.location_id= :location and o.value_datetime <= :endDate                                                                                        
				group by p.patient_id
          ) ultimo_levantamento group by patient_id
 	) ultimo_levantamento
	where ultimo_levantamento.data_ultimo_levantamento <= :endDate 
)ultimo_levantamento on ultimo_levantamento.patient_id = transferido_para.patient_id
where ultimo_levantamento.patient_id is null or ( ultimo_levantamento.patient_id is not null and ultimo_levantamento.data_ultimo_levantamento between :startDate and :endDate)

