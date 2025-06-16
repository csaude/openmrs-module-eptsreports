package org.openmrs.module.eptsreports.reporting.library.queries.data.quality;

public class Ec16Queries {

  /**
   * Get the query for EC16 patient listing
   *
   * @param programId
   * @param arvPediatriaSeguimentoEncounterType
   * @param adultoSeguimentoEncounterType
   * @return
   */
  public static String getEc16CombinedQuery(
      int programId, int arvPediatriaSeguimentoEncounterType, int adultoSeguimentoEncounterType) {
    String query =
        " SELECT patient_id, NID, Name, birthdate, Estimated_dob, Sex, First_entry_date, "
            + " Last_updated, date_enrolled,  MIN(encounter_date) AS encounter_date,"
            + " encounter_date_created, state, state_date, location_name FROM("
            + " SELECT pa.patient_id, pi.identifier AS NID, CONCAT(pn.given_name, ' ', pn.family_name ) AS Name,"
            + " DATE_FORMAT(pe.birthdate, '%d-%m-%Y') AS birthdate, IF(pe.birthdate_estimated = 1, 'Sim','Não') AS Estimated_dob,"
            + " pe.gender AS Sex, DATE_FORMAT(pa.date_created, '%d-%m-%Y') AS First_entry_date, DATE_FORMAT(pa.date_changed, '%d-%m-%Y') AS Last_updated, "
            + "DATE_FORMAT(pg.date_enrolled, '%d-%m-%Y') AS date_enrolled, DATE_FORMAT(e.encounter_datetime, '%d-%m-%Y') AS encounter_date, "
            + "DATE_FORMAT(e.date_created, '%d-%m-%Y') AS encounter_date_created,"
            + "case "
            + " when ps.state = 9 then 'ABANDONO' "
            + " when ps.state = 6 then 'ACTIVO NO PROGRAMA' "
            + " when ps.state = 10 then 'OBITOU' "
            + " when ps.state = 8 then 'SUSPENDER TRATAMENTO' "
            + " when ps.state = 7 then 'TRANSFERIDO PARA' "
            + " when ps.state = 29 then 'TRANSFERIDO DE' "
            + " end AS state, "
            + " DATE_FORMAT(ps.start_date, '%d-%m-%Y') AS state_date, "
            + "l.name AS location_name FROM patient pa "
            + " INNER JOIN patient_identifier pi ON pa.patient_id=pi.patient_id"
            + " INNER JOIN person pe ON pa.patient_id=pe.person_id"
            + " INNER JOIN person_name pn ON pa.patient_id=pn.person_id "
            + " INNER JOIN patient_program pg ON pa.patient_id=pg.patient_id "
            + "	LEFT JOIN ( "
            + " select max_estado.patient_id, max_estado.start_date, ps.state "
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
            + " INNER JOIN encounter e ON pa.patient_id=e.patient_id "
            + " INNER JOIN location l ON e.location_id=l.location_id"
            + " WHERE "
            + " pg.program_id="
            + programId
            + " AND e.voided=0 "
            + " AND e.encounter_type IN("
            + arvPediatriaSeguimentoEncounterType
            + ","
            + adultoSeguimentoEncounterType
            + ")"
            + " AND pe.birthdate IS NOT NULL"
            + " AND e.location_id IN(:location) AND pa.voided = 0 and e.voided=0 "
            + " AND pe.birthdate > e.encounter_datetime"
            + ")f_ec16 GROUP BY f_ec16.patient_id";
    return query;
  }
}
