package prj.resources.exception; 

public	class ClientErrorInfo {
	
		public enum ErrorType{
			DATA_ACESS,
			GENERIC
		}

		public ClientErrorInfo(String info, ErrorType type) {
			this.error = info;
			this.type = type;
		}
		
		private String error;
		private ErrorType type;
		
			
		public ErrorType getType() {
			return type;
		}

		public void setType(ErrorType type) {
			this.type = type;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}
		
	}