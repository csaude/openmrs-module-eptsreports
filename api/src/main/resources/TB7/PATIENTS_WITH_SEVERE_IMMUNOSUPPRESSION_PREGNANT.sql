select cd4_eligible.patient_id      
 from(
select cd4_eligible.patient_id, ultima_marcacao_gravida.data_gravida 
from (

		select cd4_eligible.* 
		from(
					select final.* 
		from (
					select 
							pe.person_id as patient_id,
							pe.gender, 
							gravida_real.data_gravida,
							lactante_real.data_parto,
							if(lactante_real.data_parto is null,1, if(gravida_real.data_gravida is null,2, if(gravida_real.data_gravida>=lactante_real.data_parto,1,2))) decisao 
		            from person pe
		            left join (
		                        select gravida_real.*
		                        from(
			             			select gravida_real.patient_id,max(gravida_real.data_gravida) data_gravida  
								from ( 
											
					           		select p.patient_id,e.encounter_datetime data_gravida 
									from patient p 
										inner join encounter e on p.patient_id=e.patient_id 
										inner join obs o on e.encounter_id=o.encounter_id 
									where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate  and :endDate and e.location_id= :location
									
									union 
									select p.patient_id,e.encounter_datetime data_gravida 
									from patient p 
										inner join encounter e on p.patient_id=e.patient_id 
										inner join obs o on e.encounter_id=o.encounter_id 
									where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
									
									union 
									
									select  p.patient_id,o.value_datetime data_gravida 
									from patient p 
										inner join encounter e on p.patient_id=e.patient_id 
										inner join obs o on e.encounter_id=o.encounter_id 
									where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
									
									union 
									
									select p.patient_id,e.encounter_datetime data_gravida 
									from patient p 
										inner join encounter e on p.patient_id=e.patient_id 
										inner join obs o on e.encounter_id=o.encounter_id 
									where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
								
									union 
									
									select pp.patient_id,pp.date_enrolled data_gravida 
									from patient_program pp 
									where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and :endDate and pp.location_id= :location
									
									union 
									
									select p.patient_id,obsART.value_datetime data_gravida 
									from patient p 
										inner join encounter e on p.patient_id=e.patient_id 
										inner join obs o on e.encounter_id=o.encounter_id 
										inner join obs obsART on e.encounter_id=obsART.encounter_id 
									where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065  and e.location_id= :location
										and e.encounter_type=53 and obsART.value_datetime between :startDate and :endDate and obsART.concept_id=1190 and obsART.voided=0 
									
									union 
									
									select p.patient_id,data_colheita.value_datetime data_gravida 
									from patient p                                                                                                                                                                        
										inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
										inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
										inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
									where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
										and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
										and data_colheita.concept_id =23821 and data_colheita.value_datetime between :startDate and :endDate and e.location_id=:location  
										
								
								) 
							gravida_real 
								group by gravida_real.patient_id
						) gravida_real
						inner join(
								select gravida_real.*
								from(
										select gravida_real.patient_id,min(gravida_real.data_gravida) data_gravida  
										from ( 
													
							           		select p.patient_id,e.encounter_datetime data_gravida 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
											where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate  and :endDate and e.location_id=:location
											
											union 
											select p.patient_id,e.encounter_datetime data_gravida 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
											where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
											
											union 
											
											select  p.patient_id,o.value_datetime data_gravida 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
											where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
											
											union 
											
											select p.patient_id,e.encounter_datetime data_gravida 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
											where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
										
											union 
											
											select pp.patient_id,pp.date_enrolled data_gravida 
											from patient_program pp 
											where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and :endDate and pp.location_id= :location
											
											union 
											
											select p.patient_id,obsART.value_datetime data_gravida 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
												inner join obs obsART on e.encounter_id=obsART.encounter_id 
											where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065  and e.location_id= :location
												and e.encounter_type=53 and obsART.value_datetime between :startDate and :endDate and obsART.concept_id=1190 and obsART.voided=0 
											
											union 
											
											select p.patient_id,data_colheita.value_datetime data_gravida 
											from patient p                                                                                                                                                                        
												inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
												inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
												inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
											where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
												and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
												and data_colheita.concept_id =23821 and data_colheita.value_datetime between :startDate and :endDate and e.location_id=:location  
										) 
										gravida_real 
										group by gravida_real.patient_id
									) 
									gravida_real
									left join (
									          select p.patient_id, max(o.obs_datetime) obs_datetime
									          from patient p
									            inner join encounter e on e.patient_id=p.patient_id
									            inner join obs o on o.encounter_id=e.encounter_id
									          where e.voided=0 and e.encounter_type in (6,13,53,51,90) and o.concept_id in (1695, 165515) and o.voided=0 
									          	and  e.location_id= :location and o.obs_datetime between :startDate and :endDate and o.obs_datetime < (:startDate + INTERVAL 8 MONTH)
									            group by p.patient_id
									  )cd4_absolute on gravida_real.patient_id = cd4_absolute.patient_id
									       where  cd4_absolute.obs_datetime is null or  cd4_absolute.obs_datetime < gravida_real.data_gravida
		    				) fator_exclusao_gravida on fator_exclusao_gravida.patient_id = gravida_real.patient_id
		
		            ) gravida_real on pe.person_id = gravida_real.patient_id 
				left join(
				
						select lactante_real.patient_id,max(lactante_real.data_parto) data_parto  
						from ( 
						
							select p.patient_id,o.value_datetime data_parto 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
								inner join obs o on e.encounter_id=o.encounter_id 
							where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599  
								and e.encounter_type in (5,6) and o.value_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and e.location_id= :location
							
							union 
							
							select p.patient_id, e.encounter_datetime data_parto 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
								inner join obs o on e.encounter_id=o.encounter_id 
							where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and e.encounter_type=6 and e.encounter_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and e.location_id= :location
							
							union
							
							select p.patient_id, e.encounter_datetime data_parto 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
								inner join obs o on e.encounter_id=o.encounter_id 
								where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332  
							and e.encounter_type in (5,6) and e.encounter_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and e.location_id= :location
							
							union  
							
							select pg.patient_id,ps.start_date data_parto 
							from patient p 
								inner join patient_program pg on p.patient_id=pg.patient_id 
								inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
							where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=8 and ps.state=27  and ps.start_date between (:startDate - INTERVAL 9 MONTH) and :endDate and location_id= :location
							
							union 
							select p.patient_id, obsART.value_datetime data_parto 
							from patient p 
								inner join encounter e on p.patient_id=e.patient_id 
								inner join obs o on e.encounter_id=o.encounter_id 
								inner join obs obsART on e.encounter_id=obsART.encounter_id 
							where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and e.encounter_type=53 and e.location_id=  :location
								and obsART.value_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and obsART.concept_id=1190 and obsART.voided=0 
							
							union
							
							select p.patient_id,data_colheita.value_datetime data_parto 
							from patient p                                                                                                                                                                        
								inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
								inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
								inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
							where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
								and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
								and data_colheita.concept_id =23821 and data_colheita.value_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and e.location_id=:location
					) 
					lactante_real 
						group by lactante_real.patient_id
			  )
			lactante_real on lactante_real.patient_id=pe.person_id 
				where (lactante_real.data_parto is not null or gravida_real.data_gravida is not null ) and pe.gender='F'
					group by pe.person_id
		)final 
		where final.decisao = 1 and data_gravida <= :endDate
		)cd4_eligible
		inner join(
			select gravida_real.*
			from(
				select gravida_real.patient_id,min(gravida_real.data_gravida) data_gravida  
				from ( 
							
			 		select p.patient_id,e.encounter_datetime data_gravida 
					from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate  and :endDate and e.location_id=:location
					
					union 
					select p.patient_id,e.encounter_datetime data_gravida 
					from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
					
					union 
					
					select  p.patient_id,o.value_datetime data_gravida 
					from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
					
					union 
					
					select p.patient_id,e.encounter_datetime data_gravida 
					from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
				
					union 
					
					select pp.patient_id,pp.date_enrolled data_gravida 
					from patient_program pp 
					where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and :endDate and pp.location_id= :location
					
					union 
					
					select p.patient_id,obsART.value_datetime data_gravida 
					from patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
						inner join obs obsART on e.encounter_id=obsART.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065  and e.location_id= :location
						and e.encounter_type=53 and obsART.value_datetime between :startDate and :endDate and obsART.concept_id=1190 and obsART.voided=0 
					
					union 
					
					select p.patient_id,data_colheita.value_datetime data_gravida 
					from patient p                                                                                                                                                                        
						inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
						inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
						inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
					where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
						and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
						and data_colheita.concept_id =23821 and data_colheita.value_datetime between :startDate and :endDate and e.location_id=:location  				
					) 
				gravida_real 
					group by gravida_real.patient_id
			) gravida_real
		)primeira_marcacao_gravida on cd4_eligible.patient_id = primeira_marcacao_gravida.patient_id
	 where primeira_marcacao_gravida.data_gravida < (:startDate + INTERVAL 8 MONTH)
)cd4_eligible
inner join(
		select gravida_real.*
		from(
			select gravida_real.patient_id,max(gravida_real.data_gravida) data_gravida  
			from ( 
						
		 		select p.patient_id,e.encounter_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate  and (:startDate + INTERVAL 8 MONTH  - INTERVAL 1 DAY) and e.location_id=:location
				
				union 
				select p.patient_id,e.encounter_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and (:startDate + INTERVAL 8 MONTH  - INTERVAL 1 DAY) and e.location_id=:location
				
				union 
				
				select  p.patient_id,o.value_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and (:startDate + INTERVAL 8 MONTH  - INTERVAL 1 DAY) and e.location_id=:location
				
				union 
				
				select p.patient_id,e.encounter_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and (:startDate + INTERVAL 8 MONTH  - INTERVAL 1 DAY) and e.location_id=:location
			
				union 
				
				select pp.patient_id,pp.date_enrolled data_gravida 
				from patient_program pp 
				where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and (:startDate + INTERVAL 8 MONTH  - INTERVAL 1 DAY) and pp.location_id= :location
				
				union 
				
				select p.patient_id,obsART.value_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
					inner join obs obsART on e.encounter_id=obsART.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065  and e.location_id= :location
					and e.encounter_type=53 and obsART.value_datetime between :startDate and (:startDate + INTERVAL 8 MONTH  - INTERVAL 1 DAY) and obsART.concept_id=1190 and obsART.voided=0 
				
				union 
				
				select p.patient_id,data_colheita.value_datetime data_gravida 
				from patient p                                                                                                                                                                        
					inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
					inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
					inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
				where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
					and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
					and data_colheita.concept_id =23821 and data_colheita.value_datetime between :startDate and (:startDate + INTERVAL 8 MONTH  - INTERVAL 1 DAY) and e.location_id=:location  				
				) 
			gravida_real 
				group by gravida_real.patient_id
		) gravida_real
)ultima_marcacao_gravida on cd4_eligible.patient_id = ultima_marcacao_gravida.patient_id
 where ultima_marcacao_gravida.data_gravida < (:startDate + INTERVAL 8 MONTH)
 
union

 select cd4_eligible.patient_id, primeira_marcacao_gravida.data_gravida 
from (
	select final.* 
	from (
		select 
			pe.person_id as patient_id,
			pe.gender, 
			gravida_real.data_gravida,
			lactante_real.data_parto,
			if(lactante_real.data_parto is null,1, if(gravida_real.data_gravida is null,2, if(gravida_real.data_gravida>=lactante_real.data_parto,1,2))) decisao 
       from person pe
	            left join (
	                        select gravida_real.*
	                        from(
		             			select gravida_real.patient_id,max(gravida_real.data_gravida) data_gravida  
							from ( 
										
				           		select p.patient_id,e.encounter_datetime data_gravida 
								from patient p 
									inner join encounter e on p.patient_id=e.patient_id 
									inner join obs o on e.encounter_id=o.encounter_id 
								where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate  and :endDate and e.location_id=:location
								
								union 
								select p.patient_id,e.encounter_datetime data_gravida 
								from patient p 
									inner join encounter e on p.patient_id=e.patient_id 
									inner join obs o on e.encounter_id=o.encounter_id 
								where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
								
								union 
								
								select  p.patient_id,o.value_datetime data_gravida 
								from patient p 
									inner join encounter e on p.patient_id=e.patient_id 
									inner join obs o on e.encounter_id=o.encounter_id 
								where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
								
								union 
								
								select p.patient_id,e.encounter_datetime data_gravida 
								from patient p 
									inner join encounter e on p.patient_id=e.patient_id 
									inner join obs o on e.encounter_id=o.encounter_id 
								where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
							
								union 
								
								select pp.patient_id,pp.date_enrolled data_gravida 
								from patient_program pp 
								where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and :endDate and pp.location_id= :location
								
								union 
								
								select p.patient_id,obsART.value_datetime data_gravida 
								from patient p 
									inner join encounter e on p.patient_id=e.patient_id 
									inner join obs o on e.encounter_id=o.encounter_id 
									inner join obs obsART on e.encounter_id=obsART.encounter_id 
								where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065  and e.location_id= :location
									and e.encounter_type=53 and obsART.value_datetime between :startDate and :endDate and obsART.concept_id=1190 and obsART.voided=0 
								
								union 
								
								select p.patient_id,data_colheita.value_datetime data_gravida 
								from patient p                                                                                                                                                                        
									inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
									inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
									inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
								where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
									and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
									and data_colheita.concept_id =23821 and data_colheita.value_datetime between :startDate and :endDate and e.location_id=:location  
									
							
							) 
						gravida_real 
							group by gravida_real.patient_id
					) gravida_real
					inner join(
							select gravida_real.*
							from(
									select gravida_real.patient_id,min(gravida_real.data_gravida) data_gravida  
									from ( 
												
						           		select p.patient_id,e.encounter_datetime data_gravida 
										from patient p 
											inner join encounter e on p.patient_id=e.patient_id 
											inner join obs o on e.encounter_id=o.encounter_id 
										where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate  and :endDate and e.location_id=:location
										
										union 
										select p.patient_id,e.encounter_datetime data_gravida 
										from patient p 
											inner join encounter e on p.patient_id=e.patient_id 
											inner join obs o on e.encounter_id=o.encounter_id 
										where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
										
										union 
										
										select  p.patient_id,o.value_datetime data_gravida 
										from patient p 
											inner join encounter e on p.patient_id=e.patient_id 
											inner join obs o on e.encounter_id=o.encounter_id 
										where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
										
										union 
										
										select p.patient_id,e.encounter_datetime data_gravida 
										from patient p 
											inner join encounter e on p.patient_id=e.patient_id 
											inner join obs o on e.encounter_id=o.encounter_id 
										where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
									
										union 
										
										select pp.patient_id,pp.date_enrolled data_gravida 
										from patient_program pp 
										where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and :endDate and pp.location_id= :location
										
										union 
										
										select p.patient_id,obsART.value_datetime data_gravida 
										from patient p 
											inner join encounter e on p.patient_id=e.patient_id 
											inner join obs o on e.encounter_id=o.encounter_id 
											inner join obs obsART on e.encounter_id=obsART.encounter_id 
										where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065  and e.location_id= :location
											and e.encounter_type=53 and obsART.value_datetime between :startDate and :endDate and obsART.concept_id=1190 and obsART.voided=0 
										
										union 
										
										select p.patient_id,data_colheita.value_datetime data_gravida 
										from patient p                                                                                                                                                                        
											inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
											inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
											inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
										where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
											and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
											and data_colheita.concept_id =23821 and data_colheita.value_datetime between :startDate and :endDate and e.location_id=:location  
									) 
									gravida_real 
									group by gravida_real.patient_id
								) 
								gravida_real
								left join (
								          select p.patient_id, max(o.obs_datetime) obs_datetime
								          from patient p
								            inner join encounter e on e.patient_id=p.patient_id
								            inner join obs o on o.encounter_id=e.encounter_id
								          where e.voided=0 and e.encounter_type in (6,13,53,51,90) and o.concept_id in (1695, 165515) and o.voided=0 
								          	and  e.location_id= :location and o.obs_datetime between :startDate and :endDate and o.obs_datetime < (:startDate + INTERVAL 8 MONTH)
								            group by p.patient_id
								  )cd4_absolute on gravida_real.patient_id = cd4_absolute.patient_id
								       where  cd4_absolute.obs_datetime is null or  cd4_absolute.obs_datetime < gravida_real.data_gravida
	    				) fator_exclusao_gravida on fator_exclusao_gravida.patient_id = gravida_real.patient_id
	
	            ) gravida_real on pe.person_id = gravida_real.patient_id 
			left join(
			
					select lactante_real.patient_id,max(lactante_real.data_parto) data_parto  
					from ( 
					
						select p.patient_id,o.value_datetime data_parto 
						from patient p 
							inner join encounter e on p.patient_id=e.patient_id 
							inner join obs o on e.encounter_id=o.encounter_id 
						where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599  
							and e.encounter_type in (5,6) and o.value_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and e.location_id= :location
						
						union 
						
						select p.patient_id, e.encounter_datetime data_parto 
						from patient p 
							inner join encounter e on p.patient_id=e.patient_id 
							inner join obs o on e.encounter_id=o.encounter_id 
						where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and e.encounter_type=6 and e.encounter_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and e.location_id= :location
						
						union
						
						select p.patient_id, e.encounter_datetime data_parto 
						from patient p 
							inner join encounter e on p.patient_id=e.patient_id 
							inner join obs o on e.encounter_id=o.encounter_id 
							where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332  
						and e.encounter_type in (5,6) and e.encounter_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and e.location_id= :location
						
						union  
						
						select pg.patient_id,ps.start_date data_parto 
						from patient p 
							inner join patient_program pg on p.patient_id=pg.patient_id 
							inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=8 and ps.state=27  and ps.start_date between (:startDate - INTERVAL 9 MONTH) and :endDate and location_id= :location
						
						union 
						select p.patient_id, obsART.value_datetime data_parto 
						from patient p 
							inner join encounter e on p.patient_id=e.patient_id 
							inner join obs o on e.encounter_id=o.encounter_id 
							inner join obs obsART on e.encounter_id=obsART.encounter_id 
						where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and e.encounter_type=53 and e.location_id=  :location
							and obsART.value_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and obsART.concept_id=1190 and obsART.voided=0 
						
						union
						
						select p.patient_id,data_colheita.value_datetime data_parto 
						from patient p                                                                                                                                                                        
							inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
							inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
							inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
						where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
							and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
							and data_colheita.concept_id =23821 and data_colheita.value_datetime between (:startDate - INTERVAL 9 MONTH) and :endDate and e.location_id=:location
				) 
				lactante_real 
					group by lactante_real.patient_id
		  )
		lactante_real on lactante_real.patient_id=pe.person_id 
			where (lactante_real.data_parto is not null or gravida_real.data_gravida is not null ) and pe.gender='F'
				group by pe.person_id
	)final 
	where final.decisao = 1 and data_gravida <= :endDate
	)cd4_eligible
	inner join(
		select gravida_real.*
		from(
			select gravida_real.patient_id,min(gravida_real.data_gravida) data_gravida  
			from ( 
						
		 		select p.patient_id,e.encounter_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate  and :endDate and e.location_id=:location
				
				union 
				select p.patient_id,e.encounter_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
				
				union 
				
				select  p.patient_id,o.value_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
				
				union 
				
				select p.patient_id,e.encounter_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate and e.location_id=:location
			
				union 
				
				select pp.patient_id,pp.date_enrolled data_gravida 
				from patient_program pp 
				where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and :endDate and pp.location_id= :location
				
				union 
				
				select p.patient_id,obsART.value_datetime data_gravida 
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
					inner join obs obsART on e.encounter_id=obsART.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065  and e.location_id= :location
					and e.encounter_type=53 and obsART.value_datetime between :startDate and :endDate and obsART.concept_id=1190 and obsART.voided=0 
				
				union 
				
				select p.patient_id,data_colheita.value_datetime data_gravida 
				from patient p                                                                                                                                                                        
					inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
					inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
					inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
				where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
					and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
					and data_colheita.concept_id =23821 and data_colheita.value_datetime between :startDate and :endDate and e.location_id=:location  				
				) 
			gravida_real 
				group by gravida_real.patient_id
		) gravida_real
	)primeira_marcacao_gravida on cd4_eligible.patient_id = primeira_marcacao_gravida.patient_id
 where primeira_marcacao_gravida.data_gravida between (:startDate + INTERVAL 8 MONTH) and :endDate
	  
) cd4_eligible                                             
 left join(
 	
		select max_cd4.patient_id, max_cd4.max_data_cd4 
		from(
		
			select max_cd4.patient_id, max_cd4.max_data_cd4 
			from( 
				
				select p.patient_id,o.obs_datetime max_data_cd4  
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
					and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
			)max_cd4 
				inner join person per on per.person_id=max_cd4.patient_id 
				inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
				and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 1695 and o.value_numeric<200 and o.location_id= :location 
		
			union
		
			select distinct max_cd4.patient_id, max_cd4.max_data_cd4 
			from( 
				
				select p.patient_id,o.obs_datetime max_data_cd4  
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165515 and  e.encounter_type in (6,51,13,53,90) 
					and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
			)max_cd4 
				inner join person per on per.person_id=max_cd4.patient_id 
				inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
				and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 165515 and o.value_coded = 165513  and o.location_id= :location
	
			union
	
			select distinct max_cd4.patient_id, max_cd4.max_data_cd4 
			from( 
				
				select p.patient_id,o.obs_datetime max_data_cd4  
				from patient p 
					inner join encounter e on p.patient_id=e.patient_id 
					inner join obs o on e.encounter_id=o.encounter_id 
				where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165519 and  e.encounter_type in (6,51,13,53,90) 
					and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
			)max_cd4 
				inner join person per on per.person_id=max_cd4.patient_id 
				inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
				and per.voided=0 and timestampdiff(year,per.birthdate,:endDate)>=5 and o.concept_id = 165519 and o.value_coded = 165513  and o.location_id= :location
		)max_cd4
	
		union
	
		select distinct max_cd4.patient_id, max_cd4.max_data_cd4 
		from( 
			
			select p.patient_id,o.obs_datetime max_data_cd4  
			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
				and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
		)max_cd4 
			inner join person per on per.person_id=max_cd4.patient_id 
			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
			and per.voided=0 and timestampdiff(month,per.birthdate,:startDate)>=12 and timestampdiff(year,per.birthdate,:endDate)< 5  and o.concept_id = 1695 and o.value_numeric < 500 and o.location_id= :location 
	
		union
	
		select distinct max_cd4.patient_id, max_cd4.max_data_cd4 
		from( 
			
			select p.patient_id,o.obs_datetime max_data_cd4  
			from patient p 
				inner join encounter e on p.patient_id=e.patient_id 
				inner join obs o on e.encounter_id=o.encounter_id 
			where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,51,13,53,90) 
				and o.obs_datetime <= date_add(:endDate, interval  33 day) and e.location_id=:location 
		)max_cd4 
			inner join person per on per.person_id=max_cd4.patient_id 
			inner join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0  
			and per.voided=0 and timestampdiff(month,per.birthdate,:endDate)< 12   and o.concept_id = 1695 and o.value_numeric < 750 and o.location_id= :location 
 
 )cd4_absolute on cd4_eligible.patient_id = cd4_absolute.patient_id                   
  where  cd4_absolute.max_data_cd4 between cd4_eligible.data_gravida and date_add(cd4_eligible.data_gravida, interval 33 day)
  