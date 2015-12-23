package me.wztc.api.response;

import org.json.JSONObject;

import me.wztc.api.parse.ParseCommon;

/**
 * 解析response基本类，只解析status，请不要做任何修改
 * 如果需要解析其他数据，请添加其他Response
 */
public final class BaseResponse extends APIResponse {

    private String status;

    public BaseResponse(JSONObject json) {
        status = ParseCommon.parseCommon(json);
    }

    public String getStatus() {
        return status;
    }
}
