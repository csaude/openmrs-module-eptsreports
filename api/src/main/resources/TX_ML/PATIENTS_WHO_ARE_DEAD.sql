select patient_id         
from (
			select inicio_fila_seg_prox.*,         
					greatest(coalesce(data_fila,data_seguimento),coalesce(data_seguimento,data_fila))  data_usar_c, 
					greatest(coalesce(data_proximo_lev,data_recepcao_levantou30),coalesce(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) maximo_proximo_fila_recepcao,    
					greatest(coalesce(data_proximo_lev,data_proximo_seguimento,data_recepcao_levantou30),coalesce(data_proximo_seguimento,data_proximo_lev,data_recepcao_levantou30),coalesce(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) data_usar 
             from (
						select inicio_fila_seg.*,
							max(obs_fila.value_datetime) data_proximo_lev,  
							max(obs_seguimento.value_datetime) data_proximo_seguimento,       
							date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30          
						from (
									select inicio.*,        
										saida.data_estado,  
										entrada_obitos.data_estado data_entrada_obito,
										saidas_obitos.data_estado data_saida_obito,
										entradas_por_transferencia.data_estado  data_entrada_transferencia,
										saidas_por_transferencia.data_estado data_saida_transferencia,
										max_fila.data_fila, 
										max_consulta.data_seguimento,     
										max_recepcao.data_recepcao_levantou   
									from (   
												select patient_id,min(data_inicio) data_inicio      
												from (           
															select  p.patient_id,min(e.encounter_datetime) data_inicio    
															from patient p         
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id   
																	inner join obs o on o.encounter_id=e.encounter_id     
															where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0           
																	and e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256              
																	and e.encounter_datetime<=:endDate and e.location_id=:location
																	group by p.patient_id     
															union   
															select  p.patient_id,min(value_datetime) data_inicio          
															from patient p
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs o on e.encounter_id=o.encounter_id     
															where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53)           
																	and o.concept_id=1190 and o.value_datetime is not null 
																	and o.value_datetime<=:endDate and e.location_id=:location
																	group by p.patient_id     
															union   
															select  pg.patient_id,min(date_enrolled) data_inicio          
															from patient p         
																	inner join person pe on pe.person_id = p.patient_id       
																	inner join patient_program pg on p.patient_id=pg.patient_id   
															where   pg.voided=0 and p.voided=0 and pe.voided = 0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
																	group by pg.patient_id    
															union   
															select e.patient_id, min(e.encounter_datetime) as data_inicio
															from patient p     
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id   
															where p.voided=0 and pe.voided = 0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location  
																	group by  p.patient_id
															union 
															select  p.patient_id,min(value_datetime) data_inicio    
															from patient p   
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs o on e.encounter_id=o.encounter_id   
															where   p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52   
																	and o.concept_id=23866 and o.value_datetime is not null            
																	and o.value_datetime<=:endDate and e.location_id=:location            
																	group by p.patient_id   
														) inicio_real           
															group by patient_id         
											) inicio 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date<= :endDate and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
																	and o.obs_datetime<= :endDate  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime<= :endDate and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
													) allsaida        
															group by patient_id 
											) entrada_obitos on inicio.patient_id = entrada_obitos.patient_id 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date< :startDate and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
																	and o.obs_datetime< :startDate  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date< :startDate
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime< :startDate and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
													) allsaida        
															group by patient_id 
											) saidas_obitos on inicio.patient_id = saidas_obitos.patient_id 
											left join
											(
												select saidas_por_transferencia.patient_id, data_estado 
													from (
																select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																from (
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (          
																						select pg.patient_id, max(ps.start_date) data_estado
																						from patient p   
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join patient_program pg on p.patient_id = pg.patient_id         
																								inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																						where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																								and ps.start_date<= :endDate  and pg.location_id =:location group by pg.patient_id       
																					 ) max_estado            
																					inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																			where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
																			union 
																			select  p.patient_id,max(o.obs_datetime) data_estado   
																			from patient p       
																					inner join person pe on pe.person_id = p.patient_id 
																					inner join encounter e on p.patient_id=e.patient_id 
																					inner join obs  o on e.encounter_id=o.encounter_id  
																			where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																					and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																					and o.obs_datetime<=:endDate and e.location_id=:location
																					group by p.patient_id   
																			union 
																			select ultimabusca.patient_id, ultimabusca.data_estado      
																			from (
																						select p.patient_id,max(e.encounter_datetime) data_estado             
																						from patient p  
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join encounter e on p.patient_id=e.patient_id 
																								inner join obs o on o.encounter_id=e.encounter_id   
																						where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= :endDate  
																								and e.encounter_type = 21 and  e.location_id= :location           
																								group by p.patient_id         
																					) ultimabusca       
																					inner join encounter e on e.patient_id = ultimabusca.patient_id       
																					inner join obs o on o.encounter_id = e.encounter_id 
																			where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id = :location 
																) saidas_por_transferencia 
																	group by patient_id 
													) saidas_por_transferencia
													left join
													(  
															select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
															from (
																		select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																		from (
																					select p.patient_id, max(encounter_datetime) data_fila     
																					from patient p     
																							inner join person pe on pe.person_id = p.patient_id   
																							inner join encounter e on e.patient_id=p.patient_id   
																					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																							and e.location_id=:location and e.encounter_datetime <= :endDate          
																							group by p.patient_id 
																		) ultimo_fila  
																		left join            
																		obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																		and obs_fila.voided=0   
																		and obs_fila.obs_datetime=ultimo_fila.data_fila      
																		and obs_fila.concept_id=5096           
																		and obs_fila.location_id=:location  
																		union
																		select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																		from patient p     
																				inner join person pe on pe.person_id = p.patient_id   
																				inner join encounter e on p.patient_id=e.patient_id   
																				inner join obs o on e.encounter_id=o.encounter_id 
																		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																				and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate
																				group by p.patient_id
															) ultimo_levantamento group by patient_id
													) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
													where ultimo_levantamento.data_ultimo_levantamento <= :endDate
											) entradas_por_transferencia on inicio.patient_id = entradas_por_transferencia.patient_id 
											left join
											(
												select saidas_por_transferencia.patient_id, data_estado 
													from (
																select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																from (
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (          
																						select pg.patient_id, max(ps.start_date) data_estado
																						from patient p   
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join patient_program pg on p.patient_id = pg.patient_id         
																								inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																						where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																								and ps.start_date< :startDate  and pg.location_id =:location group by pg.patient_id       
																					 ) max_estado            
																					inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																			where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
																			union 
																			select  p.patient_id,max(o.obs_datetime) data_estado   
																			from patient p       
																					inner join person pe on pe.person_id = p.patient_id 
																					inner join encounter e on p.patient_id=e.patient_id 
																					inner join obs  o on e.encounter_id=o.encounter_id  
																			where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																					and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																					and o.obs_datetime<:startDate and e.location_id=:location
																					group by p.patient_id   
																			union 
																			select ultimabusca.patient_id, ultimabusca.data_estado      
																			from (
																						select p.patient_id,max(e.encounter_datetime) data_estado             
																						from patient p  
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join encounter e on p.patient_id=e.patient_id 
																								inner join obs o on o.encounter_id=e.encounter_id   
																						where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime< :startDate  
																								and e.encounter_type = 21 and  e.location_id= :location           
																								group by p.patient_id         
																					) ultimabusca       
																					inner join encounter e on e.patient_id = ultimabusca.patient_id       
																					inner join obs o on o.encounter_id = e.encounter_id 
																			where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id = :location 
																) saidas_por_transferencia 
																	group by patient_id 
													) saidas_por_transferencia
													left join
													(  
															select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
															from (
																		select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																		from (
																					select p.patient_id, max(encounter_datetime) data_fila     
																					from patient p     
																							inner join person pe on pe.person_id = p.patient_id   
																							inner join encounter e on e.patient_id=p.patient_id   
																					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																							and e.location_id=:location and e.encounter_datetime < :startDate          
																							group by p.patient_id 
																		) ultimo_fila  
																		left join            
																		obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																		and obs_fila.voided=0   
																		and obs_fila.obs_datetime=ultimo_fila.data_fila      
																		and obs_fila.concept_id=5096           
																		and obs_fila.location_id=:location  
																		union
																		select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																		from patient p     
																				inner join person pe on pe.person_id = p.patient_id   
																				inner join encounter e on p.patient_id=e.patient_id   
																				inner join obs o on e.encounter_id=o.encounter_id 
																		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																				and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime < :startDate
																				group by p.patient_id
															) ultimo_levantamento group by patient_id
													) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
													where ultimo_levantamento.data_ultimo_levantamento < :startDate
											) saidas_por_transferencia on inicio.patient_id = saidas_por_transferencia.patient_id 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date<= date_add(:startDate, interval -1 day) and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (8,10) and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366,1709)       
																	and o.obs_datetime<= date_add(:startDate, interval -1 day)  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date<=date_add(:startDate, interval -1 day)
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime<= date_add(:startDate, interval -1 day)  and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
															union
															select saidas_por_transferencia.patient_id, data_estado 
															from (
																		select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																		from (
																					select distinct max_estado.patient_id, max_estado.data_estado 
																					from (          
																								select pg.patient_id, max(ps.start_date) data_estado
																								from patient p   
																										inner join person pe on pe.person_id = p.patient_id 
																										inner join patient_program pg on p.patient_id = pg.patient_id         
																										inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																								where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																										and ps.start_date<= date_add(:startDate, interval -1 day)  and pg.location_id =:location group by pg.patient_id       
																							 ) max_estado            
																							inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																							inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																					where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location   
																					union 
																					select  p.patient_id,max(o.obs_datetime) data_estado   
																					from patient p       
																							inner join person pe on pe.person_id = p.patient_id 
																							inner join encounter e on p.patient_id=e.patient_id 
																							inner join obs  o on e.encounter_id=o.encounter_id  
																					where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																							and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																							and o.obs_datetime<= date_add(:startDate, interval -1 day) and e.location_id=:location
																							group by p.patient_id   
																					union 
																					select ultimabusca.patient_id, ultimabusca.data_estado      
																					from (
																								select p.patient_id,max(e.encounter_datetime) data_estado             
																								from patient p  
																										inner join person pe on pe.person_id = p.patient_id 
																										inner join encounter e on p.patient_id=e.patient_id 
																										inner join obs o on o.encounter_id=e.encounter_id   
																								where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= date_add(:startDate, interval -1 day)   
																										and e.encounter_type = 21 and  e.location_id= :location           
																										group by p.patient_id         
																							) ultimabusca       
																							inner join encounter e on e.patient_id = ultimabusca.patient_id       
																							inner join obs o on o.encounter_id = e.encounter_id 
																					where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id = :location 
																		) saidas_por_transferencia 
																			group by patient_id 
															) saidas_por_transferencia
															left join
															(  
																	select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
																	from (
																				select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																				from (
																							select p.patient_id, max(encounter_datetime) data_fila     
																							from patient p     
																									inner join person pe on pe.person_id = p.patient_id   
																									inner join encounter e on e.patient_id=p.patient_id   
																							where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																									and e.location_id=:location and e.encounter_datetime <= date_add(:endDate, interval -3 month)          
																									group by p.patient_id 
																				) ultimo_fila  
																				left join            
																				obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																				and obs_fila.voided=0   
																				and obs_fila.obs_datetime=ultimo_fila.data_fila      
																				and obs_fila.concept_id=5096           
																				and obs_fila.location_id=:location  
																				union
																				select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																				from patient p     
																						inner join person pe on pe.person_id = p.patient_id   
																						inner join encounter e on p.patient_id=e.patient_id   
																						inner join obs o on e.encounter_id=o.encounter_id 
																				where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																						and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= date_add(:endDate, interval -3 month) 
																						group by p.patient_id
																	) ultimo_levantamento group by patient_id
															) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
															where ultimo_levantamento.data_ultimo_levantamento <= date_add(:endDate, interval -3 month)
																		  
													) allsaida        
															group by patient_id 
											) saida on inicio.patient_id = saida.patient_id            
											left join             
											( 		select p.patient_id,max(encounter_datetime) data_fila      
													from patient p     
														   inner join person pe on pe.person_id = p.patient_id   
														   inner join encounter e on e.patient_id=p.patient_id   
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
															and e.location_id=:location and e.encounter_datetime<=:endDate          
															group by p.patient_id 
											) max_fila on inicio.patient_id=max_fila.patient_id       
											left join            
											(		select  p.patient_id,max(encounter_datetime) data_seguimento            
													from patient p     
															inner join person pe on pe.person_id = p.patient_id   
															inner join encounter e on e.patient_id=p.patient_id   
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)          
															and e.location_id=:location and e.encounter_datetime<=:endDate          
															group by p.patient_id 
											) max_consulta on inicio.patient_id=max_consulta.patient_id   
											left join            
											(		select  p.patient_id,max(value_datetime) data_recepcao_levantou             
													from    patient p     
															inner join person pe on pe.person_id = p.patient_id   
															inner join encounter e on p.patient_id=e.patient_id   
															inner join obs o on e.encounter_id=o.encounter_id 
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
															and o.concept_id=23866 and o.value_datetime is not null              
															and  o.value_datetime<=:endDate and e.location_id=:location
															group by p.patient_id 
											) max_recepcao on inicio.patient_id=max_recepcao.patient_id  
												group by inicio.patient_id             
						) inicio_fila_seg     
						left join            
						obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id
								and obs_fila.voided=0   
								and obs_fila.obs_datetime=inicio_fila_seg.data_fila      
								and obs_fila.concept_id=5096           
								and obs_fila.location_id=:location     
						left join            
						obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id   
								and obs_seguimento.voided=0             
								and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento             
								and obs_seguimento.concept_id=1410      
								and obs_seguimento.location_id=:location
								group by inicio_fila_seg.patient_id     
			) inicio_fila_seg_prox
					group by patient_id   
) coorte12meses_final 
where  ( (data_saida_obito is null or (data_saida_obito is not null and  data_usar_c > data_saida_obito))  and data_entrada_obito >= data_usar_c and data_entrada_obito between :startDate and :endDate)