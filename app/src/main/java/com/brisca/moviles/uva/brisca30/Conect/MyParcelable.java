package com.brisca.moviles.uva.brisca30.Conect;

import android.os.Parcel;
import android.os.Parcelable;

public class MyParcelable implements Parcelable {
        private int mData;
        private NsdHelper mNsdHelper;

    public NsdHelper getmNsdHelper() {
        return mNsdHelper;
    }

    public void setmNsdHelper(NsdHelper mNsdHelper) {
        this.mNsdHelper = mNsdHelper;
    }

    public ChatConnection getmConnection() {
        return mConnection;
    }
    public MyParcelable(){}

    public void setmConnection(ChatConnection mConnection) {
        this.mConnection = mConnection;
    }

    private ChatConnection mConnection;
        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(mData);
        }

        public static final Parcelable.Creator<MyParcelable> CREATOR
                = new Parcelable.Creator<MyParcelable>() {
            public MyParcelable createFromParcel(Parcel in) {
                return new MyParcelable(in);
            }

            public MyParcelable[] newArray(int size) {
                return new MyParcelable[size];
            }
        };

        private MyParcelable(Parcel in) {
            mData = in.readInt();
        }
    }