package prj.resources.mgmt.domain;

import prj.resources.mgmt.domain.Message.MessageBuilder;

public class Meeting {

	private int id;
	private String toUserName;
	private String fromUserName;
	private String name;
	private String agenda;
	private String details;
	private long datetime;
	private String venue;
	private int status;
	
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
	public String getAgenda() {
		return agenda;
	}
	public String getDetails() {
		return details;
	}
	public long getDatetime() {
		return datetime;
	}
	public String getVenue() {
		return venue;
	}
	public int getStatus() {
		return status;
	}
	
	public Meeting(MeetingBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.toUserName = builder.toUserName;
		this.fromUserName = builder.fromUserName;
		this.agenda = builder.agenda;
		this.details = builder.details;
		this.venue = builder.venue;
		this.status = builder.status;
		this.datetime = builder.datetime;
	}
	
	public static class MeetingBuilder {
		private int id;
		private String toUserName;
		private String fromUserName;
		private String name;
		private String agenda;
		private String details;
		private long datetime;
		private String venue;
		private int status;
		
		public MeetingBuilder id(int value) {
			this.id = value;
			return this;
		}
		
		public MeetingBuilder name(String value) {
			this.name = value;
			return this;
		}
		
		public MeetingBuilder toUserName(String value) {
			this.toUserName = value;
			return this;
		}
		
		public MeetingBuilder fromUserName(String value) {
			this.fromUserName = value;
			return this;
		}
		
		public MeetingBuilder agenda(String value) {
			this.agenda = value;
			return this;
		}
		
		public MeetingBuilder details(String value) {
			this.details = value;
			return this;
		}
		
		public MeetingBuilder dateTime(long value) {
			this.datetime = value;
			return this;
		}
		
		public MeetingBuilder venue(String value) {
			this.venue = value;
			return this;
		}
		
		public MeetingBuilder status(int value) {
			this.status = value;
			return this;
		}
		
		public Meeting build() {
			return new Meeting(this);
		}
		
	}
	
	
}
