import com.reports.ReportsDownloader;
import com.reports.api.client.ReportingApiClient;
import com.reports.api.client.SlowReportingApiClient;
import com.reports.rest.Application;
import com.reports.storage.LocalFileReportStorageProvider;
import com.reports.storage.ReportStorageProvider;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class ReportsDownloaderTests {
  String reportsTestLocation = "C:\\tmp_test\\reports";

  @Test
  public void downloadReportTest()  {
    String reportName = "report_1";
    ReportingApiClient reportingApiClient = new SlowReportingApiClient();
    ReportingApiClient.Report report= reportingApiClient.getReport(reportName);
    assertNotNull(report);
    assertEquals(reportName, report.getName());
    assertNotNull(report.getContent());
    assertFalse(report.getContent().isEmpty());
  }

  @Test
  public void localFileStoreReportTest() throws IOException {
    ReportingApiClient.Report report= new ReportingApiClient.Report("Test Name","Test Content");
    ReportStorageProvider provider = new LocalFileReportStorageProvider(reportsTestLocation);
    provider.storeReport(report);

    File reportFile = new File(reportsTestLocation+ "\\" + report.getName() + ".txt") ;
    assertTrue(reportFile.exists());

    File reportsDirectory = new File(reportsTestLocation);

    FileUtils.deleteDirectory(reportsDirectory);
    assertFalse(reportsDirectory.exists());
  }

  @Test
  public void dowтnloadAndStore10Reports() throws IOException {
    int reportsCount = 10;
    List<String> reportNames  = new ArrayList<>();;
    IntStream.range(0, reportsCount)
        .forEach(
            reportIndex -> reportNames.add("Report_" + (reportIndex + 1))
        );

    ReportsDownloader reportsDownloader = new ReportsDownloader(new LocalFileReportStorageProvider(reportsTestLocation));
    List<String> storedReports = reportsDownloader.downloadAndStoreReports(reportNames);
    File reportsDirectory = new File(reportsTestLocation);
    List<File> listOfFiles = Arrays.asList(reportsDirectory.listFiles());

    assertEquals(reportsCount, storedReports.size());
    assertEquals(reportsCount, listOfFiles.size());

    boolean allReportNamesAreCorrect = true;
    for(File file:listOfFiles){
      allReportNamesAreCorrect = allReportNamesAreCorrect & reportNames.stream().
          filter(reportName -> reportName.equals(FilenameUtils.removeExtension(file.getName())))
          .findFirst()
          .isPresent();
    }
    assertTrue(allReportNamesAreCorrect);

    FileUtils.deleteDirectory(reportsDirectory);
    assertFalse(reportsDirectory.exists());
  }
}
