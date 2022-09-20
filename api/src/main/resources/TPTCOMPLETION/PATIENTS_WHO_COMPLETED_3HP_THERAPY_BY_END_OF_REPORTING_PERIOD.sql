select inicio_3HP.patient_id  																																
  from																												 								
  	(	
		select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_3HP 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
	  	
	  	union

		select p.patient_id, e.encounter_datetime data_inicio_3HP																			
		from	patient p																													
			inner join encounter e on p.patient_id=e.patient_id																				
			inner join obs o on o.encounter_id=e.encounter_id																				
		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9) and o.concept_id=1719 and o.value_coded=165307 			
			and e.encounter_datetime < :endDate and e.location_id= :location
			
		union
		
		select p.patient_id,e.encounter_datetime data_inicio_3HP  																					
  		from patient p														 			  															
  			inner join encounter e on p.patient_id=e.patient_id																				 		
  			inner join obs o on o.encounter_id=e.encounter_id		 																				
  			inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																
  		where e.voided=0 and p.voided=0 and e.encounter_datetime < :endDate	 			  															
  			and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  		
  			and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 							
  																																
          union																																 		
  		
          select inicio.patient_id, inicio.data_inicio_3HP	 																						
  		from 																																		
  			(	select p.patient_id, e.encounter_datetime data_inicio_3HP																			
  				from	patient p																												 	
  					inner join encounter e on p.patient_id=e.patient_id																			 	
  					inner join obs o on o.encounter_id=e.encounter_id 																				
  					inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														
  				where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
  					and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267)  					
  					and	e.encounter_datetime < :endDate and e.location_id= :location 																
  				union 																																
  				select p.patient_id, e.encounter_datetime data_inicio_3HP																			
  				from	patient p																												 	
  					inner join encounter e on p.patient_id=e.patient_id																			 	
  					inner join obs o on o.encounter_id=e.encounter_id 																				
  					left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
  						and seguimentoTPT.concept_id =23987  																						
  						and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
  						and seguimentoTPT.voided =0)																			 					
  				where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
  					and	e.encounter_datetime < :endDate and e.location_id =:location 																
  					and seguimentoTPT.concept_id is null 																							
  			) inicio 																																		
  		
          left join

  		( 
  			select p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
  			from	patient p																												 		
  				inner join encounter e on p.patient_id=e.patient_id																			 		
  				inner join obs o on o.encounter_id=e.encounter_id 																					
  			where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  		
  				and	e.encounter_datetime < :endDate and e.location_id= :location

               union

               select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_3HP 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
			where p.voided = 0 and e.voided = 0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
				and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
				and e.encounter_type in (6,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
             	
             	union
             																																			
  			select p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
  			from	patient p																												 		
  				inner join encounter e on p.patient_id=e.patient_id																			 		
  				inner join obs o on o.encounter_id=e.encounter_id																			 		
  			where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type = 6 and o.concept_id=1719 and o.value_coded = 165307 		 		
  				and e.encounter_datetime < :endDate and e.location_id= :location 
  															
  		) inicio_anterior 																															
  			on inicio_anterior.patient_id = inicio.patient_id   																					
  			and inicio_anterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 	
  		where inicio_anterior.patient_id is null		 																																						
      ) inicio_3HP 																																		
      inner join 

    (   select endTPI.patient_id, endTPI.data_final_3hp   
        from 
            (   
              	select p.patient_id, estadoProfilaxia.obs_datetime data_final_3hp 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
			where p.voided = 0 and e.voided = 0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
				and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1267 
				and e.encounter_type in (6,53) and e.location_id=:location and estadoProfilaxia.obs_datetime <= :endDate
            ) endTPI group by endTPI.patient_id 
        
        ) termino_3hp on inicio_3HP.patient_id=termino_3hp.patient_id 
    where termino_3hp.data_final_3hp between inicio_3HP.data_inicio_3HP + interval 86 day  and  inicio_3HP.data_inicio_3HP + interval 365 day

  union 																																				
 
 select inicio_3HP.patient_id  																																
  from																												 								
  	(	
		select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_3HP 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type = 6 and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
		union

		select p.patient_id, e.encounter_datetime data_inicio_3HP																			
		from	patient p																													
			inner join encounter e on p.patient_id=e.patient_id																				
			inner join obs o on o.encounter_id=e.encounter_id																				
		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9) and o.concept_id=1719 and o.value_coded=165307 			
			and e.encounter_datetime < :endDate and e.location_id= :location
) inicio_3HP 																																		
      inner join encounter e on e.patient_id= inicio_3HP.patient_id                                                                                       
    	inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id=e.encounter_id                                                                                         
    where e.voided=0 and outrasPrescricoesDT3HP.voided=0                                                                                             	
    	and e.encounter_type = 6 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded = 165307   
    	and e.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + INTERVAL 4 month) and e.location_id= :location     	
    	group by inicio_3HP.patient_id having count( distinct e.encounter_id)>=1  
 
union

select inicio_3HP.patient_id  																																
  from																												 								
  	(	
		select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_3HP 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type = 6 and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
		union

		select p.patient_id, e.encounter_datetime data_inicio_3HP																			
		from	patient p																													
			inner join encounter e on p.patient_id=e.patient_id																				
			inner join obs o on o.encounter_id=e.encounter_id																				
		where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (6,9) and o.concept_id=1719 and o.value_coded=165307 			
			and e.encounter_datetime < :endDate and e.location_id= :location
	) 
inicio_3HP 																																		
      inner join 

    (   select p.patient_id,estadoProfilaxia.obs_datetime data_final_3hp,e.encounter_id 
		from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided = 0 and e.voided = 0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded in (1256,1257)
			and e.encounter_type  in( 6) and e.location_id=:location and estadoProfilaxia.obs_datetime <= :endDate
        
        ) termino_3hp on inicio_3HP.patient_id=termino_3hp.patient_id 
  	where termino_3hp.data_final_3hp between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + INTERVAL 4 MONTH)
      group by inicio_3HP.patient_id having count(distinct termino_3hp.encounter_id)>=3
      
union
              
select inicio_3HP.patient_id  																																
from																												 								
  	(	
  		select p.patient_id,e.encounter_datetime data_inicio_3HP  																					
  		from patient p														 			  															
  			inner join encounter e on p.patient_id=e.patient_id																				 		
  			inner join obs o on o.encounter_id=e.encounter_id		 																				
  			inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																
  		where e.voided=0 and p.voided=0 and e.encounter_datetime < :endDate	 			  															
  			and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  		
  			and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 							
  																																
          union																																 		
  		
          select inicio.patient_id, inicio.data_inicio_3HP	 																						
  		from 																																		
  			(	select p.patient_id, e.encounter_datetime data_inicio_3HP																			
  				from	patient p																												 	
  					inner join encounter e on p.patient_id=e.patient_id																			 	
  					inner join obs o on o.encounter_id=e.encounter_id 																				
  					inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														
  				where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
  					and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267)  					
  					and	e.encounter_datetime < :endDate and e.location_id= :location 																
  				union 																																
  				select p.patient_id, e.encounter_datetime data_inicio_3HP																			
  				from	patient p																												 	
  					inner join encounter e on p.patient_id=e.patient_id																			 	
  					inner join obs o on o.encounter_id=e.encounter_id 																				
  					left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
  						and seguimentoTPT.concept_id =23987  																						
  						and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
  						and seguimentoTPT.voided =0)																			 					
  				where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
  					and	e.encounter_datetime < :endDate and e.location_id =:location 																
  					and seguimentoTPT.concept_id is null 																							
  			) inicio 																																		
  		
          left join

  		( 
  			select p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
  			from	patient p																												 		
  				inner join encounter e on p.patient_id=e.patient_id																			 		
  				inner join obs o on o.encounter_id=e.encounter_id 																					
  			where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  		
  				and	e.encounter_datetime < :endDate and e.location_id= :location

               union

               select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_3HP 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
			where p.voided = 0 and e.voided = 0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
				and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
				and e.encounter_type in (6,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
             	
             	union
             																																			
  			select p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
  			from	patient p																												 		
  				inner join encounter e on p.patient_id=e.patient_id																			 		
  				inner join obs o on o.encounter_id=e.encounter_id																			 		
  			where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type = 6 and o.concept_id=1719 and o.value_coded = 165307 		 		
  				and e.encounter_datetime < :endDate and e.location_id= :location 
  															
  		) inicio_anterior 																															
  			on inicio_anterior.patient_id = inicio.patient_id   																					
  			and inicio_anterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 	
  		where inicio_anterior.patient_id is null		 																																	
  	
      ) inicio_3HP 																																		
      inner join encounter e on e.patient_id= inicio_3HP.patient_id                                                                                       
    	inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id                                                                                         
    	inner join obs obsTipo on obsTipo.encounter_id=e.encounter_id                                                                                       
    where e.voided=0 and obs3hp.voided=0 and obsTipo.voided=0                                                                                            	
    	and e.encounter_type=60 and obs3hp.concept_id=23985 and obs3hp.value_coded in (23954,23984) and obsTipo.concept_id=23986 and obsTipo.value_coded=23720   
    	and e.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + INTERVAL 4 month) and e.location_id= :location     	
    	group by inicio_3HP.patient_id having count(distinct e.encounter_id)>=1    																			
            
union 																																				
            
select inicio_3HP.patient_id  																																
  from																												 								
  	(	
		select p.patient_id,e.encounter_datetime data_inicio_3HP  																					
  		from patient p														 			  															
  			inner join encounter e on p.patient_id=e.patient_id																				 		
  			inner join obs o on o.encounter_id=e.encounter_id		 																				
  			inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																
  		where e.voided=0 and p.voided=0 and e.encounter_datetime < :endDate	 			  															
  			and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  		
  			and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 							
  		  																																
          union																																 		
  		
          select inicio.patient_id, inicio.data_inicio_3HP	 																						
  		from 																																		
  			(	select p.patient_id, e.encounter_datetime data_inicio_3HP																			
  				from	patient p																												 	
  					inner join encounter e on p.patient_id=e.patient_id																			 	
  					inner join obs o on o.encounter_id=e.encounter_id 																				
  					inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														
  				where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
  					and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267)  					
  					and	e.encounter_datetime < :endDate and e.location_id= :location 																
  				union 																																
  				select p.patient_id, e.encounter_datetime data_inicio_3HP																			
  				from	patient p																												 	
  					inner join encounter e on p.patient_id=e.patient_id																			 	
  					inner join obs o on o.encounter_id=e.encounter_id 																				
  					left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
  						and seguimentoTPT.concept_id =23987  																						
  						and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
  						and seguimentoTPT.voided =0)																			 					
  				where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  	
  					and	e.encounter_datetime < :endDate and e.location_id =:location 																
  					and seguimentoTPT.concept_id is null 																							
  			) inicio 																																		
  		
          left join

  		( 
  			select p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
  			from	patient p																												 		
  				inner join encounter e on p.patient_id=e.patient_id																			 		
  				inner join obs o on o.encounter_id=e.encounter_id 																					
  			where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=60  and o.concept_id=23985 and o.value_coded in (23954,23984)  		
  				and	e.encounter_datetime < :endDate and e.location_id= :location

               union

               select p.patient_id, estadoProfilaxia.obs_datetime data_inicio_3HP 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
			where p.voided = 0 and e.voided = 0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
				and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded = 1256 
				and e.encounter_type in (6,53) and e.location_id=:location and estadoProfilaxia.obs_datetime < :endDate
             	
             	union
             																																			
  			select p.patient_id, e.encounter_datetime data_inicio_3HP																			 	
  			from	patient p																												 		
  				inner join encounter e on p.patient_id=e.patient_id																			 		
  				inner join obs o on o.encounter_id=e.encounter_id																			 		
  			where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type = 6 and o.concept_id=1719 and o.value_coded = 165307 		 		
  				and e.encounter_datetime < :endDate and e.location_id= :location 
  															
  		) inicio_anterior 																															
  			on inicio_anterior.patient_id = inicio.patient_id   																					
  			and inicio_anterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 	
  		where inicio_anterior.patient_id is null		 														
      ) inicio_3HP 																																		
	inner join encounter e on e.patient_id= inicio_3HP.patient_id                                                                                       
	inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id                                                                                         
	inner join obs obsTipo on obsTipo.encounter_id=e.encounter_id                                                                                       
where e.voided=0 and obs3hp.voided=0 and obsTipo.voided=0                                                                                             
	and e.encounter_type=60 and obs3hp.concept_id=23985 and obs3hp.value_coded in (23954,23984) and obsTipo.concept_id=23986 and obsTipo.value_coded=1098  
	and e.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + INTERVAL 4 month) and e.location_id= :location    	
	group by inicio_3HP.patient_id having count(distinct e.encounter_id)>=3             																
