	
javac trcToSql/visitors/*.java trcToSql/trcQueryElements/*.java trcToSql/trcGrammar/*.java trcToSql/testModule/*.java
java trcToSql.trcGrammar.TrcGrammar < teste
java trcToSql.testModule.Main < teste

cd trcToSql
cd visitors
del *.class
cd ..
cd trcQueryElements
del *.class
cd ..
cd trcGrammar
del *.class
cd ..
cd testModule
del *.class
cd ..
cd ..
pause