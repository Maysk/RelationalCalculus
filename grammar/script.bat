cd trcGrammar
javacc TrcGrammar.jj

cd ..

javac trcQueryElements/*.java trcGrammar/*.java

cd trcQueryElements
del *.class
cd ..
cd trcGrammar
del *.class
cd ..
pause