                      select 
                      inicio.patient_id as patient_id,
                      inicio.identifier as NID,
                      inicio.NomeCompleto as NAME,
                      inicio.gender AS GENDER,
                      inicio.idade as AGE,
                      DATE_FORMAT(date(inicio.data_inicio), '%d/%m/%Y') as ARV_INITIATION,
                      inicio.PREG_LAC as PREG_LAC,
                      inicio.TB as TB,
                      DATE_FORMAT(date(inicio.data_levantamento), '%d/%m/%Y') as DATA_LEVANTAMENTO_FILA,
                      DATE_FORMAT(date(inicio.data_proximo_levantamento), '%d/%m/%Y') as DATA_PROXIMO_LEVANTAMENTO_FILA,
                      DATE_FORMAT(date(inicio.data_levantamento_recepcao), '%d/%m/%Y') as DATA_LEVANTAMENTO_RECEPCAO,
                      DATE_FORMAT(date(inicio.data_proximo_levantamento_recepcao), '%d/%m/%Y') as DATA_PROXIMO_LEVANTAMENTO_RECEPCAO,
                      DATE_FORMAT(date(inicio.data_seguimento), '%d/%m/%Y') as DATA_SEGUIMENTO,
                      DATE_FORMAT(date(inicio.data_proximo_seguimento), '%d/%m/%Y') as DATA_PROXIMO_SEGUIMENTO,
                      inicio.state as STATE,
                      DATE_FORMAT(date(inicio.start_date), '%d/%m/%Y') as DATA_ESTADO,
                      inicio.state_fc as STATE_FC,
                      if(inicio.state_fc is null, null, DATE_FORMAT(date(inicio.SATE_FC_DATE), '%d/%m/%Y')) as SATE_FC_DATE,
                      inicio.state_fr as STATE_FR,
                      if(inicio.state_fr is null, null,DATE_FORMAT(date(inicio.STATE_FR_DATE), '%d/%m/%Y') ) as STATE_FR_DATE,
                      inicio.state_home_card as STATE_HOME_CARD,
                      DATE_FORMAT(date(inicio.home_card_date), '%d/%m/%Y') as HOME_CARD_DATE


                       from 
                       ( 
                          select 
                          inicio_real.patient_id,
                          pid.identifier,
                          concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NomeCompleto,
                          p.gender as gender,
                          if(p.birthdate is not null, floor(datediff(:evaluationDate,p.birthdate)/365),'N/A') idade,
                          min(data_inicio) data_inicio,
                          preg_or_lac.PREG_LAC,
                          if(tbFinal.patient_id is not null, 'SIM', '') as TB,
                          fila.data_levantamento,
                          fila.data_proximo_levantamento,
                          recepcao.data_levantamento_recepcao,
                          recepcao.data_proximo_levantamento_recepcao,
                          seguimento.data_seguimento,
                          obsProximaConsulta.value_datetime data_proximo_seguimento,
                          case 
                          when last_state.state = 9 then 'ABANDONO' 
                          -- when ps.state = 6 then 'ACTIVO NO PROGRAMA' 
                          when last_state.state = 10 then 'OBITO' 
                          when last_state.state = 8 then 'SUSPENSO' 
                          when last_state.state = 7 then 'TRANSFERIDO PARA' 
                          -- when ps.state = 29 then 'TRANSFERIDO DE' 
                          end AS state,
                          last_state.start_date,
                          homeCardVisit.state_home_card,
                          homeCardVisit.encounter_datetime as home_card_date,
                          FC.state_fc,
                          FC.encounter_datetime as SATE_FC_DATE,
                          FR.state_fr,
                          FR.encounter_datetime as STATE_FR_DATE

                          from 
                          ( 
                          Select p.patient_id,min(e.encounter_datetime) data_inicio from patient p  
                          inner join encounter e on p.patient_id=e.patient_id  
                          inner join obs o on o.encounter_id=e.encounter_id 
                          where e.voided=0 and o.voided=0 and p.voided=0 and  
                          e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and  
                          e.encounter_datetime<=:endDate and e.location_id=:location 
                          group by p.patient_id 
                          union 
                          Select p.patient_id,min(value_datetime) data_inicio from patient p 
                          inner join encounter e on p.patient_id=e.patient_id 
                          inner join obs o on e.encounter_id=o.encounter_id 
                          where  p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and  
                          o.concept_id=1190 and o.value_datetime is not null and  
                          o.value_datetime<=:endDate and e.location_id=:location 
                          group by p.patient_id 
                          union 
                          select pg.patient_id,min(date_enrolled) data_inicio  from patient p 
                          inner join patient_program pg on p.patient_id=pg.patient_id 
                          where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
                          group by pg.patient_id 
                          union 
                          SELECT e.patient_id, MIN(e.encounter_datetime) AS data_inicio  FROM   patient p 
                          inner join encounter e on p.patient_id=e.patient_id 
                          WHERE p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location 
                          GROUP BY p.patient_id 
                          union 
                          Select p.patient_id,min(value_datetime) data_inicio from patient p 
                          inner join encounter e on p.patient_id=e.patient_id 
                          inner join obs o on e.encounter_id=o.encounter_id 
                          where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  
                          o.concept_id=23866 and o.value_datetime is not null and  
                          o.value_datetime<=:endDate and e.location_id=:location 
                          group by p.patient_id 
                          ) inicio_real 
                           
                          inner join person p on p.person_id=inicio_real.patient_id         
                          left join   ( 
                          select pad1.*  from person_address pad1  
                          inner join   (  
                          select person_id,min(person_address_id) id   from person_address  
                          where voided=0  
                          group by person_id  
                          ) pad2  
                          where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
                          ) pad3 on pad3.person_id=inicio_real.patient_id 
                          left join( 
                          select pn1.*  from person_name pn1  
                          inner join (  
                          select person_id,min(person_name_id) id   from person_name  
                          where voided=0  
                          group by person_id  
                          ) pn2  
                          where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
                          ) pn on pn.person_id=inicio_real.patient_id 
                          left join  ( 
                          select pid1.*  from patient_identifier pid1  
                          inner join  (  
                          select patient_id,min(patient_identifier_id) id  from patient_identifier  
                          where voided=0  
                          group by patient_id  
                          ) pid2 
                          where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
                          ) pid on pid.patient_id=inicio_real.patient_id  

                          left join
                          (
                           select preg_or_lac.patient_id, preg_or_lac.data_consulta,if(preg_or_lac.orderF=1,'Grávida','Lactante') as PREG_LAC from 
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
                           where e.encounter_type in(53) and o.concept_id=1982 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 and obsInicio.voided=0
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
                           where e.encounter_type in(51) and o.concept_id=1982 and o.value_coded=1065 and p.voided=0 and e.voided=0 and o.voided=0 and obsCv.voided=0
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
                         )preg_or_lac inner join person pe on pe.person_id = preg_or_lac.patient_id where pe.gender = 'F'
                          group by preg_or_lac.patient_id
                        ) preg_or_lac on preg_or_lac.patient_id=inicio_real.patient_id

                        left join

                        ( 

                             SELECT tb.patient_id from

                             (

                               select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
                               inner join encounter e on e.patient_id=p.patient_id
                               inner join obs o on o.encounter_id=e.encounter_id
                               where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
                               and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
                               group by p.patient_id

                               union  


                               SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
                               INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
                               INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
                               WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
                               and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
                               GROUP BY p.patient_id

                               union

                               select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
                               inner join encounter e on e.patient_id=p.patient_id
                               inner join obs o on o.encounter_id=e.encounter_id
                               where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
                               and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
                               group by p.patient_id

                               union

                               select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
                               inner join encounter e on e.patient_id=p.patient_id
                               inner join obs o on o.encounter_id=e.encounter_id
                               where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
                               and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
                               group by p.patient_id

                               union

                               select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
                               inner join encounter e on e.patient_id=p.patient_id
                               inner join obs o on o.encounter_id=e.encounter_id
                               where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
                               and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
                               group by p.patient_id
                               )tb
                        ) tbFinal on tbFinal.patient_id=inicio_real.patient_id


                        left join
                        (  select fila.patient_id,fila.data_levantamento,fila.data_proximo_levantamento from (
                           select fila.patient_id, fila.data_levantamento as data_levantamento ,max(obs_fila.value_datetime) data_proximo_levantamento from (  
                           select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p 
                           inner join encounter e on p.patient_id=e.patient_id 
                           where encounter_type=18 and e.encounter_datetime <=:evaluationDate and e.location_id=:location and e.voided=0 and p.voided=0 
                           group by p.patient_id 
                           )fila 
                           inner join obs obs_fila on obs_fila.person_id=fila.patient_id 
                           where obs_fila.voided=0 and obs_fila.concept_id=5096 and fila.data_levantamento=obs_fila.obs_datetime
                           group by fila.patient_id

                        )fila 
                        group by fila.patient_id
                        ) fila on fila.patient_id=inicio_real.patient_id

                        left join 

                        (
                        select p.patient_id,max(o.value_datetime) data_levantamento_recepcao, date_add(max(o.value_datetime), INTERVAL 30 day) data_proximo_levantamento_recepcao 
                        from patient p inner join encounter e on p.patient_id = e.patient_id 
                        inner join obs o on o.encounter_id = e.encounter_id 
                        where  e.voided = 0 and p.voided = 0 and o.value_datetime <= :evaluationDate and o.voided = 0 and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location 
                        group by p.patient_id

                        )recepcao on recepcao.patient_id=inicio_real.patient_id

                        left join
                        (
                           select seguimento.patient_id, seguimento.data_seguimento from (  
                           select p.patient_id,max(e.encounter_datetime) as data_seguimento from patient p 
                           inner join encounter e on p.patient_id=e.patient_id 
                           where encounter_type in(6,9) and e.encounter_datetime <=:evaluationDate and e.location_id=:location and e.voided=0 and p.voided=0 
                           group by p.patient_id 
                           )seguimento 
                        ) seguimento on seguimento.patient_id=inicio_real.patient_id
                        
                        left join 
                        (
		                        select distinct max_estado.patient_id, max_estado.data_estado, ps.state, ps.start_date 
								from (                                          						
										select pg.patient_id,																											
											max(ps.start_date) data_estado																							
										from	patient p																												
											inner join patient_program pg on p.patient_id = pg.patient_id																
											inner join patient_state ps on pg.patient_program_id = ps.patient_program_id												
										where pg.voided=0 and ps.voided=0 and p.voided=0 and pg.program_id = 2  																				
											and ps.start_date<= :evaluationDate and pg.location_id =:location group by pg.patient_id                                              
									) 
								max_estado                                                                                                                        
									inner join patient_program pp on pp.patient_id = max_estado.patient_id															
									inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado	        
								where pp.program_id = 2 and ps.state in (9,10,8,7) and pp.voided = 0 and ps.voided = 0 and pp.location_id = :location 
						)last_state on last_state.patient_id = inicio_real.patient_id
                        left join  obs obsProximaConsulta  on obsProximaConsulta.person_id=seguimento.patient_id and obsProximaConsulta.concept_id=1410 and  obsProximaConsulta.obs_datetime=seguimento.data_seguimento and obsProximaConsulta.voided=0
                        left join

                        (
                           select 
                             p.patient_id,max(e.encounter_datetime) as encounter_datetime,   
                             case o.value_coded
                             when 1366  then 'OBITO '
                             when 1706  then 'TRANSFERIDO PARA'
                             when 23863 then 'AUTO TRASFERENCIA'
                             else null end as state_home_card
                         from  patient p 
                             inner join encounter e on e.patient_id=p.patient_id
                             inner join obs o on o.encounter_id=e.encounter_id
                         where o.voided=0 
                         and o.concept_id in(2031,23944,23945,2016) 
                         and o.value_coded in(1366,1706,23863) 
                         and e.encounter_type in (21) 
                         and e.voided=0 and e.location_id=:location 
                         and e.encounter_datetime<=:evaluationDate
                         GROUP BY p.patient_id 
                        ) homeCardVisit on homeCardVisit.patient_id=inicio_real.patient_id

                        left join

                        (
                       SELECT f.patient_id, encounter_datetime, state_fr FROM (
                             select 
                             p.patient_id,o.obs_datetime as encounter_datetime,   
                             case o.value_coded
                             when 1366  then  'OBITO '
                             when 1706  then  'TRANSFERIDO PARA'
                             when 1707  then  'ABANDONO'
                             when 1709  then  'SUSPENSO'
                             else null end as state_fr
                         from  patient p 
                             inner join encounter e on e.patient_id=p.patient_id
                             inner join obs o on o.encounter_id=e.encounter_id
                         where o.voided=0 and o.concept_id in(6272) and e.encounter_type in (53) and e.voided=0 and e.location_id=:location and o.obs_datetime<=:evaluationDate
                         order by p.patient_id, encounter_datetime desc
                         ) f
                         GROUP BY f.patient_id 
                        )FR on FR.patient_id=inicio_real.patient_id

                        left join
                        (
                        SELECT * FROM (
                           select 
                             p.patient_id,encounter_datetime as encounter_datetime,   
                             case o.value_coded
                             when 1366  then  'OBITO '
                             when 1706  then  'TRANSFERIDO PARA'
                             when 1707  then  'ABANDONO'
                             when 1709  then  'SUSPENSO'
                             else null end as state_fc
                         from  patient p 
                             inner join encounter e on e.patient_id=p.patient_id
                             inner join obs o on o.encounter_id=e.encounter_id
                         where   o.voided=0 and o.concept_id in(6273) and e.encounter_type in (6) and e.voided=0 and e.location_id=:location and e.encounter_datetime<=:evaluationDate
                         order by p.patient_id, encounter_datetime desc
                         ) fcc
                         GROUP BY fcc.patient_id 
 
                        )FC on FC.patient_id=inicio_real.patient_id


                         group by inicio_real.patient_id
                       
                       )inicio 

                      where inicio.data_inicio between :startDate and :endDate

                      group by inicio.patient_id