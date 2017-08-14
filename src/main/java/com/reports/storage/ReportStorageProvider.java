package com.reports.storage;

import com.reports.api.client.ReportingApiClient;

/**
 * Created by vserdun on 8/11/2017.
 *
 * Interface which allows to store report to specified location
 */
public interface ReportStorageProvider {
  /**
   * Stores report to specified location
   *
   * @param report returned by {@link ReportingApiClient}
   * @see com.reports.api.client.ReportingApiClient.Report
   * @return true on success or false failure
   */
  public boolean storeReport(ReportingApiClient.Report report);
}
