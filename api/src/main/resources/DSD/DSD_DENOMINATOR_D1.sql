select patient_id from ( select inicio.patient_id, inicio.data_inicio, timestampdiff(year,per.birthdate,:endDate) idade, timestampdiff(month,inicioMaisReal.data_inicio,:endDate) idadeEmTarv, cd4Absoluto.value_numeric cd4Abs,cd4SemiQuantitativo.value_coded cd4SemiQt, cd4Percentual.value_numeric cd4Per, cvmenor1000.patient_id pidcv12meses,cvmenor1000.person_id pidcvmenor100  
            from 
             (
            select coorte12meses_final.* 
			from( 
				select inicio_fila_seg_prox.*,
			     	data_encountro data_usar_c, 
			          data_proximo_lev data_usar 
			     from( 
			         	select inicio_fila_seg.*
			         	from( 
			          	select inicio.*, 
			                 saida.data_estado, 
			                 max_fila.data_fila,
			                 fila_recepcao.data_proximo_lev,
			                 consulta.data_encountro
			            	from( 
			                Select patient_id, min(data_inicio) data_inicio 
					         from ( 
					                select f.patient_id, min(f.data_inicio) data_inicio from
					                (
					                 select e.patient_id, min(e.encounter_datetime) as data_inicio
					                 from patient p
					                     inner join encounter e on p.patient_id=e.patient_id
					                 where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate
					                 and e.location_id=:location and YEAR(STR_TO_DATE(e.encounter_datetime, '%Y-%m-%d')) >= 1000
					                     group by p.patient_id
					                 union
					                 Select p.patient_id,min(value_datetime) data_inicio
					                 from patient p
					                     inner join encounter e on p.patient_id=e.patient_id
					                     inner join obs o on e.encounter_id=o.encounter_id
					                 where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866
					                     and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location
					                     and YEAR(STR_TO_DATE(o.value_datetime, '%Y-%m-%d')) >= 1000
					                     group by p.patient_id
					               )f
					               where f.patient_id is not null and f.data_inicio is not null
			                       group by f.patient_id
								     ) 
								     inicio_real group by patient_id 
			                     )inicio 
			                     left join ( 
									select saida.patient_id, max(saida.data_estado)data_estado
									from(
										select saidas_por_suspensao.patient_id, saidas_por_suspensao.data_estado 
				                        		from(
				                       			
				                       			select saidas_por_suspensao.patient_id, max(saidas_por_suspensao.data_estado) data_estado 
				                            		from( 
				                             			
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
				                              ) saidas_por_suspensao group by saidas_por_suspensao.patient_id
				                           ) saidas_por_suspensao
				                           left join(
											
											select p.patient_id,max(e.encounter_datetime) encounter_datetime
										     from patient p
										     	inner join encounter e on e.patient_id = p.patient_id
										     where p.voided = 0 and e.voided = 0 and e.encounter_datetime <:endDate and e.location_id =:location and e.encounter_type=18
										      group by p.patient_id
										) fila on saidas_por_suspensao.patient_id = fila.patient_id 
										where saidas_por_suspensao.data_estado>=fila.encounter_datetime or fila.encounter_datetime is null
										 
									union
										
									select dead_state.patient_id, dead_state.data_estado
									from(
										
										select patient_id,max(data_estado) data_estado                                                                                              
										from (  
											
											select distinct max_estado.patient_id, max_estado.data_estado 
											from(                                                                
												
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
											
											select  p.patient_id,max(o.obs_datetime) data_estado                                                                                              
											from patient p                                                                                                                   
												inner join person pe on pe.person_id = p.patient_id                                                                         
												inner join encounter e on p.patient_id=e.patient_id                                                                         
												inner join obs  o on e.encounter_id=o.encounter_id                                                                          
											where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                                                               
												and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1366                         
												and o.obs_datetime<=:endDate and e.location_id=:location                                                                        
												group by p.patient_id                                                                                                               
											
											union                                                                                                                               
											
											select person_id as patient_id,death_date as data_estado                                                                            
											from person                                                                                                                         
											where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate 
											                                                                                                           
										) dead_state group by dead_state.patient_id  
								) dead_state 
								left join(
									
									select fila_seguimento.patient_id,max(fila_seguimento.data_encountro) data_encountro
									from(
										
										select p.patient_id,max(encounter_datetime) data_encountro                                                                                                
										from patient p                                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                                         
											inner join encounter e on e.patient_id=p.patient_id                                                                                         
										where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18                                                                      
											and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
											group by p.patient_id  
										
										union
												
										select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
										from patient p                                                                                                                                   
											inner join person pe on pe.person_id = p.patient_id                                                                                         
											inner join encounter e on e.patient_id=p.patient_id                                                                                         
										where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)                                                                
											and e.location_id=:location and e.encounter_datetime<=:endDate                                                                                  
											group by p.patient_id   
									) fila_seguimento group by fila_seguimento.patient_id  
							 ) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
								where fila_seguimento.data_encountro <= dead_state.data_estado or fila_seguimento.data_encountro is null
				
							union
				
				         		select saidas_por_transferencia.patient_id,ultimo_levantamento.data_ultimo_levantamento data_estado
				          	from (
				         			
				         			select saidas_por_transferencia.patient_id, saidas_por_transferencia.data_estado
				                  	from (
				                       
				                       select saidas_por_transferencia.patient_id, max(data_estado) data_estado
				                       from(
				                           	select distinct max_estado.patient_id, max_estado.data_estado 
				                           	from(                                                                
					                                 select pg.patient_id, max(ps.start_date) data_estado                                                                                          
					                                 from patient p                                                                                                               
					                                 		inner join person pe on pe.person_id = p.patient_id                                                                         
					                                     	inner join patient_program pg on p.patient_id = pg.patient_id                                                               
					                                     	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                           
					                                 where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2                                    
					                                 		and ps.start_date< :endDate and pg.location_id =:location group by pg.patient_id                                          
				                             	) max_estado                                                                                                                        
				                              	inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
				                                 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
				                             	where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location                 
				                             
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
				
				                          ) saidas_por_transferencia group by saidas_por_transferencia.patient_id
				                     ) saidas_por_transferencia
				                       left join (
									      
									      select p.patient_id,max(e.encounter_datetime) encounter_datetime
									      from patient p
									      	inner join encounter e on e.patient_id = p.patient_id
									      where p.voided = 0 and e.voided = 0 and e.encounter_datetime < :endDate and e.location_id =:location and e.encounter_type=18
									      	group by p.patient_id
				                      	)lev on saidas_por_transferencia.patient_id=lev.patient_id
				                 	where lev.encounter_datetime<=saidas_por_transferencia.data_estado or lev.encounter_datetime is null
				                 		group by saidas_por_transferencia.patient_id 
				           ) saidas_por_transferencia
				           inner join (  
				           		
				           		select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
				                    from (
				     
				                   		select maxFila.patient_id, date_add(max(obs_fila.value_datetime), interval 1 day) data_ultimo_levantamento 
				                   		from ( 
				                   			
				                   			select fila.patient_id,fila.data_fila data_fila,e.encounter_id 
				                   			from( 
							                  
							                   select p.patient_id,max(encounter_datetime) data_fila  
							                   from patient p 
							                          inner join encounter e on e.patient_id=p.patient_id 
							                   where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location and date(e.encounter_datetime)<=:endDate 
							                         group by p.patient_id 
							                  )fila 
							                   inner join encounter e on  date(e.encounter_datetime)=date(fila.data_fila) and e.encounter_type=18 and e.voided=0 and e.patient_id=fila.patient_id 
							            )maxFila  
							   		  left join encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=maxFila.patient_id 
									 	and ultimo_fila_data_criacao.voided=0 
									   	and ultimo_fila_data_criacao.encounter_type = 18 
									   	and date(ultimo_fila_data_criacao.encounter_datetime) = date(maxFila.data_fila) 
									   	and ultimo_fila_data_criacao.location_id=:location 
									   left join obs obs_fila on obs_fila.person_id=maxFila.patient_id 
									   	and obs_fila.voided=0 
									   	and (date(obs_fila.obs_datetime)=date(maxFila.data_fila)  or (date(ultimo_fila_data_criacao.date_created) = date(obs_fila.date_created) and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id )) 
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
					                         and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime<=:endDate                                                                                        
					                   	group by p.patient_id
					               ) ultimo_levantamento group by patient_id
				           	) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
				          		where (ultimo_levantamento.data_ultimo_levantamento <= :endDate	and saidas_por_transferencia.data_estado<=:endDate)
			             ) saida group by saida.patient_id
			           ) saida on inicio.patient_id=saida.patient_id 
			                    
			           left join 
			           ( 
			           	select patient_id, data_fila_or_segu_or_recepcao, max(data_proximo_lev) data_proximo_lev from 
			           	(
			               select maxFila.patient_id, maxFila.data_fila data_fila_or_segu_or_recepcao, max(obs_fila.value_datetime) data_proximo_lev from 
			               ( 
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
			              left join encounter ultimo_fila_data_criacao on ultimo_fila_data_criacao.patient_id=maxFila.patient_id 
								and ultimo_fila_data_criacao.voided=0 
								and ultimo_fila_data_criacao.encounter_type = 18 
							    and date(ultimo_fila_data_criacao.encounter_datetime) = date(maxFila.data_fila) 
								and ultimo_fila_data_criacao.location_id=:location 
								left join 
								obs obs_fila on obs_fila.person_id=maxFila.patient_id 
								and obs_fila.voided=0 
								and (date(obs_fila.obs_datetime)=date(maxFila.data_fila)  or (date(ultimo_fila_data_criacao.date_created) = date(obs_fila.date_created) and ultimo_fila_data_criacao.encounter_id = obs_fila.encounter_id )) 
								and obs_fila.concept_id=5096 
								and obs_fila.location_id=:location 
								group by maxFila.patient_id 
			
			       		 union
						select patient_id,  dat_fila, date_add(dat_fila, interval 30 day) data_proximo_lev from (
			               Select p.patient_id, max(value_datetime) as  dat_fila
			               from patient p 
			                   inner join encounter e on p.patient_id=e.patient_id 
			                   inner join obs o on e.encounter_id=o.encounter_id 
			               where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  o.concept_id=23866 
			                   and o.value_datetime is not null and  o.value_datetime<=:endDate and e.location_id=:location 
			                   group by p.patient_id 
			                   )fila_recepcao
			                   ) fila_recepcao group by fila_recepcao.patient_id
			          
			          )fila_recepcao on fila_recepcao.patient_id=inicio.patient_id
			
			           left join
			           (
			           	select patient_id, max(data_encountro) data_encountro from 
			           	(
			
			               Select p.patient_id,max(encounter_datetime) data_encountro
			               from patient p 
			                   inner join encounter e on e.patient_id=p.patient_id 
			               where p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  e.location_id=:location and e.encounter_datetime<=:endDate 
			                   group by p.patient_id 
			                union
			
			
			                Select p.patient_id,max(encounter_datetime) data_encountro 
			                from patient p 
			                    inner join encounter e on e.patient_id=p.patient_id 
			                    where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
			                    and date(e.encounter_datetime)<=:endDate 
			                    group by p.patient_id 
			                    ) consulta group by consulta.patient_id
			           )consulta on consulta.patient_id=inicio.patient_id
			           left join
			               (
			           	Select p.patient_id,max(encounter_datetime) data_fila from patient p 
			                    inner join encounter e on e.patient_id=p.patient_id 
			                    where p.voided=0 and e.voided=0 and e.encounter_type=18 and e.location_id=:location 
			                    and date(e.encounter_datetime)<=:endDate 
			                    group by p.patient_id 
			           	)max_fila on max_fila.patient_id=inicio.patient_id
			
			) inicio_fila_seg 
			
			     ) inicio_fila_seg_prox 
			         group by patient_id 
			    ) coorte12meses_final 
			 WHERE  ((data_estado is null or ((data_estado is not null) and  data_fila>data_estado))  and date_add(data_usar, interval 59 day) >=:endDate) 

             ) inicio  
            inner join person per on per.person_id=inicio.patient_id
            left join
            (

               select patient_id,min(data_inicio) data_inicio                                                                              
                 from                                                                                                                        
                     (                                                                                                                       
                         select  p.patient_id,min(e.encounter_datetime) data_inicio                                                          
                         from    patient p                                                                                                   
                                 inner join person pe on pe.person_id = p.patient_id                                                         
                                 inner join encounter e on p.patient_id=e.patient_id                                                         
                                 inner join obs o on o.encounter_id=e.encounter_id                                                           
                         where   e.voided=0 and o.voided=0 and p.voided=0 and                                              
                                 e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and                               
                                 e.encounter_datetime<=:endDate and e.location_id=:location                                                  
                         group by p.patient_id                                                                                               
                         union                                                                                                               
          
                         select  p.patient_id,min(value_datetime) data_inicio                                                                
                         from    patient p                                                                                                   
                                 inner join person pe on pe.person_id = p.patient_id                                                         
                                 inner join encounter e on p.patient_id=e.patient_id                                                         
                                 inner join obs o on e.encounter_id=o.encounter_id                                                           
                         where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and          
                                 o.concept_id=1190 and o.value_datetime is not null and                                                      
                                 o.value_datetime<=:endDate and e.location_id=:location                                                      
                         group by p.patient_id                                                                                               
                         union                                                                                                               
           
                         select  pg.patient_id,min(date_enrolled) data_inicio                                                                
                         from    patient p                                                                                                   
                             inner join person pe on pe.person_id = p.patient_id                                                             
                             inner join patient_program pg on p.patient_id=pg.patient_id                                                     
                         where   pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
                         group by pg.patient_id                                                                                              
                         union                                                                                                               
            
                           select    e.patient_id, MIN(e.encounter_datetime) AS data_inicio                                                  
                           FROM      patient p                                                                                               
                                     inner join person pe on pe.person_id = p.patient_id                                                     
                                     inner join encounter e on p.patient_id=e.patient_id                                                     
                           WHERE     p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location  
                           GROUP BY  p.patient_id                                                                                                        
                         union                                                                                                                           
            
                         select  p.patient_id,min(value_datetime) data_inicio                                                                            
                         from    patient p                                                                                                               
                                 inner join person pe on pe.person_id = p.patient_id                                                                     
                                 inner join encounter e on p.patient_id=e.patient_id                                                                     
                                 inner join obs o on e.encounter_id=o.encounter_id                                                                       
                         where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and                                  
                                 o.concept_id=23866 and o.value_datetime is not null and                                                                 
                                 o.value_datetime<=:endDate and e.location_id=:location                                                                  
                         group by p.patient_id                                                                                                           
                     ) inicio_real
                     group by patient_id                                                                                                                       
            )inicioMaisReal on   inicioMaisReal.patient_id=inicio.patient_id
            left join  
            ( 
            select distinct max_cv.patient_id,o.person_id  from (  
            Select p.patient_id,max(date(o.obs_datetime)) max_data_cv  From patient p  
            inner join encounter e on p.patient_id=e.patient_id  
            inner join obs o on e.encounter_id=o.encounter_id  
            where  p.voided=0 and e.voided=0 and o.voided=0 and concept_id in (856,1305) and  e.encounter_type in (6,9,13,51,53) 
            and date(o.obs_datetime) between (:endDate - INTERVAL 12 MONTH) AND :endDate and e.location_id=:location  
            group by p.patient_id 
            )max_cv  
            left join obs o on o.person_id=max_cv.patient_id and date(max_cv.max_data_cv)=date(o.obs_datetime) and o.voided=0 and  
            ((o.concept_id=856 and o.value_numeric<1000) or (o.concept_id=1305 )) and o.location_id=:location 
            ) cvmenor1000 on inicio.patient_id=cvmenor1000.patient_id  
            left join ( 
            select distinct max_cd4.patient_id,o.value_numeric  from (  
            Select p.patient_id,max(o.obs_datetime) max_data_cd4  From patient p  
            inner join encounter e on p.patient_id=e.patient_id  
            inner join obs o on e.encounter_id=o.encounter_id  
            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 1695 and  e.encounter_type in (6,9,13,53,51) 
            and o.obs_datetime between (:endDate - INTERVAL 12 MONTH) AND :endDate and e.location_id=:location 
            group by p.patient_id 
            )max_cd4  
            left join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0 and  
            o.concept_id = 1695 and o.value_numeric>200 and o.location_id=:location 
            ) cd4Absoluto on inicio.patient_id=cd4Absoluto.patient_id  
            left join ( 
            select distinct max_cd4.patient_id,o.value_coded  from (  
            Select p.patient_id,max(o.obs_datetime) max_data_cd4  From patient p  
            inner join encounter e on p.patient_id=e.patient_id  
            inner join obs o on e.encounter_id=o.encounter_id  
            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id = 165515 and  e.encounter_type in (6,9,13,53,51) 
            and o.obs_datetime between (:endDate - INTERVAL 12 MONTH) AND :endDate and e.location_id=:location 
            group by p.patient_id 
            )max_cd4  
            left join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0 and  
            o.concept_id = 165515 and o.value_coded = 1254 and o.location_id=:location 
            ) cd4SemiQuantitativo on inicio.patient_id=cd4SemiQuantitativo.patient_id  
            left join  
            ( 
            select distinct max_cd4.patient_id,o.value_numeric  from ( 
            Select p.patient_id,max(o.obs_datetime) max_data_cd4  From patient p  
            inner join encounter e on p.patient_id=e.patient_id  
            inner join obs o on e.encounter_id=o.encounter_id  
            where   p.voided=0 and e.voided=0 and o.voided=0 and concept_id=730 and  e.encounter_type in (6,9,13,53,51) 
            and o.obs_datetime between (:endDate - INTERVAL 12 MONTH) AND :endDate and e.location_id=:location  
            group by p.patient_id 
            ) max_cd4  
            left join obs o on o.person_id=max_cd4.patient_id and max_cd4.max_data_cd4=o.obs_datetime and o.voided=0 and  
            o.concept_id=730 and o.value_numeric>15 and o.location_id=:location 
            ) cd4Percentual on inicio.patient_id=cd4Percentual.patient_id 
            ) elegivel  
            where (idade>=2 and idadeEmTarv>=3) and ((pidcvmenor100 is not null ) or (pidcv12meses is null and (idade>=5 and idade<=9) and (cd4Abs>200 or cd4SemiQt = 1254)) or (pidcv12meses is null and (idade>=2 and idade<=4) and (cd4Abs>750 or cd4Per>15)))