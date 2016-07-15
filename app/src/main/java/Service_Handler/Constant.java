package Service_Handler;

public interface Constant {
    //String Server = "http://stage.turnstr.net/";
     String Server = "http://turnstr.net/";
    String Login = Server + "api/login";
    String Register = Server + "api/register";
    String Fetch_posts = Server + "api/posts";
    String forgotpassword = Server + "api/forgotpassword";
    String Logout = Server + "api/logout";
    String Explorer = Server + "api/explorer";
    String upload_image = Server + "api/posts/upload";
    String MyProfile = Server + "api/myProfile";
    String Activity_List = Server + "api/activityList";
    String Profile_Posts = Server + "api/profilePosts";
    String Update_profile = Server + "api/updateProfile";
    String Comments_List = Server + "api/getComments";
    String Post_comments = Server + "api/comments";
    String LikePost = Server + "api/likePost";
    String ProfileImage_Upload = Server + "api/profileImageUpload";
    String Follow_unfollow = Server + "api/follow";
    String OtherUser = Server + "api/otheruser";
    String Post_Delete = Server + "api/posts/deletePost";
    String Fetch_posts_homepage = Server + "api/homePage";
    String report_inappropriate = Server + "api/markInappropriate";
    String Followers_me = Server + "api/followersList";
    String Followings_me = Server + "api/me/followings";
//    String Followings_user = Server + "api/me/followings";
//    String Followers_user = Server + "api/me/followings";
    String Fb_login = Server + "api/login/facebook";


}