package prj.resources.mgmt.domain;

public class Message {
	
	private int id;
	private String toUserName;
	private String fromUserName;
	private String name;
	private String subject;
	private String message;
	private int fromStatus;
	private int toStatus;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getToUserName() {
		return toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public String getSubject() {
		return subject;
	}
	public String getMessage() {
		return message;
	}
	public int getFromStatus() {
		return fromStatus;
	}
	public int getToStatus() {
		return toStatus;
	}
	
	public Message(MessageBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.toUserName = builder.toUserName;
		this.fromUserName = builder.fromUserName;
		this.subject = builder.subject;
		this.message = builder.message;
		this.fromStatus = builder.fromStatus;
		this.toStatus = builder.toStatus;
	}
	
	public static class MessageBuilder {
		private int id;
		private String toUserName;
		private String fromUserName;
		private String name;
		private String subject;
		private String message;
		private int fromStatus;
		private int toStatus;
		
		public MessageBuilder id(int value) {
			this.id = value;
			return this;
		}

		public MessageBuilder name(String value) {
			this.name = value;
			return this;
		}
		
		public MessageBuilder toUserName(String value) {
			this.toUserName = value;
			return this;
		}
		
		public MessageBuilder fromUserName(String value) {
			this.fromUserName = value;
			return this;
		}
		
		public MessageBuilder subject(String value) {
			this.subject = value;
			return this;
		}
		
		public MessageBuilder message(String value) {
			this.message = value;
			return this;
		}
		
		
		public MessageBuilder fromStatus(int value) {
			this.fromStatus = value;
			return this;
		}
		
		public MessageBuilder toStatus(int value) {
			this.toStatus = value;
			return this;
		}
		
		
		public Message build() {
			return new Message(this);
		}
		
	}
	
	
}
