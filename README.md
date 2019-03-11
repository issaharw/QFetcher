# QFetcher
Explanations on how to run the server:
Pre-requisites:
1. Have java installed on your machine.
2. Have maven installed on your machine ( download and unpack it, and that's it: http://apache.spd.co.il/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.zip)

Steps to run the server:
1. Download the zip file and unpack it
2. Change dir into the unpacked folder.
3. Build the server. Execute: 
     `<maven_dir>/bin/mvn clean package`
     The resulting jar will be inside the "/target" folder
4. Run the server. Execute:   
     `java -jar target/QFetcher-1.0.0-SNAPSHOT.jar src/main/resources/config.yaml <sources file>`
     
  you can use the supplied manifest.dat
  
     `java -jar target/QFetcher-1.0.0-SNAPSHOT.jar src/main/resources/config.yaml src/main/resources/manifest.dat`

5. The server starts on port 8080, so you can see the UI in

          http://localhost:8080/qfetcher.html
6. The configuration of the timeout is inside the config.yaml. You can reduce it to 3000, and that way the image source would time out.
7. I added to the manifest.dat an unknown source type, and you can see in the logs that it logs the error, but doesn't fail the other sources.
8. I added comments throughout the code explaining what I did.
