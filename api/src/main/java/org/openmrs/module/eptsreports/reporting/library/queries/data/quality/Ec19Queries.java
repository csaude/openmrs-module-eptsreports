package org.openmrs.module.eptsreports.reporting.library.queries.data.quality;

public class Ec19Queries {

  /**
   * EC19 The date of laboratory test specimen collection date or results report date is before 1985
   *
   * @param programId
   * @param labEncounterType
   * @return
   */
  public static String getEc19CombinedQuery(
      int programId,
      int labEncounterType,
      int FSREncounterType,
      int masterCardEncounterType,
      int adultoSeguimentoEncounterType,
      int aRVPediatriaSeguimentoEncounterType,
      int year) {
    String query =
        "SELECT 	pe.person_id As patient_id, "
            + "		pid.identifier AS NID, "
            + "		concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) AS Name, "
            + "		DATE_FORMAT(pe.birthdate, '%d-%m-%Y') AS birthdate, "
            + "		IF(pe.birthdate_estimated = 1, 'Yes','No') AS Estimated_dob, "
            + "		pe.gender AS Sex, DATE_FORMAT(pe.date_created, '%d-%m-%Y') AS First_entry_date, "
            + "		DATE_FORMAT(pe.date_changed, '%d-%m-%Y') AS Last_updated, "
            + "		DATE_FORMAT(ps.date_enrolled, '%d-%m-%Y') AS date_enrolled, "
            + "		case "
            + " when ps.state = 9 then 'ABANDONO' "
            + " when ps.state = 6 then 'ACTIVO NO PROGRAMA' "
            + " when ps.state = 10 then 'OBITOU' "
            + " when ps.state = 8 then 'SUSPENDER TRATAMENTO' "
            + " when ps.state = 7 then 'TRANSFERIDO PARA' "
            + " when ps.state = 29 then 'TRANSFERIDO DE' "
            + "		end AS state, "
            + "		DATE_FORMAT(ps.start_date, '%d-%m-%Y') AS state_date, "
            + "		DATE_FORMAT(registo_laboratorio.encounter_date, '%d-%m-%Y') AS data_ultimo_formulario_laboratorio, "
            + "		DATE_FORMAT(registo_laboratorio.date_created, '%d-%m-%Y') AS data_registo_form_laboratorio, "
            + "		DATE_FORMAT(seguimento.encounter_date, '%d-%m-%Y') AS data_consulta, "
            + "		DATE_FORMAT(seguimento.date_created, '%d-%m-%Y') AS data_registo_consulta, "
            + "		DATE_FORMAT(pedido_colheita_fsr.encounter_date, '%d-%m-%Y') AS data_FSR, "
            + "		DATE_FORMAT(pedido_colheita_fsr.date_created, '%d-%m-%Y') AS data_registo_FSR, "
            + "      l.name as location_name "
            + "FROM "
            + "person pe "
            + "left join "
            + "( "
            + "	SELECT p.patient_id AS patient_id, e.encounter_datetime as encounter_date, e.date_created , l.name "
            + "	FROM patient p "
            + "	INNER JOIN encounter e ON p.patient_id = e.patient_id "
            + "	inner join location l on l.location_id = e.location_id "
            + "	WHERE p.voided = 0 AND e.voided = 0 "
            + "	AND e.encounter_type in ("
            + labEncounterType
            + ","
            + FSREncounterType
            + ")"
            + "	AND e.location_id IN (:location) "
            + "	AND e.encounter_datetime < '1985-01-01' "
            + ") registo_laboratorio on pe.person_id = registo_laboratorio.patient_id "
            + "left join "
            + "( "
            + "	SELECT p.patient_id AS patient_id, o.obs_datetime as encounter_date, e.date_created , l.name "
            + "	FROM patient p "
            + "	INNER JOIN encounter e ON p.patient_id = e.patient_id "
            + "		inner join location l on l.location_id = e.location_id "
            + "	INNER JOIN obs o ON e.encounter_id = o.encounter_id "
            + "	WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND o.concept_id in (23821,6246) "
            + "	AND e.encounter_type = "
            + labEncounterType
            + "	AND e.location_id IN (:location) "
            + "	AND o.obs_datetime < '1985-01-01' "
            + ") pedido_colheita_laboratorio on pe.person_id = pedido_colheita_laboratorio.patient_id "
            + "left join "
            + "( "
            + "	SELECT p.patient_id AS patient_id, o.obs_datetime as encounter_date, e.date_created, l.name "
            + "	FROM patient p "
            + "	INNER JOIN encounter e ON p.patient_id = e.patient_id "
            + "     inner join location l on l.location_id = e.location_id "
            + "	INNER JOIN obs o ON e.encounter_id = o.encounter_id "
            + "	WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 AND o.concept_id in (23826,23827) "
            + "	AND e.encounter_type = "
            + FSREncounterType
            + "	AND e.location_id IN (:location) "
            + "	AND o.obs_datetime < '1985-01-01' "
            + ") pedido_colheita_fsr on pe.person_id = pedido_colheita_laboratorio.patient_id "
            + "left join ( "
            + "	SELECT p.patient_id AS patient_id, e.encounter_datetime as encounter_date, e.date_created, l.name "
            + "	FROM patient p "
            + "	INNER JOIN encounter e ON p.patient_id = e.patient_id "
            + "	inner join location l on l.location_id = e.location_id "
            + "	INNER JOIN obs o ON e.encounter_id = o.encounter_id "
            + "	WHERE p.voided = 0 AND e.voided = 0 AND o.voided = 0 "
            + "	AND e.encounter_type in ("
            + adultoSeguimentoEncounterType
            + ","
            + aRVPediatriaSeguimentoEncounterType
            + ","
            + masterCardEncounterType
            + ") AND e.location_id IN (:location) "
            + "	and o.concept_id in (1695, 856, 1690, 1691, 1692, 1693, 857, 1299, 729, 730, 678, 1022, 1021, 1694, 887, 1011, 45, 1655) "
            + "	and o.obs_datetime < '1985-01-01' ) seguimento on pe.person_id = seguimento.patient_id "
            + "left join "
            + " (	select pn1.* "
            + "	from person_name pn1 "
            + "	inner join "
            + "	( "
            + "		select person_id, min(person_name_id) id "
            + "		from person_name "
            + "		where voided = 0 "
            + "		group by person_id "
            + "	) pn2 "
            + "	where pn1.person_id = pn2.person_id and pn1.person_name_id = pn2.id "
            + ") pn on pn.person_id = pe.person_id "
            + "left join "
            + "(   select pid1.* "
            + "	from patient_identifier pid1 "
            + "	inner join "
            + "	( "
            + "		select patient_id, min(patient_identifier_id) id "
            + "		from patient_identifier "
            + "		where voided = 0 "
            + "		group by patient_id "
            + "	) pid2 "
            + "	where pid1.patient_id = pid2.patient_id and pid1.patient_identifier_id = pid2.id "
            + ") pid on pid.patient_id = pe.person_id "
            + "	LEFT JOIN ( "
            + " select max_estado.patient_id, max_estado.start_date, ps.state, pp.date_enrolled, pp.location_id "
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
            + "  INNER JOIN location l on l.location_id = ps.location_id "
            + " where pe.voided = 0 "
            + " and (registo_laboratorio.encounter_date is not null "
            + " or pedido_colheita_laboratorio.encounter_date is not null or "
            + " pedido_colheita_fsr.encounter_date is not null or "
            + " seguimento.encounter_date is not null "
            + " ) "
            + " GROUP BY pe.person_id; ";
    return query;
  }
}
