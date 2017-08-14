package com.reports;

import com.reports.api.client.ReportingApiClient;
import com.reports.api.client.SlowReportingApiClient;
import com.reports.config.ReportsConfiguration;
import com.reports.storage.LocalFileReportStorageProvider;
import com.reports.storage.ReportStorageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by vserdun on 8/9/2017.
 * Reports downloader solution implementation.
 * Downloads reports using api client {@link ReportingApiClient} and stores it to specified location
 * according {@link ReportStorageProvider}
 */

public class ReportsDownloader {

  private static final Logger log = LogManager.getLogger(ReportsDownloader.class);

  ReportStorageProvider reportStorageProvider;
  /**
   *  Constructor which use {@link ReportStorageProvider} as place for reports storage
  */
  public ReportsDownloader(ReportStorageProvider reportStorageProvider) {
    this.reportStorageProvider = reportStorageProvider;
  }

  /**
   * Downloads and stores reports in parallel mode to specified location
   * @see ReportStorageProvider
   * @param reportNames list of report names to download
   */
  public void downloadAndStoreReports(List<String> reportNames) {
    ExecutorService taskExecutor = Executors.newFixedThreadPool(100);

    CompletionService<ReportingApiClient.Report> taskCompletionService =
        new ExecutorCompletionService<>(taskExecutor);

    reportNames.forEach(
        (String reportName) -> taskCompletionService.submit(new DownloadReportTask(reportName))
    );

    taskExecutor.shutdown();

    try {
      while (!taskExecutor.isTerminated()) {
          final Future<ReportingApiClient.Report> future = taskCompletionService.take();
          final ReportingApiClient.Report report = future.get();
          reportStorageProvider.storeReport(report);
        }
      } catch (ExecutionException e) {
        log.error("Error while downloading report ", e);
      } catch (InterruptedException e) {
        log.error("InterruptedException", e);
      }
  }

  /**
   * Task for reports downloading in parallel mode
   */
  private static class DownloadReportTask implements Callable<ReportingApiClient.Report> {

    private ReportingApiClient slowClient = new SlowReportingApiClient();
    private String reportName;

    public DownloadReportTask(String reportName) {
      this.reportName = reportName;
    }

    /**
     * Downloads report using {@link ReportingApiClient}
     * @return downloaded report
     * @throws Exception
     */
    @Override
    public ReportingApiClient.Report call() throws Exception {
      return slowClient.getReport(reportName);
    }
  }
}

