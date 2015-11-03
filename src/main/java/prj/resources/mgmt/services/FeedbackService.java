package prj.resources.mgmt.services;

import prj.resources.exception.ResourceError;
import prj.resources.mgmt.domain.Feedback;

public interface FeedbackService {
	public void createMessage(Feedback feedback) throws ResourceError;
}
