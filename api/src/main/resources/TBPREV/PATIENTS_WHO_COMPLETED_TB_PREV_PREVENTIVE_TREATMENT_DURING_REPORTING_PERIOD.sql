select inicio_3HP.patient_id
from(
		select inicio_3HP.patient_id, max( inicio_3HP.data_final_3HP) 
		from (
			select inicio_3HP.patient_id, final3hp.data_final_3HP
			from (
				select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_3HP 
				from ( 
					select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
					where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
						and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
						and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)
					group by p.patient_id 
							
					union
					
					select p.patient_id,min(outrasPrescricoesDT3HP.obs_datetime) data_inicio_tpi  from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
					where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) 
						 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
					group by p.patient_id  
					
					union
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 			 
						inner join encounter e on p.patient_id=e.patient_id																				 			 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 			 
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
					group by p.patient_id
					
					union
					(
					 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
					 		(	select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 
					 				inner join encounter e on p.patient_id=e.patient_id																				 
					 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
					 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 
					 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
					 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
					 			group by p.patient_id
							
					 			union 	
								
					 			select p.patient_id,min(regime3HP.obs_datetime) data_inicio_tpi from patient p														 		 
									inner join encounter e on p.patient_id=e.patient_id																				 		 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
										and seguimentoTPT.concept_id =23987  																						
										and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
										and seguimentoTPT.voided =0)							 
								where e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	         
									and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
									and seguimentoTPT.obs_id is null					 
								group by p.patient_id	 																													 
					   		
					   		) inicio
					   		left join 																																 
					     	(	
					     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
								where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month) 
									 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
						         union
					         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
									inner join encounter e on p.patient_id = e.patient_id 
									inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
									inner join obs estado on estado.encounter_id = e.encounter_id 
								where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
									 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
									 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)
						         union
						         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
								where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
									and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
								) 
					         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
					     		where inicioAnterior.patient_id is null																									 
					 )
					) 
				inicio_3HP group by inicio_3HP.patient_id
				) 
			inicio_3HP	
			inner join 
			(
				select patient_id, max(data_final_3HP) data_final_3HP
				from 
				(
					/*
						Patients who have Última profilaxia TPT with value “3HP” and Data de Fim da Profilaxia TPT 
						registered in Ficha Resumo - Mastercard and at least 86  days apart from the 3HP Start Date
					*/
					select 	p.patient_id,max(obs3HPCompleted.obs_datetime) data_final_3HP  																		
					from 	patient p  																													
							inner join encounter e on p.patient_id=e.patient_id  																			
							inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
							inner join obs obs3HPCompleted on obs3HPCompleted.encounter_id=e.encounter_id 
					where 	e.voided=0 and p.voided=0 and obs3HPCompleted.obs_datetime BETWEEN (:startDate - interval 6 month) and :endDate and  										
							obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type in (6,53) and  
							e.location_id=:location and obs3HPCompleted.voided=0 and obs3HPCompleted.concept_id=165308 and obs3HPCompleted.value_coded = 1267 		
					group by p.patient_id
							
				) final group by final.patient_id				
			) final3hp on final3hp.patient_id = inicio_3HP.patient_id and final3hp.data_final_3hp >= inicio_3HP.data_inicio_3HP + interval 86 day 
			
		union
		
		select inicio_3HP.patient_id, e.encounter_datetime data_final_3HP
		from (
				select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_3HP 
				from ( 
					select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
					where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
						and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
						and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)
					group by p.patient_id 
							
					union
					
					select p.patient_id,min(outrasPrescricoesDT3HP.obs_datetime) data_inicio_tpi  from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
					where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) 
						 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
					group by p.patient_id  
					
					union
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 			 
						inner join encounter e on p.patient_id=e.patient_id																				 			 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 			 
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
					group by p.patient_id
					
					union
					(
					 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
					 		(	select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 
					 				inner join encounter e on p.patient_id=e.patient_id																				 
					 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
					 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 
					 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
					 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
					 			group by p.patient_id
							
					 			union 	
								
					 			select p.patient_id,min(regime3HP.obs_datetime) data_inicio_tpi from patient p														 		 
									inner join encounter e on p.patient_id=e.patient_id																				 		 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
										and seguimentoTPT.concept_id =23987  																						
										and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
										and seguimentoTPT.voided =0)							 
								where e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	         
									and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
									and seguimentoTPT.obs_id is null					 
								group by p.patient_id	 																													 
					   		
					   		) inicio
					   		left join 																																 
					     	(	
					     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
								where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month) 
									 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
						         union
					         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
									inner join encounter e on p.patient_id = e.patient_id 
									inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
									inner join obs estado on estado.encounter_id = e.encounter_id 
								where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
									 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
									 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)
						         union
						         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
								where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
									and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
								) 
					         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
					     		where inicioAnterior.patient_id is null																									 
					 )
					) 
				inicio_3HP group by inicio_3HP.patient_id
			) inicio_3HP
			inner join encounter e on e.patient_id = inicio_3HP.patient_id																				 		
			inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																				
			inner join obs dispensaTrimestral on dispensaTrimestral.encounter_id = e.encounter_id																
		where e.voided=0 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 
			and  e.location_id=:location and dispensaTrimestral.voided =0 and dispensaTrimestral.concept_id =23986 and dispensaTrimestral.value_coded=23720
			and e.encounter_datetime between inicio_3HP.data_inicio_3HP and  (inicio_3HP.data_inicio_3HP + interval 4 month) 
			
		union
		
		select inicio_3HP.patient_id, e.encounter_datetime data_final_3HP
		from (
				select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_3HP 
				from ( 
					select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
					where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
						and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
						and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)
					group by p.patient_id 
							
					union
					
					select p.patient_id,min(outrasPrescricoesDT3HP.obs_datetime) data_inicio_tpi  from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
					where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) 
						 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
					group by p.patient_id  
					
					union
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 			 
						inner join encounter e on p.patient_id=e.patient_id																				 			 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 			 
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
					group by p.patient_id
					
					union
					(
					 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
					 		(	select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 
					 				inner join encounter e on p.patient_id=e.patient_id																				 
					 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
					 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 
					 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
					 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
					 			group by p.patient_id
							
					 			union 	
								
					 			select p.patient_id,min(regime3HP.obs_datetime) data_inicio_tpi from patient p														 		 
									inner join encounter e on p.patient_id=e.patient_id																				 		 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
										and seguimentoTPT.concept_id =23987  																						
										and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
										and seguimentoTPT.voided =0)							 
								where e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	         
									and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
									and seguimentoTPT.obs_id is null					 
								group by p.patient_id	 																													 
					   		
					   		) inicio
					   		left join 																																 
					     	(	
					     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
								where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month) 
									 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
						         union
					         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
									inner join encounter e on p.patient_id = e.patient_id 
									inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
									inner join obs estado on estado.encounter_id = e.encounter_id 
								where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
									 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
									 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)
						         union
						         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
								where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
									and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
								) 
					         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
					     		where inicioAnterior.patient_id is null																									 
					 )
					) 
				inicio_3HP group by inicio_3HP.patient_id
			) inicio_3HP
			inner join encounter e on inicio_3HP.patient_id=e.patient_id
				inner join obs regimeTPT on regimeTPT.encounter_id=e.encounter_id		 																				
				inner join obs dispensaMensal on dispensaMensal.encounter_id=e.encounter_id																
			where 		e.voided=0 and e.encounter_datetime between inicio_3HP.data_inicio_3HP and  (inicio_3HP.data_inicio_3HP + interval 4 month)	 			  									
						and regimeTPT.voided=0 and regimeTPT.concept_id=23985 and regimeTPT.value_coded in (23954,23984) and e.encounter_type=60 
						and  e.location_id=:location	  		
						and dispensaMensal.voided =0 and dispensaMensal.concept_id =23986 and dispensaMensal.value_coded=1098 							
			group by inicio_3HP.patient_id
			having count(e.encounter_id)>=3
		
		union
		
		select inicio_3HP.patient_id, e.encounter_datetime data_final_3HP
		from (
			select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_3HP 
			from ( 
				select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p 
					inner join encounter e on p.patient_id = e.patient_id 
					inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
					inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
				where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
					and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
					and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)
				group by p.patient_id 
						
				union
				
				select p.patient_id,min(outrasPrescricoesDT3HP.obs_datetime) data_inicio_tpi  from patient p 
					inner join encounter e on p.patient_id = e.patient_id 
					inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
				where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) 
					 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
				group by p.patient_id  
				
				union
				
				select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 			 
					inner join encounter e on p.patient_id=e.patient_id																				 			 
					inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
					inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
				where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 			 
					and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
					and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
				group by p.patient_id
				
				union
				(
				 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
				 		(	select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 
				 				inner join encounter e on p.patient_id=e.patient_id																				 
				 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
				 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
				 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 
				 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
				 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
				 			group by p.patient_id
						
				 			union 	
							
				 			select p.patient_id,min(regime3HP.obs_datetime) data_inicio_tpi from patient p														 		 
								inner join encounter e on p.patient_id=e.patient_id																				 		 
								inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
								left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)							 
							where e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	         
								and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
								and seguimentoTPT.obs_id is null					 
							group by p.patient_id	 																													 
				   		
				   		) inicio
				   		left join 																																 
				     	(	
				     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
							where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month) 
								 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
					         union
				         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
								inner join encounter e on p.patient_id = e.patient_id 
								inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
								inner join obs estado on estado.encounter_id = e.encounter_id 
							where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
								 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
								 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)
					         union
					         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
								and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
							) 
				         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
				     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
				     		where inicioAnterior.patient_id is null																									 
				 )
				) 
			inicio_3HP group by inicio_3HP.patient_id
			) inicio_3HP
			inner join encounter e on e.patient_id = inicio_3HP.patient_id  																		
			inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id  																				
		where e.voided=0 and outrasPrescricoesDT3HP.voided=0 and e.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + interval 4 month)							
			and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type  = 6 
			and  e.location_id=:location
		
		union
		
		select inicio_3HP.patient_id, fim.encounter_datetime data_final_3HP
		from (
			select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_3HP 
				from ( 
					select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
					where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
						and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
						and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)
					group by p.patient_id 
							
					union
					
					select p.patient_id,min(outrasPrescricoesDT3HP.obs_datetime) data_inicio_tpi  from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
					where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) 
						 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
					group by p.patient_id  
					
					union
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 			 
						inner join encounter e on p.patient_id=e.patient_id																				 			 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 			 
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
					group by p.patient_id
					
					union
					(
					 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
					 		(	select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 
					 				inner join encounter e on p.patient_id=e.patient_id																				 
					 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
					 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 
					 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
					 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
					 			group by p.patient_id
							
					 			union 	
								
					 			select p.patient_id,min(regime3HP.obs_datetime) data_inicio_tpi from patient p														 		 
									inner join encounter e on p.patient_id=e.patient_id																				 		 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
										and seguimentoTPT.concept_id =23987  																						
										and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
										and seguimentoTPT.voided =0)							 
								where e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	         
									and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
									and seguimentoTPT.obs_id is null					 
								group by p.patient_id	 																													 
					   		
					   		) inicio
					   		left join 																																 
					     	(	
					     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
								where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month) 
									 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
						         union
					         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
									inner join encounter e on p.patient_id = e.patient_id 
									inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
									inner join obs estado on estado.encounter_id = e.encounter_id 
								where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
									 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
									 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)
						         union
						         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
								where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
									and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
								) 
					         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
					     		where inicioAnterior.patient_id is null																									 
					 )
					) 
				inicio_3HP group by inicio_3HP.patient_id
			) inicio_3HP

			inner join 
			(
				select  distinct  p.patient_id,e.encounter_datetime,e.encounter_id  from patient p
	            inner join encounter e on e.patient_id = p.patient_id
				inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id		 																				
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id																
			 where e.voided=0 and profilaxia3HP.voided=0 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded = 23954 and e.encounter_type=6 and  e.location_id=:location	
				   and estadoProfilaxia.voided =0 and estadoProfilaxia.concept_id =165308 and estadoProfilaxia.value_coded in (1256,1257) and p.voided=0	
	        ) fim on fim.patient_id=inicio_3HP.patient_id
	            where fim.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + interval 4 month)
	            group by inicio_3HP.patient_id
				having count(fim.encounter_id)>=3
				
			) inicio_3HP group by inicio_3HP.patient_id
	)inicio_3HP

union 

select patient_id 
from(
	select fimINH.patient_id, max(fimINH.data_fim_INH)
	from (
		select inicio_INH.patient_id, fimINH.data_fim_INH
		from (
		
				/*
				   ● Patients who have (Última profilaxia TPT with value “Isoniazida (INH)” and
						Data de Fim da Profilaxia TPTselected) until reporting end date registered
						in Ficha Resumo - Mastercard and at least 173 days apart from the INH start
						date or
					
					● Patients who have Profilaxia TPT with the value “Isoniazida (INH)” with
						Data Fim marked “Profilaxia com INH – TPI (Data Fim)” until reporting end
						date marked in Ficha de Seguimento and at least 173 days apart from the
						INH start date or
						
					● Patients who have Profilaxia (INH) with the value “Fim (F)” or (Profilaxia
						TPT with the value “Isoniazida (INH)” and Estado da Profilaxia with the
						value “Fim (F)” marked in Ficha Clínica -– Mastercard and at least 173 days
						apart from the INH start date or
				 */
		
			select inicio_INH.patient_id,min(inicio_INH.data_inicio_INH) data_inicio_INH 
			from (
					select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime between(:startDate - interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
						group by p.patient_id
					
					union	
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
						group by p.patient_id	 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
									group by p.patient_id	 
								union
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         
									group by p.patient_id
							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime between(:startDate- INTERVAL 13 MONTH) and (:endDate - interval 7 month)  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month) and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH group by inicio_INH.patient_id
		) inicio_INH
		inner join 
		(  
			select p.patient_id,max(estadoFIM.obs_datetime) data_fim_INH 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
				inner join obs estadoFIM on estadoFIM.encounter_id = e.encounter_id 
			where e.voided=0 and p.voided=0 and profilaxiaINH.voided=0 and e.encounter_type in (6,9,53)and profilaxiaINH.concept_id=23985 and profilaxiaINH.value_coded=656
				and estadoFIM.concept_id=165308 and estadoFIM.value_coded=1267 and estadoFIM.voided=0
				and estadoFIM.obs_datetime between (:startDate - interval 6 month) and :endDate  and  e.location_id=:location
				group by p.patient_id	
		) fimINH on fimINH.patient_id = inicio_INH.patient_id and fimINH.data_fim_INH  >=inicio_INH.data_inicio_INH + interval 173 day
		
		union
		
		/*
		 *	 At least 5 consultations registered on Ficha Clínica or Ficha de
			Seguimento (Adulto or Pediatria) with INH (Profilaxia INH=
			Início/Continua) or (Profilaxia com INH-TPI = Yes) or (Profilaxia
			TPT=” Isoniazida (INH)” and Estado da
			Profilaxia=”Início(I)/Continua( C)”) until a 7-month period after
			the INH Start Date (not including the INH Start Date)
		 * 
		 * */
		select inicio_INH.patient_id, fimINH.data_fim_INH
		from(
			select inicio_INH.patient_id,min(inicio_INH.data_inicio_INH) data_inicio_INH 
			from (
					select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime between(:startDate- interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
						group by p.patient_id
					
					union	
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
						group by p.patient_id	 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
									group by p.patient_id	 
								union
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         
									group by p.patient_id
							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime between(:startDate- INTERVAL 13 MONTH) and (:endDate - interval 7 month)  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month) and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH group by inicio_INH.patient_id
		) inicio_INH
		inner join 
		(
			select 	p.patient_id, estadoProfilaxia.obs_datetime data_fim_INH,e.encounter_id	 																
			from 	patient p														 			  															
					inner join encounter e on p.patient_id=e.patient_id																				 		
					inner join obs profilaxiaINH on profilaxiaINH.encounter_id=e.encounter_id		 																				
					inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id=e.encounter_id																
			where 	e.voided=0 and p.voided=0 and estadoProfilaxia.obs_datetime between (:startDate - interval 6 month) and :endDate 			  									
					and profilaxiaINH.voided=0 and profilaxiaINH.concept_id=23985 and profilaxiaINH.value_coded in (656,23982) and e.encounter_type in (6,9) and  e.location_id=:location	  		
					and estadoProfilaxia.voided =0 and estadoProfilaxia.concept_id =165308 and estadoProfilaxia.value_coded in (1256,1257)			
			
		) fimINH on fimINH.patient_id=inicio_INH.patient_id
		where fimINH.data_fim_INH BETWEEN (inicio_INH.data_inicio_INH +interval 1 day) and (inicio_INH.data_inicio_INH + interval 7 month)
		group by inicio_INH.patient_id
		HAVING count(fimINH.encounter_id)>=5
		
		union
		 
		/*
		 * 		At least 6 FILT with INH Mensal (Regime de TPT=
				Isoniazida/’Isoniazida + Piridoxina’ and Tipo de Dispensa =
				Mensal) until a 7-month period from the INH Start Date (including
				the INH Start Date) or
				
				At least 2 FILT with DT-INH (Regime de TPT= Isoniazida/’Isoniazida
				+ Piridoxina’ and Tipo de Dispensa = Trimestral) until a 5-month
				period from the Start Date (including the INH Start Date):
		 * */
		select fimINHFilt.patient_id, data_fim_INH
		from 
		(select 	inicio_INH.patient_id,
				data_inicio_INH,
				e.encounter_datetime data_fim_INH,
				COUNT(DISTINCT CASE WHEN dispensa.value_coded = 1098 THEN encounter_datetime END) mensal,
				COUNT(DISTINCT CASE WHEN dispensa.value_coded = 23720 THEN encounter_datetime END) trimestral
		from 
		(
			select inicio_INH.patient_id,min(inicio_INH.data_inicio_INH) data_inicio_INH 
			from (
					select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime between(:startDate- interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
						group by p.patient_id
					
					union	
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
						group by p.patient_id	 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
									group by p.patient_id	 
								union
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         
									group by p.patient_id
							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime between(:startDate- INTERVAL 13 MONTH) and (:endDate - interval 7 month)  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month) and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH group by inicio_INH.patient_id
		) inicio_INH			
		inner join encounter e on inicio_INH.patient_id=e.patient_id 
		inner join obs regime on regime.encounter_id=e.encounter_id 
		inner join obs dispensa on dispensa.encounter_id=e.encounter_id
		where e.voided=0 and e.encounter_datetime BETWEEN inicio_INH.data_inicio_INH and  if(dispensa.value_coded=1098,(inicio_INH.data_inicio_INH +interval 7 month),(inicio_INH.data_inicio_INH +interval 5 month)) and  
					e.encounter_type=60 and e.location_id=:location and regime.voided=0 and 
					regime.concept_id=23985 and regime.value_coded in (656,23982) and 
					dispensa.voided=0 and dispensa.concept_id=23986 and dispensa.value_coded in (1098,23720)
		group by inicio_INH.patient_id
		) fimINHFilt
		where mensal>=6 or trimestral>=2
		
		union
		
		/*
					At least 2 consultations registered on Ficha Clínica with DT-INH
					(Profilaxia INH = Início/Continua and Outras Prescrições = DT-INH)
					or (Profilaxia TPT=”Isoniazida (INH)” and Estado da
					Profilaxia=“Início(I)/Continua( C) ” and Outras Prescrições = DT-
					INH ) until a 5-month period from the INH Start Date (including
					the INH Start Date)
		 * */
		select inicio_INH.patient_id, DTINH.data_fim_INH
		from(
			select inicio_INH.patient_id,min(inicio_INH.data_inicio_INH) data_inicio_INH 
			from (
					select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime between(:startDate- interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
						group by p.patient_id
					
					union	
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
						group by p.patient_id	 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
									group by p.patient_id	 
								union
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         
									group by p.patient_id
							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime between(:startDate- INTERVAL 13 MONTH) and (:endDate - interval 7 month)  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month) and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH group by inicio_INH.patient_id
		) inicio_INH
		inner join 
		(
			select p.patient_id,estadoProfilaxia.obs_datetime data_fim_INH, e.encounter_id																		
			from	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id
				inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id																			 		
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id=e.encounter_id
				inner join obs obsDTINH on obsDTINH.encounter_id=e.encounter_id	
			where e.voided=0 and p.voided=0 and estadoProfilaxia.obs_datetime BETWEEN (:startDate - interval 6 month) and :endDate 
				and profilaxiaINH.concept_id = 23985 and profilaxiaINH.value_coded = 656 and profilaxiaINH.voided = 0
				and estadoProfilaxia.voided=0 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded in (1256,1257)
				and obsDTINH.voided=0 and obsDTINH.concept_id=1719 and obsDTINH.value_coded=23955 and e.encounter_type=6 and  e.location_id=:location	  		
		) DTINH on DTINH.patient_id=inicio_INH.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_INH.data_inicio_INH and (inicio_INH.data_inicio_INH + interval 5 month)
		group by inicio_INH.patient_id
		having count(DTINH.encounter_id)>=2
		
		union
		
			/*	At least 2 FILT with DT-INH (Regime de TPT= Isoniazida/’Isoniazida + Piridoxina’ 
                and Tipo de Dispensa = Trimestral) until a 5-month period from the Start Date 
                (including the INH Start Date) */
		
		select inicio_INH.patient_id, DTINH.data_fim_INH
		from(
			select inicio_INH.patient_id,min(inicio_INH.data_inicio_INH) data_inicio_INH 
			from (
					select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime between(:startDate- interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
						group by p.patient_id
					
					union	
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
						group by p.patient_id	 																											 
					
					union
					
					(select inicio.patient_id, inicio.data_inicio_INH
						from ( 
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
									group by p.patient_id	 
								union
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         
									group by p.patient_id
							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime between(:startDate- INTERVAL 13 MONTH) and (:endDate - interval 7 month)  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0 
						      	and obsInicioINH.obs_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month) and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH group by inicio_INH.patient_id
		) inicio_INH
		inner join 
		(
			select p.patient_id,e.encounter_datetime data_fim_INH,e.encounter_id																		
			from	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id
				inner join obs regimeTPT on regimeTPT.encounter_id = e.encounter_id																			 		
				inner join obs tipoDispensa on tipoDispensa.encounter_id=e.encounter_id
				where e.encounter_type=60 and regimeTPT.concept_id=23985 and regimeTPT.value_coded in(656,23982) and tipoDispensa.concept_id=23986 and tipoDispensa.value_coded=23720
				and p.voided=0 and e.voided=0 and regimeTPT.voided=0 and tipoDispensa.voided=0 and e.encounter_datetime BETWEEN (:startDate - interval 6 month) and :endDate 

		) DTINH on DTINH.patient_id=inicio_INH.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_INH.data_inicio_INH and (inicio_INH.data_inicio_INH + interval 5 month) 
		group by inicio_INH.patient_id
		 having count(DTINH.encounter_id)>=2
		
		union
		
		/*
			At least 3 consultations registered on Ficha Clínica with INH
			((Profilaxia INH= (Início or Continua)) or (Profilaxia TPT=”
			Isoniazida (INH)” and Estado da Profilaxia=”Início(I)/Continua( C)”)
			+ 1 Ficha Clínica com DT-INH ((Profilaxia INH = Início/Continua
			and Outras Prescrições = DT-INH) or (Profilaxia TPT=”Isoniazida
			(INH)” and Estado da Profilaxia=“Início(I)/Continua( C)” and
			Outras Prescrições = DT-INH ) until a 7-month period from the INH
			Start Date (including INH Start Date) or
		 * */
		select startAndDT.patient_id, startAndDT.data_fim_INH
		from(
		select inicio_INH.patient_id, data_inicio_INH, DTINH.data_fim_INH
		from(
			select inicio_INH.patient_id,min(inicio_INH.data_inicio_INH) data_inicio_INH 
			from (
					select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime between(:startDate- interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
						group by p.patient_id
					
					union	
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
						group by p.patient_id	 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
									group by p.patient_id	 
								union
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         
									group by p.patient_id
							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime between(:startDate- INTERVAL 13 MONTH) and (:endDate - interval 7 month)  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month) and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH group by inicio_INH.patient_id
		) inicio_INH
		inner join 
		(
			select p.patient_id,estadoProfilaxia.obs_datetime data_fim_INH,e.encounter_id																		
			from	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id
				inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id																			 		
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id=e.encounter_id
			where e.voided=0 and p.voided=0 and estadoProfilaxia.obs_datetime BETWEEN (:startDate - interval 6 month) and :endDate 
				and profilaxiaINH.concept_id = 23985 and profilaxiaINH.value_coded = 656 and profilaxiaINH.voided = 0
				and estadoProfilaxia.voided=0 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded in (1256,1257)
				and e.encounter_type=6 and  e.location_id=:location	  
		) DTINH on DTINH.patient_id=inicio_INH.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_INH.data_inicio_INH and (inicio_INH.data_inicio_INH +interval 7 month)
		group by inicio_INH.patient_id
		having count(DTINH.encounter_id)>=3
		
		) startAndDT
		inner join
		(
			select p.patient_id,estadoProfilaxia.obs_datetime data_fim_INH																		
			from	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id
				inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id																			 		
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id=e.encounter_id
				inner join obs obsDTINH on obsDTINH.encounter_id=e.encounter_id	
			where e.voided=0 and p.voided=0 and estadoProfilaxia.obs_datetime BETWEEN (:startDate - interval 6 month) and :endDate 
				and profilaxiaINH.concept_id = 23985 and profilaxiaINH.value_coded = 656 and profilaxiaINH.voided = 0
				and estadoProfilaxia.voided=0 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded in (1256,1257)
				and obsDTINH.voided=0 and obsDTINH.concept_id=1719 and obsDTINH.value_coded=23955 and e.encounter_type=6 and  e.location_id=:location	  
		) dt on startAndDT.patient_id=dt.patient_id 
		where dt.data_fim_INH BETWEEN startAndDT.data_inicio_INH and (startAndDT.data_inicio_INH + interval 7 month)
		
		union
		
		/*
					At least 3 FILT with INH Mensal (Regime de TPT=
					Isoniazida/’Isoniazida + Piridoxina’ and Tipo de Dispensa =
					Mensal) + 1 FILT with DT-INH (Regime de TPT=
					Isoniazida/’Isoniazida + Piridoxina’ and Tipo de Dispensa =
					Trimestral) until a 7-month period from the INH Start Date
					(including INH Start Date)
		 * */
		select startAndDT.patient_id, startAndDT.data_fim_INH
		from (
		select inicio_INH.patient_id, data_inicio_INH, DTINH.data_fim_INH
		from(
			select inicio_INH.patient_id,min(inicio_INH.data_inicio_INH) data_inicio_INH 
			from (
					select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime between(:startDate- interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
						group by p.patient_id
					
					union	
					
					select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
						group by p.patient_id	 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
									group by p.patient_id	 
								union
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         
									group by p.patient_id
							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime between(:startDate- INTERVAL 13 MONTH) and (:endDate - interval 7 month)  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month) and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH group by inicio_INH.patient_id
		) inicio_INH
		inner join 
		(
			select 	p.patient_id, e.encounter_datetime data_fim_INH, e.encounter_id																		
			from 	patient p														 			  															
					inner join encounter e on p.patient_id=e.patient_id																				 		
					inner join obs obsInh on obsInh.encounter_id=e.encounter_id		 																				
					inner join obs dispensaMensal on dispensaMensal.encounter_id=e.encounter_id																
			where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and :endDate	 			  									
					and obsInh.voided=0 and obsInh.concept_id=23985 and obsInh.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location	  		
					and dispensaMensal.voided =0 and dispensaMensal.concept_id =23986 and dispensaMensal.value_coded=1098 							
		) DTINH on DTINH.patient_id=inicio_INH.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_INH.data_inicio_INH and (inicio_INH.data_inicio_INH + interval 7 month)
		group by inicio_INH.patient_id
		having count(DTINH.encounter_id)>=3
		) startAndDT
		inner join
		(
			select 	p.patient_id, e.encounter_datetime  data_fim_INH																		
			from 	patient p														 			  															
					inner join encounter e on p.patient_id=e.patient_id																				 		
					inner join obs obsInh on obsInh.encounter_id=e.encounter_id		 																				
					inner join obs dispensaMensal on dispensaMensal.encounter_id=e.encounter_id																
			where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and :endDate	 			  									
					and obsInh.voided=0 and obsInh.concept_id=23985 and obsInh.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location	  		
					and dispensaMensal.voided =0 and dispensaMensal.concept_id =23986 and dispensaMensal.value_coded=23720
		) dt on startAndDT.patient_id=dt.patient_id 
		where dt.data_fim_INH BETWEEN startAndDT.data_inicio_INH and (startAndDT.data_inicio_INH + interval 7 month)
	) fimINH group by fimINH.patient_id
) fimINH