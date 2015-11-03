package prj.resources.mgmt.domain;

public class Feedback {
	
	private String fromUserName;
	private String subject;
	private String message;
	
	public String getFromUserName() {
		return fromUserName;
	}
	public String getSubject() {
		return subject;
	}
	public String getMessage() {
		return message;
	}
	
	public Feedback(FeedbackBuilder builder) {
		this.fromUserName = builder.fromUserName;
		this.subject = builder.subject;
		this.message = builder.message;
	}
	
	public static class FeedbackBuilder {
		private String fromUserName;
		private String subject;
		private String message;
		
		
		public FeedbackBuilder fromUserName(String value) {
			this.fromUserName = value;
			return this;
		}
		
		public FeedbackBuilder subject(String value) {
			this.subject = value;
			return this;
		}
		
		public FeedbackBuilder message(String value) {
			this.message = value;
			return this;
		}
		
		
		
		public Feedback build() {
			return new Feedback(this);
		}
		
	}
	
	
}
