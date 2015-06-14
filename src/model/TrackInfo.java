package model;

public class TrackInfo {
	private final String title, creator, album, content;

	public TrackInfo(String title, String creator, String album, String content) {
		this.title = title;
		this.creator = creator;
		this.album = album;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public String getCreator() {
		return creator;
	}

	public String getAlbum() {
		return album;
	}

	public String getContent() {
		return content;
	}
	
}
