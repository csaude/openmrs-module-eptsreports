		select HVL_FR17.patient_id from
		(
		select HVL_FR16.*,consultaApss2.encounter_datetime dataConsultaApss2 from

		(
		select 	HVL_FR15.*,
		if(min(consultaApss1.encounter_datetime) is not null,min(consultaApss1.encounter_datetime),'N/A') dataConsultaApss1
		from
		(
		select HVL_FR3.*,
		if(min(consultaClinica0.encounter_datetime)is not null,min(consultaClinica0.encounter_datetime),'N/A') dataConsultaClinica0,
		min(consultaApss0.encounter_datetime) dataConsultaApss0,
		primeiraColheitaCV.data_colheita  data_colheita
		from
		(
		Select 	HVL_FR4_HVL_FR5.patient_id,
		HVL_FR4_HVL_FR5.data_inicio,
		HVL_FR4_HVL_FR5.linha,
		primeiraCVAlta.data_carga,
		primeiraCVAlta.valorCV
		
		from
		(
		select HVL_FR4.patient_id,HVL_FR4.data_inicio,'1ª Linha' linha
		from
		(	Select patient_id,min(data_inicio) data_inicio
		from
		(	
		/*Patients on ART who initiated the ARV DRUGS: ART Regimen Start Date*/
		
		Select 	p.patient_id,min(e.encounter_datetime) data_inicio
		from 	patient p
			inner join encounter e on p.patient_id=e.patient_id	
			inner join obs o on o.encounter_id=e.encounter_id
		where 	e.voided=0 and o.voided=0 and p.voided=0 and
			e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and
			e.encounter_datetime<=:endDate and e.location_id=:location
		group by p.patient_id
		
		union
		
		/*Patients on ART who have art start date: ART Start date*/
		Select 	p.patient_id,min(value_datetime) data_inicio
		from 	patient p
			inner join encounter e on p.patient_id=e.patient_id
			inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and
			o.concept_id=1190 and o.value_datetime is not null and
			o.value_datetime<=:endDate and e.location_id=:location
		group by p.patient_id
		union
		/*Patients enrolled in ART Program: OpenMRS Program*/
		select 	pg.patient_id,min(date_enrolled) data_inicio
		from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id
		where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:endDate and location_id=:location
		group by pg.patient_id
		
		union
		
		
		/*Patients with first drugs pick up date set in Pharmacy: First ART Start Date*/
		SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio
		FROM 		patient p
				inner join encounter e on p.patient_id=e.patient_id
		WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location
		GROUP BY 	p.patient_id
		
		union
		
		/*Patients with first drugs pick up date set: Recepcao Levantou ARV*/
		Select 	p.patient_id,min(value_datetime) data_inicio
		from 	patient p
			inner join encounter e on p.patient_id=e.patient_id
			inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and
			o.concept_id=23866 and o.value_datetime is not null and
			o.value_datetime<=:endDate and e.location_id=:location
		group by p.patient_id	
		) inicio_real
		group by patient_id
		)HVL_FR4
		left join
		(	
		Select 	p.patient_id,max(o.obs_datetime) data_segundaLinha
		from 	patient p
		inner join encounter e on p.patient_id=e.patient_id
		inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and
		o.concept_id in (21187,21188) and o.obs_datetime<=:endDate and e.location_id=:location
		group by p.patient_id
		)segundaTerceira on  segundaTerceira.patient_id=HVL_FR4.patient_id
		where segundaTerceira.patient_id is null
		union
		select patient_id,data_segundaLinha,'2ª Linha' linha
		from
		(	
		Select 	p.patient_id,max(o.obs_datetime) data_segundaLinha
		from 	patient p
		inner join encounter e on p.patient_id=e.patient_id
		inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=53 and
		o.concept_id=21187 and o.obs_datetime<=:endDate and e.location_id=:location
		group by p.patient_id
		) HVL_FR5
		) HVL_FR4_HVL_FR5
		inner join
		(
		Select primeiraCV.patient_id,primeiraCV.data_carga,o.value_numeric valorCV
		from
		(
		Select 	p.patient_id,min(o.obs_datetime) data_carga
		from 	patient p
		inner join encounter e on p.patient_id=e.patient_id
		inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51) and
		o.concept_id=856 and o.obs_datetime BETWEEN :startDate and :endDate and e.location_id=:location and o.value_numeric>1000
		group by p.patient_id
		) primeiraCV
		inner join encounter e on e.patient_id=primeiraCV.patient_id
		inner join obs o on e.encounter_id=o.encounter_id
		where 	e.voided=0 and o.voided=0 and e.encounter_type in (13,51) and
		o.concept_id=856 and o.obs_datetime=primeiraCV.data_carga and e.location_id=:location
		) primeiraCVAlta on  primeiraCVAlta.patient_id=HVL_FR4_HVL_FR5.patient_id
		where primeiraCVAlta.data_carga BETWEEN HVL_FR4_HVL_FR5.data_inicio and :endDate
		
		) HVL_FR3
		left join encounter consultaClinica0 on consultaClinica0.patient_id=HVL_FR3.patient_id and consultaClinica0.encounter_type=6 and consultaClinica0.location_id=:location and consultaClinica0.voided=0
		and consultaClinica0.encounter_datetime BETWEEN HVL_FR3.data_carga and  :endDate
		left join encounter consultaApss0 on consultaApss0.patient_id=HVL_FR3.patient_id and consultaApss0.encounter_type=35 and consultaApss0.location_id=:location and consultaApss0.voided=0
		and consultaApss0.encounter_datetime BETWEEN HVL_FR3.data_carga and :endDate
		left join 
		(
		select p.patient_id, obsVL.obs_datetime data_resultado, obsSampleCollectDate.value_datetime data_colheita
		from patient p
		inner join encounter e on p.patient_id=e.patient_id	
		inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
		inner join obs obsVL on obsVL.encounter_id=e.encounter_id
		where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
		and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric>1000  
		and e.location_id=:location and obsSampleCollectDate.value_datetime <= :endDate
		) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR3.patient_id and primeiraColheitaCV.data_resultado  = HVL_FR3.data_carga 
		
		group by HVL_FR3.patient_id
		) HVL_FR15			
		left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
		and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
		where consultaApss1.encounter_datetime  is not null and HVL_FR15.dataConsultaApss0 is not null
		group by HVL_FR15.patient_id
		)HVL_FR16
		left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
	     and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
	     where consultaApss2.patient_id is not  null
		group by HVL_FR16.patient_id
		) HVL_FR17
         left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
	    and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
	    where consultaApss3.patient_id  is not  null
	    group by HVL_FR17.patient_id