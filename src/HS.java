import java.sql.ResultSet;

public class HS {
	private String HSID;
	private Profile p;
	private PFORHS[] ppatients;
	
	public HS(String ID) {
		this.HSID = ID;
		p = new Profile(ID);
		fetchPatients();
	}
	
	public void MainView() {
		int option = 0;
		while (option != 3) {
			System.out.println("Select from the following options:\n"
					+ "1. Profile\n"
					+ "2. Manage Patients\n"
					+ "3. Logout");
			option = StaticFunctions.nextInt();
			switch(option) {
			case 1:
				p.MainView();
				break;
			case 2:
				ManagePatients();
				break;
			case 3:
				break;
			default:
				System.out.println("Invalid Selection");
					
			}
		}
	}
	
	private void ManagePatients() {
		fetchPatients();
		if (ppatients == null) {
			System.out.println("You do not have patients to manage");
		}
		else {
			int option = 0;
			while(option != (ppatients.length+1)) {
				
				System.out.println("Select from the following patients:");
				for(int i = 0;i < ppatients.length; i++) {
					System.out.println((i+1)+". "+ppatients[i].getName());
				}
				System.out.println((ppatients.length+1)+". Go Back");
				
				option = StaticFunctions.nextInt();
				if (option < 1 || option > (ppatients.length+1)) {
					System.out.println("Invalid Selection");
				}
				else if (option != (ppatients.length+1)){
					ppatients[option-1].MainView();
				}
			}
		}
	}
	
	public void fetchPatients() {
		String query1 = "(SELECT PH."+HealthSupporters.pcolpatientid+", PH."+HealthSupporters.pcolsince
				+" FROM "+HealthSupporters.primaryTableName+" PH WHERE PH."+HealthSupporters.pcolhsid
				+"='"+HSID+"')";
		String query2 = "(SELECT SH."+HealthSupporters.scolpatientid+", SH."+HealthSupporters.scolsince
				+" FROM "+HealthSupporters.secTableName+" SH WHERE SH."+HealthSupporters.scolhsid+"='"
				+HSID+"')";
		
		String query = query1 + " UNION " + query2;
		ResultSet res = DatabaseConnector.runQuery(query);
		
		try {
			if (res.next()) {
				res.last();
				int l = res.getRow();
				ppatients = new PFORHS[l];
				res.beforeFirst();
				int i = 0;
				while(res.next()) {
					ppatients[i] = new PFORHS(res.getString(1), res.getDate(2));
					i++;
				}
			}
			else {
				
			}
		} catch (Exception e) {
			System.out.println("Unable to fetch patient information for HS");
		}
	}
}
