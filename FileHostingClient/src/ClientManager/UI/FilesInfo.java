package ClientManager.UI;

public class FilesInfo {

    private String name;
    private String comment;
    private String size;
    private String filePath;
    private String isDownloaded;
    private String id;


    public FilesInfo(String id, String name, String comment, String size, String filePath, String isDownloaded){
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.size = size;
        this.filePath = filePath;
        this.isDownloaded = isDownloaded;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        this.isDownloaded = isDownloaded;
    }





}
