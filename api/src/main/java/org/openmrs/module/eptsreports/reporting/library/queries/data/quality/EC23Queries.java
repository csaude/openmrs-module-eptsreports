package org.openmrs.module.eptsreports.reporting.library.queries.data.quality;

public class EC23Queries {

  public static String getEc23CombinedQuery() {
    String query =
        "SELECT  pe.person_id as patient_id, "
            + "pid.identifier AS NID, "
            + "concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) AS Name, "
            + "DATE_FORMAT(pe.birthdate, '%d-%m-%Y') AS birthdate, "
            + "IF(pe.birthdate_estimated = 1, 'Sim','Não') AS Estimated_dob, "
            + "pe.gender AS Sex, "
            + "DATE_FORMAT(pe.date_created, '%d-%m-%Y') AS First_entry_date, "
            + "DATE_FORMAT(pe.date_changed, '%d-%m-%Y') AS Last_updated, "
            + "DATE_FORMAT(ultimaMestruacao.value_datetime, '%d-%m-%Y') AS dataUltimaMestruacao, "
            + "DATE_FORMAT(ultimaMestruacao.encounter_datetime, '%d-%m-%Y') AS encounter_datetime, "
            + "DATE_FORMAT(ultimaMestruacao.date_created, '%d-%m-%Y') AS date_created, "
            + "DATE_FORMAT(pg.date_enrolled, '%d-%m-%Y') AS date_enrolled, "
            + "case "
            + " when ps.state = 9 then 'ABANDONO' "
            + " when ps.state = 6 then 'ACTIVO NO PROGRAMA' "
            + " when ps.state = 10 then 'OBITOU' "
            + " when ps.state = 8 then 'SUSPENDER TRATAMENTO' "
            + " when ps.state = 7 then 'TRANSFERIDO PARA' "
            + " when ps.state = 29 then 'TRANSFERIDO DE' "
            + "end AS state, DATE_FORMAT(ps.start_date, '%d-%m-%Y') as ps_start_date, "
            + "l.name AS location_name "
            + "FROM  person pe "
            + "left join ( "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate and :endDate  and e.location_id IN(:location) "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate  and e.location_id IN(:location) "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate  and e.location_id IN(:location) "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate  and e.location_id IN(:location) "
            + "union "
            + "select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp "
            + "where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and :endDate and pp.location_id IN(:location) "
            + "union "
            + "Select p.patient_id,obsART.value_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and "
            + "e.encounter_type=53 and obsART.value_datetime between :startDate and :endDate  and e.location_id IN(:location) and obsART.concept_id=1190 and obsART.voided=0 "
            + ") gravida_real on gravida_real.patient_id=pe.person_id "
            + "left join ( "
            + "Select p.patient_id,o.value_datetime value_datetime, e.encounter_datetime encounter_datetime, e.date_created date_created from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1465 and e.encounter_type=6 and o.value_datetime between :startDate and :endDate and e.location_id IN(:location) "
            + ")ultimaMestruacao on ultimaMestruacao.patient_id=pe.person_id "
            + "left join ( "
            + "select pid1.* from patient_identifier pid1 "
            + "inner join ( "
            + "select patient_id,min(patient_identifier_id) id from patient_identifier "
            + "where voided=0 group by patient_id "
            + ") pid2 "
            + "where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id "
            + ") pid on pid.patient_id=pe.person_id "
            + "left join (select pn1.* from person_name pn1 "
            + "inner join ( "
            + "select person_id,min(person_name_id) id from person_name where voided=0 "
            + "group by person_id "
            + ") pn2 "
            + "where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id "
            + ") pn on pn.person_id=pe.person_id "
            + "left join location l on pid.location_id=l.location_id "
            + " LEFT  JOIN patient_program pg ON ultimaMestruacao.patient_id = pg.patient_id AND pg.program_id = 2 "
            + "	LEFT JOIN ( "
            + " select max_estado.patient_id, max_estado.start_date, ps.state, pp.date_enrolled "
            + "	from( "
            + "			select pp.patient_id, max(max_estado.start_date) start_date,ps.patient_state_id,ps.state "
            + "			from( "
            + "				select pg.patient_id, ps.start_date,ps.voided,ps.state "
            + "				from patient p "
            + "						inner join patient_program pg on p.patient_id = pg.patient_id "
            + "				  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id "
            + "				where pg.voided=0  and p.voided=0 and  pg.program_id = 2 and ps.voided=0 "
            + "					and ps.start_date  <= :endDate  and pg.location_id=:location "
            + "                    order by pg.patient_id, ps.patient_state_id desc, ps.start_date desc "
            + "			)max_estado "
            + "				inner join patient_program pp on pp.patient_id = max_estado.patient_id "
            + "			 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.start_date and max_estado.state=ps.state "
            + "	  		where pp.program_id = 2 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location "
            + "			group by pp.patient_id "
            + "	) max_estado "
            + "	inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id "
            + "	inner join patient_program pp on pp.patient_program_id = ps.patient_program_id "
            + "	) AS ps ON pe.person_id = ps.patient_id "
            + "where pe.voided = 0 and pe.gender='F' and ultimaMestruacao.patient_id is not null and gravida_real.patient_id is null "
            + "GROUP BY pe.person_id ";
    return query;
  }

  public static String getEc23Total() {
    String query =
        "SELECT  pe.person_id as patient_id "
            + "FROM  person pe "
            + "left join ( "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and e.encounter_type in (5,6) and e.encounter_datetime  between :startDate and :endDate  and e.location_id IN(:location) "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate  and e.location_id IN(:location) "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate  and e.location_id IN(:location) "
            + "union "
            + "Select p.patient_id,e.encounter_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and e.encounter_type in (5,6) and e.encounter_datetime between :startDate and :endDate  and e.location_id IN(:location) "
            + "union "
            + "select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp "
            + "where pp.program_id=8 and pp.voided=0 and pp.date_enrolled between :startDate and :endDate and pp.location_id IN(:location) "
            + "union "
            + "Select p.patient_id,obsART.value_datetime data_gravida from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "inner join obs obsART on e.encounter_id=obsART.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and "
            + "e.encounter_type=53 and obsART.value_datetime between :startDate and :endDate  and e.location_id IN(:location) and obsART.concept_id=1190 and obsART.voided=0 "
            + ") gravida_real on gravida_real.patient_id=pe.person_id "
            + "left join ( "
            + "Select p.patient_id,o.value_datetime value_datetime, e.encounter_datetime encounter_datetime, e.date_created date_created from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id "
            + "inner join obs o on e.encounter_id=o.encounter_id "
            + "where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1465 and e.encounter_type=6 and o.value_datetime between :startDate and :endDate and e.location_id IN(:location) "
            + ")ultimaMestruacao on ultimaMestruacao.patient_id=pe.person_id "
            + "left join ( "
            + "select pid1.* from patient_identifier pid1 "
            + "inner join ( "
            + "select patient_id,min(patient_identifier_id) id from patient_identifier "
            + "where voided=0 group by patient_id "
            + ") pid2 "
            + "where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id "
            + ") pid on pid.patient_id=pe.person_id "
            + "left join (select pn1.* from person_name pn1 "
            + "inner join ( "
            + "select person_id,min(person_name_id) id from person_name where voided=0 "
            + "group by person_id "
            + ") pn2 "
            + "where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id "
            + ") pn on pn.person_id=pe.person_id "
            + "left join location l on pid.location_id=l.location_id "
            + " LEFT  JOIN patient_program pg ON ultimaMestruacao.patient_id = pg.patient_id AND pg.program_id = 2 "
            + "	LEFT JOIN ( "
            + " select max_estado.patient_id, max_estado.start_date, ps.state, pp.date_enrolled "
            + "	from( "
            + "			select pp.patient_id, max(max_estado.start_date) start_date,ps.patient_state_id,ps.state "
            + "			from( "
            + "				select pg.patient_id, ps.start_date,ps.voided,ps.state "
            + "				from patient p "
            + "						inner join patient_program pg on p.patient_id = pg.patient_id "
            + "				  	inner join patient_state ps on pg.patient_program_id = ps.patient_program_id "
            + "				where pg.voided=0  and p.voided=0 and  pg.program_id = 2 and ps.voided=0 "
            + "					and ps.start_date  <= :endDate  and pg.location_id=:location "
            + "                    order by pg.patient_id, ps.patient_state_id desc, ps.start_date desc "
            + "			)max_estado "
            + "				inner join patient_program pp on pp.patient_id = max_estado.patient_id "
            + "			 	inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.start_date and max_estado.state=ps.state "
            + "	  		where pp.program_id = 2 and pp.voided = 0 and ps.voided = 0 and pp.location_id=:location "
            + "			group by pp.patient_id "
            + "	) max_estado "
            + "	inner join patient_state ps on ps.patient_state_id =max_estado.patient_state_id "
            + "	inner join patient_program pp on pp.patient_program_id = ps.patient_program_id "
            + "	) AS ps ON pe.person_id = ps.patient_id "
            + "where pe.voided = 0 and pe.gender='F' and ultimaMestruacao.patient_id is not null and gravida_real.patient_id is null "
            + "GROUP BY pe.person_id ";
    return query;
  }
}
