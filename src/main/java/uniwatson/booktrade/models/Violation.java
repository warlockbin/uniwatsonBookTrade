package uniwatson.booktrade.models;

import java.sql.Timestamp;

public class Violation {
    private int serialNum;       // Serial_Num
    private Timestamp time;      // Time
    private String reason;       // Reason
    private String userId;       // User_ID
    private int managerId;       // Manager_ID

    public int getSerialNum() { return serialNum; }
    public void setSerialNum(int serialNum) { this.serialNum = serialNum; }
    public Timestamp getTime() { return time; }
    public void setTime(Timestamp time) { this.time = time; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getManagerId() { return managerId; }
    public void setManagerId(int managerId) { this.managerId = managerId; }
}
