Sample reports downloader application.
Application downloads 500 reports in parallel mode and store it on the disk.
1. Make sure that maven is installed and configured.<br />
Open reports-downloader root folder

2. Run the command: mvn spring-boot:run<br />
Wait until appliaction is up

3. Open link http://localhost:8080/download_reports<br />
Wait until reports are downloaded. <br />
(In the project reports are downloading in parallel using 100 threads)
