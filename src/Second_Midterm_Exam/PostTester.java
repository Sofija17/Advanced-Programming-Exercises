package Second_Midterm_Exam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Comment{
    private String commentId;
    private int likes;
    private String content;
    private String username;
    private String replyId;

    public Comment(String username,String commentId,  String content, String replyId) {
        this.username = username;
        this.commentId = commentId;
        this.content = content;
        this.replyId = replyId;
        this.likes = 0;
    }

    public void addLike(){
        this.likes++;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public String getReplyId() {
        return replyId;
    }

    public int getLikes() {
        return likes;
    }
    public String indent(int num){
        StringBuilder sb  = new StringBuilder();
        for (int i=0; i<num; i++){
            sb.append(" ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
       return String.format("%sComment: %s\n %sWritten by user: %s\n %sLikes: %d\n",
               indent(8),content,indent(8),username,indent(8),likes);
    }
}

class Post{
    private String username;
    private String postContent;
    private Map<String, List<Comment>> commentsByUser;
    private List<Comment> comments;

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        this.commentsByUser = new HashMap<>();
        this.comments = new ArrayList<>();
    }

    void addComment (String username, String commentId, String content, String replyToId){
        Comment comment = new Comment(username,commentId,content, replyToId);
        comments.add(comment);

        commentsByUser.putIfAbsent(username, new ArrayList<>());
        commentsByUser.get(username).add(comment);
    }
    void likeComment (String commentId){
        Comment comment = comments.stream().filter(c -> c.getCommentId().equals(commentId)).findFirst().get();
        comment.addLike();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Post: " + postContent).append("\n");
        sb.append("Written by: " + username).append("\n");

        sb.append("Comments: \n");
        comments.stream()
                .sorted(Comparator.comparing(Comment::getLikes).reversed())
                .forEach(sb::append);
        return sb.toString();
    }
}

public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}

