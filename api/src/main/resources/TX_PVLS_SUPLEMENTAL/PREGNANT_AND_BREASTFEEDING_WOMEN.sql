select patient_id
from 
(
select 	p.patient_id,
		gravida.data_inicio data_inicio_gravida,
		gravida.data_gravida,
		lactante.data_inicio data_inicio_lactante,
		lactante.data_parto,
		if(gravida.data_gravida is null, 2,
			if(lactante.data_parto is null, 1,
				if(gravida.data_gravida>=lactante.data_parto,1,2))) estado_final

from person per
	inner join patient p on per.person_id=p.patient_id
	left join 
	(select 	inicio_real.patient_id,
			inicio_real.data_inicio,
			gravida_final.data_gravida
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
					e.encounter_datetime > '0000-00-00 00:00' and e.encounter_datetime<=:endDate and e.location_id=:location
			group by p.patient_id

			union

			/*Patients on ART who have art start date: ART Start date*/
			Select 	p.patient_id,min(value_datetime) data_inicio
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
					inner join obs o on e.encounter_id=o.encounter_id
			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and 
					o.concept_id=1190 and o.value_datetime is not null and 
					o.value_datetime > '0000-00-00 00:00' and  o.value_datetime<=:endDate and e.location_id=:location
			group by p.patient_id

			union

			/*Patients enrolled in ART Program: OpenMRS Program*/
			select 	pg.patient_id,min(date_enrolled) data_inicio
			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id
			where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled > '0000-00-00 00:00' and date_enrolled<=:endDate and location_id=:location
			group by pg.patient_id
			
			union
			
			
			/*Patients with first drugs pick up date set in Pharmacy: First ART Start Date*/
			  SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio 
			  FROM 		patient p
						inner join encounter e on p.patient_id=e.patient_id
			  WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime > '0000-00-00 00:00' and e.encounter_datetime<=:endDate and e.location_id=:location
			  GROUP BY 	p.patient_id
		  
			union
			
			/*Patients with first drugs pick up date set: Recepcao Levantou ARV*/
			Select 	p.patient_id,min(value_datetime) data_inicio
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
					inner join obs o on e.encounter_id=o.encounter_id
			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
					o.concept_id=23866 and o.value_datetime is not null and 
					o.value_datetime > '0000-00-00 00:00' and o.value_datetime<=:endDate and e.location_id=:location
			group by p.patient_id							
			
		) inicio
	group by patient_id	
	)inicio_real
	inner join 			
	-- =====================GRAVIDAS==================
		(	select patient_id,max(data_gravida) data_gravida
			from 
				(
				/*Pregnancy registered on Ficha Clínica
				Specification:
					MAX (Encounter_datetime) >=endDate -12 months and <= endDate 
					with value_coded=1065 (Yes) for concept_id=1982 
					As Date Pregnancy registered
					EncounterType=6
				*/
				
				Select 	p.patient_id,max(encounter_datetime) data_gravida
				from 	patient p 
						inner join encounter e on e.patient_id=p.patient_id
						inner join obs o on o.encounter_id=e.encounter_id 
				where 	p.voided=0 and e.voided=0 and e.encounter_type=6 and 
						e.location_id=:location and o.concept_id=1982 and o.value_coded=1065 and 
						e.encounter_datetime BETWEEN date_add(:endDate, interval -15 month) and date_add(:endDate, interval -3 month)
				group by p.patient_id
				
				union 
				
				/*Enrollment date in Program-PTV
				Specification:
					MAX (Program Enrolment Date) >=endDate -12 months and <= endDate 
					And
					PTV/ETV Program (program_id =8)
					As Date Pregnancy registered			
				*/
				select 	pp.patient_id,pp.date_enrolled data_gravida 
				from 	patient_program pp                                                                                                                                                                          
				where 	pp.program_id=8 and pp.voided=0 and  
						pp.date_enrolled between date_add(:endDate, interval -15 month) and date_add(:endDate, interval -3 month) and 
						pp.location_id=:location                                                                                                
			
				union
				
				/*Pregnancy registered at ART initiation on Ficha Resumo
				Specification:
						Value.datetime for (concept id= 1190) >=endDate -12 months and <= endDate
						And 
						Yes (id= 1065) for (concept id=1982)

						As Date Pregnancy registered
						EncounterType=53
				*/
				select 	p.patient_id,obsART.value_datetime data_gravida 
				from 	patient p                                                                                                                                                                               
						inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
						inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
						inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
				where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and  
						e.encounter_type=53 and obsART.value_datetime between date_add(:endDate, interval -15 month) and date_add(:endDate, interval -3 month) and 
						e.location_id=:location and  obsART.concept_id=1190 and obsART.voided=0                                                                               

				union
				
				/*Pregnancy registered on e-Lab Form
				Specification:
						MAX (Encounter_datetime) >=endDate -12 months and <= endDate 
						with value_coded=1065 for concept_id=1982
						As Date Pregnancy registered
						EncounterType=51
				*/
				select 	p.patient_id,max(e.encounter_datetime) data_gravida 
				from 	patient p                                                                                                                                                                        
						inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
						inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                        
				where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51 and                                                                                                     
						e.encounter_datetime between date_add(:endDate, interval -15 month) and date_add(:endDate, interval -3 month) and e.location_id=:location 
				group by p.patient_id
			) gravida 
			group by gravida.patient_id
		) gravida_final on inicio_real.patient_id=gravida_final.patient_id
		where timestampdiff(day,inicio_real.data_inicio,gravida_final.data_gravida)>90
	) gravida on p.patient_id=gravida.patient_id
	left join 
	(			
	-- =====================LACTANTE==================
	select 	inicio_real.patient_id,
			inicio_real.data_inicio,
			lactante_final.data_parto,
			timestampdiff(day,inicio_real.data_inicio,lactante_final.data_parto) diasPartoTarv
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
					e.encounter_datetime > '0000-00-00 00:00' and e.encounter_datetime<=:endDate and e.location_id=:location
			group by p.patient_id

			union

			/*Patients on ART who have art start date: ART Start date*/
			Select 	p.patient_id,min(value_datetime) data_inicio
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
					inner join obs o on e.encounter_id=o.encounter_id
			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and 
					o.concept_id=1190 and o.value_datetime is not null and 
					o.value_datetime > '0000-00-00 00:00' and  o.value_datetime<=:endDate and e.location_id=:location
			group by p.patient_id

			union

			/*Patients enrolled in ART Program: OpenMRS Program*/
			select 	pg.patient_id,min(date_enrolled) data_inicio
			from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id
			where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled > '0000-00-00 00:00' and date_enrolled<=:endDate and location_id=:location
			group by pg.patient_id
			
			union
			
			
			/*Patients with first drugs pick up date set in Pharmacy: First ART Start Date*/
			  SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio 
			  FROM 		patient p
						inner join encounter e on p.patient_id=e.patient_id
			  WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime > '0000-00-00 00:00' and e.encounter_datetime<=:endDate and e.location_id=:location
			  GROUP BY 	p.patient_id
		  
			union
			
			/*Patients with first drugs pick up date set: Recepcao Levantou ARV*/
			Select 	p.patient_id,min(value_datetime) data_inicio
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
					inner join obs o on e.encounter_id=o.encounter_id
			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
					o.concept_id=23866 and o.value_datetime is not null and 
					o.value_datetime > '0000-00-00 00:00' and o.value_datetime<=:endDate and e.location_id=:location
			group by p.patient_id							
			
		) inicio
	group by patient_id	
	)inicio_real
	inner join 				
		(	
			select patient_id,max(data_parto) data_parto
			from 
				(
				/*Breastfeeding registered on Ficha Clinica
				Specification:
						MAX (Encounter_datetime) >=endDate -12 months and <= endDate 
						with value_coded=1065 (Yes) for concept_id=6332
						As Date Breastfeeding Registered
						EncounterType=6		
				*/
				select 	p.patient_id, max(e.encounter_datetime) data_parto 
				from 	patient p                                                                                                                                                                                 
						inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
						inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
				where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and  
						e.encounter_type=6 and e.encounter_datetime between date_add(:endDate, interval -15 month) and date_add(:endDate, interval -3 month) and :endDate and 
						e.location_id=:location 
				group by p.patient_id
				
				union 
				
				/*Birth registered in Program-PTV
				Specification:
						MAX (Client Program State Date)>=endDate -12 months and <= endDate And
						PTV (program_id =8 ) and (client state id= 27)
						As Date Breastfeeding Registered
			
				*/
				
				select 	maxEstado.patient_id,maxEstado.data_estado data_parto
				from 
					( 
						select 	pg.patient_id,max(ps.start_date) data_estado 
						from  	patient p 
								inner join patient_program pg on p.patient_id=pg.patient_id 
								inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
						where 	pg.voided=0 and ps.voided=0 and p.voided=0 and 
								pg.program_id=8 and ps.start_date BETWEEN date_add(:endDate, interval -15 month) and date_add(:endDate, interval -3 month) and :endDate and pg.location_id=:location 
						group by p.patient_id 
					) maxEstado 
					inner join patient_program pg2 on pg2.patient_id=maxEstado.patient_id 
					inner join patient_state ps2 on pg2.patient_program_id=ps2.patient_program_id 
				where 	pg2.voided=0 and ps2.voided=0 and pg2.program_id=8 and 
						ps2.start_date=maxEstado.data_estado and pg2.location_id=:location and ps2.state=27
				
				union 
				
				/*Breastfeeding registered at ART initiation in Ficha Resumo 
				Specification:
						Value.datetime for (concept id= 1190) >=endDate -12 months and <= endDate 
						Yes (id= 1065) for (concept id= 6332)
						As Date Breastfeeding Registered
						EncounterType=53	
				*/
				select 	p.patient_id, obsART.value_datetime data_parto 
				from 	patient p                                                                                                                                                                                
						inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
						inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
						inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
				where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and  
						e.encounter_type=53 and e.location_id=:location and 
						obsART.value_datetime between date_add(:endDate, interval -15 month) and date_add(:endDate, interval -3 month) and :endDate and  
						obsART.concept_id=1190 and obsART.voided=0 

				union 
				/*Breastfeeding registered on e-Lab Form 
				Specification:
						MAX (Encounter_datetime) >=endDate -12 months and <= endDate 
						with value_coded=1065 (Yes) for concept_id=6332
						As Date Breastfeeding registered
						EncounterType=51	
				*/
				select 	p.patient_id,max(e.encounter_datetime) data_parto 
				from 	patient p    																																										
						inner join encounter e on p.patient_id=e.patient_id    																																															
						inner join obs o on e.encounter_id=o.encounter_id    																																									
				where 	p.voided=0 and e.voided=0 and o.voided=0 and  
						o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51  and 
						e.encounter_datetime between date_add(:endDate, interval -15 month) and date_add(:endDate, interval -3 month) and :endDate and e.location_id=:location  
				) lactante 
				group by lactante.patient_id
		)lactante_final on inicio_real.patient_id=lactante_final.patient_id
		where timestampdiff(day,inicio_real.data_inicio,lactante_final.data_parto)>90
	) lactante on p.patient_id=lactante.patient_id
	where per.gender='F' and (gravida.patient_id is not null or lactante.patient_id is not null)
) txpvlssuplement
where estado_final=%s