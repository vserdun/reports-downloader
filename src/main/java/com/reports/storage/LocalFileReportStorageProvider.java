package com.reports.storage;

import com.reports.api.client.ReportingApiClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by vserdun on 8/11/2017.
 * Implementation of {@link ReportStorageProvider}
 * Allows to store report to file on local system
 */
public class LocalFileReportStorageProvider implements ReportStorageProvider {
  private String filePath;
  private static final Logger log = LogManager.getLogger(LocalFileReportStorageProvider.class);
  /**
   * Stores report to file on local system
   *
   * @param report returned by {@link ReportingApiClient}
   * @see com.reports.api.client.ReportingApiClient.Report
   * @return true on success or false failure
   */
  @Override
  public boolean storeReport(ReportingApiClient.Report report) {
    File directory = new File(filePath);
    if (! directory.exists()){
      directory.mkdirs();
    }
    try(  PrintWriter out = new PrintWriter(filePath + "\\" + report.getName() + ".txt" )  ){
      out.println( report.getContent() );
      log.info(String.format("Report %s stored to folder %s",report.getName(),filePath));
      return true;
    } catch (FileNotFoundException e) {
      log.error("Failed to store report to local file", e);
    }
    return false;
  }
  /**
   * Constructor. Creates new instance of {@link LocalFileReportStorageProvider}
   *
   * @param filePath path where report will be stored
   */
  public LocalFileReportStorageProvider(String filePath) {
    this.filePath = filePath;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
}
