select coorte12meses_final.patient_id as patient_id,
                    concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NomeCompleto, 
                    pid.identifier as NID, 
                    case p.gender when 'F' then 'Feminino' when 'M' then 'Masculino' else null end as gender, 
                    floor(datediff(:endDate,p.birthdate)/365) as idade_actual,
                    pat.value telefone,
                    coorte12meses_final.data_inicio as data_inicio, 
                    maxEnc.encounter_datetime lastClinicalConsultationDate,
                    positiveTbScreening.screeningDate firstPositiveTbScreening,
                    max(genexpertTest.data_pedido) genExpertRequestDate,
                    max(geneXpertResult.data_resultado) lastGenExpertResultDate,
                    if(geneXpertResult.value_coded = 703, 'Positivo',if(geneXpertResult.value_coded = 664, 'Negativo','')) as genExpertResult, 
                    max(gExpertLabResult.data_resultado) gExpertLabResultDate,
                    if(gExpertLabResult.value_coded = 703, 'Positivo',if(gExpertLabResult.value_coded = 664, 'Negativo','')) as gExpertLabResult, 
                    max(xpertLabResult.data_resultado) xpertLabResultDate,
                    if(xpertLabResult.value_coded = 1065, 'Sim',if(xpertLabResult.value_coded = 1066, 'Não','')) as xpertLabResult, 
                    max(rifampinResistanceLabResult.data_resultado) rifampinLabResultDate,
                    if(rifampinResistanceLabResult.value_coded = 1065, 'Sim',if(rifampinResistanceLabResult.value_coded = 1066, 'Não',if(rifampinResistanceLabResult.value_coded = 1138, 'Indeterminado',''))) as rifampinResistanceLabResult, 
                    max(baciloscopia.data_pedido) lastBKResquestDate,
                    max(baciloscopiaResult.data_resultado) lastBKResultDate,
                    if(baciloscopiaResult.value_coded = 703, 'Positivo',if(baciloscopiaResult.value_coded = 664, 'Negativo','')) as baciloscopiaResult, 
                    max(baciloscopiaLabResult.data_resultado) lastBKLabResultDate,
                    if(baciloscopiaLabResult.value_coded = 703, 'Positivo',if(baciloscopiaLabResult.value_coded = 165184, 'Não encontrado','')) as baciloscopiaLabResult, 
                    max(tbLam.data_pedido) lastTbLamRequestDate,
                    max(tbLamResult.data_resultado) lastTbLamResultDate,
                    if(tbLamResult.value_coded = 703, 'Positivo',if(tbLamResult.value_coded = 664, 'Negativo','')) as tbLamResult, 
                    max(tbLamLabResult.data_resultado) lastTbLamLabResultDate,
                    if(tbLamLabResult.value_coded = 703, 'Positivo',if(tbLamLabResult.value_coded = 664, 'Negativo',if(tbLamLabResult.value_coded = 1138, 'Indeterminado',''))) as tbLamLabResult, 
                    max(tbTreatment.data_inicio) tbTreatmentInitialDate   
            from 
            (
             %s               
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
    inner join 
    (
    select patient_id, min(data_inicio) screeningDate from 
    (
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
    ) positiveScreening 
     group by patient_id
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
      left join  
    ( 
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

    left join 
    (
      Select p.patient_id, o.value_datetime data_tratamento from  patient p  
      inner join encounter e on p.patient_id=e.patient_id  
      inner join obs o on e.encounter_id=o.encounter_id  
      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in(6,9)
      and o.value_datetime between DATE_SUB(:startDate, INTERVAL 6 MONTH) and :startDate
      and o.concept_id=1113 and  e.location_id=:location    

      union

      select  pg.patient_id, pg.date_enrolled  data_tratamento                            
      from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
      where   pg.voided=0 and p.voided=0 and program_id=5 and location_id=:location 
      and pg.date_enrolled between  DATE_SUB(:startDate, INTERVAL 6 MONTH) and :startDate    

      union

      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
      inner join encounter e on p.patient_id=e.patient_id  
      inner join obs o on e.encounter_id=o.encounter_id  
      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 
      and o.obs_datetime between DATE_SUB(:startDate, INTERVAL 6 MONTH) and :startDate
      and o.concept_id=1406 and o.value_coded=42 and e.location_id=:location    

      union 

      Select p.patient_id,o.obs_datetime data_tratamento from  patient p  
      inner join encounter e on p.patient_id=e.patient_id  
      inner join obs o on e.encounter_id=o.encounter_id  
      where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 
      and o.obs_datetime between DATE_SUB(:startDate, INTERVAL 6 MONTH) and :startDate
      and o.concept_id=1268 and o.value_coded=1256  and e.location_id=:location    

    )tbTretment6Months on tbTretment6Months.patient_id=coorte12meses_final.patient_id
    where  tbTretment6Months.patient_id is null
    group by coorte12meses_final.patient_id