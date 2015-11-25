package prj.resources.mgmt.domain;

public enum ParameterType {
	
		Personal(1),
		Professional(2);
		
		private final int type;
		
		private ParameterType(int _type) {
			this.type = _type;
		}

		public static ParameterType valueOf(int int1) {
			switch(int1) {
				case 1: return ParameterType.Personal;
				case 2: return ParameterType.Professional;
			}
			return ParameterType.Personal;
		}
		
		public String toString() {
			switch(this.type) {
			case 1: return "1";
			case 2: return "2";
			}
			return "1";			
		}


}
