 select *
 from
 (
 /* Primeiro alert  HVL_FR14*/
		select distinct
				HVL_FR41.patient_id,
				HVL_FR41.NID,
				HVL_FR41.NAME,
				HVL_FR41.GENDER,
				HVL_FR41.AGE,
				HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
				HVL_FR41.localidade,
				HVL_FR41.bairro,
				HVL_FR41.celula,
				HVL_FR41.TB,
				HVL_FR41.gravida_lactante,
				DATE_FORMAT(HVL_FR41.data_inicio, '%d/%m/%Y') as data_inicio,
				DATE_FORMAT(HVL_FR41.data_carga, '%d/%m/%Y') as data_carga,
				HVL_FR41.valorCV,
				DATE_FORMAT(HVL_FR41.dataPrevistaConsultaClinica0CV1, '%d/%m/%Y') as proximaConsultaSeguimento,
				HVL_FR41.alert
		from
		(	
				select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.dataConsultaClinica0CV1,
		date(HVL_FR40.dataPrevistaConsultaClinica0CV1) as dataPrevistaConsultaClinica0CV1,
		if( HVL_FR40.dataPrevistaConsultaClinica0CV1 >= :endDate and HVL_FR40.dataPrevistaConsultaClinica0CV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaClinica0CV1 is null ,'Consulta Clínica com CV registada na Ficha Clínica e Comunicada ao Paciente', null  ) alert

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													date(HVL_FR4_HVL_FR5.data_inicio) as data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													date(primeiraCVAlta.data_carga) as data_carga,
																													primeiraCVAlta.valorCV,
																													date(primeiraColheitaCV.data_colheita) as data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
		)HVL_FR41
			where HVL_FR41.alert is not null
			
	union all


  /* Segundo alert  HVL_FR15*/
		select
				HVL_FR41.patient_id,
				HVL_FR41.NID,
				HVL_FR41.NAME,
				HVL_FR41.GENDER,
				HVL_FR41.AGE,
				HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
				HVL_FR41.localidade,
				HVL_FR41.bairro,
				HVL_FR41.celula,
				HVL_FR41.TB,
				HVL_FR41.gravida_lactante,
				HVL_FR41.data_inicio,
				HVL_FR41.data_carga,
				HVL_FR41.valorCV,
				HVL_FR41.dataPrevistaConsultaApss0CV1 proximaConsultaSeguimento,
				HVL_FR41.alert
		from
		(	
				select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.dataConsultaApss0CV1,
		HVL_FR40.dataPrevistaConsultaApss0CV1, 
		if( HVL_FR40.dataPrevistaConsultaApss0CV1 >= :endDate and HVL_FR40.dataPrevistaConsultaApss0CV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaApss0CV1 is null ,'Aconselhamento para Reforço a Adesão após primeira CV alta - Sessão 0 de APSS ocorrida na Data do Resultado da CV acima de 1000', null  ) alert

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
		)HVL_FR41
			where HVL_FR41.alert is not null	

	
    union all
 /* Segundo alert  HVL_FR16*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.previstaConsultaApss1CV1 proximaConsultaSeguimento,
		HVL_FR41.alert
from
(	
				select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.dataConsultaApss1CV1,
		HVL_FR40.previstaConsultaApss1CV1, 
		if( HVL_FR40.previstaConsultaApss1CV1 >= :endDate and HVL_FR40.previstaConsultaApss1CV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaApss1CV1 is null ,'Aconselhamento para Reforço a Adesão após primeira CV alta - Data da 1ª Consulta de APSS Após CV acima de 1000', null  ) alert

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
		)HVL_FR41
			where HVL_FR41.alert is not null
 
 
     union all
 /* Segundo alert  HVL_FR17*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.previstaConsultaApss2CV1 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(					select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.dataConsultaApss2CV1,
		HVL_FR40.previstaConsultaApss2CV1, 
		if( HVL_FR40.previstaConsultaApss2CV1 >= :endDate and HVL_FR40.previstaConsultaApss2CV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaApss2CV1 is null ,'Aconselhamento para Reforço a Adesão após primeira CV alta - Data da 2ª Consulta de APSS Apos CV acima de 1000', null  )  alert

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
 
     union all
 /* Segundo alert  HVL_FR18*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.previstaConsultaApss3CV1 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(				select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.dataConsultaApss3CV1, 	
		HVL_FR40.previstaConsultaApss3CV1, 
		if( HVL_FR40.previstaConsultaApss3CV1 >= :endDate and HVL_FR40.previstaConsultaApss3CV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaApss3CV1 is null ,'Aconselhamento para Reforço a Adesão após primeira CV alta - Data da 3ª Consulta de APSS/PP Após CV acima de 1000', null  )  alert

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
			
			
		
union all
 /* Segundo alert  HVL_FR20*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaConsultaClinicaPedidoCV1 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(		select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.dataConsultaClinicaPedidoCV1,
		HVL_FR40.dataPrevistaConsultaClinicaPedidoCV1,
		if( HVL_FR40.dataPrevistaConsultaClinicaPedidoCV1 >= :endDate and HVL_FR40.dataPrevistaConsultaClinicaPedidoCV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaClinicaPedidoCV1 is null ,'Repetição da CV - Segunda Carga Viral - Data da consulta clínica para repetição da 2ª CV (pedido) ficha clínica', null  )  alert

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
		

 union all
 /* Segundo alert  HVL_FR21*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaColheitaAmostraCV1 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(			select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.dataColheitaRegistadaCV1,
		HVL_FR40.dataPrevistaColheitaAmostraCV1, 
		if( HVL_FR40.dataPrevistaColheitaAmostraCV1 >= :endDate and HVL_FR40.dataPrevistaColheitaAmostraCV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataColheitaRegistadaCV1 is null ,'Repetição da CV - Segunda Carga Viral- Data de Colheita da 2ª CV (se aplicável) do Laboratório', null  )  alert

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null		
	

 union all
 /* Segundo alert  HVL_FR22*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaResultadoCV1 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(
		select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.dataResultadoSegundaCV1,
		HVL_FR40.dataPrevistaResultadoCV1, 
		if( HVL_FR40.dataPrevistaResultadoCV1 >= :endDate and HVL_FR40.dataPrevistaResultadoCV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataResultadoSegundaCV1 is null ,'Repeticao da CV - Segunda Carga Viral - Data do resultado da 2ª CV (Lab ou FSR)', null  ) alert
from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null	
			

 union all
 /* Segundo alert  HVL_FR25*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaConsultaMudancaLinhaCV1 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(			select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataConsultaClinicaParaMudancaLinhaCV1,
		HVL_FR40.dataPrevistaConsultaMudancaLinhaCV1,  
		if( HVL_FR40.resultadoSegundaCV1 >= 1000 and HVL_FR40.dataPrevistaConsultaMudancaLinhaCV1 >= :endDate and HVL_FR40.dataPrevistaConsultaMudancaLinhaCV1 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaClinicaParaMudancaLinhaCV1 is null ,'Mudança de Linha - A: Data da consulta Clínica para Mudança de linha', null  ) alert
			
from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
	

  union all
 /* Segundo alert  HVL_FR27*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaApss0CV2 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(			select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataConsultaApss0CV2,
		HVL_FR40.dataPrevistaApss0CV2,  
		if( HVL_FR40.resultadoSegundaCV1 >= 1000 and HVL_FR40.dataPrevistaApss0CV2 >= :endDate and HVL_FR40.dataPrevistaApss0CV2 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaApss0CV2 is null ,'Aconselhamento para Reforço a Adesão - Seguimento após resposta Comité TARV: Data da Sessão 0 da APSS/PP após segunda CV alta (se aplicável)', null  ) alert
		
from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null	
			

   union all
   
 /* Segundo alert  HVL_FR28*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaApss1CV2 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(	
	select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataConsultaApss1CV2,
		HVL_FR40.dataPrevistaApss1CV2,  
		if( HVL_FR40.resultadoSegundaCV1 >= 1000 and HVL_FR40.dataPrevistaApss1CV2 >= :endDate and HVL_FR40.dataPrevistaApss1CV2 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaApss1CV2 is null ,'Aconselhamento para Reforço a Adesão - Seguimento após resposta Comité TARV - Data da 1ª Consulta de APSS Após segunda CV Alta', null  ) alert	

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
			

   union all
 /* Segundo alert  HVL_FR29*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaApss2CV2 proximaConsultaSeguimento,
		HVL_FR41.alert
from
(	
	select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataConsultaApss2CV2,
		HVL_FR40.dataPrevistaApss2CV2, 
		if( HVL_FR40.resultadoSegundaCV1 >= 1000 and HVL_FR40.dataPrevistaApss2CV2 >= :endDate and HVL_FR40.dataPrevistaApss2CV2 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaApss2CV2 is null ,'Aconselhamento para Reforço a Adesão - Seguimento após resposta Comité TARV - Data da 2ª Consulta de APSS/PP Após segunda CV alta:', null  )   alert
from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
			
			
   union all
 /* Segundo alert  HVL_FR30*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaApss3CV2 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(	
	select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataConsultaApss3CV2,
		HVL_FR40.dataPrevistaApss3CV2,  
		if( HVL_FR40.resultadoSegundaCV1 >= 1000 and HVL_FR40.dataPrevistaApss3CV2 >= :endDate and HVL_FR40.dataPrevistaApss3CV2 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaApss3CV2 is null ,'Aconselhamento para Reforço a Adesão - Seguimento após resposta Comité TARV - Data da 3ª Consulta de APSS/PP Após CV acima de 1000:', null  )  alert		
from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
		

   union all
 
 /* Segundo alert  HVL_FR_31*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaConsultaClinicaPedidoCV2 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(	
	select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataConsultaClinicaPedidoCV2,
		HVL_FR40.dataPrevistaConsultaClinicaPedidoCV2,  
		if(HVL_FR40.resultadoSegundaCV1 >= 1000 and HVL_FR40.dataPrevistaConsultaClinicaPedidoCV2 >= :endDate and HVL_FR40.dataPrevistaConsultaClinicaPedidoCV2 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaClinicaPedidoCV2 is null ,'Aconselhamento para Reforço a Adesão - Seguimento após resposta Comité TARV - Data da consulta clínica para pedido da CV (para os não aprovados) ficha clínica', null  )  alert
from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
		
		
  union all
 /* Segundo alert  HVL_FR_32*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaColheitaAmostraCV2 proximaConsultaSeguimento,
		HVL_FR41.alert
from
(	
	select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataColheitaRegistadaCV2,
		HVL_FR40.dataPrevistaColheitaAmostraCV2,  
		if( HVL_FR40.resultadoSegundaCV1 >= 1000  and HVL_FR40.dataPrevistaColheitaAmostraCV2 >= :endDate and HVL_FR40.dataPrevistaColheitaAmostraCV2 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataColheitaRegistadaCV2 is null ,'Repetição da CV - Terceira Carga Viral: Data de Colheita da terceira CV (se aplicável) do Laboratório', null  )  alert
			
from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
			
	
 union all
 /* Segundo alert  HVL_FR_33*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaResultadoTerceiraCV proximaConsultaSeguimento,
		HVL_FR41.alert
from
(	
	select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataResultadoTerceiraCV,
		HVL_FR40.dataPrevistaResultadoTerceiraCV,   
		if( HVL_FR40.resultadoSegundaCV1 >= 1000  and HVL_FR40.dataPrevistaResultadoTerceiraCV >= :endDate and HVL_FR40.dataPrevistaResultadoTerceiraCV <= date_add(:endDate, interval 7 day) and HVL_FR40.dataResultadoTerceiraCV is null ,'Repetição da CV - Terceira Carga Viral: Data de Recepção do resultado da CV (Lab ou FSR)', null  ) alert	
from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null		


   union all
 /* Segundo alert  HVL_FR_36*/
 select
		HVL_FR41.patient_id,
		HVL_FR41.NID,
		HVL_FR41.NAME,
		HVL_FR41.GENDER,
		HVL_FR41.AGE,
			HVL_FR41.telefone,
				HVL_FR41.telefoneAlternativo,
		HVL_FR41.localidade,
		HVL_FR41.bairro,
		HVL_FR41.celula,
		HVL_FR41.TB,
		HVL_FR41.gravida_lactante,
		HVL_FR41.data_inicio,
		HVL_FR41.data_carga,
		HVL_FR41.valorCV,
		HVL_FR41.dataPrevistaConsultaMudancaLinhaCV2 proximaConsultaSeguimento,
		HVL_FR41.alert
from

(	
	select 		
		HVL_FR40.patient_id,
		pid.identifier as NID,
         concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
         p.gender as GENDER, 
         floor(datediff(:endDate,birthdate)/365) AGE,
         pat.value as telefone, 
         pat2.value as telefoneAlternativo, 
         pad3.address6 as 'localidade',
         pad3.address5 as 'bairro',
         pad3.address3 as 'celula', 
		if(tbFinal.patient_id is not null, 'SIM', '') as TB,
          if(gravida_real.data_gravida is null and lactante_real.data_parto is null, null,
    			if(gravida_real.data_gravida is null, 'Lactante', if(lactante_real.data_parto is null,
    			'Grávida', if(max(lactante_real.data_parto)>max(gravida_real.data_gravida),'Lactante','Grávida')))) gravida_lactante,
		HVL_FR40.data_inicio,
		HVL_FR40.data_colheita,
		HVL_FR40.data_carga,
		HVL_FR40.linha,
		HVL_FR40.valorCV,
		HVL_FR40.resultadoSegundaCV1,
		HVL_FR40.dataConsultaClinicaParaMudancaLinhaCV2,				
		HVL_FR40.dataPrevistaConsultaMudancaLinhaCV2,   
		if( HVL_FR40.resultadoSegundaCV1 >= 1000  and HVL_FR40.dataPrevistaConsultaMudancaLinhaCV2 >= :endDate and HVL_FR40.dataPrevistaConsultaMudancaLinhaCV2 <= date_add(:endDate, interval 7 day) and HVL_FR40.dataConsultaClinicaParaMudancaLinhaCV2 is null ,'Mudança da Linha - B: Data da Consulta Clínica para Mudança de Linha)', null  ) alert

from

(
	select 		
			HVL_FR36.patient_id,
			HVL_FR36.data_inicio,
			HVL_FR36.data_colheita,
			HVL_FR36.data_carga,
			HVL_FR36.linha,
			HVL_FR36.valorCV,
			HVL_FR36.dataConsultaClinica0 dataConsultaClinica0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day)  dataPrevistaConsultaClinica0CV1,
			HVL_FR36.dataConsultaApss0 dataConsultaApss0CV1,
			date_add(HVL_FR36.data_carga,interval 7 day) dataPrevistaConsultaApss0CV1, 
			HVL_FR36.dataConsultaApss1 dataConsultaApss1CV1,
			date_add(HVL_FR36.dataConsultaApss0,interval 30 day) previstaConsultaApss1CV1, 
			HVL_FR36.dataConsultaApss2 dataConsultaApss2CV1,
			date_add(HVL_FR36.dataConsultaApss1,interval 30 day) previstaConsultaApss2CV1, 		
			HVL_FR36.dataConsultaApss3 dataConsultaApss3CV1, 	
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) previstaConsultaApss3CV1, 
			HVL_FR36.adesao,
			HVL_FR36.dataConsultaClinicaPedido dataConsultaClinicaPedidoCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR36.dataColheitaRegistada dataColheitaRegistadaCV1,
			date_add(HVL_FR36.dataConsultaApss2,interval 30 day) dataPrevistaColheitaAmostraCV1, 
			HVL_FR36.dataResultadoSegundaCV dataResultadoSegundaCV1,
			date_add(HVL_FR36.dataConsultaApss3,interval 30 day) dataPrevistaResultadoCV1, 
			HVL_FR36.resultadoSegundaCV resultadoSegundaCV1,
			HVL_FR36.mudancaLinha mudancaLinhaCV1,
			HVL_FR36.dataConsultaClinicaParaMudancaLinha dataConsultaClinicaParaMudancaLinhaCV1,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV1, 
			HVL_FR36.dataInicioNovaLinha dataInicioNovaLinhaCV1,
			HVL_FR36.dataConsultaApss2CV0 dataConsultaApss0CV2,
			date_add(HVL_FR36.dataResultadoSegundaCV,interval 30 day) dataPrevistaApss0CV2, 
			HVL_FR36.dataConsultaApss2CV1 dataConsultaApss1CV2,
			date_add(HVL_FR36.dataConsultaApss2CV0,interval 30 day) dataPrevistaApss1CV2, 
			HVL_FR36.dataConsultaApss2CV2,
			date_add(HVL_FR36.dataConsultaApss2CV1,interval 30 day) dataPrevistaApss2CV2, 
			HVL_FR36. dataConsultaApss2CV3 dataConsultaApss3CV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaApss3CV2, 
			HVL_FR36.dataConsultaClinicaPedidoCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaConsultaClinicaPedidoCV2, 
			HVL_FR36.dataColheitaRegistadaCV2,
			date_add(HVL_FR36.dataConsultaApss2CV2,interval 30 day) dataPrevistaColheitaAmostraCV2,
			HVL_FR36.dataResultadoTerceiraCV,
			date_add(HVL_FR36.dataConsultaApss2CV3,interval 30 day) dataPrevistaResultadoTerceiraCV, 
			HVL_FR36.resultadoTerceiraCV,
			HVL_FR36.mudancaLinhaCV2,		
			HVL_FR36.dataConsultaClinicaParaMudancaLinhaCV2,				
			date_add(HVL_FR36.dataResultadoTerceiraCV,interval 30 day) dataPrevistaConsultaMudancaLinhaCV2, 
			if (HVL_FR36.linha='1ª Linha' and inicioNovaLinhaCV2.value_coded=21148, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),
				if (HVL_FR36.linha='2ª Linha' and inicioNovaLinhaCV2.value_coded=21149, min(inicioNovaLinhaCV2.dataInicioNovaLinhaCV2),'N/A')) dataInicioNovaLinhaCV2
	from

	(

		select 		
				HVL_FR35.*,
				if(HVL_FR35.resultadoTerceiraCV='Indetectavel' or HVL_FR35.resultadoTerceiraCV<1000,'N',
						if(HVL_FR35.resultadoTerceiraCV>=1000,'S','N/A'))  mudancaLinhaCV2,		
				min(consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2				
		from

		(

			select 		
					HVL_FR34.*,
					if(resultadoTerceiraCVValor.concept_id=1305,'Indetectavel',resultadoTerceiraCVValor.value_numeric) resultadoTerceiraCV
			from

			(
				select 		
						HVL_FR33.*,
						min(resultadoTerceiraCVData.dataResultadoTerceiraCV) dataResultadoTerceiraCV
				from

				(
					select 		
							HVL_FR32.*,
							min(HVL_FR32.colheitaRegistadaCV2.dataColheitaRegistadaCV2)  dataColheitaRegistadaCV2
					from
					(
						select 		
								HVL_FR31.*,
								min(consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2
						from
						(
							select 		
									HVL_FR30.*,
									min(consultaApss2CV3.encounter_datetime) dataConsultaApss2CV3
							from 
							(
								select 	HVL_FR29.*,
										min(consultaApss2CV2.encounter_datetime) dataConsultaApss2CV2
								from 
								(
									select 
												HVL_FR28.*,
												min(consultaApss2CV1.encounter_datetime) dataConsultaApss2CV1
									from 
									(
											select 
																			HVL_FR26.*,
																			if (HVL_FR26.linha='1ª Linha' and inicioNovaLinha.value_coded=21148, min(inicioNovaLinha.dataInicioNovaLinha),
																			if (HVL_FR26.linha='2ª Linha' and inicioNovaLinha.value_coded=21149, min(inicioNovaLinha.dataInicioNovaLinha),'N/A')) dataInicioNovaLinha,
																			min(consultaApss2CV0.encounter_datetime) dataConsultaApss2CV0
																			
										from

										(
												select 
																				HVL_FR25.*,
																				min(consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha) dataConsultaClinicaParaMudancaLinha
																			
												from
												
												(
														select 
																					HVL_FR24.*,
																					if(HVL_FR24.resultadoSegundaCV='Indetectavel' or HVL_FR24.resultadoSegundaCV<1000,'N',
																						if(HVL_FR24.resultadoSegundaCV>=1000,'S','N/A'))  mudancaLinha
														from
														
														(
																select 
																						HVL_FR23.*,
																						if(resultadoSegundaCVValor.concept_id=1305,'Indetectavel',resultadoSegundaCVValor.value_numeric) resultadoSegundaCV
																from
																(
																		select	 		HVL_FR22.*,
																							min(resultadoSegundaCVData.dataResultadoSegundaCV) dataResultadoSegundaCV
																			
																		from
																			
																		(
																			select	 		HVL_FR21.*,
																								min(colheitaRegistada.dataColheitaRegistada) dataColheitaRegistada
																			from
																				
																				(		
																						select	 	HVL_FR20.*,
																									avalidacaoAdesao.adesao,
																									min(consultaClinicaPedido.dataConsultaClinicaPedido) dataConsultaClinicaPedido
																									
																						from
																						(
																								select 	HVL_FR19.*,
																								max(adesaoAAvaliar.dataApssAdesao) dataApssAvaliarAdesao
																						from
																							(
																								select 	HVL_FR17.*,
																										min(consultaApss3.encounter_datetime) dataConsultaApss3,
																										if(min(consultaApss3.encounter_datetime) is not null,min(consultaApss3.encounter_datetime),
																											if(dataConsultaApss2 is not null, dataConsultaApss2,
																												if(dataConsultaApss1 is not null,dataConsultaApss1,
																													if(dataConsultaApss0 is not null ,dataConsultaApss0,null)))) dataMaiorApssAvaliarAdesao
																								from
																								(
																									select 	HVL_FR16.*,
																											min(consultaApss2.encounter_datetime) dataConsultaApss2
																									from
																									(
																										select 	HVL_FR15.*,
																												min(consultaApss1.encounter_datetime) dataConsultaApss1
																										from
																										(
																										select HVL_FR3.*,
																												min(consultaClinica0.encounter_datetime) dataConsultaClinica0,
																												min(consultaApss0.encounter_datetime) dataConsultaApss0
																										from
																										(
																											Select 	HVL_FR4_HVL_FR5.patient_id,
																													HVL_FR4_HVL_FR5.data_inicio,
																													HVL_FR4_HVL_FR5.linha,
																													primeiraCVAlta.data_carga,
																													primeiraCVAlta.valorCV,
																													primeiraColheitaCV.data_colheita
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
																											left join 
																											(
																												select p.patient_id,min(obsSampleCollectDate.value_datetime) data_colheita
																												from patient p
																													inner join encounter e on p.patient_id=e.patient_id	
																													inner join obs obsSampleCollectDate on obsSampleCollectDate.encounter_id=e.encounter_id
																													inner join obs obsVL on obsVL.encounter_id=e.encounter_id
																												where p.voided=0 and e.voided=0 and obsSampleCollectDate.voided=0 and obsVL.voided = 0 and e.encounter_type in (13,51) 
																														and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																														and e.location_id=:location and obsSampleCollectDate.value_datetime BETWEEN :startDate and :endDate
																														group by p.patient_id
																											) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR4_HVL_FR5.patient_id
																											
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
																										group by HVL_FR3.patient_id
																										) HVL_FR15			
																										left join encounter consultaApss1 on consultaApss1.patient_id=HVL_FR15.patient_id and consultaApss1.encounter_type=35 and consultaApss1.location_id=:location and consultaApss1.voided=0
																													and consultaApss1.encounter_datetime  between date_add(HVL_FR15.dataConsultaApss0, interval  1 day) and :endDate
																										group by HVL_FR15.patient_id
																									)HVL_FR16
																									left join encounter consultaApss2 on consultaApss2.patient_id=HVL_FR16.patient_id and consultaApss2.encounter_type=35 and consultaApss2.location_id=:location and consultaApss2.voided=0
																													and consultaApss2.encounter_datetime between date_add(HVL_FR16.dataConsultaApss1, interval  1 day) and :endDate
																										group by HVL_FR16.patient_id
																								) HVL_FR17
																								left join encounter consultaApss3 on consultaApss3.patient_id=HVL_FR17.patient_id and consultaApss3.encounter_type=35 and consultaApss3.location_id=:location and consultaApss3.voided=0
																													and consultaApss3.encounter_datetime between date_add(HVL_FR17.dataConsultaApss2, interval  1 day) and :endDate
																										group by HVL_FR17.patient_id
																							) HVL_FR19
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataApssAdesao
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) adesaoAAvaliar on adesaoAAvaliar.patient_id=HVL_FR19.patient_id and adesaoAAvaliar.dataApssAdesao between HVL_FR19.dataConsultaApss0  and HVL_FR19.dataMaiorApssAvaliarAdesao
																								group by HVL_FR19.patient_id
																							
																						)HVL_FR20
																						left join
																						(
																								Select 	p.patient_id,e.encounter_datetime dataApssAvaliarAdesao,
																										case o.value_coded
																											when 1383 then 'B'
																											when 1749 then 'R'
																											when 1385 then 'M'				
																											else null end as adesao			
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=35 and o.concept_id=6223 and e.location_id=:location
																							) avalidacaoAdesao on avalidacaoAdesao.patient_id=HVL_FR20.patient_id and avalidacaoAdesao.dataApssAvaliarAdesao = HVL_FR20.dataApssAvaliarAdesao
																							left join
																							(
																								Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedido
																								from 	patient p
																										inner join encounter e on p.patient_id=e.patient_id	
																										inner join obs o on o.encounter_id=e.encounter_id
																								where 	e.voided=0 and o.voided=0 and p.voided=0 and
																										e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
																							) consultaClinicaPedido on consultaClinicaPedido.patient_id=HVL_FR20.patient_id and 
																								consultaClinicaPedido.dataConsultaClinicaPedido between HVL_FR20.dataConsultaApss2 and :endDate
																							group by HVL_FR20.patient_id
																				) HVL_FR21  
																				left join
																				(
																					select p.patient_id, o.value_datetime dataColheitaRegistada
																					from 	patient p
																							inner join encounter e on p.patient_id=e.patient_id	
																							inner join obs o on o.encounter_id=e.encounter_id
																						where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
																							and o.concept_id=23821 and e.location_id=:location
																				)	colheitaRegistada on colheitaRegistada.patient_id=HVL_FR21.patient_id 
																					and colheitaRegistada.dataColheitaRegistada between date_add(HVL_FR21.dataConsultaApss2, interval  1 day) and :endDate
																					group by HVL_FR21.patient_id	
																		)HVL_FR22
																		left join
																		(
																			Select 	p.patient_id,o.obs_datetime dataResultadoSegundaCV
																			from 	patient p
																					inner join encounter e on p.patient_id=e.patient_id
																					inner join obs o on e.encounter_id=o.encounter_id
																			where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																					o.concept_id in (856,1305)
																		) resultadoSegundaCVData on resultadoSegundaCVData.patient_id = HVL_FR22.patient_id and resultadoSegundaCVData.dataResultadoSegundaCV between HVL_FR22.dataConsultaApss3 and :endDate
																		group by HVL_FR22.patient_id
																) HVL_FR23
																left join
																(
																	select p.patient_id,o.obs_datetime dataResultadoSegundaCV, o.concept_id,o.value_numeric
												
																	from	patient p
																			inner join encounter e on p.patient_id=e.patient_id
																			inner join obs o on e.encounter_id=o.encounter_id
																	where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
																			o.concept_id in (856,1305)
																) resultadoSegundaCVValor on resultadoSegundaCVValor.patient_id = HVL_FR23.patient_id and resultadoSegundaCVValor.dataResultadoSegundaCV = HVL_FR23.dataResultadoSegundaCV
														)HVL_FR24
												)HVL_FR25
												left join
												(
													select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinha
													from 	patient p
															inner join encounter e on p.patient_id=e.patient_id
													where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
															e.encounter_datetime <=:endDate and e.location_id=:location 
												) consultaClinicaParaMudancaLinha on  consultaClinicaParaMudancaLinha.patient_id = HVL_FR25.patient_id and 
													consultaClinicaParaMudancaLinha.dataConsultaClinicaParaMudancaLinha between HVL_FR25.dataResultadoSegundaCV and :endDate
													group by HVL_FR25.patient_id
										)HVL_FR26
										left join 
										(
											select 	p.patient_id,e.encounter_datetime dataInicioNovaLinha,o.value_coded
											from 	patient p
													inner join encounter e on p.patient_id= e.patient_id
													inner join obs o on e.encounter_id=o.encounter_id
											where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
													o.concept_id= 21151 and o.value_coded in (21148,21149) and 
													e.encounter_datetime <=:endDate and e.location_id=:location
										) inicioNovaLinha on inicioNovaLinha.patient_id = HVL_FR26.patient_id and inicioNovaLinha.dataInicioNovaLinha between HVL_FR26.dataResultadoSegundaCV and :endDate 
										left join encounter consultaApss2CV0 on consultaApss2CV0.patient_id=HVL_FR26.patient_id and 
													consultaApss2CV0.encounter_type=35 and consultaApss2CV0.location_id=:location and consultaApss2CV0.voided=0 and 
													consultaApss2CV0.encounter_datetime BETWEEN HVL_FR26.dataResultadoSegundaCV and :endDate
										group by HVL_FR26.patient_id
									)HVL_FR28
									left join encounter consultaApss2CV1 on consultaApss2CV1.patient_id=HVL_FR28.patient_id and 
													consultaApss2CV1.encounter_type=35 and consultaApss2CV1.location_id=:location and consultaApss2CV1.voided=0 and 
													consultaApss2CV1.encounter_datetime BETWEEN  date_add(HVL_FR28.dataConsultaApss2CV0, interval  1 day) and :endDate
									group by HVL_FR28.patient_id
								)HVL_FR29
								left join encounter consultaApss2CV2 on consultaApss2CV2.patient_id=HVL_FR29.patient_id and 
													consultaApss2CV2.encounter_type=35 and consultaApss2CV2.location_id=:location and consultaApss2CV2.voided=0 and 
													consultaApss2CV2.encounter_datetime BETWEEN  date_add(HVL_FR29.dataConsultaApss2CV1, interval  1 day) and :endDate
									group by HVL_FR29.patient_id
							)HVL_FR30
							left join encounter consultaApss2CV3 on consultaApss2CV3.patient_id=HVL_FR30.patient_id and 
													consultaApss2CV3.encounter_type=35 and consultaApss2CV3.location_id=:location and consultaApss2CV3.voided=0 and 
													consultaApss2CV3.encounter_datetime BETWEEN date_add(HVL_FR30.dataConsultaApss2CV2, interval  1 day)  and :endDate
									group by HVL_FR30.patient_id
						)HVL_FR31
						left join
						(
							Select 	p.patient_id, e.encounter_datetime dataConsultaClinicaPedidoCV2
							from 	patient p
									inner join encounter e on p.patient_id=e.patient_id	
									inner join obs o on o.encounter_id=e.encounter_id
							where 	e.voided=0 and o.voided=0 and p.voided=0 and
									e.encounter_type=6 and o.concept_id=23722 and  o.value_coded = 856 and e.location_id=:location 
						) consultaClinicaPedidoCV2 on consultaClinicaPedidoCV2.patient_id=HVL_FR31.patient_id and 
							consultaClinicaPedidoCV2.dataConsultaClinicaPedidoCV2 between HVL_FR31.dataConsultaApss2CV2 and :endDate
						group by HVL_FR31.patient_id
					)HVL_FR32
					left join
					(
						select p.patient_id, o.value_datetime dataColheitaRegistadaCV2
						from 	patient p
								inner join encounter e on p.patient_id=e.patient_id	
								inner join obs o on o.encounter_id=e.encounter_id
							where e.voided=0 and o.voided=0 and p.voided=0 and e.encounter_type in (13,51) 
								and o.concept_id=23821 and e.location_id=:location
					)	colheitaRegistadaCV2 on colheitaRegistadaCV2.patient_id=HVL_FR32.patient_id 
						and colheitaRegistadaCV2.dataColheitaRegistadaCV2 between date_add(HVL_FR32.dataConsultaApss2CV2, interval  1 day) and :endDate
						group by HVL_FR32.patient_id
				)HVL_FR33
				left join
				(
					Select 	p.patient_id,o.obs_datetime dataResultadoTerceiraCV
					from 	patient p
							inner join encounter e on p.patient_id=e.patient_id
							inner join obs o on e.encounter_id=o.encounter_id
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
							o.concept_id in (856,1305)
				) resultadoTerceiraCVData on resultadoTerceiraCVData.patient_id = HVL_FR33.patient_id and resultadoTerceiraCVData.dataResultadoTerceiraCV between HVL_FR33.dataConsultaApss2CV3 and :endDate
				group by HVL_FR33.patient_id
			)HVL_FR34
			left join
			(
				select p.patient_id,o.obs_datetime dataResultadoTerceiraCV, o.concept_id,o.value_numeric

				from	patient p
						inner join encounter e on p.patient_id=e.patient_id
						inner join obs o on e.encounter_id=o.encounter_id
				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id = :location and
						o.concept_id in (856,1305)
			) resultadoTerceiraCVValor on resultadoTerceiraCVValor.patient_id = HVL_FR34.patient_id and resultadoTerceiraCVValor.dataResultadoTerceiraCV = HVL_FR34.dataResultadoTerceiraCV
		)HVL_FR35
		left join
		(
			select p.patient_id, e.encounter_datetime dataConsultaClinicaParaMudancaLinhaCV2
			from 	patient p
					inner join encounter e on p.patient_id=e.patient_id
			where 	p.voided=0 and e.voided=0 and e.encounter_type = 6 and
					e.encounter_datetime <=:endDate and e.location_id=:location 
		) consultaClinicaParaMudancaLinhaCV2 on  consultaClinicaParaMudancaLinhaCV2.patient_id = HVL_FR35.patient_id and 
			consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 between HVL_FR35.dataResultadoTerceiraCV and :endDate
			group by HVL_FR35.patient_id
	)	HVL_FR36
	left join 
	(
		select 	p.patient_id,e.encounter_datetime dataInicioNovaLinhaCV2,o.value_coded
		from 	patient p
				inner join encounter e on p.patient_id= e.patient_id
				inner join obs o on e.encounter_id=o.encounter_id
		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type=6 and
				o.concept_id= 21151 and o.value_coded in (21148,21149) and 
				e.encounter_datetime <=:endDate and e.location_id=:location
	) inicioNovaLinhaCV2 on inicioNovaLinhaCV2.patient_id = HVL_FR36.patient_id and inicioNovaLinhaCV2.dataInicioNovaLinhaCV2 between HVL_FR36.dataResultadoTerceiraCV and :endDate 
		group by HVL_FR36.patient_id
)	HVL_FR40
 inner join person p on p.person_id=HVL_FR40.patient_id          
  left join   ( 
  select pad1.*  from person_address pad1  
  inner join   (  
  select person_id,max(person_address_id) id   from person_address  
  where voided=0  
  group by person_id  
  ) pad2  
  where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
  ) pad3 on pad3.person_id=HVL_FR40.patient_id 
  left join( 
  select pn1.*  from person_name pn1  
  inner join (  
  select person_id,min(person_name_id) id   from person_name  
  where voided=0  
  group by person_id  
  ) pn2  
  where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
  ) pn on pn.person_id=HVL_FR40.patient_id 
  left join  ( 
  select pid1.*  from patient_identifier pid1  
  inner join  (  
  select patient_id,min(patient_identifier_id) id  from patient_identifier  
  where voided=0  
  group by patient_id  
  ) pid2 
  where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
  ) pid on pid.patient_id=HVL_FR40.patient_id
  left join person_attribute pat on pat.person_id=HVL_FR40.patient_id and pat.person_attribute_type_id=9 and pat.value is not null and pat.value<>'' and pat.voided=0
  left join person_attribute pat2 on pat2.person_id=HVL_FR40.patient_id and pat2.person_attribute_type_id=30 and pat2.value is not null and pat2.value<>'' and pat2.voided=0
left join
( 
    SELECT tb.patient_id from

    (
      select p.patient_id,max(o.value_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6,9) and o.concept_id=1113 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union  


      SELECT p.patient_id,pg.date_enrolled data_consulta FROM patient p
      INNER JOIN patient_program pg ON p.patient_id=pg.patient_id 
      INNER JOIN patient_state ps ON pg.patient_program_id=ps.patient_program_id  
      WHERE pg.program_id=5 AND (ps.start_date IS NOT NULL AND ps.end_date IS NULL and ps.voided = 0) 
      and pg.date_enrolled between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE() 
      GROUP BY p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(53) and o.concept_id=1406 and o.value_coded=42 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id=1268 and o.value_coded=1256 and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id

      union

      select p.patient_id,max(e.encounter_datetime) data_consulta  from patient p 
      inner join encounter e on e.patient_id=p.patient_id
      inner join obs o on o.encounter_id=e.encounter_id
      where e.encounter_type in(6) and o.concept_id in (23761) and o.value_coded in(1065) and p.voided=0 and e.voided=0 and o.voided=0 
      and e.encounter_datetime between date_sub(CURDATE(), INTERVAL 7 MONTH) and CURDATE()
      group by p.patient_id
      )tb
) tbFinal on tbFinal.patient_id=HVL_FR40.patient_id

 left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,e.encounter_datetime data_gravida from patient p     
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                             
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1982
      and value_coded=1065 and e.encounter_type in (5,6)                                                                                                                               
      and e.encounter_datetime  between date_add(curdate(), interval -24 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender = 'F'                                                                                                                             
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1279 
      and  e.encounter_type in (5,6)                                                                                                                                                   
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location
          and pe.gender = 'F'                                                                                                                                
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=1600 
      and e.encounter_type in (5,6)                                                                                                                                                    
          and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location  
          and pe.gender = 'F'                                                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,e.encounter_datetime data_gravida from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                 
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 and value_coded=6331                                                                                                                                                             
          and  e.encounter_type in (5,6) and e.encounter_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location     
          and pe.gender = 'F'                                                                                            
      union                                                                                                                                                                                                                                               
      select pp.patient_id,pp.date_enrolled data_gravida from patient_program pp
      inner join person pe on pe.person_id = pp.patient_id                                                                                                                                                                            
      where pp.program_id=8 and pp.voided=0 
      and  pp.date_enrolled between date_add(curdate(), interval -24 MONTH) 
      and curdate() and pp.location_id=:location  
      and pe.gender = 'F'                                                                                              
      union                                                                                                                                                                                                                                               
      select p.patient_id,obsART.value_datetime data_gravida from patient p  
       inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                               
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=1982 
      and o.value_coded=1065 and  e.encounter_type=53                                                                                                                                
          and obsART.value_datetime between date_add(curdate(), interval -24 MONTH) 
          and curdate() and e.location_id=:location 
          and  obsART.concept_id=1190 and obsART.voided=0
          and pe.gender = 'F'                                                                               
      union                                                                                                                                                                                                                                               
      select p.patient_id,data_colheita.value_datetime data_gravida from patient p
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                          
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                                 
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                                   
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                         
      where p.voided=0 and e.voided=0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=1982 and o.value_coded = 1065 and  e.encounter_type=51                                                                                                     
        and data_colheita.concept_id =23821 and data_colheita.value_datetime between date_add(curdate(), interval -24 MONTH) 
        and curdate() and e.location_id=:location   
        and pe.gender = 'F'                                                                                       
  )                                                                                                                                                                                                                                                       
 gravida_real on gravida_real.patient_id=HVL_FR40.patient_id and gravida_real.data_gravida between date_add(curdate(), interval -9 MONTH) and curdate()                                                                                
left join                                                                                                                                                                                                                                                   
  (   select p.patient_id,o.value_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                       
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=5599 
      and  e.encounter_type in (5,6) and o.value_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location
      and pe.gender = 'F'                                      
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6332 
      and value_coded=1065 and  e.encounter_type=6 
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) 
      and curdate() and e.location_id=:location  
      and pe.gender ='F'                  
      union                                                                                                                                                                                                                                               
      select p.patient_id, obsART.value_datetime data_parto from patient p  
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs obsART on e.encounter_id=obsART.encounter_id                                                                                                                                                                                     
      where p.voided=0 and e.voided=0 and o.voided=0 and o.concept_id=6332 
      and o.value_coded=1065 and  e.encounter_type=53 
      and e.location_id=:location and obsART.value_datetime between date_add(curdate(), interval -36 MONTH) and curdate() 
      and  obsART.concept_id=1190 and obsART.voided=0  
      and pe.gender ='F'    
      union                                                                                                                                                                                                                                               
      select p.patient_id, e.encounter_datetime data_parto from patient p 
      inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                  
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
      where p.voided=0 and e.voided=0 and o.voided=0 and concept_id=6334 
      and value_coded=6332 and  e.encounter_type in (5,6) 
      and pe.gender = 'F'
      and e.encounter_datetime between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location             
      union                                                                                                                                                                                                                                               
      select pg.patient_id,ps.start_date data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                                         
          inner join patient_program pg on p.patient_id=pg.patient_id                                                                                                                                                                                     
          inner join patient_state ps on pg.patient_program_id=ps.patient_program_id                                                                                                                                                                      
      where pg.voided=0 and ps.voided=0 and p.voided=0 and  pg.program_id=8 and ps.state=27 
      and pe.gender = 'F'  
      and ps.end_date is null and  ps.start_date between date_add(curdate(), interval -36 MONTH) and curdate() and location_id=:location                              
     union                                                                                                                                                                                                                                                
   select p.patient_id,data_colheita.value_datetime data_parto from patient p    
          inner join person pe on pe.person_id = p.patient_id                                                                                                                                                                      
          inner join encounter e on p.patient_id=e.patient_id                                                                                                                                                                                             
          inner join obs o on e.encounter_id=o.encounter_id                                                                                                                                                                                               
          inner join obs data_colheita on data_colheita.encounter_id = e.encounter_id                                                                                                                                                                     
      where p.voided=0 and e.voided =0 and o.voided=0 and data_colheita.voided = 0 
      and o.concept_id=6332 and o.value_coded = 1065 and  e.encounter_type=51    
      and pe.gender = 'F'                                                                                             
      and data_colheita.concept_id =23821 and data_colheita.value_datetime 
      between date_add(curdate(), interval -36 MONTH) and curdate() and e.location_id=:location  
      )                                                                                                                                                                                                                                                       
lactante_real on lactante_real.patient_id = HVL_FR40.patient_id                                                                                                                          
   and lactante_real.data_parto between date_add(curdate(), interval -18 MONTH) 
   and curdate()  and (lactante_real.data_parto is not null or gravida_real.data_gravida is not null) 
   group by HVL_FR40.patient_id
				
	)HVL_FR41
			where HVL_FR41.alert is not null
 ) result group by patient_id order by patient_id