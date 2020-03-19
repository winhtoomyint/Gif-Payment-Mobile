package com.example.paybill.ChargePhp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InvoiceInterface {
    @Headers(
            {       "X-KillBill-ApiKey:hELL-0xxxx-12345",
                    "X-KillBill-ApiSecret:100-200-300-400-500",
                    "Authorization:Basic YWRtaW46cGFzc3dvcmQ=",
                    "Content-Type:application/json",
                    "Accept:application/json",
                    "X-KillBill-CreatedBy:admin",
                    "X-KillBill-Comment:Test Comment",
                    "X-KillBill-Reason:Test Reason"
            })

    @GET("1.0/kb/invoices/{invoiceId}")
    Call<InvoiceForNumber> createCharge(@Path("invoiceId") String invoiceId,
                                              @Query("withChildrenItems") boolean withChildrenItems
                                    );

}
