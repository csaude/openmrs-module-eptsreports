        select distinct elegivelAColheitaCV.patient_id
            from 
            ( 
			select distinct txCurr.patient_id,
            		  consultaLevantamento.data_proxima_consulta,
            		  inicio_real.data_inicio 
			from
				(
				%s
            	)txCurr
            	inner join
			(
            	select 	consultaLevantamento.patient_id, 
            			consultaLevantamento.data_proxima_consulta
            	from 
			( 
            		select patient_id,data_consulta,max(data_proxima_consulta) data_proxima_consulta 
            		from 
            		( 
            			Select 	ultimavisita.patient_id,ultimavisita.encounter_datetime data_consulta ,o.value_datetime data_proxima_consulta 
            			from 
            				(	select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            					from 	encounter e 
            							inner join patient p on p.patient_id=e.patient_id 
            					where 	e.voided=0 and p.voided=0 and e.encounter_type=18 and e.location_id=:location and e.encounter_datetime<=:startDate 
            					group by p.patient_id 
            				) ultimavisita 
            				inner join encounter e on e.patient_id=ultimavisita.patient_id 
            				inner join obs o on o.encounter_id=e.encounter_id 
            			where 	o.concept_id=5096 and o.voided=0 and e.encounter_datetime=ultimavisita.encounter_datetime and 
            					e.encounter_type=18 and e.location_id=:location and o.value_datetime between :startDate and :endDate 
            			union 

            			select patient_id, data_levantamento, (data_levantamento + INTERVAL 30 day) as data_proximo_levantamento from (
            			Select 	p.patient_id,max(value_datetime) data_levantamento
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
            					o.concept_id=23866 and o.value_datetime is not null and 
            					o.value_datetime<= CURDATE() and e.location_id=:location
            					group by p.patient_id
            					) maxFicha where (data_levantamento + INTERVAL 30 day) between  :startDate and :endDate 
            					group by maxFicha.patient_id
            			
            			union 
            			Select 	ultimavisita.patient_id,ultimavisita.encounter_datetime,o.value_datetime 
            			from 
            				(	select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            					from 	encounter e 
            							inner join patient p on p.patient_id=e.patient_id 
            					where 	e.voided=0 and p.voided=0 and e.encounter_type in (6,9) and e.location_id=:location and e.encounter_datetime<=:startDate 
            					group by p.patient_id 
            				) ultimavisita 
            				inner join encounter e on e.patient_id=ultimavisita.patient_id 
            				left join obs o on o.encounter_id=e.encounter_id and o.concept_id=1410 and o.voided=0 
            			where  	e.encounter_datetime=ultimavisita.encounter_datetime and 
            					e.encounter_type in (6,9) and e.location_id=:location and o.value_datetime between :startDate and :endDate 
            			group by ultimavisita.patient_id 
            		) consultaRecepcao 
            		group by patient_id 
            	) consultaLevantamento 
            	) consultaLevantamento on consultaLevantamento.patient_id = txCurr.patient_id
            	inner join 
            	(	Select patient_id,min(data_inicio) data_inicio 
            		from 
            			( 
            				Select 	p.patient_id,min(e.encounter_datetime) data_inicio 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            						inner join obs o on o.encounter_id=e.encounter_id 
            				where 	e.voided=0 and o.voided=0 and p.voided=0 and 
            						e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256 and 
            						e.encounter_datetime<=:startDate and e.location_id=:location 
            				group by p.patient_id 
            				union 
            				Select 	p.patient_id,min(value_datetime) data_inicio 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            						inner join obs o on e.encounter_id=o.encounter_id 
            				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53) and 
            						o.concept_id=1190 and o.value_datetime is not null and 
            						o.value_datetime<=:startDate and e.location_id=:location 
            				group by p.patient_id 
            				union 
            				select 	pg.patient_id,min(date_enrolled) data_inicio 
            				from 	patient p inner join patient_program pg on p.patient_id=pg.patient_id 
            				where 	pg.voided=0 and p.voided=0 and program_id=2 and date_enrolled<=:startDate and location_id=:location 
            				group by pg.patient_id 
            				union 
            				  SELECT 	e.patient_id, MIN(e.encounter_datetime) AS data_inicio 
            				  FROM 		patient p 
            							inner join encounter e on p.patient_id=e.patient_id 
            				  WHERE		p.voided=0 and e.encounter_type=18 AND e.voided=0 and e.encounter_datetime<=:startDate and e.location_id=:location 
            				  GROUP BY 	p.patient_id 
            				union 
            				Select 	p.patient_id,min(value_datetime) data_inicio 
            				from 	patient p 
            						inner join encounter e on p.patient_id=e.patient_id 
            						inner join obs o on e.encounter_id=o.encounter_id 
            				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
            						o.concept_id=23866 and o.value_datetime is not null and 
            						o.value_datetime<=:startDate and e.location_id=:location 
            				group by p.patient_id 
            			) inicio 
            		group by patient_id 
            	) inicio_real on txCurr.patient_id=inicio_real.patient_id 
            	inner join(
                   select maxNextConsultaOrLevantamento.patient_id from(
            		select patient_id,max(data_proxima_consulta) data_proxima_consulta 
            		from 
            		( 
            			Select 	ultimavisita.patient_id,ultimavisita.encounter_datetime data_consulta ,o.value_datetime data_proxima_consulta 
            			from 
            				(	select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            					from 	encounter e 
            							inner join patient p on p.patient_id=e.patient_id 
            					where 	e.voided=0 and p.voided=0 and e.encounter_type=18 and e.location_id=:location and e.encounter_datetime<=:startDate 
            					group by p.patient_id 
            				) ultimavisita 
            				inner join encounter e on e.patient_id=ultimavisita.patient_id 
            				inner join obs o on o.encounter_id=e.encounter_id 
            			where 	o.concept_id=5096 and o.voided=0 and e.encounter_datetime=ultimavisita.encounter_datetime and 
            					e.encounter_type=18 and e.location_id=:location and o.value_datetime between :startDate and :endDate 
            			union 

            			select patient_id, data_levantamento, max(data_levantamento + INTERVAL 30 day) data_proximo_levantamento from (
            			Select 	p.patient_id,value_datetime data_levantamento
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=52 and 
            					o.concept_id=23866 and o.value_datetime is not null and 
            					o.value_datetime<= :startDate and e.location_id=:location 
            					order by p.patient_id, value_datetime desc
            					) maxFicha where (data_levantamento + INTERVAL 30 day) between  :startDate and :endDate 
            					group by maxFicha.patient_id
            			
            			union 
            			Select 	ultimavisita.patient_id,ultimavisita.encounter_datetime,o.value_datetime 
            			from 
            				(	select 	p.patient_id,max(encounter_datetime) as encounter_datetime 
            					from 	encounter e 
            							inner join patient p on p.patient_id=e.patient_id 
            					where 	e.voided=0 and p.voided=0 and e.encounter_type in (6,9) and e.location_id=:location and e.encounter_datetime<=:startDate 
            					group by p.patient_id 
            				) ultimavisita 
            				inner join encounter e on e.patient_id=ultimavisita.patient_id 
            				left join obs o on o.encounter_id=e.encounter_id and o.concept_id=1410 and o.voided=0 
            			where  	e.encounter_datetime=ultimavisita.encounter_datetime and 
            					e.encounter_type in (6,9) and e.location_id=:location and o.value_datetime between :startDate and :endDate 
            			group by ultimavisita.patient_id 
            		) consultaRecepcao 
            		group by patient_id 
              )maxNextConsultaOrLevantamento
	left join 
            	( 
            			select maxCarga.patient_id,maxCarga.data_carga,o.concept_id,if(o.concept_id=856,o.value_numeric,'Indetectavel') valor_carga 
            		from 
            		( 
            			Select 	p.patient_id,max(date(o.obs_datetime)) data_carga
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,6,9,53,51) and 
            					o.concept_id in (1305,856) and date(o.obs_datetime) <=:endDate and e.location_id=:location 
            			group by p.patient_id 
            		)maxCarga 
            		inner join encounter e on maxCarga.patient_id=e.patient_id 
            		inner join obs o on o.encounter_id=e.encounter_id 
            		where 	date(o.obs_datetime)=maxCarga.data_carga and 
            				o.concept_id in (1305,856) and e.encounter_type in (13,6,9,53,51) and e.location_id=:location and 
            				e.voided=0 and o.voided=0 
            	) carga_viral on maxNextConsultaOrLevantamento.patient_id=carga_viral.patient_id
				where
            		( 
            			(carga_viral.concept_id=1305 and date(carga_viral.data_carga) < date_add(maxNextConsultaOrLevantamento.data_proxima_consulta, interval -12 MONTH)) 
            			or 
            			(carga_viral.concept_id=856 and carga_viral.valor_carga<1000  and date(carga_viral.data_carga) < date_add(maxNextConsultaOrLevantamento.data_proxima_consulta, interval -12 MONTH)) 
            			or 
            			(carga_viral.concept_id=856 and carga_viral.valor_carga>=1000  and date(carga_viral.data_carga) < date_add(maxNextConsultaOrLevantamento.data_proxima_consulta, interval -3 MONTH)) 
            		) group by maxNextConsultaOrLevantamento.patient_id
            		
            		union

              select patient_id from patient p where p.voided = 0 and p.patient_id not in (
			Select 	p.patient_id 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,6,9,53,51) and 
            					o.concept_id in (856,1305) and 
            					date(o.obs_datetime)<=:endDate and e.location_id=:location 
            					) 
            	) cargaViral on cargaViral.patient_id = txCurr.patient_id
            	left join 
            	( 
            		select patient_id,min(data_gravida) dataGravidaLactante 
            		from 
            		( 
            			Select 	p.patient_id,e.encounter_datetime data_gravida 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982 and value_coded=1065 and 
            					e.encounter_type in (5,6) and e.encounter_datetime  between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id,e.encounter_datetime data_gravida 
            			from 	patient p inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 and 
            					e.encounter_type in (5,6) and e.encounter_datetime between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id,e.encounter_datetime data_gravida 
            			from 	patient p inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 and 
            					e.encounter_type in (5,6) and e.encounter_datetime between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id,e.encounter_datetime data_gravida 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331 and 
            					e.encounter_type in (5,6) and e.encounter_datetime between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location 
            			union 
            			select 	pp.patient_id,pp.date_enrolled data_gravida 
            			from 	patient_program pp 
            			where 	pp.program_id=8 and pp.voided=0 and 
            					pp.date_enrolled between date_add(:endDate, interval -9 month) AND :endDate and pp.location_id=:location 
            			union 
            			Select 	p.patient_id,obsART.value_datetime data_gravida 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            					inner join obs obsART on e.encounter_id=obsART.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 and o.value_coded=1065 and 
            					e.encounter_type=53 and obsART.value_datetime between date_add(:endDate, interval -9 month) AND :endDate and e.location_id=:location and 
            					obsART.concept_id=1190 and obsART.voided=0 
            			union 
            			select p.patient_id,data_colheita.value_datetime data_gravida from patient p 
            				inner join encounter e on p.patient_id=e.patient_id 
            				inner join obs o on e.encounter_id=o.encounter_id 
            				inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id 
            			where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51 
            			  and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id,o.value_datetime data_parto 
            			from 	patient p inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 and 
            					e.encounter_type in (5,6) and o.value_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id, e.encounter_datetime data_parto 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 and value_coded=1065 and 
            					e.encounter_type=6 and e.encounter_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            			union 
            			Select 	p.patient_id, obsART.value_datetime data_parto 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            					inner join obs obsART on e.encounter_id=obsART.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 and o.value_coded=1065 and 
            					e.encounter_type=53 and e.location_id=:location and 
            					obsART.value_datetime between date_add(:endDate, interval -18 month) AND :endDate and 
            					obsART.concept_id=1190 and obsART.voided=0 
            			union 
            			Select 	p.patient_id, e.encounter_datetime data_parto 
            			from 	patient p 
            					inner join encounter e on p.patient_id=e.patient_id 
            					inner join obs o on e.encounter_id=o.encounter_id 
            			where 	p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6332 and 
            					e.encounter_type in (5,6) and e.encounter_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            			union 
            			select 	pg.patient_id,ps.start_date data_parto 
            			from 	patient p 
            					inner join patient_program pg on p.patient_id=pg.patient_id 
            					inner join patient_state ps on pg.patient_program_id=ps.patient_program_id 
            			where 	pg.voided=0 and ps.voided=0 and p.voided=0 and 
            					pg.program_id=8 and ps.state=27 and 
            					ps.start_date between date_add(:endDate, interval -18 month) AND :endDate and location_id=:location 
            		     union 
                           select p.patient_id,data_colheita.value_datetime data_parto from patient p 
            				inner join encounter e on p.patient_id=e.patient_id 
            				inner join obs o on e.encounter_id=o.encounter_id 
            				inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id 
            			where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51 
            						  and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(:endDate, interval -18 month) AND :endDate and e.location_id=:location 
            		) lactante_real 
            		inner join person pe on pe.person_id=lactante_real.patient_id and pe.voided=0 and pe.gender='F' 
            		group by lactante_real.patient_id 
            	) gravida on txCurr.patient_id=gravida.patient_id 
            	where gravida.patient_id is null and timestampdiff(month,inicio_real.data_inicio,consultaLevantamento.data_proxima_consulta)>= 6 and consultaLevantamento.data_proxima_consulta between :startDate and :endDate
            ) elegivelAColheitaCV 
            group by elegivelAColheitaCV.patient_id