package com.metronome.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class PermissionUtil extends Activity{
    //static PermissionUtil permissionUtil;
    private Activity activity;

    static public byte PERMISSION_CODE_RECORD_AUDIO = 0x00000001;
    static public byte PERMISSION_CODE_WRITE_EXTERNAL_STORAGE = 0x00000010;

    public PermissionUtil(){

    }
    public PermissionUtil(Activity activity){
        this.activity = activity;
    }
/*
    static public PermissionUtil getInstance(Activity activity){
        if(permissionUtil != null) return permissionUtil;
        else{
            permissionUtil=new PermissionUtil();
            return permissionUtil;
        }
    }
*/
    public boolean checkPermission(String permission,int permissionCode){
        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);

        Log.d("test","permission :"+permissionCheck);
        //RECORD_AUDIO 퍼미션없을경우 퍼미션 요청
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            //권한취소이력확인
            if(activity.shouldShowRequestPermissionRationale(permission)){
                activity.requestPermissions(new String[]{permission},permissionCode);
            }else{
                //권한요청
                activity.requestPermissions(new String[]{permission},permissionCode);
            }
        }else{
            return true;
        }

        return false;
    }


}
