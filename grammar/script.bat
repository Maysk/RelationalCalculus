	
javac visitors/*.java trcQueryElements/*.java trcGrammar/*.java
java trcGrammar.TrcGrammar < teste

cd visitors
del *.class
cd ..
cd trcQueryElements
del *.class
cd ..
cd trcGrammar
del *.class
cd ..
pause