				            select coorteFinalPrep.patient_id as PATIENT_ID
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
			                        where e.voided=0 and p.voided=0 and e.encounter_type in (80,81) and e.encounter_datetime<=:startDate and e.location_id=:location 
			                        group by p.patient_id 
			                  )prepConsultation
			                  inner join person pe on pe.person_id = prepConsultation.patient_id
			                  inner join encounter e on e.patient_id= prepConsultation.patient_id
			                  inner join obs o on o.encounter_id=e.encounter_id
			                  where e.encounter_type in (80,81) and e.encounter_datetime=prepConsultation.prep_consultation_date 
			                  and pe.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=165228 and o.value_datetime between :startDate and :endDate
			                  )coorteFinal
			                  inner join 
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
												o.concept_id=6309 and o.value_coded=6307 and o.obs_datetime<=:endDate
								) consentimento on consentimento.patient_id=coorteFinal.patient_id
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

				             select tmp.patient_id, 
		                       MAX(CASE WHEN tmp.value_coded = 165287 then tmp.value_coded END) as ADOLESCENTE_JOVENS, 
		                       MAX(CASE WHEN tmp.value_coded = 1902 then tmp.value_coded END) as MILITAR_POLICÍA,
		                       MAX(CASE WHEN tmp.value_coded = 1908 then tmp.value_coded END) as MINEIRO,
		                       MAX(CASE WHEN tmp.value_coded = 1903 then tmp.value_coded END) as MOTORISTA,
		                       MAX(CASE WHEN tmp.value_coded = 1995 then tmp.value_coded END) as SERODISCORDANTES from (
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
			                inner join encounter e on grupoAlvo.patient_id=e.patient_id 
					      inner join obs o on o.encounter_id=e.encounter_id
					      where e.voided=0 and o.voided=0 and o.concept_id=165196 and o.value_coded in (165287,1902,1908,1903,1995) and e.encounter_type=80  and e.encounter_datetime=grupoAlvo.prep_consultation_date
						) tmp group by tmp.patient_id
				          )grupoAlvo on grupoAlvo.patient_id=coorteFinalPrep.patient_id
				          left join
				          (
				         select tmp.patient_id, 
				     	   MAX(CASE WHEN tmp.value_coded = 20426 then tmp.value_coded END) as PRISAO, 
		                       MAX(CASE WHEN tmp.value_coded = 1377 then tmp.value_coded END) as HSH, 
		                       MAX(CASE WHEN tmp.value_coded = 165205 then tmp.value_coded END) as TG,
		                       MAX(CASE WHEN tmp.value_coded = 1901 then tmp.value_coded END) as TS,
		                       MAX(CASE WHEN tmp.value_coded = 20454 then tmp.value_coded END) as PID,
		                       MAX(CASE WHEN tmp.value_coded = 5622 then tmp.value_coded END) as OUTRO from (
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
					      where e.voided=0 and o.voided=0 and o.concept_id=23703 and e.encounter_type in(80,81)  and e.encounter_datetime=grupoAlvoKp.prep_consultation_date
					      )tmp group by tmp.patient_id
				          )grupoAlvoKp on grupoAlvoKp.patient_id=coorteFinalPrep.patient_id
				          left join
				          (
				          	select 	prep.patient_id,
			if(g1.encounter_id is not null,'Grávida',
				if(g2.encounter_id is not null,'Grávida',
					if(g3.encounter_id is not null,'Grávida',
						if(l1.encounter_id is not null,'Lactante',
							if(l2.encounter_id is not null, 'Lactante',
								if(l3.encounter_id is not null,'Lactante',null)))))) estado
	from 
		(
			select 	p.patient_id,max(encounter_datetime) data_inicial_prep
			from 	patient p 
					inner join encounter e on e.patient_id=p.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type in (80,81) and 
					e.location_id=:location and	e.encounter_datetime<=:endDate
			group by p.patient_id
		) 	prep
			inner join encounter e on e.patient_id=prep.patient_id
			left join obs g1 on e.encounter_id=g1.encounter_id and g1.concept_id=165196 and g1.value_coded=1982 and g1.voided=0
			left join obs g2 on e.encounter_id=g2.encounter_id and g2.concept_id=1982 and g2.value_coded=1065 and g2.voided=0
			left join obs g3 on e.encounter_id=g3.encounter_id and g3.concept_id=165223 and g3.value_coded=1982 and g3.voided=0
			left join obs l1 on e.encounter_id=l1.encounter_id and l1.concept_id=165196 and l1.value_coded=6332 and l1.voided=0
			left join obs l2 on e.encounter_id=l2.encounter_id and l2.concept_id=6332 and l2.value_coded=1065 and l2.voided=0
			left join obs l3 on e.encounter_id=l3.encounter_id and l3.concept_id=165223 and l3.value_coded=6332 and l3.voided=0
			
		where 	e.voided=0 and e.encounter_type in (80,81) and 
				e.encounter_datetime=prep.data_inicial_prep and e.location_id=:location
				      
				          )gravidaLactante on gravidaLactante.patient_id=coorteFinalPrep.patient_id
				          left join
				          (
						select sector.patient_id,
						case 
							       when ISNULL(sector.value_coded) then '' 
							       when sector.value_coded = 1987 then 'SAAJ'
							       when sector.value_coded = 23873 then 'Triagem Adulto'
							       when sector.value_coded = 165206 then 'Consultas integradas'
							       when sector.value_coded = 1978 then 'CPN'
							       when sector.value_coded = 5483 then 'CPF'
							       when sector.value_coded = 23913 then 'Outro'   
							       end AS SECTOR from (
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
					      where e.voided=0 and o.voided=0 and o.concept_id=165291 and e.encounter_type=80  and e.encounter_datetime=sector.prep_consultation_date
						)sector
				          )sector on sector.patient_id=coorteFinalPrep.patient_id
				          group by coorteFinalPrep.patient_id