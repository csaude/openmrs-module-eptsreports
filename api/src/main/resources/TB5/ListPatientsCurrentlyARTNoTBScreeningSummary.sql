select             coorte12meses_final.patient_id
              from                                                                                                      
              (
                select inicio_fila_seg_prox.*,                                         
                  GREATEST(COALESCE(data_fila,data_seguimento,data_recepcao_levantou),COALESCE(data_seguimento,data_fila,data_recepcao_levantou),COALESCE(data_recepcao_levantou,data_seguimento,data_fila))  data_usar_c,      
                  GREATEST(COALESCE(data_proximo_lev,data_proximo_seguimento,data_recepcao_levantou30),COALESCE(data_proximo_seguimento,data_proximo_lev,data_recepcao_levantou30),COALESCE(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) data_usar
              from                                                            
                (
                select inicio_fila_seg.*,                                              
                max(obs_fila.value_datetime) data_proximo_lev,                                      
                max(obs_seguimento.value_datetime) data_proximo_seguimento,                               
                 date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30,
                 case obs_dispensa.value_coded 
                 when 165175 then 'HORARIO NORMAL DE EXPEDINTE'
                 when 165176 then 'FORA DO HORÁRIO'
                 when 165177 then 'FARMAC/FARMÁCIA PRIVADA'
                 when 165178 then 'DISPENSA COMUNITÁRIA VIA PROVEDOR'
                 when 165179 then 'DISPENSA COMUNITARIA VIA APE'  
                 when 165180 then 'BRIGADAS MÓVEIS DIURNAS'
                 when 165181 then 'BRIGADAS MOVEIS NOTURNAS(HOTSPOTS)'
                 when 165182 then 'CLINICAS MOVEIS DIURNAS'  
                 when 165183 then 'CLINICAS MOVEIS NOTURNAS(HOTSPOTS)'
                 else null end as type_dispensation,
                 case obs_seguimento_dispensa.value_coded 
                 when 1098  then 'DM'
                 when 23720 then 'DT'
                 when 23888 then 'DS'
                else null end as type_dispensation_seguimento

              from                                                              
            (
              select inicio.*,                                                         
                saida.data_estado,                                                      
                max_fila.data_fila,                                                     
                max_consulta.data_seguimento,                                               
                max_recepcao.data_recepcao_levantou                                             
             from                                                               
             (  Select patient_id,min(data_inicio) data_inicio                                        
                from                                                            
                  (                                                           
                    Select  p.patient_id,min(e.encounter_datetime) data_inicio                              
                    from  patient p                                                   
                        inner join encounter e on p.patient_id=e.patient_id                             
                        inner join obs o on o.encounter_id=e.encounter_id                             
                    where   e.voided=0 and o.voided=0 and p.voided=0 and                                
                        e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and                 
                        e.encounter_datetime<=:endDate and e.location_id= :location                            
                    group by p.patient_id                                               
                    union                                                       
                    Select  p.patient_id,min(value_datetime) data_inicio                                
                    from  patient p                                                 
                        inner join encounter e on p.patient_id=e.patient_id                             
                        inner join obs o on e.encounter_id=o.encounter_id                             
                    where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and              
                        o.concept_id=1190 and o.value_datetime is not null and                            
                        o.value_datetime<=:endDate and e.location_id= :location                              
                    group by p.patient_id                                               
                    union                                                       
                    select  pg.patient_id,min(date_enrolled) data_inicio                                
                    from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
                    where   pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id= :location       
                    group by pg.patient_id                                                
                    union                                                       

                    SELECT  e.patient_id, MIN(e.encounter_datetime) AS data_inicio                          
                      FROM    patient p                                               
                          inner join encounter e on p.patient_id=e.patient_id                           
                      WHERE   p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id= :location    
                      GROUP BY  p.patient_id                                                    
                    union                                                             

                    Select  p.patient_id,min(value_datetime) data_inicio                                      
                    from  patient p                                                       
                        inner join encounter e on p.patient_id=e.patient_id                                   
                        inner join obs o on e.encounter_id=o.encounter_id                                   
                    where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and                          
                        o.concept_id=23866 and o.value_datetime is not null and                                 
                        o.value_datetime<=:endDate and e.location_id= :location                                    
                    group by p.patient_id                                                     
                  ) inicio_real                                                           
                group by patient_id                                                           
              )inicio                                                                 

              left join                                                                 
              (                                                                     
              select patient_id,max(data_estado) data_estado                                                
              from                                                                    
                (                                                                   

                select distinct max_estado.patient_id, max_estado.data_estado from (                                                      
                    select  pg.patient_id,                                                      
                        max(ps.start_date) data_estado                                              
                    from  patient p                                                       
                      inner join patient_program pg on p.patient_id = pg.patient_id                               
                      inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                        
                    where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2                                          
                      and ps.start_date<= :endDate and pg.location_id = :location   group by pg.patient_id                                              
                  ) max_estado                                                                                                                        
                    inner join patient_program pp on pp.patient_id = max_estado.patient_id                              
                    inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
                  where pp.program_id = 2 and ps.state in (7,8,10) and pp.voided = 0 and ps.voided = 0 and pp.location_id= :location           
                  union                                                               

                  select  p.patient_id,                                                       
                      max(o.obs_datetime) data_estado                                               
                  from  patient p                                                         
                      inner join encounter e on p.patient_id=e.patient_id                                     
                      inner join obs  o on e.encounter_id=o.encounter_id                                      
                  where   e.voided=0 and o.voided=0 and p.voided=0 and                                        
                      e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and              
                      o.obs_datetime<=:endDate and e.location_id= :location                                      
                  group by p.patient_id                                                       
                  union                                                               

                  select person_id as patient_id,death_date as data_estado                                      
                  from person                                                             
                  where dead=1 and death_date is not null and death_date<=:endDate                                 
                  union                                                               

                  select  p.patient_id,                                                       
                      max(obsObito.obs_datetime) data_estado                                            
                  from  patient p                                                         
                      inner join encounter e on p.patient_id=e.patient_id                                     
                      inner join obs obsObito on e.encounter_id=obsObito.encounter_id                               
                  where   e.voided=0 and p.voided=0 and obsObito.voided=0 and                                     
                      e.encounter_type in (21,36,37) and  e.encounter_datetime<=:endDate and  e.location_id= :location   and             
                      obsObito.concept_id in (2031,23944,23945) and obsObito.value_coded=1366                           
                  group by p.patient_id                                                       
                  union                                                               

                  select  p.patient_id,max(e.encounter_datetime) data_estado                                      
                  from  patient p                                                         
                      inner join encounter e on p.patient_id=e.patient_id                                     
                      inner join obs o on o.encounter_id=e.encounter_id                                     
                  where   e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and                              
                      o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and e.encounter_type in (21,36,37) and  e.location_id= :location   
                  group by p.patient_id                                                           
                                                                                        
                ) allSaida                                                                    
              group by patient_id                                                                 
              ) saida on inicio.patient_id=saida.patient_id                                                   

              left join                                                                      
              ( Select p.patient_id,max(encounter_datetime) data_fila                                               
              from  patient p                                                                 
                  inner join encounter e on e.patient_id=p.patient_id                                             
              where   p.voided=0 and e.voided=0 and e.encounter_type=18 and                                           
                  e.location_id= :location   and e.encounter_datetime<=:endDate                                         
              group by p.patient_id                                                               
             ) max_fila on inicio.patient_id=max_fila.patient_id                                                  

             left join                                                                     
              ( Select  p.patient_id,max(encounter_datetime) data_seguimento                                          
              from  patient p                                                                 
                  inner join encounter e on e.patient_id=p.patient_id                                             
              where   p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and                                         
                  e.location_id= :location   and e.encounter_datetime<=:endDate                                         
              group by p.patient_id                                                               
             ) max_consulta on inicio.patient_id=max_consulta.patient_id                                              

             left join                                                                     
              (                                                                         
              Select  p.patient_id,max(value_datetime) data_recepcao_levantou                                           
              from  patient p                                                                 
                  inner join encounter e on p.patient_id=e.patient_id                                             
                  inner join obs o on e.encounter_id=o.encounter_id                                             
              where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and                                    
                  o.concept_id=23866 and o.value_datetime is not null and                                           
                  o.value_datetime<=:endDate and e.location_id= :location                                              
              group by p.patient_id                                                               
              ) max_recepcao on inicio.patient_id=max_recepcao.patient_id                                             
              group by inicio.patient_id                                                              
             ) inicio_fila_seg                                                                  

             left join                                                                     
               obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id                                            
               and obs_fila.voided=0                                                                
               and obs_fila.obs_datetime=inicio_fila_seg.data_fila                                                
               and obs_fila.concept_id=5096                                                           
               and obs_fila.location_id= :location   
              
               left join                                                                     
               obs obs_dispensa on obs_dispensa.person_id=inicio_fila_seg.patient_id                                            
               and obs_dispensa.voided=0                                                                
               and obs_dispensa.obs_datetime=inicio_fila_seg.data_fila                                                
               and obs_dispensa.concept_id=165174                                                           
               and obs_dispensa.location_id= :location   


               left join                                                                     
              obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id                                     
              and obs_seguimento.voided=0                                                             
              and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento                                           
              and obs_seguimento.concept_id=1410                                                          
              and obs_seguimento.location_id= :location  

              left join                                                                     
              obs obs_seguimento_dispensa on obs_seguimento_dispensa.person_id=inicio_fila_seg.patient_id                                     
              and obs_seguimento_dispensa.voided=0                                                             
              and obs_seguimento_dispensa.obs_datetime=inicio_fila_seg.data_seguimento                                           
              and obs_seguimento_dispensa.concept_id=23739                                                          
              and obs_seguimento_dispensa.location_id= :location   
              group by inicio_fila_seg.patient_id  
              
             ) inicio_fila_seg_prox                                                               
             group by patient_id                                                                  
             ) coorte12meses_final

            inner join person p on p.person_id=coorte12meses_final.patient_id          
            left join   ( 
            select pad1.*  from person_address pad1  
            inner join   (  
            select person_id,min(person_address_id) id   from person_address  
            where voided=0  
            group by person_id  
            ) pad2  
            where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
            ) pad3 on pad3.person_id=coorte12meses_final.patient_id 
            left join( 
            select pn1.*  from person_name pn1  
            inner join (  
            select person_id,min(person_name_id) id   from person_name  
            where voided=0  
            group by person_id  
            ) pn2  
            where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
            ) pn on pn.person_id=coorte12meses_final.patient_id 
            left join  ( 
            select pid1.*  from patient_identifier pid1  
            inner join  (  
            select patient_id,min(patient_identifier_id) id  from patient_identifier  
            where voided=0  
            group by patient_id  
            ) pid2 
            where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
            ) pid on pid.patient_id=coorte12meses_final.patient_id

            left join person_attribute pat on pat.person_id=coorte12meses_final.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0


            left join
                          (
                           select distinct preg_or_lac.patient_id, preg_or_lac.data_consulta,if(preg_or_lac.orderF=1,'Grávida','Lactante') as PREG_LAC from 
                           (
                          select final.patient_id,final.data_consulta,final.orderF 

                          from 
                          (
                           select p.patient_id,max(e.encounter_datetime) data_consulta, 1 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           where e.encounter_type in(5,6) and o.concept_id=1982 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 
                           and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           group by p.patient_id

                           union

                           select p.patient_id,max(e.encounter_datetime) data_consulta,1 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           where e.encounter_type in(5,6) and o.concept_id=1279 and p.voided=0 and e.voided=0 and o.voided=0 
                           and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           group by p.patient_id

                           union

                           select p.patient_id,max(o.value_datetime) data_consulta,1 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           where e.encounter_type in(5,6) and o.concept_id=1600 and p.voided=0 and e.voided=0 and o.voided=0 
                           and o.value_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           group by p.patient_id

                           union

                           select p.patient_id,max(e.encounter_datetime) data_consulta,1 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           where e.encounter_type in(6) and o.concept_id=6334 and o.value_coded=6331 and p.voided=0 and e.voided=0 and o.voided=0 
                           and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           group by p.patient_id

                           union

                           SELECT p.patient_id,pg.date_enrolled data_consulta,1 orderF FROM patient p
                           INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
                           INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
                           WHERE pg.program_id=8 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
                           and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE() 
                           GROUP BY p.patient_id

                           union

                           select p.patient_id,max(e.encounter_datetime) data_consulta,1 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           inner join obs obsInicio on obsInicio.encounter_id=e.encounter_id
                           where e.encounter_type in(53) and o.concept_id=1982 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 
                           and obsInicio.voided=0 and obsInicio.concept_id=1190
                           and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           and obsInicio.value_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           group by p.patient_id

                           union


                           select p.patient_id,max(e.encounter_datetime) data_consulta,1 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           where e.encounter_type in(6) and o.concept_id=1982 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 
                           and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           group by p.patient_id

                           union

                           select p.patient_id,max(e.encounter_datetime) data_consulta,1 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           inner join obs obsCv on obsCv.encounter_id=e.encounter_id
                           where e.encounter_type in(51) and o.concept_id=1982 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 
                           and obsCv.voided=0 and obsCv.concept_id=23821
                           and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           and obsCv.value_datetime between date_sub(CURDATE(), INTERVAL 9 MONTH) and CURDATE()
                           group by p.patient_id
                      
                           union

                           select p.patient_id,max(o.value_datetime) data_consulta,2 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           where e.encounter_type in(5,6) and o.concept_id=5599 and p.voided=0 and e.voided=0 and o.voided=0 
                           and o.value_datetime between date_sub(CURDATE(), INTERVAL 18 MONTH) and CURDATE()
                           group by p.patient_id

                           union

                           select p.patient_id,max(e.encounter_datetime) data_consulta,2 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           where e.encounter_type in(6) and o.concept_id=6332 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 
                           and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 18 MONTH) and CURDATE()
                           group by p.patient_id

                           union

                           select p.patient_id,max(e.encounter_datetime) data_consulta,2 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           where e.encounter_type in(5,6) and o.concept_id=6334 and o.value_coded=6332 and p.voided=0 and e.voided=0 and o.voided=0 
                           and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 18 MONTH) and CURDATE()
                           group by p.patient_id

                           union

                           SELECT p.patient_id,ps.start_date data_consulta,2 orderF FROM patient p
                           INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
                           INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
                           WHERE  pg.program_id=8  and ps.voided = 0 AND ps.state=27
                           and ps.start_date between date_sub(CURDATE(), INTERVAL 18 MONTH) and CURDATE()
                           GROUP BY p.patient_id

                           union

                           select p.patient_id,max(e.encounter_datetime) data_consulta,2 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           inner join obs obsInicio on obsInicio.encounter_id=e.encounter_id
                           where e.encounter_type in(53) and o.concept_id=6332 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 
                           and obsInicio.voided=0 and obsInicio.concept_id=1190
                           and obsInicio.value_datetime between date_sub(CURDATE(), INTERVAL 18 MONTH) and CURDATE() 
                           group by p.patient_id

                           union

                           select p.patient_id,max(e.encounter_datetime) data_consulta,2 orderF from patient p 
                           inner join encounter e on e.patient_id=p.patient_id
                           inner join obs o on o.encounter_id=e.encounter_id
                           inner join obs obsCv on obsCv.encounter_id=e.encounter_id
                           where e.encounter_type in(51) and o.concept_id=6332 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 
                           and obsCv.voided=0 and obsCv.concept_id=23821
                           and obsCv.value_datetime between date_sub(CURDATE(), INTERVAL 18 MONTH) and CURDATE()
                           group by p.patient_id
                              )final
                             order by patient_id,data_consulta desc,orderF
                         )preg_or_lac 

                          inner join person pe on pe.person_id = preg_or_lac.patient_id where pe.gender = 'F'
                          group by preg_or_lac.patient_id
                        ) preg_or_lac on preg_or_lac.patient_id=coorte12meses_final.patient_id

         left join(

         select   TX_TB.patient_id,TX_TB.data_inicio,EX1.data_transferidopara,EX2.data_tratamento  from 
                        (

                     
                     select  TX_TB.patient_id, max(TX_TB.data_inicio) as data_inicio from 
                        (
                            Select  p.patient_id,max(o.value_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id= :location and  o.value_datetime <=:endDate
                                    group by p.patient_id

                            union

                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=6257 and o.value_coded in(1065,1066)  
                                    and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id

                                    union

                           Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=6277 and o.value_coded in(703)  
                                    and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id

                                    union

                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (53) and o.concept_id=1406 and o.value_coded=42  and e.location_id= :location   and e.encounter_datetime <=:endDate
                                    group by p.patient_id

                            union
                            
                            select  pg.patient_id,max(date_enrolled) data_inicio                                                             
                            from    patient p inner join patient_program pg on p.patient_id=pg.patient_id                                       
                            where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location  and date_enrolled <=:endDate 
                            group by p.patient_id       

                            union

                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6) and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id


                            union
                            
                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23758 and o.value_coded in(1065,1066)  and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id


                           union
                           
                           Select   p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23761 and o.value_coded in(1065)   and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id



                            union
                           
                           Select   p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,1761)   and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id


                           union
                           
                           Select   p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   and e.location_id= :location and e.encounter_datetime <=:endDate
                                    group by p.patient_id

                          union


                           Select   p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id in(23723,23774,23951,307) and o.value_coded in(703,664)   and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id


                            union
                            

                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (6) and o.concept_id in(12) and o.value_coded in(703,664,1138)  and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id

                            union

                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(23723,23724,23951) and o.value_coded in(703,664)  and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id

                            union
                            
                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(307) and o.value_coded in(703,165184)  and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id

                            union
                            
                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(165189) and o.value_coded in(1065,1066)  and e.location_id= :location  and e.encounter_datetime <=:endDate
                                    group by p.patient_id


                            union
                            
                            Select  p.patient_id,max(e.encounter_datetime) data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(23951)  and e.location_id= :location  and e.encounter_datetime <=:endDate 
                                    group by p.patient_id
          


                        )TX_TB 
 
                        where TX_TB.patient_id not in (
                        Select NOT_TX_TB.patient_id from  (


                            Select  p.patient_id,o.value_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id= :location and  o.value_datetime <=:endDate

                            union

              Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=6257 and o.value_coded in(1065,1066)  
                                    and e.location_id= :location  and e.encounter_datetime <=:endDate

                                    union

                           Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=6277 and o.value_coded in(703)  
                                    and e.location_id= :location  and e.encounter_datetime <=:endDate

                                    union                            

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (53) and o.concept_id=1406 and o.value_coded=42  and e.location_id= :location   and e.encounter_datetime <=:endDate

                            union
                            
                            select  pg.patient_id,date_enrolled data_inicio                                                             
                            from    patient p inner join patient_program pg on p.patient_id=pg.patient_id                                       
                            where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location  and date_enrolled <=:endDate 

                            union

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6) and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location  and e.encounter_datetime <=:endDate


                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23758 and o.value_coded in(1065,1066)  and e.location_id= :location  and e.encounter_datetime <=:endDate


                           union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23761 and o.value_coded in(1065)   and e.location_id= :location  and e.encounter_datetime <=:endDate



                            union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,1761)   and e.location_id= :location  and e.encounter_datetime <=:endDate


                           union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   and e.location_id= :location and e.encounter_datetime <=:endDate

                          union


                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id in(23723,23774,23951,307) and o.value_coded in(703,664)   and e.location_id= :location  and e.encounter_datetime <=:endDate


                            union
                            

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (6) and o.concept_id in(12) and o.value_coded in(703,664,1138)  and e.location_id= :location  and e.encounter_datetime <=:endDate 

                            union      

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(23723,23724,23951) and o.value_coded in(703,664)  and e.location_id= :location  and e.encounter_datetime <=:endDate

                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(307) and o.value_coded in(703,165184)  and e.location_id= :location  and e.encounter_datetime <=:endDate

                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(165189) and o.value_coded in(1065,1066)  and e.location_id= :location  and e.encounter_datetime <=:endDate


                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(23951)  and e.location_id= :location  and e.encounter_datetime <=:endDate
          


                            )NOT_TX_TB

                             where NOT_TX_TB.data_inicio between date_sub(:endDate, INTERVAL 6 MONTH) and :endDate
                             
                             group by NOT_TX_TB.patient_id

                            )
                             group by TX_TB.patient_id
 


                  )TX_TB

         left join


         (
     select TR.patient_id,TR.data_transferidopara from 

     ( 
               select transferidopara.patient_id, transferidopara.data_transferidopara from 
                          ( 
                             
                              select patient_id,max(data_transferidopara) data_transferidopara from 
                              ( 

                              select pg.patient_id,max(ps.start_date) data_transferidopara from  patient p  
                              inner join patient_program pg on p.patient_id=pg.patient_id  
                              inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                              where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.state=7 and  ps.start_date<:endDate
                              group by p.patient_id  
                              
                              union  
                              
                              select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on o.encounter_id=e.encounter_id  
                              where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate and e.location_id= :location   
                              and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53  
                              group by p.patient_id  
                              
                              union 
                              
                              select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 
                              and e.encounter_datetime <:endDate and e.location_id= :location    
                              and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 
                              group by p.patient_id 


                              union

                              select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on o.encounter_id=e.encounter_id  
                              where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate and e.location_id= :location   
                              and o.voided=0 and o.concept_id=2016 and o.value_coded in(1706,23863) and e.encounter_type=21  
                              group by p.patient_id  
                               
                               ) transferido 
                               group by patient_id  
                               ) transferidopara  
                              inner join (

                              select patient_id,max(encounter_datetime) encounter_datetime 
                              from
                              (  
                              select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p  
                              inner join encounter e on e.patient_id=p.patient_id 
                              where p.voided=0 and e.voided=0 and e.encounter_datetime <:endDate and e.location_id= :location   
                              and e.encounter_type in (18,6,9) 
                              group by p.patient_id  
                              
                              union  
                              
                              Select p.patient_id,max(value_datetime) encounter_datetime from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on e.encounter_id=o.encounter_id  
                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
                              and o.concept_id=23866 and o.value_datetime is not null 
                              and o.value_datetime<:endDate and e.location_id= :location    
                              group by p.patient_id
                              
                              ) consultaLev group by patient_id 
                              ) consultaOuARV 
                              on transferidopara.patient_id=consultaOuARV.patient_id                         
                              where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara  and transferidopara.data_transferidopara <= :endDate

                              )TR

                          left join

                          (
                             select transferidopara.patient_id, transferidopara.data_transferidopara from 
                          ( 
                             
                              select patient_id,max(data_transferidopara) data_transferidopara from 
                              ( 

                              select pg.patient_id,max(ps.start_date) data_transferidopara from  patient p  
                              inner join patient_program pg on p.patient_id=pg.patient_id  
                              inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
                              where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id=2 and ps.state=7 and  ps.start_date<:endDate
                              group by p.patient_id  
                              
                              union  
                              
                              select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on o.encounter_id=e.encounter_id  
                              where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate and e.location_id= :location   
                              and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53  
                              group by p.patient_id  
                              
                              union 
                              
                              select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 
                              and e.encounter_datetime <:endDate and e.location_id= :location    
                              and o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 
                              group by p.patient_id 


                              union

                              select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on o.encounter_id=e.encounter_id  
                              where e.voided=0 and p.voided=0 and o.obs_datetime <:endDate and e.location_id= :location   
                              and o.voided=0 and o.concept_id=2016 and o.value_coded in(1706,23863) and e.encounter_type=21  
                              group by p.patient_id  
                               
                               ) transferido 
                               group by patient_id  
                               ) transferidopara  
                              inner join (

                              select patient_id,max(encounter_datetime) encounter_datetime 
                              from
                              (  
                              select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p  
                              inner join encounter e on e.patient_id=p.patient_id 
                              where p.voided=0 and e.voided=0 and e.encounter_datetime <:endDate and e.location_id= :location   
                              and e.encounter_type in (18,6,9) 
                              group by p.patient_id  
                              
                              union  
                              
                              Select p.patient_id,max(value_datetime) encounter_datetime from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on e.encounter_id=o.encounter_id  
                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
                              and o.concept_id=23866 and o.value_datetime is not null 
                              and o.value_datetime<:endDate and e.location_id= :location    
                              group by p.patient_id
                              
                              ) consultaLev group by patient_id 
                              ) consultaOuARV 
                              on transferidopara.patient_id=consultaOuARV.patient_id                         
                              where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara  
                              and transferidopara.data_transferidopara between date_sub(:endDate, INTERVAL 6 MONTH) and :endDate

                              )TRF_OUT on TR.patient_id= TRF_OUT.patient_id and TRF_OUT.patient_id is null


                     left join
                      ( 
               
                       Select TB.patient_id,TB.data_tratamento from    
                      (
                            Select p.patient_id, o.value_datetime data_tratamento from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on e.encounter_id=o.encounter_id  
                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in(6,9)
                              and o.concept_id=1113 and  e.location_id= :location    

                              union

                              select  pg.patient_id, pg.date_enrolled  data_tratamento                            
                                      from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
                              where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location     

                              union

                              Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on e.encounter_id=o.encounter_id  
                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53
                              and o.concept_id=1406 and o.value_coded=42 and e.location_id= :location    

                              union 

                              Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on e.encounter_id=o.encounter_id  
                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6
                              and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location    

                              ) TB  


                             where TB.data_tratamento <=:endDate and  TB.patient_id not in

                                (

                                    Select TB.patient_id from    
                                   (
                                    Select p.patient_id, o.value_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in(6,9)
                                      and o.concept_id=1113 and  e.location_id= :location    

                                      union

                                      select  pg.patient_id, pg.date_enrolled  data_tratamento                            
                                              from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
                                      where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location     

                                      union

                                      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53
                                      and o.concept_id=1406 and o.value_coded=42 and e.location_id= :location    

                                      union 

                                      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6
                                      and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location    
                                      

                                      ) TB  where TB.data_tratamento between date_sub(:endDate, INTERVAL 6 MONTH)  and :endDate
                                )    
                       )TB2 ON  TB2.patient_id=TRF_OUT.patient_id 

         )EX1 on EX1.patient_id=TX_TB.patient_id


         left join

         ( 
                   
             Select TB.patient_id,TB.data_tratamento from    
                      (
                            Select p.patient_id, o.value_datetime data_tratamento from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on e.encounter_id=o.encounter_id  
                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in(6,9)
                              and o.concept_id=1113 and  e.location_id= :location    

                              union

                              select  pg.patient_id, pg.date_enrolled  data_tratamento                            
                                      from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
                              where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location     

                              union

                              Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on e.encounter_id=o.encounter_id  
                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53
                              and o.concept_id=1406 and o.value_coded=42 and e.location_id= :location    

                              union 

                              Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                              inner join encounter e on p.patient_id=e.patient_id  
                              inner join obs o on e.encounter_id=o.encounter_id  
                              where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6
                              and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location    

                              ) TB  


                             where TB.data_tratamento  between date_sub(:endDate, INTERVAL 12 MONTH)  and :endDate and  TB.patient_id not in

                                (

                                    Select TB.patient_id from    
                                   (
                                    Select p.patient_id, o.value_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in(6,9)
                                      and o.concept_id=1113 and  e.location_id= :location    

                                      union

                                      select  pg.patient_id, pg.date_enrolled  data_tratamento                            
                                              from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
                                      where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location     

                                      union

                                      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53
                                      and o.concept_id=1406 and o.value_coded=42 and e.location_id= :location    

                                      union 

                                      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6
                                      and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location    
                                      

                                      ) TB  where TB.data_tratamento between date_sub(:endDate, INTERVAL 12 MONTH)  and date_sub(:endDate, INTERVAL 6 MONTH)
                                  )
              ) EX2 on EX2.patient_id=TX_TB.patient_id

            where 

            (EX1.data_transferidopara is null and EX2.data_tratamento is null)  

            group by TX_TB.patient_id

            )TX_TB on coorte12meses_final.patient_id=TX_TB.patient_id

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
                                         when 165264  then 'BRIGADAS MVEIS (DCBM)'
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
                                and obsEstado.value_coded in(1256,1257) 
                                and obsEstado.voided=0 
                                and o.voided=0 
                                and grupo.voided=0
                                and e.encounter_datetime <= CURDATE()
                                )f
        
                                group by f.patient_id,f.encounter_datetime
        
                        )f
            ) MDC on MDC.patient_id=coorte12meses_final.patient_id
            
            where

            (coorte12meses_final.data_estado is null or (coorte12meses_final.data_estado is not null and  coorte12meses_final.data_usar_c>coorte12meses_final.data_estado)) 
            and date_add(coorte12meses_final.data_usar, interval 28 day) >=:endDate     
            and coorte12meses_final.patient_id not in
            (
                                      Select NOT_TX_TB.patient_id from  (


                            Select  p.patient_id,o.value_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=1113  and e.location_id= :location and  o.value_datetime <=:endDate

                            union

              Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=6257 and o.value_coded in(1065,1066)  
                                    and e.location_id= :location  and e.encounter_datetime <=:endDate

                                    union

                           Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6,9) and o.concept_id=6277 and o.value_coded in(703)  
                                    and e.location_id= :location  and e.encounter_datetime <=:endDate

                                    union                            

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (53) and o.concept_id=1406 and o.value_coded=42  and e.location_id= :location   and e.encounter_datetime <=:endDate

                            union
                            
                            select  pg.patient_id,date_enrolled data_inicio                                                             
                            from    patient p inner join patient_program pg on p.patient_id=pg.patient_id                                       
                            where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location  and date_enrolled <=:endDate 

                            union

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id                                                           
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                                
                                    e.encounter_type in (6) and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location  and e.encounter_datetime <=:endDate


                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23758 and o.value_coded in(1065,1066)  and e.location_id= :location  and e.encounter_datetime <=:endDate


                           union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23761 and o.value_coded in(1065)   and e.location_id= :location  and e.encounter_datetime <=:endDate



                            union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=1766 and o.value_coded in(1763,1764,1762,1760,1765,1761)   and e.location_id= :location  and e.encounter_datetime <=:endDate


                           union
                           
                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id=23722 and o.value_coded in(23723,23774,23951,307,12)   and e.location_id= :location and e.encounter_datetime <=:endDate

                          union


                           Select   p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 and                                                            
                                    e.encounter_type in (6) and o.concept_id in(23723,23774,23951,307) and o.value_coded in(703,664)   and e.location_id= :location  and e.encounter_datetime <=:endDate


                            union
                            

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (6) and o.concept_id in(12) and o.value_coded in(703,664,1138)  and e.location_id= :location  and e.encounter_datetime <=:endDate

                            union        

                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(23723,23724,23951) and o.value_coded in(703,664)  and e.location_id= :location  and e.encounter_datetime <=:endDate

                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(307) and o.value_coded in(703,165184)  and e.location_id= :location  and e.encounter_datetime <=:endDate

                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(165189) and o.value_coded in(1065,1066)  and e.location_id= :location  and e.encounter_datetime <=:endDate


                            union
                            
                            Select  p.patient_id,e.encounter_datetime data_inicio                                                           
                            from    patient p                                                                                                   
                                    inner join encounter e on p.patient_id=e.patient_id                                                         
                                    inner join obs o on o.encounter_id=e.encounter_id
                            where   e.voided=0 and o.voided=0 and p.voided=0 
                                    and e.encounter_type in (13) and o.concept_id in(23951)  and e.location_id= :location  and e.encounter_datetime <=:endDate
          


                            )NOT_TX_TB

                              
 
                           left join  (

                                    Select TB.patient_id,TB.data_tratamento from    
                                   (
                                    Select p.patient_id, o.value_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in(6,9)
                                      and o.concept_id=1113 and  e.location_id= :location    

                                      union

                                      select  pg.patient_id, pg.date_enrolled  data_tratamento                            
                                              from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
                                      where   pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location     

                                      union

                                      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53
                                      and o.concept_id=1406 and o.value_coded=42 and e.location_id= :location    

                                      union 

                                      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
                                      inner join encounter e on p.patient_id=e.patient_id  
                                      inner join obs o on e.encounter_id=o.encounter_id  
                                      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6
                                      and o.concept_id=1268 and o.value_coded=1256  and e.location_id= :location    
                                      

                                      ) TB  where TB.data_tratamento  between date_sub(:endDate, INTERVAL 12 MONTH)  and date_sub(:endDate, INTERVAL 6 MONTH)

                              )TB on TB.patient_id=NOT_TX_TB.patient_id

                              where NOT_TX_TB.data_inicio between date_sub(:endDate, INTERVAL 6 MONTH) and :endDate and TB.patient_id is null
                              
                             group by NOT_TX_TB.patient_id

            )

            group by coorte12meses_final.patient_id