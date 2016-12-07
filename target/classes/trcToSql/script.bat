	
javac visitors/*.java trcQueryElements/*.java trcGrammar/*.java main/*.java
java trcGrammar.TrcGrammar < teste
java main.Main < teste

cd visitors
del *.class
cd ..
cd trcQueryElements
del *.class
cd ..
cd trcGrammar
del *.class
cd ..
cd main
del *.class
cd ..
pause