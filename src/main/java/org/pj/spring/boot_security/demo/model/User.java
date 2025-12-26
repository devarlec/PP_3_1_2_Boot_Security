package org.pj.spring.boot_security.demo.model;


import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long id;

   @Column(name = "name")
   private String name;

   @Column(name = "last_name")
   private String lastName;

   @Column(name="age")
   private int age;

   @Column(name = "email", unique = true, length = 100)
   private String email;

   @Column(name = "password")
   private String password;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "users_role",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id"))
   private Set<Role> roles = new HashSet<>();

   public User() {
   }

   public User(String name, String lastName, int age, String email, String password) {
      this.name = name;
      this.lastName = lastName;
      this.age = age;
      this.email = email;
      this.password = password;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return getEmail();
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return getRoles();
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }

   public UserDetails fromUser(User user) {
      return new org.springframework.security.core.userdetails.User
              (user.getEmail(), user.getPassword(), user.getRoles());
   }


   @Override
   public final boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
      Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
      if (thisEffectiveClass != oEffectiveClass) return false;
      User user = (User) o;
      return getId() != null && Objects.equals(getId(), user.getId());
   }

   @Override
   public final int hashCode() {
      return this instanceof HibernateProxy
              ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
              : getClass().hashCode();
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", firstName='" + name + '\'' +
              ", lastName='" + lastName + '\'' +
              ", age='" + age + '\'' +
              ", email=" + email +
              ", password=" + password +
              ", roles=" + roles +
              '}';
   }
}