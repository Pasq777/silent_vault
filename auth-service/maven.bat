@ECHO OFF

set JAVA_HOME=C:\Users\pasqualino.mandarini\Desktop\Formazione\PersonalProjects\Silent-vault\silentVault_config\jdk-21.0.7
set M2_HOME=C:\Users\pasqualino.mandarini\Desktop\Formazione\PersonalProjects\Silent-vault\silentVault_config\apache-maven-3.9.9
set M2_REPO=C:\Users\pasqualino.mandarini\Desktop\Formazione\PersonalProjects\Silent-vault\silentVault_config\m2custom

set LOCAL_REPO=%M2_REPO%
set MAVEN_OPTS=%MAVEN_OPTS%

set MAVEN=%M2_HOME%\bin\mvn -Dmaven.test.skip=true

set argv=%1
set clean=false
set install=false
set deploy=false
set release=false
set releaseProd=false

if not x%argv:c=%==x%argv% (set clean=true)
if not x%argv:i=%==x%argv% (set install=true)
if not x%argv:d=%==x%argv% (set deploy=true)

if "%clean%"=="true" (

	echo *****************************
	echo [] Command CLEAN
	echo *****************************

	echo Deploy: false
	if "%2"=="" (
		call %MAVEN% clean
	) else (
		call %MAVEN% clean -pl %2 --also-make
	)
	
)
if "%install%"=="true" (

	echo *****************************
	echo [] Command INSTALL
	echo *****************************

	if "%2"=="" (
		call %MAVEN% install
	) else (
		call %MAVEN% install -pl %2 --also-make
	)
)