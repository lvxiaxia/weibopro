package lvxia.model;

public class VerifiedText {

	private String id;
	private String verifiedText;
	private String profession;
	
	public VerifiedText(String id,String verifiedText,String profession){
		this.id = id;
		this.verifiedText = verifiedText;
		this.profession = profession;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVerifiedText() {
		return verifiedText;
	}
	public void setVerifiedText(String verifiedText) {
		this.verifiedText = verifiedText;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	
	
}
