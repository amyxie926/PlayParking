package me.wztc.api.request;

import java.util.HashMap;

import me.wztc.api.CallAPI;
import me.wztc.api.HttpMethod;
import me.wztc.api.response.APIResponse;
import me.wztc.util.DeviceInfo;

public abstract class ApiRequest<RESPONSE extends APIResponse> extends CallAPI<RESPONSE> {
    @Override
    protected String getServiceSchema() {
        return DeviceInfo.getDev().getServiceSchema();
    }

    @Override
    protected String getServiceHost() {
        return DeviceInfo.getDev().getServiceHost();
    }

    @Override
    protected String getServiceHostPath() {
        return DeviceInfo.getDev().getServiceBasePath();
    }

    @Override
    protected int getServicePort() {
        return DeviceInfo.getDev().getServicePort();
    }

    @Override
    protected int getServiceTimeout() {
        return DeviceInfo.getDev().getServiceTimeout();
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.post;
    }

    @Override
    protected HashMap<String, String> getHeaders() {
        return null;
    }

//    @SuppressWarnings("unused")
//    private String getSortString(ArrayList<String> list) {
//
//        Collections.sort(list, new Comparator<String>() {
//            @Override
//            public int compare(String lhs, String rhs) {
//                return lhs.compareTo(rhs);
//            }
//        });
//
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < list.size(); i++) {
//            builder.append(list.get(i));
//        }
//
//        return builder.toString();
//    }
//
//    // MD5加密，32位
//    @SuppressWarnings("unused")
//    private String getMD5(String str) {
//        MessageDigest md5 = null;
//        try {
//            md5 = MessageDigest.getInstance("MD5");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return "";
//        }
//
//        char[] charArray = str.toCharArray();
//        byte[] byteArray = new byte[charArray.length];
//
//        for (int i = 0; i < charArray.length; i++) {
//            byteArray[i] = (byte) charArray[i];
//        }
//        byte[] md5Bytes = md5.digest(byteArray);
//
//        StringBuffer hexValue = new StringBuffer();
//        for (int i = 0; i < md5Bytes.length; i++) {
//            int val = ((int) md5Bytes[i]) & 0xff;
//            if (val < 16) {
//                hexValue.append("0");
//            }
//            hexValue.append(Integer.toHexString(val));
//        }
//        return hexValue.toString();
//    }
}
