package com.imooc.code.azkaban.response;

import lombok.Data;

@Data
public class FetchExecJobLogs extends BaseResponse {
    private String data;
    private Integer offset;
    private Integer length;
}
