select fim_3hp.patient_id 
from
(
select inicio_3HP.patient_id, max( inicio_3HP.data_final_3HP) 
		from (
			select inicio_3HP.patient_id, final3hp.data_final_3HP
			from (
				select inicio_3HP.patient_id,inicio_3HP.data_inicio_tpi data_inicio_3HP 
				from ( 
					select p.patient_id,estadoProfilaxia.obs_datetime data_inicio_tpi from  patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
					where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
						and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
						and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime <:endDate
							
					union
					
					select p.patient_id,outrasPrescricoesDT3HP.obs_datetime data_inicio_tpi  from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
					where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime <:endDate 
						 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
					
					union
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 			 
						inner join encounter e on p.patient_id=e.patient_id																				 			 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime<:endDate	 			 
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
					
					union
					(
					 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
					 		(	select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 
					 				inner join encounter e on p.patient_id=e.patient_id																				 
					 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
					 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 
					 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
					 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 

							
					 			union 	
								
					 			select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p														 		 
									inner join encounter e on p.patient_id=e.patient_id																				 		 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
										and seguimentoTPT.concept_id =23987  																						
										and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
										and seguimentoTPT.voided =0)							 
								where e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate	         
									and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
									and seguimentoTPT.obs_id is null					  																													 
					   		
					   		) inicio
					   		left join 																																 
					     	(	
					     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
								where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate 
									 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
						         union
					         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
									inner join encounter e on p.patient_id = e.patient_id 
									inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
									inner join obs estado on estado.encounter_id = e.encounter_id 
								where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
									 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
									 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime <:endDate
						         union
						         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
								where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime <:endDate  
									and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
								) 
					         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
					     		where inicioAnterior.patient_id is null																									 
					 )
					) 
				inicio_3HP 
				) 
			inicio_3HP	
			inner join 
			(
					/*
						Patients who have Última profilaxia TPT with value “3HP” and Data de Fim da Profilaxia TPT 
						registered in Ficha Resumo - Mastercard and at least 86  days apart from the 3HP Start Date
					*/
					select 	p.patient_id,obs3HPCompleted.obs_datetime data_final_3HP  																		
					from 	patient p  																													
							inner join encounter e on p.patient_id=e.patient_id  																			
							inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id 
							inner join obs obs3HPCompleted on obs3HPCompleted.encounter_id=e.encounter_id 
					where 	e.voided=0 and p.voided=0 and obs3HPCompleted.obs_datetime <=:endDate and  										
							obs3hp.voided=0 and obs3hp.concept_id=23985 and obs3hp.value_coded=23954 and e.encounter_type in (6,53) and  
							e.location_id=:location and obs3HPCompleted.voided=0 and obs3HPCompleted.concept_id=165308 and obs3HPCompleted.value_coded = 1267 		
									
			) final3hp on final3hp.patient_id = inicio_3HP.patient_id and final3hp.data_final_3hp between inicio_3HP.data_inicio_3HP + interval 86 day and inicio_3HP.data_inicio_3HP + interval 365 day
			
		union
		
		select inicio_3HP.patient_id, e.encounter_datetime data_final_3HP
		from (
				select inicio_3HP.patient_id,inicio_3HP.data_inicio_tpi data_inicio_3HP 
				from ( 
					select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
					where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
						and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
						and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime <:endDate 
							
					union
					
					select p.patient_id,outrasPrescricoesDT3HP.obs_datetime data_inicio_tpi  from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
					where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime <:endDate 
						 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location  
					
					union
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 			 
						inner join encounter e on p.patient_id=e.patient_id																				 			 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 			 
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
					
					union
					(
					 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
					 		(	select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 
					 				inner join encounter e on p.patient_id=e.patient_id																				 
					 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
					 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 
					 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
					 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
							
					 			union 	
								
					 			select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p														 		 
									inner join encounter e on p.patient_id=e.patient_id																				 		 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
										and seguimentoTPT.concept_id =23987  																						
										and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
										and seguimentoTPT.voided =0)							 
								where e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate	         
									and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
									and seguimentoTPT.obs_id is null					 	 																													 
					   		
					   		) inicio
					   		left join 																																 
					     	(	
					     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
								where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate 
									 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
						         union
					         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
									inner join encounter e on p.patient_id = e.patient_id 
									inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
									inner join obs estado on estado.encounter_id = e.encounter_id 
								where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
									 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
									 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime <:endDate
						         union
						         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
								where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime <:endDate  
									and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
								) 
					         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
					     		where inicioAnterior.patient_id is null																									 
					 )
					) 
				inicio_3HP 
			) inicio_3HP
			inner join encounter e on e.patient_id = inicio_3HP.patient_id																				 		
			inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																				
			inner join obs dispensaTrimestral on dispensaTrimestral.encounter_id = e.encounter_id																
		where e.voided=0 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 
			and  e.location_id=:location and dispensaTrimestral.voided =0 and dispensaTrimestral.concept_id =23986 and dispensaTrimestral.value_coded=23720
			and e.encounter_datetime between inicio_3HP.data_inicio_3HP and  (inicio_3HP.data_inicio_3HP + interval 4 month) 
			and e.encounter_datetime<= :endDate
			
		union
		
		select inicio_3HP.patient_id, e.encounter_datetime data_final_3HP
		from (
				select inicio_3HP.patient_id,inicio_3HP.data_inicio_tpi data_inicio_3HP 
				from ( 
					select p.patient_id,estadoProfilaxia.obs_datetime data_inicio_tpi from  patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
					where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
						and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
						and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime <:endDate 
							
					union
					
					select p.patient_id,outrasPrescricoesDT3HP.obs_datetime data_inicio_tpi  from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
					where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime <:endDate
						 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
					
					union
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 			 
						inner join encounter e on p.patient_id=e.patient_id																				 			 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 			 
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
					
					union
					(
					 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
					 		(	select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 
					 				inner join encounter e on p.patient_id=e.patient_id																				 
					 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
					 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 
					 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
					 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
							
					 			union 	
								
					 			select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p														 		 
									inner join encounter e on p.patient_id=e.patient_id																				 		 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
										and seguimentoTPT.concept_id =23987  																						
										and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
										and seguimentoTPT.voided =0)							 
								where e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate	         
									and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
									and seguimentoTPT.obs_id is null					  																													 
					   		
					   		) inicio
					   		left join 																																 
					     	(	
					     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
								where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate
									 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
						         union
					         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
									inner join encounter e on p.patient_id = e.patient_id 
									inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
									inner join obs estado on estado.encounter_id = e.encounter_id 
								where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
									 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
									 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime <:endDate
						         union
						         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
								where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime<:endDate
									and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
								) 
					         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
					     		where inicioAnterior.patient_id is null																									 
					 )
					) 
				inicio_3HP
			) inicio_3HP
			inner join encounter e on inicio_3HP.patient_id=e.patient_id
				inner join obs regimeTPT on regimeTPT.encounter_id=e.encounter_id		 																				
				inner join obs dispensaMensal on dispensaMensal.encounter_id=e.encounter_id																
			where 		e.voided=0 and e.encounter_datetime between inicio_3HP.data_inicio_3HP and  (inicio_3HP.data_inicio_3HP + interval 4 month)	 			  									
						and regimeTPT.voided=0 and regimeTPT.concept_id=23985 and regimeTPT.value_coded in (23954,23984) and e.encounter_type=60 
						and  e.location_id=:location and e.encounter_datetime <= :endDate  		
						and dispensaMensal.voided =0 and dispensaMensal.concept_id =23986 and dispensaMensal.value_coded=1098 							
			group by inicio_3HP.patient_id
			having count(e.encounter_id)>=3
		
		union
		
		select inicio_3HP.patient_id, data_inicio_3HP
		from (
			select inicio_3HP.patient_id,inicio_3HP.data_inicio_tpi data_inicio_3HP 
			from ( 
				select p.patient_id,estadoProfilaxia.obs_datetime data_inicio_tpi from  patient p 
					inner join encounter e on p.patient_id = e.patient_id 
					inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
					inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
				where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
					and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
					and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime <:endDate
						
				union
				
				select p.patient_id,outrasPrescricoesDT3HP.obs_datetime data_inicio_tpi  from patient p 
					inner join encounter e on p.patient_id = e.patient_id 
					inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
				where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime <:endDate
					 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
				
				union
				
				select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 			 
					inner join encounter e on p.patient_id=e.patient_id																				 			 
					inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
					inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
				where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 			 
					and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
					and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
				
				union
				(
				 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
				 		(	select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 
				 				inner join encounter e on p.patient_id=e.patient_id																				 
				 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
				 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
				 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 
				 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
				 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
						
				 			union 	
							
				 			select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p														 		 
								inner join encounter e on p.patient_id=e.patient_id																				 		 
								inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
								left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)							 
							where e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate	         
								and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
								and seguimentoTPT.obs_id is null					 
	 																													 
				   		
				   		) inicio
				   		left join 																																 
				     	(	
				     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
							where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate 
								 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
					         union
				         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
								inner join encounter e on p.patient_id = e.patient_id 
								inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
								inner join obs estado on estado.encounter_id = e.encounter_id 
							where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
								 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
								 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime <:endDate
					         union
					         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime <:endDate  
								and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
							) 
				         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
				     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
				     		where inicioAnterior.patient_id is null																									 
				 )
				) 
			inicio_3HP 
			) inicio_3HP
			inner join encounter e on e.patient_id = inicio_3HP.patient_id  																		
			inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id  																				
		where e.voided=0 and outrasPrescricoesDT3HP.voided=0 and e.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + interval 4 month)		
		     and e.encounter_datetime <= :endDate 
			and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type  = 6 
			and  e.location_id=:location
		
		union
		
		select inicio_3HP.patient_id, fim.encounter_datetime data_final_3HP
		from (
			select inicio_3HP.patient_id,inicio_3HP.data_inicio_tpi data_inicio_3HP 
				from ( 
					select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
					where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
						and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
						and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime <:endDate
							
					union
					
					select p.patient_id,outrasPrescricoesDT3HP.obs_datetime data_inicio_tpi  from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
					where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime <:endDate
						 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
					
					union
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 			 
						inner join encounter e on p.patient_id=e.patient_id																				 			 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 			 
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
					
					union
					(
					 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
					 		(	select p.patient_id,seguimentoTPT.obs_datetime data_inicio_tpi from patient p														 
					 				inner join encounter e on p.patient_id=e.patient_id																				 
					 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
					 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate	 
					 				and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
					 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
							
					 			union 	
								
					 			select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p														 		 
									inner join encounter e on p.patient_id=e.patient_id																				 		 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																					 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
										and seguimentoTPT.concept_id =23987  																						
										and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
										and seguimentoTPT.voided =0)							 
								where e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate	         
									and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
									and seguimentoTPT.obs_id is null					  																													 
					   		
					   		) inicio
					   		left join 																																 
					     	(	
					     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
								where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime <:endDate 
									 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
						         union
					         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
									inner join encounter e on p.patient_id = e.patient_id 
									inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
									inner join obs estado on estado.encounter_id = e.encounter_id 
								where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
									 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
									 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime <:endDate
						         union
						         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
								where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime<:endDate  
									and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
								) 
					         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
					     		where inicioAnterior.patient_id is null																									 
					 )
					) 
				inicio_3HP
			) inicio_3HP

			inner join 
			(
				select  distinct  p.patient_id,e.encounter_datetime,e.encounter_id  from patient p
	            inner join encounter e on e.patient_id = p.patient_id
				inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id		 																				
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id																
			 where e.voided=0 and profilaxia3HP.voided=0 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded = 23954 and e.encounter_type=6 and  e.location_id=:location	
				   and estadoProfilaxia.voided =0 and estadoProfilaxia.concept_id =165308 and estadoProfilaxia.value_coded in (1256,1257) and p.voided=0	
				   and e.encounter_datetime <=:endDate
	        ) fim on fim.patient_id=inicio_3HP.patient_id
	            where fim.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + interval 4 month)
	            group by inicio_3HP.patient_id
				having count(fim.encounter_id)>=3
				
			) inicio_3HP group by inicio_3HP.patient_id
	)fim_3hp