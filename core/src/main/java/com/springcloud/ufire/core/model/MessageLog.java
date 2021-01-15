package com.springcloud.ufire.core.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table broker_message_log
 *
 * @mbg.generated do_not_delete_during_merge
 */
public class MessageLog implements Serializable {
    /**
     * Database Column Remarks:
     *   消息唯一ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column broker_message_log.message_id
     *
     * @mbg.generated
     */
    private String messageId;

    /**
     * Database Column Remarks:
     *   消息内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column broker_message_log.message
     *
     * @mbg.generated
     */
    private String message;

    /**
     * Database Column Remarks:
     *   重试次数
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column broker_message_log.try_count
     *
     * @mbg.generated
     */
    private Integer tryCount;

    /**
     * Database Column Remarks:
     *   消息投递状态 0投递中，1投递成功，2投递失败
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column broker_message_log.status
     *
     * @mbg.generated
     */
    private String status;

    /**
     * Database Column Remarks:
     *   下一次重试时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column broker_message_log.next_retry
     *
     * @mbg.generated
     */
    private LocalDateTime nextRetry;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column broker_message_log.create_time
     *
     * @mbg.generated
     */
    private LocalDateTime createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column broker_message_log.update_time
     *
     * @mbg.generated
     */
    private LocalDateTime updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table broker_message_log
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column broker_message_log.message_id
     *
     * @return the value of broker_message_log.message_id
     *
     * @mbg.generated
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column broker_message_log.message_id
     *
     * @param messageId the value for broker_message_log.message_id
     *
     * @mbg.generated
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column broker_message_log.message
     *
     * @return the value of broker_message_log.message
     *
     * @mbg.generated
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column broker_message_log.message
     *
     * @param message the value for broker_message_log.message
     *
     * @mbg.generated
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column broker_message_log.try_count
     *
     * @return the value of broker_message_log.try_count
     *
     * @mbg.generated
     */
    public Integer getTryCount() {
        return tryCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column broker_message_log.try_count
     *
     * @param tryCount the value for broker_message_log.try_count
     *
     * @mbg.generated
     */
    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column broker_message_log.status
     *
     * @return the value of broker_message_log.status
     *
     * @mbg.generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column broker_message_log.status
     *
     * @param status the value for broker_message_log.status
     *
     * @mbg.generated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column broker_message_log.next_retry
     *
     * @return the value of broker_message_log.next_retry
     *
     * @mbg.generated
     */
    public LocalDateTime getNextRetry() {
        return nextRetry;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column broker_message_log.next_retry
     *
     * @param nextRetry the value for broker_message_log.next_retry
     *
     * @mbg.generated
     */
    public void setNextRetry(LocalDateTime nextRetry) {
        this.nextRetry = nextRetry;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column broker_message_log.create_time
     *
     * @return the value of broker_message_log.create_time
     *
     * @mbg.generated
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column broker_message_log.create_time
     *
     * @param createTime the value for broker_message_log.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column broker_message_log.update_time
     *
     * @return the value of broker_message_log.update_time
     *
     * @mbg.generated
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column broker_message_log.update_time
     *
     * @param updateTime the value for broker_message_log.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table broker_message_log
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", messageId=").append(messageId);
        sb.append(", message=").append(message);
        sb.append(", tryCount=").append(tryCount);
        sb.append(", status=").append(status);
        sb.append(", nextRetry=").append(nextRetry);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}