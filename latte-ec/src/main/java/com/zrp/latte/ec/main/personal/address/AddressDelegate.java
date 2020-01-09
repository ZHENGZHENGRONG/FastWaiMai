package com.zrp.latte.ec.main.personal.address;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.latte.latte_ec.R;
import com.example.latte.latte_ec.R2;
import com.joanzapata.iconify.widget.IconTextView;
import com.zrp.latte.delegates.LatteDelegate;
import com.zrp.latte.net.RestClient;
import com.zrp.latte.net.callback.ISuccess;
import com.zrp.latte.ui.recycler.DataConverter;
import com.zrp.latte.ui.recycler.MultipleItemEntity;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressDelegate extends LatteDelegate implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R2.id.tv_address_add)
    TextView mTvAddressAdd;
    @BindView(R2.id.rv_address_view)
    RecyclerView mRvAddress;


    private AddressAdapter mAdapter = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_address;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View view) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRvAddress.setLayoutManager(manager);
        RestClient.builder()
                .url("api/address")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final DataConverter converter =
                                new AddressDataConverter().setJsonData(response);
                        mAdapter = new AddressAdapter(converter.convert(), AddressDelegate.this);
                        mAdapter.setOnItemClickListener(AddressDelegate.this);
                        mRvAddress.setAdapter(mAdapter);

                    }
                })
                .build()
                .get();
    }


    @OnClick(R2.id.iv_address_backarrow)
    public void onViewClickedReturn() {
        getSupportDelegate().pop();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleItemEntity entity = (MultipleItemEntity) adapter.getData().get(position);
        final String name = entity.getField(AddressItemFields.SURNAME);
        String surname = name.substring(0, 1);
        final String phone = entity.getField(AddressItemFields.PHONE);
        final Boolean isDefault = entity.getField(AddressItemFields.DEFAULT);
        final String gender = entity.getField(AddressItemFields.GENDER);
        final Bundle bundle = new Bundle();
        bundle.putString("address", surname + gender+"("+phone+")");
        getSupportDelegate().setFragmentResult(RESULT_OK, bundle);
        getSupportDelegate().onDestroy();
        getSupportDelegate().pop();
    }
}
