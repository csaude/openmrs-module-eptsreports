package org.openmrs.module.eptsreports.reporting.library.queries;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.openmrs.module.eptsreports.reporting.utils.EptsQuerysUtils;

public class TXTBQueries {

  private static final String TX_NEW =
      "TX_NEW/PATIENTS_WHO_ARE_NEWLY_ENROLLED_ON_ART_UNTIL_END_DATE.sql";

  private static final String FIND_PATIENTS_WHO_ARE_SCEENED_FOR_TB = "TXTB/TB_SCREENED.sql";

  private static final String FIND_PATIENTS_WHO_ARE_TB_POSITIVE_SCREENING =
      "TXTB/TB_POSITIVE_SCREENING.sql";

  private static final String FIND_PATIENTS_WHO_ARE_TRANSFERRED_OUT =
      "TRANSFERRED_OUT/FIND_PATIENTS_WHO_ARE_TRANSFERRED_OUT.sql";

  public static String inTBProgramWithinReportingPeriodAtLocation() {
    return "select pg.patient_id from patient p inner join patient_program pg on "
        + " p.patient_id=pg.patient_id where pg.voided=0 and "
        + " p.voided=0 and program_id=5 "
        + "and date_enrolled between :startDate and :endDate and location_id=:location ";
  }

  public static String dateObs(
      Integer questionId, List<Integer> encounterTypeIds, boolean startDate) {
    String sql =
        String.format(
            "select obs.person_id from obs inner join encounter on encounter.encounter_id = obs.encounter_id "
                + " where obs.concept_id = %s and encounter.encounter_type in(%s) and obs.location_id = :location and obs.voided=0 and ",
            questionId, StringUtils.join(encounterTypeIds, ","));
    if (startDate) {
      sql += "obs.value_datetime >= :startDate and obs.value_datetime <= :endDate";
    } else {
      sql += "obs.value_datetime <= :endDate";
    }
    return sql;
  }

  public static String dateObsByObsDateTimeClausule(
      Integer conceptQuestionId, Integer conceptAnswerId, Integer encounterId) {
    String sql =
        String.format(
            "select distinct obs.person_id from obs "
                + "    inner join encounter on encounter.encounter_id = obs.encounter_id "
                + "  where obs.concept_id =%s and obs.value_coded =%s and encounter.encounter_type =%s "
                + "  and obs.location_id =:location and obs.obs_datetime >=:startDate and obs.obs_datetime <=:endDate and obs.voided=0",
            conceptQuestionId, conceptAnswerId, encounterId);

    return sql;
  }

  public static String findPatientWhoAreNewEnrolledOnARTUntilEndDate() {

    return EptsQuerysUtils.loadQuery(TX_NEW);
  }

  public static String findPatientWhoAreScreenedForTB() {

    return EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_SCEENED_FOR_TB);
  }

  public static String findPatientWhoAreTBPositiveScreening() {

    return EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_TB_POSITIVE_SCREENING);
  }

  public static String findPatientWhoAreInTBTreatment() {

    return "select patient_id                                                                                                 "
        + "from(                                                                                                            "
        + "      select p.patient_id,o.value_datetime data_tratamento                                                       "
        + "      from patient p                                                                                             "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                  "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                                    "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                             "
        + "            and e.encounter_type in (6,9) and o.concept_id=1113 and e.location_id= :location                     "
        + "            and o.value_datetime between :startDate and :endDate                                                 "
        + "      union                                                                                                      "
        + "      select pg.patient_id,date_enrolled data_tratamento                                                         "
        + "      from patient p                                                                                             "
        + "            inner join patient_program pg on p.patient_id=pg.patient_id                                          "
        + "      where pg.voided=0 and p.voided=0 and program_id=5 and location_id= :location                               "
        + "            and date_enrolled between :startDate and :endDate                                                    "
        + "      union                                                                                                      "
        + "      select  p.patient_id,o.obs_datetime data_tratamento                                                        "
        + "      from patient p                                                                                             "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                  "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                                    "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                             "
        + "            and e.encounter_type=53 and o.concept_id=1406 and o.value_coded=42 and e.location_id= :location      "
        + "            and o.obs_datetime between :startDate and :endDate                                                   "
        + "      union                                                                                                      "
        + "      select p.patient_id,o.obs_datetime data_tratamento                                                         "
        + "      from patient p                                                                                             "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                  "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                                    "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                             "
        + "            and e.encounter_type=6 and o.concept_id=1268 and o.value_coded=1256 and e.location_id= :location     "
        + "            and o.obs_datetime between :startDate and :endDate                                                   "
        + ")TBTretment                                                                                                      ";
  }

  public static String findPatientWhoAreTransferedOut() {

    return EptsQuerysUtils.loadQuery(FIND_PATIENTS_WHO_ARE_TRANSFERRED_OUT);
  }

  public static String findPatientWhoAreSpecimenSent() {

    return "select specimenSent.patient_id                                                                            "
        + "from(                                                                                                     "
        + "      select p.patient_id,e.encounter_datetime data_inicio                                                "
        + "      from patient p                                                                                      "
        + "            inner join encounter e on p.patient_id=e.patient_id                                           "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                             "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                      "
        + "            and e.encounter_type in (13, 51) and o.concept_id in(23723, 165588, 23951, 307, 23774)        "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate      "
        + "      union                                                                                               "
        + "      select p.patient_id,e.encounter_datetime data_inicio                                                "
        + "      from patient p                                                                                      "
        + "            inner join encounter e on p.patient_id=e.patient_id                                           "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                             "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                      "
        + "            and e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23723, 23774, 23951, 307)  "
        + "            and e.location_id= :location and e.encounter_datetime   between :startDate and :endDate       "
        + "      union                                                                                               "
        + "      select p.patient_id,e.encounter_datetime data_inicio                                                "
        + "      from patient p                                                                                      "
        + "            inner join encounter e on p.patient_id=e.patient_id                                           "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                             "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                      "
        + "            and e.encounter_type=6 and o.concept_id in(23723, 23774, 23951, 307)                          "
        + "            and e.location_id= :location and e.encounter_datetime   between :startDate and :endDate       "
        + " )specimenSent                                                                                            ";
  }

  public static String findPatientWhoAreDiagnosticTestSmearMicroscopyOnly() {

    return "select diagnosticTest.patient_id                                                                    "
        + "from(                                                                                               "
        + "      select p.patient_id,e.encounter_datetime data_inicio                                          "
        + "      from patient p                                                                                "
        + "            inner join encounter e on p.patient_id=e.patient_id                                     "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                       "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                "
        + "            and e.encounter_type in(6) and o.concept_id=23722 and o.value_coded=307                 "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate "
        + "      union                                                                                          "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                          "
        + "      from patient p                                                                                 "
        + "            inner join encounter e on p.patient_id=e.patient_id                                      "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                        "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                 "
        + "            and e.encounter_type in(6,13) and o.concept_id=307 and o.value_coded is not null         "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate "
        + ")diagnosticTest                                                                                      ";
  }

  public static String findPatientWhoHaveTBLAMDiagnosticTest() {

    return "select diagnosticTest.patient_id                                                                    "
        + "from(                                                                                               "
        + "      select p.patient_id,e.encounter_datetime data_inicio                                          "
        + "      from patient p                                                                                "
        + "            inner join encounter e on p.patient_id=e.patient_id                                     "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                       "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                "
        + "            and e.encounter_type in(6) and o.concept_id=23722 and o.value_coded=23951                "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate "
        + "      union                                                                                          "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                          "
        + "      from patient p                                                                                 "
        + "            inner join encounter e on p.patient_id=e.patient_id                                      "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                        "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                 "
        + "            and e.encounter_type in(6,13, 51) and o.concept_id=23951 and o.value_coded is not null         "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate "
        + ")diagnosticTest                                                                                      ";
  }

  public static String findPatientWhoAreDiagnosticTestMWRD() {

    return "select diagnosticTest.patient_id 																			"
        + "from(                                                                                                     "
        + "		select  p.patient_id,e.encounter_datetime data_inicio                                               "
        + "		from patient p                                                                                      "
        + "      		inner join encounter e on p.patient_id=e.patient_id                                         "
        + "      		inner join obs o on o.encounter_id=e.encounter_id 											"
        + "		where e.voided=0 and o.voided=0 and p.voided=0                                                      "
        + "      		and e.encounter_type=6 and o.concept_id=23722 and o.value_coded=23723  						"
        + "      		and e.location_id= :location  and e.encounter_datetime between  :startDate and :endDate     "
        + "		union                                                                                               "
        + "		select p.patient_id,e.encounter_datetime data_inicio                                                "
        + "		from patient p                                                                                      "
        + "      		inner join encounter e on p.patient_id=e.patient_id                                         "
        + "      		inner join obs o on o.encounter_id=e.encounter_id                                           "
        + "		where e.voided=0 and o.voided=0 and p.voided=0                                                      "
        + "     			and e.encounter_type in(6,13) and o.concept_id=23723  and o.value_coded is not null		"
        + "      		and e.location_id= :location  and e.encounter_datetime between  :startDate and :endDate     "
        + "		union 																								"
        + "		select p.patient_id,e.encounter_datetime data_inicio                                                "
        + "		from patient p                                                                                      "
        + "      		inner join encounter e on p.patient_id=e.patient_id                                         "
        + "      		inner join obs o on o.encounter_id=e.encounter_id                                           "
        + "		where e.voided=0 and o.voided=0 and p.voided=0                                                      "
        + "      		and e.encounter_type =13 and o.concept_id=165588											"
        + "      		and e.location_id= :location  and e.encounter_datetime between  :startDate and :endDate 	"
        + ")diagnosticTest 																							";
  }

  public static String findPatientWhoAreDiagnosticTestOther() {

    return "select diagnosticTest.patient_id                                                                                "
        + "from(                                                                                                           "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                                     "
        + "      from patient p                                                                                            "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                 "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                                   "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                            "
        + "            and e.encounter_type=6 and o.concept_id=23722 and o.value_coded in(23774,23951)                     "
        + "            and e.location_id= :location  and e.encounter_datetime   between  :startDate and :endDate           "
        + "      union                                                                                                     "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                                     "
        + "      from patient p                                                                                            "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                 "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                                   "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                            "
        + "            and e.encounter_type in (6,13) and o.concept_id in(23774,23951)                                     "
        + "            and e.location_id= :location  and e.encounter_datetime   between  :startDate and :endDate           "
        + ")diagnosticTest                                                                                                 ";
  }

  public static String findPatientWhoAreDiagnosticTestOtherCulture() {

    return "select diagnosticTest.patient_id                                                                                "
        + "from(                                                                                                           "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                                     "
        + "      from patient p                                                                                            "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                 "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                                   "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                            "
        + "            and e.encounter_type=6 and o.concept_id=23722 and o.value_coded = 23774                             "
        + "            and e.location_id= :location  and e.encounter_datetime   between  :startDate and :endDate           "
        + "      union                                                                                                     "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                                     "
        + "      from patient p                                                                                            "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                 "
        + "            inner join obs o on o.encounter_id=e.encounter_id                                                   "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                            "
        + "            and e.encounter_type in (6,13) and o.concept_id = 23774  and o.value_coded is not null              "
        + "            and e.location_id= :location  and e.encounter_datetime   between  :startDate and :endDate           "
        + ")diagnosticTest                                                                                                 ";
  }

  public static String findPatientWhoArePosiveResult() {

    return "select diagnosticTest.patient_id                                                               "
        + "from(                                                                                         "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                                       "
        + "      from patient p                                                                                                    "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                          "
        + "            inner join obs o on o.encounter_id=e.encounter_id "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                              "
        + "            and e.encounter_type=6 and o.concept_id in(23723,23774,23951,307) and o.value_coded in(703) "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate "
        + "      union                                                                                                     "
        + "      select p.patient_id,e.encounter_datetime data_inicio                                                       "
        + "      from patient p                                                                                                    "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                          "
        + "            inner join obs o on o.encounter_id=e.encounter_id "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                              "
        + "            and e.encounter_type=13 and o.concept_id in(307,23723,23774,23951) and o.value_coded in(703) "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate "
        + "      union                                                                                                       "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                                       "
        + "      from patient p                                                                                                    "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                          "
        + "            inner join obs o on o.encounter_id=e.encounter_id "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                              "
        + "            and e.encounter_type=13 and o.concept_id=165588 and o.value_coded in(703,165190,6230,6229,6228,165587) "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate "
        + "      union                                                                                                         "
        + "      select  p.patient_id,e.encounter_datetime data_inicio                                                       "
        + "      from patient p                                                                                                    "
        + "            inner join encounter e on p.patient_id=e.patient_id                                                          "
        + "            inner join obs o on o.encounter_id=e.encounter_id "
        + "      where e.voided=0 and o.voided=0 and p.voided=0                                                              "
        + "            and e.encounter_type=51 and o.concept_id=23951 and o.value_coded=703 "
        + "            and e.location_id= :location  and e.encounter_datetime   between :startDate and :endDate            "
        + ")diagnosticTest                                                                                                 ";
  }

  public static String findPatientWhoAreCXR() {
    return "select diagnosticTest.patient_id  										"
        + "from( 																	"
        + "		select p.patient_id,e.encounter_datetime data_inicio                "
        + "		from patient p                                                      "
        + "      		inner join encounter e on p.patient_id=e.patient_id         "
        + "      		inner join obs o on o.encounter_id=e.encounter_id 			"
        + "		where e.voided=0 and o.voided=0 and p.voided=0                      "
        + "      		and e.encounter_type in(6) and o.concept_id=12 and o.value_coded in(23956, 664,1138)   "
        + "      		and e.location_id= :location  and e.encounter_datetime between :startDate and :endDate "
        + ")diagnosticTest 															"
        + "	inner join person on person.person_id = diagnosticTest.patient_id 		"
        + "where person.birthdate is not null and  floor(datediff(:endDate,person.birthdate )/365) >= 10 ";
  }

  public static String dateObsForEncounterAndQuestionAndAnswers(
      Integer encounterId, List<Integer> questionConceptIds, List<Integer> answerConceptIds) {
    String sql =
        String.format(
            "select distinct obs.person_id from obs "
                + "	 inner join encounter on encounter.encounter_id = obs.encounter_id where encounter.encounter_type = %s "
                + "  and obs.concept_id in (%s) and obs.value_coded in (%s) and encounter.voided = 0 and obs.voided =0 and obs.location_id =:location "
                + "  and encounter.encounter_datetime >=:startDate and encounter.encounter_datetime <=:endDate",
            encounterId,
            StringUtils.join(questionConceptIds, ","),
            StringUtils.join(answerConceptIds, ","));

    return sql;
  }
}
