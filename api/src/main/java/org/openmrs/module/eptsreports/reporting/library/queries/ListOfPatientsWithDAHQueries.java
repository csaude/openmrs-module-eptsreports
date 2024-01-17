package org.openmrs.module.eptsreports.reporting.library.queries;

public interface ListOfPatientsWithDAHQueries {

  class QUERY {

    public static final String findPatientsInMDSOfDAHDuringPeriodTotal =
        "					 		select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and  e.encounter_type =90 "
            + "	and e.encounter_datetime >= :startDate and e.encounter_datetime <= :endDate and e.location_id=:location ";

    public static final String findPatientsInMDSOfDAHBeforePeriodTotal =
        "					 		select p.patient_id from patient p "
            + "	inner join encounter e on p.patient_id=e.patient_id "
            + "	where e.voided=0 and p.voided=0 and  e.encounter_type =90 "
            + "	and e.encounter_datetime < :startDate and e.location_id=:location ";

    public static final String findPatientsTransferredOut =
        "	    select transferidopara.patient_id  from ( "
            + "    select patient_id,max(data_transferidopara) data_transferidopara from ( "
            + "    select maxEstado.patient_id,maxEstado.data_transferidopara from ( "
            + "    select pg.patient_id,max(ps.start_date) data_transferidopara "
            + "    from patient p "
            + "    inner join patient_program pg on p.patient_id=pg.patient_id "
            + "    inner join patient_state ps on pg.patient_program_id=ps.patient_program_id "
            + "    where pg.voided=0 and ps.voided=0 and p.voided=0 and "
            + "    pg.program_id=2 and ps.start_date<=:endDate and pg.location_id=:location group by p.patient_id "
            + "    ) maxEstado "
            + "    inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id "
            + "    inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id "
            + "    where pg2.voided=0 and ps2.voided=0 and pg2.program_id=2 and "
            + "    ps2.start_date=maxEstado.data_transferidopara and pg2.location_id=:location and ps2.state=7 "
            + "    union "
            + "    select p.patient_id,max(o.obs_datetime) data_transferidopara from patient p "
            + "    inner join encounter e on p.patient_id=e.patient_id "
            + "    inner join obs o on o.encounter_id=e.encounter_id "
            + "    where e.voided=0 and p.voided=0 and o.obs_datetime<=:endDate and o.voided=0 and o.concept_id=6272 and o.value_coded=1706 and e.encounter_type=53 "
            + "    and  e.location_id=:location group by p.patient_id "
            + "    union "
            + "    select p.patient_id,max(e.encounter_datetime) data_transferidopara from  patient p "
            + "    inner join encounter e on p.patient_id=e.patient_id "
            + "    inner join obs o on o.encounter_id=e.encounter_id where  e.voided=0 and p.voided=0 and e.encounter_datetime<=:endDate and "
            + "    o.voided=0 and o.concept_id=6273 and o.value_coded=1706 and e.encounter_type=6 and  e.location_id=:location group by p.patient_id "
            + "    ) transferido group by patient_id "
            + "    ) transferidopara "
            + "	    inner join( "
            + "    select patient_id,max(encounter_datetime) encounter_datetime from "
            + "    ( "
            + "    select p.patient_id,max(e.encounter_datetime) encounter_datetime from  patient p "
            + "    inner join encounter e on e.patient_id=p.patient_id where  p.voided=0 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location and e.encounter_type in (18,6) "
            + "    group by p.patient_id "
            + "    ) consultaLev group by patient_id "
            + "    ) consultaOuARV on transferidopara.patient_id=consultaOuARV.patient_id "
            + "    where consultaOuARV.encounter_datetime<=transferidopara.data_transferidopara ";
  }
}
