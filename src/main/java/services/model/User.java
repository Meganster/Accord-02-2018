package services.model;


@SuppressWarnings("unused")
public class User {
    private long id;
    private String email;
    private String password;
    private String nickname;
    private Integer rating;
    private String avatar;

    public User(String nickname, String email, String password, Integer rating) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.rating = rating;
    }

    public User(User user) {
        this.id = user.id;
        this.nickname = user.nickname;
        this.password = user.password;
        this.email = user.email;
        this.rating = user.rating;
        this.setDefaultAvatar();
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Integer getRating() {
        return rating;
    }

    public String getAvatar() {
        return avatar;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDefaultAvatar() {
        this.avatar = "default.jpg";
    }
}
