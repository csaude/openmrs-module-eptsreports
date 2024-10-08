/** */
package org.openmrs.module.eptsreports.reporting.library.queries;

public interface ResumoTrimestralAPSSQueries {

  public class QUERY {

    public static final String findPatientsReceivedTotalDiagnosticRevelationInReportingPeriod =
        "select totalDiagnosticoNoPeriodo.patient_id from "
            + "("
            + "select patient.patient_id from patient "
            + "join encounter on encounter.patient_id = patient.patient_id "
            + "join obs on obs.encounter_id = encounter.encounter_id "
            + "where encounter.encounter_type =35 and obs.concept_id = 6340 and obs.value_coded =6337 "
            + "and patient.voided =0 and encounter.voided = 0 and obs.voided = 0 and encounter.encounter_datetime >= :startDate and encounter.encounter_datetime <= :endDate "
            + "and encounter.location_id =:location "
            + "group by encounter.patient_id "
            + ") totalDiagnosticoNoPeriodo "
            + "left join "
            + "( "
            + "select patient.patient_id from patient "
            + "join encounter on encounter.patient_id = patient.patient_id "
            + "join obs on obs.encounter_id = encounter.encounter_id "
            + "where encounter.encounter_type =35 and obs.concept_id = 6340 and obs.value_coded =6337 "
            + "and patient.voided =0 and encounter.voided = 0 and obs.voided = 0 and encounter.encounter_datetime < :startDate "
            + "and encounter.location_id = :location "
            + "group by encounter.patient_id "
            + ") totalDiagnosticoAntesPeriodo on totalDiagnosticoAntesPeriodo.patient_id = totalDiagnosticoNoPeriodo.patient_id "
            + "where totalDiagnosticoAntesPeriodo.patient_id is null ";

    public static final String findPatientsRegisteredInAPSSPPAconselhamentoWithinReportingPeriod =
        "select patient.patient_id from patient "
            + "join encounter on encounter.patient_id = patient.patient_id "
            + "join obs on obs.encounter_id = encounter.encounter_id "
            + "where encounter.encounter_type =35 and obs.concept_id = 23886 and obs.value_coded =1065 "
            + "and patient.voided =0 and encounter.voided = 0 and obs.voided = 0 "
            + "and encounter.encounter_datetime >= :startDate and encounter.encounter_datetime <= :endDate "
            + "and encounter.location_id =:location "
            + "group by patient.patient_id ";

    public static final String findPatientsWithSeguimentoDeAdesao =
        "    select f.patient_id from ( "
            + "             select patient_id, min(art_start_date) art_start_date from "
            + "             (   select p.patient_id, min(e.encounter_datetime) art_start_date from patient p "
            + "                     join encounter e on p.patient_id=e.patient_id "
            + "                     join obs o on o.encounter_id=e.encounter_id "
            + "                 where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (18,6,9) "
            + "                     and o.concept_id=1255 and o.value_coded=1256 and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "                     group by p.patient_id "
            + "                 union "
            + "                 select p.patient_id, min(value_datetime) art_start_date from patient p "
            + "                     join encounter e on p.patient_id=e.patient_id "
            + "                     join obs o on e.encounter_id=o.encounter_id "
            + "                 where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) "
            + "                     and o.concept_id=1190 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location "
            + "                     group by p.patient_id "
            + "                 union "
            + "                 select pg.patient_id, min(date_enrolled) art_start_date from patient p "
            + "                     join patient_program pg on p.patient_id=pg.patient_id "
            + "                 where pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location "
            + "                 group by pg.patient_id "
            + "                 union "
            + "                 select e.patient_id, min(e.encounter_datetime) as art_start_date from patient p "
            + "                     join encounter e on p.patient_id=e.patient_id "
            + "                 where p.voided=0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location "
            + "                 group by p.patient_id "
            + "                 union "
            + "                 select p.patient_id, min(value_datetime) art_start_date from patient p "
            + "                     join encounter e on p.patient_id=e.patient_id "
            + "                     join obs o on e.encounter_id=o.encounter_id "
            + "                 where p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 "
            + "                     and o.concept_id=23866 and o.value_datetime is not null and o.value_datetime<=:endDate and e.location_id=:location "
            + "                 group by p.patient_id "
            + "             ) art_start "
            + "             group by patient_id "
            + "             )f "
            + "             inner join "
            + "             ( "
            + "               select * from "
            + "                ( "
            + "                         select patient.patient_id, encounter.encounter_datetime,encounter.encounter_id,obs.concept_id,obs.value_coded from patient "
            + "                             join encounter on encounter.patient_id = patient.patient_id "
            + "                             join obs on obs.encounter_id = encounter.encounter_id "
            + "                         where encounter.encounter_type =35 and obs.concept_id in (23716,23887) and obs.value_coded in(1065) "
            + "                             and patient.voided =0 and encounter.voided = 0 and obs.voided = 0 "
            + "                             and encounter.encounter_datetime between :startDate and:endDate and encounter.location_id=:location "
            + "                         union "
            + "                         select patient.patient_id, encounter.encounter_datetime,encounter.encounter_id,obs.concept_id,obs.value_coded from patient "
            + "                             join encounter on encounter.patient_id = patient.patient_id "
            + "                             join obs on obs.encounter_id = encounter.encounter_id "
            + "                         where encounter.encounter_type =35 and  (obs.concept_id = 6223 and obs.value_coded in(1383,1749,1385)) "
            + "                             and patient.voided =0 and encounter.voided = 0 and obs.voided = 0 "
            + "                             and encounter.encounter_datetime between :startDate and :endDate and encounter.location_id=:location "
            + " "
            + "                ) seguimentoAdesao "
            + "             )  seguimentoAdesao on seguimentoAdesao.patient_id=f.patient_id "
            + " "
            + "             WHERE   seguimentoAdesao.encounter_datetime >= DATE_ADD(f.art_start_date, INTERVAL 30 DAY) ";

    public static final String findPatientsWithPrevencaoPosetivaInReportingPeriod =
        "select patient.patient_id from patient "
            + "join encounter on encounter.patient_id = patient.patient_id "
            + "join obs on obs.encounter_id = encounter.encounter_id "
            + "where encounter.encounter_type =35 and obs.concept_id in (6317,6318,6319,6320,5271,6321,6322) and obs.value_coded =1065 "
            + "and patient.voided =0 and encounter.voided = 0 and obs.voided = 0 and encounter.encounter_datetime >= :startDate and encounter.encounter_datetime <= :endDate and encounter.location_id =:location "
            + "group by patient.patient_id having count(distinct obs.concept_id)=7 ";

    public static final String findPatientsWhoFaultTreatmentRF15E1 =
        "Select E1.patient_id from (select patient_id,max(data_levantamento) data_levantamento,max(data_proximo_levantamento) data_proximo_levantamento, date_add(max(data_proximo_levantamento), INTERVAL 5 day) data_proximo_levantamento5 "
            + "from(select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 30 day)  data_proximo_levantamento "
            + "from patient p inner join encounter e on p.patient_id = e.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "where  e.voided = 0 and p.voided = 0 and o.value_datetime <= :endDate and o.voided = 0 and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location "
            + "group by p.patient_id union "
            + "select fila.patient_id, fila.data_levantamento,obs_fila.value_datetime data_proximo_levantamento from ( "
            + "select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id where encounter_type=18 and e.encounter_datetime <=:endDate and e.location_id=:location and e.voided=0 and p.voided=0 group by p.patient_id)fila "
            + "inner join obs obs_fila on obs_fila.person_id=fila.patient_id "
            + "where obs_fila.voided=0 and obs_fila.concept_id=5096 and fila.data_levantamento=obs_fila.obs_datetime) maxFilaRecepcao group by patient_id "
            + "having date_add(max(data_proximo_levantamento), INTERVAL 5 day )> :startDate and date_add(max(data_proximo_levantamento), INTERVAL 5 day )< :endDate )E1 ";

    public static final String findPatientsWhoAbandonedTreatmentRF15E1 =
        "Select E1.patient_id from (select patient_id,max(data_levantamento) data_levantamento,max(data_proximo_levantamento) data_proximo_levantamento, date_add(max(data_proximo_levantamento), INTERVAL 60 day) data_proximo_levantamento60 "
            + "from(select p.patient_id,max(o.value_datetime) data_levantamento, date_add(max(o.value_datetime), INTERVAL 30 day)  data_proximo_levantamento "
            + "from patient p inner join encounter e on p.patient_id = e.patient_id "
            + "inner join obs o on o.encounter_id = e.encounter_id "
            + "where  e.voided = 0 and p.voided = 0 and o.value_datetime <= :endDate and o.voided = 0 and o.concept_id = 23866 and e.encounter_type=52 and e.location_id=:location "
            + "group by p.patient_id union "
            + "select fila.patient_id, fila.data_levantamento,obs_fila.value_datetime data_proximo_levantamento from ( "
            + "select p.patient_id,max(e.encounter_datetime) as data_levantamento from patient p "
            + "inner join encounter e on p.patient_id=e.patient_id where encounter_type=18 and e.encounter_datetime <=:endDate and e.location_id=:location and e.voided=0 and p.voided=0 group by p.patient_id)fila "
            + "inner join obs obs_fila on obs_fila.person_id=fila.patient_id "
            + "where obs_fila.voided=0 and obs_fila.concept_id=5096 and fila.data_levantamento=obs_fila.obs_datetime) maxFilaRecepcao group by patient_id "
            + "having date_add(max(data_proximo_levantamento), INTERVAL 59 day )> :startDate and date_add(max(data_proximo_levantamento), INTERVAL 59 day )< :endDate )E1 ";

    public static final String
        findPatientsRegistredInLivroDeChamadasVisitasDomiciliariasWithElegivelParaReintegracaoRF15E1 =
            "select livro_visitas.patient_id from( "
                + "select p.patient_id,max(obsElegibilidade.value_datetime) "
                + "from patient p inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs obsReintegracao on obsReintegracao.encounter_id = e.encounter_id "
                + "inner join obs obsElegibilidade on obsElegibilidade.encounter_id = e.encounter_id "
                + "where  e.voided = 0 and p.voided = 0 and obsReintegracao.voided = 0 and obsReintegracao.concept_id = 23996 and obsReintegracao.value_coded = 23993 "
                + "and obsElegibilidade.voided = 0 and obsElegibilidade.concept_id = 23997 and obsElegibilidade.value_datetime between :startDate and :endDate "
                + "and e.encounter_type=61 and e.location_id=:location "
                + "group by p.patient_id) livro_visitas ";

    public static final String
        findPatientsRegistredInLivroDeChamadasVisitasDomiciliariasForChamadaOrEncontradoRF16E2 =
            "select livro_visitas.patient_id from( "
                + "select p.patient_id,max(dataEligibilidade.value_datetime) "
                + "from patient p inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs chamada1 on chamada1.encounter_id = e.encounter_id "
                + "inner join obs dataCh1 on dataCh1.encounter_id = e.encounter_id "
                + "inner join obs dataEligibilidade on dataEligibilidade.encounter_id = e.encounter_id "
                + "where  e.voided = 0 and p.voided = 0 and dataEligibilidade.voided = 0 and dataEligibilidade.concept_id = 23997 and dataEligibilidade.value_datetime between :startDate and :endDate and chamada1.voided = 0 and chamada1.concept_id = 23998 and chamada1.value_coded = 1065 and e.encounter_type=61 and e.location_id=:location "
                + "and dataCh1.voided = 0 and dataCh1.concept_id = 24001 and dataCh1.value_datetime is not null group by p.patient_id "
                + "union "
                + "select p.patient_id,max(dataEligibilidade.value_datetime) "
                + "from patient p inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs chamada2 on chamada2.encounter_id = e.encounter_id "
                + "inner join obs dataCh2 on dataCh2.encounter_id = e.encounter_id "
                + "inner join obs dataEligibilidade on dataEligibilidade.encounter_id = e.encounter_id "
                + "where  e.voided = 0 and p.voided = 0 and dataEligibilidade.voided = 0 and dataEligibilidade.concept_id = 23997 and dataEligibilidade.value_datetime between :startDate and :endDate and chamada2.voided = 0 and chamada2.concept_id = 23999 and chamada2.value_coded = 1065 and e.encounter_type=61 and e.location_id=:location "
                + "and dataCh2.voided = 0 and dataCh2.concept_id = 24002 and dataCh2.value_datetime is not null group by p.patient_id "
                + "union "
                + "select p.patient_id,max(dataEligibilidade.value_datetime) "
                + "from patient p inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs chamada3 on chamada3.encounter_id = e.encounter_id "
                + "inner join obs dataCh3 on dataCh3.encounter_id = e.encounter_id "
                + "inner join obs dataEligibilidade on dataEligibilidade.encounter_id = e.encounter_id "
                + "where  e.voided = 0 and p.voided = 0 and dataEligibilidade.voided = 0 and dataEligibilidade.concept_id = 23997 and dataEligibilidade.value_datetime between :startDate and :endDate and chamada3.voided = 0 and chamada3.concept_id = 24000 and chamada3.value_coded = 1065 and e.encounter_type=61 and e.location_id=:location "
                + "and dataCh3.voided = 0 and dataCh3.concept_id = 24003 and dataCh3.value_datetime is not null group by p.patient_id "
                + "union "
                + "select p.patient_id,max(dataEligibilidade.value_datetime) "
                + "from patient p inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs visita1 on visita1.encounter_id = e.encounter_id "
                + "inner join obs dataVi1 on dataVi1.encounter_id = e.encounter_id "
                + "inner join obs dataEligibilidade on dataEligibilidade.encounter_id = e.encounter_id "
                + "where  e.voided = 0 and p.voided = 0 and dataEligibilidade.voided = 0 and dataEligibilidade.concept_id = 23997 and dataEligibilidade.value_datetime between :startDate and :endDate and visita1.voided = 0 and visita1.concept_id = 24008 and visita1.value_coded = 1065 and e.encounter_type=61 and e.location_id=:location "
                + "and dataVi1.voided = 0 and dataVi1.concept_id = 23933 and dataVi1.value_datetime is not null group by p.patient_id "
                + "union "
                + "select p.patient_id,max(dataEligibilidade.value_datetime) "
                + "from patient p inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs visita2 on visita2.encounter_id = e.encounter_id "
                + "inner join obs dataVi2 on dataVi2.encounter_id = e.encounter_id "
                + "inner join obs dataEligibilidade on dataEligibilidade.encounter_id = e.encounter_id "
                + "where  e.voided = 0 and p.voided = 0 and dataEligibilidade.voided = 0 and dataEligibilidade.concept_id = 23997 and dataEligibilidade.value_datetime between :startDate and :endDate and visita2.voided = 0 and visita2.concept_id = 24009 and visita2.value_coded = 1065 and e.encounter_type=61 and e.location_id=:location "
                + "and dataVi2.voided = 0 and dataVi2.concept_id = 23934 and dataVi2.value_datetime is not null group by p.patient_id "
                + "union "
                + "select p.patient_id,max(dataEligibilidade.value_datetime) "
                + "from patient p inner join encounter e on p.patient_id = e.patient_id "
                + "inner join obs visita3 on visita3.encounter_id = e.encounter_id "
                + "inner join obs dataVi3 on dataVi3.encounter_id = e.encounter_id "
                + "inner join obs dataEligibilidade on dataEligibilidade.encounter_id = e.encounter_id "
                + "where  e.voided = 0 and p.voided = 0 and dataEligibilidade.voided = 0 and dataEligibilidade.concept_id = 23997 and dataEligibilidade.value_datetime between :startDate and :endDate and visita3.voided = 0 and visita3.concept_id = 24010 and visita3.value_coded = 1065 and e.encounter_type=61 and e.location_id=:location "
                + "and dataVi3.voided = 0 and dataVi3.concept_id = 23935 and dataVi3.value_datetime is not null group by p.patient_id "
                + ")livro_visitas ";

    public static final String findPatientsReturnedToHospitalAfterChamadaOrVisitaDomiciliarRF17E3 =
        "select patient_id from ( "
            + "select livro_visita.patient_id, livro_visita.value_datetime from ( "
            + "select p.patient_id,dataRetorno.value_datetime "
            + "from patient p inner join encounter e on p.patient_id = e.patient_id "
            + "inner join obs chamadaRetorno on chamadaRetorno.encounter_id = e.encounter_id "
            + "inner join obs dataRetorno on dataRetorno.encounter_id = e.encounter_id "
            + "where  e.voided = 0 and p.voided = 0 and dataRetorno.voided = 0 and dataRetorno.concept_id = 24006 and dataRetorno.value_datetime between :startDate and :endDate "
            + "and chamadaRetorno.voided = 0 and chamadaRetorno.concept_id = 24005 and chamadaRetorno.value_coded = 1065 and e.encounter_type=61 and e.location_id=:location group by p.patient_id ) livro_visita "
            + "join "
            + "	(select p.patient_id, e.encounter_datetime "
            + "	from patient p "
            + " 	join encounter e on e.patient_id = p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 35 "
            + " 	and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by p.patient_id) apss on apss.patient_id = livro_visita.patient_id and apss.encounter_datetime = livro_visita.value_datetime "
            + "union "
            + "select livro_visita.patient_id, livro_visita.value_datetime from ( "
            + "select p.patient_id,dataRetVisita.value_datetime "
            + "from patient p inner join encounter e on p.patient_id = e.patient_id "
            + "inner join obs visitaRetorno on visitaRetorno.encounter_id = e.encounter_id "
            + "inner join obs dataRetVisita on dataRetVisita.encounter_id = e.encounter_id "
            + "where  e.voided = 0 and p.voided = 0 and dataRetVisita.voided = 0 and dataRetVisita.concept_id = 24012 and dataRetVisita.value_datetime between :startDate and :endDate "
            + "and visitaRetorno.voided = 0 and visitaRetorno.concept_id = 24011 and visitaRetorno.value_coded = 1065 and e.encounter_type=61 and e.location_id=:location "
            + "group by p.patient_id ) livro_visita "
            + "join "
            + "	(select p.patient_id, e.encounter_datetime "
            + "	from patient p "
            + " 	join encounter e on e.patient_id = p.patient_id "
            + "where p.voided=0 and e.voided=0 and e.encounter_type = 35 "
            + " 	and e.encounter_datetime between :startDate and :endDate and e.location_id=:location "
            + "group by p.patient_id) apss on apss.patient_id = livro_visita.patient_id and apss.encounter_datetime = livro_visita.value_datetime "
            + ") e3 ";
  }
}
