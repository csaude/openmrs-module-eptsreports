select inicio_TPT.patient_id
from 
(
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
				and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
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
			where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
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
						where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
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
						where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
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
					where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate  
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
					union
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_tpi from patient p 
				     	inner join encounter e on p.patient_id = e.patient_id 
				        	inner join obs o on o.encounter_id = e.encounter_id 
				        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
				     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
				      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
				      	and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
				)
				inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
					and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 7 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
				where inicioAnterior.patient_id is null	
	  		)
	 	) 
	inicio_INH group by inicio_INH.patient_id
)inicio_TPT	where inicio_TPT.patient_id is not null