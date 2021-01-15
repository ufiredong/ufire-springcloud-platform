package com.springcloud.ufire.core.mapper;

import com.springcloud.ufire.core.model.MessageLog;
import com.springcloud.ufire.core.model.MessageLogExample;
import mboog.support.mapper.ReadMapper;
import mboog.support.mapper.UpsertMapper;
import mboog.support.mapper.WriteMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageLogMapper extends UpsertMapper<String, MessageLog, MessageLogExample>, ReadMapper<String, MessageLog, MessageLogExample>, WriteMapper<String, MessageLog, MessageLogExample> {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table broker_message_log
     *
     * @mbg.generated
     */
    default MessageLog selectByPrimaryKeyWithColumns(String primaryKey, boolean include, MessageLogExample.C... cs) {
        MessageLogExample example = new MessageLogExample();
        if (include) {
                example.includeColumns(cs);
        } else {
                example.excludeColumns(cs);
        }
        example.or(cri -> cri
               .andMessageIdEqualTo(primaryKey)
        );
        return selectByExampleSingleResult(example);
    }
}