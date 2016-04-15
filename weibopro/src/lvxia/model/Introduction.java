package lvxia.model;

public class Introduction {

	private String id;
	private String introduction;
	private String profession;
	
	public Introduction(String id,String introduction,String profession){
		this.id = id;
		this.introduction = introduction;
		this.profession = profession;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}
	
	
}
