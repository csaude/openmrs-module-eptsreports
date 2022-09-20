select coorte12meses_final.patient_id as patient_id 
            from 
            (select     inicio_fila_seg_prox.*, 
                        GREATEST(COALESCE(data_fila,data_seguimento,data_recepcao_levantou),COALESCE(data_seguimento,data_fila,data_recepcao_levantou),COALESCE(data_recepcao_levantou,data_seguimento,data_fila))  data_usar_c, 
            GREATEST(COALESCE(data_proximo_lev,data_proximo_seguimento,data_recepcao_levantou30),COALESCE(data_proximo_seguimento,data_proximo_lev,data_recepcao_levantou30),COALESCE(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) data_usar 
            from 
            (select     inicio_fila_seg.*, 
                    max(obs_fila.value_datetime) data_proximo_lev, 
                    max(obs_seguimento.value_datetime) data_proximo_seguimento, 
                    date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30 
            from 
            (select inicio.*,        
                    saida.data_estado,       
                    max_fila.data_fila, 
                    max_consulta.data_seguimento, 
                    max_recepcao.data_recepcao_levantou 
            from 
            (   Select patient_id,min(data_inicio) data_inicio 
                    from 
                        (    
                            Select  p.patient_id,min(e.encounter_datetime) data_inicio 
                            from    patient p  
                                    inner join encounter e on p.patient_id=e.patient_id  
                                    inner join obs o on o.encounter_id=e.encounter_id 
                            where   e.voided=0 and o.voided=0 and p.voided=0 and  
                                    e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and  
                                    e.encounter_datetime<=:endDate and e.location_id=:location
                            group by p.patient_id 
                            union 
                            Select  p.patient_id,min(value_datetime) data_inicio 
                            from    patient p 
                                    inner join encounter e on p.patient_id=e.patient_id 
                                    inner join obs o on e.encounter_id=o.encounter_id 
                            where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and  
                                    o.concept_id=1190 and o.value_datetime is not null and  
                                    o.value_datetime<=:endDate and e.location_id=:location
                            group by p.patient_id 
                            union 
                            select  pg.patient_id,min(date_enrolled) data_inicio 
                            from    patient p inner join patient_program pg on p.patient_id=pg.patient_id 
                            where   pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location
                            group by pg.patient_id 
                            union 
                              SELECT    e.patient_id, MIN(e.encounter_datetime) AS data_inicio  
                              FROM      patient p 
                                        inner join encounter e on p.patient_id=e.patient_id 
                              WHERE     p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location
                              GROUP BY  p.patient_id 
                            union 
                            Select  p.patient_id,min(value_datetime) data_inicio 
                            from    patient p 
                                    inner join encounter e on p.patient_id=e.patient_id 
                                    inner join obs o on e.encounter_id=o.encounter_id 
                            where   p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and  
                                    o.concept_id=23866 and o.value_datetime is not null and  
                                    o.value_datetime<=:endDate and e.location_id=:location
                            group by p.patient_id    
                        ) inicio_real 
                    group by patient_id 
            )inicio 
            left join 
            (select patient_id,max(data_estado) data_estado 
                from  
                    ( 
                        select distinct max_estado.patient_id, max_estado.data_estado 
                    from (                                                                  
                        select pg.patient_id,                                                                                                           
                            max(ps.start_date) data_estado                                                                                          
                        from    patient p                                                                                                               
                            inner join patient_program pg on p.patient_id = pg.patient_id                                                               
                            inner join patient_state ps on pg.patient_program_id = ps.patient_program_id                                                
                        where pg.voided=0 and ps.voided=0 and p.voided=0                                                                                
                            and ps.start_date<= :endDate and location_id =:location and pg.program_id =2 group by pg.patient_id                                               
                        ) 
                    max_estado                                                                                                                        
                        inner join patient_program pp on pp.patient_id = max_estado.patient_id                                                          
                        inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
                    where pp.program_id = 2 and ps.state in (7,8,10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location      

                        union 
                        select  p.patient_id, 
                                max(o.obs_datetime) data_estado 
                        from    patient p  
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs  o on e.encounter_id=o.encounter_id 
                        where   e.voided=0 and o.voided=0 and p.voided=0 and  
                                e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1706,1366,1709) and   
                                o.obs_datetime<=:endDate and e.location_id=:location
                        group by p.patient_id 
                        union 
                        select person_id as patient_id,death_date as data_estado 
                        from person  
                        where dead=1 and death_date is not null and death_date<=:endDate 
                        union 
                        select  p.patient_id, 
                                max(obsObito.obs_datetime) data_estado 
                        from    patient p  
                                inner join encounter e on p.patient_id=e.patient_id 
                                inner join obs obsObito on e.encounter_id=obsObito.encounter_id 
                        where   e.voided=0 and p.voided=0 and obsObito.voided=0 and  
                                e.encounter_type in (21,36,37) and  e.encounter_datetime<=:endDate and  e.location_id=:location and  
                                obsObito.concept_id in (2031,23944,23945) and obsObito.value_coded=1366  
                        group by p.patient_id 
                    ) allSaida 
                group by patient_id          
            ) saida on inicio.patient_id=saida.patient_id 
            left join 
            (   Select  p.patient_id,max(encounter_datetime) data_fila 
                from    patient p  
                        inner join encounter e on e.patient_id=p.patient_id 
                where   p.voided=0 and e.voided=0 and e.encounter_type=18 and  
                        e.location_id=:location and e.encounter_datetime<=:endDate 
                group by p.patient_id 
            ) max_fila on inicio.patient_id=max_fila.patient_id  
            left join 
            (   Select  p.patient_id,max(encounter_datetime) data_seguimento 
                from    patient p  
                        inner join encounter e on e.patient_id=p.patient_id 
                where   p.voided=0 and e.voided=0 and e.encounter_type in (6,9) and  
                        e.location_id=:location and e.encounter_datetime<=:endDate 
                group by p.patient_id 
            ) max_consulta on inicio.patient_id=max_consulta.patient_id 
            left join 
            ( 
                Select  p.patient_id,max(value_datetime) data_recepcao_levantou 
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
                    where voided=0 
                    group by patient_id 
                ) pid2 
                where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id 
            ) pid on pid.patient_id=coorte12meses_final.patient_id 
            left join person_attribute pat on pat.person_id=coorte12meses_final.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value <> '' and pat.voided=0 
            inner join(
	Select p.patient_id,max(e.encounter_datetime) encounter_datetime from patient p 
	inner join encounter e on p.patient_id=e.patient_id 
	where p.voided=0 and e.voided=0 and e.encounter_type=6 and 
	e.encounter_datetime<=curdate()  and e.location_id=:location 
	group by p.patient_id 
            )maxEnc on maxEnc.patient_id = coorte12meses_final.patient_id
            inner join (
	select patient_id, max(data_inicio) screeningDate from (
	select p.patient_id,obsTB.obs_datetime data_inicio from patient p  
	inner join encounter e on e.patient_id=p.patient_id  
	inner join obs obsTB on obsTB.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startDate and  :endDate  and   
	e.location_id=:location and e.encounter_type=53 and obsTB.concept_id=1406 and obsTB.value_coded =42 and obsTB.voided=0  
	union
	select  pg.patient_id,date_enrolled data_inicio from  patient p  
	inner join patient_program pg on p.patient_id=pg.patient_id                         
	where pg.voided=0 and p.voided=0 and program_id=5 and date_enrolled between :startDate and :endDate  and location_id=:location 
	union
	select p.patient_id,obsTB.obs_datetime data_inicio from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTB on obsTB.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startDate and :endDate and  
	e.location_id=:location and e.encounter_type=6 and obsTB.concept_id=1268 and obsTB.value_coded= 1256 and obsTB.voided=0 
	union
	select p.patient_id, e.encounter_datetime data_inicio from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTBPositivo on obsTBPositivo.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and :endDate and 
	e.location_id=:location and e.encounter_type=6 and obsTBPositivo.concept_id=23758 and obsTBPositivo.value_coded=1065 and obsTBPositivo.voided=0 
	union
	select p.patient_id, e.encounter_datetime data_inicio  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTBActiva on obsTBActiva.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and :endDate and  
	e.location_id=:location and e.encounter_type=6 and obsTBActiva.concept_id=23761 and obsTBActiva.value_coded=1065 and obsTBActiva.voided=0 
	union
	select p.patient_id, e.encounter_datetime data_inicio  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsSintomasTB on obsSintomasTB.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and :endDate and  
	e.location_id=:location and e.encounter_type=6 and obsSintomasTB.concept_id=1766 and obsSintomasTB.value_coded in (1763,1764,1762,1760,23760,1765,161) and obsSintomasTB.voided=0 
	union
	select p.patient_id, e.encounter_datetime data_inicio  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsLabResearch on obsLabResearch.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and :endDate and  
	e.location_id=:location and e.encounter_type=6 and obsLabResearch.concept_id=23722 and obsLabResearch.value_coded in (23723,23774,23951,307,12) and obsLabResearch.voided=0 
	union
	select p.patient_id, e.encounter_datetime data_inicio  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and :endDate and  
	e.location_id=:location and e.encounter_type=6 and obsTestResult.concept_id in (23723, 23774, 23951, 307, 12) and obsTestResult.value_coded in (703, 664, 1138) and obsTestResult.voided=0
	union
	select p.patient_id, e.encounter_datetime data_inicio  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and :endDate and  
	e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id in (307, 23723, 23951, 23774) and obsTestResult.value_coded in (703, 1067, 664) and obsTestResult.voided=0
	union
	select p.patient_id, e.encounter_datetime data_inicio  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and :endDate and  
	e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165189 and obsTestResult.value_coded in (1065,1066) and obsTestResult.voided=0
	) positiveScreening group by patient_id
	) positiveTbScreening on positiveTbScreening.patient_id = coorte12meses_final.patient_id
	left join
	(
	select p.patient_id, e.encounter_datetime data_pedido  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsLabResearch on obsLabResearch.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=6 and obsLabResearch.concept_id=23722 
	and obsLabResearch.value_coded = 23723 and obsLabResearch.voided=0 
	)genexpertTest on (genexpertTest.patient_id = coorte12meses_final.patient_id and genexpertTest.data_pedido between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=6 and obsTestResult.concept_id = 23723 and obsTestResult.value_coded in (703, 664) and obsTestResult.voided=0
	)geneXpertResult on (geneXpertResult.patient_id = coorte12meses_final.patient_id and geneXpertResult.data_resultado between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_pedido  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsLabResearch on obsLabResearch.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=6 and obsLabResearch.concept_id=23722 
	and obsLabResearch.value_coded = 307 and obsLabResearch.voided=0 
	)baciloscopia on (baciloscopia.patient_id = coorte12meses_final.patient_id and baciloscopia.data_pedido between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_pedido  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsLabResearch on obsLabResearch.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=6 and obsLabResearch.concept_id=23722 
	and obsLabResearch.value_coded = 23951 and obsLabResearch.voided=0 
	)tbLam on (tbLam.patient_id = coorte12meses_final.patient_id and tbLam.data_pedido between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_resultado,obsTestResult.value_coded  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=6 and obsTestResult.concept_id = 307 and obsTestResult.value_coded in (703, 664) and obsTestResult.voided=0
	)baciloscopiaResult on (baciloscopiaResult.patient_id = coorte12meses_final.patient_id and baciloscopiaResult.data_resultado between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=6 and obsTestResult.concept_id = 23951 and obsTestResult.value_coded in (703, 664) and obsTestResult.voided=0
	)tbLamResult on (tbLamResult.patient_id = coorte12meses_final.patient_id and tbLamResult.data_resultado between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 23723 
	and obsTestResult.value_coded in (703, 664) and obsTestResult.voided=0
	)gExpertLabResult on (gExpertLabResult.patient_id = coorte12meses_final.patient_id and gExpertLabResult.data_resultado between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_resultado,obsTestResult.value_coded  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165189 
	and obsTestResult.value_coded in (1065, 1066) and obsTestResult.voided=0
	)xpertLabResult on (xpertLabResult.patient_id = coorte12meses_final.patient_id and xpertLabResult.data_resultado between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 165192 
	and obsTestResult.value_coded in (1065, 1066, 1138) and obsTestResult.voided=0
	)rifampinResistanceLabResult on (rifampinResistanceLabResult.patient_id = coorte12meses_final.patient_id and rifampinResistanceLabResult.data_resultado between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 307 
	and obsTestResult.value_coded in (703, 165184) and obsTestResult.voided=0
	)baciloscopiaLabResult on (baciloscopiaLabResult.patient_id = coorte12meses_final.patient_id and baciloscopiaLabResult.data_resultado between positiveTbScreening.screeningDate and curdate())
	left join
	(
	select p.patient_id, e.encounter_datetime data_resultado, obsTestResult.value_coded  from patient p 
	inner join encounter e on e.patient_id=p.patient_id 
	inner join obs obsTestResult on obsTestResult.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and e.encounter_datetime between  :startDate and curdate() and  
	e.location_id=:location and e.encounter_type=13 and obsTestResult.concept_id = 23951 
	and obsTestResult.value_coded in (703, 664, 1138) and obsTestResult.voided=0
	)tbLamLabResult on (tbLamLabResult.patient_id = coorte12meses_final.patient_id and tbLamLabResult.data_resultado between positiveTbScreening.screeningDate and curdate())
	  left join  ( 
	select  pg.patient_id,date_enrolled data_inicio from  patient p  
	inner join patient_program pg on p.patient_id=pg.patient_id                         
	where pg.voided=0 and p.voided=0 and program_id=5 and date_enrolled between :startDate and  curdate()  and location_id=:location 
	union 
	select p.patient_id,obsTB.obs_datetime data_inicio from patient p  
	inner join encounter e on e.patient_id=p.patient_id  
	inner join obs obsTB on obsTB.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startDate and  curdate()  and   
	e.location_id=:location and e.encounter_type=53 and obsTB.concept_id=1406 and obsTB.value_coded =42 and obsTB.voided=0 
	union                 
	select p.patient_id, obsTB.obs_datetime data_inicio from patient p  
	inner join encounter e on e.patient_id=p.patient_id  
	inner join obs obsTB on obsTB.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startDate and  curdate()  and   
	e.location_id=:location and e.encounter_type=6 and obsTB.concept_id=1268 and obsTB.value_coded in (1256) and obsTB.voided=0  
	union 
	select p.patient_id,obsTB.obs_datetime data_inicio from patient p  
	inner join encounter e on e.patient_id=p.patient_id  
	inner join obs obsTB on obsTB.encounter_id=e.encounter_id 
	where p.voided=0 and e.voided=0 and obsTB.obs_datetime between :startDate and  curdate()  and   
	e.location_id=:location and e.encounter_type=6 and obsTB.concept_id=23761 and obsTB.value_coded =1065 and obsTB.voided=0  
	)tbTreatment on (coorte12meses_final.patient_id=tbTreatment.patient_id and tbTreatment.data_inicio between positiveTbScreening.screeningDate and curdate())
	where (data_estado is null or (data_estado is not null and  data_usar_c>data_estado)) and date_add(data_usar, interval 28 day) >=:endDate  
	group by coorte12meses_final.patient_id