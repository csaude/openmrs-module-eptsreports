 select                             coorte12meses_final.patient_id
					             from                              
                         (
                        
		             select *                                                                                                   
		             from                                                                                                                    
		             (select 
		             inicio_fila_seg_prox.*,GREATEST(COALESCE(data_proximo_lev,data_recepcao_levantou30), COALESCE(data_recepcao_levantou30,data_proximo_lev)) data_usar 
		             from                                                                                                                        
		                 (select 
		                 inicio_fila_seg.*,                                                                                          
		                 max(obs_fila.value_datetime) data_proximo_lev,                                                                          
		                 date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30                                              
		              from                                                                                                                           
		            (select inicio.*,                                                                                                                    
		                 saida.data_estado, 
		                 saida.estado,                                                                                                         
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
		                                          ) tx_new 
		                                    ) inicio
		              )inicio                                                                                                                                    
		              left join                                                                                                                                  
		             ( 
		                
		                select patient_id,max(data_estado) data_estado,estado                                                                                             
		                     from                                                                                                                                        
		                         (                                                                                                                                       
		                             select distinct max_estado.patient_id, max_estado.data_estado, 1 estado from 
		                             (                                                                
		                                 select  pg.patient_id,                                                                                                          
		                                         max(ps.start_date) data_estado                                                                                          
		                                 from    patient p                                                                                                               
		                                     inner join patient_program pg on p.patient_id = pg.patient_id                                                               
		                                     inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
		                                 where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                        
		                                     and ps.start_date<= CURDATE() and pg.location_id =:location group by pg.patient_id                                           
		                             ) max_estado                                                                                                                        
		                                 inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
		                                 inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
		                             where pp.program_id = 2 and ps.state = 8 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location                 
		                             
		                                           union                                                                                                                               
		                             
		                                           select dead_state.patient_id, dead_state.data_estado, 2 estado 
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
		                                                                                          and ps.start_date<= CURDATE() and pg.location_id =:location group by pg.patient_id                                           
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
		                                                                        and o.obs_datetime<=CURDATE() and e.location_id=:location                                                                        
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
		                                                                        and e.encounter_type in (21,36,37) and  e.encounter_datetime<=CURDATE() and  e.location_id=:location                          
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
		                                                                                    and e.location_id=:location and e.encounter_datetime<=CURDATE()                                                                                  
		                                                                                    group by p.patient_id  
		                                                                        union
		                                                                        
		                                                                        select  p.patient_id,max(encounter_datetime) data_encountro                                                                                    
		                                                                        from patient p                                                                                                                                   
		                                                                              inner join encounter e on e.patient_id=p.patient_id                                                                                         
		                                                                        where   p.voided=0 and e.voided=0 and e.encounter_type in (6,9)                                                                
		                                                                              and e.location_id=:location and e.encounter_datetime<=CURDATE()                                                                                  
		                                                                              group by p.patient_id   
		                                                      ) fila_seguimento group by fila_seguimento.patient_id  
		                                           ) fila_seguimento on dead_state.patient_id = fila_seguimento.patient_id
		                                                where fila_seguimento.data_encountro is null or  fila_seguimento.data_encountro <= dead_state.data_estado
		                                           
		                                           union
		                                           
		                             select  p.patient_id,max(o.obs_datetime) data_estado, 1 estadoa
		                                                                                                                     
		                             from    patient p                                                                                                                   
		                                     inner join encounter e on p.patient_id=e.patient_id                                                                         
		                                     inner join obs  o on e.encounter_id=o.encounter_id                                                                          
		                             where   e.voided=0 and o.voided=0 and p.voided=0 and                                                              
		                                     e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1709 and                        
		                                     o.obs_datetime<=CURDATE() and e.location_id=:location                                                                        
		                             group by p.patient_id                                                                                                               
		                             union
		                                select saidas_por_transferencia.patient_id, data_estado, 3 estado  
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
		                                                                  and ps.start_date<=CURDATE() and pg.location_id =:location group by pg.patient_id                                           
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
		                                                            and o.obs_datetime<=CURDATE() and e.location_id=:location                                                                        
		                                                            group by p.patient_id                                                                                                               
		                                                      
		                                                     union                                                                                                                               
		                                                     
		                                                      select ultimaBusca.patient_id, ultimaBusca.data_estado                                                                              
		                                                      from (                                                                                                                              
		                                                             select p.patient_id,max(e.encounter_datetime) data_estado                                                                   
		                                                             from patient p                                                                                                              
		                                                                 inner join encounter e on p.patient_id=e.patient_id                                                                     
		                                                                 inner join obs o on o.encounter_id=e.encounter_id                                                                       
		                                                             where e.voided=0 and p.voided=0 and e.encounter_datetime<=CURDATE()                                       
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
		                                                                  and e.location_id=:location and e.encounter_datetime <=CURDATE()                                                                              
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
		                                          where ultimo_levantamento.data_ultimo_levantamento <=CURDATE()
		                                                                                                                                                                                                 
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
                         ) coorte12meses_final                              
                         inner join person p on p.person_id=coorte12meses_final.patient_id                              
                         left join                               
                         (    select pad1.*                              
                              from person_address pad1                              
                              inner join                               
                              (                              
                                    select person_id,max(person_address_id) id                               
                                    from person_address                              
                                    where voided=0                              
                                    group by person_id                              
                              ) pad2                              
                              where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id                              
                         ) pad3 on pad3.person_id=coorte12meses_final.patient_id                                                    
                         left join                                            
                         (    select pn1.*                              
                              from person_name pn1                              
                              inner join                               
                              (                              
                                    select person_id,max(person_name_id) id                               
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
                                    select patient_id,max(patient_identifier_id) id                              
                                    from patient_identifier                              
                                    where voided=0                              
                                    group by patient_id                              
                              ) pid2                              
                              where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id                              
                         ) pid on pid.patient_id=coorte12meses_final.patient_id                              
                         left join person_attribute pat on pat.person_id=coorte12meses_final.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0                              
                         left join                               
                         (                              
                                          select gravidaLactante.patient_id, if(decisao=1,'Lactante','Grávida') estadoMulher                              
                              from                               
                              (     select      p.person_id as patient_id,                              
                                                gravida_real.data_gravida,                              
                                                lactante_real.data_parto,                              
                                                if(max(gravida_real.data_gravida) is null and max(lactante_real.data_parto) is null,null,                              
                                                      if(max(gravida_real.data_gravida) is null,1,                              
                                                            if(max(lactante_real.data_parto) is null,2,                              
                                                                  if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),1,2)))) decisao                              
                                    from                              
                                                       
                                    person p                             
                                    left join                               
                                    (                              
                                          Select      p.patient_id,e.encounter_datetime data_gravida                              
                                          from  patient p                               
                                                      inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and                               
                                                      e.encounter_type in (5,6) and e.encounter_datetime  between (curdate() - interval 9 month) and curdate() and e.location_id=:location                              
                                          union                                     
                                          Select      p.patient_id,e.encounter_datetime data_gravida                              
                                          from  patient p inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and                               
                                                      e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - interval 9 month) and curdate() and e.location_id=:location                              
                                          union                                     
                                          Select      p.patient_id,e.encounter_datetime data_gravida                              
                                          from  patient p inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and                               
                                                      e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - interval 9 month) and curdate() and e.location_id=:location                               
                                          union                              
                                          Select      p.patient_id,e.encounter_datetime data_gravida                              
                                          from  patient p                               
                                                      inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and                               
                                                      e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - interval 9 month) and curdate() and e.location_id=:location                                     
                                          union                              
                                          select      pp.patient_id,pp.date_enrolled data_gravida                              
                                          from  patient_program pp                               
                                          where       pp.program_id=8 and pp.voided=0 and                               
                                                      pp.date_enrolled between (curdate() - interval 9 month) and curdate() and pp.location_id=:location                              
                                          union                                     
                                          Select      p.patient_id,obsART.value_datetime data_gravida                              
                                          from  patient p                               
                                                      inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                                      inner join obs obsART on e.encounter_id=obsART.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and                               
                                                      e.encounter_type=53 and obsART.value_datetime between (curdate() - interval 9 month) and curdate() and e.location_id=:location and                               
                                                      obsART.concept_id=1190 and obsART.voided=0                              
                                          union                                     
                                          Select      p.patient_id,e.encounter_datetime data_gravida                              
                                          from  patient p inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1465 and                               
                                                      e.encounter_type=6 and e.encounter_datetime between (curdate() - interval 9 month) and curdate() and e.location_id=:location  

                                          union
                                          
                                          Select      p.patient_id,e.encounter_datetime data_gravida                              
                                          from  patient p inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id   
                                                      inner join obs obsColheita on   obsColheita.encounter_id=e.encounter_id                         
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and   o.value_coded=1065 and
                                                      obsColheita.concept_id=23821 and obsColheita.voided=0   and                     
                                                      e.encounter_type=51 and obsColheita.value_datetime between (curdate() - interval 9 month) and curdate() and e.location_id=:location  


                                    ) gravida_real on gravida_real.patient_id=p.person_id                                
                                    left join                               
                                    (                              
                                          Select      p.patient_id,o.value_datetime data_parto                              
                                          from  patient p inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 and                               
                                                      e.encounter_type in (5,6) and o.value_datetime between (curdate() - interval 18 month) and curdate() and e.location_id=:location                                  
                                          union                               
                                          Select      p.patient_id, e.encounter_datetime data_parto                              
                                          from  patient p                               
                                                      inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and                               
                                                      e.encounter_type=6 and e.encounter_datetime between (curdate() - interval 18 month) and curdate() and e.location_id=:location                              
                                          union                              
                                          Select      p.patient_id, obsART.value_datetime data_parto                              
                                          from  patient p                               
                                                      inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                                      inner join obs obsART on e.encounter_id=obsART.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and                               
                                                      e.encounter_type=53 and e.location_id=:location and                               
                                                      obsART.value_datetime between (curdate() - interval 18 month) and curdate() and                               
                                                      obsART.concept_id=1190 and obsART.voided=0                              
                                          union                              
                                          Select      p.patient_id, e.encounter_datetime data_parto                              
                                          from  patient p                               
                                                      inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id                              
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332 and                               
                                                      e.encounter_type in (5,6) and e.encounter_datetime between (curdate() - interval 18 month) and curdate() and e.location_id=:location                              
                                          union                                     
                                          select      pg.patient_id,ps.start_date data_parto                              
                                          from  patient p                               
                                                      inner join patient_program pg on p.patient_id=pg.patient_id                              
                                                      inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                              
                                          where       pg.voided=0 and ps.voided=0 and p.voided=0 and                               
                                                      pg.program_id=8 and ps.state=27 and                               
                                                      ps.start_date between (curdate() - interval 18 month) and curdate() and location_id=:location    
                                          union
                                          
                                          Select      p.patient_id,e.encounter_datetime data_gravida                              
                                          from  patient p inner join encounter e on p.patient_id=e.patient_id                              
                                                      inner join obs o on e.encounter_id=o.encounter_id   
                                                      inner join obs obsColheita on   obsColheita.encounter_id=e.encounter_id                         
                                          where       p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and   o.value_coded=1065 and
                                                      obsColheita.concept_id=23821 and obsColheita.voided=0   and                     
                                                      e.encounter_type=51 and obsColheita.value_datetime between (curdate() - interval 9 month) and curdate() and e.location_id=:location                                       
                                                                              
                                    ) lactante_real on lactante_real.patient_id=p.person_id  and p.gender='F'                          
                              where lactante_real.data_parto is not null or gravida_real.data_gravida is not null  and p.voided=0 and p.gender='F'                         
                                            group by p.person_id                                                      
                        ) gravidaLactante                                     
                     ) estadoMulher on estadoMulher.patient_id=coorte12meses_final.patient_id                              
                         left join                              
                         (                              
                             SELECT       p.patient_id,'S' temTB                               
                              FROM  patient p                              
                                          INNER JOIN encounter e ON e.patient_id = p.patient_id                              
                                          INNER JOIN obs o ON o.encounter_id = e.encounter_id                              
                             WHERE  p.voided = 0 AND e.voided = 0 AND o.voided = 0                              
                                     AND e.encounter_type=6                              
                                     AND o.concept_id = 1268                              
                                     AND o.value_coded=1256                              
                                     AND e.location_id =:location AND o.obs_datetime between (curdate() - interval 7 month) and curdate()                               
                              UNION  

                             SELECT       p.patient_id,'S' temTB                               
                              FROM  patient p                              
                                          INNER JOIN encounter e ON e.patient_id = p.patient_id                              
                                          INNER JOIN obs o ON o.encounter_id = e.encounter_id                              
                             WHERE  p.voided = 0 AND e.voided = 0 AND o.voided = 0                              
                                     AND e.encounter_type IN (6,9)                              
                                     AND o.concept_id = 1113                              
                                     AND e.location_id =:location AND o.value_datetime between (curdate() - interval 7 month) and curdate()                               

                              UNION      

                              SELECT      p.patient_id,'S' temTB                               
                              FROM  patient p                              
                                          INNER JOIN encounter e ON e.patient_id = p.patient_id                              
                                          INNER JOIN obs o ON o.encounter_id = e.encounter_id                              
                             WHERE  p.voided = 0 AND e.voided = 0 AND o.voided = 0                              
                                     AND e.encounter_type=53                              
                                     AND o.concept_id = 42                              
                                     AND o.value_coded=1065                              
                                     AND e.location_id =:location AND o.obs_datetime between (curdate() - interval 7 month) and curdate()                               

                              UNION   

                                SELECT    p.patient_id,'S' temTB                               
                              FROM  patient p                              
                                          INNER JOIN patient_program pg ON pg.patient_id = p.patient_id                              
                             WHERE  p.voided = 0 AND pg.voided = 0                              
                                     AND pg.program_id = 5                               
                                     AND pg.date_enrolled BETWEEN (curdate() - interval 7 month) and curdate() AND pg.location_id =:location                             

                              UNION

                              SELECT      p.patient_id,'S' temTB                               
                              FROM  patient p                              
                                          INNER JOIN encounter e ON e.patient_id = p.patient_id                              
                                          INNER JOIN obs o ON o.encounter_id = e.encounter_id                              
                             WHERE  p.voided = 0 AND e.voided = 0 AND o.voided = 0                              
                                     AND e.encounter_type=6                              
                                     AND o.concept_id = 23761                              
                                     AND o.value_coded=1065                              
                                     AND e.location_id =:location AND e.encounter_datetime between (curdate() - interval 7 month) and curdate()                               

                         ) tb on tb.patient_id=coorte12meses_final.patient_id                              
                         left join                                                                                                                                                                                                                                                                                      
                         (                                                                                                                                                                                                                                                                                              
	                         select final.*, 
                                    obsAssinatura.value_datetime,
                                    if(final.dataConsentimento is not null and final.value_coded=1065 and obsAssinatura.value_datetime is not null,'S',
                                    if(final.dataConsentimento is not null and final.value_coded=1066 and obsAssinatura.value_datetime is not null,'N', null)) as consente
                             from 
                             (
	                         select  maxConsent.patient_id,maxConsent.dataConsentimento,o.value_coded, e.encounter_id
	                         from                                                                                                                                                                                                                                                                                     
	                         (                                                                                                                                                                                                                                                                                        
	                           select p.patient_id,max(e.encounter_datetime) dataConsentimento                                                                                                                                                                                          
	                           from  patient p                                                                                                                                                                                                                                                                  
	                               inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                  
	                               inner join obs o on o.encounter_id=e.encounter_id                                                                                                                                                                                                          
	                           where   p.voided=0 and e.voided=0 and o.voided=0 and                                                                                                                                                                                                           
	                               e.encounter_datetime<=curdate() and e.encounter_type=35 and e.location_id=:location and o.concept_id=6306                                                                                                                                            
	                           group by p.patient_id                                                                                                                                                                                                                                                            
	                         )maxConsent 
	                         left join encounter e on e.patient_id=maxConsent.patient_id
	                         left join obs o on o.encounter_id=e.encounter_id
	                         where e.voided=0 and o.voided=0 and e.encounter_type=35 and o.concept_id=6306 and o.value_coded in(1065,1066) and o.obs_datetime=maxConsent.dataConsentimento
	                         )final
	                         left join encounter e on e.patient_id=final.patient_id
	                         left join obs obsAssinatura on   obsAssinatura.encounter_id=e.encounter_id 
	                          where e.voided=0  and e.encounter_type=35 and e.location_id=:location and obsAssinatura.concept_id=23775 and obsAssinatura.voided=0 and e.encounter_datetime<=curdate()<=curdate()  and  e.encounter_datetime=final.dataConsentimento                                                                                                                                                                                                   
                         ) consentimentoPaciente on coorte12meses_final.patient_id = consentimentoPaciente.patient_id                                                                                                                                                               
                         left join                                                                                                                                                                                                                                                                                      
                         (                                                                                                                                                                                                                                                                                              
                             select final.*, 
                                    obsAssinatura.value_datetime,
                                    if(final.dataConsentimento is not null and final.value_coded=1065 and obsAssinatura.value_datetime is not null,'S',
                                    if(final.dataConsentimento is not null and final.value_coded=1066 and obsAssinatura.value_datetime is not null,'N', null)) as consente
                             from 
                             (
	                         select  maxConsent.patient_id,maxConsent.dataConsentimento,o.value_coded, e.encounter_id
	                         from                                                                                                                                                                                                                                                                                     
	                         (                                                                                                                                                                                                                                                                                        
	                           select p.patient_id,max(e.encounter_datetime) dataConsentimento                                                                                                                                                                                          
	                           from  patient p                                                                                                                                                                                                                                                                  
	                               inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                  
	                               inner join obs o on o.encounter_id=e.encounter_id                                                                                                                                                                                                          
	                           where   p.voided=0 and e.voided=0 and o.voided=0 and                                                                                                                                                                                                           
	                               e.encounter_datetime<=curdate() and e.encounter_type=35 and e.location_id=:location and o.concept_id=6177                                                                                                                                            
	                           group by p.patient_id                                                                                                                                                                                                                                                            
	                         )maxConsent 
	                         left join encounter e on e.patient_id=maxConsent.patient_id
	                         left join obs o on o.encounter_id=e.encounter_id
	                         where e.voided=0 and o.voided=0 and e.encounter_type=35 and o.concept_id=6177 and o.value_coded in(1065,1066) and o.obs_datetime=maxConsent.dataConsentimento
	                         )final
	                         left join encounter e on e.patient_id=final.patient_id
	                         left join obs obsAssinatura on   obsAssinatura.encounter_id=e.encounter_id 
	                          where e.voided=0  and e.encounter_type=35 and e.location_id=:location and obsAssinatura.concept_id=23776 and obsAssinatura.voided=0 and e.encounter_datetime<=curdate()<=curdate()  and  e.encounter_datetime=final.dataConsentimento
                         ) consentimentoConfidente on coorte12meses_final.patient_id = consentimentoConfidente.patient_id                                   
                         left join                               
                         (                              
                          select finalDispensa.patient_id, finalDispensa.data_clinica,                                                    
                                            case finalDispensa.tipoDispensa                                                  
                                                  when 1 then 'DM'
                                                  when 2 then 'DB'                                                  
                                                  when 3 then 'DT'                                                   
                                                  when 4 then 'DS'  
                                                  when 5 then 'DA'                                              
                                            else null end as tipoDispensa                                                                                                                                                                             
                                from                                                                                                                                                                                                      
                                (                                                                                                                                                                                                         
                                      select *                                                                                                                                                                                        
                                      from                                                                                                                                                                                                  
                                      (         


                                         select tiposDeDispensa.* from (
																			select patient_id, max(data_clinica) data_clinica                                                                                                                                                                                      
                                                from                                                                                                                                                                                                  
                                                (                                                                                                                                                                                                     
                                                                                                                                                                                                                                                                                                                                                       
                                                        select      maxFila.patient_id,                                                                                                                                                     
                                                                          maxFila.last_levantamento data_clinica,                                                                                                                     
                                                                          if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento)<53,1,
                                                                          if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento) BETWEEN 53 and 82,2,
                                                                          if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento) BETWEEN 83 and 173,3,
                                                                          if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento) BETWEEN 174 and 334,4,
                                                                           if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento)> 334,5,null
                                                                            ))))) tipoDispensa,              
                                                                          1 as fonte,                                                                                                                                                                   
                                                                          1 as ordemMDS                                                                                                                                                                 
                                                        from                                                                                                                                                                                            
                                                                  (                                                                                                                                                                                         
                                                                          SELECT p.patient_id, MAX(e.encounter_datetime) last_levantamento                                                                                      
                                                                          FROM  patient p                                                                                                                                                         
                                                                                            INNER JOIN encounter e ON e.patient_id = p.patient_id                                                                                           
                                                                          WHERE       p.voided = 0  AND e.voided = 0  AND e.encounter_type = 18  and                                                                      
                                                                                            e.location_id=:location and date(e.encounter_datetime) <=:endDate                                                                    
                                                                          GROUP BY p.patient_id                                                                                                                                                   
                                                                  ) maxFila                                                                                                                                                                           
                                                                  inner join encounter e on e.patient_id=maxFila.patient_id                                                                                                   
                                                                  inner join obs obsNext on e.encounter_id=obsNext.encounter_id                                                                                               
                                                        where       date(e.encounter_datetime)=date(maxFila.last_levantamento) and                                                                                  
                                                                          e.encounter_type=18 and e.location_id=:location and e.voided=0 and obsNext.voided=0 and                                             
                                                                          obsNext.concept_id=5096                                                                                                                                                 
                                                        group  by maxFila.patient_id                                                                                                                                              
                                                                                                                                                                                                                                                                                                                                                        
                                                        UNION                                                                                                                                                                                           
                                                                                                                                                                                                                                                                                                                                                        
                                                        select    lastTipo.patient_id,                                                                                                                                                    
                                                                  lastTipo.data_clinica,                                                                                                                                                        
                                                                  case obsTipo.value_coded                                                                                                                                                      
                                                                                    when 1098 then 1                                                                                                                                                  
                                                                                    when 23720 then 3                                                                                                                                                       
                                                                                    when 23888 then 4                                                                                                                                                 
                                                                  else null end as tipoDispensa,                                                                                                                                                
                                                                  2 as fonte,                                                                                                                                                                         
                                                                  1 as ordemMDS                                                                                                                                                                       
                                                        from                                                                                                                                                                                            
                                                        (                                                                                                                                                                                               
                                                                  Select      p.patient_id,max(e.encounter_datetime) data_clinica                                                                                             
                                                                  from  patient p                                                                                                                                                               
                                                                                    inner join encounter e on p.patient_id=e.patient_id                                                                                             
                                                                                    inner join obs o on o.encounter_id=e.encounter_id                                                                                                     
                                                                  where       e.voided=0 and o.voided=0 and p.voided=0 and                                                                                                          
                                                                                    e.encounter_type =6 and                                                                                                        
                                                                                    e.encounter_datetime<=:endDate and e.location_id=:location                                                                                  
                                                                  group by p.patient_id                                                                                                                                                         
                                                        ) lastTipo                                                                                                                                                                                
                                                        inner join encounter e on e.patient_id=lastTipo.patient_id                                                                                                        
                                                        inner join obs obsTipo on obsTipo.encounter_id=e.encounter_id                                                                                                     
                                                        where       lastTipo.data_clinica=e.encounter_datetime and                                                                                                              
                                                                          e.encounter_type=6 and e.voided=0 and obsTipo.voided=0 and                                                                                            
                                                                          e.location_id=:location and obsTipo.concept_id=23739                                                                                                  
                                                                                                                                                                                                                                                                                                                                                        
                                                        UNION                                                                                                                                                                                           
                                                                                                                                                                                                                                                                                                                                                
                                                        select      lastMDS.patient_id,                                                                                                                                                     
                                                                          lastMDS.data_clinica,                                                                                                                                                   
                                                                          case o.value_coded                                                                                                                                                            
                                                                                    when 23730  then 2                                                                                                    
                                                                                    when 23888  then 3                                                                                                    
                                                                                    when 165314 then 4
                                                                                    when 165340 then 5                                                                                             
                                                                          else null end as tipoDispensa,                                                                                                                                       
                                                                          3 as fonte,                                                                                                                                                                   
                                                                          case o.value_coded                                                                                                                                                            
                                                                                    when 23730 then 1                                                                                                                                                       
                                                                                    when 23888 then 1                                                                                                                                                       
                                                                                    when 165314 then 1 
                                                                                    when 165340 then 1                                                                                                                                                    
                                                                          else 2 end as ordemMDS                                                                                                                                                  
                                                        from                                                                                                                                                                                            
                                                        (                                                                                                                                                                                               
                                                                  Select      p.patient_id,max(e.encounter_datetime) data_clinica                                                                                             
                                                                  from  patient p                                                                                                                                                               
                                                                                    inner join encounter e on p.patient_id=e.patient_id                                                                                             
                                                                                    inner join obs o on o.encounter_id=e.encounter_id                                                                                                     
                                                                  where       e.voided=0 and o.voided=0 and p.voided=0 and                                                                                                          
                                                                                    e.encounter_type =6 and                                                                                                       
                                                                                    e.encounter_datetime<=:endDate and e.location_id=:location                                                                                  
                                                                  group by p.patient_id                                                                                                                                                         
                                                        ) lastMDS                                                                                                                                                                                 
                                                        inner join encounter e on lastMDS.patient_id=e.patient_id                                                                                                         
                                                        inner join obs grupo on grupo.encounter_id=e.encounter_id                                                                                                         
                                                        inner join obs o on o.encounter_id=e.encounter_id                                                                                                                       
                                                        inner join obs obsEstado on obsEstado.encounter_id=e.encounter_id                                                                                                 
                                                        where       e.encounter_type=6 and e.location_id=:location and                                                                                                    
                                                                          o.concept_id=165174 and o.voided=0                                                                                                                                
                                                                          and grupo.concept_id=165323  and grupo.voided=0                                                                                                             
                                                                          and obsEstado.concept_id=165322  and obsEstado.value_coded in(1256,1257)                                                                   
                                                                          and obsEstado.voided=0  and grupo.voided=0                                                                                                                  
                                                                          and grupo.obs_id=o.obs_group_id and grupo.obs_id=obsEstado.obs_group_id                                                                         
                                                                          and e.encounter_datetime=lastMDS.data_clinica                                                                                                               
                                                
                                      ) allTipoSource   
                                      group by patient_id
                                      ) maxTipoDispensa
                                      inner join (
                                                select *                                                                                                                                                                                        
                                                from                                                                                                                                                                                                  
                                                (                                                                                                                                                                                                     
                                                                                                                                                                                                                                                                                                                                                       
                                                        select      maxFila.patient_id,                                                                                                                                                     
                                                                          maxFila.last_levantamento data_clinica,                                                                                                                     
                                                                          if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento)<53,1,
                                                                          if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento) BETWEEN 53 and 82,2,
                                                                          if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento) BETWEEN 83 and 173,3,
                                                                          if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento) BETWEEN 174 and 334,4,
                                                                           if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento)> 334,5,10
                                                                            ))))) tipoDispensa,              
                                                                          1 as fonte,                                                                                                                                                                   
                                                                          1 as ordemMDS                                                                                                                                                                 
                                                        from                                                                                                                                                                                            
                                                                  (                                                                                                                                                                                         
                                                                          SELECT p.patient_id, MAX(e.encounter_datetime) last_levantamento                                                                                      
                                                                          FROM  patient p                                                                                                                                                         
                                                                                            INNER JOIN encounter e ON e.patient_id = p.patient_id                                                                                           
                                                                          WHERE       p.voided = 0  AND e.voided = 0  AND e.encounter_type = 18  and                                                                      
                                                                                            e.location_id=:location and date(e.encounter_datetime) <=:endDate                                                                    
                                                                          GROUP BY p.patient_id                                                                                                                                                   
                                                                  ) maxFila                                                                                                                                                                           
                                                                  inner join encounter e on e.patient_id=maxFila.patient_id                                                                                                   
                                                                  inner join obs obsNext on e.encounter_id=obsNext.encounter_id                                                                                               
                                                        where       date(e.encounter_datetime)=date(maxFila.last_levantamento) and                                                                                  
                                                                          e.encounter_type=18 and e.location_id=:location and e.voided=0 and obsNext.voided=0 and                                             
                                                                          obsNext.concept_id=5096                                                                                                                                                 
                                                        group  by maxFila.patient_id                                                                                                                                              
                                                                                                                                                                                                                                                                                                                                                        
                                                        UNION                                                                                                                                                                                           
                                                                                                                                                                                                                                                                                                                                                        
                                                        select    lastTipo.patient_id,                                                                                                                                                    
                                                                  lastTipo.data_clinica,                                                                                                                                                        
                                                                  case obsTipo.value_coded                                                                                                                                                      
                                                                                    when 1098 then 1                                                                                                                                                  
                                                                                    when 23720 then 3                                                                                                                                                       
                                                                                    when 23888 then 4                                                                                                                                                 
                                                                  else 10 end as tipoDispensa,                                                                                                                                                
                                                                  2 as fonte,                                                                                                                                                                         
                                                                  1 as ordemMDS                                                                                                                                                                       
                                                        from                                                                                                                                                                                            
                                                        (                                                                                                                                                                                               
                                                                  Select      p.patient_id,max(e.encounter_datetime) data_clinica                                                                                             
                                                                  from  patient p                                                                                                                                                               
                                                                                    inner join encounter e on p.patient_id=e.patient_id                                                                                             
                                                                                    inner join obs o on o.encounter_id=e.encounter_id                                                                                                     
                                                                  where       e.voided=0 and o.voided=0 and p.voided=0 and                                                                                                          
                                                                                    e.encounter_type =6 and                                                                                                        
                                                                                    e.encounter_datetime<=:endDate and e.location_id=:location                                                                                  
                                                                  group by p.patient_id                                                                                                                                                         
                                                        ) lastTipo                                                                                                                                                                                
                                                        inner join encounter e on e.patient_id=lastTipo.patient_id                                                                                                        
                                                        inner join obs obsTipo on obsTipo.encounter_id=e.encounter_id                                                                                                     
                                                        where       lastTipo.data_clinica=e.encounter_datetime and                                                                                                              
                                                                          e.encounter_type=6 and e.voided=0 and obsTipo.voided=0 and                                                                                            
                                                                          e.location_id=:location and obsTipo.concept_id=23739                                                                                                  
                                                                                                                                                                                                                                                                                                                                                        
                                                        UNION                                                                                                                                                                                           
                                                                                                                                                                                                                                                                                                                                                
                                                        select      lastMDS.patient_id,                                                                                                                                                     
                                                                          lastMDS.data_clinica,                                                                                                                                                   
                                                                          case o.value_coded                                                                                                                                                            
                                                                                    when 23730  then 2                                                                                                    
                                                                                    when 23888  then 3                                                                                                    
                                                                                    when 165314 then 4
                                                                                    when 165340 then 5                                                                                             
                                                                          else 10 end as tipoDispensa,                                                                                                                                       
                                                                          3 as fonte,                                                                                                                                                                   
                                                                          case o.value_coded                                                                                                                                                            
                                                                                    when 23730 then 1                                                                                                                                                       
                                                                                    when 23888 then 1                                                                                                                                                       
                                                                                    when 165314 then 1 
                                                                                    when 165340 then 1                                                                                                                                                    
                                                                          else 10 end as ordemMDS                                                                                                                                                  
                                                        from                                                                                                                                                                                            
                                                        (                                                                                                                                                                                               
                                                                  Select      p.patient_id,max(e.encounter_datetime) data_clinica                                                                                             
                                                                  from  patient p                                                                                                                                                               
                                                                                    inner join encounter e on p.patient_id=e.patient_id                                                                                             
                                                                                    inner join obs o on o.encounter_id=e.encounter_id                                                                                                     
                                                                  where       e.voided=0 and o.voided=0 and p.voided=0 and                                                                                                          
                                                                                    e.encounter_type =6 and                                                                                                       
                                                                                    e.encounter_datetime<=:endDate and e.location_id=:location                                                                                  
                                                                  group by p.patient_id                                                                                                                                                         
                                                        ) lastMDS                                                                                                                                                                                 
                                                        inner join encounter e on lastMDS.patient_id=e.patient_id                                                                                                         
                                                        inner join obs grupo on grupo.encounter_id=e.encounter_id                                                                                                         
                                                        inner join obs o on o.encounter_id=e.encounter_id                                                                                                                       
                                                        inner join obs obsEstado on obsEstado.encounter_id=e.encounter_id                                                                                                 
                                                        where       e.encounter_type=6 and e.location_id=:location and                                                                                                    
                                                                          o.concept_id=165174 and o.voided=0                                                                                                                                
                                                                          and grupo.concept_id=165323  and grupo.voided=0                                                                                                             
                                                                          and obsEstado.concept_id=165322  and obsEstado.value_coded in(1256,1257)                                                                   
                                                                          and obsEstado.voided=0  and grupo.voided=0                                                                                                                  
                                                                          and grupo.obs_id=o.obs_group_id and grupo.obs_id=obsEstado.obs_group_id                                                                         
                                                                          and e.encounter_datetime=lastMDS.data_clinica                                                                                                               
                                                
                                      ) allTipoSource                                                                                                                                                                           
                                                ) tiposDeDispensa on maxTipoDispensa.patient_id = tiposDeDispensa.patient_id
                                      where maxTipoDispensa.data_clinica = tiposDeDispensa.data_clinica
                                      order by patient_id,data_clinica desc,fonte asc
                                      ) allTipoSourcefirst                                                                                                                                                                            
                                      group by patient_id                                                                                                                                                                             
                                )finalDispensa
                                                                                                                                                                                                             
                         ) dispensaTipo on dispensaTipo.patient_id=coorte12meses_final.patient_id                              
                         left join                               
                         (                              
                     select max_consulta.patient_id,max_consulta.data_seguimento,obs_seguimento.value_datetime 
                            from  
                            (   Select  p.patient_id,max(encounter_datetime) data_seguimento 
                                from    patient p  
                                        inner join encounter e on e.patient_id=p.patient_id 
                                where   p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  
                                        e.location_id=:location and e.encounter_datetime<=:endDate 
                                group by p.patient_id 
                            ) max_consulta  
                            left join 
                                obs obs_seguimento on obs_seguimento.person_id=max_consulta.patient_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=max_consulta.data_seguimento 
                                and obs_seguimento.concept_id=1410 
                                and obs_seguimento.location_id=:location                      
                                                       
                         ) consultaClinica on consultaClinica.patient_id=coorte12meses_final.patient_id   

                         left join

                         (

                       select max_consultaApss.patient_id,max_consultaApss.data_seguimento,obs_seguimento.value_datetime 
                            from  
                            (   Select  p.patient_id,max(encounter_datetime) data_seguimento 
                                from    patient p  
                                        inner join encounter e on e.patient_id=p.patient_id 
                                where   p.voided=0 and e.voided=0 and e.encounter_type in (35) and  
                                        e.location_id=:location and e.encounter_datetime<=:endDate 
                                group by p.patient_id 
                            ) max_consultaApss  
                            left join 
                                obs obs_seguimento on obs_seguimento.person_id=max_consultaApss.patient_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=max_consultaApss.data_seguimento 
                                and obs_seguimento.concept_id=6310 
                                and obs_seguimento.location_id=:location                      
                         )max_consultaApss on   max_consultaApss.patient_id= coorte12meses_final.patient_id   


                         left join
                         (
                           select menthorMotherFC.patient_id,menthorMotherFC.data_seguimento, menthorMotherFC.encounter_id,obs_seguimento.concept_id,
                            case obs_seguimento.value_coded                                                 
                                when 1256 then 'INICIO'                                                  
                                when 1257 then 'CONTINUA'                                                   
                                when 1267 then 'FIM'  
                            else null end as menthorMother  
                            from  
                            (   Select  p.patient_id,max(encounter_datetime) data_seguimento, e.encounter_id 
                                from    patient p  
                                        inner join encounter e on e.patient_id=p.patient_id 
                                where   p.voided=0 and e.voided=0 and e.encounter_type=6 and  
                                        e.location_id=:location and e.encounter_datetime<=:endDate and e.voided=0
                               group by p.patient_id 
                            ) menthorMotherFC 
                               left join 
                                encounter e on e.patient_id=menthorMotherFC.patient_id
                                join obs obs_seguimento on obs_seguimento.encounter_id=e.encounter_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=menthorMotherFC.data_seguimento 
                                and obs_seguimento.concept_id=24031 
                                and obs_seguimento.location_id=:location 
                                and e.encounter_type=6
                                and e.voided=0
                         ) menthorMotherFC on  menthorMotherFC.patient_id= coorte12meses_final.patient_id    

                        left join

                        (

                         select teenagerAndYoungFC.patient_id,teenagerAndYoungFC.data_seguimento, 
                            case obs_seguimento.value_coded                                                 
                                when 1256 then 'INICIO'                                                  
                                when 1257 then 'CONTINUA'                                                   
                                when 1267 then 'FIM'  
                            else null end as teenagerAndYoung  
                            from  
                            (   Select  p.patient_id,max(encounter_datetime) data_seguimento 
                                from    patient p  
                                        inner join encounter e on e.patient_id=p.patient_id 
                                where   p.voided=0 and e.voided=0 and e.encounter_type in (6) and  
                                        e.location_id=:location and e.encounter_datetime<=:endDate 
                                group by p.patient_id 
                            ) teenagerAndYoungFC 
                            left join 
                                obs obs_seguimento on obs_seguimento.person_id=teenagerAndYoungFC.patient_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=teenagerAndYoungFC.data_seguimento 
                                and obs_seguimento.concept_id=165324 
                                and obs_seguimento.location_id=:location  

                        )  teenagerAndYoungFC on teenagerAndYoungFC.patient_id=coorte12meses_final.patient_id

                        left join

                        (

                        select championManFC.patient_id,championManFC.data_seguimento, 
                            case obs_seguimento.value_coded                                                 
                                when 1256 then 'INICIO'                                                  
                                when 1257 then 'CONTINUA'                                                   
                                when 1267 then 'FIM'  
                            else null end as championMan  
                            from  
                            (   Select  p.patient_id,max(encounter_datetime) data_seguimento 
                                from    patient p  
                                        inner join encounter e on e.patient_id=p.patient_id 
                                where   p.voided=0 and e.voided=0 and e.encounter_type in (6) and  
                                        e.location_id=:location and e.encounter_datetime<=:endDate
                                group by p.patient_id 
                            ) championManFC 
                            left join 
                                obs obs_seguimento on obs_seguimento.person_id=championManFC.patient_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=championManFC.data_seguimento 
                                and obs_seguimento.concept_id=165325 
                                and obs_seguimento.location_id=:location  
                        )championManFC on championManFC.patient_id=coorte12meses_final.patient_id

                        left join
                         (
                         select menthorMotherAPSS.patient_id,menthorMotherAPSS.data_seguimento, menthorMotherAPSS.encounter_id,obs_seguimento.concept_id,
                            case obs_seguimento.value_coded                                                 
                                when 1256 then 'INICIO'                                                  
                                when 1257 then 'CONTINUA'                                                   
                                when 1267 then 'FIM'  
                            else null end as menthorMotherApss  
                            from  
                            (   Select  p.patient_id,max(encounter_datetime) data_seguimento, e.encounter_id 
                                from    patient p  
                                        inner join encounter e on e.patient_id=p.patient_id 
                                where   p.voided=0 and e.voided=0 and e.encounter_type in (35) and  
                                        e.location_id=:location and e.encounter_datetime<=:endDate and e.voided=0
                               group by p.patient_id 
                            ) menthorMotherAPSS 
                               left join 
                                encounter e on e.patient_id=menthorMotherAPSS.patient_id
                                join obs obs_seguimento on obs_seguimento.encounter_id=e.encounter_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=menthorMotherAPSS.data_seguimento 
                                and obs_seguimento.concept_id=24031 
                                and obs_seguimento.location_id=:location 
                                and e.encounter_type=35
                                and e.voided=0

                         ) menthorMotherAPSS on  menthorMotherAPSS.patient_id= coorte12meses_final.patient_id    

                        left join

                        (

                         select teenagerAndYoungAPSS.patient_id,teenagerAndYoungAPSS.data_seguimento, 
                            case obs_seguimento.value_coded                                                 
                                when 1256 then 'INICIO'                                                  
                                when 1257 then 'CONTINUA'                                                   
                                when 1267 then 'FIM'  
                            else null end as teenagerAndYoungApss  
                            from  
                            (   Select  p.patient_id,max(encounter_datetime) data_seguimento 
                                from    patient p  
                                        inner join encounter e on e.patient_id=p.patient_id 
                                where   p.voided=0 and e.voided=0 and e.encounter_type in (35) and  
                                        e.location_id=:location and e.encounter_datetime<=:endDate 
                                group by p.patient_id 
                            ) teenagerAndYoungAPSS 
                            left join 
                                obs obs_seguimento on obs_seguimento.person_id=teenagerAndYoungAPSS.patient_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=teenagerAndYoungAPSS.data_seguimento 
                                and obs_seguimento.concept_id=165324 
                                and obs_seguimento.location_id=:location  

                        )  teenagerAndYoungAPSS on teenagerAndYoungAPSS.patient_id=coorte12meses_final.patient_id

                        left join

                        (

                        select championManAPSS.patient_id,championManAPSS.data_seguimento, 
                            case obs_seguimento.value_coded                                                 
                                when 1256 then 'INICIO'                                                  
                                when 1257 then 'CONTINUA'                                                   
                                when 1267 then 'FIM'  
                            else null end as championManApss  
                            from  
                            (   Select  p.patient_id,max(encounter_datetime) data_seguimento 
                                from    patient p  
                                        inner join encounter e on e.patient_id=p.patient_id 
                                where   p.voided=0 and e.voided=0 and e.encounter_type in (35) and  
                                        e.location_id=:location and e.encounter_datetime<=:endDate
                                group by p.patient_id 
                            ) championManAPSS 
                            left join 
                                obs obs_seguimento on obs_seguimento.person_id=championManAPSS.patient_id 
                                and obs_seguimento.voided=0 
                                and obs_seguimento.obs_datetime=championManAPSS.data_seguimento 
                                and obs_seguimento.concept_id=165325 
                                and obs_seguimento.location_id=:location  
                        )championManAPSS on championManAPSS.patient_id=coorte12meses_final.patient_id
                        left join
                        (
                                                    select     
                            f.patient_id,f.encounter_datetime as encounter_datetime,
                            @num_mdc := 1 + LENGTH(f.MDC) - LENGTH(REPLACE(f.MDC, ',', '')) AS MDC,  
                            SUBSTRING_INDEX(f.MDC, ',', 1) AS MDC1,  
                            IF(@num_mdc > 1, SUBSTRING_INDEX(SUBSTRING_INDEX(f.MDC, ',', 2), ',', -1), '') AS MDC2,  
                            IF(@num_mdc > 2, SUBSTRING_INDEX(SUBSTRING_INDEX(f.MDC, ',', 3), ',', -1), '') AS MDC3,  
                            IF(@num_mdc > 3, SUBSTRING_INDEX(SUBSTRING_INDEX(f.MDC, ',', 4), ',', -1), '') AS MDC4, 
                            IF(@num_mdc > 4, SUBSTRING_INDEX(SUBSTRING_INDEX(f.MDC, ',', 5), ',', -1), '') AS MDC5
                         from (
                         select final.patient_id,final.encounter_datetime,group_concat(final.MDC) as MDC  from
                         (
                         select final.patient_id,final.encounter_datetime encounter_datetime,o.value_coded,final.obs_group_id,
                                  case o.value_coded
				              when  165340 then 'DB'
				              when  23730 then 'DT' 
				              when  165314 then 'DA' 
				              when  23888 then 'DS' 
				              when  23724 then 'GA'
				              when  23726 then 'CA'
				              when  165317 then 'TB' 
				              when  165320 then 'SMI' 
				              when  165321 then 'DAH' 
				              when  165178 then 'DCP' 
				              when  165179 then 'DCA' 
				              when  165318 then 'CT' 
				              when  165315 then 'DD' 
				              when  165264 then 'BM' 
				              when  165265 then 'CM'
				              when  23725  then 'AF'
				              when  23729  then 'FR'
				              when  165316 then 'EH' 
				              when  165319 then 'SAAJ'
				              when  23727  then 'PU'
				              when  165177 then 'FARMAC/Farmácia Privada'
				              when  23732  then 'OUTRO'
				              end AS MDC
                         
                         from
                         (   
                           select f.patient_id,max(date(f.encounter_datetime)) as encounter_datetime,encounter_id,f.obs_group_id  from 
                            (
				             select  e.patient_id,e.encounter_datetime encounter_datetime,e.encounter_id,o.value_coded,obsEstado.obs_group_id
				   
				              from patient p 
				              join encounter e on p.patient_id=e.patient_id 
				              join obs grupo on grupo.encounter_id=e.encounter_id 
				              join obs o on o.encounter_id=e.encounter_id 
				              join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
				              where  e.encounter_type in(6) 
				              and e.location_id =:location 
				              and o.concept_id=165174  
				              and o.voided=0 
				              and grupo.concept_id=165323  
				              and grupo.voided=0 
				              and obsEstado.concept_id=165322  
				              and obsEstado.value_coded in(1256,1257) 
				              and obsEstado.voided=0  
				              and grupo.voided=0 
				              and grupo.obs_id=o.obs_group_id 
				              and grupo.obs_id=obsEstado.obs_group_id 
				              order by p.patient_id, date(e.encounter_datetime) desc ,obsEstado.obs_group_id  ASC
                                )f
                                group by f.patient_id
                                )final
                                  join encounter e on final.patient_id=e.patient_id 
				              join obs grupo on grupo.encounter_id=e.encounter_id 
				              join obs o on o.encounter_id=e.encounter_id 
				              join obs obsEstado on obsEstado.encounter_id=e.encounter_id 
				              where  e.encounter_type in(6) 
				              and e.location_id =:location 
				              and o.concept_id=165174  
				              and o.voided=0 
				              and grupo.concept_id=165323  
				              and grupo.voided=0 
				              and obsEstado.concept_id=165322  
				              and obsEstado.value_coded in(1256,1257) 
				              and obsEstado.voided=0  
				              and grupo.voided=0 
				              and grupo.obs_id=o.obs_group_id 
				              and grupo.obs_id=obsEstado.obs_group_id 
				              and date(final.encounter_datetime)=date(e.encounter_datetime)
                                )final
                                group by  final.patient_id
                                )f 
                                group by f.patient_id order by @num_mdc desc                               
                          ) MDC on MDC.patient_id=coorte12meses_final.patient_id

                          left join
                          (
                           select p.person_id as patient_id,keyPop.fcWithKeyPopDate,keyPop.keyPopHSH,keyPop.keyPopPID,keyPop.keyPopREC,keyPop.keyPopTS
		                      from person p 
		                      left join 
		                      (                     
		                      select * from
		                      ( 
		                        select final.patient_id,
		                               final.value_coded,
		                               IF(ISNULL(final.obs_datetime), 'N/A', DATE_FORMAT(final.obs_datetime, '%d-%m-%Y')) AS fcWithKeyPopDate,
							           IF(ISNULL(final.value_coded) OR final.value_coded<>1377, 'N', 'S') AS keyPopHSH,
							           IF(ISNULL(final.value_coded) OR final.value_coded<>20454, 'N', 'S') AS keyPopPID,
							           IF(ISNULL(final.value_coded) OR final.value_coded<>20426, 'N', 'S') AS keyPopREC,
							           IF(ISNULL(final.value_coded) OR final.value_coded<>1901, 'N', 'S') AS keyPopTS 
		                        from
		                        
		                        (
		                        select maxkp.patient_id, o.value_coded as value_coded,o.obs_datetime from 
		                        	   (                                                            
		                            select p.patient_id,max(e.encounter_datetime) maxkpdate from patient p                                                                      
		                                inner join encounter e on p.patient_id=e.patient_id                                                                                     
		                                inner join obs o on e.encounter_id=o.encounter_id                                                                                       
		                            where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=23703 and  e.encounter_type=6 and e.encounter_datetime<=CURDATE() and     
		                                  e.location_id=:location                                                                                                          
		                                group by p.patient_id                                                                                                               
		                            )                                                                                                                                       
		                        maxkp                                                                                                                                       
		                            left join encounter e on e.patient_id=maxkp.patient_id and maxkp.maxkpdate=e.encounter_datetime                                            
		                            left join obs o on o.encounter_id=e.encounter_id and maxkp.maxkpdate=o.obs_datetime                                                        
		                            inner join person pe on pe.person_id=maxkp.patient_id                                                                                       
		                        where o.concept_id=23703 and o.voided=0 and e.encounter_type=6 and e.voided=0 and e.location_id =:location and pe.voided=0                   
		                            and o.value_coded in (1377, 20454,20426,1901)
		                          )final
		                          )final
		                          )keyPop on keyPop.patient_id=p.person_id and p.voided=0
                          ) keyPop on keyPop.patient_id=coorte12meses_final.patient_id
                          left join
                          (
                          select abandonoNotificado.patient_id,abandonoNotificado.data_estado_abandono from 
                                                ( 
	                                             select pg.patient_id,max(ps.start_date) data_estado_abandono 
	                                             from patient p 
	                                                 inner join patient_program pg on p.patient_id=pg.patient_id 
	                                                 inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
	                                             where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.start_date<=CURDATE() and pg.location_id=:location 
	                                             and ps.state=9
	                                             group by p.patient_id 
                                                     union 
                                                select p.patient_id, max(o.obs_datetime) data_estado_abandono 
                                                 from patient p 
                                                     inner join encounter e on p.patient_id=e.patient_id 
                                                     inner join obs  o on e.encounter_id=o.encounter_id 
                                                 where e.voided=0 and o.voided=0 and p.voided=0 and  e.encounter_type in (53,6) and o.concept_id in (6272,6273) 
                                                     and o.value_coded=1707 and o.obs_datetime<=CURDATE() and e.location_id=:location 
                                                     group by p.patient_id 
                                                  ) abandonoNotificado 
                          )abandonoNotificado on abandonoNotificado.patient_id=coorte12meses_final.patient_id
                          left join person_attribute patOVCEntryDate on patOVCEntryDate.person_id=coorte12meses_final.patient_id and patOVCEntryDate.person_attribute_type_id=50 and patOVCEntryDate.value is not null and patOVCEntryDate.value <> '' and patOVCEntryDate.voided=0 
			           left join person_attribute patOVCExitDate on patOVCExitDate.person_id=coorte12meses_final.patient_id and patOVCExitDate.person_attribute_type_id=51 and patOVCExitDate.value is not null and patOVCExitDate.value <> '' and patOVCExitDate.voided=0 
			           left join person_attribute patOVCStatus on patOVCStatus.person_id=coorte12meses_final.patient_id and patOVCStatus.person_attribute_type_id=52 and patOVCStatus.value is not null and patOVCStatus.value <> '' and patOVCStatus.voided=0 
                         where (data_estado is null or (data_estado is not null and  data_fila > data_estado)) and    datediff(:endDate,data_usar)>=3 and datediff(:endDate,data_usar) between :minDelayDays AND :maxDelayDays
                         group by coorte12meses_final.patient_id                                                                          