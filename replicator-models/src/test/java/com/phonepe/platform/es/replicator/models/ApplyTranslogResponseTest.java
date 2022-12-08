package com.phonepe.platform.es.replicator.models;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentParserUtils;
import org.elasticsearch.common.xcontent.json.JsonXContentParser;

import java.io.IOException;


@Slf4j
class ApplyTranslogResponseTest {


    void fromXContent() throws IOException {
        ApplyTranslogResponse response = ApplyTranslogResponse.builder()
                .errorCode(ApplyTranslogResponse.ErrorCode.MAPPING_UPDATE_REQUIRED)
                .success(false)
                .errorMessage("Need mappig update")
                .sequence(100)
                .build();

        XContentBuilder builder = XContentFactory.jsonBuilder();
        response.toXContent(builder, null);
        String content = Strings.toString(builder);
        log.info("Content: {}", content);
    }
}