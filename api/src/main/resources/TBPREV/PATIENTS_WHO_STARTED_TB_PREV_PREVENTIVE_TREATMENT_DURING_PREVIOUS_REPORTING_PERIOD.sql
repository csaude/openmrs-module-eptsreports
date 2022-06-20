select patient_id 
from (
	select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_tpi 
	from ( 
	/*						Patients who have Última profilaxia TPT with value “3HP” and Data Inicio
							da Profilaxia TPT registered in Ficha Resumo - Mastercard during the
							previous reporting period (3HP Start Date)
	 */
		select p.patient_id,min(obsInicio3HP.value_datetime) data_inicio_tpi from  patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs o on o.encounter_id = e.encounter_id 
			inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id
		where p.voided=0 and e.voided=0  and o.voided = 0 and obsInicio3HP.voided = 0  
			and  o.concept_id = 23985  and o.value_coded = 23954 and obsInicio3HP.concept_id=6128 
			and e.encounter_type in (53) and e.location_id=:location and obsInicio3HP.value_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)
		group by p.patient_id 
		
	    union 
		/*
		  					Patients who have Última profilaxia TPT with value “3HP” and Data Inicio
							da Profilaxia TPT registered in Ficha Resumo - Mastercard during the
							previous reporting period (3HP Start Date)
		 * */
		 
		 select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs o on o.encounter_id = e.encounter_id 
			inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 
		where e.voided=0  and p.voided=0 and obsInicio3HP.voided=0 and o.voided=0  
			 and o.concept_id=23985 and o.value_coded=23954 and obsInicio3HP.concept_id=165308 and obsInicio3HP.value_coded=1256 
			 and e.encounter_type in (6) and e.location_id=:location and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)
		group by p.patient_id 
		
		union
		/*
		 * 
					Patients who have Outras Prescrições with the value “3HP” marked on
					Ficha Clínica - Mastercard during the previous reporting period (3HP Start
					Date) and no other 3HP prescriptions marked on Ficha Clinica in the 4
					months prior to the 3HP Start Date and no pick-ups with Regime de TPT
					with the values “3HP or 3HP + Piridoxina” marked on FILT in the 4 months
					prior to this 3HP Start Date
		 * 
		 */
		(	select inicio.patient_id, inicio.data_inicio_tpi 
			from(	
				select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														 
					inner join encounter e on p.patient_id=e.patient_id																				 
					inner join obs o on o.encounter_id=e.encounter_id																				 
				where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) 	 
		    			and o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  e.location_id=:location			 
		         		group by p.patient_id
		      	)
		     inicio
		     left join 
		     (	select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
					inner join encounter e on p.patient_id=e.patient_id																				 
					inner join obs o on o.encounter_id=e.encounter_id																				 
				where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
					and o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  e.location_id=:location
				
				union
				
			    select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p														         
					inner join encounter e on p.patient_id=e.patient_id																				 		 
					inner join obs o on o.encounter_id=e.encounter_id																				         
				where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 10 month) and (:endDate - interval 6 month)	         
					and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
		     )	
			inicioAnterior on inicio.patient_id=inicioAnterior.patient_id  
		     	and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
		    where inicioAnterior.patient_id is null	 	
		)
		union
		/*
					  	Patients who have Outras Prescrições with the value “DT-3HP” marked on
						Ficha Clínica - Mastercard during the previous reporting period (3HP Start
						Date)
		 * */
		select p.patient_id,min(e.encounter_datetime) data_inicio_tpi  from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs o on o.encounter_id = e.encounter_id 
		where p.voided=0 and e.voided=0  and o.voided=0 and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) 
			 and o.concept_id=1719 and o.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
		group by p.patient_id  
		
		union
		
		/*
					  Patients who have Regime de TPT with the values “3HP or 3HP +
					  Piridoxina” and “Seguimento de tratamento TPT” = (‘Inicio’ or ‘Re-Inicio’)
					  marked on Ficha de Levantamento de TPT (FILT) during the previous
					reporting period (3HP Start Date)
					 
		  */
		select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														 			 
			inner join encounter e on p.patient_id=e.patient_id																				 			 
			inner join obs o on o.encounter_id=e.encounter_id		 																					 
			inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
		where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 			 
			and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
			and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
		group by p.patient_id
		
		/*
					 * Patients who have Regime de TPT with the values “3HP or 3HP +
					Piridoxina” and “Seguimento de Tratamento TPT” with values “Continua”
					or “Fim” or no value marked on the first pick-up date on Ficha de
					Levantamento de TPT (FILT) during the previous reporting period (FILT 3HP
					Start Date) and:
					o No other Regime de TPT with the values “3HP or 3HP +
					Piridoxina” marked on FILT in the 4 months prior to this FILT 3HP
					Start Date and
					o No other 3HP Start Dates marked on Ficha Clinica ((Profilaxia TPT
					with the value “3HP” and Estado da Profilaxia with the value
					“Inicio (I)”) or (Outras Prescrições with the value “3HP”/“DT-
					3HP”)) in the 4 months prior to this FILT 3HP Start Date and
					o No other 3HP Start Dates marked on Ficha Resumo (Última
					profilaxia TPT with value “3HP” and Data Inicio da Profilaxia TPT)
					in the 4 months prior to this FILT 3HP Start Date:
		  */
		union
		(
		 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
		 		(	select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														 
		 				inner join encounter e on p.patient_id=e.patient_id																				 
		 				inner join obs o on o.encounter_id=e.encounter_id	 																			 
		 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
		 			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	 
		 				and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
		 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
		 			group by p.patient_id
				
		 			union 	
					
		 			select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														 		 
						inner join encounter e on p.patient_id=e.patient_id																				 		 
						inner join obs o on o.encounter_id=e.encounter_id	 																					 
						left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
							and seguimentoTPT.concept_id =23987  																						
							and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
							and seguimentoTPT.voided =0)							 
					where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)	         
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
						and seguimentoTPT.obs_id is null					 
					group by p.patient_id	 																													 
		   		
		   		) inicio
		   		left join 																																 
		     	(	
		     		select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id																				 
					where  e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month) 
						 and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
			         union
		         		select p.patient_id, e.encounter_datetime data_inicio_tpi from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 
					where e.voided=0  and p.voided=0 and obsInicio3HP.voided=0 and o.voided=0  
						 and o.concept_id=23985 and o.value_coded=23954 and obsInicio3HP.concept_id=165308 and obsInicio3HP.value_coded=1256 
						 and e.encounter_type in (6) and e.location_id=:location and e.encounter_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)
			         union
			         select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id																				 
					where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
						and o.voided=0 and o.concept_id=1719 and o.value_coded in (23954,165307) and e.encounter_type in (6) and  e.location_id=:location
					union
					select p.patient_id,obsInicio3HP.value_datetime data_inicio_tpi from patient p																 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id
						inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 	
					where e.voided=0 and p.voided=0 and p.voided = 0 and obsInicio3HP.value_datetime between (:startDate- INTERVAL 10 MONTH) and (:endDate - interval 6 month)  and 
						  o.voided=0 and o.concept_id=23985 and o.value_coded=23954 and obsInicio3HP.concept_id=6128  and obsInicio3HP.voided=0 and 
						  e.encounter_type in (53) and e.location_id=:location
		         		) 
		         		inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
		     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
		     		where inicioAnterior.patient_id is null																									 
		 )
		) 
	inicio_3HP group by inicio_3HP.patient_id
	union
	select inicio_INH.patient_id,min(inicio_INH.data_inicio_tpi) data_inicio_tpi 
	from (
	     /*
						      Patients who have “Última profilaxia Isoniazida (Data Início)” or (Última
							 profilaxia TPT with value “Isoniazida (INH)” and Data Inicio da Profilaxia
					         TPT) registered in Ficha Resumo - Mastercard during the previous reporting
					         period (INH Start Date) or
	      * */
			select p.patient_id,min(obsInicioINH.value_datetime) data_inicio_tpi 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs o on o.encounter_id = e.encounter_id 
				inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
			where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type=53 and o.concept_id=23985 and o.value_coded=656
				and obsInicioINH.concept_id=6128 and obsInicioINH.voided=0
				and obsInicioINH.value_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
				group by p.patient_id
			union
			
			/*
			 * Patients who have “Última profilaxia Isoniazida (Data Início) In Ficha Clinia, ficha Resumo
			 * */
			
			select p.patient_id,min(o.value_datetime) data_inicio_tpi 
			from patient p																 
				inner join encounter e on p.patient_id=e.patient_id																				 
				inner join obs o on o.encounter_id=e.encounter_id	
				left join obs regimeTPT on (regimeTPT.encounter_id  = e.encounter_id and  regimeTPT.concept_id = 23985 and regimeTPT.voided = 0)
			where e.voided=0 and p.voided=0 and o.value_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) 	     
				and o.voided=0 and o.concept_id=6128 and e.encounter_type in (6,9,53) and e.location_id=:location	
				and regimeTPT.person_id is null
			group by p.patient_id	
			union
						
			select 	p.patient_id,min(e.encounter_datetime) data_inicio_tpi  																		
			from 	patient p  																														
					inner join encounter e on p.patient_id=e.patient_id  																				
					inner join obs o on o.encounter_id=e.encounter_id  																					
			where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)  												
					and o.voided=0 and o.concept_id=6122 and o.value_coded=1256 and e.encounter_type in (6,9) and  e.location_id=:location 				
			group by p.patient_id
		
			union	
			/*
			 * 
					Patients who have  (Profilaxia
					TPT with the value “Isoniazida (INH)” and Estado da Profilaxia with the
					value “Inicio (I)”) marked on Ficha Clínica 
			 * */
			select p.patient_id,min(e.encounter_datetime) data_inicio_tpi 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs o on o.encounter_id = e.encounter_id 
				inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
			where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type=6 and o.concept_id=23985 and o.value_coded=656
				and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
				and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month) and  e.location_id=:location
				group by p.patient_id
			union
			
			select 	p.patient_id,min(e.encounter_datetime) data_inicio_tpi  																		
			from 	patient p  																														
					inner join encounter e on p.patient_id=e.patient_id  																				
					inner join obs o on o.encounter_id=e.encounter_id  																					
			where 	e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)  												
					and o.voided=0 and o.concept_id=6122 and o.value_coded=1256 and e.encounter_type in (6,9) and  e.location_id=:location 				
			group by p.patient_id
		
			union	
			
			/*
			 *   Patients who have Regime de TPT with the values (“Isoniazida” or
					“Isoniazida + Piridoxina”) and “Seguimento de tratamento TPT” = (‘Inicio’ or
					‘Re-Inicio’) marked on Ficha de Levantamento de TPT (FILT) during the
					previous reporting period (INH Start Date)
			 * */
			select p.patient_id,min(e.encounter_datetime) data_inicio_tpi	
			from	patient p													 
				inner join encounter e on p.patient_id=e.patient_id																				 
				inner join obs o on o.encounter_id=e.encounter_id	 																			 
				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
				and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
				group by p.patient_id	 																											 
			union
			/*
							Patients who have Regime de TPT with the values (“Isoniazida” or
							“Isoniazida + Piridoxina”) and “Seguimento de Tratamento TPT” with values
							“Continua” or no value marked on the first pick-up date on Ficha de
							Levantamento de TPT (FILT) during the previous reporting period (FILT INH
							Start Date) and:
							o No other Regime de TPT with the values INH values (“Isoniazida”
							or “Isoniazida + Piridoxina”) marked on FILT in the 7 months prior
							to this FILT INH Start Date and
							o No other INH Start Dates marked on Ficha Clinica (Profilaxia (INH)
							with the value “I” (Início) or (Profilaxia TPT with the value
							“Isoniazida (INH)” and Estado da Profilaxia with the value “Inicio
							(I)”) in the 7 months prior to this FILT INH Start Date and
							o No other INH Start Dates marked on Ficha de Seguimento
							(Profilaxia com INH – TPI (Data Início)) in the 7 months prior to
							this FILT INH Start Date and
							o No other INH Start Dates marked onor Ficha resumo (“Última
							profilaxia Isoniazida (Data Início)” or (Última profilaxia TPT with
							value “Isoniazida (INH)” and Data Inicio da Profilaxia TPT)) in the
							7 months prior to this ‘FILT INH Start Date’
			 * */
			(	select inicio.patient_id, inicio.data_inicio_tpi
				from (
						select p.patient_id,min(e.encounter_datetime) data_inicio_tpi	
						from	patient p													 
							inner join encounter e on p.patient_id=e.patient_id																				 
							inner join obs o on o.encounter_id=e.encounter_id	 																			 
							inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
						where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
							and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
							and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
							group by p.patient_id	 
						union
						select p.patient_id,min(e.encounter_datetime) data_inicio_tpi	from	patient p													         
							inner join encounter e on p.patient_id=e.patient_id																				 
							inner join obs o on o.encounter_id=e.encounter_id																				 
							left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
							and seguimentoTPT.concept_id =23987  																						
							and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
							and seguimentoTPT.voided =0)						 
						where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 6 month) and (:endDate - interval 6 month)   
							and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
							and seguimentoTPT.obs_id is null 	         
							group by p.patient_id		
					)
		 		inicio
				left join
				(
					select p.patient_id,e.encounter_datetime data_inicio_tpi 
					from patient p																 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id																				 
					where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- INTERVAL 13 MONTH) and (:endDate - interval 7 month)  
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
					union
					select p.patient_id, e.encounter_datetime data_inicio_tpi from patient p                                                                
						inner join encounter e on p.patient_id=e.patient_id                                                                                      
						inner join obs o on o.encounter_id=e.encounter_id                                                                                        
					where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month)            
						and o.voided=0 and o.concept_id=6122  and o.value_coded =1256 and  e.encounter_type in(6,9) and e.location_id=:location 
					union
					select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p 
				     	inner join encounter e on p.patient_id = e.patient_id 
				        	inner join obs o on o.encounter_id = e.encounter_id 
				        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
				     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type=6 and o.concept_id=23985 and o.value_coded=656
				      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
				      	and e.encounter_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month) and  e.location_id=:location
				     union
				     select p.patient_id, o.value_datetime data_inicio_tpi from patient p                                                                
						inner join encounter e on p.patient_id=e.patient_id                                                                                      
						inner join obs o on o.encounter_id=e.encounter_id  
						left join obs regimeTPT on (regimeTPT.encounter_id  = e.encounter_id and  regimeTPT.concept_id = 23985 and regimeTPT.voided = 0)
					where e.voided=0 and p.voided=0 and o.value_datetime between (:startDate- interval 13 month) and (:endDate - interval 7 month)            
						and o.voided=0 and o.concept_id=6128 and e.encounter_type in(6,9,53) and e.location_id=:location 
						and regimeTPT.person_id is null
				)
				inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 7 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
				where inicioAnterior.patient_id is null	
	  		)
	 	) 
	inicio_INH group by inicio_INH.patient_id	
) inicio_TPT where inicio_TPT.patient_id is not null

