/*|~^~|Copyright (c) 2008-2015, Massachusetts Institute of Technology (MIT)
 |~^~|All rights reserved.
 |~^~|
 |~^~|Redistribution and use in source and binary forms, with or without
 |~^~|modification, are permitted provided that the following conditions are met:
 |~^~|
 |~^~|-1. Redistributions of source code must retain the above copyright notice, this
 |~^~|ist of conditions and the following disclaimer.
 |~^~|
 |~^~|-2. Redistributions in binary form must reproduce the above copyright notice,
 |~^~|this list of conditions and the following disclaimer in the documentation
 |~^~|and/or other materials provided with the distribution.
 |~^~|
 |~^~|-3. Neither the name of the copyright holder nor the names of its contributors
 |~^~|may be used to endorse or promote products derived from this software without
 |~^~|specific prior written permission.
 |~^~|
 |~^~|THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 |~^~|AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 |~^~|IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 |~^~|DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 |~^~|FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 |~^~|DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 |~^~|SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 |~^~|CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 |~^~|OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 |~^~|OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\*/
/**
 *
 */
package edu.mit.ll.phinics.android.api.handlers;

import org.apache.http.Header;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.mit.ll.phinics.android.api.DataManager;
import edu.mit.ll.phinics.android.api.RestClient;

public class FieldReportResponseHandler extends AsyncHttpResponseHandler {
	private DataManager mDataManager;
	private long mReportId;
	
	public FieldReportResponseHandler(DataManager dataManager, long reportId) {
		mReportId = reportId;
		mDataManager = dataManager;
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		String content = (responseBody != null) ? new String(responseBody) : "";
		
		Log.e("PhinicsRest", "Success to post Field Report information");
		Log.e("PhinicsRest", "Deleting: " + mReportId + " success: " + mDataManager.deleteFieldReportStoreAndForward(mReportId));
		mDataManager.addPersonalHistory("Field Report successfully sent: " + content + "\n");
		mDataManager.requestFieldReportRepeating(mDataManager.getIncidentDataRate(), true);
		
		RestClient.setSendingFieldReports(false);
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		Log.e("PhinicsRest", "Failed to post Field Report information: " + error.getMessage());

		RestClient.setSendingFieldReports(false);
	}

}