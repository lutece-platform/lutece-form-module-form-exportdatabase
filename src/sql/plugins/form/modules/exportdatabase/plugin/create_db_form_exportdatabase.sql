DROP TABLE IF EXISTS form_exportdatabase_formconfiguration;
DROP TABLE IF EXISTS form_exportdatabase_entryconfiguration;

--
-- table struture for form_exportdatabase_formconfiguration
--

CREATE TABLE form_exportdatabase_formconfiguration (
  id_form  		  INT 		   NOT NULL,
  table_name      VARCHAR(255) NOT NULL,
  table_name_blob VARCHAR(100) NOT NULL,
  PRIMARY KEY(id_form)
);

--
-- table struture for form_exportdatabase_entryconfiguration
--

CREATE TABLE form_exportdatabase_entryconfiguration (
  id_form  		INT			 NOT NULL,
  id_entry      VARCHAR(255) NOT NULL,
  column_name   VARCHAR(100) NOT NULL,
  is_long_value INT 		 NOT NULL,
  has_ref_table INT 		 NOT NULL,
  id_position   INT 		 NOT NULL,
  PRIMARY KEY(id_form, id_entry)
);
