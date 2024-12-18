select HVL_FR36.patient_id
	from
	 ( select
		
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
																															and obsSampleCollectDate.concept_id=23821 and obsVL.concept_id=856 and obsVL.value_numeric> 1000  
																															and e.location_id=:location and obsSampleCollectDate.value_datetime <= :endDate
																												) primeiraColheitaCV on primeiraColheitaCV.patient_id = HVL_FR3.patient_id and primeiraColheitaCV.data_resultado  = HVL_FR3.data_carga 
																												
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
																				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id=:location and
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
																		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id=:location and
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
						where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id=:location and
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
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id=:location and
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
				where HVL_FR35.dataResultadoTerceiraCV is not null and consultaClinicaParaMudancaLinhaCV2.dataConsultaClinicaParaMudancaLinhaCV2 is not null and   HVL_FR35.dataApssAvaliarAdesao is not null
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
		where inicioNovaLinhaCV2.value_coded is not null
			group by HVL_FR36.patient_id