package lvxia.model;

public class Tags {

	private String id;
	private String tags;
	
	public Tags(String id,String tags){
		this.id = id;
		this.tags = tags;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
}
