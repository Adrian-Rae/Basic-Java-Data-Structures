Tester.class:
	javac *.java -cp .;\lib\mysql-connector-java-5.1.48\mysql-connector-java-5.1.48-bin.jar;.;
	
run: Tester.class
	java Tester -cp .;\lib\mysql-connector-java-5.1.48\mysql-connector-java-5.1.48-bin.jar;.;
	
clean:
	rm *.class
	
tar:
	tar -cvz -f A2.zip makefile *.java 