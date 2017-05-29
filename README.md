## Synopsis
Primer ejemplo mostrado durante el meetup para mostrar como ejecutar liquibase desde línea de comando y cómo las modificaciones afectan la base de datos sin perjudicar el servicio propiamente tal.


## Objetivo
Simular el inicio de uso de Liquibase en un proyecto que ya ha partido, generando un changelog inicial a partir de cual se seguirá trabajando durante las restantes fases del proyecto.


## Instalación 
Para poder compilar y probar el servicio se debe ejecutar, previamente, los siguientes pasos:
1. Crear la base de datos (schema) 'liquibase-ejemplo-1' y usuario *liquibase* con password *liquibase* como owner del schema. 
2. Ejecutar el script liquibase-base.sql que generará la tabla empleados.
3. Ejecutar el comando maven que permita saltar la ejecución de tests (por tema de tiempo y alcance, no se hicieron)
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

Ejemplo de uso: http://localhost:8080/ejemplo-1/empleado?rut=12345678


**listaEmpleados**

Obtiene el listado completo de empleados y su respectiva información

Ejemplo de uso: http://localhost:8080/ejemplo-1/listaEmpleados


## Creación de changelog base
Para la generación del changelog base, se utilizará liquibase desde la línea de comandos. Como se aprecia en la secuencia que está más abajo, se debe utilizar varios parámetros.

| Parámetro       | Uso         | 
| ----------------|-------------| 
| **driver**      |Driver de base de datos a utilizar |
|**classpath**    |Ruta completa de la ubicación del driver|
|**changeLogFile**|Ruta y nombre del archivo que se generará|
|**url**          |URL de conexión a la base de datos|
|**username**     |Nombre de usuario para conectarse a la base de datos (se asume que el usuario tiene privilegios suficientes para las operaciones de lectura, escritura y manipulación de tablas)|
|**password**     |Clave para conexión del usuario|
|**generateChangeLog**|Genera un archivo ***changeLogFile***|
|**migrate**|Ejecuta instrucciones en un archivo ***changeLogFile***|

 
Previo a la ejecución, se debe acceder al directorio raíz del proyecto, desde donde habitualmente se ejecutaría el comando *mvn* (maven).

```console
$ liquibase --driver=com.mysql.jdbc.Driver \
      --classpath=/Users/harenas/Desarrollo/mysql-connector-java-5.1.41-bin.jar \
      --changeLogFile=src/main/resources/liquibase/changelog-000.xml \
      --url="jdbc:mysql://localhost:3306/liquibase-ejemplo-1" \
      --username=liquibase \
      --password=liquibase \
      generateChangeLog
```

Como resultadoo del comando anterior, obtendremos el archivo **changelog-000.xml** en el directorio *src/main/resources/liquibase/* ubicado dentro del proyecto.

Junto con generar el archivo, se puede apreciar en el schema en uso, la creación de dos tablas: ***databasechangelog*** y ***databasechangelock***. 

La primera tabla lleva un registro con los changeSets especificados por liquibase (un historial completo) con el checkSum de cada uno de ellos, para de esa forma garantizar la integridad de los cambios a lo largo del tiempo.

La segunda tabla lleva el control de liquibase para permitir cambios en ese momento o bloquear la base de datos mientras otro usuario está realizando cambios para posteriormente liberarla.

## Agregar columna address

En este paso se verá cómo modificar una tabla desde Liquibase. Puntualmente, se agregará la columna ***address***.

Junto con agregar la nueva columna, se completarán los datos que al momento de crearla quedarán con ***null*** y, por último, se agregará un nuevo registro completo con un nuevo empleado.

Puede revisar el contenido del archivo ***changelog-001.xml*** ubicado en ***src/main/resources/liquibase***, dentro del proyecto, con los cambios a realizar.

Analizando un poco el código, tenemos:

```xml
	<changeSet author="harenas" id="20170427184001">
		<comment>Agrega dato address a la tabla empleados </comment>
		<addColumn catalogName="liquibase-ejemplo-1"
			tableName="empleados">
			<column name="address" type="varchar(255)" />
		</addColumn>
	</changeSet>
```

La definición de un changeSet creador por Héctor Arenas el día 27 de abril del 2017 a las 18:40. Este changeSet tiene por finalidad agregar dato address a la tabla empleados.

Finalmente, se indica que en la base de datos (catalogName) ***liquibase-ejemplo-1***, tabla ***empleados***, se agregará (addColumn) la columna ***address*** de tipo (type) varchar(255)


## Generar modificación - Agrega ADDRESS

Respecto del comando anterior, sólo observar la diferencia en el último parámetro ***migrate*** que provoca que en lugar de retornar un archivo (generateChangeLog), leerá un archivo y ejecutará las ordenes que estén contenidas.

```console
$ liquibase --driver=com.mysql.jdbc.Driver \
     --classpath=/Users/harenas/Desarrollo/mysql-connector-java-5.1.41-bin.jar \
     --changeLogFile=src/main/resources/liquibase/changelog-001.xml \
     --url="jdbc:mysql://localhost:3306/liquibase-ejemplo-1" \
     --username=liquibase \
     --password=liquibase \
     migrate
```

Luego de ejecutar el comando, revisar la tabla ***empleados*** y su contenido.


## Modificaciones en código java

Para ver reflejados los cambios en el servicio, se puede editar la clase ***EmpleadosEntity.java*** del package controller y descomentar los bloques de código que están listos para ser usados con las modificaciones de la base de datos.

Esto simularía un cambio a nivel de código fuente de java más un cambio a nivel de base de datos. Algo bastante típico dentro de un proyecto.


## Rollback

A continuación se verá un ejemplo de cómo utilizar la capacidad de ***rollback*** de Liquibase. Para esto se agregará dos tablas y un registro con error de tipo de dato para así generar un rollback.

Es sumamente importante hacer notas que el rollback de Liquibase no es una tarea automática de deshacer una transacción tal cual estamos acostumbrados a ver en una base de datos, sino más bien es un conjunto de tareas descritas una por una, en caso de error.

En este documento sólo se verá uno de los tipos de rollback (durante el meetup se vieron dos caminos), que es la de utilizar los tag de rollback definidos en cada changeSet.

Los cambios a realizar se encuentran en el archivo ***changelog-002.xml*** ubicado en ***src/main/resources/liquibase***. En este archivo podemos ver que hay definidos tres changeSets, donde el primero crea una tabla llamada ***prueba_rollback1***

```xml
	<!-- Change set for Rollback -->
	<changeSet author="harenas" id="20170427184101">
		<comment>Agrega tabla de prueba 1</comment>
		<createTable tableName="prueba_rollback1">
			<column name="id" type="int" />
		</createTable>
		<rollback>
			<dropTable tableName="prueba_rollback1" />
		</rollback>
	</changeSet>
```

El segundo changeSet crea la tabla ***prueba_rollback2***
```xml
	<changeSet author="harenas" id="20170427184102">
		<comment>Agrega tabla de prueba 2</comment>
		<createTable tableName="prueba_rollback2">
			<column name="id" type="int" />
		</createTable>
		<rollback>
			<dropTable tableName="prueba_rollback2" />
		</rollback>
	</changeSet>
```

El tercer changeSet, en este caso, será el que genere un error deliberado para ver cómo las tablas creadas anteriormente será eliminadas del schema. En este changeSet se indica que ante un error (rollback) se ejecuten nos las actividades de rollback definidas en los changeSets ***20170427184101*** y ***20170427184102*** definidos anteriormente en el mismo archivo.

```xml
	<changeSet author="harenas" id="20170427184103">
		<comment>Inserta un registro completo, con error de tipo de dato</comment>
		<insert tableName="empleados">
			<column name="rut" value="ABC" />
			<column name="dv" value="1" />
			<column name="nombre" value="Ricky" />
			<column name="apellido" value="Lakes" />
			<column name="email" value="rlakes@arkhotech.com" />
			<column name="address" value="Moneda S/N, Santiago, Chile" />
		</insert>
		<rollback changeSetId="20170427184101" changeSetAuthor="harenas"/>
		<rollback changeSetId="20170427184102" changeSetAuthor="harenas"/>	
	</changeSet>
```



## Generar modificación

Finalmente se ejecuta el comando liquibase con el contenido del archivo ***changelog-002.xml***

```console
liquibase --driver=com.mysql.jdbc.Driver \
     --classpath=/Users/harenas/Desarrollo/mysql-connector-java-5.1.41-bin.jar \
     --changeLogFile=src/main/resources/liquibase/changelog-002.xml \
     --url="jdbc:mysql://localhost:3306/liquibase-ejemplo-1" \
     --username=liquibase \
     --password=liquibase \
     migrate
```



## Licencia

Siéntanse en libertad de ejecutar, estudiar, compartir y modificar el código sin necesidad de autorización ni aviso al autor.
