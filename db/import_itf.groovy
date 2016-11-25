@GrabResolver(name='catais', root='http://www.catais.org/maven/repository/release/', m2Compatible='true')
@Grab(group='ch.interlis', module='ili2pg', version='3.5.0')

import groovy.io.FileType

import ch.ehi.ili2db.base.Ili2db
import ch.ehi.ili2db.base.Ili2dbException
import ch.ehi.ili2db.gui.Config
import ch.ehi.ili2db.mapping.NameMapping
import ch.ehi.sqlgen.generator.SqlConfiguration

def list = []

def ant = new AntBuilder()
//ant.echo('hello from Ant!')

def dir = new File("/home/stefan/tmp/itf/")
dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.(?i)zip$/) { file ->
  ant.unzip(src:file,
            dest:dir,
            overwrite:"true")
}

dir.traverse(type: FileType.FILES, nameFilter: ~/^(1|2)[a-zA-Z]?.*\.(?i)itf$/) { file ->
  list << file
}

def config = new Config()

def dbhost = "localhost"
def dbport = "5432"
def dbdatabase = "xanadu2"
def dbusr = "stefan"
def dbpwd = "ziegler12"
def dbschema = "av_avdpool_ng"
config.setModeldir(ch.interlis.ili2c.Main.ILI_REPOSITORY)
config.setModels(Ili2db.XTF)
config.setDefaultSrsAuthority("EPSG")
config.setDefaultSrsCode("2056")
config.setMaxSqlNameLength(Integer.toString(NameMapping.DEFAULT_NAME_LENGTH))
config.setIdGenerator(ch.ehi.ili2db.base.TableBasedIdGen.class.getName())
config.setInheritanceTrafo(config.INHERITANCE_TRAFO_SMART1)
//config.setCatalogueRefTrafo(Config.CATALOGUE_REF_TRAFO_COALESCE)
//config.setMultiSurfaceTrafo(Config.MULTISURFACE_TRAFO_COALESCE)
//config.setMultilingualTrafo(Config.MULTILINGUAL_TRAFO_EXPAND)
config.setValidation(false)
//config.setCreateFk(config.CREATE_FK_YES)
config.setCreateFkIdx(config.CREATE_FKIDX_YES)
config.setValue(SqlConfiguration.CREATE_GEOM_INDEX,"True")
config.setSqlNull(config.SQL_NULL_ENABLE)
config.setBasketHandling(config.BASKET_HANDLING_READWRITE)
config.setNameOptimization(config.NAME_OPTIMIZATION_TOPIC)
config.setGeometryConverter(ch.ehi.ili2pg.converter.PostgisColumnConverter.class.getName())
config.setDdlGenerator(ch.ehi.sqlgen.generator_impl.jdbc.GeneratorPostgresql.class.getName())
config.setJdbcDriver("org.postgresql.Driver")
config.setIdGenerator(ch.ehi.ili2pg.PgSequenceBasedIdGen.class.getName())
config.setUuidDefaultValue("uuid_generate_v4()")
config.setDbhost(dbhost)
config.setDbdatabase(dbdatabase)
config.setDbport(dbport)
config.setDbusr(dbusr)
config.setDbpwd(dbpwd)
config.setDburl("jdbc:postgresql://" + dbhost + ":" + dbport + "/" + dbdatabase)
config.setDbschema(dbschema)
config.setConfigReadFromDb(true);

list.each {

	println "Importing: ${it.getName()}"

	def dataSet = it.getName()[0..3]
	def fileName = it.toString()

	config.setDatasetName(dataSet);
	config.setXtffile(fileName);

	try {
		 Ili2db.runUpdate(config, "",Config.FC_REPLACE)
	} catch (Ili2dbException e) {
		println "Upsi..."
		e.printStackTrace()
	}
}
