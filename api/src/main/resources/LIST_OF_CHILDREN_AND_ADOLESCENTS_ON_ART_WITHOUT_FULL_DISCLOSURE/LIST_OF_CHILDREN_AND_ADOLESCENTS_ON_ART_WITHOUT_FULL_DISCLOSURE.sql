select 
  coorte12meses_final.patient_id as PATIENT_ID, 
  DATE(txNew.art_start_date) as INIT_ART, 
  pid.identifier as NID, 
  p.gender as GENDER, 
  if(p.birthdate is not null, floor(datediff(:endDate, p.birthdate)/ 365), 'N/A') AGE, 
  concat( ifnull(pn.given_name, ''), ' ', ifnull(pn.middle_name, ''), ' ', ifnull(pn.family_name, '')) as NAME, 
  if(ultimaRevelacao.estadoRevelacao is not null, ultimaRevelacao.estadoRevelacao, '') AS CODE, 
  if(revelacaoParcial.data_revelacao is not null, DATE_FORMAT(DATE(revelacaoParcial.data_revelacao),'%%d/%%m/%%Y'), 'N/A') as DATE_REVELATION, 
  if(revelacaoParcial.data_revelacao is not null, DATEDIFF(:endDate, revelacaoParcial.data_revelacao), 'N/A') as DIF_DAYS 
from 
  (
  %s
  ) coorte12meses_final 
  inner join person p on p.person_id = coorte12meses_final.patient_id 
  left join 
   (
   	SELECT * FROM 
			(SELECT patient_id, MIN(art_start_date) art_start_date FROM 
			(
			SELECT p.patient_id, MIN(e.encounter_datetime) art_start_date FROM patient p 
			INNER JOIN encounter e ON p.patient_id=e.patient_id 
			INNER JOIN obs o ON o.encounter_id=e.encounter_id 
			WHERE e.voided=0 AND o.voided=0 AND p.voided=0 AND e.encounter_type in (18,6,9) 
			AND o.concept_id=1255 AND o.value_coded=1256 AND e.encounter_datetime<=:endDate AND e.location_id=:location 
			GROUP BY p.patient_id 
			UNION 
			SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p INNER JOIN encounter e ON p.patient_id=e.patient_id 
			INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type IN (18,6,9,53) 
			AND o.concept_id=1190 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endDate AND e.location_id=:location 
			GROUP BY p.patient_id 
			UNION 
			SELECT pg.patient_id, MIN(date_enrolled) art_start_date FROM patient p 
			INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
			WHERE pg.voided=0 AND p.voided=0 AND program_id=2 AND date_enrolled<=:endDate AND location_id=:location 
			GROUP BY pg.patient_id 
			UNION SELECT e.patient_id, MIN(e.encounter_datetime) AS art_start_date FROM patient p 
			INNER JOIN encounter e ON p.patient_id=e.patient_id 
			WHERE p.voided=0 AND e.encounter_type=18 AND e.voided=0 AND e.encounter_datetime<=:endDate AND e.location_id=:location 
			GROUP BY p.patient_id 
			UNION 
			SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p 
			INNER JOIN encounter e ON p.patient_id=e.patient_id 
			INNER JOIN obs o ON e.encounter_id=o.encounter_id 
			WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=52 
			AND o.concept_id=23866 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endDate AND e.location_id=:location 
			GROUP BY p.patient_id 
			) 
			art_start 
			GROUP BY patient_id 
			) tx_new WHERE art_start_date <=:endDate 
  	)txNew on txNew.patient_id=coorte12meses_final.patient_id
  
  left join (
    select 
      pn1.* 
    from 
      person_name pn1 
      inner join (
        select 
          person_id, 
          max(person_name_id) id 
        from 
          person_name 
        where 
          voided = 0 
        group by 
          person_id
      ) pn2 
    where 
      pn1.person_id = pn2.person_id 
      and pn1.person_name_id = pn2.id
  ) pn on pn.person_id = coorte12meses_final.patient_id 
  left join (
    select 
      pid1.* 
    from 
      patient_identifier pid1 
      inner join (
        select 
          patient_id, 
          max(patient_identifier_id) id 
        from 
          patient_identifier 
        where 
          voided = 0 
        group by 
          patient_id
      ) pid2 
    where 
      pid1.patient_id = pid2.patient_id 
      and pid1.patient_identifier_id = pid2.id
  ) pid on pid.patient_id = coorte12meses_final.patient_id 
  left join (
    select 
      p.patient_id, 
      max(e.encounter_datetime) encounter_datetime 
    from 
      patient p 
      inner join encounter e on p.patient_id = e.patient_id 
      inner join obs o on e.encounter_id = o.encounter_id 
    where 
      p.voided = 0 
      and e.voided = 0 
      and o.voided = 0 
      and o.concept_id = 6340 
      and o.value_coded = 6337 
      and e.encounter_type = 35 
      and e.encounter_datetime <=:endDate 
      and e.location_id=:location 
    group by 
      p.patient_id
  ) revelado on revelado.patient_id = coorte12meses_final.patient_id 
  left join (
    select 
      maxApss.patient_id, 
      maxApss.encounter_datetime dataRevelado, 
      case obsRevelado.value_coded when 6339 then 'N' when 6338 then 'P' end as estadoRevelacao 
    from 
      (
        select 
          p.patient_id, 
          max(e.encounter_datetime) encounter_datetime 
        from 
          patient p 
          inner join encounter e on p.patient_id = e.patient_id 
        where 
          p.voided = 0 
          and e.voided = 0 
          and e.encounter_type = 35 
          and e.encounter_datetime <=:endDate 
          and e.location_id=:location 
        group by 
          p.patient_id
      ) maxApss 
      inner join encounter e on e.patient_id = maxApss.patient_id 
      inner join obs obsRevelado on obsRevelado.encounter_id = e.encounter_id 
    where 
      obsRevelado.voided = 0 
      and e.encounter_datetime = maxApss.encounter_datetime 
      and obsRevelado.concept_id = 6340 
      and e.location_id=:location 
      and e.encounter_type = 35 
      and obsRevelado.value_coded in (6338, 6339)
  ) ultimaRevelacao on ultimaRevelacao.patient_id = coorte12meses_final.patient_id 
  left join (
    select 
      p.patient_id, 
      min(e.encounter_datetime) data_revelacao 
    from 
      patient p 
      inner join encounter e on p.patient_id = e.patient_id 
      inner join obs o on e.encounter_id = o.encounter_id 
    where 
      p.voided = 0 
      and e.voided = 0 
      and o.voided = 0 
      and o.concept_id = 6340 
      and o.value_coded = 6338 
      and e.encounter_type = 35 
      and e.encounter_datetime <=:endDate 
      and e.location_id=:location 
    group by 
      p.patient_id
  ) revelacaoParcial on revelacaoParcial.patient_id = coorte12meses_final.patient_id 
where floor(datediff(:endDate, p.birthdate)/ 365) between 8 and 14 and revelado.patient_id is null
