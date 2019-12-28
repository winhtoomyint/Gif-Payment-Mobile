package com.example.paybill.commitsavoids;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommitAvoidInterface {
    @Headers(
            {       "X-KillBill-ApiKey:ApexPayMentHinThar",
                    "X-KillBill-ApiSecret:app10000",
                    "Authorization:Basic YWRtaW46cGFzc3dvcmQ=",
                    "X-KillBill-CreatedBy:admin",
            })

    @PUT("1.0/kb/invoices/{invoiceId}/commitInvoice")
    Call<Void> commitInvoice(@Path("invoiceId") String invoiceId);

    @PUT("1.0/kb/invoices/{invoiceId}/voidInvoice")
    Call<Void> avoidInvoice(@Path("invoiceId") String invoiceId);
}
