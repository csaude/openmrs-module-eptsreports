                select patient_id 
                        from( 
                         select inicio_fila_seg_prox.*, 
                             GREATEST(COALESCE(data_fila,data_seguimento), 
                             COALESCE(data_seguimento,data_fila), 
                             COALESCE(data_seguimento,data_fila)) data_usar_c, 
             	             GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30), COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_usar 
                         from    ( 
                             select inicio_fila_seg.*, 
                                     max(obs_seguimento.value_datetime) data_proximo_seguimento, 
                                     date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30 
                             from ( 
                                 select inicio.*, 
                                     saida.data_estado, 
                                     saida.decisao, 
                                     max_fila.data_fila, 
                                     max_fila.data_proximo_lev, 
                                     max_consulta.data_seguimento, 
                                     max_recepcao.data_recepcao_levantou 
                                 from ( 
                                         Select patient_id, min(data_inicio) data_inicio 
                                         from ( 
                                             select e.patient_id, min(e.encounter_datetime) as data_inicio 
                                             from patient p 
                                                 inner join encounter e on p.patient_id=e.patient_id 
                                             where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
                                                 group by p.patient_id 
                                             union 
                                             Select p.patient_id,min(value_datetime) data_inicio 
                                             from patient p 
                                                 inner join encounter e on p.patient_id=e.patient_id 
                                                 inner join obs o on e.encounter_id=o.encounter_id 
                                             where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866 
                                                 and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location 
                                                 group by p.patient_id 
                                             ) 
                                             inicio_real group by patient_id 
                                         )inicio 
                                         left join 
                                        ( 
                                           select saidas_por_suspensao.patient_id,saidas_por_suspensao.data_estado, 1 decisao from 
                                                ( 
                                                 select maxEstado.patient_id,maxEstado.data_estado data_estado 
                                                 from( 
                                                     select pg.patient_id,max(ps.start_date) data_estado 
                                                     from patient p 
                                                         inner join patient_program pg on p.patient_id=pg.patient_id 
                                                         inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                                                     where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location 
                                                         group by p.patient_id 
                                                     ) 
                                                 maxEstado 
                                                     inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
                                                     inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
                                                 where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 
                                                     and ps2.start_date=maxEstado.data_estado and pg2.location_id=:location and ps2.state =8 
                                                     union 
                                                select p.patient_id, max(o.obs_datetime) data_estado 
                                                 from patient p 
                                                     inner join encounter e on p.patient_id=e.patient_id 
                                                     inner join obs  o on e.encounter_id=o.encounter_id 
                                                 where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) 
                                                     and o.value_coded in (1709) and o.obs_datetime<=:endDate and e.location_id=:location 
                                                     group by p.patient_id 
                                                  ) saidas_por_suspensao 
							 union
							 select dead_state.patient_id, dead_state.data_estado, dead_state.decisao
							 from (
											select patient_id,max(data_estado) data_estado,decisao                                                                                              
											from (  
											
											select distinct max_estado.patient_id, max_estado.data_estado,2 decisao  
											from (                                                                
														select  pg.patient_id,                                                                                                          
															max(ps.start_date) data_estado                                                                                          
														from patient p                                                                                                               
															inner join person pe on pe.person_id = p.patient_id                                                                         
															inner join patient_program pg on p.patient_id = pg.patient_id                                                               
															inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
														where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                        
															and ps.start_date<=:endDate and pg.location_id =:location group by pg.patient_id                                           
											) max_estado                                                                                                                        
												inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
												inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
											where pp.program_id = 2 and ps.state = 10 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location  
											 union
											select  p.patient_id,max(o.obs_datetime) data_estado,2 decisao                                                                                              
											from patient p                                                                                                                   
												inner join person pe on pe.person_id = p.patient_id                                                                         
												inner join encounter e on p.patient_id=e.patient_id                                                                         
												inner join obs  o on e.encounter_id=o.encounter_id                                                                          
											where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
												and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1366                         
												and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
												group by p.patient_id                                                                                                               
											union                                                                                                                               
											select person_id as patient_id,death_date as data_estado, 2 decisao                                                                            
											from person                                                                                                                         
											where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate 
											                                                                                                           
									) dead_state group by dead_state.patient_id  
							) dead_state 
							inner join
							(
									select fila_seguimento.patient_id,max(fila_seguimento.data_encountro) data_encountro
									from(
												select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
												from    patient p                                                                                                                                   
														inner join person pe on pe.person_id = p.patient_id                                                                                         
														inner join encounter e on e.patient_id=p.patient_id                                                                                         
												where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
														and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
														group by p.patient_id  
												union
												
												select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
												from patient p                                                                                                                                   
													inner join person pe on pe.person_id = p.patient_id                                                                                         
													inner join encounter e on e.patient_id=p.patient_id                                                                                         
												where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)                                                                
													and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
													group by p.patient_id   
									) fila_seguimento	group by fila_seguimento.patient_id  
							 ) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
								where fila_seguimento.data_encountro is null or  fila_seguimento.data_encountro <= dead_state.data_estado

							union

				         select saidas_por_transferencia.patient_id, data_estado, 2 decisao 
	                         from
	                        	(
		                            select saidas_por_transferencia.patient_id, max(data_estado) data_estado
		                            from
		                                (
			                           	select distinct max_estado.patient_id, max_estado.data_estado 
			                           	from 
			                           		(                                                                
				                                 select pg.patient_id, max(ps.start_date) data_estado                                                                                          
				                                 from patient p                                                                                                               
				                                 		inner join person pe on pe.person_id = p.patient_id                                                                         
				                                     	inner join patient_program pg on p.patient_id = pg.patient_id                                                               
				                                     	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
				                                 where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                        
				                                 		and ps.start_date<=:endDate and pg.location_id =:location group by pg.patient_id                                           
			                             		) max_estado                                                                                                                        
			                                 		inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
			                                 		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
			                             		where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location                 
			                             
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
			     
		                                ) saidas_por_transferencia 
	                                	group by patient_id 
                               	) saidas_por_transferencia
                                left join
			                 	(  
				                    select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
				                    from
				                    (
	                    
	                                  select patient_id, date_add(max(obs_fila.value_datetime), interval 1 day) data_ultimo_levantamento 
	                                  from ( 
	                                  select fila.patient_id,fila.data_fila data_fila,e.encounter_id 
	                                  from ( 
	                                  Select p.patient_id,max(encounter_datetime) data_fila from 
	                                         patient p 
	                                         inner join encounter e on e.patient_id=p.patient_id 
	                                  where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location and date(e.encounter_datetime)<=:endDate 
	                                        group by p.patient_id 
	                                         )fila 
	                                  inner join encounter e on  date(e.encounter_datetime)=date(fila.data_fila) and e.encounter_type=18 and e.voided=0 and e.patient_id=fila.patient_id 
	                                      )maxFila  
	                                  left join 
	                                  obs obs_fila on obs_fila.person_id=maxFila.patient_id 
	                                  and obs_fila.voided=0 
	                                  and obs_fila.encounter_id=maxFila.encounter_id 
	                                  and obs_fila.concept_id=5096 
	                                  and obs_fila.location_id=:location 
	                                  group by maxFila.patient_id 

		                        		union

			                        	select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento                                                                                     
			                        	from patient p                                                                                                                                   
			                         	inner join person pe on pe.person_id = p.patient_id                                                                                         
			                              inner join encounter e on p.patient_id=e.patient_id                                                                                         
			                              inner join obs o on e.encounter_id=o.encounter_id                                                                                           
			                        	where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52                                                       
			                              and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <=:endDate                                                                                        
			                        	group by p.patient_id
				                    	) ultimo_levantamento group by patient_id
			                		) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
			               		where ultimo_levantamento.data_ultimo_levantamento <=:endDate	
                                        ) 
                                         saida on inicio.patient_id=saida.patient_id 
                                         left join 
                                         ( 
                                             select patient_id, max(obs_fila.value_datetime) data_proximo_lev, data_fila from ( 
                                                  select fila.patient_id,fila.data_fila data_fila,e.encounter_id from 
                                                  ( 
                                                  Select p.patient_id,max(encounter_datetime) data_fila from patient p 
                                                  inner join encounter e on e.patient_id=p.patient_id 
                                                  where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
                                                  and date(e.encounter_datetime)<=:endDate 
                                                  group by p.patient_id 
                                                  )fila 
                                                  inner join encounter e on  date(e.encounter_datetime)=date(fila.data_fila) and e.encounter_type=18 and e.voided=0 and e.patient_id=fila.patient_id 
                                                  )maxFila 
                                                       left join 
                                                  obs obs_fila on obs_fila.person_id=maxFila.patient_id 
                                                  and obs_fila.voided=0 
                                                  and obs_fila.encounter_id=maxFila.encounter_id 
                                                  and obs_fila.concept_id=5096 
                                                  and obs_fila.location_id=:location 
                                                  group by maxFila.patient_id 

                                         ) 
                                         max_fila on inicio.patient_id=max_fila.patient_id 
                                         left join 
                                         ( 
                                             Select p.patient_id,max(encounter_datetime) data_seguimento 
                                             from patient p 
                                                 inner join encounter e on e.patient_id=p.patient_id 
                                             where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  e.location_id=:location and e.encounter_datetime<=:endDate 
                                                 group by p.patient_id 
                                         ) 
                                         max_consulta on inicio.patient_id=max_consulta.patient_id 
                                         left join 
                                         ( 
                                             Select p.patient_id,max(value_datetime) data_recepcao_levantou 
                                             from patient p 
                                                 inner join encounter e on p.patient_id=e.patient_id 
                                                 inner join obs o on e.encounter_id=o.encounter_id 
                                             where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866 
                                                 and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location 
                                                 group by p.patient_id 
                                         ) 
                                         max_recepcao on inicio.patient_id=max_recepcao.patient_id 
                                             group by inicio.patient_id 
                             ) inicio_fila_seg 
                                     left join obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id 
                                         and obs_seguimento.voided=0 
                                         and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento 
                                         and obs_seguimento.concept_id=1410 
                                         and obs_seguimento.location_id=:location 
                                         group by inicio_fila_seg.patient_id 
                         ) inicio_fila_seg_prox 
                             group by patient_id 
                        ) coorte12meses_final 
                               where (((data_estado is null or ((data_estado is not null and decisao=2) and  data_usar_c>data_estado))  and date_add(data_usar, interval 60 day) >=:endDate) 
                         OR    ((decisao=1 and data_estado<data_fila) and date_add(data_usar, interval 60 day) >=:endDate)) ;