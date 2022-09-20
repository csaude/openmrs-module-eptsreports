                    select patient_id
					from 
					(
						  select 
                          coorte12meses_final.patient_id,
						  dataRevelacaoTotal,
						  dataRevelacaoParcial,
						  dataNaoRevelado,
                          if(revelado.patient_id is not null,1,if(revelacaoParcial.patient_id is not null,2,if(naoRevelacao.patient_id is not null,3,4))) statusRevelacao
                          from ( 
				        	select  inicio_fila_seg_prox.*,GREATEST(COALESCE(data_fila,data_seguimento,data_recepcao_levantou),COALESCE(data_seguimento,data_fila,data_recepcao_levantou),COALESCE(data_recepcao_levantou,data_seguimento,data_fila))  data_usar_c, 
				            GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30),COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_usar from (select inicio_fila_seg.*, 
				            max(obs_fila.value_datetime) data_proximo_lev, 
				            max(obs_seguimento.value_datetime) data_proximo_seguimento, 
				            date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30 from (select inicio.*,saida.data_estado,max_fila.data_fila,max_consulta.data_seguimento, max_recepcao.data_recepcao_levantou from ( 
				            Select patient_id,min(data_inicio) data_inicio from ( 
				            Select p.patient_id,min(e.encounter_datetime) data_inicio from patient p  
				            inner join encounter e on p.patient_id=e.patient_id 
				            inner join obs o on o.encounter_id=e.encounter_id 
				            where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and  e.encounter_datetime<=:endDate and e.location_id=:location 
				            group by p.patient_id 
				            union 
				            Select p.patient_id,min(value_datetime) data_inicio from patient p 
				            inner join encounter e on p.patient_id=e.patient_id 
				            inner join obs o on e.encounter_id=o.encounter_id 
				            where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and  o.concept_id=1190 and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location group by p.patient_id 
				            union 
				            select pg.patient_id,min(date_enrolled) data_inicio from patient p inner join patient_program pg on p.patient_id=pg.patient_id 
				            where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
				            group by pg.patient_id 
				            union 
				            SELECT e.patient_id, MIN(e.encounter_datetime) AS data_inicio  FROM 	patient p 
				            inner join encounter e on p.patient_id=e.patient_id 
				            WHERE p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
				            GROUP BY p.patient_id 
				            union 
				            Select p.patient_id,min(value_datetime) data_inicio from patient p 
				            inner join encounter e on p.patient_id=e.patient_id 
				            inner join obs o on e.encounter_id=o.encounter_id 
				            where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866 and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location 
				            group by p.patient_id) inicio_real group by patient_id)inicio 
				            left join ( 
				            select patient_id,max(data_estado) data_estado from ( 
				            select maxEstado.patient_id,maxEstado.data_transferidopara data_estado from( 
				            select pg.patient_id,max(ps.start_date) data_transferidopara from patient p 
				            inner join patient_program pg on p.patient_id=pg.patient_id 
				            inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
				            where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
				            group by p.patient_id )maxEstado 
				            inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
				            inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
				            where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and 
				            ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state in (7,8,10) 
				            union 
				            select p.patient_id, max(o.obs_datetime) data_estado from patient p  
				            inner join encounter e on p.patient_id=e.patient_id 
				            inner join obs  o on e.encounter_id=o.encounter_id 
				            where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and o.obs_datetime<=:endDate and e.location_id=:location 
				            group by p.patient_id 
				            union 
				            select person_id as patient_id,death_date as data_estado from person  
				            where dead=1 and death_date is not null and death_date<=:endDate) allSaida 
				            group by patient_id) saida on inicio.patient_id=saida.patient_id 
				            left join ( 
				            Select p.patient_id,max(encounter_datetime) data_fila from patient p  
				            inner join encounter e on e.patient_id=p.patient_id 
				            where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location and date(e.encounter_datetime)<=:endDate 
				            group by p.patient_id) max_fila on inicio.patient_id=max_fila.patient_id	
				            left join (Select p.patient_id,max(encounter_datetime) data_seguimento from patient p 
				            inner join encounter e on e.patient_id=p.patient_id 
				            where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  e.location_id=:location and e.encounter_datetime<=:endDate group by p.patient_id) max_consulta on inicio.patient_id=max_consulta.patient_id 
				            left join ( 
				            Select p.patient_id,max(value_datetime) data_recepcao_levantou from patient p 
				            inner join encounter e on p.patient_id=e.patient_id 
				            inner join obs o on e.encounter_id=o.encounter_id 
				            where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866 and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location 
				            group by p.patient_id) max_recepcao on inicio.patient_id=max_recepcao.patient_id 
				            group by inicio.patient_id) inicio_fila_seg 
				            left join obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id and obs_fila.voided=0 and obs_fila.obs_datetime=inicio_fila_seg.data_fila and obs_fila.concept_id=5096 and obs_fila.location_id=:location 
				            left join obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id and obs_seguimento.voided=0 and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento and obs_seguimento.concept_id=1410 and obs_seguimento.location_id=:location 
				            group by inicio_fila_seg.patient_id) inicio_fila_seg_prox 
				            group by patient_id 
				
				            ) coorte12meses_final 
                          inner join person p on p.person_id=coorte12meses_final.patient_id  
                          
                          
						  left join 
						  (
							select 	p.patient_id,max(e.encounter_datetime) dataRevelacaoTotal
							from 	patient p 
									inner join encounter e on p.patient_id=e.patient_id
									inner join obs o on e.encounter_id=o.encounter_id
							where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 6340 and o.value_coded=6337 and e.encounter_type=35 and 
									e.encounter_datetime <=:endDate and e.location_id=:location
							group by p.patient_id
							) revelado on revelado.patient_id=coorte12meses_final.patient_id
							left join 
							(
								select 	p.patient_id,max(e.encounter_datetime) dataRevelacaoParcial
								from 	patient p 
										inner join encounter e on p.patient_id=e.patient_id
										inner join obs o on e.encounter_id=o.encounter_id
								where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 6340 and o.value_coded=6338 and e.encounter_type=35 and 
										e.encounter_datetime <=:endDate and e.location_id=:location
								group by p.patient_id
							) revelacaoParcial on revelacaoParcial.patient_id=coorte12meses_final.patient_id
							left join 
							(
								select 	p.patient_id,max(e.encounter_datetime) dataNaoRevelado
								from 	patient p 
										inner join encounter e on p.patient_id=e.patient_id
										inner join obs o on e.encounter_id=o.encounter_id
								where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id = 6340 and o.value_coded=6339 and e.encounter_type=35 and 
										e.encounter_datetime <=:endDate and e.location_id=:location
								group by p.patient_id
							) naoRevelacao on naoRevelacao.patient_id=coorte12meses_final.patient_id
							
                          where (coorte12meses_final.data_estado is null or (coorte12meses_final.data_estado is not null and  coorte12meses_final.data_usar_c>coorte12meses_final.data_estado)) 
                          and date_add(coorte12meses_final.data_usar, interval 60 day) >=:endDate and floor(datediff(:endDate,p.birthdate)/365)  between 8 and 14 
				) revelacao  
