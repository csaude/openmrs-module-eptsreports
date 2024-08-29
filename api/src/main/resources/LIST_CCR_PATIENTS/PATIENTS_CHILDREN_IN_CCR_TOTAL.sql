       select distinct ccr.patient_id
          from (
        select patient_id, min(data_inicio) data_inicio from (
         select  pg.patient_id,min(date_enrolled) data_inicio                                
         from  patient p inner join patient_program pg on p.patient_id=pg.patient_id                   
         where   pg.voided=0 and p.voided=0 and program_id=6 and date_enrolled between :startDate and :endDate and location_id=:location       
         group by pg.patient_id  
        union
        Select  p.patient_id,min(e.encounter_datetime) data_inicio                                
          from  patient p                                                 
              inner join encounter e on p.patient_id=e.patient_id                                                   
          where   p.voided=0 and e.voided=0 and e.encounter_type = 92 and                                      
              e.encounter_datetime between :startDate and :endDate and e.location_id=:location                              
          group by p.patient_id 
          ) ccr group by ccr.patient_id
          ) ccr
            inner join person p on p.person_id=ccr.patient_id          
            left join   ( 
            select pad1.*  from person_address pad1  
            inner join   (  
            select person_id,max(person_address_id) id   from person_address  
            where voided=0  
            group by person_id  
            ) pad2  
            where pad1.person_id=pad2.person_id and pad1.person_address_id=pad2.id  
            ) pad3 on pad3.person_id=ccr.patient_id 
            left join( 
            select pn1.*  from person_name pn1  
            inner join (  
            select person_id,min(person_name_id) id   from person_name  
            where voided=0  
            group by person_id  
            ) pn2  
            where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id  
            ) pn on pn.person_id=ccr.patient_id 
            inner join  ( 
            select pid1.*  from patient_identifier pid1  
            inner join  (  
            select patient_id,min(patient_identifier_id) id, identifier  from patient_identifier  
            where voided=0 and identifier_type = 9
            group by patient_id  
            ) pid2 
            where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id  
            ) pid on pid.patient_id=ccr.patient_id