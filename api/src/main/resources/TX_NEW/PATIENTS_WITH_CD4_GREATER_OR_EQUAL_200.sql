select tx_new.patient_id
from (
		select tx_new.patient_id, min(tx_new.data_cd4) data_cd4,tx_new.data_cd4__less
		from (
		select tx_new.patient_id, tx_new.art_start_date,first_cd4.data_cd4,first_cd4.data_cd4__less  from  
		(
		select tx_new.patient_id, tx_new.art_start_date  from  
		(
		      select tx_new.patient_id, tx_new.art_start_date from 
		      (
		            select patient_id, min(art_start_date) art_start_date 
		            from 
		                  (
		                        select p.patient_id, min(e.encounter_datetime) art_start_date 
		                        from patient p 
		                              inner join encounter e on p.patient_id=e.patient_id 
		                              inner join obs o on o.encounter_id=e.encounter_id 
		                        where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
		                              and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location 
		                              group by p.patient_id 
		                        union 
		                        
		                        select p.patient_id, min(value_datetime) art_start_date 
		                        from patient p 
		                              inner join encounter e on p.patient_id=e.patient_id 
		                              inner join obs o on e.encounter_id=o.encounter_id 
		                        where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
		                              and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
		                              group by p.patient_id 
		                        
		                        union 
		             
		                        select pg.patient_id, min(date_enrolled) art_start_date 
		                        from patient p 
		                              inner join patient_program pg on p.patient_id=pg.patient_id 
		                        where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
		                              group by pg.patient_id 
		                        
		                        union 
		      
		                        select e.patient_id, min(e.encounter_datetime) as art_start_date 
		                        from patient p 
		                              inner join encounter e on p.patient_id=e.patient_id 
		                        where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
		                              group by p.patient_id 
		                        
		                        union 
		             
		                        select p.patient_id, min(value_datetime) art_start_date 
		                        from patient p 
		                              inner join encounter e on p.patient_id=e.patient_id 
		                              inner join obs o on e.encounter_id=o.encounter_id 
		                        where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
		                              and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
		                              group by p.patient_id
		                  ) 
		            art_start group by patient_id 
		      ) tx_new where art_start_date between :startDate and :endDate and art_start_date < '2023-12-21'
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
		                                    where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
		                                          group by p.patient_id 
		                                    
		                                    union 
		                         
		                                    select p.patient_id, min(value_datetime) art_start_date 
		                                    from patient p 
		                                          inner join encounter e on p.patient_id=e.patient_id 
		                                          inner join obs o on e.encounter_id=o.encounter_id 
		                                    where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
		                                          and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
		                                          group by p.patient_id
		                              ) 
		                        art_start group by patient_id 
		                  ) tx_new where art_start_date between :startDate and :endDate and art_start_date >= '2023-12-21'
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
		                                          and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location 
		                                          group by p.patient_id 
		                                    union 
		                                    
		                                    select p.patient_id, min(value_datetime) art_start_date 
		                                    from patient p 
		                                          inner join encounter e on p.patient_id=e.patient_id 
		                                          inner join obs o on e.encounter_id=o.encounter_id 
		                                    where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
		                                          and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
		                                          group by p.patient_id 
		                                    
		                                    union 
		                         
		                                    select pg.patient_id, min(date_enrolled) art_start_date 
		                                    from patient p 
		                                          inner join patient_program pg on p.patient_id=pg.patient_id 
		                                    where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
		                                          group by pg.patient_id 
		                              ) 
		                        art_start group by patient_id 
		                  ) tx_new where art_start_date < '2023-12-21'
		            ) tx_new_period_anterior on tx_new.patient_id = tx_new_period_anterior.patient_id
		             where tx_new_period_anterior.patient_id is null
		            ) tx_new
		      )tx_new
				inner join person pe on pe.person_id = tx_new.patient_id
				where (pe.birthdate is not null and floor(datediff(tx_new.art_start_date,pe.birthdate )/365) >= 5) or pe.birthdate is  null
		) tx_new
		left join
		(     
	    select * from 
       (
       select first_cd4.patient_id, first_cd4.data_cd4 data_cd4,first_cd4_less.data_cd4__less
		     from (
		      	select first_cd4.patient_id, first_cd4.data_cd4 data_cd4
		      	from( 
						select p.patient_id, lastCD4.obs_datetime data_cd4
					from patient p
						inner join encounter e on e.patient_id = p.patient_id
					   	inner join obs lastCD4 on lastCD4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and lastCD4.voided is false and e.encounter_type = 53 and e.location_id=:location 
						and lastCD4.concept_id = 1695 and lastCD4.value_numeric >= 200 and lastCD4.obs_datetime <= :endDate
					 
					union

					Select p.patient_id, o.obs_datetime as data_cd4  From patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165515 and  e.encounter_type = 53 and o.value_coded = 1254 
						and e.location_id=:location and o.obs_datetime <= :endDate

					union
					
					select p.patient_id, artStartDate.value_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4ArtStart on cd4ArtStart.encounter_id = e.encounter_id
					   inner join obs artStartDate on artStartDate.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4ArtStart.voided is false and artStartDate.voided is false and e.encounter_type = 53 and e.location_id=:location 
					   and cd4ArtStart.concept_id = 23896 and cd4ArtStart.value_numeric >=200 and artStartDate.concept_id = 1190 and artStartDate.value_datetime <= :endDate
					  
					union

					
					select p.patient_id, artStartDate.value_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4ArtStart on cd4ArtStart.encounter_id = e.encounter_id
					   inner join obs artStartDate on artStartDate.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4ArtStart.voided is false and artStartDate.voided is false and e.encounter_type = 53 and e.location_id=:location 
					   and cd4ArtStart.concept_id = 165519 and cd4ArtStart.value_coded=1254 and artStartDate.concept_id = 1190 and artStartDate.value_datetime <= :endDate

					   union
					
					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 6 and e.location_id=:location 
					   and cd4.concept_id = 1695 and cd4.value_numeric >=200 and e.encounter_datetime <= :endDate
					  
					union

					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 6 and e.location_id=:location 
					   and cd4.concept_id = 165515 and cd4.value_coded=1254 and e.encounter_datetime <= :endDate

					 union
					
					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 13 and e.location_id=:location 
					   and cd4.concept_id = 1695 and cd4.value_numeric >=200 and e.encounter_datetime <= :endDate
					 
					union

					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 13 and e.location_id=:location 
					   and cd4.concept_id = 165515 and cd4.value_coded = 1254 and e.encounter_datetime <= :endDate

					union
					
					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 51  and e.location_id=:location
					   and cd4.concept_id = 1695 and cd4.value_numeric >=200 and e.encounter_datetime <= :endDate

					   union

					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 51 and e.location_id=:location 
					   and cd4.concept_id = 165515 and cd4.value_coded = 1254 and e.encounter_datetime <= :endDate
		      	) first_cd4  
		      ) first_cd4 
		      left join
		      ( 
		      	  select tx_new.patient_id,tx_new.art_start_date,first_cd4_less.data_cd4__less from  
	                    (
	                  	select tx_new.patient_id, tx_new.art_start_date  from  
			          (
				       select tx_new.patient_id, tx_new.art_start_date from 
				      (
				            select patient_id, min(art_start_date) art_start_date 
			            from 
			                  (
			                        select p.patient_id, min(e.encounter_datetime) art_start_date 
			                        from patient p 
			                              inner join encounter e on p.patient_id=e.patient_id 
			                              inner join obs o on o.encounter_id=e.encounter_id 
			                        where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
			                              and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location 
			                              group by p.patient_id 
			                        union 
			                        
			                        select p.patient_id, min(value_datetime) art_start_date 
			                        from patient p 
			                              inner join encounter e on p.patient_id=e.patient_id 
			                              inner join obs o on e.encounter_id=o.encounter_id 
			                        where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
			                              and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
			                              group by p.patient_id 
			                        
			                        union 
			             
			                        select pg.patient_id, min(date_enrolled) art_start_date 
			                        from patient p 
			                              inner join patient_program pg on p.patient_id=pg.patient_id 
			                        where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
			                              group by pg.patient_id 
			                        
			                        union 
			      
			                        select e.patient_id, min(e.encounter_datetime) as art_start_date 
			                        from patient p 
			                              inner join encounter e on p.patient_id=e.patient_id 
			                        where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
			                              group by p.patient_id 
			                        
			                        union 
			             
			                        select p.patient_id, min(value_datetime) art_start_date 
			                        from patient p 
			                              inner join encounter e on p.patient_id=e.patient_id 
			                              inner join obs o on e.encounter_id=o.encounter_id 
			                        where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
			                              and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
			                              group by p.patient_id
			                  ) 
			            art_start group by patient_id 
			      ) tx_new where art_start_date between :startDate and :endDate and art_start_date < '2023-12-21'
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
			                                    where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
			                                          group by p.patient_id 
			                                    
			                                    union 
			                         
			                                    select p.patient_id, min(value_datetime) art_start_date 
			                                    from patient p 
			                                          inner join encounter e on p.patient_id=e.patient_id 
			                                          inner join obs o on e.encounter_id=o.encounter_id 
			                                    where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
			                                          and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
			                                          group by p.patient_id
			                              ) 
			                        art_start group by patient_id 
			                  ) tx_new where art_start_date between :startDate and :endDate and art_start_date >= '2023-12-21'
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
			                                          and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location 
			                                          group by p.patient_id 
			                                    union 
			                                    
			                                    select p.patient_id, min(value_datetime) art_start_date 
			                                    from patient p 
			                                          inner join encounter e on p.patient_id=e.patient_id 
			                                          inner join obs o on e.encounter_id=o.encounter_id 
			                                    where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
			                                          and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
			                                          group by p.patient_id 
			                                    
			                                    union 
			                         
			                                    select pg.patient_id, min(date_enrolled) art_start_date 
			                                    from patient p 
			                                          inner join patient_program pg on p.patient_id=pg.patient_id 
			                                    where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
			                                          group by pg.patient_id 
			                              ) 
			                        art_start group by patient_id 
			                  ) tx_new where art_start_date < '2023-12-21'
			            ) tx_new_period_anterior on tx_new.patient_id = tx_new_period_anterior.patient_id
			             where tx_new_period_anterior.patient_id is null
			            ) tx_new
			         )tx_new
					inner join person pe on pe.person_id = tx_new.patient_id
					where (pe.birthdate is not null and floor(datediff(tx_new.art_start_date,pe.birthdate )/365) >= 5) or pe.birthdate is  null
                  )tx_new
                  inner join
                  (
                  select first_cd4_less.patient_id, first_cd4_less.data_cd4 data_cd4__less
		      	from( 
					select p.patient_id, lastCD4.obs_datetime data_cd4
					from patient p
						inner join encounter e on e.patient_id = p.patient_id
					   	inner join obs lastCD4 on lastCD4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and lastCD4.voided is false and e.encounter_type = 53 and e.location_id=:location 
						and lastCD4.concept_id = 1695 and lastCD4.value_numeric <200 and lastCD4.obs_datetime <= :endDate
					
					union
					
					Select p.patient_id, o.obs_datetime as data_cd4  From patient p 
						inner join encounter e on p.patient_id=e.patient_id 
						inner join obs o on e.encounter_id=o.encounter_id 
					where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165515 and  e.encounter_type = 53 and o.value_coded = 165513 
						and e.location_id=:location and o.obs_datetime <= :endDate 

					union
					
					select p.patient_id, artStartDate.value_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4ArtStart on cd4ArtStart.encounter_id = e.encounter_id
					   inner join obs artStartDate on artStartDate.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4ArtStart.voided is false and artStartDate.voided is false and e.encounter_type = 53 and e.location_id=:location 
					   and cd4ArtStart.concept_id = 23896 and cd4ArtStart.value_numeric <200 and artStartDate.concept_id = 1190 and artStartDate.value_datetime <= :endDate
					
					union

					select p.patient_id, artStartDate.value_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4ArtStart on cd4ArtStart.encounter_id = e.encounter_id
					   inner join obs artStartDate on artStartDate.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4ArtStart.voided is false and artStartDate.voided is false and e.encounter_type = 53 and e.location_id=:location 
					   and cd4ArtStart.concept_id = 165519 and cd4ArtStart.value_coded=165513 and artStartDate.concept_id = 1190 and artStartDate.value_datetime <= :endDate

					union
					
					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 6 and e.location_id=:location 
					   and cd4.concept_id = 1695 and cd4.value_numeric <200 and e.encounter_datetime <= :endDate
					
					union

					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 6 and e.location_id=:location 
					   and cd4.concept_id = 165515 and cd4.value_coded=165513 and e.encounter_datetime <= :endDate

					   union
					
					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 13 and e.location_id=:location 
					   and cd4.concept_id = 1695 and cd4.value_numeric <200 and e.encounter_datetime <= :endDate
					 
					union

					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 13 and e.location_id=:location 
					   and cd4.concept_id = 165515 and cd4.value_coded = 165513 and e.encounter_datetime <= :endDate
					
					union
					
					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 51 and e.location_id=:location 
					   and cd4.concept_id = 1695 and cd4.value_numeric <200 and e.encounter_datetime <= :endDate

					   union

					select p.patient_id, e.encounter_datetime data_cd4
					from patient p
					   inner join encounter e on e.patient_id = p.patient_id
					   inner join obs cd4 on cd4.encounter_id = e.encounter_id
					where p.voided is false and e.voided is false and cd4.voided is false and e.encounter_type = 51 and e.location_id=:location 
					   and cd4.concept_id = 165515 and cd4.value_coded = 165513 and e.encounter_datetime <= :endDate
		      	) first_cd4_less 
		      	)first_cd4_less on first_cd4_less.patient_id=tx_new.patient_id
		      	where (first_cd4_less.data_cd4__less between date_add(tx_new.art_start_date, interval - 90 day)  and date_add(tx_new.art_start_date, interval 28 day))
		      ) first_cd4_less on first_cd4_less.patient_id = first_cd4.patient_id  
		      where (first_cd4.data_cd4 < first_cd4_less.data_cd4__less or first_cd4_less.data_cd4__less is null) 
		     ) first_cd4
			) first_cd4 on first_cd4.patient_id = tx_new.patient_id
			)tx_new
		     where (tx_new.data_cd4 between date_add(tx_new.art_start_date, interval - 90 day)  and date_add(tx_new.art_start_date, interval 28 day)) 
			group by tx_new.patient_id 
			)tx_new 