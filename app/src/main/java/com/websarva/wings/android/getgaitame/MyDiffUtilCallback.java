package com.websarva.wings.android.getgaitame;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import java.util.ArrayList;

/**
 * Created by am on 2018/08/09.
 */

public class MyDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<gaitameDataBox> oldData, newData;

    public MyDiffUtilCallback(ArrayList<gaitameDataBox>oldPersons, ArrayList<gaitameDataBox> newPersons) {
        this.oldData = oldPersons;
        this.newData = newPersons;
    }
    @Override
    public int getOldListSize() {
        return oldData.size();
    }
    @Override
    public int getNewListSize() {
        return newData.size();
    }
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (
           oldData.get(oldItemPosition).getBid().equals(newData.get(newItemPosition).getBid()) &&
           oldData.get(oldItemPosition).getOpen().equals(newData.get(newItemPosition).getOpen()) &&
           oldData.get(oldItemPosition).getHigh().equals(newData.get(newItemPosition).getHigh()) &&
           oldData.get(oldItemPosition).getLow().equals(newData.get(newItemPosition).getLow()) &&
           oldData.get(oldItemPosition).getAsk().equals(newData.get(newItemPosition).getAsk())
        );
    }
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
    }
    @Nullable @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // some additional information
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
