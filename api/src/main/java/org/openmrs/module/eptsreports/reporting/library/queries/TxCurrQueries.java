/** */
package org.openmrs.module.eptsreports.reporting.library.queries;

/** @author Stélio Moiane */
public interface TxCurrQueries {

  public class QUERY {

    public enum DispensationIntervalType {
      MONTHLY(1),

      QUARTERLY(2),

      SEMI_ANNUAL(3);

      private Integer intervalValue;

      private DispensationIntervalType(Integer intervalValue) {
        this.intervalValue = intervalValue;
      }

      public Integer getIntervalValue() {
        return this.intervalValue;
      }
    }

    public static final String findPatientsByGenderAndRage =
        "SELECT patient_id FROM patient "
            + "INNER JOIN person ON patient_id = person_id WHERE patient.voided=0 AND person.voided=0 "
            + "AND TIMESTAMPDIFF(year,birthdate,:endDate) BETWEEN %d AND %d AND gender='%s' AND birthdate IS NOT NULL";

    public static final String findPatientsWhoAreInDispenseType(
        final DispensationIntervalType dispensationInterval) {
      String query =
          ""
              + "select patient_id 																														"
              + "from  																																	"
              + "( 																																		"
              + "	select *  																																"
              + "	from 																																	"
              + "	( 																																		"
              + "		select * 																															"
              + "		from  																																"
              + "		( 																																	"
              /* ==================FILA================== */
              + " 																																			"
              + "			select 	maxFila.patient_id, 																									"
              + "					maxFila.last_levantamento data_clinica, 																				"
              + "					if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento)<83,1, 												"
              + "						if(datediff(max(obsNext.value_datetime),maxFila.last_levantamento) BETWEEN 83 and 173,2,3)) tipoDispensa, 			"
              + "					1 as fonte, 																											"
              + "					1 as ordemMDS 																											"
              + "			from  																															"
              + "				(  																															"
              + "					SELECT p.patient_id, MAX(e.encounter_datetime) last_levantamento  														"
              + "					FROM 	patient p  																										"
              + "							INNER JOIN encounter e ON e.patient_id = p.patient_id  															"
              + "					WHERE 	p.voided = 0  AND e.voided = 0  AND e.encounter_type = 18  and  												"
              + "							e.location_id =:location and date(e.encounter_datetime) <=:endDate  											"
              + "					GROUP BY p.patient_id  																									"
              + "				) maxFila  																													"
              + "				inner join encounter e on e.patient_id=maxFila.patient_id  																	"
              + "				inner join obs obsNext on e.encounter_id=obsNext.encounter_id 																"
              + "			where 	date(e.encounter_datetime)=date(maxFila.last_levantamento) and  														"
              + "					e.encounter_type=18 and e.location_id=:location and e.voided=0 and obsNext.voided=0 and  								"
              + "					obsNext.concept_id=5096	 																								"
              + "			group  by maxFila.patient_id  																									"
              + " 																																			"
              + "			UNION  																															"
              + " 																																			"
              /* ====================TIPO DISPENSA=========== */
              + "			select 	lastTipo.patient_id, 																									"
              + "				lastTipo.data_clinica, 																										"
              + "				case obsTipo.value_coded			 																						"
              + "						when 1098 then 1 																									"
              + "						when 23720 then 2 																									"
              + "						when 23888 then 3			 																						"
              + "				else null end as tipoDispensa, 																								"
              + "				2 as fonte, 																												"
              + "				1 as ordemMDS 																												"
              + "			from  																															"
              + "			( 																																"
              + "				Select 	p.patient_id,max(e.encounter_datetime) data_clinica  																"
              + "				from 	patient p  																											"
              + "						inner join encounter e on p.patient_id=e.patient_id  																"
              + "						inner join obs o on o.encounter_id=e.encounter_id  																	"
              + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and  																		"
              + "						e.encounter_type =6 and o.concept_id=23739 and  																	"
              + "						e.encounter_datetime<=:endDate and e.location_id=:location  														"
              + "				group by p.patient_id  																										"
              + "			) lastTipo 																														"
              + "			inner join encounter e on e.patient_id=lastTipo.patient_id 																		"
              + "			inner join obs obsTipo on obsTipo.encounter_id=e.encounter_id 																	"
              + "			where 	lastTipo.data_clinica=e.encounter_datetime and  																		"
              + "					e.encounter_type=6 and e.voided=0 and obsTipo.voided=0 and  															"
              + "					e.location_id=:location and obsTipo.concept_id=23739 																	"
              + "		  																																	"
              + "			UNION 																															"
              + " 																																		"
              /* ======================MDS========================== */
              + "			select 	lastMDS.patient_id, 																									"
              + "					lastMDS.data_clinica, 																									"
              + "					case o.value_coded			 																							"
              + "						when 23730 then if(obsEstado.value_coded=1267,4,2) 																	"
              + "						when 23888 then if(obsEstado.value_coded=1267,4,3) 																	"
              + "						when 165314 then if(obsEstado.value_coded=1267,4,3) 			 													"
              + "					else 4 end as tipoDispensa, 																							"
              + "					3 as fonte, 																											"
              + "					case o.value_coded			 																							"
              + "						when 23730 then 1 																									"
              + "						when 23888 then 1 																									"
              + "						when 165314 then 1			 																						"
              + "					else 2 end as ordemMDS 																									"
              + "			from  																															"
              + "			( 																																"
              + "				Select 	p.patient_id,max(e.encounter_datetime) data_clinica  																"
              + "				from 	patient p  																											"
              + "						inner join encounter e on p.patient_id=e.patient_id  																"
              + "						inner join obs o on o.encounter_id=e.encounter_id  																	"
              + "				where 	e.voided=0 and o.voided=0 and p.voided=0 and  																		"
              + "						e.encounter_type =6 and o.concept_id=165174 and  																	"
              + "						e.encounter_datetime<=:endDate and e.location_id=:location  														"
              + "				group by p.patient_id  																										"
              + "			) lastMDS 																														"
              + "			inner join encounter e on lastMDS.patient_id=e.patient_id  																		"
              + "			inner join obs grupo on grupo.encounter_id=e.encounter_id  																		"
              + "			inner join obs o on o.encounter_id=e.encounter_id  																				"
              + "			inner join obs obsEstado on obsEstado.encounter_id=e.encounter_id  																"
              + "			where  	e.encounter_type=6 and e.location_id=:location and  																	"
              + "					o.concept_id=165174 and o.voided=0  																					"
              + "					and grupo.concept_id=165323  and grupo.voided=0  																		"
              + "					and obsEstado.concept_id=165322  and obsEstado.value_coded in(1256,1257,1267)  											"
              + "					and obsEstado.voided=0  and grupo.voided=0  																			"
              + "					and grupo.obs_id=o.obs_group_id and grupo.obs_id=obsEstado.obs_group_id   												"
              + "					and e.encounter_datetime=lastMDS.data_clinica 																			"
              + "		) allTipoSource 																													"
              + "		order by patient_id,data_clinica desc,fonte,ordemMDS 																				"
              + "	) allTipoSourcefirst 																													"
              + "	group by patient_id 																													"
              + ")finalDispensa 																																"
              + "where tipoDispensa = %s 																													";

      return String.format(query, dispensationInterval.getIntervalValue());
    }
  }
}
