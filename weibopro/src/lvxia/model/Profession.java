package lvxia.model;

public class Profession {

	private String uid;
	private String name;
	private String profession1;
	private String profession2;
	private String profession3;
	
	public Profession(String uid,String name,String profession1,String profession2,String profession3){
		this.uid = uid;
		this.name = uid;
		this.profession1 = profession1;
		this.profession2 = profession2;
		this.profession3 = profession3;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfession1() {
		return profession1;
	}

	public void setProfession1(String profession1) {
		this.profession1 = profession1;
	}

	public String getProfession2() {
		return profession2;
	}

	public void setProfession2(String profession2) {
		this.profession2 = profession2;
	}

	public String getProfession3() {
		return profession3;
	}

	public void setProfession3(String profession3) {
		this.profession3 = profession3;
	}
	
}
