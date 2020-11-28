package quartz.model;

public class Post {
    private final String name;
    private final String link;
    private final String created;
    private final String postText;

    public Post(PostTopic topic, PostDate date) {
        this.name = topic.name();
        this.link = topic.href();
        this.postText = topic.postText();
        this.created = date.formatDate();
    }

    public String name() {
        return this.name;
    }

    public String link() {
        return this.link;
    }

    public String created() {
        return this.created;
    }

    public String postText() {
        return this.postText;
    }

    @Override
    public String toString() {
        return "Post{"
                + "name='" + name + '\''
                + ", link='" + link + '\''
                + ", created='" + created + '\''
                + ", postText='" + postText + '\''
                + '}';
    }
}
