package org.openmrs.module.eptsreports.reporting.library.queries.data.quality;

public class EC24Queries {

  public static String getEc24CombinedQuery() {
    String query =
        "SELECT "
            + " pe.person_id As patient_id, "
            + " pid.identifier AS NID, "
            + " concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) AS Name, "
            + " DATE_FORMAT(pe.birthdate, '%d-%m-%Y') AS birthdate, "
            + " IF(pe.birthdate_estimated = 1, 'Sim','Não') AS Estimated_dob, "
            + " pe.gender AS Sex, DATE_FORMAT(pe.date_created, '%d-%m-%Y') AS First_entry_date, "
            + " DATE_FORMAT(pe.date_changed, '%d-%m-%Y') AS Last_updated, "
            + " DATE_FORMAT(programState.date_enrolled, '%d-%m-%Y') AS date_enrolled, "
            + " case "
            + " when programState.state = 9 then 'DROPPED FROM TREATMENT' "
            + " when programState.state = 6 then 'ACTIVE ON PROGRAM' "
            + " when programState.state = 10 then 'PATIENT HAS DIED' "
            + " when programState.state = 8 then 'SUSPENDED TREATMENT' "
            + " when programState.state = 7 then 'TRANSFERED OUT TO ANOTHER FACILITY' "
            + " when programState.state = 29 then 'TRANSFERRED FROM OTHER FACILTY' "
            + " end AS state, "
            + " DATE_FORMAT(programState.start_date, '%d-%m-%Y') AS state_date, "
            + " l.name AS location_name, "
            + " DATE_FORMAT(fichaClinica.encounter_datetime, '%d-%m-%Y') AS ultimaFichaClinica, "
            + " DATE_FORMAT(fichaClinica.date_created, '%d-%m-%Y') AS dataRegistoFicha, "
            + " DATE_FORMAT(laboratorio.encounter_datetime, '%d-%m-%Y') AS lastLaboratorioDate, "
            + " DATE_FORMAT(fsr.encounter_datetime, '%d-%m-%Y') AS lastFsrDate "
            + " FROM "
            + " person pe "
            + " INNER JOIN "
            + " ( select pn1.* "
            + " from person_name pn1 "
            + " inner join "
            + " ( "
            + " select person_id, min(person_name_id) id "
            + " from person_name "
            + " where voided = 0 "
            + " group by person_id "
            + " ) pn2 "
            + " where pn1.person_id = pn2.person_id and pn1.person_name_id = pn2.id "
            + " ) pn on pn.person_id = pe.person_id "
            + " INNER JOIN "
            + " ( select pid1.* "
            + " from patient_identifier pid1 "
            + " inner join "
            + " ( "
            + " select patient_id, min(patient_identifier_id) id "
            + " from patient_identifier "
            + " where voided = 0 "
            + " group by patient_id "
            + " ) pid2 "
            + " where pid1.patient_id = pid2.patient_id and pid1.patient_identifier_id = pid2.id "
            + " ) pid on pid.patient_id = pe.person_id "
            + " INNER JOIN location l ON l.location_id = pid.location_id "
            + " LEFT JOIN (SELECT pg.patient_id, pg.date_enrolled, ps.state, max(ps.start_date) AS start_date "
            + " FROM patient_program pg "
            + " INNER JOIN patient_state ps ON pg.patient_program_id = ps.patient_program_id "
            + " AND ps.start_date IS NOT NULL "
            + " AND ps.end_date IS NULL "
            + " AND pg.program_id = 2 "
            + " AND pg.location_id =:location "
            + " GROUP BY pg.patient_id "
            + " ) AS programState ON pe.person_id = programState.patient_id "
            + "left join "
            + "( "
            + "select patient_id, max(encounter_datetime) encounter_datetime from( "
            + " select quantitativa.patient_id,quantitativa.encounter_datetime FROM ( "
            + "select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 51 and o.concept_id = 856 "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") quantitativa "
            + "inner join( "
            + " select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime  from patient p "
            + "inner join encounter e on e.patient_id = p.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 51 and o.concept_id = 1305 and o.value_coded is not null "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") qualitativa on quantitativa.patient_id = qualitativa.patient_id "
            + "where quantitativa.encounter_id = qualitativa.encounter_id "
            + ")fsr group by fsr.patient_id "
            + ")fsr on fsr.patient_id = pe.person_id "
            + "left join "
            + "( "
            + "select patient_id, max(encounter_datetime) encounter_datetime from( "
            + " select quantitativa.patient_id,quantitativa.encounter_datetime FROM ( "
            + "select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 13 and o.concept_id = 856 "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") quantitativa "
            + "inner join( "
            + " select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime  from patient p "
            + "inner join encounter e on e.patient_id = p.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 13 and o.concept_id = 1305 and o.value_coded is not null "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") qualitativa on quantitativa.patient_id = qualitativa.patient_id "
            + "where quantitativa.encounter_id = qualitativa.encounter_id "
            + ")laboratorio group by laboratorio.patient_id "
            + ")laboratorio on laboratorio.patient_id = pe.person_id "
            + "left join "
            + "( "
            + "select patient_id, max(encounter_datetime) encounter_datetime, date_created from( "
            + " select quantitativa.patient_id,quantitativa.encounter_datetime,quantitativa.date_created FROM ( "
            + "select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime, e.date_created from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and o.concept_id = 856 "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") quantitativa "
            + "inner join( "
            + " select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime, e.date_created  from patient p "
            + "inner join encounter e on e.patient_id = p.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and o.concept_id = 1305 and o.value_coded is not null "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") qualitativa on quantitativa.patient_id = qualitativa.patient_id "
            + "where quantitativa.encounter_id = qualitativa.encounter_id "
            + ")fichaClinica group by fichaClinica.patient_id "
            + ")fichaClinica on fichaClinica.patient_id = pe.person_id "
            + "where pe.voided = 0 "
            + "and (fsr.patient_id is not null or laboratorio.patient_id is not null or fichaClinica.patient_id is not null) ";
    return query;
  }

  public static String getEc24Total() {
    String query =
        "SELECT "
            + " pe.person_id As patient_id "
            + " FROM "
            + " person pe "
            + " INNER JOIN "
            + " ( select pn1.* "
            + " from person_name pn1 "
            + " inner join "
            + " ( "
            + " select person_id, min(person_name_id) id "
            + " from person_name "
            + " where voided = 0 "
            + " group by person_id "
            + " ) pn2 "
            + " where pn1.person_id = pn2.person_id and pn1.person_name_id = pn2.id "
            + " ) pn on pn.person_id = pe.person_id "
            + " INNER JOIN "
            + " ( select pid1.* "
            + " from patient_identifier pid1 "
            + " inner join "
            + " ( "
            + " select patient_id, min(patient_identifier_id) id "
            + " from patient_identifier "
            + " where voided = 0 "
            + " group by patient_id "
            + " ) pid2 "
            + " where pid1.patient_id = pid2.patient_id and pid1.patient_identifier_id = pid2.id "
            + " ) pid on pid.patient_id = pe.person_id "
            + " INNER JOIN location l ON l.location_id = pid.location_id "
            + " LEFT JOIN (SELECT pg.patient_id, pg.date_enrolled, ps.state, max(ps.start_date) AS start_date "
            + " FROM patient_program pg "
            + " INNER JOIN patient_state ps ON pg.patient_program_id = ps.patient_program_id "
            + " AND ps.start_date IS NOT NULL "
            + " AND ps.end_date IS NULL "
            + " AND pg.program_id = 2 "
            + " AND pg.location_id =:location "
            + " GROUP BY pg.patient_id "
            + " ) AS programState ON pe.person_id = programState.patient_id "
            + "left join "
            + "( "
            + "select patient_id, max(encounter_datetime) encounter_datetime from( "
            + " select quantitativa.patient_id,quantitativa.encounter_datetime FROM ( "
            + "select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 51 and o.concept_id = 856 "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") quantitativa "
            + "inner join( "
            + " select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime  from patient p "
            + "inner join encounter e on e.patient_id = p.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 51 and o.concept_id = 1305 and o.value_coded is not null "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") qualitativa on quantitativa.patient_id = qualitativa.patient_id "
            + "where quantitativa.encounter_id = qualitativa.encounter_id "
            + ")fsr group by fsr.patient_id "
            + ")fsr on fsr.patient_id = pe.person_id "
            + "left join "
            + "( "
            + "select patient_id, max(encounter_datetime) encounter_datetime from( "
            + " select quantitativa.patient_id,quantitativa.encounter_datetime FROM ( "
            + "select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 13 and o.concept_id = 856 "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") quantitativa "
            + "inner join( "
            + " select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime  from patient p "
            + "inner join encounter e on e.patient_id = p.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 13 and o.concept_id = 1305 and o.value_coded is not null "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") qualitativa on quantitativa.patient_id = qualitativa.patient_id "
            + "where quantitativa.encounter_id = qualitativa.encounter_id "
            + ")laboratorio group by laboratorio.patient_id "
            + ")laboratorio on laboratorio.patient_id = pe.person_id "
            + "left join "
            + "( "
            + "select patient_id, max(encounter_datetime) encounter_datetime, date_created from( "
            + " select quantitativa.patient_id,quantitativa.encounter_datetime,quantitativa.date_created FROM ( "
            + "select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime, e.date_created from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and o.concept_id = 856 "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") quantitativa "
            + "inner join( "
            + " select p.patient_id, e.encounter_id,max(e.encounter_datetime) encounter_datetime, e.date_created  from patient p "
            + "inner join encounter e on e.patient_id = p.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 6 and o.concept_id = 1305 and o.value_coded is not null "
            + "and e.location_id =:location and e.encounter_datetime <= curdate() "
            + "group by p.patient_id "
            + ") qualitativa on quantitativa.patient_id = qualitativa.patient_id "
            + "where quantitativa.encounter_id = qualitativa.encounter_id "
            + ")fichaClinica group by fichaClinica.patient_id "
            + ")fichaClinica on fichaClinica.patient_id = pe.person_id "
            + "where pe.voided = 0 "
            + "and (fsr.patient_id is not null or laboratorio.patient_id is not null or fichaClinica.patient_id is not null) ";
    return query;
  }
}