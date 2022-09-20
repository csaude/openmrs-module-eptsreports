select patient_id 
from (
	select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_tpi 
	from ( 
	/*	Inicio  3HP na Ficha Clinica, Seguimento e Resumo
	 */
		select p.patient_id,min(estadoProfilaxia.obs_datetime) data_inicio_tpi from  patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
			inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id
		where p.voided=0 and e.voided=0  and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0  
			and  profilaxia3HP.concept_id = 23985  and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded = 1256 
			and e.encounter_type in (6,9,53) and e.location_id=:location and estadoProfilaxia.obs_datetime between (:endDate - interval 7 month) and :endDate
		group by p.patient_id 
				
		union
		/*
		 Inicio Usando Outras prescrições DT-3HP na Ficha Clinica			  	
		 * */
		select p.patient_id,min(outrasPrescricoesDT3HP.obs_datetime) data_inicio_tpi  from patient p 
			inner join encounter e on p.patient_id = e.patient_id 
			inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
		where p.voided=0 and e.voided=0  and outrasPrescricoesDT3HP.voided=0 and outrasPrescricoesDT3HP.obs_datetime between (:endDate - interval 7 month) and :endDate
			 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307 and e.encounter_type in (6)  and e.location_id=:location 
		group by p.patient_id  
		
		union
		
		/*
					  Patients who have Regime de TPT with the values “3HP or 3HP +
					  Piridoxina” and “Seguimento de tratamento TPT” = (‘Inicio’ or ‘Re-Inicio’)
					  marked on Ficha de Levantamento de TPT (FILT) during the previous
					reporting period (3HP Start Date)
					 
		  */
		select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 			 
			inner join encounter e on p.patient_id=e.patient_id																				 			 
			inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id		 																					 
			inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
		where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - interval 7 month) and :endDate	 			 
			and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
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
		 		(	select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi from patient p														 
		 				inner join encounter e on p.patient_id=e.patient_id																				 
		 				inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id	 																			 
		 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
		 			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - interval 7 month) and :endDate	 
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
					where e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:endDate - interval 7 month) and :endDate	         
						and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
						and seguimentoTPT.obs_id is null					 
					group by p.patient_id	 																													 
		   		
		   		) inicio
		   		left join 																																 
		     	(	
		     		select p.patient_id,regime3HP.obs_datetime data_inicio_tpi from patient p																 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id																				 
					where  e.voided=0 and p.voided=0 and regime3HP.obs_datetime between (:endDate - interval 20 month) and :endDate
						 and regime3HP.voided=0 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
			         union
		         		select p.patient_id, estado.obs_datetime data_inicio_tpi from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
						inner join obs estado on estado.encounter_id = e.encounter_id 
					where e.voided=0  and p.voided=0 and estado.voided=0 and profilaxia3HP.voided=0  
						 and profilaxia3HP.concept_id=23985 and profilaxia3HP.value_coded=23954 and estado.concept_id=165308 and estado.value_coded=1256 
						 and e.encounter_type in (6,53) and e.location_id=:location and estado.obs_datetime between (:endDate - interval 20 month) and :endDate
			         union
			         select p.patient_id,outrasPrecricoesDT3HP.obs_datetime data_inicio_tpi from patient p																 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs outrasPrecricoesDT3HP on outrasPrecricoesDT3HP.encounter_id=e.encounter_id																				 
					where e.voided=0 and p.voided=0 and outrasPrecricoesDT3HP.obs_datetime between (:endDate - interval 20 month) and :endDate 
						and outrasPrecricoesDT3HP.voided=0 and outrasPrecricoesDT3HP.concept_id=1719 and outrasPrecricoesDT3HP.value_coded = 165307 and e.encounter_type in (6) and  e.location_id=:location
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
			 * 
					Patients who have  (Profilaxia
					TPT with the value “Isoniazida (INH)” and Estado da Profilaxia with the
					value “Inicio (I)”) marked on Ficha Clínica , Ficha Seguimento and Ficha Resumo
			 * */	
			select p.patient_id,min(obsInicioINH.obs_datetime) data_inicio_tpi 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs o on o.encounter_id = e.encounter_id 
				inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
			where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
				and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
				and obsInicioINH.obs_datetime between (:endDate - interval 7 month) and :endDate and  e.location_id=:location
				group by p.patient_id
			
			union	
			
			/*
			 *   Patients who have Regime de TPT with the values (“Isoniazida” or
					“Isoniazida + Piridoxina”) and “Seguimento de tratamento TPT” = (‘Inicio’ or
					‘Re-Inicio’) marked on Ficha de Levantamento de TPT (FILT) during the
					previous reporting period (INH Start Date)
			 * */
			select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi	
			from	patient p													 
				inner join encounter e on p.patient_id=e.patient_id																				 
				inner join obs o on o.encounter_id=e.encounter_id	 																			 
				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - interval 7 month) and :endDate 
				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
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
						select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi	
						from	patient p													 
							inner join encounter e on p.patient_id=e.patient_id																				 
							inner join obs o on o.encounter_id=e.encounter_id	 																			 
							inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
						where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - interval 7 month) and :endDate  
							and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
							and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
							group by p.patient_id	 
						union
						select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_tpi	from	patient p													         
							inner join encounter e on p.patient_id=e.patient_id																				 
							inner join obs o on o.encounter_id=e.encounter_id																				 
							left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
							and seguimentoTPT.concept_id =23987  																						
							and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
							and seguimentoTPT.voided =0)						 
						where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime between (:endDate - interval 7 month) and :endDate  
							and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
							and seguimentoTPT.obs_id is null 	         
							group by p.patient_id
					)
		 		inicio
				left join
				(
					select p.patient_id,o.obs_datetime data_inicio_tpi 
					from patient p																 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id																				 
					where e.voided=0 and p.voided=0 and o.obs_datetime between (:endDate - interval 20 month) and :endDate
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
					union
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_tpi from patient p 
				     	inner join encounter e on p.patient_id = e.patient_id 
				        	inner join obs o on o.encounter_id = e.encounter_id 
				        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
				     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
				      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
				      	and obsInicioINH.obs_datetime between (:endDate - interval 20 month) and :endDate and  e.location_id=:location
				)
				inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 7 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
				where inicioAnterior.patient_id is null	
	  		)
	 	) 
	inicio_INH group by inicio_INH.patient_id	
) inicio_TPT where inicio_TPT.patient_id is not null