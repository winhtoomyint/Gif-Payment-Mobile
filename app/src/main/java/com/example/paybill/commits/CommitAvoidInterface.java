package com.example.paybill.commits;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommitAvoidInterface {
    @Headers(
            {       "X-KillBill-ApiKey:hELL-0xxxx-12345",
                    "X-KillBill-ApiSecret:100-200-300-400-500",
                    "Authorization:Basic YWRtaW46cGFzc3dvcmQ=",
                    "X-KillBill-CreatedBy:admin",
            })

    @PUT("1.0/kb/invoices/{invoiceId}/commitInvoice")
    Call<Void> commitInvoice(@Path("invoiceId") String invoiceId);

    @PUT("1.0/kb/invoices/{invoiceId}/voidInvoice")
    Call<Void> avoidInvoice(@Path("invoiceId") String invoiceId);
}
