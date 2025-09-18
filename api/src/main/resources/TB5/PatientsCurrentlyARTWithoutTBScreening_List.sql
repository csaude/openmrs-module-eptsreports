select coorte12meses_final.*,
		coorte12meses_final.data_fila as LAST_FILA, 
		coorte12meses_final.data_seguimento as ULTIMO_SEGUIMENTO,
		max(obs_seguimento.value_datetime) as PROXIMO_SEGUIMENTO,		
		case obs_dispensa.value_coded 
			when 165175 then 'HORARIO NORMAL DE EXPEDIENTE'
			when 165176 then 'FORA DO HORÁRIO'
			when 165177 then 'FARMAC/FARMÁCIA PRIVADA'
			when 165178 then 'DISPENSA COMUNITÁRIA VIA PROVEDOR'
			when 165179 then 'DISPENSA COMUNITARIA VIA APE'  
			when 165180 then 'BRIGADAS MÓVEIS DIURNAS'
			when 165181 then 'BRIGADAS MOVEIS NOTURNAS(HOTSPOTS)'
			when 165182 then 'CLINICAS MOVEIS DIURNAS'  
			when 165183 then 'CLINICAS MOVEIS NOTURNAS(HOTSPOTS)'
			else 'N/A' 
		end as TIPO_DESPENSA_FILA,
		case obs_seguimento_dispensa.value_coded 
			when 1098  then 'DM'
			when 23720 then 'DT'
			when 23888 then 'DS'
			else 'N/A' 
		end as TIPO_DISPENSA_SEGUIMENTO
		
		from(
		select coorte12meses_final.patient_id as PATIENT_ID , 
					pid.identifier as NID,
					concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
					p.gender as GENDER, 
					floor(datediff(:endDate,birthdate)/365) AGE,
					pad3.address6 as 'localidade',
					pad3.address5 as 'bairro',
					ponto_referencia.value as 'pontoReferencia', 
					pat.value as CONTACTO,                                               
					gravidaLactante.decisao PREG_LAC,
					DATE_FORMAT(DATE(coorte12meses_final.data_inicio), '%d/%m/%Y') as INIT_ARV, 
					DATE_FORMAT(DATE(coorte12meses_final.data_recepcao_levantou), '%d/%m/%Y') as LAST_RECEPCAO,
					DATE_FORMAT(DATE(coorte12meses_final.data_proximo_lev), '%d/%m/%Y') as NEXT_FILA,
					DATE_FORMAT(DATE(coorte12meses_final.data_recepcao_levantou30), '%d/%m/%Y')   as PROXIMO_RECEPCAO,
					DATE_FORMAT(DATE(coorte12meses_final.data_fila), '%d/%m/%Y') as data_fila,
					DATE_FORMAT(DATE(coorte12meses_final.data_seguimento), '%d/%m/%Y') as data_seguimento,
					DATE_FORMAT(DATE(TB_SCREENED.data_inicio), '%d/%m/%Y') as TB_SCREENED_DATE,
					DATE_FORMAT(DATE(TB_START.data_inicio), '%d/%m/%Y') as TB_START_DATE,
					DATE_FORMAT(DATE(MDC.encounter_datetime), '%d/%m/%Y') as DATA_MDC ,
					MDC.MDC1 as MDC1,
					MDC.MDC2 as MDC2,
					MDC.MDC3 as MDC3,
					MDC.MDC4 AS MDC4,
					MDC.MDC5 as MDC5 				
		from	(
		select coorte12meses_final.*
		from ( 
				select tx_curr.*                                                                                                   
				from	(
						select *                                                                                                   
						from	(
									select inicio_fila_seg_prox.*,     
										GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30), COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_usar 
									from (
												select inicio_fila_seg.*,                                                                                          
													max(obs_fila.value_datetime) data_proximo_lev,  
													date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30
												from ( 
															select inicio.*,                                                                                                                    
																saida.data_estado,                                                                                                          
																max_fila.data_fila,
																max_consulta.data_seguimento,   
																max_recepcao.data_recepcao_levantou                                                                                         
															from (   
																			select patient_id,data_inicio from 
							(
								  select patient_id, min(data_inicio) data_inicio 
								  from 
										(
											  select p.patient_id, min(e.encounter_datetime) data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on o.encounter_id=e.encounter_id 
											  where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) 
													and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location 
													group by p.patient_id 
											  union 
											  
											  select p.patient_id, min(value_datetime) data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
											  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) 
													and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
													group by p.patient_id 
											  
											  union 
								   
											  select pg.patient_id, min(date_enrolled) data_inicio 
											  from patient p 
													inner join patient_program pg on p.patient_id=pg.patient_id 
											  where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
													group by pg.patient_id 
											  
											  union 
							
											  select e.patient_id, min(e.encounter_datetime) as data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
											  where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
													group by p.patient_id 
											  
											  union 
								   
											  select p.patient_id, min(value_datetime) data_inicio 
											  from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
											  where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
													and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location 
													group by p.patient_id
										) 
								  art_start group by patient_id 
							) tx_new			
															)
														inicio                                                                                                                                    
														left join                                                                                                                                  
														( 
															select patient_id,max(data_estado) data_estado                                                                                              
															from (                                                                                                                                        
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (                                                                
																						select  pg.patient_id,                                                                                                          
																							max(ps.start_date) data_estado                                                                                          
																						from patient p                                                                                                               
																						 inner join patient_program pg on p.patient_id = pg.patient_id                                                               
																						 inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
																					 where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
																						 and ps.start_date<= :endDate and pg.location_id =:location group by pg.patient_id                                           
																			) 
																		max_estado                                                                                                                        
																			 inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
																			 inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																		 where pp.program_id = 2 and ps.state = 8 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location                 
																		 
																		union                                                                                                                               
													 
																		 select dead_state.patient_id, dead_state.data_estado 
																		 from (
																						select patient_id,max(data_estado) data_estado                                                                                              
																						from (  
																									select distinct max_estado.patient_id, max_estado.data_estado 
																									from (                                                                
																												select  pg.patient_id,                                                                                                          
																													max(ps.start_date) data_estado                                                                                          
																												from patient p                                                                                                               
																													inner join patient_program pg on p.patient_id = pg.patient_id                                                               
																													inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
																												where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
																													and ps.start_date<= :endDate and pg.location_id =:location 
																													group by pg.patient_id                                           
																									) 
																								max_estado                                                                                                                        
																									inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
																									inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																								where pp.program_id = 2 and ps.state = 10 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location  
																						
																								union
																								
																								select  p.patient_id,                                                                                                               
																									max(o.obs_datetime) data_estado                                                                                             
																								from patient p                                                                                                                   
																									inner join encounter e on p.patient_id=e.patient_id                                                                         
																									inner join obs  o on e.encounter_id=o.encounter_id                                                                          
																								where e.voided=0 and o.voided=0 and p.voided=0                                                                
																									and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1366                         
																									and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
																									group by p.patient_id                                                                                                               
																								
																								union                                                                                                                               
																								
																								select person_id as patient_id,death_date as data_estado                                                                            
																								from person                                                                                                                         
																								where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate 
																								
																								union                                                                                                                               
																								
																								select  p.patient_id,                                                                                                               
																									max(obsObito.obs_datetime) data_estado                                                                                      
																								from patient p                                                                                                                   
																									inner join encounter e on p.patient_id=e.patient_id                                                                         
																									inner join obs obsObito on e.encounter_id=obsObito.encounter_id                                                             
																								where e.voided=0 and p.voided=0 and obsObito.voided=0                                                        
																									and e.encounter_type in (21,36,37) and  e.encounter_datetime<=:endDate and  e.location_id=:location                          
																									and obsObito.concept_id in (2031,23944,23945) and obsObito.value_coded=1366                                                     
																									group by p.patient_id                                                                                                               
																						) 
																					dead_state group by dead_state.patient_id  
																		) 
																	dead_state 
																	inner join
																	(
																		select fila_seguimento.patient_id,max(fila_seguimento.data_encountro) data_encountro
																		from (
																					select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
																					from patient p                                                                                                                                   
																						inner join encounter e on e.patient_id=p.patient_id                                                                                         
																					where p.voided=0 and e.voided=0 and e.encounter_type=18                                                                      
																						and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
																						group by p.patient_id  
																					
																					union
																					
																					select p.patient_id,max(encounter_datetime) data_encountro                                                                                    
																					from patient p                                                                                                                                   
																						inner join encounter e on e.patient_id=p.patient_id                                                                                         
																					where p.voided=0 and e.voided=0 and e.encounter_type in (6,9)                                                                
																						and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
																						group by p.patient_id   
																		) fila_seguimento	group by fila_seguimento.patient_id  
																	 ) 
																fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
																	where fila_seguimento.data_encountro is null or  fila_seguimento.data_encountro <= dead_state.data_estado
														
																union
																
																select  p.patient_id,                                                                                                               
																	max(o.obs_datetime) data_estado                                                                                             
																from patient p                                                                                                                   
																	inner join encounter e on p.patient_id=e.patient_id                                                                         
																	inner join obs  o on e.encounter_id=o.encounter_id                                                                          
																where   e.voided=0 and o.voided=0 and p.voided=0                                                               
																	and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1709                         
																	and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
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
																										inner join patient_program pg on p.patient_id = pg.patient_id                                                               
																										inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
																									where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
																										and ps.start_date<= :endDate and pg.location_id =:location group by pg.patient_id                                           
																						) 
																					max_estado                                                                                                                        
																						inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
																						inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																					where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location                 
																			 
																					union                                                                                                                               
																					
																					select  p.patient_id,max(o.obs_datetime) data_estado                                                                                             
																					from patient p                                                                                                                   
																						inner join encounter e on p.patient_id=e.patient_id                                                                         
																						inner join obs o on e.encounter_id=o.encounter_id                                                                          
																					where e.voided=0 and o.voided=0 and p.voided=0                                                                
																						and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706                         
																						and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
																						group by p.patient_id                                                                                                               
																					
																					union                                                                                                                               
																					
																					select ultimaBusca.patient_id, ultimaBusca.data_estado                                                                              
																					from (                                                                                                                              
																								select p.patient_id,max(e.encounter_datetime) data_estado                                                                   
																								from patient p                                                                                                              
																									inner join encounter e on p.patient_id=e.patient_id                                                                     
																									inner join obs o on o.encounter_id=e.encounter_id                                                                       
																								where e.voided=0 and p.voided=0 and e.encounter_datetime<= :endDate                                       
																									and e.encounter_type = 21 and  e.location_id= :location                                                                 
																									group by p.patient_id                                                                                                   
																					) 
																				ultimaBusca                                                                                                                   
																						inner join encounter e on e.patient_id = ultimaBusca.patient_id                                                             
																						 inner join obs o on o.encounter_id = e.encounter_id                                                                         
																					where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimaBusca.data_estado = e.encounter_datetime and e.location_id = :location 
																			) saidas_por_transferencia group by patient_id 
																) 
															saidas_por_transferencia
																left join
																(  
																	select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
																	from (
																				select p.patient_id, date_add(max(o.value_datetime), interval 1 day) data_ultimo_levantamento                                                                                            
																				from patient p                                                                                                                                   
																					inner join encounter e on e.patient_id= p.patient_id 
																					inner join obs o on o.encounter_id = e.encounter_id                                                                                        
																				where p.voided= 0 and e.voided=0 and o.voided = 0 and e.encounter_type=18 and o.concept_id = 5096                                                                  
																					and e.location_id=:location and e.encounter_datetime <= :endDate                                                                               
																					group by p.patient_id 
																				
																				union
																				
																				select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
																				from patient p                                                                                                                                   
																					inner join encounter e on p.patient_id=e.patient_id                                                                                         
																					inner join obs o on e.encounter_id=o.encounter_id                                                                                           
																				where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
																					and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate                                                                                        
																					group by p.patient_id
																	) ultimo_levantamento group by patient_id
																) 
																ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
																where ultimo_levantamento.data_ultimo_levantamento <= :endDate
															) allSaida group by patient_id 
											) saida on inicio.patient_id = saida.patient_id                                                                                                      
									 left join                                                                                                                                           
									  ( select p.patient_id,max(encounter_datetime) data_fila                                                                                                
										from patient p                                                                                                                                   
											inner join encounter e on e.patient_id=p.patient_id                                                                                         
										where p.voided=0 and e.voided=0 and e.encounter_type=18                                                                      
											and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
											group by p.patient_id                                                                                                                               
									 ) max_fila on inicio.patient_id=max_fila.patient_id
									 left join ( 
										select  p.patient_id,max(encounter_datetime) data_seguimento                                          
										from patient p                                                                 
											inner join encounter e on e.patient_id=p.patient_id                                             
										where p.voided=0 and e.voided=0 and e.encounter_type in (6,9)                                          
											and e.location_id= :location and e.encounter_datetime<=:endDate                                         
											group by p.patient_id                                                               
										) max_consulta on inicio.patient_id=max_consulta.patient_id                                              
									 left join                                                                                                                                          
									  (                                                                                                                                                  
										select  p.patient_id,max(value_datetime) data_recepcao_levantou                                                                                     
										from patient p                                                                                                                                   
											inner join encounter e on p.patient_id=e.patient_id                                                                                         
											inner join obs o on e.encounter_id=o.encounter_id                                                                                           
										where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
											and o.concept_id=23866 and o.value_datetime is not null                                                                                      
											and o.value_datetime<=:endDate and e.location_id=:location                                                                                      
											group by p.patient_id                                                                                                                               
									  ) max_recepcao on inicio.patient_id=max_recepcao.patient_id                                                                                        
										group by inicio.patient_id                                                                                                                         
								   ) inicio_fila_seg
								left join                                                                                                                                          
									encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=inicio_fila_seg.patient_id                                                                                      
										and ultimo_fila_data_criacao.voided=0                                     
										and ultimo_fila_data_criacao.encounter_type = 18  
										and ultimo_fila_data_criacao.encounter_datetime = inicio_fila_seg.data_fila                                                                                            
										and ultimo_fila_data_criacao.location_id=:location                     
								left join                                                                                                                                          
									obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id                                                                                      
										and obs_fila.voided=0                                                                                                                             
										and (obs_fila.obs_datetime=inicio_fila_seg.data_fila  or (ultimo_fila_data_criacao.date_created = obs_fila.date_created and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id ))                                                                                                       
										and obs_fila.concept_id=5096                                                                                                                       
										and obs_fila.location_id=:location  
											  
										group by inicio_fila_seg.patient_id                                                                                                                 
								) inicio_fila_seg_prox                                                                                                                              
									group by patient_id                                                                                                                                 
						) coorte12meses_final                                                                                                                               
							where (data_estado is null or (data_estado is not null and  data_fila > data_estado)) and date_add(data_usar, interval 28 day) >=:endDate 
				)tx_curr
				left join
				(
					select patient_id 
					from(
								select  p.patient_id,o.value_datetime data_inicio                                                           
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id                                                           
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
									  e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id= :location 
									 and  o.value_datetime  between (:endDate - interval 6 month) and :endDate
								
								union
								
								select  p.patient_id,e.encounter_datetime data_inicio                                                           
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id                                                           
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
									  e.encounter_type in (6,9) and o.concept_id=6257 
									  and o.value_coded in(1065,1066)  and e.location_id= :location 
									  and  e.encounter_datetime  between (:endDate - interval 6 month) and :endDate
								
								union
								
								select  p.patient_id,e.encounter_datetime data_inicio                                                           
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id                                                           
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
									  e.encounter_type in (6,9) and o.concept_id=6277 
									  and o.value_coded = 703  and e.location_id= :location 
									  and  e.encounter_datetime  between (:endDate - interval 6 month) and :endDate
								union
								
								select  p.patient_id,e.encounter_datetime data_inicio                                                           
								from patient p                                                                                                   
									inner join encounter e on p.patient_id=e.patient_id                                                         
									inner join obs obs6277 on obs6277.encounter_id= e.encounter_id 
									inner join obs obs6257 on obs6257.encounter_id= e.encounter_id                                                           
								where p.voided = 0 and e.voided = 0 and obs6277.voided = 0 and obs6257.voided = 0                                                                  
									and e.encounter_type in (6,9) and obs6277.concept_id=6277 and obs6277.value_coded = 664 and e.location_id= :location and obs6257.concept_id= 6257 and obs6257.value_coded in(1065,1066)
									and e.encounter_datetime  between (:endDate - interval 6 month) and :endDate
						
								union
		
								select  p.patient_id,o.obs_datetime data_inicio                                                           
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id                                                           
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
									  e.encounter_type=53 and o.concept_id=1406 
									  and o.value_coded=42  and e.location_id= :location 
									  and  o.obs_datetime  between (:endDate - interval 6 month) and :endDate
								union
								
								select  pg.patient_id,date_enrolled data_inicio                                                             
								from    patient p 
										inner join patient_program pg on p.patient_id=pg.patient_id                                       
								where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location  
										and date_enrolled  between (:endDate - interval 6 month) and :endDate
								union
		
								select  p.patient_id, o.obs_datetime data_inicio                                                           
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id                                                           
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
									  e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location  
									  and o.obs_datetime between (:endDate - interval 6 month) and :endDate
								union
								
								select  p.patient_id,e.encounter_datetime data_inicio                                                           
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id                                                           
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
									  e.encounter_type=6 and o.concept_id=23758 and o.value_coded in(1065,1066)  and e.location_id= :location  
									  and e.encounter_datetime between (:endDate - interval 6 month) and :endDate
									  
								union
								
								select  p.patient_id,e.encounter_datetime data_inicio                                                           
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id                                                           
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
									  e.encounter_type=6 and o.concept_id=23761 and o.value_coded=1065  and e.location_id= :location  
									  and e.encounter_datetime between (:endDate - interval 6 month) and :endDate
									  
								union
								
								select  p.patient_id,e.encounter_datetime data_inicio                                                           
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
									  e.encounter_type in (6) and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,23760,161)   
									  and e.location_id= :location  and e.encounter_datetime  between (:endDate - interval 6 month) and :endDate
									  
								union
								select  p.patient_id,e.encounter_datetime data_inicio                                                      
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
									  e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   
									  and e.location_id= :location and e.encounter_datetime between (:endDate - interval 6 month) and :endDate
							
							union
		
								select  p.patient_id,e.encounter_datetime data_inicio                                                    
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
									  e.encounter_type=6 and o.concept_id in(23723, 23774, 23951, 307, 12) and o.value_coded is not null
									  and e.location_id= :location and e.encounter_datetime between (:endDate - interval 6 month) and :endDate
								union
		
								select  p.patient_id,e.encounter_datetime data_inicio                                                    
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
									  e.encounter_type=13 and o.concept_id in(307, 23723, 23774, 23951, 165588) and o.value_coded is not null
									  and e.location_id= :location and e.encounter_datetime between (:endDate - interval 6 month) and :endDate
		
								union
		
								select  p.patient_id,e.encounter_datetime data_inicio                                                    
								from    patient p                                                                                                   
									  inner join encounter e on p.patient_id=e.patient_id                                                         
									  inner join obs o on o.encounter_id=e.encounter_id
								where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
									  e.encounter_type=51 and o.concept_id in(23951) and o.value_coded is not null
									  and e.location_id= :location and e.encounter_datetime between (:endDate - interval 6 month) and :endDate
					) ScreenedForTB
				) ScreenedForTB on tx_curr.patient_id = ScreenedForTB.patient_id
			where  ScreenedForTB.patient_id is null
		) coorte12meses_final group by coorte12meses_final.patient_id
		) coorte12meses_final 
		inner join person p on p.person_id=coorte12meses_final.patient_id
		left join (
				 select     
					f.patient_id,f.encounter_datetime as encounter_datetime,
					@num_mdc := 1 + LENGTH(f.MDC) - LENGTH(REPLACE(f.MDC, ',', '')) AS MDC,  
					SUBSTRING_INDEX(f.MDC, ',', 1) AS MDC1,  
					IF(@num_mdc > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(f.MDC, ',', 2), ',', -1), '') AS MDC2,  
					IF(@num_mdc > 2, SUBSTRING_INDEX(SUBSTRING_INDEX(f.MDC, ',', 3), ',', -1), '') AS MDC3,  
					IF(@num_mdc > 3, SUBSTRING_INDEX(SUBSTRING_INDEX(f.MDC, ',', 4), ',', -1), '') AS MDC4, 
					IF(@num_mdc > 4, SUBSTRING_INDEX(SUBSTRING_INDEX(f.MDC, ',', 5), ',', -1), '') AS MDC5
				 from (   
				   select f.patient_id,max(f.encounter_datetime) as encounter_datetime,group_concat(f.MDC) as MDC from 
					(
		   
				   select  distinct e.patient_id,e.encounter_datetime encounter_datetime,
							case o.value_coded
								when  23730  then 'DISPENSA TRIMESTRAL (DT)'
								when  23888  then 'DISPENSA SEMESTRAL'
								when 165314  then 'DISPENSA ANUAL DE ARV'
								when 165315  then 'DISPENSA DESCENTRALIZADA DE ARV'
								when 165178  then 'DISPENSA COMUNITÁRIA VIA PROVEDOR'
								when 165179  then 'DISPENSA COMUNITARIA VIA APE'
								when 165264  then 'BRIGADAS MOVEIS (DCBM)'
								when 165265  then 'CLINICAS MOVEIS (DCCM)'
								when  23725  then 'ABORDAGEM FAMILIAR (AF)'
								when  23729  then 'FLUXO RÁPIDO (FR)'
								when  23724  then 'GAAC'
								when  23726  then 'CLUBES DE ADESÃO (CA)'
								when 165316  then 'EXTENSAO DE HORARIO'
								when 165317  then 'PARAGEM UNICA NO SECTOR DA TB'
								when 165318  then 'PARAGEM UNICA NOS SERVICOS DE TARV' 
								when 165319  then 'PARAGEM UNICA NO SAAJ'
								when 165320  then  'PARAGEM UNICA NA SMI'
								when 165321  then  'DOENCA AVANCADA POR HIV'
								when 23727  then  'PARAGEM ÚNICA (PU)'
								when 165340 then  'DB - DISPENSA BIMESTRAL'
								when 23730 then  'DT - DISPENSA TRIMESTRAL DE ARV'
							else null end  as MDC
				 from  encounter e  
						join obs grupo on grupo.encounter_id=e.encounter_id 
						join obs o on o.encounter_id=e.encounter_id 
						join obs obsEstado on obsEstado.encounter_id=e.encounter_id
				where   grupo.concept_id=165323 
						and o.concept_id=165174 
						and e.encounter_type in(6,9) 
						and e.location_id= :location 
						and obsEstado.concept_id=165322 
						and o.obs_group_id = grupo.obs_id
						and obsEstado.obs_group_id = grupo.obs_id 
						and obsEstado.value_coded in(1256,1257) 
						and obsEstado.voided=0 
						and o.voided=0 
						and grupo.voided=0
						and e.encounter_datetime <= CURDATE()
						order by e.encounter_datetime
						)f
						group by f.patient_id,f.encounter_datetime  order by f.encounter_datetime desc
						)f
				  group by f.patient_id order by @num_mdc            
				  ) MDC on coorte12meses_final.patient_id = MDC.patient_id
		   
		left join ( 
			select pad1.*  from person_address pad1  
				inner join ( select person_id,max(person_address_id) id   from person_address where voided=0 group by person_id ) pad2  
			where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
		) pad3 on pad3.person_id=coorte12meses_final.patient_id 
		left join (select pa.person_id, pa.value from person_attribute pa where pa.person_attribute_type_id  = 31 and pa.voided = 0)ponto_referencia on coorte12meses_final.patient_id = ponto_referencia.person_id
		left join ( 
			select pn1.*  from person_name pn1  
				inner join ( select person_id,min(person_name_id) id   from person_name where voided=0 group by person_id) pn2  
			where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
		) pn on pn.person_id=coorte12meses_final.patient_id 
		left join ( 
			select pid1.*  from patient_identifier pid1  
				inner join  ( select patient_id,min(patient_identifier_id) id  from patient_identifier where voided=0 group by patient_id ) pid2 
		    where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
		) pid on pid.patient_id=coorte12meses_final.patient_id
		left join person_attribute pat on pat.person_id=coorte12meses_final.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
		
		left join (
							            
					select final.patient_id, decisao
					from (
							select 
									pe.person_id as patient_id,
									pe.gender, 
									gravida_real.data_gravida,
									lactante_real.data_parto,
									if(lactante_real.data_parto is null, 'GRÁVIDA',  if(gravida_real.data_gravida is null, 'LACTANTE',  if(gravida_real.data_gravida>=lactante_real.data_parto, 'GRÁVIDA', 'LACTANTE'))) decisao 
				            from person pe
				            left join (
									select gravida_real.patient_id,max(gravida_real.data_gravida) data_gravida  
									from ( 
												select p.patient_id,e.encounter_datetime data_gravida 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
												where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location
												
												union 
												select p.patient_id,e.encounter_datetime data_gravida 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
												where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location
												
												union 
												
												select  p.patient_id,o.value_datetime data_gravida 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
												where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location
												
												union 
												
												select p.patient_id,e.encounter_datetime data_gravida 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
												where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location
											
												union 
												
												select pp.patient_id,pp.date_enrolled data_gravida 
												from patient_program pp 
												where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between (curdate() - INTERVAL 9 MONTH) and curdate() and pp.location_id=:location
												
												union 
												
												select p.patient_id,obsART.value_datetime data_gravida 
												from patient p 
													inner join encounter e on p.patient_id=e.patient_id 
													inner join obs o on e.encounter_id=o.encounter_id 
													inner join obs obsART on e.encounter_id=obsART.encounter_id 
												where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065  and e.location_id=:location
													and e.encounter_type=53 and obsART.value_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location and obsART.concept_id=1190 and obsART.voided=0 
												
												union 
												
												select p.patient_id,data_colheita.value_datetime data_gravida 
												from patient p                                                                                                                                                                        
													inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
													inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
													inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
												where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
													and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
													and data_colheita.concept_id =23821 and data_colheita.value_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id= :location                                                      
									) 
								gravida_real 
									group by gravida_real.patient_id
				            ) gravida_real on pe.person_id = gravida_real.patient_id 
						left join(
								select lactante_real.patient_id,max(lactante_real.data_parto) data_parto  
								from ( 
											select p.patient_id,o.value_datetime data_parto 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
											where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599  
												and e.encounter_type in (5,6) and o.value_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and e.location_id=:location
											
											union 
											
											select p.patient_id, e.encounter_datetime data_parto 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
											where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and e.encounter_type=6 and e.encounter_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and e.location_id=:location
											
											union
											
											select p.patient_id, e.encounter_datetime data_parto 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
												where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332  
											and e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and e.location_id=:location
											
											union  
											
											select pg.patient_id,ps.start_date data_parto 
											from patient p 
												inner join patient_program pg on p.patient_id=pg.patient_id 
												inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
											where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=8 and ps.state=27  and ps.start_date between (curdate() - INTERVAL 18 MONTH) and curdate() and location_id=:location
											
											union 
											select p.patient_id, obsART.value_datetime data_parto 
											from patient p 
												inner join encounter e on p.patient_id=e.patient_id 
												inner join obs o on e.encounter_id=o.encounter_id 
												inner join obs obsART on e.encounter_id=obsART.encounter_id 
											where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and e.encounter_type=53 and e.location_id=:location  
												and obsART.value_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and obsART.concept_id=1190 and obsART.voided=0 
											
											union
											
											select p.patient_id,data_colheita.value_datetime data_gravida 
											from patient p                                                                                                                                                                        
												inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
												inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id
												inner join obs data_colheita on e.encounter_id=data_colheita.encounter_id                                                                                                                                                                           
											where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982                                                                                                                                 
												and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                                                                                                         
												and data_colheita.concept_id =23821 and data_colheita.value_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and e.location_id= :location
								) 
							lactante_real 
								group by lactante_real.patient_id
						)
					lactante_real on lactante_real.patient_id=pe.person_id 
						where (lactante_real.data_parto is not null or gravida_real.data_gravida is not null ) and pe.gender='F'
							group by pe.person_id
				)final
		     ) gravidaLactante on coorte12meses_final.patient_id = gravidaLactante.patient_id
			   left join (
					select patient_id, max(data_inicio) data_inicio 
					from(
						select  p.patient_id, max(o.value_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0                                                                 
							 and e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id= :location 
							 and  o.value_datetime  <= :endDate
							 group by p.patient_id
						
						union
						
						select  p.patient_id, max(e.encounter_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0                                                                 
							  and e.encounter_type in (6,9) and o.concept_id=6257 
							  and o.value_coded in(1065,1066)  and e.location_id= :location 
							  and  e.encounter_datetime  <= :endDate
							  group by p.patient_id
						union
						
						select  p.patient_id, max(e.encounter_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0                                                                 
							 and e.encounter_type in (6,9) and o.concept_id=6277 
							 and o.value_coded = 703  and e.location_id= :location 
							 and e.encounter_datetime  <= :endDate
							 group by p.patient_id
						union
						
						select  p.patient_id, max(e.encounter_datetime) data_inicio                                                           
						from patient p                                                                                                   
							inner join encounter e on p.patient_id=e.patient_id                                                         
							inner join obs obs6277 on obs6277.encounter_id= e.encounter_id 
							inner join obs obs6257 on obs6257.encounter_id= e.encounter_id                                                           
						where p.voided = 0 and e.voided = 0 and obs6277.voided = 0 and obs6257.voided = 0                                                                  
							and e.encounter_type in (6,9) and obs6277.concept_id=6277 and obs6277.value_coded = 664 and e.location_id= :location and obs6257.concept_id= 6257 and obs6257.value_coded in(1065,1066)
							and e.encounter_datetime  <= :endDate
							group by p.patient_id
							
						union
		
						select  p.patient_id, max(o.obs_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
							  e.encounter_type=53 and o.concept_id=1406 
							  and o.value_coded=42  and e.location_id= :location 
							  and  o.obs_datetime  between (:endDate - interval 6 month) and :endDate
							  group by p.patient_id
						union
						
						select  pg.patient_id, max(date_enrolled) data_inicio                                                             
						from    patient p 
								inner join patient_program pg on p.patient_id=pg.patient_id                                       
						where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location  
							   and date_enrolled  <= :endDate
							   group by p.patient_id
						union
		
						select  p.patient_id, max(o.obs_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
							  e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location  
							  and o.obs_datetime <= :endDate
							  group by p.patient_id
						union
						
						select  p.patient_id, max(e.encounter_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
							  e.encounter_type=6 and o.concept_id=23758 and o.value_coded in(1065,1066)  and e.location_id= :location  
							  and e.encounter_datetime <= :endDate
							 group by p.patient_id 
						union
						
						select p.patient_id, max(e.encounter_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
							  e.encounter_type=6 and o.concept_id=23761 and o.value_coded=1065  and e.location_id= :location  
							  and e.encounter_datetime <= :endDate
							  group by p.patient_id
						union
						
						select  p.patient_id, max(e.encounter_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
							  e.encounter_type in (6) and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,23760,161)   
							  and e.location_id= :location  and e.encounter_datetime  <= :endDate
							  group by p.patient_id
						union
						select  p.patient_id, max(e.encounter_datetime) data_inicio                                                      
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
							  e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   
							  and e.location_id= :location and e.encounter_datetime <= :endDate
							  group by p.patient_id
						union
						select p.patient_id, max(e.encounter_datetime) data_inicio                                                    
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
							  e.encounter_type=6 and o.concept_id in(23723, 23774, 23951, 307, 12) and o.value_coded is not null
							  and e.location_id= :location and e.encounter_datetime <= :endDate
							  group by p.patient_id
						union
		
						select  p.patient_id, max(e.encounter_datetime) data_inicio                                                    
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
							  e.encounter_type=13 and o.concept_id in(307, 23723, 23774, 23951, 165588) and o.value_coded is not null
							  and e.location_id= :location and e.encounter_datetime <= :endDate
							group by p.patient_id
						union
		
						select p.patient_id, max(e.encounter_datetime) data_inicio                                                    
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                            
							  e.encounter_type=51 and o.concept_id in(23951) and o.value_coded is not null
							  and e.location_id= :location and e.encounter_datetime <= :endDate
							group by p.patient_id
					) ScreenedForTB group by ScreenedForTB.patient_id
			   )TB_SCREENED on  coorte12meses_final.patient_id = TB_SCREENED.patient_id 
			   left join (
					select patient_id, max(data_inicio) data_inicio 
					from(
						select  p.patient_id, max(o.value_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0                                                                 
							 and e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id= :location 
							 and  o.value_datetime  <= :endDate
							 group by p.patient_id
						
						union
						
						select  pg.patient_id, max(date_enrolled) data_inicio                                                             
						from    patient p 
								inner join patient_program pg on p.patient_id=pg.patient_id                                       
						where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location  
							   and date_enrolled  <= :endDate
							   group by p.patient_id
						union
		
						select  p.patient_id, max(o.obs_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
							  e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location  
							  and o.obs_datetime <= :endDate
							  group by p.patient_id
						union
		
						select  p.patient_id, max(o.obs_datetime) data_inicio                                                           
						from    patient p                                                                                                   
							  inner join encounter e on p.patient_id=e.patient_id                                                         
							  inner join obs o on o.encounter_id=e.encounter_id                                                           
						where e.voided=0 and o.voided=0 and p.voided=0 and                                                                
							  e.encounter_type=53 and o.concept_id=1406 
							  and o.value_coded=42  and e.location_id= :location 
							  and  o.obs_datetime  <=  :endDate
							  group by p.patient_id	
				) TB_START group by TB_START.patient_id
			)TB_START on  coorte12meses_final.patient_id = TB_START.patient_id 
		) coorte12meses_final
		left join (
				select maxFila.patient_id,o.obs_datetime, o.value_coded from (
					select p.patient_id, max(e.encounter_datetime) encounter_datetime
			from patient p
		  		inner join encounter e on e.patient_id = p.patient_id
		 	where p.voided = 0 and e.voided = 0 and e.encounter_datetime <= :endDate
		 	 	and e.encounter_type = 18 and e.location_id = :location
		 	 	group by p.patient_id
		 	 	) maxFila inner join
		 	 	 encounter e on maxFila.patient_id = e.patient_id
		  		inner join obs o on o.encounter_id = e.encounter_id
		 	where e.voided = 0 and o.voided = 0 and e.encounter_datetime = maxFila.encounter_datetime
		 	 	and e.encounter_type = 18 and e.location_id = :location and o.concept_id=165174
		) obs_dispensa on coorte12meses_final.patient_id = obs_dispensa.patient_id                                          
		left join (	
			select maxSeguimento.patient_id, o.obs_datetime, o.value_datetime from (
					select p.patient_id, max(e.encounter_datetime) encounter_datetime
			from patient p
		  		inner join encounter e on e.patient_id = p.patient_id
		 	where p.voided = 0 and e.voided = 0 and e.encounter_datetime <= :endDate
		 	 	and e.encounter_type in (6,9) and e.location_id = :location
		 	 	group by p.patient_id
		 	 	) maxSeguimento inner join
		 	 	 encounter e on maxSeguimento.patient_id = e.patient_id
		  		inner join obs o on o.encounter_id = e.encounter_id
		 	where e.voided = 0 and o.voided = 0 and e.encounter_datetime = maxSeguimento.encounter_datetime
		 	 	and e.encounter_type in (6,9) and e.location_id = :location and o.concept_id=1410
		) obs_seguimento on coorte12meses_final.patient_id = obs_seguimento.patient_id
		left join (
			select maxSeguimento.patient_id, o.obs_datetime, o.value_coded from (
					select p.patient_id, max(e.encounter_datetime) encounter_datetime
			from patient p
		  		inner join encounter e on e.patient_id = p.patient_id
		 	where p.voided = 0 and e.voided = 0 and e.encounter_datetime <= :endDate
		 	 	and e.encounter_type in (6,9) and e.location_id = :location
		 	 	group by p.patient_id
		 	 	) maxSeguimento inner join
		 	 	 encounter e on maxSeguimento.patient_id = e.patient_id
		  		inner join obs o on o.encounter_id = e.encounter_id
		 	where e.voided = 0 and o.voided = 0 and e.encounter_datetime = maxSeguimento.encounter_datetime
		 	 	and e.encounter_type in (6,9) and e.location_id = :location and o.concept_id=23739
		) obs_seguimento_dispensa on coorte12meses_final.patient_id = obs_seguimento_dispensa.patient_id  
		group by coorte12meses_final.patient_id     
 