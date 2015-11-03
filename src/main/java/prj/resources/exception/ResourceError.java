package prj.resources.exception;


public class ResourceError extends Exception{
	
	private final Exception underlyingCause;
	private String errorString;
	private int errorCode;

	public ResourceError(Exception e) {
		underlyingCause = e;
	}
	
	
	public Exception getCause() {
		return underlyingCause;
	}
	
	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	

}
