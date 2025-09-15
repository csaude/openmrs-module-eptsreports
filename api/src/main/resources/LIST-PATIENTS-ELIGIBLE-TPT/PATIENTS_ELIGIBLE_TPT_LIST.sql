select coorte12meses_final.patient_id as patient_id,
                    DATE_FORMAT(DATE(coorte12meses_final.data_inicio), '%d/%m/%Y') as data_inicio, 
                    concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NomeCompleto, 
                    pid.identifier as NID, 
                    p.gender as gender, 
                    floor(datediff(:endDate,p.birthdate)/365) as idade_actual, 
                    TPT_ELIG_FR16.decisao as gravidaLactante, 
                    DATE_FORMAT(DATE(TPT_ELIG_FR19.data_seguimento), '%d/%m/%Y') as data_seguimento, 
                    DATE_FORMAT(DATE(TPT_ELIG_FR19.data_proximo_seguimento), '%d/%m/%Y') as data_proximo_seguimento, 
                    DATE_FORMAT(DATE(fila.data_levantamento), '%d/%m/%Y') as LAST_FILA, 
                    DATE_FORMAT(DATE(fila.data_proximo_levantamento), '%d/%m/%Y') as NEXT_FILA
             from 
            (
            select inicio_fila_seg_prox.*,     
             	    GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30), COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_usar 
             from                                                                                                                        
                 (select     inicio_fila_seg.*,                                                                                          
                 max(obs_fila.value_datetime) data_proximo_lev,                                                                          
                 date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30                                              
              from                                                                                                                           
            (select inicio.*,                                                                                                                    
                 saida.data_estado,                                                                                                          
                 max_fila.data_fila,
                 max_recepcao.data_recepcao_levantou                                                                                         
             from                                                                                                                                
             (   
						select patient_id,data_inicio 
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
							) tx_new where data_inicio <= :endDate and data_inicio < '2023-12-21'
							union
							select tx_new.patient_id, tx_new.data_inicio
							from 
							(
								  select tx_new.patient_id, tx_new.data_inicio 
								  from
								  ( 
										select patient_id, data_inicio from 
										(
											  select patient_id, min(data_inicio) data_inicio 
											  from 
													(
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
										) tx_new where data_inicio <= :endDate and data_inicio >= '2023-12-21'
								  ) tx_new
								  left join
								  (
										select patient_id from 
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
													) 
											  art_start group by patient_id 
										) tx_new where data_inicio < '2023-12-21'
								  ) tx_new_period_anterior on tx_new.patient_id = tx_new_period_anterior.patient_id
								   where tx_new_period_anterior.patient_id is null
							) tx_new
						) inicio
              )inicio                                                                                                                                    
              left join                                                                                                                                  
             ( 
                select patient_id,max(data_estado) data_estado                                                                                              
                     from                                                                                                                                        
                         (                                                                                                                                       
                             select distinct max_estado.patient_id, max_estado.data_estado from (                                                                
                                 select  pg.patient_id,                                                                                                          
                                         max(ps.start_date) data_estado                                                                                          
                                 from    patient p                                                                                                               
                                     inner join patient_program pg on p.patient_id = pg.patient_id                                                               
                                     inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
                                 where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
                                     and ps.start_date<= :endDate and pg.location_id =:location group by pg.patient_id                                           
                             ) max_estado                                                                                                                        
                                 inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
                                 inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
                             where pp.program_id = 2 and ps.state = 8 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location                 
                             
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
															and ps.start_date<= :endDate and pg.location_id =:location group by pg.patient_id                                           
											) max_estado                                                                                                                        
												inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
												inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
											where pp.program_id = 2 and ps.state = 10 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location  
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
									) dead_state group by dead_state.patient_id  
							) dead_state 
							inner join
							(
									select fila_seguimento.patient_id,max(fila_seguimento.data_encountro) data_encountro
									from(
												select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
												from    patient p                                                                                                                                   
														inner join encounter e on e.patient_id=p.patient_id                                                                                         
												where   p.voided=0 and e.voided=0 and e.encounter_type=18                                                                      
														and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
														group by p.patient_id  
												union
												
												select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
												from patient p                                                                                                                                   
													inner join encounter e on e.patient_id=p.patient_id                                                                                         
												where   p.voided=0 and e.voided=0 and e.encounter_type in (6,9)                                                                
													and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
													group by p.patient_id   
									) fila_seguimento	group by fila_seguimento.patient_id  
							 ) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
								where fila_seguimento.data_encountro is null or  fila_seguimento.data_encountro <= dead_state.data_estado
							 
							 union
							 
							 select  p.patient_id,                                                                                                               
                                     max(o.obs_datetime) data_estado                                                                                             
                             from    patient p                                                                                                                   
                                     inner join encounter e on p.patient_id=e.patient_id                                                                         
                                     inner join obs  o on e.encounter_id=o.encounter_id                                                                          
                             where   e.voided=0 and o.voided=0 and p.voided=0 and                                                              
                                     e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1709 and                        
                                     o.obs_datetime<=:endDate and e.location_id=:location                                                                        
                             group by p.patient_id                                                                                                               
                             union
 					         select saidas_por_transferencia.patient_id, data_estado 
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
				                                 		inner join patient_program pg on p.patient_id = pg.patient_id                                                               
				                                     	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
				                                 where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
				                                 		and ps.start_date<= :endDate and pg.location_id =:location group by pg.patient_id                                           
			                             		) max_estado                                                                                                                        
			                                 		inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
			                                 		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
			                             		where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location                 
			                             
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
				                                         and e.encounter_type = 21 and  e.location_id=:location                                                                 
				                                         group by p.patient_id                                                                                                   
				                                 ) ultimaBusca                                                                                                                   
				                                     inner join encounter e on e.patient_id = ultimaBusca.patient_id                                                             
				                                     inner join obs o on o.encounter_id = e.encounter_id                                                                         
				                                where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimaBusca.data_estado = e.encounter_datetime and e.location_id =:location 
		                                ) saidas_por_transferencia 
	                                	group by patient_id 
                               	) saidas_por_transferencia
                                left join
			                 	(  
				                    select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
				                    from
				                    (
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
			                		) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
			               		where ultimo_levantamento.data_ultimo_levantamento <= :endDate
				                                                                                                                                                                         
                         	) allSaida                                                                                                                                      
                 				group by patient_id 
                
              		) saida on inicio.patient_id = saida.patient_id                                                                                                      
             left join                                                                                                                                           
              ( select p.patient_id,max(encounter_datetime) data_fila                                                                                                
             from    patient p                                                                                                                                   
                     inner join encounter e on e.patient_id=p.patient_id                                                                                         
             where   p.voided=0 and e.voided=0 and e.encounter_type=18 and                                                                     
                     e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
             group by p.patient_id                                                                                                                               
             ) max_fila on inicio.patient_id=max_fila.patient_id  
              left join                                                                                                                                          
              (                                                                                                                                                  
             select  p.patient_id,max(value_datetime) data_recepcao_levantou                                                                                     
             from    patient p                                                                                                                                   
                     inner join encounter e on p.patient_id=e.patient_id                                                                                         
                     inner join obs o on e.encounter_id=o.encounter_id                                                                                           
             where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and                                                      
                     o.concept_id=23866 and o.value_datetime is not null and                                                                                     
                     o.value_datetime<=:endDate and e.location_id=:location                                                                                      
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
            inner join person p on p.person_id=coorte12meses_final.patient_id        
            left join  
            (   select pad1.* 
                from person_address pad1 
                inner join  
                ( 
                    select person_id,min(person_address_id) id  
                    from person_address 
                    where voided=0 
                    group by person_id 
                ) pad2 
                where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id 
            ) pad3 on pad3.person_id=coorte12meses_final.patient_id              
            left join            
            (   select pn1.* 
                from person_name pn1 
                inner join  
                ( 
                    select person_id,min(person_name_id) id  
                    from person_name 
                    where voided=0 
                    group by person_id 
                ) pn2 
                where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id 
            ) pn on pn.person_id=coorte12meses_final.patient_id          
            left join 
            (   select pid1.* 
                from patient_identifier pid1 
                inner join 
                ( 
                    select patient_id,min(patient_identifier_id) id 
                    from patient_identifier 
                    where voided=0 and identifier_type = 2
                    group by patient_id 
                ) pid2 
                where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id 
            ) pid on pid.patient_id=coorte12meses_final.patient_id 
            left join  
            (       
              
                select p.patient_id, min(obsEstado.obs_datetime) data_inicio_INH 
                    from patient p 
                        inner join encounter e on p.patient_id = e.patient_id 
                        inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                        inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                      where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                      and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                      and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and e.location_id =:location
                      and obsEstado.obs_datetime between (:endDate - INTERVAL 7 MONTH) and :endDate
                      group by p.patient_id  
                     
                     union

                     select p.patient_id,min(e.encounter_datetime) data_inicio_INH 
                     from patient p 
                        inner join encounter e on p.patient_id=e.patient_id 
                         inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                         inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id
                       where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 and seguimentoTPT.voided =0
                        and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982) and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) --  (1257) --inicio or reinicio
                         and e.encounter_datetime between (:endDate - INTERVAL 7 MONTH) and :endDate and  e.location_id =:location
                         group by p.patient_id 
                     
                     union     
                     
                     select inicio.patient_id,inicio.data_inicio_INH 
                     from (  
                                select p.patient_id,min(e.encounter_datetime) data_inicio_INH from patient p                                                             
                                    inner join encounter e on p.patient_id=e.patient_id                                                                                          
                                    inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id                                                                                            
                                    left join obs seguimentoTPT on ( seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 )                                                                   
                            where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0  
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)     
                                    and e.encounter_datetime between (:endDate - INTERVAL 7 MONTH) and :endDate and  e.location_id =:location                 
                                    and ( seguimentoTPT.value_coded = 1257 or  seguimentoTPT.value_coded is null )
                                    group by p.patient_id 
                            ) 
                        inicio
                        left join
                        (   
                            select p.patient_id,e.encounter_datetime data_inicio_INH 
                            from patient p 
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                            where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                and e.encounter_datetime between (:endDate - INTERVAL 15 MONTH) and :endDate and  e.location_id =:location
                           
                            union
                            
                            select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                            from patient p 
                                inner join encounter e on p.patient_id = e.patient_id 
                                 inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                 inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                            where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                                 and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                                 and obsEstado.obs_datetime between (:endDate - INTERVAL 15 MONTH) and :endDate
                             )inicioAnterior on inicioAnterior.patient_id=inicio.patient_id and  inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
                       where inicioAnterior.patient_id is null 
                 union
                 
                 select p.patient_id, min(obsEstado.obs_datetime) data_inicio_3HP 
                 from patient p 
                    inner join encounter e on p.patient_id = e.patient_id 
                    inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                    inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                  where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
                      and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                      and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                      and obsEstado.obs_datetime between (:endDate - INTERVAL 120 DAY) and :endDate
                      group by p.patient_id   
                union
                select p.patient_id, min(e.encounter_datetime) data_inicio_3HP 
                from patient p 
                    inner join encounter e on p.patient_id = e.patient_id 
                    inner join obs o on o.encounter_id = e.encounter_id
                where e.encounter_type in (6,9) and o.concept_id=1719 and o.value_coded=165307 
                    and p.voided=0 and e.voided=0 and o.voided=0
                    and e.encounter_datetime between (:endDate -  INTERVAL 120 DAY) and :endDate
                    group by p.patient_id
                union         
                
                select p.patient_id,min(e.encounter_datetime) data_inicio_3HP from patient p                                                             
                    inner join encounter e on p.patient_id=e.patient_id                                                                                          
                    inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id                                                                                            
                    inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id                                                                    
                where p.voided=0 and e.voided=0 and regime3HP.voided=0 and seguimentoTPT.voided =0 
                    and e.encounter_type=60  and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)
                    and e.encounter_datetime between (:endDate - INTERVAL 120 DAY) and :endDate and  e.location_id =:location                   
                    group by p.patient_id  
                
                union
                
                select inicio.patient_id,inicio.data_inicio_3HP 
                from (
                    select p.patient_id,min(e.encounter_datetime) data_inicio_3HP 
                        from patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                            inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id 
                                left join obs seguimentoTPT on (seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided = 0 and seguimentoTPT.concept_id =23987 )       
                        where p.voided=0 and e.voided=0 and regime3HP.voided=0 
                            and e.encounter_type=60 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984)      
                            and e.encounter_datetime between (:endDate - INTERVAL 120 DAY) and :endDate and  e.location_id =:location
                             and ( seguimentoTPT.value_coded in (1257,1267) or seguimentoTPT.value_coded is null) 
                             group by p.patient_id 
                        ) 
                    inicio  
                    left join  
                    (   
                        select p.patient_id,e.encounter_datetime data_inicio_3HP 
                         from patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                              inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id 
                            where p.voided=0 and e.voided=0 and regime3HP.voided=0  
                            and e.encounter_type=60 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984)
                              and e.encounter_datetime between (:endDate - INTERVAL 9 MONTH) and :endDate and e.location_id =:location
                        
                        union
                        
                        select p.patient_id, obsEstado.obs_datetime data_inicio_3HP 
                            from patient p 
                            inner join encounter e on p.patient_id = e.patient_id 
                                inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                          where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
                            and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                            and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                            and obsEstado.obs_datetime between (:endDate - INTERVAL 9 MONTH) and :endDate
                        
                        union

                         select p.patient_id, outrasPrescricoes3HP.obs_datetime data_inicio_3HP 
                         from patient p 
                            inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs outrasPrescricoes3HP on outrasPrescricoes3HP.encounter_id = e.encounter_id 
                          where p.voided=0 and e.voided=0 and outrasPrescricoes3HP.voided=0 
                            and e.encounter_type in (6,9) and outrasPrescricoes3HP.concept_id=1719 and outrasPrescricoes3HP.value_coded in (23954,165307)
                              and e.encounter_datetime between (:endDate - INTERVAL 9 MONTH) and :endDate and  e.location_id =:location  
                    ) inicioAnterior on inicioAnterior.patient_id=inicio.patient_id and  inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 120 day) and (inicio.data_inicio_3HP - INTERVAL 1 day) 
                    where inicioAnterior.patient_id is null      
            ) TPT_ELIG_FR4 on TPT_ELIG_FR4.patient_id=coorte12meses_final.patient_id 
            left join  
            (       
              select inicio_inh.patient_id 
                from (

                    select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                    from patient p 
                        inner join encounter e on p.patient_id = e.patient_id 
                            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                     where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                        and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                        and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                        and obsEstado.obs_datetime <= :endDate
                     
                     union
                     
                     select p.patient_id,e.encounter_datetime data_inicio_INH 
                     from patient p                                                             
                        inner join encounter e on p.patient_id=e.patient_id                                                                                          
                        inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id                                                                                            
                         inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id                                                                    
                     where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 and seguimentoTPT.voided =0 
                        and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)  and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)  
                         and e.encounter_datetime <= :endDate and  e.location_id=:location                
                     
                     union
                     
                     select inicio.patient_id,inicio.data_inicio_INH 
                     from (  
                            select p.patient_id, e.encounter_datetime data_inicio_INH 
                            from patient p 
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                left join obs seguimentoTPT on ( seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987)
                            where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                    and ( seguimentoTPT.value_coded = 1257 or seguimentoTPT.value_coded  is null)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                            )
                         inicio
                        left join
                        (   
                                select p.patient_id,e.encounter_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                                
                                union
                            
                            select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id = e.patient_id 
                                    inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                    inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                            where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                                and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                                and obsEstado.obs_datetime <= :endDate
                                --group by p.patient_id,obsEstado.obs_datetime
                                )inicioAnterior on inicioAnterior.patient_id=inicio.patient_id and  inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
                     where inicioAnterior.patient_id is null 
                  ) inicio_inh
                    inner join                   
                     (   
                        select patient_id, data_final_INH               
                        from(  
                                select p.patient_id, obsEstado.obs_datetime data_final_INH 
                              from patient p 
                                inner join encounter e on p.patient_id = e.patient_id 
                                    inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                    inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                               where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                and obsEstado.concept_id=165308 and obsEstado.value_coded=1267 
                                    and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                                    and obsEstado.obs_datetime <= :endDate
                            ) endINH                 
                      ) termino_inh on inicio_inh.patient_id = termino_inh.patient_id
                      where termino_inh.data_final_INH between inicio_inh.data_inicio_INH + interval 173 day and inicio_inh.data_inicio_INH + interval 365 day
                
                union
               
               select inicio_inh.patient_id 
                from
                (   
                 select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                        from patient p 
                            inner join encounter e on p.patient_id = e.patient_id 
                            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                          where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                          and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                          and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                          and obsEstado.obs_datetime <= :endDate
                        
                  )  inicio_inh
                    inner join
                  ( 
                    select p.patient_id, obsEstado.obs_datetime encounter_datetime, e.encounter_id  
                        from patient p 
                            inner join encounter e on p.patient_id = e.patient_id 
                                inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                          where e.encounter_type in(6,9) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                            and obsEstado.concept_id=165308 and obsEstado.value_coded in(1256,1257) 
                            and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                            and obsEstado.obs_datetime <= :endDate

                    )consultasINH on inicio_inh.patient_id = consultasINH.patient_id
                    where consultasINH.encounter_datetime between (inicio_inh.data_inicio_INH + INTERVAL 1 day) and (inicio_inh.data_inicio_INH + INTERVAL 7 MONTH)
                    group by inicio_inh.patient_id,inicio_inh.data_inicio_INH having count(distinct consultasINH.encounter_id)>=5                  
               
                union
               
                select inicio_inh.patient_id 
                from
                (   
                     select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                        from patient p 
                            inner join encounter e on p.patient_id = e.patient_id 
                            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                          where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                          and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                          and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and  e.location_id =:location
                          and obsEstado.obs_datetime <= :endDate
                      
                  )  inicio_inh
                    inner join
                  (
                select p.patient_id, obsEstado.obs_datetime encounter_datetime,e.encounter_id 
                        from patient p 
                            inner join encounter e on p.patient_id = e.patient_id 
                            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                            inner join obs outraPrescricaoDTINH on outraPrescricaoDTINH.encounter_id=e.encounter_id
                          where   e.encounter_type in(6,9) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                          and obsEstado.concept_id=165308 and obsEstado.value_coded in(1256,1257) 
                          and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0    
                          and outraPrescricaoDTINH.concept_id=1719 and outraPrescricaoDTINH.value_coded=23955 and outraPrescricaoDTINH.voided=0  and  e.location_id =:location
                          and obsEstado.obs_datetime <= :endDate
                    ) consultasINH on inicio_inh.patient_id = consultasINH.patient_id
                where consultasINH.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 5 MONTH)
                group by inicio_inh.patient_id,inicio_inh.data_inicio_INH having count(distinct consultasINH.encounter_id)>=2  
               
                union
                
                select consultasSemDTINH.patient_id
                from(
                        select inicio_inh.patient_id 
                        from(   
                            select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                                from patient p 
                                    inner join encounter e on p.patient_id = e.patient_id 
                                    inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                    inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                            where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                and obsEstado.concept_id=165308 and obsEstado.value_coded=1256  
                                and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and  e.location_id =:location
                                and obsEstado.obs_datetime <= :endDate
                            ) 
                        inicio_inh
                        inner join
                        (
                                select consultasSemDTINH.patient_id, consultasSemDTINH.encounter_datetime, outraPrescricaoDTINH.obs_datetime, e.encounter_id 
                                from(  
                                    select p.patient_id,obsEstado.obs_datetime encounter_datetime 
                                    from patient p 
                                        inner join encounter e on p.patient_id = e.patient_id 
                                        inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                        inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                                where e.encounter_type = 6 and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                    and obsEstado.concept_id=165308 and  obsEstado.value_coded in(1256,1257)   
                                    and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and  e.location_id =:location
                                    and obsEstado.obs_datetime <= :endDate
                                )
                            consultasSemDTINH
                                    inner join encounter e on e.patient_id = consultasSemDTINH.patient_id
                                    left join obs outraPrescricaoDTINH on e.encounter_id=outraPrescricaoDTINH.encounter_id
                                where e.encounter_type  = 6 and e.voided = 0 and outraPrescricaoDTINH.encounter_id is null 
                        
                        )consultasSemDTINH on inicio_inh.patient_id = consultasSemDTINH.patient_id
                    where consultasSemDTINH.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH)
                    group by inicio_inh.patient_id,inicio_inh.data_inicio_INH  having count(distinct consultasSemDTINH.encounter_id)>=:location 
                  ) consultasSemDTINH
             inner join 
             (
            select consultasComINH.patient_id, consultasComINH.encounter_datetime from
             (
                select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                        from patient p 
                            inner join encounter e on p.patient_id = e.patient_id 
                            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                          where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                          and obsEstado.concept_id=165308 and obsEstado.value_coded=1256  
                          and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and  e.location_id =:location
                          and obsEstado.obs_datetime <= :endDate
                 ) inicio_inh
                inner join

                (
                  select distinct consultasComINH.patient_id, consultasComINH.encounter_datetime , consultasComINH.encounter_id 
                        from 
                        (  
                            select  p.patient_id, obsEstado.obs_datetime encounter_datetime, e.encounter_id 
                            from    patient p 
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs profilaxiaINH on profilaxiaINH.encounter_id=e.encounter_id 
                                inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                                inner join obs outraPrescricaoDTINH on e.encounter_id=outraPrescricaoDTINH.encounter_id
                            where p.voided=0 and e.voided=0 and profilaxiaINH.voided=0 and outraPrescricaoDTINH.voided=0 and obsEstado.voided=0
                                and e.encounter_type = 6 and profilaxiaINH.concept_id=23985 and profilaxiaINH.value_coded in (656) and obsEstado.concept_id=165308 and obsEstado.value_coded in(1256,1257)
                                and outraPrescricaoDTINH.concept_id=1719 and outraPrescricaoDTINH.value_coded=23955
                                    and e.encounter_datetime <= :endDate and  e.location_id=:location
                            )consultasComINH
                    )consultasComINH on inicio_inh.patient_id = consultasComINH.patient_id
                    where consultasComINH.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH)
                    group by inicio_inh.patient_id,inicio_inh.data_inicio_INH  having count(distinct consultasComINH.encounter_id)>=1
                )consultasComINH on consultasComINH.patient_id=consultasSemDTINH.patient_id
                
                union
                
                select inicio_inh.patient_id 
                from(   
                select p.patient_id,e.encounter_datetime data_inicio_INH 
                     from patient p                                                             
                        inner join encounter e on p.patient_id=e.patient_id                                                                                          
                        inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id                                                                                            
                         inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id                                                                    
                     where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 and seguimentoTPT.voided =0 
                        and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)  and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)  
                         and e.encounter_datetime <= :endDate and  e.location_id=:location                
                     
                     union
                     
                     select inicio.patient_id,inicio.data_inicio_INH 
                     from (  
                            select p.patient_id, e.encounter_datetime data_inicio_INH 
                            from patient p 
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                left join obs seguimentoTPT on ( seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987)
                            where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                    and ( seguimentoTPT.value_coded = 1257 or seguimentoTPT.value_coded  is null)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                            )
                         inicio
                        left join
                        (   
                                select p.patient_id,e.encounter_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                                
                                union
                            
                            select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id = e.patient_id 
                                    inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                    inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                            where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                                and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                                and obsEstado.obs_datetime <= :endDate
                                group by  p.patient_id, obsEstado.obs_datetime
                                )inicioAnterior on inicioAnterior.patient_id=inicio.patient_id and  inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
                     where inicioAnterior.patient_id is null 
                   
                  ) inicio_inh
                    inner join encounter e on e.patient_id=inicio_inh.patient_id                     
                    inner join obs obsDTINH on e.encounter_id=obsDTINH.encounter_id                  
                    inner join obs obsLevTPI on e.encounter_id=obsLevTPI.encounter_id                
                where e.voided=0 and obsDTINH.voided=0 and obsLevTPI.voided=0 and e.encounter_type=60               
                    and obsDTINH.concept_id=23986 and obsDTINH.value_coded=1098   
                     and obsLevTPI.concept_id=23985 and obsLevTPI.value_coded in (656,23982)  
                     and e.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH) and e.location_id=:location 
                    group by inicio_inh.patient_id having count(distinct e.encounter_id )>=6  
               
                union
                
                select inicio_inh.patient_id 
                from( 
                  
                     select p.patient_id,e.encounter_datetime data_inicio_INH 
                     from patient p                                                             
                        inner join encounter e on p.patient_id=e.patient_id                                                                                          
                        inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id                                                                                            
                         inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id                                                                    
                     where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 and seguimentoTPT.voided =0 
                        and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)  and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)  
                         and e.encounter_datetime <= :endDate and  e.location_id=:location                
                     
                     union
                     
                     select inicio.patient_id,inicio.data_inicio_INH 
                     from (  
                            select p.patient_id, e.encounter_datetime data_inicio_INH 
                            from patient p 
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                left join obs seguimentoTPT on ( seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987)
                            where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                    and ( seguimentoTPT.value_coded = 1257 or seguimentoTPT.value_coded  is null)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                            )
                         inicio
                        left join
                        (   
                                select p.patient_id,e.encounter_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                                
                                union
                            
                            select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id = e.patient_id 
                                    inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                    inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                            where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                                and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                                and obsEstado.obs_datetime <= :endDate
                                group by p.patient_id, obsEstado.obs_datetime
                                )inicioAnterior on inicioAnterior.patient_id=inicio.patient_id and  inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
                     where inicioAnterior.patient_id is null 
                  ) 
                inicio_inh
                 inner join encounter e on e.patient_id=inicio_inh.patient_id                     
                 inner join obs obsDTINH on e.encounter_id=obsDTINH.encounter_id                  
                 inner join obs obsLevTPI on e.encounter_id=obsLevTPI.encounter_id                
                 where e.voided=0 and obsDTINH.voided=0 and obsLevTPI.voided=0 and e.encounter_type=60               
                     and obsDTINH.concept_id=23986 and obsDTINH.value_coded=23720   
                     and obsLevTPI.concept_id=23985 and obsLevTPI.value_coded in (656,23982)  
                     and e.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 5 MONTH) and e.location_id=:location 
                 group by inicio_inh.patient_id  
                 having count(distinct e.encounter_id)>=2 
                
                union
                
                select inicio_inh.patient_id  
                from ( 
                
                    select inicio_inh.patient_id 
                    from(
                     select p.patient_id,e.encounter_datetime data_inicio_INH 
                     from patient p                                                             
                        inner join encounter e on p.patient_id=e.patient_id                                                                                          
                        inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id                                                                                            
                         inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id                                                                    
                     where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 and seguimentoTPT.voided =0 
                        and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)  and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)  
                         and e.encounter_datetime <= :endDate and  e.location_id=:location                
                     
                     union
                     
                     select inicio.patient_id,inicio.data_inicio_INH 
                     from (  
                            select p.patient_id, e.encounter_datetime data_inicio_INH 
                            from patient p 
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                left join obs seguimentoTPT on ( seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987)
                            where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                    and ( seguimentoTPT.value_coded = 1257 or seguimentoTPT.value_coded  is null)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                            )
                         inicio
                        left join
                        (   
                                select p.patient_id,e.encounter_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                                
                                union
                            
                            select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id = e.patient_id 
                                    inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                    inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                            where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                                and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                                and obsEstado.obs_datetime <= :endDate
                                group by p.patient_id,obsEstado.obs_datetime
                                )inicioAnterior on inicioAnterior.patient_id=inicio.patient_id and  inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
                     where inicioAnterior.patient_id is null 
                      ) 
                    inicio_inh
                        inner join encounter e on e.patient_id=inicio_inh.patient_id                 
                        inner join obs obsDTINH on e.encounter_id=obsDTINH.encounter_id              
                        inner join obs obsLevTPI on e.encounter_id=obsLevTPI.encounter_id            
                        where e.voided=0 and obsDTINH.voided=0 and obsLevTPI.voided=0 and e.encounter_type in (60)          
                            and obsDTINH.concept_id=23986 and obsDTINH.value_coded=1098  and obsLevTPI.concept_id=23985 and obsLevTPI.value_coded in (656,23982)  
                            and e.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH) and e.location_id=:location 
                            group by inicio_inh.patient_id having count(distinct e.encounter_id)>=:location 
                    )
                 inicio_inh  
                 inner join
                 (  
                    select inicio_inh.patient_id 
                    from(
                       
                     select p.patient_id,e.encounter_datetime data_inicio_INH 
                     from patient p                                                             
                        inner join encounter e on p.patient_id=e.patient_id                                                                                          
                        inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id                                                                                            
                         inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id                                                                    
                     where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 and seguimentoTPT.voided =0 
                        and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)  and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)  
                         and e.encounter_datetime <= :endDate and  e.location_id=:location                
                     
                     union
                     
                     select inicio.patient_id,inicio.data_inicio_INH 
                     from (  
                            select p.patient_id, e.encounter_datetime data_inicio_INH 
                            from patient p 
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                left join obs seguimentoTPT on ( seguimentoTPT.encounter_id=e.encounter_id and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987)
                            where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                    and ( seguimentoTPT.value_coded = 1257 or seguimentoTPT.value_coded  is null)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                            )
                         inicio
                        left join
                        (   
                                select p.patient_id,e.encounter_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs regimeIsoniazida on regimeIsoniazida.encounter_id=e.encounter_id 
                                where p.voided=0 and e.voided=0 and regimeIsoniazida.voided=0 
                                    and e.encounter_type=60 and regimeIsoniazida.concept_id=23985 and regimeIsoniazida.value_coded in (656,23982)
                                and e.encounter_datetime <= :endDate and  e.location_id=:location
                                
                                union
                            
                            select p.patient_id, obsEstado.obs_datetime data_inicio_INH 
                            from patient p 
                                    inner join encounter e on p.patient_id = e.patient_id 
                                    inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
                                    inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
                            where e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=656 
                                and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
                                and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
                                and obsEstado.obs_datetime <= :endDate
                                --group by p.patient_id, obsEstado.obs_datetime
                                
                                )inicioAnterior on inicioAnterior.patient_id=inicio.patient_id and  inicioAnterior.data_inicio_INH between (inicio.data_inicio_INH - INTERVAL 7 MONTH) and (inicio.data_inicio_INH - INTERVAL 1 day) 
                     where inicioAnterior.patient_id is null 
                      ) 
                    inicio_inh
                        inner join encounter e on e.patient_id=inicio_inh.patient_id                 
                          inner join obs obsDTINH on e.encounter_id=obsDTINH.encounter_id              
                          inner join obs obsLevTPI on e.encounter_id=obsLevTPI.encounter_id            
                          where e.voided=0 and obsDTINH.voided=0 and obsLevTPI.voided=0 and e.encounter_type in (60)          
                              and obsDTINH.concept_id=23986 and obsDTINH.value_coded=23720  and obsLevTPI.concept_id=23985 and obsLevTPI.value_coded in (656,23982)  
                              and e.encounter_datetime between inicio_inh.data_inicio_inh and (inicio_inh.data_inicio_inh + INTERVAL 7 MONTH) and e.location_id=:location 
                          group by inicio_inh.patient_id having count(distinct e.encounter_id)>=1    
                 ) inicio_inh_dt on inicio_inh_dt.patient_id = inicio_inh.patient_id 

            )TPT_ELIG_FR8 on TPT_ELIG_FR8.patient_id=coorte12meses_final.patient_id 
            left join  
            (   

select inicio_3HP.patient_id  
from 
(                                                                                                                                                     
    select p.patient_id, obsEstado.obs_datetime data_inicio_3HP 
         from patient p 
        inner join encounter e on p.patient_id = e.patient_id 
        inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
        inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
        where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
         and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
         and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location
         and obsEstado.obs_datetime <=:endDate
    
    union
    select p.patient_id, e.encounter_datetime data_inicio_3HP                                                                                         
        from patient p                                                                                                                             
        inner join encounter e on p.patient_id = e.patient_id                                                                                         
        inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id                                                                                         
        where p.voided = 0 and e.voided = 0 and outrasPrescricoesDT3HP.voided = 0 
            and e.encounter_type = 6 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded = 165307                      
        and e.encounter_datetime <=:endDate and e.location_id =:location
    
    union
    
    select p.patient_id, e.encounter_datetime data_inicio_3HP 
    from patient p 
        inner join encounter e on p.patient_id = e.patient_id 
        inner join obs regime3HP on regime3HP.encounter_id = e.encounter_id 
        inner join obs seguimentoTPT on seguimentoTPT.encounter_id = e.encounter_id
    where p.voided = 0 and e.voided = 0 and regime3HP.voided = 0 and seguimentoTPT.voided = 0
        and e.encounter_type = 60 and regime3HP.concept_id = 23985 and regime3HP.value_coded in (23954,23984) and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705) 
        and e.encounter_datetime <=:endDate and  e.location_id=:location
    union
    
    select inicio.patient_id, inicio.data_inicio_3HP 
    from (
            select p.patient_id, e.encounter_datetime data_inicio_3HP 
            from patient p 
                inner join encounter e on p.patient_id = e.patient_id 
                inner join obs regime3HP on regime3HP.encounter_id = e.encounter_id 
                left join obs seguimentoTPT on ( seguimentoTPT.encounter_id = e.encounter_id and seguimentoTPT.voided = 0 and seguimentoTPT.concept_id = 23987 )
            where p.voided = 0 and e.voided = 0 and regime3HP.voided = 0 
                and e.encounter_type = 60 and regime3HP.concept_id = 23985 and regime3HP.value_coded in (23954,23984)
                and ( seguimentoTPT.value_coded in (1257,1267) or seguimentoTPT.value_coded is null)
                and  e.encounter_datetime <=:endDate and  e.location_id =:location
       )
     inicio
    left join
        (
            select p.patient_id, e.encounter_datetime data_inicio_3HP 
            from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id 
            where p.voided=0 and e.voided=0 and regime3HP.voided=0 
                and e.encounter_type=60 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) 
                and e.encounter_datetime <= :endDate and e.location_id=:location
          union
          
          select p.patient_id, obsEstado.obs_datetime data_inicio_3HP 
          from patient p 
            inner join encounter e on p.patient_id = e.patient_id 
            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
            where   e.encounter_type in(6,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
             and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
             and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location
             and obsEstado.obs_datetime <=:endDate
            
            union
            
            select p.patient_id, e.encounter_datetime data_inicio_3HP                                                                                         
            from patient p                                                                                                                             
            inner join encounter e on p.patient_id = e.patient_id                                                                                         
               inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id                                                                                         
            where p.voided = 0 and e.voided = 0 and outrasPrescricoesDT3HP.voided = 0 
            and e.encounter_type = 6 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded in (165307,23954)                       
            and e.encounter_datetime <=:endDate and e.location_id =:location
        )inicioAnterior on inicioAnterior.patient_id  = inicio.patient_id 
        and inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 
    where inicioAnterior.patient_id is null 
   ) inicio_3HP  
inner join                   
(   
  select patient_id, data_final_3HP              
    from (

           select p.patient_id,obsEstado.obs_datetime data_final_3HP 
           from patient p 
           inner join encounter e on p.patient_id = e.patient_id 
           inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id  
           inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
           where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
           and obsEstado.concept_id=165308 and obsEstado.value_coded=1267 
           and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id =:location
           and obsEstado.obs_datetime <= :endDate

        ) end3HP                 
) termino_3hp on inicio_3HP.patient_id = termino_3hp.patient_id
where termino_3hp.data_final_3HP between inicio_3HP.data_inicio_3HP + interval 86 day and inicio_3HP.data_inicio_3HP + interval 365 day

union

select inicio_3HP.patient_id  
from (                                                                                                                                                     
   select p.patient_id, obsEstado.obs_datetime data_inicio_3HP 
         from patient p 
        inner join encounter e on p.patient_id = e.patient_id 
        inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
        inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
        where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
         and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
         and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location
         and obsEstado.obs_datetime <=:endDate
    
    union
    select p.patient_id, e.encounter_datetime data_inicio_3HP                                                                                         
        from patient p                                                                                                                             
        inner join encounter e on p.patient_id = e.patient_id                                                                                         
        inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id                                                                                         
        where p.voided = 0 and e.voided = 0 and outrasPrescricoesDT3HP.voided = 0 
            and e.encounter_type = 6 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded = 165307                      
        and e.encounter_datetime <=:endDate and e.location_id =:location
    
   ) inicio_3HP 
 inner join (   
        select patient_id, data_final_3HP , encounter_id              
        from(                    
               select p.patient_id, estadoProfilaxia.obs_datetime data_final_3HP, e.encounter_id 
               from patient p 
                inner join encounter e on p.patient_id = e.patient_id 
                   inner join obs profilaxia3HP on profilaxia3HP.encounter_id = e.encounter_id 
                   inner join obs estadoProfilaxia on estadoProfilaxia.encounter_id = e.encounter_id 
               where p.voided = 0 and e.voided = 0 and profilaxia3HP.voided = 0 and estadoProfilaxia.voided = 0 
                   and e.encounter_type in(6) and profilaxia3HP.concept_id = 23985 and profilaxia3HP.value_coded = 23954 and estadoProfilaxia.concept_id = 165308 and estadoProfilaxia.value_coded in(1256,1257)
                   and e.encounter_datetime <= :endDate and e.location_id =:location
             ) end3HP                 
      ) termino_3HP on inicio_3HP.patient_id = termino_3HP.patient_id
where termino_3HP.data_final_3HP between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + INTERVAL 120 DAY)
 group by termino_3HP.patient_id having count(distinct termino_3HP.encounter_id)>=:location

union

select inicio_3HP.patient_id  
from (
                                                                                                                                                     
    select p.patient_id, obsEstado.obs_datetime data_inicio_3HP 
             from patient p 
            inner join encounter e on p.patient_id = e.patient_id 
            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
            where   e.encounter_type in(6,9,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
             and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
             and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location
             and obsEstado.obs_datetime <=:endDate
        
        union
        select p.patient_id, e.encounter_datetime data_inicio_3HP                                                                                         
            from patient p                                                                                                                             
            inner join encounter e on p.patient_id = e.patient_id                                                                                         
            inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id                                                                                         
            where p.voided = 0 and e.voided = 0 and outrasPrescricoesDT3HP.voided = 0 
                and e.encounter_type = 6 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded = 165307                      
            and e.encounter_datetime <=:endDate and e.location_id =:location
   ) inicio_3HP 
inner join                   
(   
select patient_id, data_final_3HP, encounter_id               
from(                    
              select p.patient_id, e.encounter_datetime data_final_3HP ,e.encounter_id
               from patient p 
                    inner join encounter e on p.patient_id=e.patient_id 
                    inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id 
                where p.voided=0 and e.voided=0 and outrasPrescricoesDT3HP.voided=0 
                and e.encounter_type in (6,9) and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded=165307
                    and e.encounter_datetime <= :endDate and  e.location_id=:location 
             ) end3HP                 
      ) termino_3HP on inicio_3HP.patient_id = termino_3HP.patient_id
where termino_3HP.data_final_3HP between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + INTERVAL 120 DAY)
group by termino_3HP.patient_id having count(distinct termino_3HP.encounter_id)>=1

union

select inicio_3HP.patient_id  
from (                                                                                                                                                     
        
     select p.patient_id, e.encounter_datetime data_inicio_3HP 
    from patient p 
        inner join encounter e on p.patient_id = e.patient_id 
        inner join obs regime3HP on regime3HP.encounter_id = e.encounter_id 
        inner join obs seguimentoTPT on seguimentoTPT.encounter_id = e.encounter_id
    where p.voided = 0 and e.voided = 0 and regime3HP.voided = 0 and seguimentoTPT.voided = 0
        and e.encounter_type = 60 and regime3HP.concept_id = 23985 and regime3HP.value_coded in (23954,23984) and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705) 
        and e.encounter_datetime <=:endDate and  e.location_id=:location
    union
    
    select inicio.patient_id, inicio.data_inicio_3HP 
    from (
            select p.patient_id, e.encounter_datetime data_inicio_3HP 
            from patient p 
                inner join encounter e on p.patient_id = e.patient_id 
                inner join obs regime3HP on regime3HP.encounter_id = e.encounter_id 
                left join obs seguimentoTPT on ( seguimentoTPT.encounter_id = e.encounter_id and seguimentoTPT.voided = 0 and seguimentoTPT.concept_id = 23987 )
            where p.voided = 0 and e.voided = 0 and regime3HP.voided = 0 
                and e.encounter_type = 60 and regime3HP.concept_id = 23985 and regime3HP.value_coded in (23954,23984)
                and ( seguimentoTPT.value_coded in (1257,1267) or seguimentoTPT.value_coded is null)
                and  e.encounter_datetime <=:endDate and  e.location_id =:location
       )
     inicio
    left join
        (
            select p.patient_id, e.encounter_datetime data_inicio_3HP 
            from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id 
            where p.voided=0 and e.voided=0 and regime3HP.voided=0 
                and e.encounter_type=60 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) 
                and e.encounter_datetime <=:endDate and e.location_id=:location
          union
          
          select p.patient_id, obsEstado.obs_datetime data_inicio_3HP 
          from patient p 
            inner join encounter e on p.patient_id = e.patient_id 
            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
            where   e.encounter_type in(6,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
             and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
             and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location
             and obsEstado.obs_datetime <=:endDate
            
            union
            
            select p.patient_id, e.encounter_datetime data_inicio_3HP                                                                                         
            from patient p                                                                                                                             
            inner join encounter e on p.patient_id = e.patient_id                                                                                         
               inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id                                                                                         
            where p.voided = 0 and e.voided = 0 and outrasPrescricoesDT3HP.voided = 0 
            and e.encounter_type = 6 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded in (165307,23954)                       
            and e.encounter_datetime <=:endDate and e.location_id =:location
        )inicioAnterior on inicioAnterior.patient_id  = inicio.patient_id 
        and inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 
    where inicioAnterior.patient_id is null 
    
   ) inicio_3HP 
    inner join encounter e on e.patient_id= inicio_3HP.patient_id                                                                                         
    inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id                                                                                             
    inner join obs obsTipo on obsTipo.encounter_id=e.encounter_id                                                                                         
    where e.voided=0 and obs3hp.voided=0 and obsTipo.voided=0                                                                                              
        and e.encounter_type=60 and obs3hp.concept_id=23985  
            and obs3hp.value_coded in (23954,23984) and obsTipo.concept_id=23986 and obsTipo.value_coded=23720     
            and e.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + INTERVAL 4 month) and e.location_id=:location     
        group by inicio_3HP.patient_id 
          having count(distinct e.encounter_id)>=1   
          
union

select inicio_3HP.patient_id  
from (
                                                                                                                                                     
    select p.patient_id, e.encounter_datetime data_inicio_3HP 
    from patient p 
        inner join encounter e on p.patient_id = e.patient_id 
        inner join obs regime3HP on regime3HP.encounter_id = e.encounter_id 
        inner join obs seguimentoTPT on seguimentoTPT.encounter_id = e.encounter_id
    where p.voided = 0 and e.voided = 0 and regime3HP.voided = 0 and seguimentoTPT.voided = 0
        and e.encounter_type = 60 and regime3HP.concept_id = 23985 and regime3HP.value_coded in (23954,23984) and seguimentoTPT.concept_id = 23987 and seguimentoTPT.value_coded in (1256,1705) 
        and e.encounter_datetime <=:endDate and  e.location_id=:location
    union
    
    select inicio.patient_id, inicio.data_inicio_3HP 
    from (
            select p.patient_id, e.encounter_datetime data_inicio_3HP 
            from patient p 
                inner join encounter e on p.patient_id = e.patient_id 
                inner join obs regime3HP on regime3HP.encounter_id = e.encounter_id 
                left join obs seguimentoTPT on ( seguimentoTPT.encounter_id = e.encounter_id and seguimentoTPT.voided = 0 and seguimentoTPT.concept_id = 23987 )
            where p.voided = 0 and e.voided = 0 and regime3HP.voided = 0 
                and e.encounter_type = 60 and regime3HP.concept_id = 23985 and regime3HP.value_coded in (23954,23984)
                and ( seguimentoTPT.value_coded in (1257,1267) or seguimentoTPT.value_coded is null)
                and  e.encounter_datetime <=:endDate and  e.location_id =:location
       )
     inicio
    left join
        (
            select p.patient_id, e.encounter_datetime data_inicio_3HP 
            from patient p 
                inner join encounter e on p.patient_id=e.patient_id 
                inner join obs regime3HP on regime3HP.encounter_id=e.encounter_id 
            where p.voided=0 and e.voided=0 and regime3HP.voided=0 
                and e.encounter_type=60 and regime3HP.concept_id=23985 and regime3HP.value_coded in (23954,23984) 
                and e.encounter_datetime <=:endDate and e.location_id=:location
          union
          
          select p.patient_id, obsEstado.obs_datetime data_inicio_3HP 
          from patient p 
            inner join encounter e on p.patient_id = e.patient_id 
            inner join obs ultimaProfilaxiaIsoniazia on ultimaProfilaxiaIsoniazia.encounter_id = e.encounter_id 
            inner join obs obsEstado on obsEstado.encounter_id = e.encounter_id
            where   e.encounter_type in(6,53) and  ultimaProfilaxiaIsoniazia.concept_id=23985 and ultimaProfilaxiaIsoniazia.value_coded=23954 
             and obsEstado.concept_id=165308 and obsEstado.value_coded=1256 
             and p.voided=0 and e.voided=0 and ultimaProfilaxiaIsoniazia.voided=0 and obsEstado.voided=0 and   e.location_id=:location
             and obsEstado.obs_datetime <=:endDate
            
            union
            
            select p.patient_id, e.encounter_datetime data_inicio_3HP                                                                                         
            from patient p                                                                                                                             
            inner join encounter e on p.patient_id = e.patient_id                                                                                         
               inner join obs outrasPrescricoesDT3HP on outrasPrescricoesDT3HP.encounter_id = e.encounter_id                                                                                         
            where p.voided = 0 and e.voided = 0 and outrasPrescricoesDT3HP.voided = 0 
            and e.encounter_type = 6 and outrasPrescricoesDT3HP.concept_id=1719 and outrasPrescricoesDT3HP.value_coded in (165307,23954)                       
            and e.encounter_datetime <=:endDate and e.location_id =:location
        )inicioAnterior on inicioAnterior.patient_id  = inicio.patient_id 
        and inicioAnterior.data_inicio_3HP between (inicio.data_inicio_3HP - INTERVAL 4 MONTH) and (inicio.data_inicio_3HP - INTERVAL 1 day) 
    where inicioAnterior.patient_id is null 
    
   ) 
inicio_3HP 
    inner join encounter e on e.patient_id= inicio_3HP.patient_id                                                                                         
     inner join obs obs3hp on obs3hp.encounter_id=e.encounter_id                                                                                             
     inner join obs obsTipo on obsTipo.encounter_id=e.encounter_id                                                                                         
where e.voided=0 and obs3hp.voided=0 and obsTipo.voided=0                                                                                              
    and e.encounter_type=60 and obs3hp.concept_id=23985 and obs3hp.value_coded in (23954,23984)  
        and obsTipo.concept_id=23986 and obsTipo.value_coded=1098     
    and e.encounter_datetime between inicio_3HP.data_inicio_3HP and (inicio_3HP.data_inicio_3HP + INTERVAL 4 month)  
        and e.location_id=:location    
    group by inicio_3HP.patient_id  
    having count(distinct e.encounter_id)>=:location    

)TPT_ELIG_FR9 on TPT_ELIG_FR9.patient_id=coorte12meses_final.patient_id 
            left join  
            (   select  p.patient_id 
                from    patient p 
                        inner join encounter e on p.patient_id=e.patient_id 
                        inner join obs o on o.encounter_id=e.encounter_id 
                where   e.encounter_type in (6,9) and e.voided=0 and o.voided=0 and p.voided=0  
                        and o.concept_id=1268 and o.value_coded=1256 and e.location_id=:location 
                        and o.obs_datetime between (:endDate - INTERVAL 7 MONTH) and :endDate 
                union 
                select  p.patient_id 
                from    patient p 
                        inner join encounter e on p.patient_id=e.patient_id 
                        inner join obs o on o.encounter_id=e.encounter_id 
                where   e.encounter_type in (6,9) and e.voided=0 and o.voided=0 and p.voided=0 and 
                        o.concept_id=1113 and e.location_id=:location and  
                        o.value_datetime between (:endDate - INTERVAL 7 MONTH) and :endDate  
                union 
                select  patient_id 
                from    patient_program 
                where   program_id=5 and voided=0 and date_enrolled between (:endDate - INTERVAL 7 MONTH) and :endDate and 
                        location_id=:location
                union 
                SELECT  p.patient_id  
                FROM    patient p 
                        INNER JOIN encounter e ON e.patient_id = p.patient_id 
                        INNER JOIN obs o ON o.encounter_id = e.encounter_id 
                WHERE   p.voided = 0 AND e.voided = 0 AND o.voided = 0 
                        AND e.encounter_type=53 
                        AND o.concept_id = 1406 
                        AND o.value_coded=42 
                        AND e.location_id=:location and o.obs_datetime between (:endDate - INTERVAL 7 MONTH) and :endDate 
                union 
                select  p.patient_id 
                from    patient p 
                        inner join encounter e on p.patient_id=e.patient_id 
                        inner join obs o on o.encounter_id=e.encounter_id 
                where   e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 and 
                        o.concept_id=23761 and o.value_coded=1065 and  
                        e.encounter_datetime between (:endDate - INTERVAL 7 MONTH) and :endDate and  
                        e.location_id=:location
            )TPT_ELIG_FR10 on TPT_ELIG_FR10.patient_id=coorte12meses_final.patient_id 
            left join  
            ( 
            select p.patient_id,o.value_datetime data_tratamento                                                           
		from patient p                                                                                                   
			inner join encounter e on p.patient_id=e.patient_id                                                         
		    inner join obs o on o.encounter_id=e.encounter_id                                                           
		where e.voided=0 and o.voided=0 and p.voided=0                                                                 
			and e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id=:location 
     		and  o.value_datetime  between (:endDate - INTERVAL 14 DAY) and :endDate
		
     	union

		select p.patient_id,e.encounter_datetime data_tratamento                                                           
		from patient p                                                                                                   
			inner join encounter e on p.patient_id=e.patient_id                                                         
		    inner join obs o on o.encounter_id=e.encounter_id                                                           
		where e.voided=0 and o.voided=0 and p.voided=0                                                                 
		      and e.encounter_type in (6,9) and o.concept_id=6257 and o.value_coded =1065 and e.location_id=:location 
		      and  e.encounter_datetime  between (:endDate - INTERVAL 14 DAY) and :endDate
		
		 union

		select p.patient_id,e.encounter_datetime data_tratamento                                                           
        from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id                                                           
		where e.voided=0 and o.voided=0 and p.voided=0                                                                 
     		and e.encounter_type in (6,9) and o.concept_id=6277 and o.value_coded = 703  and e.location_id=:location 
     		and e.encounter_datetime  between (:endDate - INTERVAL 14 DAY) and :endDate
     		
     	union
     	
     	select  p.patient_id,e.encounter_datetime data_inicio                                                           
		from patient p                                                                                                   
			inner join encounter e on p.patient_id=e.patient_id                                                         
			inner join obs obs6277 on obs6277.encounter_id= e.encounter_id 
			inner join obs obs6257 on obs6257.encounter_id= e.encounter_id                                                           
		where p.voided = 0 and e.voided = 0 and obs6277.voided = 0 and obs6257.voided = 0                                                                  
			and e.encounter_type in (6,9) and obs6277.concept_id=6277 and obs6277.value_coded = 664 and e.location_id=:location and obs6257.concept_id= 6257 and obs6257.value_coded in(1065,1066)
			and e.encounter_datetime  between (:endDate - INTERVAL 14 DAY) and :endDate

		union

		select p.patient_id,o.obs_datetime data_tratamento                                                           
		from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id                                                           
		where e.voided=0 and o.voided=0 and p.voided=0                                                                 
      		and e.encounter_type=53 and o.concept_id=1406 and o.value_coded=42  and e.location_id=:location 
     		and o.obs_datetime  between (:endDate - INTERVAL 14 DAY) and :endDate
		
     	union

		select pg.patient_id, date_enrolled data_tratamento                                                             
		from patient p 
        	inner join patient_program pg on p.patient_id=pg.patient_id                                       
		where pg.voided=0 and p.voided=0 and program_id=5 and location_id=:location  
        	and date_enrolled between (:endDate - INTERVAL 14 DAY) and :endDate
		
        union

		select p.patient_id,e.encounter_datetime data_tratamento                                                           
		from patient p                                                                                                   
			inner join encounter e on p.patient_id=e.patient_id                                                         
			inner join obs o on o.encounter_id=e.encounter_id                                                           
		where e.voided=0 and o.voided=0 and p.voided=0                                                                 
			and e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256  and e.location_id=:location 
		    and  e.encounter_datetime  between (:endDate - INTERVAL 14 DAY) and  :endDate
		
		union

		select p.patient_id,e.encounter_datetime data_tratamento                                                           
		from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id                                                           
		where e.voided=0 and o.voided=0 and p.voided=0                                                                 
      		and e.encounter_type=6 and o.concept_id=23758 and o.value_coded=1065  and e.location_id=:location 
     		and e.encounter_datetime  between (:endDate - INTERVAL 14 DAY) and :endDate
		
     	union

		select p.patient_id,e.encounter_datetime data_tratamento                                                           
		from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id                                                           
		where e.voided=0 and o.voided=0 and p.voided=0                                                                 
      		and e.encounter_type=6 and o.concept_id=23761 and o.value_coded=1065  and e.location_id=:location 
     		and e.encounter_datetime  between (:endDate - INTERVAL 14 DAY) and :endDate

		union

		select p.patient_id,e.encounter_datetime data_inicio                                                           
		from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id
		where e.voided=0 and o.voided=0 and p.voided=0                                                             
     		and e.encounter_type = 6 and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,23760,161)   
      		and e.location_id=:location  and e.encounter_datetime   between (:endDate - INTERVAL 14 DAY) and :endDate
		
      	union

      	select p.patient_id,e.encounter_datetime data_inicio                                                      
		from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id
		where e.voided=0 and o.voided=0 and p.voided=0                                                             
      		and e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   
      		and e.location_id=:location  and e.encounter_datetime   between (:endDate - INTERVAL 14 DAY) and :endDate
		
      	union

		select p.patient_id,e.encounter_datetime data_inicio                                                      
		from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id
		where e.voided=0 and o.voided=0 and p.voided=0                                                             
     		and e.encounter_type=6 and o.concept_id  in(23723,23774,23951,307,12) and o.value_coded is not null
      		and e.location_id=:location  and e.encounter_datetime   between (:endDate - INTERVAL 14 DAY) and :endDate
      
		union
		
		select p.patient_id,e.encounter_datetime data_inicio                                                      
		from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id
		where e.voided=0 and o.voided=0 and p.voided=0                                                             
     		and e.encounter_type=13 and o.concept_id  in(307, 23723, 23724, 23951, 165189) and o.value_coded is not null
      		and e.location_id=:location  and e.encounter_datetime   between (:endDate - INTERVAL 14 DAY) and :endDate
		
 		union
		
 		select p.patient_id,e.encounter_datetime data_inicio                                                      
		from patient p                                                                                                   
      		inner join encounter e on p.patient_id=e.patient_id                                                         
      		inner join obs o on o.encounter_id=e.encounter_id
		where e.voided=0 and o.voided=0 and p.voided=0                                                             
      		and e.encounter_type= 51 and o.concept_id = 23951 and o.value_coded is not null
      		and e.location_id=:location  and e.encounter_datetime   between (:endDate - INTERVAL 14 DAY) and :endDate
            ) TPT_ELIG_FR12 on TPT_ELIG_FR12.patient_id=coorte12meses_final.patient_id 
            left join  
            (select patient_id,decisao 
                from  
                (   select  inicio_real.patient_id, 
                            gravida_real.data_gravida, 
                            lactante_real.data_parto, 
                            if(max(gravida_real.data_gravida) is null and max(lactante_real.data_parto) is null,null, 
                                if(max(gravida_real.data_gravida) is null,'LACTANTE', 
                                    if(max(lactante_real.data_parto) is null,'GRÁVIDA', 
                                        if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'LACTANTE','GRÁVIDA')))) decisao 
                    from 
                    (   select  p.patient_id  
                        from    patient p inner join encounter e on e.patient_id=p.patient_id  
                        where   e.voided=0 and p.voided=0 and e.encounter_type in (5,7) and e.encounter_datetime<=:endDate and e.location_id=:location
                        union 
                        select  pg.patient_id 
                        from    patient p inner join patient_program pg on p.patient_id=pg.patient_id 
                        where   pg.voided=0 and p.voided=0 and program_id in (1,2) and date_enrolled<=:endDate and location_id=:location
                        union 
                        Select  p.patient_id 
                        from    patient p 
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and  
                                o.concept_id=23891 and o.value_datetime is not null and  
                                o.value_datetime<=:endDate and e.location_id=:location
                    )inicio_real 
                    left join  
                    (   Select  p.patient_id,e.encounter_datetime data_gravida 
                        from    patient p  
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and  
                                e.encounter_type in (5,6) and e.encounter_datetime  between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location
                        union 
                        Select  p.patient_id,e.encounter_datetime data_gravida 
                        from    patient p inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and  
                                e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location
                        union 
                        Select  p.patient_id,e.encounter_datetime data_gravida 
                        from    patient p inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and  
                                e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location 
                        union 
                        Select  p.patient_id,e.encounter_datetime data_gravida 
                        from    patient p  
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and  
                                e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location     
                        union 
                        select  pp.patient_id,pp.date_enrolled data_gravida 
                        from    patient_program pp  
                        where   pp.program_id=8 and pp.voided=0 and  
                                pp.date_enrolled between (curdate() - INTERVAL 9 MONTH) and curdate() and pp.location_id=:location
                        union 
                        Select  p.patient_id,obsART.value_datetime data_gravida 
                        from    patient p  
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                                inner join obs obsART on e.encounter_id=obsART.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and  
                                e.encounter_type=53 and obsART.value_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location and  
                                obsART.concept_id=1190 and obsART.voided=0 
                        union  
                        
			        select p.patient_id,data_colheita.value_datetime data_gravida from patient p                                                                                                                                                                       	
				inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                               	
				inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id                                                                                                                                                                           	
				inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                        	
			where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=1982 																																
				and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                     																					
				and data_colheita.concept_id =23821 and data_colheita.value_datetime between (curdate() - INTERVAL 9 MONTH) and curdate() and e.location_id=:location 
				
                        ) gravida_real on gravida_real.patient_id=inicio_real.patient_id   
                    left join  
                    (Select     p.patient_id,o.value_datetime data_parto 
                        from    patient p inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 and  
                                e.encounter_type in (5,6) and o.value_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and e.location_id=:location    
                        union    
                        Select  p.patient_id, e.encounter_datetime data_parto 
                        from    patient p  
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and  
                                e.encounter_type=6 and e.encounter_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and e.location_id=:location
                        union 
                        Select  p.patient_id, obsART.value_datetime data_parto 
                        from    patient p  
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                                inner join obs obsART on e.encounter_id=obsART.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and  
                                e.encounter_type=53 and e.location_id=:location and  
                                obsART.value_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and  
                                obsART.concept_id=1190 and obsART.voided=0 
                        union  
                        Select  p.patient_id, e.encounter_datetime data_parto 
                        from    patient p  
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs o on e.encounter_id=o.encounter_id 
                        where   p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332 and  
                                e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and e.location_id=:location
                        union        
                        select  pg.patient_id,ps.start_date data_parto 
                        from    patient p  
                                inner join patient_program pg on p.patient_id=pg.patient_id 
                                inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                        where   pg.voided=0 and ps.voided=0 and p.voided=0 and  
                                pg.program_id=8 and ps.state=27 and ps.end_date is null and  
                                ps.start_date between (curdate() - INTERVAL 18 MONTH) and curdate() and location_id=:location

                       union

                    select p.patient_id,data_colheita.value_datetime data_gravida from patient p                                                                                                                                                                       	
				inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                               	
				inner join obs esta_gravida on e.encounter_id=esta_gravida.encounter_id                                                                                                                                                                           	
				inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                        	
			where p.voided=0 and e.voided=0 and esta_gravida.voided=0 and data_colheita.voided = 0 and esta_gravida.concept_id=6332 																																
				and esta_gravida.value_coded = 1065 and e.encounter_type=51                                                                                                     																					
				and data_colheita.concept_id =23821 and data_colheita.value_datetime between (curdate() - INTERVAL 18 MONTH) and curdate() and e.location_id=:location 
				
                    ) lactante_real on lactante_real.patient_id=inicio_real.patient_id 
                    where   lactante_real.data_parto is not null or gravida_real.data_gravida is not null 
                    group by inicio_real.patient_id 
                ) gravidaLactante        
                inner join person pe on pe.person_id=gravidaLactante.patient_id      
                where pe.voided=0 and pe.gender='F' 
            ) TPT_ELIG_FR16 on TPT_ELIG_FR16.patient_id=coorte12meses_final.patient_id 
            left join  
            (
            select max_consulta.patient_id,max_consulta.data_seguimento,proximaConsulta.data_proximo_seguimento 
                from  
                (   Select  p.patient_id,max(encounter_datetime) data_seguimento, e.encounter_id
                    from    patient p  
                            inner join encounter e on e.patient_id=p.patient_id 
                    where   p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  
                            e.location_id=:location and e.encounter_datetime<=curdate() 
                    group by p.patient_id 
                ) max_consulta  
                   left join 
                   ( 
                   select fr.patient_id,fr.encounter_datetime data_seguimento,obs_seguimento.value_datetime data_proximo_seguimento,fr.encounter_id from
                    (
                 Select  p.patient_id,max(encounter_datetime) encounter_datetime, e.encounter_id
                    from    patient p  
                            inner join encounter e on e.patient_id=p.patient_id 
                    where   p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  
                            e.location_id=:location and e.encounter_datetime<=curdate() 
                    group by p.patient_id 
                    ) fr 
                    inner join encounter e on e.patient_id=fr.patient_id
		            inner join obs obs_seguimento on obs_seguimento.encounter_id=e.encounter_id  
                    and  obs_seguimento.concept_id=1410 
                    and obs_seguimento.voided=0 and  e.encounter_type in (6,9) and e.encounter_datetime=fr.encounter_datetime
                    ) proximaConsulta on proximaConsulta.patient_id=max_consulta.patient_id
           
                    ) TPT_ELIG_FR19 on TPT_ELIG_FR19.patient_id=coorte12meses_final.patient_id 
                    left join (
					                    select f.patient_id,f.data_levantamento,data_proximo_levantamento  from  ( 
					select fila.patient_id,max(fila.data_levantamento) data_levantamento,obs_fila.value_datetime data_proximo_levantamento  from  ( 
					Select p.patient_id,max(encounter_datetime) data_levantamento  from  patient p 
					inner join encounter e on e.patient_id=p.patient_id 
					where  p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
					and e.encounter_datetime <=curdate() 
					group by p.patient_id 
					)fila 
					inner join  obs obs_fila on obs_fila.person_id=fila.patient_id 
					and obs_fila.voided=0  and obs_fila.obs_datetime=fila.data_levantamento 
					and obs_fila.concept_id=5096 
					and obs_fila.location_id=:location 
					group by fila.patient_id 
					) f
                    )fila on fila.patient_id=coorte12meses_final.patient_id
            where (data_estado is null or (data_estado is not null and  data_fila > data_estado)) and date_add(data_usar, interval 28 day) >=:endDate  
                    and  TPT_ELIG_FR4.patient_id is null  
                    and  TPT_ELIG_FR8.patient_id is null  
                    and  TPT_ELIG_FR9.patient_id is null  
                    and  TPT_ELIG_FR10.patient_id is null  
                    and  TPT_ELIG_FR12.patient_id is null group by patient_id