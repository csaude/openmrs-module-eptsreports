									select coorteFinalPrep.patient_id
				            from (
        					select coorteFinal.patient_id, coorteFinal.prep_consultation_date,coorteFinal.data_proximo 
				            from 
				            (
				            select prepConsultation.patient_id, prepConsultation.prep_consultation_date,o.value_datetime data_proximo
				            from 
				                  (
			                        select p.patient_id, max(e.encounter_datetime) prep_consultation_date
			                        from patient p 
			                              inner join encounter e on p.patient_id=e.patient_id 
			                        where e.voided=0 and p.voided=0 and e.encounter_type in (80,81) and e.encounter_datetime<=:endDate and e.location_id=:location 
			                        group by p.patient_id 
			                  )prepConsultation
			                  inner join person pe on pe.person_id = prepConsultation.patient_id
			                  inner join encounter e on e.patient_id= prepConsultation.patient_id
			                  inner join obs o on o.encounter_id=e.encounter_id
			                  where pe.voided = 0 and e.encounter_type in (80,81) and e.encounter_datetime = prepConsultation.prep_consultation_date 
			                  and e.voided=0 and o.voided=0 and o.concept_id=165228 and date_add(o.value_datetime, interval 90 day) < :endDate
			                  )coorteFinal
			                  left join
			                  (
			                  select 	prep.patient_id
					from 
						(
							select 	p.patient_id,max(encounter_datetime) data_inicial_prep
							from 	patient p 
									inner join encounter e on e.patient_id=p.patient_id
							where 	p.voided=0 and e.voided=0 and e.encounter_type=80 and 
									e.location_id=:location and	e.encounter_datetime<=:endDate
							group by p.patient_id
						) 	prep
							inner join encounter e on e.patient_id=prep.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
							
						where 	e.voided=0 and o.voided=0 and e.encounter_type=80 and 
								e.encounter_datetime=prep.data_inicial_prep and 
								o.concept_id = 165292 and o.value_coded in (1706,1260) and o.obs_datetime<=:endDate
					union 
					
					-- Interrompido Ficha Seguimento
					select 	prep.patient_id
					from 
						(
							select 	p.patient_id,max(encounter_datetime) data_seguimento_prep
							from 	patient p 
									inner join encounter e on e.patient_id=p.patient_id
							where 	p.voided=0 and e.voided=0 and e.encounter_type=81 and 
									e.location_id=:location and	e.encounter_datetime<=:endDate
							group by p.patient_id
						) 	prep
							inner join encounter e on e.patient_id=prep.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
							
						where 	e.voided=0 and o.voided=0 and e.encounter_type=81 and 
								e.encounter_datetime=prep.data_seguimento_prep and 
								o.concept_id=165225 and o.value_coded is not null and o.obs_datetime<=:endDate
			                   )saidas on saidas.patient_id=coorteFinal.patient_id
			                   where saidas.patient_id is null
			                   group by coorteFinal.patient_id
			                  )coorteFinalPrep
			                 left join person p on p.person_id=coorteFinalPrep.patient_id
			                 left join person_attribute pat on pat.person_id=coorteFinalPrep.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
			                 left join person_attribute pat2 on pat2.person_id=coorteFinalPrep.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
				            left join
				            ( 
				            select pn1.*  from person_name pn1  
				            inner join (  
				            select person_id,min(person_name_id) id   from person_name  
				            where voided=0  
				            group by person_id  
				            ) pn2  
				            where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
				            ) pn on pn.person_id=coorteFinalPrep.patient_id 
				            left join  
				            ( 
				            select pid1.*  from patient_identifier pid1  
				            inner join 
				            (  
				            select patient_id,min(patient_identifier_id) id, identifier  from patient_identifier  
				            where voided=0 and identifier_type = 17
				            group by patient_id  
				            ) pid2 
				            where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
				            ) pid on pid.patient_id=coorteFinalPrep.patient_id

				            left join
				            (
			               select grupoAlvo.patient_id,grupoAlvo.prep_consultation_date,o.value_coded  from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_date
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type=80 
					                  and o.concept_id=165196 
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location 
				                   group by p.patient_id 
			               )grupoAlvo
			                left join encounter e on grupoAlvo.patient_id=e.patient_id 
					      left join obs o on o.encounter_id=e.encounter_id
					      where e.voided=0 and o.voided=0 and o.concept_id=165196 and e.encounter_type=80  and e.encounter_datetime=grupoAlvo.prep_consultation_date
				          )grupoAlvo on grupoAlvo.patient_id=coorteFinalPrep.patient_id
				          left join
				          (
				         select grupoAlvoKp.patient_id,grupoAlvoKp.prep_consultation_date,o.value_coded  from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_date
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type in(80,81) 
					                  and o.concept_id=23703  
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location 
				                   group by p.patient_id 
			               )grupoAlvoKp
			                left join encounter e on grupoAlvoKp.patient_id=e.patient_id 
					      left join obs o on o.encounter_id=e.encounter_id
					      where e.voided=0 and o.voided=0 and o.concept_id=165196 and e.encounter_type in(80,81)  and e.encounter_datetime=grupoAlvoKp.prep_consultation_date
				          )grupoAlvoKp on grupoAlvoKp.patient_id=coorteFinalPrep.patient_id
				          left join
				          (
				          select gravidaFinal.patient_id,
			                      gravidaFinal.prep_consultation_data_gravida,
			                      lactanteFinal.prep_consultation_data_lactante,
						       if(lactanteFinal.prep_consultation_data_lactante is null,'Gravida', if(gravidaFinal.prep_consultation_data_gravida is null,'Lactante', if(gravidaFinal.prep_consultation_data_gravida>=lactanteFinal.prep_consultation_data_lactante,'Gravida','Lactante'))) decisao 

							 from person p 
							 inner join
			                (
			               select gravidaFinal.patient_id,max(gravidaFinal.prep_consultation_data_gravida) prep_consultation_data_gravida, 1 type 
			               from
			               (
			               select gravida.patient_id,gravida.prep_consultation_data_gravida 
			               from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_data_gravida
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type=80
					                  and o.concept_id=165196 
					                  and o.value_coded in(1982) 
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location 
				                   group by p.patient_id 
			               )gravida
			               left join encounter e on gravida.patient_id=e.patient_id 
					     left join obs o on o.encounter_id=e.encounter_id
					     where e.voided=0 and o.voided=0 and o.concept_id=165196 and e.encounter_type=80  and e.encounter_datetime=gravida.prep_consultation_data_gravida
					     
					     union

					    select gravida.patient_id,gravida.prep_consultation_data_gravida 
			               from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_data_gravida
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type=80
					                  and o.concept_id=1982 
					                  and o.value_coded=1065 
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location 
				                   group by p.patient_id 
			               )gravida
			               
			               union

			               select gravida.patient_id,gravida.prep_consultation_data_gravida 
			               from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_data_gravida
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type=81
					                  and o.concept_id=165223 
					                  and o.value_coded=1982 
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location 
				                   group by p.patient_id 
			               )gravida
			               )gravidaFinal
			                group by gravidaFinal.patient_id
			               )gravidaFinal on gravidaFinal.patient_id=p.person_id
			               left join
			               (
			               select lactanteFinal.patient_id,max(lactanteFinal.prep_consultation_data_lactante) prep_consultation_data_lactante, 2 type 
			               from
			               (
			               select lactante.patient_id,lactante.prep_consultation_data_lactante 
			               from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_data_lactante
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type=80
					                  and o.concept_id=165196 
					                  and o.value_coded=6332
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location 
				                   group by p.patient_id 
			               )lactante
			               left join encounter e on lactante.patient_id=e.patient_id 
					     left join obs o on o.encounter_id=e.encounter_id
					     where e.voided=0 and o.voided=0 and o.concept_id=165196 and e.encounter_type=80  and e.encounter_datetime=lactante.prep_consultation_data_lactante
					     
					     union

					    select lactante.patient_id,lactante.prep_consultation_data_lactante 
			               from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_data_lactante
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type=80
					                  and o.concept_id=6332 
					                  and o.value_coded=1065 
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location 
				                   group by p.patient_id 
			               )lactante
			               
			               union

			               select lactante.patient_id,lactante.prep_consultation_data_lactante 
			               from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_data_lactante
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type=81
					                  and o.concept_id=165223 
					                  and o.value_coded=6332 
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location
				                   group by p.patient_id 
			               )lactante
			               )lactanteFinal
			                group by lactanteFinal.patient_id
			               )lactanteFinal on lactanteFinal.patient_id=p.person_id
				          )gravidaLactante on gravidaLactante.patient_id=coorteFinalPrep.patient_id
				          left join
				          (
				           select sector.patient_id,sector.prep_consultation_date,o.value_coded  from 
			                (
			                select p.patient_id, max(e.encounter_datetime) prep_consultation_date
				                     from patient p 
					                  inner join encounter e on p.patient_id=e.patient_id 
					                  inner join obs o on o.encounter_id=e.encounter_id
					                  where e.voided=0 
					                  and o.voided=0 
					                  and p.voided=0 
					                  and e.encounter_type=80 
					                  and o.concept_id=165291 
					                  and e.encounter_datetime<=:endDate 
					                  and e.location_id=:location 
				                   group by p.patient_id 
			               )sector
			                left join encounter e on sector.patient_id=e.patient_id 
					      left join obs o on o.encounter_id=e.encounter_id
					      where e.voided=0 and o.voided=0 and o.concept_id=165291 and e.encounter_type=80 and e.encounter_datetime=sector.prep_consultation_date
				          )sector on sector.patient_id=coorteFinalPrep.patient_id
				          group by coorteFinalPrep.patient_id   