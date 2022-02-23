 select patient_id 
 from 														                                        						         
 	
 	(select inicio_3HP.patient_id,min(inicio_3HP.data_inicio_tpi) data_inicio_tpi 
 		from 

 			( 
 			-- Este bloco procura por pacientes que tem inicio tpt 3hp  no periodo anterior, estamos acreientando novas fontes da ficha resumo e ficha clinica
 				
 			 select inicio.patient_id,min(inicio.data_inicio_tpi) data_inicio_tpi
 				from 																					 
         		(
         		 
         	    select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														 
         					inner join encounter e on p.patient_id=e.patient_id																				 
         					inner join obs o on o.encounter_id=e.encounter_id																				 
         				where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) 	 
         					and o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  e.location_id=:location			 
         				group by p.patient_id	

         		union
         				
         		select p.patient_id,min(obsInicio3HP.value_datetime) data_inicio_tpi from  patient p 
			        inner join encounter e on p.patient_id = e.patient_id 
			        inner join obs o on o.encounter_id = e.encounter_id 
			        inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 
			      where e.voided=0  and p.voided=0  and obsInicio3HP.value_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and 
			            o.voided=0  and o.concept_id = 23985  and o.value_coded = 23954 and obsInicio3HP.concept_id=6128 and obsInicio3HP.voided=0 and 
			            e.encounter_type in (53) and e.location_id=:location 
			        group by p.patient_id 
			      
			      union 
			      
			      select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p 
			        inner join encounter e on p.patient_id = e.patient_id 
			        inner join obs o on o.encounter_id = e.encounter_id 
			        inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 
			      where e.voided=0  and p.voided=0  and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and o.voided=0 and 
			            o.concept_id=23985 and o.value_coded=23954 and obsInicio3HP.concept_id=165308 and obsInicio3HP.value_coded=1256 and obsInicio3HP.voided=0 
			            and e.encounter_type in (6) and e.location_id=:location 
			      group by p.patient_id 

			      union

			      select p.patient_id,min(e.encounter_datetime) data_inicio_tpi  from patient p 
			        inner join encounter e on p.patient_id = e.patient_id 
			        inner join obs o on o.encounter_id = e.encounter_id 
			      where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) and 
			      		o.voided=0 and o.concept_id=1719 and o.value_coded=165307  
			      		and e.encounter_type in (6)  and e.location_id=:location 
			      group by p.patient_id  

         		) inicio 

			left join 																																 
         	
         	-- Este bloco ira verificar todos que tem um inicio anterior ao incio que estamos a avaliar, caso tenha um inicio 4 meses anterior ao inicio em avaliacao sera excluido
         	 (
         		select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id																				 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
         				and o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  e.location_id=:location

         			union			 
         		   
         		    select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id																				 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
         				and o.voided=0 and o.concept_id=1719 and o.value_coded=165307 and e.encounter_type in (6) and  e.location_id=:location

         			union
         			
         		    select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id
         				inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 																				 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
         				and o.voided=0 and o.concept_id=23985 and o.value_coded=23954 and e.encounter_type in (6) and  e.location_id=:location
	         			and obsInicio3HP.concept_id = 165308 
				        and obsInicio3HP.value_coded = 1256 
				        and obsInicio3HP.voided = 0 

				   union

				   select p.patient_id,obsInicio3HP.value_datetime data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id
         				inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 																				 
         			where e.voided=0 and p.voided=0 and p.voided = 0 and obsInicio3HP.value_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  and 
         				  o.voided=0 and o.concept_id=23985 and o.value_coded=23954 and obsInicio3HP.concept_id=6128  and obsInicio3HP.voided=0 and 
         				  e.encounter_type in (53) and e.location_id=:location 

         		    union 	

         		    select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p														         
						inner join encounter e on p.patient_id=e.patient_id																				 		 
						inner join obs o on o.encounter_id=e.encounter_id																				         
					where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 10 month) and (:endDate - interval 6 month)	         
						and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	           																															 
         		) 
         		inicioAnterior on inicio.patient_id=inicioAnterior.patient_id  
         		and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
         		where inicioAnterior.patient_id is null	 																								 
		

		union 


		 select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														 			 
			inner join encounter e on p.patient_id=e.patient_id																				 			 
			inner join obs o on o.encounter_id=e.encounter_id		 																					 
			inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
		where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	 			 
			and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	  			 
			and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705) 								 
		group by p.patient_id	

 		union		

 		select inicio.patient_id,inicio.data_inicio_tpi from																					 
 		(	select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														 
 				inner join encounter e on p.patient_id=e.patient_id																				 
 				inner join obs o on o.encounter_id=e.encounter_id	 																			 
 				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
 			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	 
 				and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 
 				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257,1267) 					 
 			group by p.patient_id

			union 	

			select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														 		 
				inner join encounter e on p.patient_id=e.patient_id																				 		 
				inner join obs o on o.encounter_id=e.encounter_id	 																					 
				left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
					and seguimentoTPT.concept_id =23987  																						
					and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
					and seguimentoTPT.voided =0)							 
			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	         
				and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location	 	     
				and seguimentoTPT.obs_id is null					 
			group by p.patient_id	 																													 
   		
   		) inicio 		


     		left join 																																 
     		
     		(	select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
     				inner join encounter e on p.patient_id=e.patient_id																				 
     				inner join obs o on o.encounter_id=e.encounter_id																				 
     			where  e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month) 
     				 and o.voided=0 and o.concept_id=23985 and o.value_coded in (23954,23984) and e.encounter_type=60 and  e.location_id=:location   
     	          
     	            union

     	            select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id																				 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
         				and o.voided=0 and o.concept_id=1719 and o.value_coded=165307 and e.encounter_type in (6) and  e.location_id=:location

         			union
         			
         		    select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id
         				inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 																				 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
         				and o.voided=0 and o.concept_id=23985 and o.value_coded=23954 and e.encounter_type in (6) and  e.location_id=:location
	         			and obsInicio3HP.concept_id = 165308 
				        and obsInicio3HP.value_coded = 1256 
				        and obsInicio3HP.voided = 0 

				   union

				   select p.patient_id,obsInicio3HP.value_datetime data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id
         				inner join obs obsInicio3HP on obsInicio3HP.encounter_id = e.encounter_id 																				 
         			where e.voided=0 and p.voided=0 and p.voided = 0 and obsInicio3HP.value_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  and 
         				  o.voided=0 and o.concept_id=23985 and o.value_coded=23954 and obsInicio3HP.concept_id=6128  and obsInicio3HP.voided=0 and 
         				  e.encounter_type in (53) and e.location_id=:location 

         		   union

     	          select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p														     
     				inner join encounter e on p.patient_id=e.patient_id																				 
     				inner join obs o on o.encounter_id=e.encounter_id																				 
     			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 10 MONTH) and (:endDate - interval 6 month)  
     				and o.voided=0 and o.concept_id=1719 and o.value_coded=23954 and e.encounter_type in (6,9) and  e.location_id=:location			 
     		
     		) inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
     			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 4 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
     		where inicioAnterior.patient_id is null																									 
         	
         ) inicio_3HP group by inicio_3HP.patient_id		


          union

         	-- Este bloco procura inicio de INH no periodo anterior

         	select inicio_INH.patient_id,min(inicio_INH.data_inicio_tpi) data_inicio_tpi from 
         	(														 
         	   select inicio.patient_id,inicio.data_inicio_tpi from																					 
         		(
         		   select p.patient_id,min(e.encounter_datetime) data_inicio_tpi	from	patient p													 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id	 																			 
         				inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id														 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)   
         				and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1257)	 						 
         				and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location		 
         			group by p.patient_id	 																											 
			union 

			select p.patient_id,min(e.encounter_datetime) data_inicio_tpi	from	patient p													         
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id																				 
         				left join obs seguimentoTPT on (e.encounter_id =seguimentoTPT.encounter_id	 													
							and seguimentoTPT.concept_id =23987  																						
							and seguimentoTPT.value_coded in(1256,1257,1705,1267)  																		
							and seguimentoTPT.voided =0)						 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)   
         				and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
         				 and seguimentoTPT.obs_id is null 	         
         			group by p.patient_id			                                                                                                     
         		)inicio	

         		left join 																																 
         		(
         		 
         		select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id																				 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - INTERVAL 13 MONTH) and (:endDate - interval 6 month)  
         				and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location      
         			union                                                                                                                                
         		
         		select p.patient_id, o.value_datetime data_inicio_tpi from patient p                                                                
					inner join encounter e on p.patient_id=e.patient_id                                                                                      
					inner join obs o on o.encounter_id=e.encounter_id                                                                                        
				where e.voided=0 and p.voided=0 and o.value_datetime between (:startDate - interval 13 month) and (:endDate - interval 7 month)            
							and o.voided=0 and o.concept_id=6128 and e.encounter_type in(6,9,53) and e.location_id=:location  	

				union

				select p.patient_id, e.encounter_datetime data_inicio_tpi from patient p                                                                
					inner join encounter e on p.patient_id=e.patient_id                                                                                      
					inner join obs o on o.encounter_id=e.encounter_id                                                                                        
				where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 13 month) and (:endDate - interval 7 month)            
							and o.voided=0 and o.concept_id=6122  and o.value_coded =1256 and  e.encounter_type in(6,9) and e.location_id=:location 

                -- Estamos adicionando novas fontes da ficha resumo e ficha clinica tambem para exclusao caso o paciente tenha uma consulta INH no periodo anterior ao periodo em analise
				
				union 

				select p.patient_id,obsInicioINH.value_datetime data_inicio_tpi 
			      from 
			        patient p 
			        inner join encounter e on p.patient_id = e.patient_id 
			        inner join obs o on o.encounter_id = e.encounter_id 
			        inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
			      where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type=53 and o.concept_id=23985 and o.value_coded=656
			      	    and obsInicioINH.concept_id=6128 and obsInicioINH.voided=0
			      	    and obsInicioINH.value_datetime between (:startDate - interval 13 month) and (:endDate - interval 7 month)

			     union

			    select p.patient_id,e.encounter_datetime data_inicio_tpi from patient p 
			        inner join encounter e on p.patient_id = e.patient_id 
			        inner join obs o on o.encounter_id = e.encounter_id 
			        inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
			      where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type=6 and o.concept_id=23985 and o.value_coded=656
			      	    and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
			      	    and e.encounter_datetime between (:startDate - interval 13 month) and (:endDate - interval 7 month)
 		                                 		      		                                  		                                                    
         		) inicioAnterior on inicioAnterior.patient_id=inicio.patient_id  																		 
         			and inicioAnterior.data_inicio_tpi between (inicio.data_inicio_tpi - INTERVAL 7 MONTH) and (inicio.data_inicio_tpi - INTERVAL 1 day) 
         		where inicioAnterior.patient_id is null	

         		union 

		         select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p														     
					inner join encounter e on p.patient_id=e.patient_id																				             
					inner join obs o on o.encounter_id=e.encounter_id		                                                                                     
					inner join obs seguimentoTPT on seguimentoTPT.encounter_id=e.encounter_id																	 
				where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)	             
					and o.voided=0 and o.concept_id=23985 and o.value_coded in (656,23982) and e.encounter_type=60 and  e.location_id=:location	                 
					and seguimentoTPT.voided =0 and seguimentoTPT.concept_id =23987 and seguimentoTPT.value_coded in (1256,1705)                                 
				group by p.patient_id    

         		union	

         		select p.patient_id,min(o.value_datetime) data_inicio_tpi from patient p																 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id																				 
         			where e.voided=0 and p.voided=0 and o.value_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month) 	     
         				and o.voided=0 and o.concept_id=6128 and e.encounter_type in (6,9,53) and e.location_id=:location								 
         			group by p.patient_id	

         		union 	

         		select p.patient_id,min(e.encounter_datetime) data_inicio_tpi from patient p															 
         				inner join encounter e on p.patient_id=e.patient_id																				 
         				inner join obs o on o.encounter_id=e.encounter_id																				 
         			where e.voided=0 and p.voided=0 and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)   
         				and o.voided=0 and o.concept_id=6122 and o.value_coded=1256 and e.encounter_type in (6,9) and  e.location_id=:location			 
         			group by p.patient_id	

         		union																											 
         	
         			-- Acreicentando as novas fontes da ficha clinica e ficha resumo para determinar o inicio INH

         	    select p.patient_id,min(obsInicioINH.value_datetime) data_inicio_tpi 
			      from 
			        patient p 
			        inner join encounter e on p.patient_id = e.patient_id 
			        inner join obs o on o.encounter_id = e.encounter_id 
			        inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
			      where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type=53 and o.concept_id=23985 and o.value_coded=656
			      	    and obsInicioINH.concept_id=6128 and obsInicioINH.voided=0
			      	    and obsInicioINH.value_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)
			     group by p.patient_id

			     union

			    select p.patient_id,min(e.encounter_datetime) data_inicio_tpi 
			      from 
			        patient p 
			        inner join encounter e on p.patient_id = e.patient_id 
			        inner join obs o on o.encounter_id = e.encounter_id 
			        inner join obs obsInicioINH on obsInicioINH.encounter_id = e.encounter_id 
			      where e.voided=0 and p.voided=0 and o.voided=0 and e.encounter_type=6 and o.concept_id=23985 and o.value_coded=656
			      	    and obsInicioINH.concept_id=165308 and obsInicioINH.value_coded=1256 and obsInicioINH.voided=0
			      	    and e.encounter_datetime between (:startDate - interval 6 month) and (:endDate - interval 6 month)
			      group by p.patient_id 	    

         	) inicio_INH group by inicio_INH.patient_id																									 
        ) inicio_TPT	                                                                                                                                 ;