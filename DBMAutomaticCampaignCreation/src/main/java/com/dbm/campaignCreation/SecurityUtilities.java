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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.doubleclickbidmanager.DoubleClickBidManager;
import com.google.common.collect.ImmutableSet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Utility class to handle OAuth and service creation
 */
public class SecurityUtilities {    
  // HTTP Transport object used for automatically refreshing access tokens.
  static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  // JSON factory used for parsing refresh token responses. 
  static final JacksonFactory JSON_FACTORY = new JacksonFactory();  

  /**
   * Be sure to specify the name of your application. If the application name is
   * {@code null} or blank, the application will log a warning. Suggested format
   * is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "GoogleDbm1";
  
  // Scopes for the OAuth token
  private static final Set<String> SCOPES = 
      ImmutableSet.of("https://www.googleapis.com/auth/doubleclickbidmanager"); 
  
  // Directory to store the OAuth Refresh Token if using User Credentials .
  private static final java.io.File DATA_STORE_DIR =
      new java.io.File(System.getProperty("user.home"), ".store/dbm_sample");

  private static FileDataStoreFactory dataStoreFactory;
  
  public static String token;
  
  public static Credential getUserCredential() throws Exception {
	  GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("F:\\GoogleAds-bd4350a329cf.json"));

	    // Update the credential object with appropriate scopes and impersonation info.
	    credential = new GoogleCredential.Builder()
	        .setTransport(credential.getTransport())
	        .setJsonFactory(credential.getJsonFactory())
	        .setServiceAccountId(credential.getServiceAccountId())
	        .setServiceAccountPrivateKey(credential.getServiceAccountPrivateKey())
	        .setServiceAccountScopes(SCOPES)
	        // Set the email of the user you are impersonating (this can be yourself).
	        .setServiceAccountUser("agrawal.saurabh167@gmail.com")
	        .build();

	    return credential;
    
    
  }
  
  private static HttpRequestInitializer setHttpTimeout(
      final HttpRequestInitializer requestInitializer) {
    return new HttpRequestInitializer() {
      @Override
      public void initialize(HttpRequest httpRequest) throws IOException {
        requestInitializer.initialize(httpRequest);
        httpRequest.setReadTimeout(3 * 60000);  // 3 minutes read timeout.       
      }
    };
  }

  public static DoubleClickBidManager getAPIService() throws Exception {
    DoubleClickBidManager bmService = new DoubleClickBidManager
        .Builder(HTTP_TRANSPORT, JSON_FACTORY, 
            setHttpTimeout(getUserCredential()))
        .setApplicationName(APPLICATION_NAME).build();
    return bmService;
  }
  
  public static NetHttpTransport getTransport() {
    return HTTP_TRANSPORT;
  }
}
