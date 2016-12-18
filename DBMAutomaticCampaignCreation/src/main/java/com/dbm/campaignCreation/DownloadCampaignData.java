/*
 * Copyright (c) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.dbm.campaignCreation;

import com.google.api.services.doubleclickbidmanager.DoubleClickBidManager;
import com.google.api.services.doubleclickbidmanager.DoubleClickBidManager.Sdf;
import com.google.api.services.doubleclickbidmanager.DoubleClickBidManager.Sdf.Download;
import com.google.api.services.doubleclickbidmanager.model.DownloadLineItemsRequest;
import com.google.api.services.doubleclickbidmanager.model.DownloadLineItemsResponse;
import com.google.api.services.doubleclickbidmanager.model.DownloadRequest;
import com.google.api.services.doubleclickbidmanager.model.DownloadResponse;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.Arrays;

/**
 * In DoubleClick Bid Manager, line items bid on impressions and deliver ads 
 * through exchanges and networks.  Once your line items are setup in the UI,
 * you can use the API to download a filtered list of line items to a csv file
 * which can then be modified and re-uploaded using UploadLineItems.
 */
public class DownloadCampaignData {
  public static void main(String[] args) throws Exception {
    // Get an authenticated connection to the API.
    DoubleClickBidManager service = SecurityUtilities.getAPIService();
   
   
    Sdf sdf = service.sdf();
    
  //  UploadRequest 
    
   
    DownloadRequest request = new DownloadRequest().setFilterType("ADVERTISER_ID").setFilterIds(Arrays.asList(932635L));
    	    
    
    DownloadResponse response = service.sdf().download(request).execute();
    		
    Download obj  = sdf.download(request);
  //  obj.setOauthToken(SecurityUtilities.token);
  
    response  = obj.execute();
    System.out.println(response.getLineItems());
    // Setup any filtering on the API request
   
  
    
    
    DownloadLineItemsRequest dliRequest = new DownloadLineItemsRequest().setFilterType("ADVERTISER_ID").setFilterIds(Arrays.asList(696834L));
    /* If your download requests times out you may need to filter to reduce the
     * number of items returned - the commented code below filters on 
     * ADVERTISER_ID, refer to the reference guide at 
     * https://developers.google.com/bid-manager/ for other options. 
     */    
      // .setFilterType("ADVERTISER_ID")
      // .setFilterIds(Arrays.asList(0L, 1L, 2L));

    // Call the API, getting the (filtered) list of line items.
   
  
    
    
    DownloadLineItemsResponse dliResponse =
        service.lineitems().downloadlineitems(dliRequest).execute();
    
    System.out.println(dliRequest.getFileSpec());
    
    File to = new File("line_items.csv");
    Files.write(dliResponse.getLineItems(), to, Charsets.UTF_8);
    System.out.println("Download complete.");
  }
}
