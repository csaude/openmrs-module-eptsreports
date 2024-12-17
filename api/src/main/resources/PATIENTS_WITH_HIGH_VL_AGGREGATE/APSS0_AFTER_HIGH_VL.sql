	       select HVL_FR3.patient_id
		      
		   from 
		   (
             Select   HVL_FR4_HVL_FR5.patient_id,
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
		    left join encounter consultaApss0 on consultaApss0.patient_id=HVL_FR3.patient_id 
		    and consultaApss0.encounter_type=35 
		    and consultaApss0.location_id=:location 
		    and consultaApss0.voided=0
            and consultaApss0.encounter_datetime BETWEEN HVL_FR3.data_carga and :endDate
            where consultaApss0.encounter_datetime is not null
            group by HVL_FR3.patient_id