select patient_id 
from	(
		select patient_id, max(encounter_datetime)
		from (
				select * 
				from (
						select distinct cargaQuantitativa.patient_id, cargaQuantitativa.encounter_datetime, 1 as ordem 
						from	(
								select cargaQuantitativa.patient_id, cargaQuantitativa.encounter_datetime, cargaQuantitativa.ordem
								from	(
										select patient_id, max(encounter_datetime) encounter_datetime, ordem
										from	(
												select patient_id, encounter_datetime, ordem
												from (			
														select patient_id, encounter_datetime,ordem
														from (			
														select f.patient_id, max(f.encounter_datetime) encounter_datetime,f.ordem from (
														 select f.patient_id,max(f.encounter_datetime) encounter_datetime,f.ordem from (																
	
														 select p.patient_id, max(e.encounter_datetime)  encounter_datetime, 1 as ordem from patient p
																	inner join encounter e on e.patient_id = p.patient_id
																	inner join obs o on o.encounter_id = e.encounter_id
																where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 13 and o.concept_id = 856 
																		and e.location_id =:location and e.encounter_datetime between date_sub(:endDate, interval 12 month) and :endDate
																	group by p.patient_id
																union
																select p.patient_id, max(e.encounter_datetime)  encounter_datetime, 2 as ordem from patient p
																     inner join encounter e on e.patient_id = p.patient_id
																     inner join obs o on o.encounter_id = e.encounter_id
																 where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 51 and o.concept_id = 856 
																     and e.location_id =:location and e.encounter_datetime between date_sub(:endDate, interval 12 month) and :endDate
																     group by p.patient_id
																union
																select p.patient_id, max(e.encounter_datetime)  encounter_datetime, 3 as ordem from patient p
																     inner join encounter e on e.patient_id = p.patient_id
																     inner join obs o on o.encounter_id = e.encounter_id
																 where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 6 and o.concept_id = 856 
																     and e.location_id =:location and e.encounter_datetime between date_sub(:endDate, interval 12 month) and :endDate
															     group by p.patient_id
															     union
															   
															     select p.patient_id, max(o.obs_datetime)  encounter_datetime, 4 as ordem from patient p
																	inner join encounter e on e.patient_id = p.patient_id
																	inner join obs o on o.encounter_id = e.encounter_id
																where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 53 and o.concept_id = 856 
																		and e.location_id =:location and o.obs_datetime between date_sub(:endDate, interval 12 month) and :endDate
																	group by p.patient_id
															)f group by f.patient_id, encounter_datetime desc, ordem asc
															)f group by f.patient_id
														) cargaQuantitaviva group by patient_id
						   							) cargaQuantitaviva group by patient_id
											) cargaQuantitativa group by patient_id
									) cargaQuantitativa
								left join 
									(
								 		select patient_id, encounter_datetime, cargaQualitativa.ordem
										from (			
												select p.patient_id, e.encounter_datetime, 1 as ordem from patient p
													inner join encounter e on e.patient_id = p.patient_id
													inner join obs o on o.encounter_id = e.encounter_id
												where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 13 and o.concept_id = 1305 
														and e.location_id =:location and e.encounter_datetime between date_sub(:endDate, interval 12 month) and :endDate
												union
												select p.patient_id, e.encounter_datetime, 2 as ordem from patient p
												     inner join encounter e on e.patient_id = p.patient_id
												     inner join obs o on o.encounter_id = e.encounter_id
												 where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 51 and o.concept_id =1305 
												     and e.location_id =:location and e.encounter_datetime between date_sub(:endDate, interval 12 month) and :endDate
												union
												select p.patient_id, e.encounter_datetime  encounter_datetime, 3 as ordem from patient p
												     inner join encounter e on e.patient_id = p.patient_id
												     inner join obs o on o.encounter_id = e.encounter_id
												 where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 6 and o.concept_id = 1305 
												     and e.location_id =:location and e.encounter_datetime between date_sub(:endDate, interval 12 month) and :endDate
												union
											     select p.patient_id, o.obs_datetime obs_datetime, 4 as ordem from patient p
									               	inner join encounter e on e.patient_id = p.patient_id
									              		inner join obs o on o.encounter_id = e.encounter_id
									           	where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 53 and o.concept_id = 1305 
									               	and e.location_id =:location and o.obs_datetime between date_sub(:endDate, interval 12 month) and :endDate
											) cargaQualitativa
								 	)  cargaQualitativa on cargaQuantitativa.patient_id = cargaQualitativa.patient_id 
			       				     where cargaQualitativa.patient_id is null  
			       					or ( cargaQuantitativa.encounter_datetime > cargaQualitativa.encounter_datetime) 
			       					or ( cargaQuantitativa.encounter_datetime = cargaQualitativa.encounter_datetime and cargaQuantitativa.ordem <= cargaQualitativa.ordem )  
							) cargaQuantitativa
							join 
					        (
					          select f.patient_id,f.encounter_datetime,obCv.value_numeric
                              from (  
							    select p.patient_id, max(o.obs_datetime) encounter_datetime from  patient p
								inner join encounter e on e.patient_id = p.patient_id 
								inner join obs o on o.encounter_id = e.encounter_id
								where e.voided = 0 and o.voided =0 and e.encounter_type in (13, 51, 6,53) and o.concept_id = 856 and e.location_id =:location  
								and o.obs_datetime between date_sub(:endDate, interval 12 month) and :endDate 
                                group by p.patient_id
                                ) f 
                                inner join obs obCv on obCv.person_id=f.patient_id and obCv.concept_id=856 and obCv.value_numeric>=1000 and obCv.obs_datetime=f.encounter_datetime and obCv.voided=0
					
						     ) filter on filter.patient_id=cargaQuantitativa.patient_id
						
					    union
						
						select cargaQuantitativa.patient_id, cargaQuantitativa.obs_datetime encounter_datetime , 2 as ordem
						from (
								select cargaQuantitativa.patient_id, cargaQuantitativa.obs_datetime
						   		from	(
						          		select p.patient_id, max(o.obs_datetime)  obs_datetime from patient p
						             			inner join encounter e on e.patient_id = p.patient_id
						               		inner join obs o on o.encounter_id = e.encounter_id
						          	 	where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 53 and o.concept_id = 856 
						               		and e.location_id = :location and o.obs_datetime between date_sub(:endDate, interval 12 month) and :endDate
						               		group by p.patient_id
						       		) 
						       	cargaQuantitativa
						       	left join
						       		(   
						           		select p.patient_id, o.obs_datetime obs_datetime from patient p
						               		inner join encounter e on e.patient_id = p.patient_id
						               		inner join obs o on o.encounter_id = e.encounter_id
						           		where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  = 53 and o.concept_id = 1305 
						              	 		and e.location_id =:location and o.obs_datetime between date_sub(:endDate, interval 12 month) and :endDate
										union
						          		select p.patient_id, e.encounter_datetime encounter_datetime from patient p
							 				inner join encounter e on e.patient_id = p.patient_id
							          		inner join obs o on o.encounter_id = e.encounter_id
							      		where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type  in(13,51, 6) and o.concept_id = 1305 
							      			and e.location_id =:location and e.encounter_datetime between date_sub(:endDate, interval 12 month) and :endDate       	
						       		)   
						       	cargaQualitativa on cargaQuantitativa.patient_id = cargaQualitativa.patient_id 
						   		where cargaQualitativa.patient_id is null  or cargaQuantitativa.obs_datetime >= cargaQualitativa.obs_datetime
							) cargaQuantitativa
							join 
					        (
					          select f.patient_id,f.encounter_datetime,obCv.value_numeric
                              from (  
							    select p.patient_id, max(o.obs_datetime) encounter_datetime from  patient p
								inner join encounter e on e.patient_id = p.patient_id 
								inner join obs o on o.encounter_id = e.encounter_id
								where e.voided = 0 and o.voided =0 and e.encounter_type in (13, 51, 6,53) and o.concept_id = 856 and e.location_id =:location  
								and o.obs_datetime between date_sub(:endDate, interval 12 month) and :endDate 
                                group by p.patient_id
								   ) f 
                                inner join obs obCv on obCv.person_id=f.patient_id and obCv.concept_id=856 and obCv.value_numeric>=1000 and obCv.obs_datetime=f.encounter_datetime and obCv.voided=0
					
						     ) filter on filter.patient_id=cargaQuantitativa.patient_id 
				     ) cargaQuantitativa group by patient_id, encounter_datetime desc, ordem asc 
			) cargaQuantitativa group by patient_id
	) cargaQuantitativa
