package uniwatson.booktrade.models;

import java.sql.Timestamp;

public class Message {
    private int serialNum;      // Serial_Num
    private Timestamp time;     // Time
    private String content;     // Content
    private String sendoutId;   // SendoutID
    private String receivedId;  // ReceivedID
    private Integer orderId;    // OrderID (nullable)

    public int getSerialNum() { return serialNum; }
    public void setSerialNum(int serialNum) { this.serialNum = serialNum; }
    public Timestamp getTime() { return time; }
    public void setTime(Timestamp time) { this.time = time; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getSendoutId() { return sendoutId; }
    public void setSendoutId(String sendoutId) { this.sendoutId = sendoutId; }
    public String getReceivedId() { return receivedId; }
    public void setReceivedId(String receivedId) { this.receivedId = receivedId; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
}
