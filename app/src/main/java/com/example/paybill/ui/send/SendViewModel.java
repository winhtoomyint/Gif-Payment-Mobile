package com.example.paybill.ui.send;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendViewModel extends ViewModel {
    private final MutableLiveData<String> mAccountId = new MutableLiveData<>();
    private final MutableLiveData<String> mAmount = new MutableLiveData<>();
    private final MutableLiveData<String> mDescription = new MutableLiveData<>();

    public void setAccountId(String accountId){mAccountId.setValue(accountId);}
    public void setAmount(String amount){mAmount.setValue(amount);}
    public void setDescription(String description){mDescription.setValue(description);}

    public LiveData<String> getAccountId(){return mAccountId;}
    public LiveData<String> getAmount(){return mAmount;}
    public LiveData<String> getDescription(){return mDescription;}
}