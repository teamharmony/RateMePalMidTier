package prj.resources.mgmt.domain;

public class DataRequest {
	private int requestId;
	private String requestName;
	private int friendCreated = 0;
	private long creationTime;
	
	private int detailId;
	private Parameter[] paramIds;
	private User[] friends;
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public String getRequestName() {
		return requestName;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public int getFriendCreated() {
		return friendCreated;
	}
	public void setFriendCreated(int friendCreated) {
		this.friendCreated = friendCreated;
	}
	public long getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	public int getDetailId() {
		return detailId;
	}
	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}
	public Parameter[] getParamIds() {
		return paramIds;
	}
	public void setParamIds(Parameter[] paramIds) {
		this.paramIds = paramIds;
	}
	public User[] getFriends() {
		return friends;
	}
	public void setFriends(User[] friends) {
		this.friends = friends;
	}
	
	
	

	
}
