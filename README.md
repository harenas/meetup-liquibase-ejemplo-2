## Synopsis
Segundo ejemplo mostrado durante el meetup para mostrar la integración de liquibase con Maven.


## Objetivo
Mostrar cómo ejecutar los comandos de liquibase desde Maven, con la idea de generar todos los cambios durante el proceso de building de una aplicación. Este proyecto, además, creará la base de datos en caso de que esta no exista.


## Instalación 
Para poder compilar y probar el servicio se debe ejecutar, previamente, los siguientes pasos:
1. Ejecutar el comando maven que permita saltar la ejecución de tests (por tema de tiempo y alcance, no se hicieron)
	```console 
   $ mvn install -Dmaven.test.skip=true 
	```

## Ejecución del servicio
```console
$ java -jar target/liquibase-demo-base.jar
```

## Operaciones del servicio (METHOD GET)

**empleado**

Obtiene la información de un empleado en particular según su 'rut'

Ejemplo de uso: http://localhost:8080/ejemplo-2/empleado?rut=12345678


**listaEmpleados**

Obtiene el listado completo de empleados y su respectiva información

Ejemplo de uso: http://localhost:8080/ejemplo-2/listaEmpleados


## Tras bambalinas
Hay varios puntos que revisar para entender qué y cómo han ocurrido las cosas. 

En primer lugar está la configuración a nivel de aplicación, que se encuentra en el archivo application.properties y que no varía en comparación con el ejemplo anterior.

Se segundo lugar tenemos un nuevo archivo de configuración llamado ***liquibase.properties*** ubicado en ***src/main/resources/liquibase***. En este archivo es donde va la información que en el ejemplo anterior se pasaba como parámetro del comando principal y que sirve para indicar a liquibase la forma de conexión con la base de datos.

```console
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/liquibase_ejemplo_2?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8
username=liquibase
password=liquibase
promptOnNonLocalDatabase=false
changeLogFile=src/main/resources/liquibase/master.xml
```

De este archivo es importante, para este documento, entender dos líneas:

**url**

Es la URL de conexión a la base de datos, pero tiene un detalle importante que son los parámetros ***createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8** que nos permiten crear la base de datos en formato utf-8 en caso no existir, para posteriormente crear las tablas.

**changeLogFile**

En este parámetro indicamos cuál es el archivo maestro, o controlador, de los todos los cambios que serán aplicados al proyecto al momento de iniciar un proceso de build de WAR o EAR correspondiente a la aplicación. En este caso se trata del archivo ***src/main/resources/liquibase/master.xml*** (puede tener cualquier nombre, pero se recomienda el indicado aquí).

Finalmente tenemos los archivos con los cambios a realizar y que ya hemos visto en el ejemplo anterior. En este caso sólo utilizaremos changelog-000.xml y changelog-001.xml.

Cabe destacar que liquibase mantiene un historial de los cambios realizados, por lo tanto no repetirán las modificaciones realizadas cada vez que se ejecuten los archivos changelog. Por esta misma razón, se debe volver a atrás en algún cambio hecho con anterioridad, este cambio debe ser un nuevo changeset y no se debe intentar modificar los cambios históricos. Generará un error.



## Liquibase & Maven

El propósito principal de este ejemplo es mostrar cómo integrar liquibase con maven. Para esto es necesario agregar cierto código en el archivo ***pom.xml*** dentro del tag ***plugin*** para liquibase.

```console
	<build>
		<finalName>liquibase-demo-2</finalName>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.liquibase</groupId>
				<artifactId>liquibase-maven-plugin</artifactId>
				<version>3.0.5</version>
				<configuration>
					<propertyFile>src/main/resources/liquibase/liquibase.properties</propertyFile>
				</configuration>
				<executions>
					<execution>
						<phase>process-resources</phase>
						<goals>
							<goal>update</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
````
En donde tal y como se aprecia, se indica la ubicación del archivo properties de liquibase.



## Licencia

Siéntanse en libertad de ejecutar, estudiar, compartir y modificar el código sin necesidad de autorización ni aviso al autor.

