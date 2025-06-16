select  fimINH.patient_id
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
		
			select inicio_INH.patient_id,inicio_INH.data_inicio_INH data_inicio_INH 
			from (
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
					
					union	
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
		 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 

								union
								
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         

							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH 
		) inicio_INH
		inner join 
		(  
			select p.patient_id,estadoFIM.obs_datetime data_fim_INH 
			from patient p 
				inner join encounter e on p.patient_id = e.patient_id 
				inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id 
				inner join obs estadoFIM on estadoFIM.encounter_id = e.encounter_id 
			where e.voided=0 and p.voided=0 and profilaxiaINH.voided=0 and e.encounter_type in (6,9,53)and profilaxiaINH.concept_id=23985 and profilaxiaINH.value_coded=656
				and estadoFIM.concept_id=165308 and estadoFIM.value_coded=1267 and estadoFIM.voided=0
				and estadoFIM.obs_datetime <=:endDate  and  e.location_id=:location	
		) fimINH on fimINH.patient_id = inicio_INH.patient_id and fimINH.data_fim_INH between inicio_INH.data_inicio_INH + interval 173 day and inicio_INH.data_inicio_INH + interval 365 day
		
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
	select inicio_INH.patient_id, inicio_INH.data_inicio_INH from (
	select inicio_INH.patient_id, inicio_INH.data_inicio_INH, inicio_INH.nDataFim from (
       select distinct inicio_INH.patient_id,inicio_INH.data_inicio_INH,fimINH.data_fim_INH, count(fimINH.encounter_id) nDataFim
		from(
			select inicio_INH.patient_id,inicio_INH.data_inicio_INH data_inicio_INH 
			from (
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
						group by p.patient_id, obsInicioINH.obs_datetime 

			 	) 
			inicio_INH 
		) inicio_INH
		inner join 
		(
		select * from (
			select 	p.patient_id, e.encounter_datetime data_fim_INH,estadoProfilaxia.value_coded,estadoProfilaxia.concept_id, e.encounter_id	 																
			from 	patient p														 			  															
					inner join encounter e on p.patient_id=e.patient_id																				 		
					inner join obs profilaxiaINH on profilaxiaINH.encounter_id=e.encounter_id		 																				
					inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id=e.encounter_id																
			where 	e.voided=0 and p.voided=0 and estadoProfilaxia.obs_datetime <=:endDate 			  									
					and profilaxiaINH.voided=0 and profilaxiaINH.concept_id=23985 and profilaxiaINH.value_coded in (656,23982) and e.encounter_type in (6,9) and  e.location_id=:location	  		
					and estadoProfilaxia.voided =0 and estadoProfilaxia.concept_id =165308 and estadoProfilaxia.value_coded in (1256,1257)
					) fim group by fim.patient_id, fim.encounter_id
			
		) fimINH on fimINH.patient_id=inicio_INH.patient_id
		where fimINH.data_fim_INH BETWEEN (inicio_INH.data_inicio_INH +interval 1 day) and (inicio_INH.data_inicio_INH + interval 7 month)
		group by inicio_INH.patient_id,inicio_INH.data_inicio_INH
		order by inicio_INH.data_inicio_INH
		) inicio_INH
		HAVING inicio_INH.nDataFim>=5
		)inicio_INH
		
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
		select final.patient_id, data_inicio_INH
		from 
		(
		select   inicio_INH.patient_id,
				data_inicio_INH,
				fimINHFilt.encounter_datetime,
				count(fimINHFilt.encounter_id) nConsultasFim
		from 
		(
			select inicio_INH.patient_id,inicio_INH.data_inicio_INH data_inicio_INH 
			from (
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
					
					union	
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
 
								union
								
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         

							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH 
		) inicio_INH
		inner join (
		select p.patient_id,e.encounter_id, e.encounter_datetime, dispensa.value_coded  from patient p 
				     inner join encounter e on p.patient_id=e.patient_id 
		inner join obs regime on regime.encounter_id=e.encounter_id 
		inner join obs dispensa on dispensa.encounter_id=e.encounter_id
		where e.voided=0 and
					e.encounter_type=60 and e.location_id=:location and regime.voided=0 and 
					regime.concept_id=23985 and regime.value_coded in (656,23982) and 
					dispensa.voided=0 and dispensa.concept_id=23986 and dispensa.value_coded = 1098 and e.encounter_datetime <= :endDate
		)fimINHFilt on fimINHFilt.patient_id = inicio_INH.patient_id
		where fimINHFilt.encounter_datetime BETWEEN inicio_INH.data_inicio_INH and inicio_INH.data_inicio_INH +interval 7 month
		group by inicio_INH.patient_id,inicio_INH.data_inicio_INH
		order by inicio_INH.data_inicio_INH
		) final where nConsultasFim >= 6

		union

				select final.patient_id, data_inicio_INH
		from 
		(
		select   inicio_INH.patient_id,
				data_inicio_INH,
				fimINHFilt.encounter_datetime,
				count(fimINHFilt.encounter_id) nConsultasFim
		from 
		(
			select inicio_INH.patient_id,inicio_INH.data_inicio_INH data_inicio_INH 
			from (
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
					
					union	
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
 
								union
								
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         

							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH 
		) inicio_INH
		inner join (
		select p.patient_id,e.encounter_id, e.encounter_datetime, dispensa.value_coded  from patient p 
				     inner join encounter e on p.patient_id=e.patient_id 
		inner join obs regime on regime.encounter_id=e.encounter_id 
		inner join obs dispensa on dispensa.encounter_id=e.encounter_id
		where e.voided=0 and
					e.encounter_type=60 and e.location_id=:location and regime.voided=0 and 
					regime.concept_id=23985 and regime.value_coded in (656,23982) and 
					dispensa.voided=0 and dispensa.concept_id=23986 and dispensa.value_coded = 23720 and e.encounter_datetime <= :endDate
		)fimINHFilt on fimINHFilt.patient_id = inicio_INH.patient_id
		where fimINHFilt.encounter_datetime BETWEEN inicio_INH.data_inicio_INH and inicio_INH.data_inicio_INH +interval 5 month
		group by inicio_INH.patient_id,inicio_INH.data_inicio_INH
		order by inicio_INH.data_inicio_INH
		) final where nConsultasFim >= 2
		
		union
		
		/*
					At least 2 consultations registered on Ficha Clínica with DT-INH
					(Profilaxia INH = Início/Continua and Outras Prescrições = DT-INH)
					or (Profilaxia TPT=”Isoniazida (INH)” and Estado da
					Profilaxia=“Início(I)/Continua( C) ” and Outras Prescrições = DT-
					INH ) until a 5-month period from the INH Start Date (including
					the INH Start Date)
		 * */
		select patient_id, data_fim_INH from (
		select inicio_INH.patient_id, DTINH.data_fim_INH,count(DTINH.encounter_id) nConsultasFim
		from(
			select inicio_INH.patient_id,inicio_INH.data_inicio_INH data_inicio_INH 
			from (
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime<:endDate  and  e.location_id=:location
						
					
					union	
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
	 
								union
								
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         

							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH 
		) inicio_INH
		inner join 
		(
			select p.patient_id,estadoProfilaxia.obs_datetime data_fim_INH, e.encounter_id																		
			from	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id
				inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id																			 		
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id=e.encounter_id
				inner join obs obsDTINH on obsDTINH.encounter_id=e.encounter_id	
			where e.voided=0 and p.voided=0 and estadoProfilaxia.obs_datetime <=:endDate 
				and profilaxiaINH.concept_id = 23985 and profilaxiaINH.value_coded = 656 and profilaxiaINH.voided = 0
				and estadoProfilaxia.voided=0 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded in (1256,1257)
				and obsDTINH.voided=0 and obsDTINH.concept_id=1719 and obsDTINH.value_coded=23955 and e.encounter_type=6 and  e.location_id=:location	  		
		) DTINH on DTINH.patient_id=inicio_INH.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_INH.data_inicio_INH and (inicio_INH.data_inicio_INH + interval 5 month)
		group by inicio_INH.patient_id,inicio_INH.data_inicio_INH
		order by inicio_INH.data_inicio_INH
		) final
		where nConsultasFim>=2
		
		union
		
			/*	At least 2 FILT with DT-INH (Regime de TPT= Isoniazida/’Isoniazida + Piridoxina’ 
                and Tipo de Dispensa = Trimestral) until a 5-month period from the Start Date 
                (including the INH Start Date) */

		select patient_id, data_inicio_INH from (
		select inicio_INH.patient_id,inicio_INH.data_inicio_INH, count(DTINH.encounter_id) nConsultasFim
		from(
			select inicio_INH.patient_id,inicio_INH.data_inicio_INH data_inicio_INH 
			from (
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
					
					union	
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 																											 
					
					union
					
					(select inicio.patient_id, inicio.data_inicio_INH
						from ( 
								select p.patient_id,min(seguimentoTPT.obs_datetime) data_inicio_INH
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
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
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
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
							where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0 
						      	and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH 
		) inicio_INH
		inner join 
		(
			select p.patient_id,e.encounter_datetime data_fim_INH,e.encounter_id																		
			from	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id
				inner join obs regimeTPT on regimeTPT.encounter_id = e.encounter_id																			 		
				inner join obs tipoDispensa on tipoDispensa.encounter_id=e.encounter_id
				where e.encounter_type=60 and regimeTPT.concept_id=23985 and regimeTPT.value_coded in(656,23982) and tipoDispensa.concept_id=23986 and tipoDispensa.value_coded=23720
				and p.voided=0 and e.voided=0 and regimeTPT.voided=0 and tipoDispensa.voided=0 and e.encounter_datetime <=:endDate

		) DTINH on DTINH.patient_id=inicio_INH.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_INH.data_inicio_INH and (inicio_INH.data_inicio_INH + interval 5 month) 
		group by inicio_INH.patient_id,inicio_INH.data_inicio_INH
		order by inicio_INH.data_inicio_INH
		) final
		where nConsultasFim >=2
		
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
			select inicio_INH.patient_id,inicio_INH.data_inicio_INH data_inicio_INH 
			from (
					select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH 
					from patient p 
						inner join encounter e on p.patient_id = e.patient_id 
						inner join obs o on o.encounter_id = e.encounter_id 
						inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
					where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53)and o.concept_id=23985 and o.value_coded=656
						and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
					
					union	
					
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 	 																											 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		  
								union
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         
							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime<:endDate  
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH 
		) inicio_INH
		inner join 
		(
			select p.patient_id,estadoProfilaxia.obs_datetime data_fim_INH,e.encounter_id																		
			from	patient p														 			  															
				inner join encounter e on p.patient_id=e.patient_id
				inner join obs profilaxiaINH on profilaxiaINH.encounter_id = e.encounter_id																			 		
				inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id=e.encounter_id
			where e.voided=0 and p.voided=0 and estadoProfilaxia.obs_datetime <:endDate 
				and profilaxiaINH.concept_id = 23985 and profilaxiaINH.value_coded = 656 and profilaxiaINH.voided = 0
				and estadoProfilaxia.voided=0 and estadoProfilaxia.concept_id=165308 and estadoProfilaxia.value_coded in (1256,1257)
				and e.encounter_type=6 and  e.location_id=:location
		) DTINH on DTINH.patient_id=inicio_INH.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_INH.data_inicio_INH and (inicio_INH.data_inicio_INH +interval 7 month)
		group by inicio_INH.patient_id,inicio_INH.data_inicio_INH
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
			where e.voided=0 and p.voided=0 and estadoProfilaxia.obs_datetime <=:endDate 
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
		select final.patient_id, data_inicio_INH, data_fim_INH from (
		select inicio_INH.patient_id, data_inicio_INH, DTINH.data_fim_INH, count(DTINH.encounter_id) nConsultasFim 
		from(
			select inicio_INH.patient_id,inicio_INH.data_inicio_INH data_inicio_INH
			from (
					select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
					from	patient p													 
						inner join encounter e on p.patient_id=e.patient_id																				 
						inner join obs o on o.encounter_id=e.encounter_id	 																			 
						inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
					where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
						and seguimentoTPT.voided =0 and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705)	 						 
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location 		 
																												 
					union
					(	select inicio.patient_id, inicio.data_inicio_INH
						from (
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	
								from	patient p													 
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id	 																			 
									inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
				 
								union
								
								select p.patient_id,seguimentoTPT.obs_datetime data_inicio_INH	from	patient p													         
									inner join encounter e on p.patient_id=e.patient_id																				 
									inner join obs o on o.encounter_id=e.encounter_id																				 
									left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
									and seguimentoTPT.concept_id =23987  																						
									and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
									and seguimentoTPT.voided =0)						 
								where e.voided=0 and p.voided=0 and seguimentoTPT.obs_datetime <:endDate   
									and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
									and seguimentoTPT.obs_id is null 	         

							)
				 		inicio
						left join
						(
							select p.patient_id,o.obs_datetime data_inicio_INH 
							from patient p																 
								inner join encounter e on p.patient_id=e.patient_id																				 
								inner join obs o on o.encounter_id=e.encounter_id																				 
							where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate
								and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location
							union
							select p.patient_id,obsInicioINH.obs_datetime data_inicio_INH from patient p 
						     	inner join encounter e on p.patient_id = e.patient_id 
						        	inner join obs o on o.encounter_id = e.encounter_id 
						        	inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
						     where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type in (6,9,53) and o.concept_id=23985 and o.value_coded=656
						      	and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
						      	and obsInicioINH.obs_datetime <:endDate and  e.location_id=:location
						)
						inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
							and inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
						where inicioAnterior.patient_id is null	
			  		)
			 	) 
			inicio_INH 
		) inicio_INH
		inner join 
		(
			select 	p.patient_id, e.encounter_datetime data_fim_INH, e.encounter_id																		
			from 	patient p														 			  															
					inner join encounter e on p.patient_id=e.patient_id																				 		
					inner join obs obsInh on obsInh.encounter_id=e.encounter_id		 																				
					inner join obs dispensaMensal on dispensaMensal.encounter_id=e.encounter_id																
			where 	e.voided=0 and p.voided=0 and e.encounter_datetime <:endDate	 			  									
					and obsInh.voided=0 and obsInh.concept_id=23985 and obsInh.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location	  		
					and dispensaMensal.voided =0 and dispensaMensal.concept_id =23986 and dispensaMensal.value_coded=1098							
		) DTINH on DTINH.patient_id=inicio_INH.patient_id
		where DTINH.data_fim_INH BETWEEN inicio_INH.data_inicio_INH and (inicio_INH.data_inicio_INH + interval 7 month)
		group by inicio_INH.patient_id,inicio_INH.data_inicio_INH
		order by inicio_INH.data_inicio_INH
		)final where nConsultasFim >= 3
		) startAndDT
		inner join
		(
			select 	p.patient_id, e.encounter_datetime  data_fim_INH																		
			from 	patient p														 			  															
					inner join encounter e on p.patient_id=e.patient_id																			 		
					inner join obs obsInh on obsInh.encounter_id=e.encounter_id		 																				
					inner join obs dispensaMensal on dispensaMensal.encounter_id=e.encounter_id																
			where 	e.voided=0 and p.voided=0 and e.encounter_datetime <=:endDate	 			  									
					and obsInh.voided=0 and obsInh.concept_id=23985 and obsInh.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location	  		
					and dispensaMensal.voided =0 and dispensaMensal.concept_id =23986 and dispensaMensal.value_coded=23720
		) dt on startAndDT.patient_id=dt.patient_id 
		where dt.data_fim_INH BETWEEN startAndDT.data_inicio_INH and (startAndDT.data_inicio_INH + interval 7 month)
	) fimINH group by fimINH.patient_id
) fimINH