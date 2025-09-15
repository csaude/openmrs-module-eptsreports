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
			DATE_FORMAT(HVL_FR41.data_inicio,'%d/%m/%Y') data_inicio,
			DATE_FORMAT(HVL_FR41.data_colheita,'%d/%m/%Y') data_colheita,
			DATE_FORMAT(HVL_FR41.data_carga,'%d/%m/%Y') data_carga,
			HVL_FR41.linha,
			HVL_FR41.valorCV,
			if(HVL_FR41.dataConsultaClinica0CV1 ='N/A','N/A',DATE_FORMAT(HVL_FR41.dataConsultaClinica0CV1,'%d/%m/%Y')) dataConsultaClinica0CV1,
			DATE_FORMAT(HVL_FR41.dataPrevistaConsultaClinica0CV1,'%d/%m/%Y') dataPrevistaConsultaClinica0CV1,
			IF(COALESCE(HVL_FR41.dataConsultaApss0CV1, '') != '', DATE_FORMAT(HVL_FR41.dataConsultaApss0CV1, '%d/%m/%Y'), 'N/A') AS dataConsultaApss0CV1,
			DATE_FORMAT(HVL_FR41.dataPrevistaConsultaApss0CV1,'%d/%m/%Y') dataPrevistaConsultaApss0CV1, 
			DATE_FORMAT(HVL_FR41.dataConsultaApss1CV1,'%d/%m/%Y') dataConsultaApss1CV1,
			DATE_FORMAT(HVL_FR41.previstaConsultaApss1CV1,'%d/%m/%Y') previstaConsultaApss1CV1,
			DATE_FORMAT(HVL_FR41.dataConsultaApss2CV1,'%d/%m/%Y') dataConsultaApss2CV1,
			DATE_FORMAT(HVL_FR41.previstaConsultaApss2CV1,'%d/%m/%Y') previstaConsultaApss2CV1, 		
			DATE_FORMAT(HVL_FR41.dataConsultaApss3CV1,'%d/%m/%Y') dataConsultaApss3CV1, 	
			HVL_FR41.previstaConsultaApss3CV1, 
			HVL_FR41.adesao,
			DATE_FORMAT(HVL_FR41.dataConsultaClinicaPedidoCV1,'%d/%m/%Y') dataConsultaClinicaPedidoCV1,
			DATE_FORMAT(HVL_FR41.dataPrevistaConsultaClinicaPedidoCV1,'%d/%m/%Y') dataPrevistaConsultaClinicaPedidoCV1, 
			DATE_FORMAT(HVL_FR41.dataColheitaRegistadaCV1,'%d/%m/%Y') dataColheitaRegistadaCV1,
			DATE_FORMAT(HVL_FR41.dataPrevistaColheitaAmostraCV1,'%d/%m/%Y') dataPrevistaColheitaAmostraCV1, 
			DATE_FORMAT(HVL_FR41.dataResultadoSegundaCV1,'%d/%m/%Y') dataResultadoSegundaCV1,
			DATE_FORMAT(HVL_FR41.dataPrevistaResultadoCV1,'%d/%m/%Y') dataPrevistaResultadoCV1, 
			HVL_FR41.resultadoSegundaCV1,
			HVL_FR41.mudancaLinhaCV1,
			DATE_FORMAT(HVL_FR41.dataConsultaClinicaParaMudancaLinhaCV1,'%d/%m/%Y') dataConsultaClinicaParaMudancaLinhaCV1 ,
			DATE_FORMAT(HVL_FR41.dataPrevistaConsultaMudancaLinhaCV1,'%d/%m/%Y') dataPrevistaConsultaMudancaLinhaCV1, 
			DATE_FORMAT(HVL_FR41.dataInicioNovaLinhaCV1,'%d/%m/%Y') dataInicioNovaLinhaCV1,
		   case 
	       when HVL_FR41.type_patient = 1 then 'OBITO'
	       when HVL_FR41.type_patient  = 2 then 'TRANSFERIDO PARA'
	       when HVL_FR41.type_patient  = 3 then 'ABANDONO'
	       when  HVL_FR41.type_patient  = 4 then 'SUSPENSO'
	       when ISNULL(HVL_FR41.type_patient) then null
	       end as type_patient,
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataConsultaApss0CV2,'%d/%m/%Y'), 'N/A') dataConsultaApss0CV2 ,

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataPrevistaApss0CV2,'%d/%m/%Y'), 'N/A') dataPrevistaApss0CV2 ,	

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataConsultaApss1CV2,'%d/%m/%Y'), 'N/A') dataConsultaApss1CV2 ,				
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataPrevistaApss1CV2,'%d/%m/%Y'), 'N/A') dataPrevistaApss1CV2 ,	

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataConsultaApss2CV2,'%d/%m/%Y'), 'N/A') dataConsultaApss2CV2 ,		

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataPrevistaApss2CV2,'%d/%m/%Y'), 'N/A') dataPrevistaApss2CV2 ,	


			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataConsultaApss3CV2,'%d/%m/%Y'), 'N/A') dataConsultaApss3CV2 ,	

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataPrevistaApss3CV2,'%d/%m/%Y'), 'N/A') dataPrevistaApss3CV2 ,	
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataConsultaClinicaPedidoCV2,'%d/%m/%Y'), 'N/A') dataConsultaClinicaPedidoCV2 ,	
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataPrevistaConsultaClinicaPedidoCV2,'%d/%m/%Y'), 'N/A') dataPrevistaConsultaClinicaPedidoCV2 ,

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataColheitaRegistadaCV2,'%d/%m/%Y'), 'N/A') dataColheitaRegistadaCV2 ,	

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataPrevistaColheitaAmostraCV2,'%d/%m/%Y'), 'N/A') dataPrevistaColheitaAmostraCV2 ,	

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataResultadoTerceiraCV,'%d/%m/%Y'), 'N/A') dataResultadoTerceiraCV ,		

			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataPrevistaResultadoTerceiraCV,'%d/%m/%Y'), 'N/A') dataPrevistaResultadoTerceiraCV ,		
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', HVL_FR41.resultadoTerceiraCV, 'N/A') resultadoTerceiraCV ,
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', HVL_FR41.mudancaLinhaCV2, 'N/A') mudancaLinhaCV2 ,
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataConsultaClinicaParaMudancaLinhaCV2,'%d/%m/%Y'), 'N/A') dataConsultaClinicaParaMudancaLinhaCV2 ,
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataPrevistaConsultaMudancaLinhaCV2,'%d/%m/%Y'), 'N/A') dataPrevistaConsultaMudancaLinhaCV2 ,
			
			if(HVL_FR41.dataInicioNovaLinhaCV1 = 'N/A', DATE_FORMAT(HVL_FR41.dataInicioNovaLinhaCV2,'%d/%m/%Y'), 'N/A') dataInicioNovaLinhaCV2 

from

(
	select 		
			HVL_FR40.patient_id,
			pid.identifier as NID,
			 concat(ifnull(pn.given_name,''),' ',ifnull(pn.middle_name,''),' ',ifnull(pn.family_name,'')) as NAME,
			 p.gender as GENDER, 
			 (TIMESTAMPDIFF(year,birthdate,:endDate)) AGE, 
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
			HVL_FR40.dataPrevistaConsultaClinica0CV1,	
			HVL_FR40.dataConsultaApss0CV1,
			HVL_FR40.dataPrevistaConsultaApss0CV1, 
			HVL_FR40.dataConsultaApss1CV1,
			HVL_FR40.previstaConsultaApss1CV1, 
			HVL_FR40.dataConsultaApss2CV1,
			DATE(HVL_FR40.previstaConsultaApss2CV1) previstaConsultaApss2CV1, 		
			HVL_FR40.dataConsultaApss3CV1, 	
			HVL_FR40.previstaConsultaApss3CV1, 
			HVL_FR40.adesao,
			HVL_FR40.dataConsultaClinicaPedidoCV1,
			HVL_FR40.dataPrevistaConsultaClinicaPedidoCV1, 
			HVL_FR40.dataColheitaRegistadaCV1,
			HVL_FR40.dataPrevistaColheitaAmostraCV1, 
			HVL_FR40.dataResultadoSegundaCV1,
			HVL_FR40.dataPrevistaResultadoCV1, 
			HVL_FR40.resultadoSegundaCV1,
			HVL_FR40.mudancaLinhaCV1,
			estadoPermanencia.type_patient,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataConsultaClinicaParaMudancaLinhaCV1) dataConsultaClinicaParaMudancaLinhaCV1 ,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataPrevistaConsultaMudancaLinhaCV1) dataPrevistaConsultaMudancaLinhaCV1 , 
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataInicioNovaLinhaCV1) dataInicioNovaLinhaCV1 ,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataConsultaApss0CV2) dataConsultaApss0CV2 ,
			if(HVL_FR40.resultadoSegundaCV1 < 1000 , 'N/A', HVL_FR40.dataPrevistaApss0CV2) dataPrevistaApss0CV2 , 
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataConsultaApss1CV2) dataConsultaApss1CV2 ,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataPrevistaApss1CV2) dataPrevistaApss1CV2 , 
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataConsultaApss2CV2) dataConsultaApss2CV2 ,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataPrevistaApss2CV2) dataPrevistaApss2CV2 , 
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataConsultaApss3CV2) dataConsultaApss3CV2,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataPrevistaApss3CV2) dataPrevistaApss3CV2, 
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataConsultaClinicaPedidoCV2) dataConsultaClinicaPedidoCV2,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataPrevistaConsultaClinicaPedidoCV2) dataPrevistaConsultaClinicaPedidoCV2, 
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataColheitaRegistadaCV2) dataColheitaRegistadaCV2 ,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataPrevistaColheitaAmostraCV2) dataPrevistaColheitaAmostraCV2 ,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataResultadoTerceiraCV) dataResultadoTerceiraCV,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataPrevistaResultadoTerceiraCV) dataPrevistaResultadoTerceiraCV, 
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.resultadoTerceiraCV) resultadoTerceiraCV,
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.mudancaLinhaCV2) mudancaLinhaCV2 ,		
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataConsultaClinicaParaMudancaLinhaCV2) dataConsultaClinicaParaMudancaLinhaCV2,				
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataPrevistaConsultaMudancaLinhaCV2) dataPrevistaConsultaMudancaLinhaCV2, 
			if(HVL_FR40.resultadoSegundaCV1 < 1000, 'N/A', HVL_FR40.dataInicioNovaLinhaCV2) dataInicioNovaLinhaCV2
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
																				where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id =:location and
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
																		where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id =:location and
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
						where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id =:location and
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
					where 	p.voided=0 and e.voided=0 and o.voided=0 and e.encounter_type in (13,51)  and e.location_id =:location and
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
	left join

	(
select final.patient_id, final.type_patient

 from (
select iit.patient_id, 3 type_patient from (
select patient_id         
from (
			select inicio_fila_seg_prox.*,         
					greatest(coalesce(data_fila,data_seguimento),coalesce(data_seguimento,data_fila))  data_usar_c, 
					greatest(coalesce(data_proximo_lev,data_recepcao_levantou30),coalesce(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) maximo_proximo_fila_recepcao,    
					greatest(coalesce(data_proximo_lev,data_proximo_seguimento,data_recepcao_levantou30),coalesce(data_proximo_seguimento,data_proximo_lev,data_recepcao_levantou30),coalesce(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) data_usar 
             from (
						select inicio_fila_seg.*,
							max(obs_fila.value_datetime) data_proximo_lev,  
							max(obs_seguimento.value_datetime) data_proximo_seguimento,       
							date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30          
						from (
									select inicio.*,        
										saida.data_estado,  
										entrada_obitos.data_estado data_entrada_obito,
										saidas_obitos.data_estado data_saida_obito,
										entradas_por_transferencia.data_estado  data_entrada_transferencia,
										saidas_por_transferencia.data_estado data_saida_transferencia,
										max_fila.data_fila, 
										max_consulta.data_seguimento,     
										max_recepcao.data_recepcao_levantou   
									from (   
												select patient_id,min(data_inicio) data_inicio      
												from (           
															select  p.patient_id,min(e.encounter_datetime) data_inicio    
															from patient p         
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id   
																	inner join obs o on o.encounter_id=e.encounter_id     
															where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0           
																	and e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256              
																	and e.encounter_datetime<=:endDate and e.location_id=:location
																	group by p.patient_id     
															union   
															select  p.patient_id,min(value_datetime) data_inicio          
															from patient p
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs o on e.encounter_id=o.encounter_id     
															where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53)           
																	and o.concept_id=1190 and o.value_datetime is not null 
																	and o.value_datetime<=:endDate and e.location_id=:location
																	group by p.patient_id     
															union   
															select  pg.patient_id,min(date_enrolled) data_inicio          
															from patient p         
																	inner join person pe on pe.person_id = p.patient_id       
																	inner join patient_program pg on p.patient_id=pg.patient_id   
															where   pg.voided=0 and p.voided=0 and pe.voided = 0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
																	group by pg.patient_id    
															union   
															select e.patient_id, min(e.encounter_datetime) as data_inicio
															from patient p     
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id   
															where p.voided=0 and pe.voided = 0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location  
																	group by  p.patient_id
															union 
															select  p.patient_id,min(value_datetime) data_inicio    
															from patient p   
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs o on e.encounter_id=o.encounter_id   
															where   p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52   
																	and o.concept_id=23866 and o.value_datetime is not null            
																	and o.value_datetime<=:endDate and e.location_id=:location            
																	group by p.patient_id   
														) inicio_real           
															group by patient_id         
											) inicio 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date<= :endDate and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
																	and o.obs_datetime<= :endDate  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime<= :endDate and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
													) allsaida        
															group by patient_id 
											) entrada_obitos on inicio.patient_id = entrada_obitos.patient_id 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date< :startDate and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
																	and o.obs_datetime< :startDate  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date< :startDate
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime< :startDate and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
													) allsaida        
															group by patient_id 
											) saidas_obitos on inicio.patient_id = saidas_obitos.patient_id 
											left join
											(
												select saidas_por_transferencia.patient_id, data_estado 
													from (
																select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																from (
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (          
																						select pg.patient_id, max(ps.start_date) data_estado
																						from patient p   
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join patient_program pg on p.patient_id = pg.patient_id         
																								inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																						where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																								and ps.start_date<= :endDate  and pg.location_id =:location group by pg.patient_id       
																					 ) max_estado            
																					inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																			where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																			union 
																			select  p.patient_id,max(o.obs_datetime) data_estado   
																			from patient p       
																					inner join person pe on pe.person_id = p.patient_id 
																					inner join encounter e on p.patient_id=e.patient_id 
																					inner join obs  o on e.encounter_id=o.encounter_id  
																			where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																					and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																					and o.obs_datetime<=:endDate and e.location_id=:location
																					group by p.patient_id   
																			union 
																			select ultimabusca.patient_id, ultimabusca.data_estado      
																			from (
																						select p.patient_id,max(e.encounter_datetime) data_estado             
																						from patient p  
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join encounter e on p.patient_id=e.patient_id 
																								inner join obs o on o.encounter_id=e.encounter_id   
																						where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= :endDate  
																								and e.encounter_type = 21 and  e.location_id=:location           
																								group by p.patient_id         
																					) ultimabusca       
																					inner join encounter e on e.patient_id = ultimabusca.patient_id       
																					inner join obs o on o.encounter_id = e.encounter_id 
																			where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																) saidas_por_transferencia 
																	group by patient_id 
													) saidas_por_transferencia
													left join
													(  
															select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
															from (
																		select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																		from (
																					select p.patient_id, max(encounter_datetime) data_fila     
																					from patient p     
																							inner join person pe on pe.person_id = p.patient_id   
																							inner join encounter e on e.patient_id=p.patient_id   
																					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																							and e.location_id=:location and e.encounter_datetime <= :endDate          
																							group by p.patient_id 
																		) ultimo_fila  
																		left join            
																		obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																		and obs_fila.voided=0   
																		and obs_fila.obs_datetime=ultimo_fila.data_fila      
																		and obs_fila.concept_id=5096           
																		and obs_fila.location_id=:location  
																		union
																		select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																		from patient p     
																				inner join person pe on pe.person_id = p.patient_id   
																				inner join encounter e on p.patient_id=e.patient_id   
																				inner join obs o on e.encounter_id=o.encounter_id 
																		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																				and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate
																				group by p.patient_id
															) ultimo_levantamento group by patient_id
													) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
													where ultimo_levantamento.data_ultimo_levantamento <= :endDate
											) entradas_por_transferencia on inicio.patient_id = entradas_por_transferencia.patient_id 
											left join
											(
												select saidas_por_transferencia.patient_id, data_estado 
													from (
																select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																from (
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (          
																						select pg.patient_id, max(ps.start_date) data_estado
																						from patient p   
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join patient_program pg on p.patient_id = pg.patient_id         
																								inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																						where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																								and ps.start_date< :startDate  and pg.location_id =:location group by pg.patient_id       
																					 ) max_estado            
																					inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																			where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																			union 
																			select  p.patient_id,max(o.obs_datetime) data_estado   
																			from patient p       
																					inner join person pe on pe.person_id = p.patient_id 
																					inner join encounter e on p.patient_id=e.patient_id 
																					inner join obs  o on e.encounter_id=o.encounter_id  
																			where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																					and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																					and o.obs_datetime<:startDate and e.location_id=:location
																					group by p.patient_id   
																			union 
																			select ultimabusca.patient_id, ultimabusca.data_estado      
																			from (
																						select p.patient_id,max(e.encounter_datetime) data_estado             
																						from patient p  
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join encounter e on p.patient_id=e.patient_id 
																								inner join obs o on o.encounter_id=e.encounter_id   
																						where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime< :startDate  
																								and e.encounter_type = 21 and  e.location_id=:location           
																								group by p.patient_id         
																					) ultimabusca       
																					inner join encounter e on e.patient_id = ultimabusca.patient_id       
																					inner join obs o on o.encounter_id = e.encounter_id 
																			where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																) saidas_por_transferencia 
																	group by patient_id 
													) saidas_por_transferencia
													left join
													(  
															select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
															from (
																		select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																		from (
																					select p.patient_id, max(encounter_datetime) data_fila     
																					from patient p     
																							inner join person pe on pe.person_id = p.patient_id   
																							inner join encounter e on e.patient_id=p.patient_id   
																					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																							and e.location_id=:location and e.encounter_datetime < :startDate          
																							group by p.patient_id 
																		) ultimo_fila  
																		left join            
																		obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																		and obs_fila.voided=0   
																		and obs_fila.obs_datetime=ultimo_fila.data_fila      
																		and obs_fila.concept_id=5096           
																		and obs_fila.location_id=:location  
																		union
																		select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																		from patient p     
																				inner join person pe on pe.person_id = p.patient_id   
																				inner join encounter e on p.patient_id=e.patient_id   
																				inner join obs o on e.encounter_id=o.encounter_id 
																		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																				and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime < :startDate
																				group by p.patient_id
															) ultimo_levantamento group by patient_id
													) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
													where ultimo_levantamento.data_ultimo_levantamento < :startDate
											) saidas_por_transferencia on inicio.patient_id = saidas_por_transferencia.patient_id 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date<= date_add(:startDate, interval -1 day) and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (8,10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366,1709)       
																	and o.obs_datetime<= date_add(:startDate, interval -1 day)  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date<=date_add(:startDate, interval -1 day)
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime<= date_add(:startDate, interval -1 day)  and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
															union
															select saidas_por_transferencia.patient_id, data_estado 
															from (
																		select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																		from (
																					select distinct max_estado.patient_id, max_estado.data_estado 
																					from (          
																								select pg.patient_id, max(ps.start_date) data_estado
																								from patient p   
																										inner join person pe on pe.person_id = p.patient_id 
																										inner join patient_program pg on p.patient_id = pg.patient_id         
																										inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																								where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																										and ps.start_date<= date_add(:startDate, interval -1 day)  and pg.location_id =:location group by pg.patient_id       
																							 ) max_estado            
																							inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																							inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																					where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																					union 
																					select  p.patient_id,max(o.obs_datetime) data_estado   
																					from patient p       
																							inner join person pe on pe.person_id = p.patient_id 
																							inner join encounter e on p.patient_id=e.patient_id 
																							inner join obs  o on e.encounter_id=o.encounter_id  
																					where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																							and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																							and o.obs_datetime<= date_add(:startDate, interval -1 day) and e.location_id=:location
																							group by p.patient_id   
																					union 
																					select ultimabusca.patient_id, ultimabusca.data_estado      
																					from (
																								select p.patient_id,max(e.encounter_datetime) data_estado             
																								from patient p  
																										inner join person pe on pe.person_id = p.patient_id 
																										inner join encounter e on p.patient_id=e.patient_id 
																										inner join obs o on o.encounter_id=e.encounter_id   
																								where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= date_add(:startDate, interval -1 day)   
																										and e.encounter_type = 21 and  e.location_id=:location           
																										group by p.patient_id         
																							) ultimabusca       
																							inner join encounter e on e.patient_id = ultimabusca.patient_id       
																							inner join obs o on o.encounter_id = e.encounter_id 
																					where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																		) saidas_por_transferencia 
																			group by patient_id 
															) saidas_por_transferencia
															left join
															(  
																	select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
																	from (
																				select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																				from (
																							select p.patient_id, max(encounter_datetime) data_fila     
																							from patient p     
																									inner join person pe on pe.person_id = p.patient_id   
																									inner join encounter e on e.patient_id=p.patient_id   
																							where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																									and e.location_id=:location and e.encounter_datetime <= date_add(:endDate, interval -3 month)          
																									group by p.patient_id 
																				) ultimo_fila  
																				left join            
																				obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																				and obs_fila.voided=0   
																				and obs_fila.obs_datetime=ultimo_fila.data_fila      
																				and obs_fila.concept_id=5096           
																				and obs_fila.location_id=:location  
																				union
																				select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																				from patient p     
																						inner join person pe on pe.person_id = p.patient_id   
																						inner join encounter e on p.patient_id=e.patient_id   
																						inner join obs o on e.encounter_id=o.encounter_id 
																				where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																						and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= date_add(:endDate, interval -3 month) 
																						group by p.patient_id
																	) ultimo_levantamento group by patient_id
															) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
															where ultimo_levantamento.data_ultimo_levantamento <= date_add(:endDate, interval -3 month)
																		  
													) allsaida        
															group by patient_id 
											) saida on inicio.patient_id = saida.patient_id            
											left join             
											( 		select p.patient_id,max(encounter_datetime) data_fila      
													from patient p     
														   inner join person pe on pe.person_id = p.patient_id   
														   inner join encounter e on e.patient_id=p.patient_id   
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
															and e.location_id=:location and e.encounter_datetime<=:endDate          
															group by p.patient_id 
											) max_fila on inicio.patient_id=max_fila.patient_id       
											left join            
											(		select  p.patient_id,max(encounter_datetime) data_seguimento            
													from patient p     
															inner join person pe on pe.person_id = p.patient_id   
															inner join encounter e on e.patient_id=p.patient_id   
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)          
															and e.location_id=:location and e.encounter_datetime<=:endDate          
															group by p.patient_id 
											) max_consulta on inicio.patient_id=max_consulta.patient_id   
											left join            
											(		select  p.patient_id,max(value_datetime) data_recepcao_levantou             
													from    patient p     
															inner join person pe on pe.person_id = p.patient_id   
															inner join encounter e on p.patient_id=e.patient_id   
															inner join obs o on e.encounter_id=o.encounter_id 
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
															and o.concept_id=23866 and o.value_datetime is not null              
															and  o.value_datetime<=:endDate and e.location_id=:location
															group by p.patient_id 
											) max_recepcao on inicio.patient_id=max_recepcao.patient_id  
												group by inicio.patient_id             
						) inicio_fila_seg     
						left join            
						obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id
								and obs_fila.voided=0   
								and obs_fila.obs_datetime=inicio_fila_seg.data_fila      
								and obs_fila.concept_id=5096           
								and obs_fila.location_id=:location     
						left join            
						obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id   
								and obs_seguimento.voided=0             
								and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento             
								and obs_seguimento.concept_id=1410      
								and obs_seguimento.location_id=:location
								group by inicio_fila_seg.patient_id     
			) inicio_fila_seg_prox
					group by patient_id   
) coorte12meses_final 
where ((data_estado is null or (data_estado is not null and  data_usar_c > data_estado)) and date_add(data_usar, interval 28 day) >=date_add(:startDate, interval -1 day)  and date_add(data_usar, interval 28 day) < :endDate)
or ( (data_saida_transferencia is null or (data_saida_transferencia is not null and  data_usar_c > data_saida_transferencia))  and data_entrada_obito is null and data_entrada_transferencia >= data_usar_c and data_entrada_transferencia <= :endDate and date_add(maximo_proximo_fila_recepcao, interval 1 day) >=:startDate) 
or ( (data_saida_obito is null or (data_saida_obito is not null and  data_usar_c > data_saida_obito))  and data_entrada_obito >= data_usar_c and data_entrada_obito between :startDate and :endDate)
)iit

union

select dead.patient_id, 1 type_patient from (

select patient_id         
from (
			select inicio_fila_seg_prox.*,         
					greatest(coalesce(data_fila,data_seguimento),coalesce(data_seguimento,data_fila))  data_usar_c, 
					greatest(coalesce(data_proximo_lev,data_recepcao_levantou30),coalesce(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) maximo_proximo_fila_recepcao,    
					greatest(coalesce(data_proximo_lev,data_proximo_seguimento,data_recepcao_levantou30),coalesce(data_proximo_seguimento,data_proximo_lev,data_recepcao_levantou30),coalesce(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) data_usar 
             from (
						select inicio_fila_seg.*,
							max(obs_fila.value_datetime) data_proximo_lev,  
							max(obs_seguimento.value_datetime) data_proximo_seguimento,       
							date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30          
						from (
									select inicio.*,        
										saida.data_estado,  
										entrada_obitos.data_estado data_entrada_obito,
										saidas_obitos.data_estado data_saida_obito,
										entradas_por_transferencia.data_estado  data_entrada_transferencia,
										saidas_por_transferencia.data_estado data_saida_transferencia,
										max_fila.data_fila, 
										max_consulta.data_seguimento,     
										max_recepcao.data_recepcao_levantou   
									from (   
												select patient_id,min(data_inicio) data_inicio      
												from (           
															select  p.patient_id,min(e.encounter_datetime) data_inicio    
															from patient p         
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id   
																	inner join obs o on o.encounter_id=e.encounter_id     
															where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0           
																	and e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256              
																	and e.encounter_datetime<=:endDate and e.location_id=:location
																	group by p.patient_id     
															union   
															select  p.patient_id,min(value_datetime) data_inicio          
															from patient p
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs o on e.encounter_id=o.encounter_id     
															where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53)           
																	and o.concept_id=1190 and o.value_datetime is not null 
																	and o.value_datetime<=:endDate and e.location_id=:location
																	group by p.patient_id     
															union   
															select  pg.patient_id,min(date_enrolled) data_inicio          
															from patient p         
																	inner join person pe on pe.person_id = p.patient_id       
																	inner join patient_program pg on p.patient_id=pg.patient_id   
															where   pg.voided=0 and p.voided=0 and pe.voided = 0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
																	group by pg.patient_id    
															union   
															select e.patient_id, min(e.encounter_datetime) as data_inicio
															from patient p     
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id   
															where p.voided=0 and pe.voided = 0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location  
																	group by  p.patient_id
															union 
															select  p.patient_id,min(value_datetime) data_inicio    
															from patient p   
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs o on e.encounter_id=o.encounter_id   
															where   p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52   
																	and o.concept_id=23866 and o.value_datetime is not null            
																	and o.value_datetime<=:endDate and e.location_id=:location            
																	group by p.patient_id   
														) inicio_real           
															group by patient_id         
											) inicio 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date<= :endDate and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
																	and o.obs_datetime<= :endDate  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime<= :endDate and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
													) allsaida        
															group by patient_id 
											) entrada_obitos on inicio.patient_id = entrada_obitos.patient_id 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date< :startDate and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
																	and o.obs_datetime< :startDate  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date< :startDate
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime< :startDate and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
													) allsaida        
															group by patient_id 
											) saidas_obitos on inicio.patient_id = saidas_obitos.patient_id 
											left join
											(
												select saidas_por_transferencia.patient_id, data_estado 
													from (
																select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																from (
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (          
																						select pg.patient_id, max(ps.start_date) data_estado
																						from patient p   
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join patient_program pg on p.patient_id = pg.patient_id         
																								inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																						where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																								and ps.start_date<= :endDate  and pg.location_id =:location group by pg.patient_id       
																					 ) max_estado            
																					inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																			where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																			union 
																			select  p.patient_id,max(o.obs_datetime) data_estado   
																			from patient p       
																					inner join person pe on pe.person_id = p.patient_id 
																					inner join encounter e on p.patient_id=e.patient_id 
																					inner join obs  o on e.encounter_id=o.encounter_id  
																			where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																					and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																					and o.obs_datetime<=:endDate and e.location_id=:location
																					group by p.patient_id   
																			union 
																			select ultimabusca.patient_id, ultimabusca.data_estado      
																			from (
																						select p.patient_id,max(e.encounter_datetime) data_estado             
																						from patient p  
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join encounter e on p.patient_id=e.patient_id 
																								inner join obs o on o.encounter_id=e.encounter_id   
																						where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= :endDate  
																								and e.encounter_type = 21 and  e.location_id=:location           
																								group by p.patient_id         
																					) ultimabusca       
																					inner join encounter e on e.patient_id = ultimabusca.patient_id       
																					inner join obs o on o.encounter_id = e.encounter_id 
																			where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																) saidas_por_transferencia 
																	group by patient_id 
													) saidas_por_transferencia
													left join
													(  
															select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
															from (
																		select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																		from (
																					select p.patient_id, max(encounter_datetime) data_fila     
																					from patient p     
																							inner join person pe on pe.person_id = p.patient_id   
																							inner join encounter e on e.patient_id=p.patient_id   
																					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																							and e.location_id=:location and e.encounter_datetime <= :endDate          
																							group by p.patient_id 
																		) ultimo_fila  
																		left join            
																		obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																		and obs_fila.voided=0   
																		and obs_fila.obs_datetime=ultimo_fila.data_fila      
																		and obs_fila.concept_id=5096           
																		and obs_fila.location_id=:location  
																		union
																		select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																		from patient p     
																				inner join person pe on pe.person_id = p.patient_id   
																				inner join encounter e on p.patient_id=e.patient_id   
																				inner join obs o on e.encounter_id=o.encounter_id 
																		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																				and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate
																				group by p.patient_id
															) ultimo_levantamento group by patient_id
													) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
													where ultimo_levantamento.data_ultimo_levantamento <= :endDate
											) entradas_por_transferencia on inicio.patient_id = entradas_por_transferencia.patient_id 
											left join
											(
												select saidas_por_transferencia.patient_id, data_estado 
													from (
																select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																from (
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (          
																						select pg.patient_id, max(ps.start_date) data_estado
																						from patient p   
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join patient_program pg on p.patient_id = pg.patient_id         
																								inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																						where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																								and ps.start_date< :startDate  and pg.location_id =:location group by pg.patient_id       
																					 ) max_estado            
																					inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																			where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																			union 
																			select  p.patient_id,max(o.obs_datetime) data_estado   
																			from patient p       
																					inner join person pe on pe.person_id = p.patient_id 
																					inner join encounter e on p.patient_id=e.patient_id 
																					inner join obs  o on e.encounter_id=o.encounter_id  
																			where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																					and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																					and o.obs_datetime<:startDate and e.location_id=:location
																					group by p.patient_id   
																			union 
																			select ultimabusca.patient_id, ultimabusca.data_estado      
																			from (
																						select p.patient_id,max(e.encounter_datetime) data_estado             
																						from patient p  
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join encounter e on p.patient_id=e.patient_id 
																								inner join obs o on o.encounter_id=e.encounter_id   
																						where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime< :startDate  
																								and e.encounter_type = 21 and  e.location_id=:location           
																								group by p.patient_id         
																					) ultimabusca       
																					inner join encounter e on e.patient_id = ultimabusca.patient_id       
																					inner join obs o on o.encounter_id = e.encounter_id 
																			where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																) saidas_por_transferencia 
																	group by patient_id 
													) saidas_por_transferencia
													left join
													(  
															select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
															from (
																		select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																		from (
																					select p.patient_id, max(encounter_datetime) data_fila     
																					from patient p     
																							inner join person pe on pe.person_id = p.patient_id   
																							inner join encounter e on e.patient_id=p.patient_id   
																					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																							and e.location_id=:location and e.encounter_datetime < :startDate          
																							group by p.patient_id 
																		) ultimo_fila  
																		left join            
																		obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																		and obs_fila.voided=0   
																		and obs_fila.obs_datetime=ultimo_fila.data_fila      
																		and obs_fila.concept_id=5096           
																		and obs_fila.location_id=:location  
																		union
																		select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																		from patient p     
																				inner join person pe on pe.person_id = p.patient_id   
																				inner join encounter e on p.patient_id=e.patient_id   
																				inner join obs o on e.encounter_id=o.encounter_id 
																		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																				and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime < :startDate
																				group by p.patient_id
															) ultimo_levantamento group by patient_id
													) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
													where ultimo_levantamento.data_ultimo_levantamento < :startDate
											) saidas_por_transferencia on inicio.patient_id = saidas_por_transferencia.patient_id 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date<= date_add(:startDate, interval -1 day) and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (8,10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366,1709)       
																	and o.obs_datetime<= date_add(:startDate, interval -1 day)  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date<=date_add(:startDate, interval -1 day)
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime<= date_add(:startDate, interval -1 day)  and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
															union
															select saidas_por_transferencia.patient_id, data_estado 
															from (
																		select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																		from (
																					select distinct max_estado.patient_id, max_estado.data_estado 
																					from (          
																								select pg.patient_id, max(ps.start_date) data_estado
																								from patient p   
																										inner join person pe on pe.person_id = p.patient_id 
																										inner join patient_program pg on p.patient_id = pg.patient_id         
																										inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																								where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																										and ps.start_date<= date_add(:startDate, interval -1 day)  and pg.location_id =:location group by pg.patient_id       
																							 ) max_estado            
																							inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																							inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																					where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																					union 
																					select  p.patient_id,max(o.obs_datetime) data_estado   
																					from patient p       
																							inner join person pe on pe.person_id = p.patient_id 
																							inner join encounter e on p.patient_id=e.patient_id 
																							inner join obs  o on e.encounter_id=o.encounter_id  
																					where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																							and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																							and o.obs_datetime<= date_add(:startDate, interval -1 day) and e.location_id=:location
																							group by p.patient_id   
																					union 
																					select ultimabusca.patient_id, ultimabusca.data_estado      
																					from (
																								select p.patient_id,max(e.encounter_datetime) data_estado             
																								from patient p  
																										inner join person pe on pe.person_id = p.patient_id 
																										inner join encounter e on p.patient_id=e.patient_id 
																										inner join obs o on o.encounter_id=e.encounter_id   
																								where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= date_add(:startDate, interval -1 day)   
																										and e.encounter_type = 21 and  e.location_id=:location           
																										group by p.patient_id         
																							) ultimabusca       
																							inner join encounter e on e.patient_id = ultimabusca.patient_id       
																							inner join obs o on o.encounter_id = e.encounter_id 
																					where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																		) saidas_por_transferencia 
																			group by patient_id 
															) saidas_por_transferencia
															left join
															(  
																	select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
																	from (
																				select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																				from (
																							select p.patient_id, max(encounter_datetime) data_fila     
																							from patient p     
																									inner join person pe on pe.person_id = p.patient_id   
																									inner join encounter e on e.patient_id=p.patient_id   
																							where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																									and e.location_id=:location and e.encounter_datetime <= date_add(:endDate, interval -3 month)          
																									group by p.patient_id 
																				) ultimo_fila  
																				left join            
																				obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																				and obs_fila.voided=0   
																				and obs_fila.obs_datetime=ultimo_fila.data_fila      
																				and obs_fila.concept_id=5096           
																				and obs_fila.location_id=:location  
																				union
																				select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																				from patient p     
																						inner join person pe on pe.person_id = p.patient_id   
																						inner join encounter e on p.patient_id=e.patient_id   
																						inner join obs o on e.encounter_id=o.encounter_id 
																				where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																						and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= date_add(:endDate, interval -3 month) 
																						group by p.patient_id
																	) ultimo_levantamento group by patient_id
															) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
															where ultimo_levantamento.data_ultimo_levantamento <= date_add(:endDate, interval -3 month)
																		  
													) allsaida        
															group by patient_id 
											) saida on inicio.patient_id = saida.patient_id            
											left join             
											( 		select p.patient_id,max(encounter_datetime) data_fila      
													from patient p     
														   inner join person pe on pe.person_id = p.patient_id   
														   inner join encounter e on e.patient_id=p.patient_id   
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
															and e.location_id=:location and e.encounter_datetime<=:endDate          
															group by p.patient_id 
											) max_fila on inicio.patient_id=max_fila.patient_id       
											left join            
											(		select  p.patient_id,max(encounter_datetime) data_seguimento            
													from patient p     
															inner join person pe on pe.person_id = p.patient_id   
															inner join encounter e on e.patient_id=p.patient_id   
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)          
															and e.location_id=:location and e.encounter_datetime<=:endDate          
															group by p.patient_id 
											) max_consulta on inicio.patient_id=max_consulta.patient_id   
											left join            
											(		select  p.patient_id,max(value_datetime) data_recepcao_levantou             
													from    patient p     
															inner join person pe on pe.person_id = p.patient_id   
															inner join encounter e on p.patient_id=e.patient_id   
															inner join obs o on e.encounter_id=o.encounter_id 
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
															and o.concept_id=23866 and o.value_datetime is not null              
															and  o.value_datetime<=:endDate and e.location_id=:location
															group by p.patient_id 
											) max_recepcao on inicio.patient_id=max_recepcao.patient_id  
												group by inicio.patient_id             
						) inicio_fila_seg     
						left join            
						obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id
								and obs_fila.voided=0   
								and obs_fila.obs_datetime=inicio_fila_seg.data_fila      
								and obs_fila.concept_id=5096           
								and obs_fila.location_id=:location     
						left join            
						obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id   
								and obs_seguimento.voided=0             
								and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento             
								and obs_seguimento.concept_id=1410      
								and obs_seguimento.location_id=:location
								group by inicio_fila_seg.patient_id     
			) inicio_fila_seg_prox
					group by patient_id   
) coorte12meses_final 
where  ( (data_saida_obito is null or (data_saida_obito is not null and  data_usar_c > data_saida_obito))  and data_entrada_obito >= data_usar_c and data_entrada_obito between :startDate and :endDate)
)dead

union

select trOut.patient_id, 2 type_patient from 
(
select patient_id         
from (
			select inicio_fila_seg_prox.*,         
					greatest(coalesce(data_fila,data_seguimento),coalesce(data_seguimento,data_fila))  data_usar_c, 
					greatest(coalesce(data_proximo_lev,data_recepcao_levantou30),coalesce(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) maximo_proximo_fila_recepcao,    
					greatest(coalesce(data_proximo_lev,data_proximo_seguimento,data_recepcao_levantou30),coalesce(data_proximo_seguimento,data_proximo_lev,data_recepcao_levantou30),coalesce(data_recepcao_levantou30,data_proximo_seguimento,data_proximo_lev)) data_usar 
             from (
						select inicio_fila_seg.*,
							max(obs_fila.value_datetime) data_proximo_lev,  
							max(obs_seguimento.value_datetime) data_proximo_seguimento,       
							date_add(data_recepcao_levantou, interval 30 day) data_recepcao_levantou30          
						from (
									select inicio.*,        
										saida.data_estado,  
										entrada_obitos.data_estado data_entrada_obito,
										saidas_obitos.data_estado data_saida_obito,
										entradas_por_transferencia.data_estado  data_entrada_transferencia,
										saidas_por_transferencia.data_estado data_saida_transferencia,
										max_fila.data_fila, 
										max_consulta.data_seguimento,     
										max_recepcao.data_recepcao_levantou   
									from (   
												select patient_id,min(data_inicio) data_inicio      
												from (           
															select  p.patient_id,min(e.encounter_datetime) data_inicio    
															from patient p         
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id   
																	inner join obs o on o.encounter_id=e.encounter_id     
															where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0           
																	and e.encounter_type in (18,6,9) and o.concept_id=1255 and o.value_coded=1256              
																	and e.encounter_datetime<=:endDate and e.location_id=:location
																	group by p.patient_id     
															union   
															select  p.patient_id,min(value_datetime) data_inicio          
															from patient p
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs o on e.encounter_id=o.encounter_id     
															where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type in (18,6,9,53)           
																	and o.concept_id=1190 and o.value_datetime is not null 
																	and o.value_datetime<=:endDate and e.location_id=:location
																	group by p.patient_id     
															union   
															select  pg.patient_id,min(date_enrolled) data_inicio          
															from patient p         
																	inner join person pe on pe.person_id = p.patient_id       
																	inner join patient_program pg on p.patient_id=pg.patient_id   
															where   pg.voided=0 and p.voided=0 and pe.voided = 0 and program_id=2 and date_enrolled<=:endDate and location_id=:location 
																	group by pg.patient_id    
															union   
															select e.patient_id, min(e.encounter_datetime) as data_inicio
															from patient p     
																	inner join person pe on pe.person_id = p.patient_id   
																	inner join encounter e on p.patient_id=e.patient_id   
															where p.voided=0 and pe.voided = 0 and e.encounter_type=18 and e.voided=0 and e.encounter_datetime<=:endDate and e.location_id=:location  
																	group by  p.patient_id
															union 
															select  p.patient_id,min(value_datetime) data_inicio    
															from patient p   
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs o on e.encounter_id=o.encounter_id   
															where   p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52   
																	and o.concept_id=23866 and o.value_datetime is not null            
																	and o.value_datetime<=:endDate and e.location_id=:location            
																	group by p.patient_id   
														) inicio_real           
															group by patient_id         
											) inicio 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date<= :endDate and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
																	and o.obs_datetime<= :endDate  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date<=:endDate
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime<= :endDate and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
													) allsaida        
															group by patient_id 
											) entrada_obitos on inicio.patient_id = entrada_obitos.patient_id 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date< :startDate and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366)       
																	and o.obs_datetime< :startDate  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date< :startDate
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime< :startDate and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
													) allsaida        
															group by patient_id 
											) saidas_obitos on inicio.patient_id = saidas_obitos.patient_id 
											left join
											(
												select saidas_por_transferencia.patient_id, data_estado 
													from (
																select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																from (
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (          
																						select pg.patient_id, max(ps.start_date) data_estado
																						from patient p   
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join patient_program pg on p.patient_id = pg.patient_id         
																								inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																						where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																								and ps.start_date<= :endDate  and pg.location_id =:location group by pg.patient_id       
																					 ) max_estado            
																					inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																			where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																			union 
																			select  p.patient_id,max(o.obs_datetime) data_estado   
																			from patient p       
																					inner join person pe on pe.person_id = p.patient_id 
																					inner join encounter e on p.patient_id=e.patient_id 
																					inner join obs  o on e.encounter_id=o.encounter_id  
																			where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																					and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																					and o.obs_datetime<=:endDate and e.location_id=:location
																					group by p.patient_id   
																			union 
																			select ultimabusca.patient_id, ultimabusca.data_estado      
																			from (
																						select p.patient_id,max(e.encounter_datetime) data_estado             
																						from patient p  
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join encounter e on p.patient_id=e.patient_id 
																								inner join obs o on o.encounter_id=e.encounter_id   
																						where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= :endDate  
																								and e.encounter_type = 21 and  e.location_id=:location           
																								group by p.patient_id         
																					) ultimabusca       
																					inner join encounter e on e.patient_id = ultimabusca.patient_id       
																					inner join obs o on o.encounter_id = e.encounter_id 
																			where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																) saidas_por_transferencia 
																	group by patient_id 
													) saidas_por_transferencia
													left join
													(  
															select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
															from (
																		select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																		from (
																					select p.patient_id, max(encounter_datetime) data_fila     
																					from patient p     
																							inner join person pe on pe.person_id = p.patient_id   
																							inner join encounter e on e.patient_id=p.patient_id   
																					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																							and e.location_id=:location and e.encounter_datetime <= :endDate          
																							group by p.patient_id 
																		) ultimo_fila  
																		left join            
																		obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																		and obs_fila.voided=0   
																		and obs_fila.obs_datetime=ultimo_fila.data_fila      
																		and obs_fila.concept_id=5096           
																		and obs_fila.location_id=:location  
																		union
																		select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																		from patient p     
																				inner join person pe on pe.person_id = p.patient_id   
																				inner join encounter e on p.patient_id=e.patient_id   
																				inner join obs o on e.encounter_id=o.encounter_id 
																		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																				and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= :endDate
																				group by p.patient_id
															) ultimo_levantamento group by patient_id
													) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
													where ultimo_levantamento.data_ultimo_levantamento <= :endDate
											) entradas_por_transferencia on inicio.patient_id = entradas_por_transferencia.patient_id 
											left join
											(
												select saidas_por_transferencia.patient_id, data_estado 
													from (
																select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																from (
																			select distinct max_estado.patient_id, max_estado.data_estado 
																			from (          
																						select pg.patient_id, max(ps.start_date) data_estado
																						from patient p   
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join patient_program pg on p.patient_id = pg.patient_id         
																								inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																						where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																								and ps.start_date< :startDate  and pg.location_id =:location group by pg.patient_id       
																					 ) max_estado            
																					inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																					inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																			where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																			union 
																			select  p.patient_id,max(o.obs_datetime) data_estado   
																			from patient p       
																					inner join person pe on pe.person_id = p.patient_id 
																					inner join encounter e on p.patient_id=e.patient_id 
																					inner join obs  o on e.encounter_id=o.encounter_id  
																			where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																					and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																					and o.obs_datetime<:startDate and e.location_id=:location
																					group by p.patient_id   
																			union 
																			select ultimabusca.patient_id, ultimabusca.data_estado      
																			from (
																						select p.patient_id,max(e.encounter_datetime) data_estado             
																						from patient p  
																								inner join person pe on pe.person_id = p.patient_id 
																								inner join encounter e on p.patient_id=e.patient_id 
																								inner join obs o on o.encounter_id=e.encounter_id   
																						where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime< :startDate  
																								and e.encounter_type = 21 and  e.location_id=:location           
																								group by p.patient_id         
																					) ultimabusca       
																					inner join encounter e on e.patient_id = ultimabusca.patient_id       
																					inner join obs o on o.encounter_id = e.encounter_id 
																			where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																) saidas_por_transferencia 
																	group by patient_id 
													) saidas_por_transferencia
													left join
													(  
															select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
															from (
																		select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																		from (
																					select p.patient_id, max(encounter_datetime) data_fila     
																					from patient p     
																							inner join person pe on pe.person_id = p.patient_id   
																							inner join encounter e on e.patient_id=p.patient_id   
																					where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																							and e.location_id=:location and e.encounter_datetime < :startDate          
																							group by p.patient_id 
																		) ultimo_fila  
																		left join            
																		obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																		and obs_fila.voided=0   
																		and obs_fila.obs_datetime=ultimo_fila.data_fila      
																		and obs_fila.concept_id=5096           
																		and obs_fila.location_id=:location  
																		union
																		select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																		from patient p     
																				inner join person pe on pe.person_id = p.patient_id   
																				inner join encounter e on p.patient_id=e.patient_id   
																				inner join obs o on e.encounter_id=o.encounter_id 
																		where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																				and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime < :startDate
																				group by p.patient_id
															) ultimo_levantamento group by patient_id
													) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
													where ultimo_levantamento.data_ultimo_levantamento < :startDate
											) saidas_por_transferencia on inicio.patient_id = saidas_por_transferencia.patient_id 
											left join    
											( 
												  select patient_id,max(data_estado) data_estado    
												  from (         
															select distinct max_estado.patient_id, max_estado.data_estado 
															from (          
																		select  pg.patient_id,  
																			max(ps.start_date) data_estado
																		from patient p   
																				inner join person pe on pe.person_id = p.patient_id 
																				inner join patient_program pg on p.patient_id = pg.patient_id         
																				inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																		where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																				and ps.start_date<= date_add(:startDate, interval -1 day) and pg.location_id =:location 
																				group by pg.patient_id       
																	) max_estado            
																		inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																		inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
															where pp.program_id = 2 and ps.state in (8,10) and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
															union 
															select  p.patient_id,   
																	max(o.obs_datetime) data_estado   
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs  o on e.encounter_id=o.encounter_id  
															where   e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																	and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded in (1366,1709)       
																	and o.obs_datetime<= date_add(:startDate, interval -1 day)  and e.location_id=:location
																	group by p.patient_id   
															union 
															select person_id as patient_id,death_date as data_estado    
															from person             
															where dead=1 and voided = 0 and death_date is not null and death_date<=date_add(:startDate, interval -1 day)
															union 
															select  p.patient_id,   
																	max(obsobito.obs_datetime) data_estado
															from patient p       
																	inner join person pe on pe.person_id = p.patient_id 
																	inner join encounter e on p.patient_id=e.patient_id 
																	inner join obs obsobito on e.encounter_id=obsobito.encounter_id       
															where   e.voided=0 and p.voided=0 and pe.voided = 0 and obsobito.voided=0  
																	and e.encounter_type in (21,36,37) and  e.encounter_datetime<= date_add(:startDate, interval -1 day)  and  e.location_id=:location        
																	and obsobito.concept_id in (2031,23944,23945) and obsobito.value_coded=1366   
																	group by p.patient_id   
															union
															select saidas_por_transferencia.patient_id, data_estado 
															from (
																		select saidas_por_transferencia.patient_id, max(data_estado) data_estado
																		from (
																					select distinct max_estado.patient_id, max_estado.data_estado 
																					from (          
																								select pg.patient_id, max(ps.start_date) data_estado
																								from patient p   
																										inner join person pe on pe.person_id = p.patient_id 
																										inner join patient_program pg on p.patient_id = pg.patient_id         
																										inner join patient_state ps on pg.patient_program_id = ps.patient_program_id            
																								where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2    
																										and ps.start_date<= date_add(:startDate, interval -1 day)  and pg.location_id =:location group by pg.patient_id       
																							 ) max_estado            
																							inner join patient_program pp on pp.patient_id = max_estado.patient_id    
																							inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado         
																					where pp.program_id = 2 and ps.state = 7 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location   
																					union 
																					select  p.patient_id,max(o.obs_datetime) data_estado   
																					from patient p       
																							inner join person pe on pe.person_id = p.patient_id 
																							inner join encounter e on p.patient_id=e.patient_id 
																							inner join obs  o on e.encounter_id=o.encounter_id  
																					where e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0         
																							and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1706       
																							and o.obs_datetime<= date_add(:startDate, interval -1 day) and e.location_id=:location
																							group by p.patient_id   
																					union 
																					select ultimabusca.patient_id, ultimabusca.data_estado      
																					from (
																								select p.patient_id,max(e.encounter_datetime) data_estado             
																								from patient p  
																										inner join person pe on pe.person_id = p.patient_id 
																										inner join encounter e on p.patient_id=e.patient_id 
																										inner join obs o on o.encounter_id=e.encounter_id   
																								where e.voided=0 and p.voided=0 and pe.voided = 0 and e.encounter_datetime<= date_add(:startDate, interval -1 day)   
																										and e.encounter_type = 21 and  e.location_id=:location           
																										group by p.patient_id         
																							) ultimabusca       
																							inner join encounter e on e.patient_id = ultimabusca.patient_id       
																							inner join obs o on o.encounter_id = e.encounter_id 
																					where e.encounter_type = 21 and o.voided=0 and o.concept_id=2016 and o.value_coded in (1706,23863) and ultimabusca.data_estado = e.encounter_datetime and e.location_id =:location 
																		) saidas_por_transferencia 
																			group by patient_id 
															) saidas_por_transferencia
															left join
															(  
																	select patient_id, max(data_ultimo_levantamento)  data_ultimo_levantamento    
																	from (
																				select ultimo_fila.patient_id, date_add(obs_fila.value_datetime, interval 1 day) data_ultimo_levantamento
																				from (
																							select p.patient_id, max(encounter_datetime) data_fila     
																							from patient p     
																									inner join person pe on pe.person_id = p.patient_id   
																									inner join encounter e on e.patient_id=p.patient_id   
																							where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
																									and e.location_id=:location and e.encounter_datetime <= date_add(:endDate, interval -3 month)          
																									group by p.patient_id 
																				) ultimo_fila  
																				left join            
																				obs obs_fila on obs_fila.person_id=ultimo_fila.patient_id
																				and obs_fila.voided=0   
																				and obs_fila.obs_datetime=ultimo_fila.data_fila      
																				and obs_fila.concept_id=5096           
																				and obs_fila.location_id=:location  
																				union
																				select p.patient_id, date_add(max(value_datetime), interval 31 day) data_ultimo_levantamento             
																				from patient p     
																						inner join person pe on pe.person_id = p.patient_id   
																						inner join encounter e on p.patient_id=e.patient_id   
																						inner join obs o on e.encounter_id=o.encounter_id 
																				where p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
																						and o.concept_id=23866 and o.value_datetime is not null and e.location_id=:location and o.value_datetime <= date_add(:endDate, interval -3 month) 
																						group by p.patient_id
																	) ultimo_levantamento group by patient_id
															) ultimo_levantamento on saidas_por_transferencia.patient_id = ultimo_levantamento.patient_id 
															where ultimo_levantamento.data_ultimo_levantamento <= date_add(:endDate, interval -3 month)
																		  
													) allsaida        
															group by patient_id 
											) saida on inicio.patient_id = saida.patient_id            
											left join             
											( 		select p.patient_id,max(encounter_datetime) data_fila      
													from patient p     
														   inner join person pe on pe.person_id = p.patient_id   
														   inner join encounter e on e.patient_id=p.patient_id   
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18  
															and e.location_id=:location and e.encounter_datetime<=:endDate          
															group by p.patient_id 
											) max_fila on inicio.patient_id=max_fila.patient_id       
											left join            
											(		select  p.patient_id,max(encounter_datetime) data_seguimento            
													from patient p     
															inner join person pe on pe.person_id = p.patient_id   
															inner join encounter e on e.patient_id=p.patient_id   
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9)          
															and e.location_id=:location and e.encounter_datetime<=:endDate          
															group by p.patient_id 
											) max_consulta on inicio.patient_id=max_consulta.patient_id   
											left join            
											(		select  p.patient_id,max(value_datetime) data_recepcao_levantou             
													from    patient p     
															inner join person pe on pe.person_id = p.patient_id   
															inner join encounter e on p.patient_id=e.patient_id   
															inner join obs o on e.encounter_id=o.encounter_id 
													where   p.voided=0 and pe.voided = 0 and e.voided=0 and o.voided=0 and e.encounter_type=52 
															and o.concept_id=23866 and o.value_datetime is not null              
															and  o.value_datetime<=:endDate and e.location_id=:location
															group by p.patient_id 
											) max_recepcao on inicio.patient_id=max_recepcao.patient_id  
												group by inicio.patient_id             
						) inicio_fila_seg     
						left join            
						obs obs_fila on obs_fila.person_id=inicio_fila_seg.patient_id
								and obs_fila.voided=0   
								and obs_fila.obs_datetime=inicio_fila_seg.data_fila      
								and obs_fila.concept_id=5096           
								and obs_fila.location_id=:location     
						left join            
						obs obs_seguimento on obs_seguimento.person_id=inicio_fila_seg.patient_id   
								and obs_seguimento.voided=0             
								and obs_seguimento.obs_datetime=inicio_fila_seg.data_seguimento             
								and obs_seguimento.concept_id=1410      
								and obs_seguimento.location_id=:location
								group by inicio_fila_seg.patient_id     
			) inicio_fila_seg_prox
					group by patient_id   
) coorte12meses_final 
where  ( (data_saida_transferencia is null or (data_saida_transferencia is not null and  data_usar_c > data_saida_transferencia))  and data_entrada_obito is null and data_entrada_transferencia >= data_usar_c and data_entrada_transferencia <= :endDate and date_add(maximo_proximo_fila_recepcao, interval 1 day) >=:startDate) 
)trOut

union

select refuse.patient_id, 4 type_patient from
(

            select all_stopped_treatment.patient_id                               
            from                
            (                   
                select patient_id,max(data_estado) data_estado                    
                from            
                 (              
                    select distinct max_estado.patient_id, max_estado.data_estado 
                    from        
                    (           
                        select  pg.patient_id, max(ps.start_date) data_estado     
                            from patient p     
                                inner join person pe on pe.person_id = p.patient_id                     
                                inner join patient_program pg on p.patient_id = pg.patient_id           
                                inner join patient_state ps on pg.patient_program_id = ps.patient_program_id           
                            where pg.voided=0 and ps.voided=0 and p.voided=0 and pe.voided = 0 and pg.program_id = 2   
                                and ps.start_date<=:endDate and pg.location_id =:location group by pg.patient_id      
                     ) max_estado              
                            inner join patient_program pp on pp.patient_id = max_estado.patient_id      
                            inner join patient_state ps on ps.patient_program_id = pp.patient_program_id and ps.start_date = max_estado.data_estado       
                        where pp.program_id = 2 and ps.state = 8 and pp.voided = 0 and ps.voided = 0 and pp.location_id =:location                       
                                
                     union      
                                
                     select  p.patient_id,max(o.obs_datetime) data_estado         
                     from   patient p          
                            inner join person pe on pe.person_id = p.patient_id   
                           inner join encounter e on p.patient_id=e.patient_id    
                           inner join obs  o on e.encounter_id=o.encounter_id     
                     where  e.voided=0 and o.voided=0 and p.voided=0 and pe.voided = 0                  
                           and e.encounter_type in (53,6) and o.concept_id in (6272,6273) and o.value_coded = 1709     
                            and o.obs_datetime<=:endDate and e.location_id=:location                    
                            group by p.patient_id                                 
                                
                   ) all_stopped_treatment     
             group by patient_id
            ) all_stopped_treatment            
            left join           
              (                 
                select patient_id,max(max_date) max_date                          
                from            
                  (             
                        Select p.patient_id,max(encounter_datetime) max_date      
                        from    patient p      
                                inner join person pe on pe.person_id = p.patient_id                     
                                inner join encounter e on e.patient_id=p.patient_id                     
                        where   p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type=18     
                                and e.location_id=:location and e.encounter_datetime <=:endDate                     
                            group by p.patient_id                                 
                                
                        union   
                                
                        Select p.patient_id,max(encounter_datetime) max_date      
                        from  patient p        
                                inner join person pe on pe.person_id = p.patient_id                     
                                inner join encounter e on e.patient_id=p.patient_id                     
                        where p.voided=0 and pe.voided = 0 and e.voided=0 and e.encounter_type in (6,9) 
                                and e.location_id=:location and e.encounter_datetime <=:endDate                     
                            group by p.patient_id                                 
                                
                ) last_encounter group  by patient_id                             
                                
            ) last_encounter on all_stopped_treatment.patient_id  = last_encounter.patient_id           
            where all_stopped_treatment.data_estado >= last_encounter.max_date    
)refuse

)final
order by final.patient_id,final.type_patient
)estadoPermanencia on estadoPermanencia.patient_id=HVL_FR40.patient_id

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
) HVL_FR41