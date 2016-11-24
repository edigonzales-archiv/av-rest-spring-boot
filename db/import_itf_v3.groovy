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

def dir = new File("/Users/stefan/tmp/av_tmp_test/")
dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.(?i)zip$/) { file ->
  ant.unzip(src:file,
            dest:dir,
            overwrite:"true")
}

dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.(?i)itf$/) { file ->
  list << file
}

println "#!/bin/bash"

list.each {

	println "#Importing: ${it.getName()}"

	def dataSet = it.getName()[0..3]
	def fileName = it.toString()

	try {

    println "java -jar /Users/stefan/Apps/ili2pg/ili2pg-3.5.0.jar --dbdatabase xanadu2 --dbusr stefan --dbpwd ziegler12 --dbschema av_avdpool_ng --sqlEnableNull --createFkIdx --createGeomIdx --createBasketCol --models DM01AVSO24LV95 --disableValidation --nameByTopic --defaultSrsCode 2056 --dataset $dataSet --replace $fileName"

	} catch (Ili2dbException e) {
		println "Upsi..."
		e.printStackTrace()
	}
}
