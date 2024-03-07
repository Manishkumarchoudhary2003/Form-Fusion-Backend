package Manish.FormFusion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    //    @JsonIgnore
    private String password;

    private Long contact;

    private String country;

    private boolean verified;

    private String otp;



    @Column(unique = true)
    @Email(regexp = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*",flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    private String role;
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Form> forms;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;


    public User() {

    }

    public User(Long userId, String username, String password, Long contact, String country, boolean verified, String otp, String email, String role, List<Form> forms, List<Response> responses, List<Answer> answers) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.contact = contact;
        this.country = country;
        this.verified = verified;
        this.otp = otp;
        this.email = email;
        this.role = role;
        this.forms = forms;
        this.responses = responses;
        this.answers = answers;
    }

//    public User(String username, String password, String email, Long contact, String country, String role, List<Form> forms, List<Response> responses, List<Answer> answers) {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.role = role;
//        this.forms = forms;
//        this.responses = responses;
//        this.answers = answers;
//        this.contact = contact;
//        this.country = country;
//    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    //    @Override
//    public String toString() {
//        return "User{" +
//                "userId=" + userId +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                ", email='" + email + '\'' +
//                ", role='" + role + '\'' +
////                ", forms=" + forms +
////                ", responses=" + responses +
//                '}';
//    }
    @Override
    public String toString() {
        return "{" +
                "\"userId\": \"" + userId + "\"," +
                "\"username\": \"" + username + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"role\": \"" + role + "\"," +
                "\"contact\": \"" + contact + "\"," +
                "\"country\": \"" + country + "\"" +
                "}";
    }


}
