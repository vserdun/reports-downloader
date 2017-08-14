package com.reports.rest.controller;

import com.reports.ReportsDownloader;
import com.reports.config.ReportsConfiguration;
import com.reports.storage.LocalFileReportStorageProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by vserdun on 8/13/2017.
 */
@RestController
@RequestMapping("/")
public class ReportsController {

  @RequestMapping("/download_reports")
  public String DownloadReports() {

    List<String> reportNames = ReportsConfiguration.generateSampleReportNames();
    String storageFilePath = ReportsConfiguration.getReportsDownloadFilePath();

    ReportsDownloader reportsDownloader = new ReportsDownloader(new LocalFileReportStorageProvider(storageFilePath));
    reportsDownloader.downloadAndStoreReports(reportNames);

    return new String(String.format("Reports with names %s were downloaded to folder %s",reportNames,storageFilePath));
  }
}
