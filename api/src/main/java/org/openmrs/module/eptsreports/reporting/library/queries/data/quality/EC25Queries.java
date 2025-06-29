package org.openmrs.module.eptsreports.reporting.library.queries.data.quality;

public class EC25Queries {

  public static String getEc25CombinedQuery() {
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
            + " when programState.state = 9 then 'ABANDONO' "
            + " when programState.state = 6 then 'ACTIVO NO PROGRAMA' "
            + " when programState.state = 10 then 'OBITOU' "
            + " when programState.state = 8 then 'SUSPENDER TRATAMENTO' "
            + " when programState.state = 7 then 'TRANSFERIDO PARA' "
            + " when programState.state = 29 then 'TRANSFERIDO DE' "
            + " end AS state, "
            + " DATE_FORMAT(programState.start_date, '%d-%m-%Y') AS state_date, "
            + " l.name AS location_name, "
            + " min(DATE_FORMAT(fila.encounter_datetime, '%d-%m-%Y')) AS firstFilaDate, "
            + "min(DATE_FORMAT(recepcao.dataLevantamento, '%d-%m-%Y')) AS firstdataLevantamento, "
            + "DATE_FORMAT(recepcao.dataRegisto, '%d-%m-%Y') AS dataRegistoRecepcao "
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
            + "	) AS programState ON pe.person_id = programState.patient_id "
            + "inner join ( "
            + "select patient_id, min(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "where p.voided = 0  and e.voided = 0 and e.encounter_type = 18 "
            + "and e.location_id =:location and e.encounter_datetime "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id, min(o.value_datetime) value_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 52 "
            + "and o.concept_id = 23866 and e.location_id =:location "
            + "group by p.patient_id "
            + ")levantamento group by levantamento.patient_id "
            + ") levantamento on levantamento.patient_id = pe.person_id "
            + "inner join "
            + "( "
            + "select pg.patient_id,min(date_enrolled) data_inicio  from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "where pg.voided=0 and p.voided=0 and program_id=2 "
            + "and location_id=:location "
            + "group by pg.patient_id ) pg on pg.patient_id = pe.person_id "
            + "left join "
            + "( "
            + "select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "where p.voided = 0  and e.voided = 0 and e.encounter_type = 18 "
            + "and e.location_id =:location "
            + "group by p.patient_id "
            + ") fila on fila.patient_id = pe.person_id "
            + "left join "
            + "( "
            + "select p.patient_id, min(o.value_datetime) dataLevantamento, e.encounter_datetime dataRegisto from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 52 "
            + "and o.concept_id = 23866 and e.location_id =:location "
            + "group by p.patient_id "
            + ")recepcao on recepcao.patient_id = pe.person_id "
            + "where pe.voided = 0 and (fila.patient_id is not null or recepcao.patient_id is not null) and "
            + "levantamento.encounter_datetime < pg.data_inicio "
            + " group by pe.person_id ";
    return query;
  }

  public static String getEc25Total() {
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
            + "	) AS programState ON pe.person_id = programState.patient_id "
            + "inner join ( "
            + "select patient_id, min(encounter_datetime) encounter_datetime from ( "
            + "select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "where p.voided = 0  and e.voided = 0 and e.encounter_type = 18 "
            + "and e.location_id =:location and e.encounter_datetime "
            + "group by p.patient_id "
            + "union "
            + "select p.patient_id, min(o.value_datetime) value_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 52 "
            + "and o.concept_id = 23866 and e.location_id =:location "
            + "group by p.patient_id "
            + ")levantamento group by levantamento.patient_id "
            + ") levantamento on levantamento.patient_id = pe.person_id "
            + "inner join "
            + "( "
            + "select pg.patient_id,min(date_enrolled) data_inicio  from patient p "
            + "inner join patient_program pg on p.patient_id=pg.patient_id "
            + "where pg.voided=0 and p.voided=0 and program_id=2 "
            + "and location_id=:location "
            + "group by pg.patient_id ) pg on pg.patient_id = pe.person_id "
            + "left join "
            + "( "
            + "select p.patient_id, min(e.encounter_datetime) encounter_datetime from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "where p.voided = 0  and e.voided = 0 and e.encounter_type = 18 "
            + "and e.location_id =:location "
            + "group by p.patient_id "
            + ") fila on fila.patient_id = pe.person_id "
            + "left join "
            + "( "
            + "select p.patient_id, min(o.value_datetime) dataLevantamento, e.encounter_datetime dataRegisto from patient p "
            + "	inner join encounter e on e.patient_id = p.patient_id "
            + "	inner join obs o on o.encounter_id = e.encounter_id "
            + "where p.voided = 0  and e.voided = 0 and o.voided = 0 and e.encounter_type = 52 "
            + "and o.concept_id = 23866 and e.location_id =:location "
            + "group by p.patient_id "
            + ")recepcao on recepcao.patient_id = pe.person_id "
            + "where pe.voided = 0 and (fila.patient_id is not null or recepcao.patient_id is not null) and "
            + "levantamento.encounter_datetime < pg.data_inicio "
            + " group by pe.person_id ";
    return query;
  }
}
