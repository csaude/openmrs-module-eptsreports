update obs inner join encounter using(encounter_id)
set value_coded = 1
where obs.concept_id = 165337 and obs.value_coded =1169 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146; 
  	
update obs inner join encounter using(encounter_id)
set value_coded = 2
where obs.concept_id = 165337 and obs.value_coded = 1366 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146; 
  	 
update obs inner join encounter using(encounter_id)
set value_coded = 3
where obs.concept_id = 165337 and obs.value_coded = 1704 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146;   

update obs inner join encounter using(encounter_id)
set value_coded = 4
where obs.concept_id = 165337 and obs.value_coded = 6275 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146;   
	
update obs inner join encounter using(encounter_id)
set value_coded = 5
where obs.concept_id = 165337 and obs.value_coded = 6276 and obs.comments is null and obs.date_created <= now()
and encounter.encounter_type = 47 and encounter.form_id =146;   
 	
update obs set value_coded = 1704, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 1169 => 1704)' where concept_id = 165337 and value_coded = 1;

update obs set value_coded = 1169, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 1366 => 1169)' where concept_id = 165337 and value_coded = 2;

update obs set value_coded = 6275, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 1704 => 6275)' where concept_id = 165337 and value_coded = 3;

update obs set value_coded = 6276, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 6275 => 6276)' where concept_id = 165337 and value_coded = 4;

update obs set value_coded = 1366, comments = 'SESP Ver. 3.13.0 - Situação HIV (value_coded 6276 => 1366)' where concept_id = 165337 and value_coded = 5;


update obs inner join encounter using(encounter_id)
set value_coded = 165469,
comments = 'SESP Ver. 3.13.0 - Prep Inicial (value_coded 23882 => 165469)'
where obs.concept_id = 165290 and obs.value_coded = 23882 
and encounter.encounter_type = 80;

update program set retired = 1 where program_id = 9;

update obs join encounter on encounter.encounter_id = obs.encounter_id
 set value_coded = 165306, 
  comments ='SESP Release 13.3.0 - change FILT Regimen TPT from answer 755' 
 where concept_id = 23985 and value_coded = 755
 and encounter.encounter_type  = 60;

update obs join encounter on encounter.encounter_id = obs.encounter_id 
set
 obs.value_coded =6424,
 obs.comments ='SESP release 3.13.0 - change ANTI-RETROVIRAIS PRESCRITOS answer from 6108'
where
 obs.concept_id in (1087, 21190, 21187, 21188, 23893) and obs.value_coded = 6108 
and encounter.encounter_type in (6, 90, 53);