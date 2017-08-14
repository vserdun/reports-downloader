package com.reports.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by vserdun on 8/13/2017.
 * Basic configuration for reports downloader
 *
 */
public class ReportsConfiguration {
  private static final int REPORTS_DOWNLOAD_COUNT = 500;
  private static final String REPORTS_DOWNLOAD_FILE_PATH = "C:\\temp\\reports";;

  public static int getReportsDownloadCount() {
    return REPORTS_DOWNLOAD_COUNT;
  }

  public static String getReportsDownloadFilePath() {
    return REPORTS_DOWNLOAD_FILE_PATH;
  }

  /**
   * Sample method which generates report names in format report_1, ..., report_n
   * @return list of generated report names
   */
  public static List<String> generateSampleReportNames(){
    List<String> reportNames = new ArrayList<>();
    IntStream.range(0, ReportsConfiguration.getReportsDownloadCount()).
        forEach(
            reportIndex -> reportNames.add("Report_" + (reportIndex + 1))
        );

    return reportNames;
  }
}
